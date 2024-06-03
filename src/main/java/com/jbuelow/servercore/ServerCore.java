package com.jbuelow.servercore;

import com.jbuelow.servercore.trust.TrustCommand;
import com.jbuelow.servercore.trust.TrustCheckEventListener;
import com.jbuelow.servercore.trust.UntrustCommand;
import com.jbuelow.servercore.trust.service.MapUserTrustService;
import com.jbuelow.servercore.trust.service.UserTrustService;
import com.jbuelow.servercore.trust.service.VaultPermissionsUserTrustService;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerCore extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        getServer().getPluginManager().registerEvents(new TrustCheckEventListener(), this);

        this.getCommand("trust").setExecutor(new TrustCommand());
        this.getCommand("untrust").setExecutor(new UntrustCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static UserTrustService getTrustService() {
        RegisteredServiceProvider<UserTrustService> provider = Bukkit.getServicesManager().getRegistration(UserTrustService.class);
        if (provider == null) {
            RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
            Permission vaultPerms = rsp.getProvider();

            Bukkit.getServicesManager().register(UserTrustService.class, new VaultPermissionsUserTrustService(vaultPerms), getPlugin(ServerCore.class), ServicePriority.Normal);

            provider = Bukkit.getServicesManager().getRegistration(UserTrustService.class);

            if (provider == null) {
                return null;
            }
        }

        return provider.getProvider();
    }
}
