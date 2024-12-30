package com.jbuelow.servercore.balloon;

import com.jbuelow.servercore.PluginModule;
import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.ServerCoreModule;
import com.jbuelow.servercore.item.CustomItemsModule;

@PluginModule(name = "balloon")
public class BalloonModule implements ServerCoreModule {
    private final ServerCore plugin;

    public BalloonModule(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(new BalloonEventListener(), plugin);

        CustomItemsModule itemsModule = plugin.getModule(CustomItemsModule.class);
        if (itemsModule == null) {
            throw new RuntimeException("BalloonModule depends on CustomItemsModule!");
        }

        BalloonItem item = new BalloonItem();
        itemsModule.registerCustomItem(item);
        itemsModule.registerItemRecipes(item);
    }

    @Override
    public void onDisable() {

    }
}
