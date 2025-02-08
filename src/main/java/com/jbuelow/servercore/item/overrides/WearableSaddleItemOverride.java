package com.jbuelow.servercore.item.overrides;

import com.jbuelow.servercore.item.ItemStackMaterialPropertyOverride;
import com.jbuelow.servercore.util.CompatUtils;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

@SuppressWarnings("UnstableApiUsage")
public final class WearableSaddleItemOverride extends ItemStackMaterialPropertyOverride {
    public WearableSaddleItemOverride() {
        super(Material.SADDLE);
    }

    @Override
    public void apply(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        if (CompatUtils.serverSupportsDataComponents()) {
            EquippableComponent equip = meta.getEquippable();
            equip.setSlot(EquipmentSlot.HEAD);
            meta.setEquippable(equip);
        }

        itemStack.setItemMeta(meta);
    }
}
