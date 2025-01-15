package com.jbuelow.servercore;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

public final class ServerCore extends JavaPlugin {
    private static final Logger log = Bukkit.getLogger();

    private static ServerCore instance;

    private YamlDocument config;
    private List<ServerCoreModule> modules = new ArrayList<>();

    public ServerCore() {
        instance = this;
    }

    @Override
    public void onLoad() { }

    @Override
    public void onEnable() {
        // Plugin startup logic

        try {
            config = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Reflections reflect = new Reflections("com.jbuelow.servercore");
        Set<Class<?>> classes = reflect.get(SubTypes.of(TypesAnnotated.with(PluginModule.class)).asClass());
        for (Class<?> clazz : classes) {
            PluginModule anno = clazz.getAnnotation(PluginModule.class);

            if (anno.enableByConfig()) {
                if (!config.getSection("modules").getBoolean(anno.name())) {
                    log.warning("Skipping module '" + anno.name() + "' as it is disabled in plugin configuration");
                    continue;
                }
            }

            try {
                Object modObject = clazz.getConstructor(ServerCore.class).newInstance(this);
                if (modObject instanceof ServerCoreModule) {
                    modules.add((ServerCoreModule) modObject);
                    log.info("Loaded plugin module '" + anno.name() + "' with class: " + clazz.getName());
                } else {
                    log.severe("Failed to initialize plugin module: " + clazz.getCanonicalName());
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                log.severe("Failed to initialize plugin module: " + clazz.getCanonicalName());
                e.printStackTrace();
            }
        }

        for (ServerCoreModule module : modules) {
            log.info("Enabling module: " + module.getClass().getName());
            module.onEnable();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        for (ServerCoreModule module : modules) {
            module.onDisable();
        }
    }

    public <T extends ServerCoreModule> T getModule(Class<T> moduleClass) {
        for (ServerCoreModule module : modules) {
            if (moduleClass.isAssignableFrom(module.getClass())) {
                return moduleClass.cast(module);
            }
        }

        return null;
    }

    public YamlDocument getConfiguration() {
        return config;
    }

    public static ServerCore get() {
        return instance;
    }
}
