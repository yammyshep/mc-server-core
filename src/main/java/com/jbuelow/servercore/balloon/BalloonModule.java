package com.jbuelow.servercore.balloon;

import com.jbuelow.servercore.PluginModule;
import com.jbuelow.servercore.ServerCoreModule;
import org.bukkit.plugin.java.JavaPlugin;

@PluginModule(name = "balloon")
public class BalloonModule implements ServerCoreModule {

    private final JavaPlugin plugin;

    public BalloonModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(new BalloonEventListener(), plugin);

        BalloonItem balloonItem = new BalloonItem();
        plugin.getServer().addRecipe(balloonItem.getRecipe());
    }

    @Override
    public void onDisable() {

    }
}
