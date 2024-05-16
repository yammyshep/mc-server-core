package com.jbuelow.servercore;

import com.jbuelow.servercore.trust.TrustCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerCore extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.getCommand("trust").setExecutor(new TrustCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
