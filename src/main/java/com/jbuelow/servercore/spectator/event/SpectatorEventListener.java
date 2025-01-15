package com.jbuelow.servercore.spectator.event;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.spectator.SpectatorManager;
import com.jbuelow.servercore.spectator.SpectatorModule;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class SpectatorEventListener implements Listener {
    private final SpectatorManager manager;

    private final boolean preventVoidDamage;

    private final double maxDistance;
    private final double maxDistanceSquared;

    public SpectatorEventListener(SpectatorModule module) {
        manager = module.getManager();
        YamlDocument config = ServerCore.get().getConfiguration();

        preventVoidDamage = config.getBoolean("spectator.prevent-void-damage", true);
        maxDistance = config.getDouble("spectator.max-distance", Double.POSITIVE_INFINITY);
        maxDistanceSquared = maxDistance * maxDistance;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!preventVoidDamage) {
            event.getHandlers().unregister(this);
            return;
        }

        // Prevent players in spectator mode from taking void damage
        if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (manager.isPlayerSpectating(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (maxDistance == Double.POSITIVE_INFINITY || maxDistance < 0.0) {
            event.getHandlers().unregister(this);
            return;
        }

        if (!manager.isPlayerSpectating(event.getPlayer())) {
            return;
        }

        // Prevent players in spectator mode from moving too far away from their non-spectator position
        Location returnLocation = manager.getReturnLocation(event.getPlayer());
        double distance = event.getTo().distanceSquared(returnLocation);
        if (distance >= maxDistanceSquared) {
            Location location = event.getTo();
            Vector correctedLocation = location.toVector().subtract(returnLocation.toVector())
                    .normalize().multiply(maxDistance).add(returnLocation.toVector());
            location.setX(correctedLocation.getX());
            location.setY(correctedLocation.getY());
            location.setZ(correctedLocation.getZ());
            event.setTo(location);
        }
    }
}
