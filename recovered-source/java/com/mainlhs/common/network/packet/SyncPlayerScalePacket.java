/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.fml.DistExecutor
 *  net.minecraftforge.network.NetworkEvent$Context
 */
package com.mainlhs.common.network.packet;

import com.mainlhs.client.ClientPacketHandler;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public record SyncPlayerScalePacket(UUID playerId, float sizeParam) {
    public static void encode(SyncPlayerScalePacket packet, FriendlyByteBuf buf) {
        buf.m_130077_(packet.playerId());
        buf.writeFloat(packet.sizeParam);
    }

    public static SyncPlayerScalePacket decode(FriendlyByteBuf buf) {
        return new SyncPlayerScalePacket(buf.m_130259_(), buf.readFloat());
    }

    public static void handle(SyncPlayerScalePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn((Dist)Dist.CLIENT, () -> () -> ClientPacketHandler.handleScaleSync(packet)));
        context.setPacketHandled(true);
    }
}

