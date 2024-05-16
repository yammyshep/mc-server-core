package com.jbuelow.servercore.trust;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
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

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null)
            return false;
        LuckPerms luckPerms = provider.getProvider();

        User user = luckPerms.getUserManager().loadUser(otherPlayer.getUniqueId()).join();

        // Add the trusted group to the user
        user.data().add(Node.builder("group.trusted").build());
        provider.getProvider().getUserManager().saveUser(user);

        sender.sendMessage("Successfully trusted user %user%!");
        return true;
    }
}
