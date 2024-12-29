package com.jbuelow.servercore.trust;

import com.jbuelow.servercore.trust.service.UserTrustService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class UntrustCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Error: Must specify player to untrust!");
            return false;
        }

        Player otherPlayer = Bukkit.getPlayer(args[0]);

        if (otherPlayer == null) {
            sender.sendMessage("Error: Could not find player!");
            return false;
        }

        RegisteredServiceProvider<UserTrustService> provider = Bukkit.getServicesManager().getRegistration(UserTrustService.class);
        if (provider == null)
            return false;
        UserTrustService trust = provider.getProvider();

        if (!trust.isTrusted(otherPlayer)) {
            sender.sendMessage("Error: That player is not trusted. Cannot untrust!");
            return true;
        }

        trust.setTrust(otherPlayer, false);

        sender.sendMessage("Revoked trust from " + otherPlayer.getDisplayName() + ".");
        otherPlayer.sendMessage("Your server trust status has been revoked!");
        return true;
    }
}
