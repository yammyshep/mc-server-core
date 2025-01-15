package com.jbuelow.servercore.spectator;

import com.jbuelow.servercore.ServerCore;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class SpectatorManager {
    private final NamespacedKey stateKey;
    private final static SpectatorStateDataType stateType = new SpectatorStateDataType();

    public SpectatorManager() {
        this.stateKey = new NamespacedKey(ServerCore.get(), "spectator_state");
    }

    public void enterSpectatorMode(Player player) {
        SpectatorState spectatorState = new SpectatorState(player.getLocation(), player.getGameMode());

        spectatorState.setPotionEffects(player.getActivePotionEffects());
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        spectatorState.setFallDistance(player.getFallDistance());

        player.getPersistentDataContainer().set(stateKey, stateType, spectatorState);
        player.setGameMode(GameMode.SPECTATOR);
    }

    public boolean hasSpectatorStateInfo(Player player) {
        return player.getPersistentDataContainer().has(stateKey, stateType);
    }

    public void exitSpectatorMode(Player player) {
        if (!hasSpectatorStateInfo(player)) return;

        SpectatorState state = player.getPersistentDataContainer().get(stateKey, stateType);
        player.setGameMode(state.getGameMode());
        player.teleport(state.getLocation());
        player.addPotionEffects(state.getPotionEffects());
        player.setFallDistance(state.getFallDistance());

        player.getPersistentDataContainer().remove(stateKey);
    }
}
