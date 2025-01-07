package com.jbuelow.servercore.item;

import com.jbuelow.servercore.PluginModule;
import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.ServerCoreModule;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Logger;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

@PluginModule(name = "customItems", enableByConfig = false)
public class CustomItemsModule implements ServerCoreModule {
    private static final Logger log = Bukkit.getLogger();
    private final ServerCore plugin;

    private final Map<NamespacedKey, CustomItem> customItemMap = new HashMap<>();

    public CustomItemsModule(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(new CustomItemEnchantmentEventListener(this), plugin);

        Reflections reflect = new Reflections();

        // Register any CustomItem classes with RegisterCustomItem annotation
        Set<Class<?>> classes = reflect.get(SubTypes.of(TypesAnnotated.with(RegisterCustomItem.class)).asClass());
        for (Class<?> clazz : classes) {
            RegisterCustomItem customItemAnno = clazz.getAnnotation(RegisterCustomItem.class);
            if (customItemAnno == null) continue;

            try {
                Object itemObject = clazz.getConstructor().newInstance();
                if (itemObject instanceof CustomItem) {
                    registerCustomItem((CustomItem) itemObject);
                } else {
                    log.severe("Failed to register custom item: " + customItemAnno.key());
                }
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                     InstantiationException e) {
                log.severe("Failed to register custom item: " + customItemAnno.key());
                e.printStackTrace();
            }
        }

        // Register any custom recipes with the RegisterCustomRecipes annotation
        classes = reflect.get(SubTypes.of(TypesAnnotated.with(RegisterCustomRecipes.class)).asClass());
        for (Class<?> clazz : classes) {
            RegisterCustomRecipes customRecipeAnno = clazz.getAnnotation(RegisterCustomRecipes.class);
            if (customRecipeAnno == null) continue;

            try {
                Object recipeObject = clazz.getConstructor().newInstance();
                if (recipeObject instanceof ItemRecipe) {
                    registerItemRecipes((ItemRecipe) recipeObject);
                } else {
                    log.severe("Failed to register custom recipes class: " + clazz.getCanonicalName());
                }
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                     InstantiationException e) {
                log.severe("Failed to register custom recipes class: " + clazz.getCanonicalName());
                e.printStackTrace();
            }
        }
    }

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

    public Optional<CustomItem> getItem(ItemStack item) {
        for (CustomItem customItem : customItemMap.values()) {
            if (customItem.isItem(item)) {
                try {
                    return Optional.of(customItem.getClass().getConstructor(int.class).newInstance(item.getAmount()));
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                         IllegalAccessException ignored) { }
            }
        }

        return Optional.empty();
    }
}
