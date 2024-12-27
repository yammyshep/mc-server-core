package com.jbuelow.servercore.trust;

import com.jbuelow.servercore.PluginModule;
import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.ServerCoreModule;
import com.jbuelow.servercore.trust.service.UserTrustService;
import com.jbuelow.servercore.trust.service.VaultPermissionsUserTrustService;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

@PluginModule(name = "trust")
public class TrustModule implements ServerCoreModule {

    private final JavaPlugin plugin;

    private UserTrustService trustService = null;

    public TrustModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable() {
        // Create and register the user trust service before other parts of the module
        RegisteredServiceProvider<UserTrustService> provider = Bukkit.getServicesManager().getRegistration(UserTrustService.class);
        if (provider != null) {
            this.trustService = provider.getProvider();
        }

        RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
        trustService = new VaultPermissionsUserTrustService(rsp.getProvider());
        Bukkit.getServicesManager().register(UserTrustService.class, trustService, plugin, ServicePriority.Normal);


        plugin.getServer().getPluginManager().registerEvents(new TrustCheckEventListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new TrustCheckChatEventListener(trustService), plugin);

        plugin.getCommand("trust").setExecutor(new TrustCommand());
        plugin.getCommand("untrust").setExecutor(new UntrustCommand());
        plugin.getCommand("notrust").setExecutor(new NotrustCommand());
    }

    @Override
    public void onDisable() {

    }

    public UserTrustService getTrustService() {
        return trustService;
    }
}
