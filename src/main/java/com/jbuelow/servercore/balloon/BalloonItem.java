package com.jbuelow.servercore.balloon;

import com.jbuelow.servercore.ServerCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BalloonItem {

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.PUFFERFISH);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Pufferfish Balloon");

        NamespacedKey balloonKey = new NamespacedKey(ServerCore.get(), "is_balloon");
        meta.getPersistentDataContainer().set(balloonKey, PersistentDataType.BOOLEAN, true);

        item.setItemMeta(meta);
        return item;
    }

    public ShapedRecipe getRecipe() {
        ItemStack item = getItemStack();
        NamespacedKey key = new NamespacedKey(ServerCore.get(), "pufferfish_balloon");

        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape(
                " P ",
                " S ",
                " S ");
        recipe.setIngredient('P', Material.PUFFERFISH);
        recipe.setIngredient('S', Material.STRING);

        return recipe;
    }

    public boolean isItem(ItemStack itemStack) {
        ItemStack thisItem = getItemStack();
        if (!thisItem.isSimilar(itemStack))
            return false;

        PersistentDataContainer data = itemStack.getItemMeta().getPersistentDataContainer();
        if (data.has(new NamespacedKey(ServerCore.get(), "is_balloon"), PersistentDataType.BOOLEAN)) {
            return data.get(new NamespacedKey(ServerCore.get(), "is_balloon"), PersistentDataType.BOOLEAN);
        }

        return false;
    }
}
