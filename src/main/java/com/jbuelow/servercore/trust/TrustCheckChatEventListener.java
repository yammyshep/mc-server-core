package com.jbuelow.servercore.trust;

import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.trust.service.UserTrustService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class TrustCheckChatEventListener implements Listener {

    private final UserTrustService trust;
    private static final long OTHER_PLAYER_NOTICE_INTERVAL = 15000;
    private final Map<Player, Long> lastChatTimes = new HashMap<>();

    public TrustCheckChatEventListener(UserTrustService trustService) {
        trust = trustService;
    }

    private void handleUntrustedChatMessage(Player player, Event event) {
        long lastChat = lastChatTimes.getOrDefault(player, 0L);
        if (System.currentTimeMillis() - OTHER_PLAYER_NOTICE_INTERVAL >= lastChat) {
            // Send all players the first message, only trusted players the second.
            for (Player trustedPlayer : Bukkit.getOnlinePlayers()) {
                trustedPlayer.sendMessage(player.getDisplayName() + " wants to chat but is not trusted yet.");

                if (trust.isTrusted(trustedPlayer)) {
                    trustedPlayer.sendMessage("If you know and trust this player, type /trust " + player.getDisplayName());
                }
            }

            lastChatTimes.put(player, System.currentTimeMillis());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (trust.isTrusted(event.getPlayer())) {
            return;
        }

        event.setCancelled(true);
        handleUntrustedChatMessage(event.getPlayer(), event);
    }
}
