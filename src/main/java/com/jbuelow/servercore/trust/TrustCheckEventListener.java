package com.jbuelow.servercore.trust;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.trust.service.UserTrustService;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.Map;

public class TrustCheckEventListener implements Listener {

    private static final long WARNING_INTERVAL = 10000;

    private final UserTrustService trust;

    public TrustCheckEventListener() {
        TrustModule module = ServerCore.get().getModule();
        trust = module.getTrustService();
    }

    private final Map<Player, Long> lastWarningTimes = new HashMap<>();

    private void handleUntrustedAction(Player player, Event event) {
        long lastWarn = lastWarningTimes.getOrDefault(player, 0L);
        if (System.currentTimeMillis() - WARNING_INTERVAL >= lastWarn)
        {
            warnUntrustedPlayer(player);
            lastWarningTimes.put(player, System.currentTimeMillis());
        }
    }

    private void warnUntrustedPlayer(Player player) {
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1.5f, 1.f);
        player.sendTitle("You can't do that yet!", "Ask another player to type /trust " + player.getDisplayName(), 10, 100, 10);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (trust.isTrusted(event.getPlayer())) {
            return;
        }

        event.setCancelled(true);
        handleUntrustedAction(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (trust.isTrusted(event.getPlayer())) {
            return;
        }

        event.setCancelled(true);
        handleUntrustedAction(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (trust.isTrusted(event.getPlayer())) {
            return;
        }

        event.setCancelled(true);
        handleUntrustedAction(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (trust.isTrusted(event.getPlayer())) {
            return;
        }

        event.setCancelled(true);
        handleUntrustedAction(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        if (trust.isTrusted(player)) {
            return;
        }

        event.setCancelled(true);
        handleUntrustedAction(player, event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        if (trust.isTrusted(player)) {
            return;
        }

        // Allow player to pickup item stacks if the item was thrown by that player
        if (player.getUniqueId() == event.getItem().getThrower()) {
            return;
        }

        event.setCancelled(true);
        handleUntrustedAction(player, event);
    }
}
