package com.jbuelow.servercore.crops;

import com.jbuelow.servercore.PluginModule;
import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.ServerCoreModule;

@PluginModule(name = "crops")
public class CropsModule implements ServerCoreModule {
    private final ServerCore plugin;

    public CropsModule(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(new CropReplantEventListener(this), plugin);
    }

    @Override
    public void onDisable() { }
}
