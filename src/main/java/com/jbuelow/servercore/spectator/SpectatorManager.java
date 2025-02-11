package com.jbuelow.servercore.spectator;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.spectator.event.PlayerEnterSpectatorModeEvent;
import com.jbuelow.servercore.spectator.event.PlayerExitSpectatorModeEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Map;

public class SpectatorManager {
    private final NamespacedKey stateKey;
    private final static SpectatorStateDataType stateType = new SpectatorStateDataType();
    private final Map<Player, Location> cachedReturnLocations = new HashMap<>();

    public SpectatorManager() {
        this.stateKey = new NamespacedKey(ServerCore.get(), "spectator_state");
    }

    public void enterSpectatorMode(Player player) {
        SpectatorState spectatorState = new SpectatorState(player);

        // Emit event and stop if cancelled
        PlayerEnterSpectatorModeEvent event = new PlayerEnterSpectatorModeEvent(player);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        // Unleash any entities leashed by player and drop a lead
        for (Entity entity : player.getWorld().getEntities()) {
            if (!(entity instanceof LivingEntity)) {
                continue;
            }

            LivingEntity livingEntity = (LivingEntity) entity;
            if (livingEntity.isLeashed() && livingEntity.getLeashHolder() == player) {
                livingEntity.setLeashHolder(null);
                player.getWorld().dropItemNaturally(livingEntity.getLocation(), new ItemStack(Material.LEAD, 1));
            }
        }

        player.getPersistentDataContainer().set(stateKey, stateType, spectatorState);
        player.setGameMode(GameMode.SPECTATOR);
    }

    public boolean hasSpectatorStateInfo(Player player) {
        return player.getPersistentDataContainer().has(stateKey, stateType);
    }

    public void exitSpectatorMode(Player player) {
        if (!hasSpectatorStateInfo(player)) return;

        PlayerExitSpectatorModeEvent event = new PlayerExitSpectatorModeEvent(player);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        SpectatorState state = player.getPersistentDataContainer().get(stateKey, stateType);
        if (state == null) return;
        state.applyStateToPlayer(player);

        player.getPersistentDataContainer().remove(stateKey);
        cachedReturnLocations.remove(player);
    }

    public boolean isPlayerSpectating(Player player) {
        return player.getGameMode() == GameMode.SPECTATOR && hasSpectatorStateInfo(player);
    }

    public Location getReturnLocation(Player player) {
        return cachedReturnLocations.computeIfAbsent(player, p -> {
            if (!isPlayerSpectating(player)) {
                return null;
            }

            SpectatorState state = player.getPersistentDataContainer().get(stateKey, stateType);
            if (state == null) return null;
            return state.getLocation();
        });
    }
}
