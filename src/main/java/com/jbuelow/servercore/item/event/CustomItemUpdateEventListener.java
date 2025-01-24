package com.jbuelow.servercore.item.event;

import com.jbuelow.servercore.item.CustomItem;
import com.jbuelow.servercore.item.CustomItemsModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class CustomItemUpdateEventListener implements Listener {
    public final CustomItemsModule module;

    public CustomItemUpdateEventListener(CustomItemsModule module) {
        this.module = module;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event) {
        upgradeInventory(event.getPlayer().getInventory());
    }

    @EventHandler(ignoreCancelled = true)
    private void onInventoryOpen(InventoryOpenEvent event) {
        upgradeInventory(event.getInventory());
    }

    private void upgradeInventory(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack == null || itemStack.isEmpty() || !module.isCustomItem(itemStack)) {
                continue;
            }

            upgradeItem(itemStack);
        }
    }

    private void upgradeItem(ItemStack itemStack) {
        Optional<CustomItem> itemOpt = module.getCustomItem(itemStack);
        itemOpt.ifPresent(customItem -> itemStack.editMeta(customItem::applyCustomItemMeta));
    }
}
