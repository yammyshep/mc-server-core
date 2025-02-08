package com.jbuelow.servercore.item.event;

import com.jbuelow.servercore.item.CustomItemsModule;
import com.jbuelow.servercore.item.ItemOverrideRegistry;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.EntitiesLoadEvent;

public class ItemOverridesEventListener implements Listener {
    private final ItemOverrideRegistry registry;

    public ItemOverridesEventListener(CustomItemsModule module) {
        registry = module.getItemOverrideRegistry();
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event) {
        registry.applyOverrides(event.getPlayer().getInventory());
    }

    @EventHandler(ignoreCancelled = true)
    private void onInventoryOpen(InventoryOpenEvent event) {
        registry.applyOverrides(event.getInventory());
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntityLoad(EntitiesLoadEvent event) {
        for (Entity e : event.getEntities()) {
            if (e instanceof Item item) {
                registry.applyOverrides(item.getItemStack());
            } else if (e instanceof ItemFrame itemFrame) {
                registry.applyOverrides(itemFrame.getItem());
            } else if (e instanceof LivingEntity livingEntity) {
                registry.applyOverrides(livingEntity.getEquipment());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onItemSpawn(ItemSpawnEvent event) {
        registry.applyOverrides(event.getEntity().getItemStack());
    }

    @EventHandler(ignoreCancelled = true)
    private void onInventoryClick(InventoryClickEvent event) {
        registry.applyOverrides(event.getCurrentItem());
        registry.applyOverrides(event.getCursor());
    }
}
