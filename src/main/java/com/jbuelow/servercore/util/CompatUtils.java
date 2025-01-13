package com.jbuelow.servercore.util;

import org.bukkit.Bukkit;

public class CompatUtils {
    public static boolean serverSupportsModelDataComponent() {
        return serverIsAtLeastVersion("1.21.4");
    }

    public static boolean serverSupportsDataComponents() {
        return serverIsAtLeastVersion("1.20.5");
    }

    public static boolean serverSupportsInfinitePotionDuration() {
        return serverIsAtLeastVersion("1.19.4");
    }

    public static boolean serverIsAtLeastVersion(String version) {
        String[] givenSplit = version.split("\\.");
        if (givenSplit.length < 2) return false;

        int givenMajor = Integer.parseInt(givenSplit[0]);
        int givenMinor = Integer.parseInt(givenSplit[1]);
        int givenPatch = 0;
        if (givenSplit.length >= 3) {
            givenPatch = Integer.parseInt(givenSplit[2]);
        }

        String currentVersion = Bukkit.getBukkitVersion().split("-")[0];
        String[] currentSplit = currentVersion.split("\\.");
        if (currentSplit.length < 2) return false;

        int currentMajor = Integer.parseInt(currentSplit[0]);
        int currentMinor = Integer.parseInt(currentSplit[1]);
        int currentPatch = 0;
        if (currentSplit.length >= 3) {
            currentPatch = Integer.parseInt(currentSplit[2]);
        }

        return currentMajor > givenMajor ||
                (currentMajor == givenMajor && currentMinor > givenMinor) ||
                (currentMajor == givenMajor && currentMinor == givenMinor && currentPatch >= givenPatch);
    }
}
