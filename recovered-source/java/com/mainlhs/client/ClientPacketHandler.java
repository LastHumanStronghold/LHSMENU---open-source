/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.world.entity.player.Player
 */
package com.mainlhs.client;

import com.mainlhs.client.ClientRealisticMode;
import com.mainlhs.client.ClientScaleCache;
import com.mainlhs.common.network.packet.SetRealisticModePacket;
import com.mainlhs.common.network.packet.SyncPlayerScalePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public final class ClientPacketHandler {
    private ClientPacketHandler() {
    }

    public static void handleScaleSync(SyncPlayerScalePacket packet) {
        ClientScaleCache.set(packet.playerId(), packet.sizeParam());
        Minecraft minecraft = Minecraft.m_91087_();
        if (minecraft.f_91073_ == null) {
            return;
        }
        Player player = minecraft.f_91073_.m_46003_(packet.playerId());
        if (player != null) {
            player.m_6210_();
        }
    }

    public static void handleRealisticMode(SetRealisticModePacket packet) {
        ClientRealisticMode.setEnabled(packet.enabled());
    }

    public static float getPlayerSize(Player player) {
        return ClientScaleCache.get(player.m_20148_());
    }
}

