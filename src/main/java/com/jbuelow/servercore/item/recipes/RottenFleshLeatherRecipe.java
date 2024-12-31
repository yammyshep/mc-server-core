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
public final class RottenFleshLeatherRecipe implements ItemRecipe {
    @Override
    public List<Recipe> getRecipes() {
        return List.of(new ShapelessRecipe(
            new NamespacedKey(ServerCore.get(), "rottenflesh_leather_recipe"), new ItemStack(Material.LEATHER))
            .addIngredient(9, Material.ROTTEN_FLESH)
        );
    }
}
