package com.jbuelow.servercore.item.items;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.item.CustomItem;
import com.jbuelow.servercore.item.ItemRecipe;
import com.jbuelow.servercore.item.RegisterCustomItem;
import com.jbuelow.servercore.item.RegisterCustomRecipes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

@RegisterCustomItem(key = "apple_pie")
@RegisterCustomRecipes
public class ApplePieItem extends CustomItem implements ItemRecipe {
    public ApplePieItem() {
        this(1);
    }

    public ApplePieItem(final int amount) {
        this(amount, (short) 0);
    }

    public ApplePieItem(final int amount, final short damage) {
        super(Material.PUMPKIN_PIE, amount,  damage);

        //TODO: Adjust food stats
    }

    @Override
    public String getInternalItemKey() {
        return "apple_pie";
    }

    @Override
    public String getName() {
        return "Apple Pie";
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(ServerCore.get(), "apple_pie_recipe"), this);
        recipe.shape(
                "AS",
                "E "
        );

        recipe.setIngredient('A', Material.APPLE);
        recipe.setIngredient('S', Material.SUGAR);
        recipe.setIngredient('E', Material.EGG);

        return List.of(recipe);
    }

    @Override
    public int getCustomModelData() {
        return 3068330;
    }
}
