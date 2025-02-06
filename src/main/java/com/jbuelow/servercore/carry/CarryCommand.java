package com.jbuelow.servercore.carry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CarryCommand extends Command {
    private final CarryModule module;

    protected CarryCommand(CarryModule module) {
        super("carry", "Carry another player", "/<command> <player>", List.of());
        this.module = module;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Error: You are not a player!");
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage("Error: Must specify player to carry!");
            return false;
        }

        Player otherPlayer = Bukkit.getPlayer(args[0]);

        if (otherPlayer == null) {
            sender.sendMessage("Error: Could not find player!");
            return false;
        }

        module.attemptMountPlayers(otherPlayer, player, player);
        return true;
    }
}
