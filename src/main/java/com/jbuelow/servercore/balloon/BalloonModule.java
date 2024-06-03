package com.jbuelow.servercore.balloon;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.ServerCoreModule;

public class BalloonModule implements ServerCoreModule {

    private final ServerCore plugin;

    public BalloonModule(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(new BalloonEventListener(), plugin);
    }

    @Override
    public void onDisable() {

    }
}
