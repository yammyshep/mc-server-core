package com.jbuelow.servercore.spectator;

import org.bukkit.GameMode;
import org.bukkit.Location;

import java.io.Serializable;
import java.util.Map;

public class SpectatorState implements Serializable {
    private Map<String, Object> returnLocation;
    private GameMode returnGamemode;

    public SpectatorState(Location location, GameMode gameMode) {
        returnLocation = location.serialize();
        returnGamemode = gameMode;
    }

    public Location getReturnLocation() {
        return Location.deserialize(returnLocation);
    }

    public GameMode getReturnGamemode() {
        return returnGamemode;
    }

    //TODO: Handle potion effects
    //TODO: Handle fall damage
    //TODO: Handle drowning
    //TODO: Handle freezing
    //TODO: Handle burning
}
