package com.jbuelow.servercore.balloon;

import com.jbuelow.servercore.ServerCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BalloonEventListener implements Listener {
    private static final BalloonItem balloonItem = new BalloonItem();

    private boolean isBalloon(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        return balloonItem.isItem(itemStack);
    }

    private void updatePlayerBalloonState(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (isBalloon(mainHand) || isBalloon(offHand)) {
            startBalloon(player);
        } else {
            endBalloon(player);
        }
    }

    //TODO: Don't rely on potion effects! Creates ability to remove the LEVITATION potion effect using the balloon item!
    // also grants the "Great view from up here" challange prematurely
    private void startBalloon(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, -1, 1, true));
    }

    private void endBalloon(Player player) {
        player.removePotionEffect(PotionEffectType.LEVITATION);
    }

    // Called when the player changes what item is selected in the hotbar
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Inventory inv = event.getPlayer().getInventory();

        if (isBalloon(inv.getItem(event.getPreviousSlot()))) {
            endBalloon(event.getPlayer());
        }

        if (isBalloon(inv.getItem(event.getNewSlot()))) {
            startBalloon(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayerBalloonState(event.getPlayer());
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            Bukkit.getScheduler().runTaskLater(ServerCore.get(), () -> {
                updatePlayerBalloonState(player);
            }, 1L);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        updatePlayerBalloonState(event.getPlayer());
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (isBalloon(event.getMainHandItem()) || isBalloon(event.getOffHandItem())) {
            startBalloon(event.getPlayer());
        } else {
            endBalloon(event.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Bukkit.getScheduler().runTaskLater(ServerCore.get(), () -> {
                updatePlayerBalloonState((Player) event.getWhoClicked());
            }, 1L);
        }
    }
}
