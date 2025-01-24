package com.jbuelow.servercore.item.event;

import com.jbuelow.servercore.item.CustomItem;
import com.jbuelow.servercore.item.CustomItemsModule;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;

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
            if (itemStack == null || itemStack.isEmpty()) {
                continue;
            }

            if (module.isCustomItem(itemStack)) {
                upgradeItem(itemStack);
            }

            // Recurse into shulker boxes
            if (itemStack.getItemMeta() instanceof BlockStateMeta blockMeta) {
                if (blockMeta.getBlockState() instanceof ShulkerBox shulker) {
                    upgradeInventory(shulker.getInventory());
                }
            }

            // Recurse into bundles
            if (itemStack.getItemMeta() instanceof BundleMeta bundleMeta) {
                for (ItemStack bundleItem : bundleMeta.getItems()) {
                    if (module.isCustomItem(bundleItem)) {
                        upgradeItem(bundleItem);
                    }
                }
            }
        }
    }

    private void upgradeItem(ItemStack itemStack) {
        Optional<CustomItem> itemOpt = module.getCustomItem(itemStack);
        itemOpt.ifPresent(customItem -> itemStack.editMeta(customItem::applyCustomItemMeta));
    }
}
