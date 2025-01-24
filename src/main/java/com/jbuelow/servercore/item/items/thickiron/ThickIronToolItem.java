package com.jbuelow.servercore.item.items.thickiron;

import com.jbuelow.servercore.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class ThickIronToolItem extends CustomItem {
    public ThickIronToolItem(Material material, final int amount, final short damage) {
        super(material, amount, damage);
    }

    @Override
    public boolean hasCustomItemModel() {
        return true;
    }

    @Override
    public void applyCustomItemMeta(ItemMeta meta) {
        super.applyCustomItemMeta(meta);

        meta.setUnbreakable(true);
    }
}
