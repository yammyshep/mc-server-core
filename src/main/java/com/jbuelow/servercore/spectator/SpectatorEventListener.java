package com.jbuelow.servercore.spectator;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class SpectatorEventListener implements Listener {
    private final SpectatorModule module;
    private final SpectatorManager manager;
    private double maxDistance = 100;
    private double maxDistanceSquared = maxDistance * maxDistance; //TODO: Make configuration value

    public SpectatorEventListener(SpectatorModule module) {
        this.module = module;
        manager = module.getManager();
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
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
        if (!manager.isPlayerSpectating(event.getPlayer())) {
            return;
        }

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
