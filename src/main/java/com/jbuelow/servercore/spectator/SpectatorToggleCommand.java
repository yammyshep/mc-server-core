package com.jbuelow.servercore.spectator;

import com.jbuelow.servercore.ServerCore;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpectatorToggleCommand extends Command {
    private final SpectatorManager manager;

    public SpectatorToggleCommand(SpectatorManager manager) {
        super("spec");
        setPermission("servercore.command.spec");
        setAliases(List.of("s"));
        setDescription("Toggle spectator mode");
        setUsage("/<command>");

        this.manager = manager;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Error: This command must be executed by a player");
            return false;
        }

        Player player = (Player) sender;

        if (player.getGameMode() == GameMode.SPECTATOR) {
            if (!manager.hasSpectatorStateInfo(player)) {
                player.setGameMode(GameMode.SURVIVAL); //TODO: Use default from server.properties
            }

            manager.exitSpectatorMode(player);
        } else {
            manager.enterSpectatorMode(player);
        }

        return true;
    }
}
