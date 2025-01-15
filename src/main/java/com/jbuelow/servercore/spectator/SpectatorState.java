package com.jbuelow.servercore.spectator;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SpectatorState implements Serializable {
    private Map<String, Object> serializedLocation;
    private GameMode gameMode;
    private final List<Map<String, Object>> serializedPotionEffects = new ArrayList<>();
    private float fallDistance;

    public SpectatorState(Location location, GameMode gameMode) {
        serializedLocation = location.serialize();
        this.gameMode = gameMode;
    }

    public Location getLocation() {
        return Location.deserialize(serializedLocation);
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setPotionEffects(Collection<PotionEffect> effects) {
        serializedPotionEffects.clear();
        for (PotionEffect effect : effects) {
            serializedPotionEffects.add(effect.serialize());
        }
    }

    public Collection<PotionEffect> getPotionEffects() {
        Collection<PotionEffect> effects = new ArrayList<>();
        for (Map<String, Object> serializedEffect : serializedPotionEffects) {
            effects.add(new PotionEffect(serializedEffect));
        }
        return effects;
    }

    public void setFallDistance(float fallDistance) {
        this.fallDistance = fallDistance;
    }

    public float getFallDistance() {
        return fallDistance;
    }

    //TODO: Handle drowning
    //TODO: Handle freezing
    //TODO: Handle burning
}
