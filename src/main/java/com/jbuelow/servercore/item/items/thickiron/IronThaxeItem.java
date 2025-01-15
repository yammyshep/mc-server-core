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

@RegisterCustomItem(key = "iron_thaxe")
@RegisterCustomRecipes
public class IronThaxeItem extends ThickIronToolItem implements ItemRecipe {
    public IronThaxeItem() {
        this(1);
    }

    public IronThaxeItem(final int amount) {
        this(1, (short) 0);
    }

    public IronThaxeItem(final int amount, final short damage) {
        super(Material.IRON_AXE, amount, damage);
    }

    @Override
    public String getInternalItemKey() {
        return "iron_thaxe";
    }

    @Override
    public String getName() {
        return "Iron Thaxe";
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(ServerCore.get(), "iron_thaxe_recipe"), this);
        recipe.shape(
                "II ",
                "IL ",
                " L "
        );

        recipe.setIngredient('I', Material.IRON_BLOCK);
        recipe.setIngredient('L', new RecipeChoice.MaterialChoice(Tag.LOGS));

        return List.of(recipe);
    }

    @Override
    public int getCustomModelData() {
        return 2504039;
    }
}
