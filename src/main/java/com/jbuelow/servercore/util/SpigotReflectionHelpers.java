package com.jbuelow.servercore.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;

public final class SpigotReflectionHelpers {
    private SpigotReflectionHelpers() {}

    public static CommandMap getCommandMap() {
        CommandMap map = null;

        try {
            PluginManager pluginManager = Bukkit.getPluginManager();
            Field f = pluginManager.getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            map = (CommandMap) f.get(pluginManager);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
            Bukkit.getLogger().severe("Failed to get server command map!");
            e.printStackTrace();
        }

        return map;
    }
}
