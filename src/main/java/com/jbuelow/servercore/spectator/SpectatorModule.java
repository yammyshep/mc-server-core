package com.jbuelow.servercore.spectator;

import com.jbuelow.servercore.PluginModule;
import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.ServerCoreModule;
import com.jbuelow.servercore.util.SpigotReflectionHelpers;
import org.bukkit.command.CommandMap;

@PluginModule(name = "spectator")
public class SpectatorModule implements ServerCoreModule {
    private final ServerCore plugin;

    private final SpectatorManager manager = new SpectatorManager();

    public SpectatorModule(ServerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        CommandMap commandMap = SpigotReflectionHelpers.getCommandMap();
        commandMap.register("servercore", new SpectatorToggleCommand(manager));
    }

    @Override
    public void onDisable() {

    }
}