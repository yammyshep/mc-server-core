package com.jbuelow.servercore.item.recipes;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.item.ItemRecipe;
import com.jbuelow.servercore.item.RegisterCustomRecipes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

@RegisterCustomRecipes
public final class NametagRecipe implements ItemRecipe {
    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(ServerCore.get(), "nametag_recipe"),
                new ItemStack(Material.NAME_TAG));
        recipe.shape(
                "  E",
                " L ",
                "P  "
        );

        recipe.setIngredient('E', Material.LEATHER);
        recipe.setIngredient('L', Material.LEAD);
        recipe.setIngredient('P', Material.PAPER);

        return List.of(recipe);
    }
}
