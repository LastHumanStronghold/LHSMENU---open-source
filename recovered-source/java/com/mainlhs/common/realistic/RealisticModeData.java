/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Player
 */
package com.mainlhs.common.realistic;

import net.minecraft.world.entity.player.Player;

public final class RealisticModeData {
    private static final String TAG = "mainlhs_realistic_bodycam";

    private RealisticModeData() {
    }

    public static boolean isEnabled(Player player) {
        return player != null && player.getPersistentData().m_128471_(TAG);
    }

    public static boolean toggle(Player player) {
        boolean enabled = !RealisticModeData.isEnabled(player);
        RealisticModeData.setEnabled(player, enabled);
        return enabled;
    }

    public static void setEnabled(Player player, boolean enabled) {
        if (player == null) {
            return;
        }
        if (enabled) {
            player.getPersistentData().m_128379_(TAG, true);
        } else {
            player.getPersistentData().m_128473_(TAG);
        }
    }
}

