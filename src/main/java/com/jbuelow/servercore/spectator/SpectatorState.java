package com.jbuelow.servercore.spectator;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SpectatorState implements Serializable {
    private final Map<String, Object> serializedLocation;
    private final GameMode gameMode;
    private final List<Map<String, Object>> serializedPotionEffects = new ArrayList<>();
    private final float fallDistance;
    private final int remainingAir;
    private final int freezeTicks;
    private final int fireTicks;
    private final int noDamageTicks;

    public SpectatorState(Player player) {
        serializedLocation = player.getLocation().serialize();
        gameMode = player.getGameMode();
        fallDistance = player.getFallDistance();
        remainingAir = player.getRemainingAir();
        fireTicks = player.getFireTicks();
        freezeTicks = player.getFreezeTicks();
        noDamageTicks = player.getNoDamageTicks();

        serializedPotionEffects.clear();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            serializedPotionEffects.add(effect.serialize());
        }
    }

    public void applyStateToPlayer(Player player) {
        player.teleport(getLocation());
        player.setGameMode(getGameMode());
        player.setFallDistance(getFallDistance());
        player.setRemainingAir(getRemainingAir());
        player.setFireTicks(getFireTicks());
        player.setFreezeTicks(getFreezeTicks());
        player.setNoDamageTicks(getNoDamageTicks());

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.addPotionEffects(getPotionEffects());
    }

    public Location getLocation() {
        return Location.deserialize(serializedLocation);
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Collection<PotionEffect> getPotionEffects() {
        Collection<PotionEffect> effects = new ArrayList<>();
        for (Map<String, Object> serializedEffect : serializedPotionEffects) {
            effects.add(new PotionEffect(serializedEffect));
        }
        return effects;
    }

    public float getFallDistance() {
        return fallDistance;
    }

    public int getRemainingAir() {
        return remainingAir;
    }

    private int getFireTicks() {
        return fireTicks;
    }

    public int getFreezeTicks() {
        return freezeTicks;
    }

    public int getNoDamageTicks() {
        return noDamageTicks;
    }
}
