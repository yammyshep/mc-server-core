package com.jbuelow.servercore.spectator;

import com.jbuelow.servercore.PluginModule;
import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.ServerCoreModule;
import com.jbuelow.servercore.spectator.event.SpectatorEventListener;
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
        plugin.getServer().getPluginManager().registerEvents(new SpectatorEventListener(this), plugin);

        new SpectatorVisualizerRunnable(this).runTaskTimer(plugin, 5L, 5L);
    }

    @Override
    public void onDisable() {

    }

    public SpectatorManager getManager() {
        return manager;
    }
}
