package com.jbuelow.servercore.carry;

import com.jbuelow.servercore.PluginModule;
import com.jbuelow.servercore.ServerCore;
import com.jbuelow.servercore.ServerCoreModule;
import com.jbuelow.servercore.util.SpigotReflectionHelpers;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

@PluginModule(name = "carry")
public class CarryModule implements ServerCoreModule {
    private final double maxDistance = 10.0;

    public CarryModule(ServerCore plugin) {}

    @Override
    public void onEnable() {
        CommandMap commandMap = SpigotReflectionHelpers.getCommandMap();
        commandMap.register("servercore", new CarryCommand(this));
        commandMap.register("servercore", new RideCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public void mountPlayers(Player top, Player bottom, Player instigator) {
        if (instigator != top && instigator != bottom) {
            return;
        }

        if (top.getWorld() != bottom.getWorld()) {
            instigator.sendMessage("Other player must be in the same world!");
            return;
        }

        double playersDistance = top.getLocation().distance(bottom.getLocation());
        if (playersDistance > maxDistance) {
            instigator.sendMessage("That player is too far away!");
            return;
        }

        World world = top.getWorld();
        Vector rayDirection = top.getEyeLocation().toVector().subtract(bottom.getEyeLocation().toVector()).normalize();
        RayTraceResult result = world.rayTraceBlocks(bottom.getEyeLocation(), rayDirection, playersDistance);

        if (result != null) {
            if (result.getHitBlock() != null && !result.getHitBlock().isEmpty()) {
                instigator.sendMessage("There is something in the way!");
                return;
            }
        }

        bottom.addPassenger(top);
    }
}
