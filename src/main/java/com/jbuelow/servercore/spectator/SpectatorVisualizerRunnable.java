package com.jbuelow.servercore.spectator;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class SpectatorVisualizerRunnable extends BukkitRunnable {
    private final SpectatorManager manager;

    public SpectatorVisualizerRunnable(SpectatorModule module) {
        manager = module.getManager();
    }

    @Override
    public void run() {
        List<Player> spectatingPlayers = new ArrayList<>();
        List<Player> nonSpectatingPlayers = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (manager.isPlayerSpectating(player)) {
                spectatingPlayers.add(player);
            } else if (player.getGameMode() != GameMode.SPECTATOR) {
                nonSpectatingPlayers.add(player);
            }
        }

        for (Player spectator : spectatingPlayers) {
            for (Player nonSpectator : nonSpectatingPlayers) {
                nonSpectator.spawnParticle(Particle.REDSTONE, spectator.getEyeLocation(), 1,
                        new Particle.DustOptions(Color.PURPLE, 25.0f));
            }
        }
    }
}
