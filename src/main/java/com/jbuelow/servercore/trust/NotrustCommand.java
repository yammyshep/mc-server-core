package com.jbuelow.servercore.trust;

import com.jbuelow.servercore.trust.service.UserTrustService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class NotrustCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Error: Must specify player to trust!");
            return false;
        }

        Player otherPlayer = Bukkit.getPlayer(args[0]);

        if (otherPlayer == null) {
            sender.sendMessage("Error: Could not find player!");
            return false;
        }

        if (sender == otherPlayer) {
            sender.sendMessage("Error: You cannot kick yourself!");
            return true;
        }

        RegisteredServiceProvider<UserTrustService> provider = Bukkit.getServicesManager().getRegistration(UserTrustService.class);
        if (provider == null)
            return false;
        UserTrustService trust = provider.getProvider();

        if (trust.isTrusted(otherPlayer)) {
            sender.sendMessage("Error: That player is trusted! Cannot kick!");
            return true;
        }

        otherPlayer.kickPlayer("Kicked by a trusted player");
        Bukkit.broadcastMessage(sender.getName() + " kicked untrusted player " + otherPlayer.getDisplayName() + " from the server.");
        return true;
    }
}
