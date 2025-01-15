package com.jbuelow.servercore.crops;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CropReplantEventListener implements Listener {
    private final CropsModule module;

    public CropReplantEventListener(CropsModule module) {
        this.module = module;
    }

    private static Material getCropMaterial(Material seedMaterial) {
        return switch (seedMaterial) {
            case WHEAT_SEEDS -> Material.WHEAT;
            case BEETROOT_SEEDS -> Material.BEETROOT;
            case CARROT -> Material.CARROTS;
            case POTATO -> Material.POTATOES;
            case COCOA_BEANS -> Material.COCOA;
            case NETHER_WART -> Material.NETHER_WART_BLOCK;
            case PITCHER_POD -> Material.PITCHER_PLANT;
            default -> null;
        };
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack usedItem = event.getItem();

        if (usedItem == null) return;
        if (event.getClickedBlock() == null) return;

        Material seedType = usedItem.getType();
        Material cropType = getCropMaterial(seedType);
        if (cropType == null) return;

        // Block is not a crop
        Block cropBlock = event.getClickedBlock();
        if (!(cropBlock.getBlockData() instanceof Ageable ageableBlock)) {
            return;
        }

        // Crop is not fully grown
        if (ageableBlock.getAge() < ageableBlock.getMaximumAge()) {
            return;
        }

        if (cropBlock.breakNaturally()) {
            cropBlock.setType(cropType);
            ageableBlock.setAge(0);
            cropBlock.setBlockData(ageableBlock);
        }
    }
}
