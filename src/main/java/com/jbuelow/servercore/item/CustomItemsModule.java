package com.jbuelow.servercore.item;

import com.jbuelow.servercore.PluginModule;
import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.ServerCoreModule;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@PluginModule(name = "customItems", enableByConfig = false)
public class CustomItemsModule implements ServerCoreModule {
    private final ServerCore plugin;

    private final Map<NamespacedKey, CustomItem> customItemMap = new HashMap<>();

    public CustomItemsModule(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() { }

    @Override
    public void onDisable() { }

    public void registerCustomItems(List<CustomItem> items) {
        for (CustomItem item : items) {
            registerCustomItem(item);
        }
    }

    public void registerAllItemRecipes(List<ItemRecipe> itemRecipes) {
        for (ItemRecipe recipes : itemRecipes) {
            registerItemRecipes(recipes);
        }
    }

    public void registerCustomItem(CustomItem item) {
        if (!customItemMap.containsKey(item.getItemKey())) {
            customItemMap.put(item.getItemKey(), item);
        }
    }

    public void registerItemRecipes(ItemRecipe itemRecipe) {
        for (Recipe recipe : itemRecipe.getRecipes()) {
            plugin.getServer().addRecipe(recipe);
        }
    }

    public Optional<CustomItem> getItem(NamespacedKey itemKey) {
        return Optional.ofNullable(customItemMap.get(itemKey));
    }

    public Optional<CustomItem> findItemByClass(Class<CustomItem> customItemClass) {
        for (CustomItem item : customItemMap.values()) {
            if (customItemClass.isAssignableFrom(item.getClass())) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }
}
