package com.jbuelow.servercore.item;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BundleMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemOverrideRegistry {
    private final Map<Material, List<ItemStackMaterialPropertyOverride>> overrides = new HashMap<>();

    public boolean materialHasOverrides(Material material) {
        return overrides.containsKey(material) && !overrides.get(material).isEmpty();
    }

    public void addOverride(ItemStackMaterialPropertyOverride override) {
        overrides.putIfAbsent(override.getType(), new ArrayList<>());
        overrides.get(override.getType()).add(override);
    }

    public void removeOverride(ItemStackMaterialPropertyOverride override) {
        for (List<ItemStackMaterialPropertyOverride> matOverrides : overrides.values()) {
            matOverrides.remove(override);
        }
    }

    public void clearOverrides(Material material) {
        overrides.remove(material);
    }

    public void applyOverrides(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        for (ItemStackMaterialPropertyOverride override : overrides.getOrDefault(itemStack.getType(), List.of())) {
            override.apply(itemStack);
        }

        if (itemStack.getItemMeta() instanceof BlockStateMeta blockMeta) {
            if (blockMeta.getBlockState() instanceof ShulkerBox shulker) {
                applyOverrides(shulker.getInventory());
            }
        }

        if (itemStack.getItemMeta() instanceof BundleMeta bundleMeta) {
            for (ItemStack bundleItem : bundleMeta.getItems()) {
                applyOverrides(bundleItem);
            }
        }
    }

    public void applyOverrides(Inventory inventory) {
        for (ItemStack i : inventory.getContents()) {
            applyOverrides(i);
        }
    }

    public void applyOverrides(EntityEquipment entityEquipment) {
        applyOverrides(entityEquipment.getHelmet());
        applyOverrides(entityEquipment.getChestplate());
        applyOverrides(entityEquipment.getLeggings());
        applyOverrides(entityEquipment.getBoots());
        applyOverrides(entityEquipment.getItemInMainHand());
        applyOverrides(entityEquipment.getItemInOffHand());
    }
}
