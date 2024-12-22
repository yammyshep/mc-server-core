package com.jbuelow.servercore.trust;

import com.jbuelow.servercore.trust.service.UserTrustService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TrustCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Error: Must specify player to trust!");
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

        trust.setTrust(otherPlayer, true);

        sender.sendMessage("Successfully trusted " + otherPlayer.getDisplayName() + "!");
        otherPlayer.sendMessage(sender.getName() + " has trusted you to the server.");
        return true;
    }
}
