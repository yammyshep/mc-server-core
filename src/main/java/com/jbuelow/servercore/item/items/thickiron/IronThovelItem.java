package com.jbuelow.servercore.item.items.thickiron;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.item.ItemRecipe;
import com.jbuelow.servercore.item.RegisterCustomItem;
import com.jbuelow.servercore.item.RegisterCustomRecipes;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

@RegisterCustomItem(key = "iron_thovel")
@RegisterCustomRecipes
public class IronThovelItem extends ThickIronToolItem implements ItemRecipe {
    public IronThovelItem() {
        this(1);
    }

    public IronThovelItem(final int amount) {
        this(1, (short) 0);
    }

    public IronThovelItem(final int amount, final short damage) {
        super(Material.IRON_SHOVEL, amount, damage);
    }

    @Override
    public String getInternalItemKey() {
        return "iron_thovel";
    }

    @Override
    public String getName() {
        return "Iron Thovel";
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(ServerCore.get(), "iron_thovel_recipe"), this);
        recipe.shape(
                " I ",
                " L ",
                " L "
        );

        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('L', new RecipeChoice.MaterialChoice(Tag.LOGS));

        return List.of(recipe);
    }
}
