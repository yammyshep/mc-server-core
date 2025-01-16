package com.jbuelow.servercore.crops;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

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
                yield seed == Material.COCOA_BEANS; //Only allow cocoa to replant with itself
            case NETHER_WART:
                yield seed == Material.NETHER_WART; //Only allow nether wart to replant with itself
            default:
                yield false;
        };
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Material seedType = event.getMaterial();
        Material plantingType = getCropMaterial(seedType);
        if (plantingType == null) return;

        if (event.getClickedBlock() == null) return;
        Block cropBlock = event.getClickedBlock();
        Material existingType = cropBlock.getType();
        BlockData existingData = cropBlock.getBlockData();

        // Check that this seed is allowed to replant the existing crop
        if (!isReplantAllowed(seedType, existingType)) {
            return;
        }

        // Growing torchflowers are TORCHFLOWER_CROP, but turn into a TORCHFLOWER once fully grown
        // TORCHFLOWER is not ageable, so treat it as fully grown and skip age check
        if (existingType != Material.TORCHFLOWER) {
            if (existingData instanceof Ageable existingAgeable) {
                if (existingAgeable.getAge() < existingAgeable.getMaximumAge()) {
                    return; //Not fully grown
                }
            } else {
                return; //Non-torchflower and non-ageable
            }
        }

        if (cropBlock.breakNaturally()) {
            cropBlock.setType(plantingType);

            BlockData replantedData = cropBlock.getBlockData();
            if (replantedData instanceof Ageable newAge) {
                newAge.setAge(0);
            }

            // Copy block facing direction when possible
            if ((replantedData instanceof Directional replantDir) && (existingData instanceof Directional existingDir)) {
                replantDir.setFacing(existingDir.getFacing());
            }

            cropBlock.setBlockData(replantedData);
        }
    }
}
