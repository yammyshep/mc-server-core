package com.jbuelow.servercore;

import com.jbuelow.servercore.balloon.BalloonModule;
import com.jbuelow.servercore.trust.TrustModule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class ServerCore extends JavaPlugin {

    private static ServerCore instance;

    private List<ServerCoreModule> modules = new ArrayList<>();

    public ServerCore() {
        instance = this;
    }

    @Override
    public void onLoad() {
        modules.add(new TrustModule(this));
        modules.add(new BalloonModule(this));
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        for (ServerCoreModule module : modules) {
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

    public <T extends ServerCoreModule> T getModule() {
        for (ServerCoreModule module : modules) {
            try {
                return (T) module;
            } catch (ClassCastException ignored) {}
        }
        return null;
    }

    public static ServerCore get() {
        return instance;
    }
}
