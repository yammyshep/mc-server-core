package com.jbuelow.servercore.item;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class CustomItemEnchantmentEventListener implements Listener {
    private final CustomItemsModule module;

    public CustomItemEnchantmentEventListener(CustomItemsModule itemsModule) {
        this.module = itemsModule;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPrepareItemEnchant(PrepareItemEnchantEvent event) {
        Optional<CustomItem> customItem = module.getItem(event.getItem());
        if (customItem.isEmpty()) {
            return;
        }

        if (customItem.get().preventEnchanting()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack anvilResult = event.getResult();
        if (anvilResult == null) {
            return;
        }

        Optional<CustomItem> customItem = module.getItem(anvilResult);
        if (customItem.isEmpty()) {
            return;
        }

        if (customItem.get().preventEnchanting()) {
            if (anvilResult.getEnchantments().hashCode() != customItem.get().getEnchantments().hashCode()) {
                event.setResult(null);
            }
        }
    }
}
