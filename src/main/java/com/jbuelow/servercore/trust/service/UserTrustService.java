package com.jbuelow.servercore.trust.service;

import org.bukkit.entity.Player;

public interface UserTrustService {
    boolean isTrusted(Player player);
    void setTrust(Player player, boolean isTrusted);
    void trustPlayer(Player player);
    void revokeTrust(Player player);
}
