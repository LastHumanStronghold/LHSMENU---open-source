/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.network.NetworkRegistry
 *  net.minecraftforge.network.PacketDistributor
 *  net.minecraftforge.network.simple.SimpleChannel
 */
package com.mainlhs.common.network;

import com.mainlhs.common.network.packet.SetRealisticModePacket;
import com.mainlhs.common.network.packet.SyncPlayerScalePacket;
import com.mainlhs.common.realistic.RealisticModeData;
import com.mainlhs.common.scale.PlayerScaleData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class NetworkHandler {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel((ResourceLocation)new ResourceLocation("mainlhs", "sync"), () -> "1", "1"::equals, "1"::equals);
    private static int packetId = 0;
    private static boolean registered;

    private NetworkHandler() {
    }

    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        CHANNEL.registerMessage(packetId++, SyncPlayerScalePacket.class, SyncPlayerScalePacket::encode, SyncPlayerScalePacket::decode, SyncPlayerScalePacket::handle);
        CHANNEL.registerMessage(packetId++, SetRealisticModePacket.class, SetRealisticModePacket::encode, SetRealisticModePacket::decode, SetRealisticModePacket::handle);
    }

    public static void syncToAll(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), (Object)new SyncPlayerScalePacket(player.m_20148_(), PlayerScaleData.getSize((Player)player)));
    }

    public static void syncToPlayer(ServerPlayer viewer, ServerPlayer subject) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> viewer), (Object)new SyncPlayerScalePacket(subject.m_20148_(), PlayerScaleData.getSize((Player)subject)));
    }

    public static void syncRealisticToPlayer(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), (Object)new SetRealisticModePacket(RealisticModeData.isEnabled((Player)player)));
    }
}

