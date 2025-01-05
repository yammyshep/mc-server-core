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

@RegisterCustomItem(key = "berry_pie")
@RegisterCustomRecipes
public class BerryPieItem extends CustomItem implements ItemRecipe {
    public BerryPieItem() {
        this(1);
    }

    public BerryPieItem(final int amount) {
        this(amount, (short) 0);
    }

    public BerryPieItem(final int amount, final short damage) {
        super(Material.PUMPKIN_PIE, amount,  damage);

        //TODO: Adjust food stats
    }

    @Override
    public String getInternalItemKey() {
        return "berry_pie";
    }

    @Override
    public String getName() {
        return "Berry Pie";
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(ServerCore.get(), "berry_pie_recipe"), this);
        recipe.shape(
                "BS",
                "E "
        );

        recipe.setIngredient('B', Material.SWEET_BERRIES);
        recipe.setIngredient('S', Material.SUGAR);
        recipe.setIngredient('E', Material.EGG);

        return List.of(recipe);
    }

    @Override
    public boolean hasCustomItemModel() {
        return true;
    }
}
