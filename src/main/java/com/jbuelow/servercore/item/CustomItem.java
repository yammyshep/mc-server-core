package com.jbuelow.servercore.item;

import com.jbuelow.servercore.ServerCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class CustomItem extends ItemStack {
    protected static final NamespacedKey customItemKey = new NamespacedKey(ServerCore.get(), "custom_item_key");

    public CustomItem(@NotNull final Material type) {
        this(type, 1);
    }

    public CustomItem(@NotNull final Material type, final int amount) {
        this(type, amount, (short) 0);
    }

    public CustomItem(@NotNull final Material type, final int amount, final short damage) {
        super(type, amount, damage);

        ItemMeta meta = getItemMeta();
        assert meta != null;

        meta.setDisplayName(ChatColor.RESET + getName());
        meta.setCustomModelData(getCustomModelData());
        meta.getPersistentDataContainer().set(customItemKey, PersistentDataType.STRING, getInternalItemKey());

        setItemMeta(meta);
    }

    public boolean isItem(ItemStack itemStack) {
        if (!isSimilar(itemStack)) return false;

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (!data.has(customItemKey, PersistentDataType.STRING)) {
            return false;
        }

        return Objects.equals(data.get(customItemKey, PersistentDataType.STRING), getInternalItemKey());
    }

    public abstract String getInternalItemKey();

    public NamespacedKey getItemKey() {
        return new NamespacedKey(ServerCore.get(), getInternalItemKey());
    }

    public abstract String getName();

    public int getCustomModelData() {
        return 0;
    }
}
