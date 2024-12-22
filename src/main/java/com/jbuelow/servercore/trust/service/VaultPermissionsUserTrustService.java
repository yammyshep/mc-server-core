package com.jbuelow.servercore.trust.service;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

public class VaultPermissionsUserTrustService implements UserTrustService {

    private static final String TRUSTED_PERM = "group.trusted";

    private final Permission perms;

    public VaultPermissionsUserTrustService(Permission perms) {
        this.perms = perms;
    }

    @Override
    public boolean isTrusted(Player player) {
        return perms.has(player, TRUSTED_PERM);
    }

    @Override
    public void setTrust(Player player, boolean isTrusted) {
        if (isTrusted) {
            perms.playerAdd(null, player, TRUSTED_PERM);
        } else {
            perms.playerRemove(null, player, TRUSTED_PERM);
        }
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
