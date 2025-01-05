package com.jbuelow.servercore.balloon;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.item.ItemRecipe;
import com.jbuelow.servercore.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

public class BalloonItem extends CustomItem implements ItemRecipe {
    public BalloonItem() {
        this(1);
    }

    public BalloonItem(final int amount) {
        this(amount, (short) 0);
    }

    public BalloonItem(final int amount, final short damage) {
        super(Material.PUFFERFISH, amount, damage);
    }

    @Override
    public String getInternalItemKey() {
        return "balloon";
    }

    @Override
    public String getName() {
        return "Pufferfish Balloon";
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(ServerCore.get(), "balloon_recipe"), this);
        recipe.shape(
                " P ",
                " S ",
                " S ");

        recipe.setIngredient('P', Material.PUFFERFISH);
        recipe.setIngredient('S', Material.STRING);

        return List.of(recipe);
    }

    @Override
    public boolean hasCustomItemModel() {
        return true;
    }
}
