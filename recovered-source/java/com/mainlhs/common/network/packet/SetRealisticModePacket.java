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
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public record SetRealisticModePacket(boolean enabled) {
    public static void encode(SetRealisticModePacket packet, FriendlyByteBuf buf) {
        buf.writeBoolean(packet.enabled());
    }

    public static SetRealisticModePacket decode(FriendlyByteBuf buf) {
        return new SetRealisticModePacket(buf.readBoolean());
    }

    public static void handle(SetRealisticModePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn((Dist)Dist.CLIENT, () -> () -> ClientPacketHandler.handleRealisticMode(packet)));
        context.setPacketHandled(true);
    }
}

