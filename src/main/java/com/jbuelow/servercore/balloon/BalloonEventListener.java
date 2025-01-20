package com.jbuelow.servercore.balloon;

import com.jbuelow.servercore.ServerCore;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class BalloonEventListener implements Listener {
    private static final BalloonItem balloonItem = new BalloonItem();

    private final Map<Player, PotionEffect> prevLevitationEffects = new HashMap<>();

    private boolean isBalloon(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        return balloonItem.isItem(itemStack);
    }

    private void updatePlayerBalloonState(Player player) {
        if (getActiveBalloonItem(player) != null) {
            startBalloon(player);
        } else {
            endBalloon(player);
        }
    }

    private ItemStack getActiveBalloonItem(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (isBalloon(mainHand)) {
            return mainHand;
        } else if (isBalloon(offHand)) {
            return offHand;
        }

        return null;
    }

    public boolean isBallooning(Player player) {
        return prevLevitationEffects.containsKey(player);
    }

    private void startBalloon(Player player) {
        if (isBallooning(player)) {
            return;
        }

        prevLevitationEffects.put(player, player.getPotionEffect(PotionEffectType.LEVITATION));
        player.removePotionEffect(PotionEffectType.LEVITATION);

        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, -1, 1, true));
    }

    private void endBalloon(Player player) {
        if (!isBallooning(player)) {
            return;
        }

        player.removePotionEffect(PotionEffectType.LEVITATION);

        PotionEffect restoredLevitation = prevLevitationEffects.remove(player);
        if (restoredLevitation != null) {
            player.addPotionEffect(restoredLevitation);
        }
    }

    public void popBalloon(Player player) {
        if (!isBallooning(player)) {
            return;
        }

        ItemStack balloon = getActiveBalloonItem(player);
        if (balloon != null) {
            balloon.setAmount(balloon.getAmount() - 1);
            endBalloon(player);

            // Let the player fall a bit before off-hand or stacked balloons work again
            Bukkit.getScheduler().runTaskLater(ServerCore.get(), () -> {
                updatePlayerBalloonState(player);
            }, 10L);
        }

        Location popLocation = player.getEyeLocation();
        World world = player.getWorld();
        world.playSound(popLocation, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f);
        player.spawnParticle(Particle.ITEM_CRACK, player.getEyeLocation(),50, balloon);
    }

    // Called when the player changes what item is selected in the hotbar
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Inventory inv = event.getPlayer().getInventory();

        if (isBalloon(inv.getItem(event.getPreviousSlot()))) {
            endBalloon(event.getPlayer());
        }

        if (isBalloon(inv.getItem(event.getNewSlot()))) {
            startBalloon(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayerBalloonState(event.getPlayer());
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            Bukkit.getScheduler().runTaskLater(ServerCore.get(), () -> {
                updatePlayerBalloonState(player);
            }, 1L);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        updatePlayerBalloonState(event.getPlayer());
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        if (isBalloon(event.getMainHandItem()) || isBalloon(event.getOffHandItem())) {
            startBalloon(event.getPlayer());
        } else {
            endBalloon(event.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Bukkit.getScheduler().runTaskLater(ServerCore.get(), () -> {
                updatePlayerBalloonState((Player) event.getWhoClicked());
            }, 1L);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player targetPlayer)) {
            return;
        }

        if (!isBallooning(targetPlayer)) {
            return;
        }

        if (event.getModifiedType() != PotionEffectType.LEVITATION) {
            return;
        }

        if (event.getCause() == EntityPotionEffectEvent.Cause.PLUGIN) {
            return;
        }

        if (event.getAction() == EntityPotionEffectEvent.Action.ADDED ||
                event.getAction() == EntityPotionEffectEvent.Action.CHANGED) {
            event.setCancelled(true);
            prevLevitationEffects.put(targetPlayer, event.getNewEffect());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!isBallooning(event.getPlayer())) {
            return;
        }

        Player player = event.getPlayer();
        Location touchingHead = event.getTo().clone();
        touchingHead.add(0, player.getBoundingBox().getHeight(), 0);
        Block aboveHead = touchingHead.getBlock();
        if (!aboveHead.isEmpty()) {
            Vector pointAboveHead = touchingHead.toVector();
            pointAboveHead.subtract(aboveHead.getLocation().toVector());
            boolean isColliding = false;
            for (BoundingBox box : aboveHead.getCollisionShape().getBoundingBoxes()) {
                if (box.contains(pointAboveHead)) {
                    isColliding = true;
                    break;
                }
            }
            if (isColliding) {
                popBalloon(event.getPlayer());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer)) {
            return;
        }

        switch (event.getCause()) {
            case ENTITY_EXPLOSION:
            case LIGHTNING:
            case PROJECTILE:
            case FALLING_BLOCK:
                popBalloon(damagedPlayer);
        }
    }
}
