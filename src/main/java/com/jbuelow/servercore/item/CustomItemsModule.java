package com.jbuelow.servercore.item;

import com.jbuelow.servercore.PluginModule;
import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.ServerCoreModule;
import com.jbuelow.servercore.item.event.CustomItemUpdateEventListener;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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
        plugin.getServer().getPluginManager().registerEvents(new CustomItemUpdateEventListener(this), plugin);

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

            if (item instanceof Listener) {
                plugin.getServer().getPluginManager().registerEvents((Listener) item, plugin);
            }
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

    public boolean isCustomItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.has(CustomItem.customItemKey, PersistentDataType.STRING);
    }

    public Optional<CustomItem> getCustomItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return Optional.empty();

        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (!data.has(CustomItem.customItemKey, PersistentDataType.STRING)) {
            return Optional.empty();
        }

        String keyString = data.getOrDefault(CustomItem.customItemKey, PersistentDataType.STRING, "");
        NamespacedKey itemKey = NamespacedKey.fromString(keyString, ServerCore.get());
        Optional<CustomItem> itemOpt = getItem(itemKey);
        if (itemOpt.isEmpty()) {
            return Optional.empty();
        }

        short damage = 0;
        if (item instanceof Damageable damageableItem) {
            damage = (short) damageableItem.getDamage();
        }

        Class<? extends CustomItem> itemClass = itemOpt.get().getClass();
        try {
            CustomItem customItem = itemClass.getConstructor(int.class, short.class).newInstance(item.getAmount(), damage);
            return Optional.of(customItem);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.severe("Failure to clone custom item class: " + itemClass.getCanonicalName());
            e.printStackTrace();
            return Optional.empty();
        }
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
