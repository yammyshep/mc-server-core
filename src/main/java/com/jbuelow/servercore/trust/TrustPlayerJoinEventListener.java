package com.jbuelow.servercore.trust;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.trust.service.UserTrustService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TrustPlayerJoinEventListener implements Listener {

    private final UserTrustService trust;

    public TrustPlayerJoinEventListener(UserTrustService trust) {
        this.trust = trust;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (trust.isTrusted(player)) {
            return;
        }

        player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You are not yet a trusted member of this server!",
                ChatColor.GRAY + "You won't be able to interact with the world until another player trusts you using /trust " +
                player.getDisplayName() + ".");

        // Send messages to other players 1 tick later to display in chat after the usual join message
        Bukkit.getScheduler().runTaskLaterAsynchronously(ServerCore.get(), () -> {
            for (Player trustedPlayer : Bukkit.getOnlinePlayers()) {
                if (!trust.isTrusted(trustedPlayer)) {
                    continue;
                }

                trustedPlayer.sendMessage(ChatColor.GRAY + "If you know and trust " + player.getDisplayName() +
                        " you can trust them to the server using /trust " + player.getDisplayName() + ".");
            }
        }, 1L);
    }
}
