package com.jbuelow.servercore.crops;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
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
            case NETHER_WART -> Material.NETHER_WART;
            case TORCHFLOWER_SEEDS -> Material.TORCHFLOWER_CROP;
            case PITCHER_POD -> Material.PITCHER_PLANT;
            default -> null;
        };
    }

    private static boolean isReplantAllowed(Material seed, Material crop) {
        return switch (crop) {
            case WHEAT:
            case BEETROOT:
            case CARROTS:
            case POTATOES:
            case TORCHFLOWER:
            case TORCHFLOWER_CROP:
            case PITCHER_PLANT:
                yield switch (seed) {
                    // Allow single block farmland crops to be replanted interchangeably
                    case WHEAT_SEEDS, BEETROOT_SEEDS, CARROT, POTATO, TORCHFLOWER_SEEDS, PITCHER_POD -> true;
                    default -> false;
                };
            case COCOA:
                yield seed == Material.COCOA_BEANS;  // Only allow cocoa to replant with itself
            case NETHER_WART:
                yield seed == Material.NETHER_WART;  // Only allow nether wart to replant with itself
            default:
                yield false;
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
        BlockData currBlockData = cropBlock.getBlockData();

        if (!isReplantAllowed(seedType, cropBlock.getType())) {
            return;
        }

        if (cropBlock.getType() != Material.TORCHFLOWER) {
            // TORCHFLOWER_CROP is used for a growing torchflower, while TORCHFLOWER is used once fully grown, which is not ageable
            if (!(currBlockData instanceof Ageable ageableBlock)) {
                return;
            }

            // Crop is not fully grown
            if (ageableBlock.getAge() < ageableBlock.getMaximumAge()) {
                return;
            }
        }

        if (cropBlock.breakNaturally()) {
            cropBlock.setType(cropType);

            BlockData newBlockData = cropBlock.getBlockData();
            if (newBlockData instanceof Ageable newAge) {
                newAge.setAge(0);
            }

            if ((newBlockData instanceof Directional newDir) && (currBlockData instanceof Directional oldDir)) {
                newDir.setFacing(oldDir.getFacing());
            }

            cropBlock.setBlockData(newBlockData);
        }
    }
}
