package com.jbuelow.servercore.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class ItemStackMaterialPropertyOverride {
    protected Material targetMaterial;

    public ItemStackMaterialPropertyOverride(Material targetMaterial) {
        this.targetMaterial = targetMaterial;
    }

    public Material getType() {
        return targetMaterial;
    }

    public abstract void apply(ItemStack itemStack);
}
