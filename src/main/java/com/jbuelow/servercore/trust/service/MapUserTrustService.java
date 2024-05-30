package com.jbuelow.servercore.trust.service;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MapUserTrustService implements UserTrustService {

    private Map<Player, Boolean> data = new HashMap();

    @Override
    public boolean isTrusted(Player player) {
        return data.getOrDefault(player, false);
    }

    @Override
    public void setTrust(Player player, boolean isTrusted) {
        data.put(player, isTrusted);
    }

    @Override
    public void trustPlayer(Player player) {
        setTrust(player, true);
    }

    @Override
    public void revokeTrust(Player player) {
        setTrust(player, false);
    }
}
