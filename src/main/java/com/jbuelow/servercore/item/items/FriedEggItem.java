package com.jbuelow.servercore.item.items;

import com.jbuelow.servercore.item.CustomItem;
import com.jbuelow.servercore.item.ItemRecipe;
import com.jbuelow.servercore.item.RegisterCustomItem;
import com.jbuelow.servercore.item.RegisterCustomRecipes;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import java.util.List;

@RegisterCustomItem(key = "fried_egg")
@RegisterCustomRecipes
public class FriedEggItem extends CustomItem implements ItemRecipe {
    public FriedEggItem() {
        this(1);
    }

    public FriedEggItem(final int amount) {
        this(amount, (short) 0);
    }

    public FriedEggItem(final int amount, final short damage) {
        //TODO: Update Spigot api to utilize components to change nutrition and saturation values
        // low nutrition, high saturation
        // https://hub.spigotmc.org/jira/browse/SPIGOT-7984
        super(Material.BAKED_POTATO, amount, damage);
    }

    @Override
    public String getInternalItemKey() {
        return "fried_egg";
    }

    @Override
    public String getName() {
        return "Fried Egg";
    }

    @Override
    public List<Recipe> getRecipes() {
        return List.of();
    }
}
