package com.jbuelow.servercore.carry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RideCommand extends Command {
    private final CarryModule module;

    protected RideCommand(CarryModule module) {
        super("ride", "Ride another player", "/<command> <player>", List.of());
        this.module = module;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Error: You must be a player!");
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage("Error: Must specify player to ride!");
            return false;
        }

        Player otherPlayer = Bukkit.getPlayer(args[0]);

        if (otherPlayer == null) {
            sender.sendMessage("Error: Could not find player!");
            return false;
        }

        module.mountPlayers(player, otherPlayer, player);
        return true;
    }
}
