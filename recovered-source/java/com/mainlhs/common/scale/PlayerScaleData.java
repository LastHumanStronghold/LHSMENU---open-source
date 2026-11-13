/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.fml.DistExecutor
 */
package com.mainlhs.common.scale;

import com.mainlhs.client.ClientPacketHandler;
import com.mainlhs.common.network.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public final class PlayerScaleData {
    private static final String NBT_KEY = "GenericScale";

    private PlayerScaleData() {
    }

    public static float getSize(Player player) {
        if (player.m_9236_().f_46443_) {
            return ((Float)DistExecutor.unsafeCallWhenOn((Dist)Dist.CLIENT, () -> () -> Float.valueOf(ClientPacketHandler.getPlayerSize(player)))).floatValue();
        }
        CompoundTag tag = player.getPersistentData();
        if (tag.m_128441_(NBT_KEY)) {
            return tag.m_128457_(NBT_KEY);
        }
        return 1.0f;
    }

    public static void setSize(Player player, float sizeParam) {
        CompoundTag tag = player.getPersistentData();
        if (Math.abs(sizeParam - 1.0f) < 1.0E-4f) {
            tag.m_128473_(NBT_KEY);
        } else {
            tag.m_128350_(NBT_KEY, sizeParam);
        }
        player.m_6210_();
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            NetworkHandler.syncToAll(serverPlayer);
        }
    }
}

