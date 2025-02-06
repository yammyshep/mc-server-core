package com.jbuelow.servercore.carry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CarryEventListener implements Listener {
    private final CarryModule module;

    public CarryEventListener(CarryModule module) {
        this.module = module;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player clickedPlayer)) {
            return;
        }

        ItemStack heldItem = event.getPlayer().getInventory().getItemInMainHand();
        if (!heldItem.getType().isAir()) {
            return;
        }

        ItemStack headItem = clickedPlayer.getInventory().getItem(EquipmentSlot.HEAD);
        if (headItem != null && headItem.getType() == Material.SADDLE) {
            module.mountPlayers(event.getPlayer(), clickedPlayer, event.getPlayer());
            event.setCancelled(true);
        }
    }
}
