package com.jbuelow.servercore.item.items;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.item.CustomItem;
import com.jbuelow.servercore.item.ItemRecipe;
import com.jbuelow.servercore.item.RegisterCustomItem;
import com.jbuelow.servercore.item.RegisterCustomRecipes;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.List;

@RegisterCustomItem(key = "shrink_potion")
@RegisterCustomRecipes
public class ShrinkPotionItem extends CustomItem implements Listener, ItemRecipe {
    public ShrinkPotionItem() {
        this(1);
    }

    public ShrinkPotionItem(final int amount) {
        this(amount, (short) 0);
    }

    public ShrinkPotionItem(final int amount, final short damage) {
        super(Material.POTION, amount, damage);
    }

    @Override
    public String getInternalItemKey() {
        return "shrink_potion";
    }

    @Override
    public String getName() {
        return "Potion of Shrinkening";
    }

    @Override
    public List<Recipe> getRecipes() {
        ShapelessRecipe recipe = new ShapelessRecipe(new NamespacedKey(ServerCore.get(), "shrink_potion_recipe"), this);
        recipe.addIngredient(4, Material.APPLE); //TODO: Needs a real brewing recipe
        return List.of(recipe);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (!isItem(event.getItem())) {
            return;
        }

        AttributeInstance scaleAttribute = event.getPlayer().getAttribute(Attribute.SCALE);
        scaleAttribute.setBaseValue(Math.max(scaleAttribute.getDefaultValue() * 0.25, scaleAttribute.getBaseValue() - 0.25));
    }

    @Override
    public void applyCustomItemMeta(ItemMeta meta) {
        super.applyCustomItemMeta(meta);

        PotionMeta potion = (PotionMeta) meta;
        potion.setBasePotionType(PotionType.WATER);
        potion.setColor(Color.LIME);
    }
}
