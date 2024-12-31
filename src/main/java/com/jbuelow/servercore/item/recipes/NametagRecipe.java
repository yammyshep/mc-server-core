package com.jbuelow.servercore.item.recipes;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.item.ItemRecipe;
import com.jbuelow.servercore.item.RegisterCustomRecipes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.List;

@RegisterCustomRecipes
public final class NametagRecipe implements ItemRecipe {
    @Override
    public List<Recipe> getRecipes() {
        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(ServerCore.get(), "nametag_recipe"),
                new ItemStack(Material.NAME_TAG));
        recipe.addIngredient(Material.LEAD);
        recipe.addIngredient(Material.WRITABLE_BOOK);

        return List.of(recipe);
    }
}
