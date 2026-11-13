/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityDimensions
 *  net.minecraft.world.entity.Pose
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.event.RegisterCommandsEvent
 *  net.minecraftforge.event.entity.EntityEvent$Size
 *  net.minecraftforge.event.entity.player.PlayerEvent$Clone
 *  net.minecraftforge.event.entity.player.PlayerEvent$PlayerLoggedInEvent
 *  net.minecraftforge.event.entity.player.PlayerEvent$StartTracking
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.mainlhs.server;

import com.mainlhs.common.network.NetworkHandler;
import com.mainlhs.common.realistic.RealisticModeData;
import com.mainlhs.common.scale.PlayerScaleData;
import com.mainlhs.common.scale.ScaleMath;
import com.mainlhs.server.command.GenericScaleCommand;
import com.mainlhs.server.command.RealisticCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="mainlhs", bus=Mod.EventBusSubscriber.Bus.FORGE)
public final class ScaleEvents {
    private ScaleEvents() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        GenericScaleCommand.register((CommandDispatcher<CommandSourceStack>)event.getDispatcher());
        RealisticCommand.register((CommandDispatcher<CommandSourceStack>)event.getDispatcher());
    }

    @SubscribeEvent
    public static void onEntitySize(EntityEvent.Size event) {
        Player player;
        Entity entity = event.getEntity();
        if (!(entity instanceof Player) || !(player = (Player)entity).isAddedToWorld()) {
            return;
        }
        float sizeParam = PlayerScaleData.getSize(player);
        if (Math.abs(sizeParam - 1.0f) < 1.0E-4f) {
            return;
        }
        float factor = ScaleMath.getScaleFactor(sizeParam);
        EntityDimensions baseSize = ScaleEvents.getBasePlayerDimensions(event.getPose());
        EntityDimensions scaled = baseSize.m_20390_(factor, factor);
        event.setNewSize(scaled, true);
        float baseEyeHeight = player.m_6431_(event.getPose(), baseSize);
        event.setNewEyeHeight(baseEyeHeight * factor);
    }

    private static EntityDimensions getBasePlayerDimensions(Pose pose) {
        return switch (pose) {
            case Pose.CROUCHING -> EntityDimensions.m_20395_((float)Player.f_36088_.f_20377_, (float)1.5f);
            case Pose.SWIMMING, Pose.FALL_FLYING, Pose.SPIN_ATTACK -> EntityDimensions.m_20395_((float)0.6f, (float)0.6f);
            default -> Player.f_36088_;
        };
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        float size = PlayerScaleData.getSize(event.getOriginal());
        if (Math.abs(size - 1.0f) < 1.0E-4f) {
            RealisticModeData.setEnabled(event.getEntity(), RealisticModeData.isEnabled(event.getOriginal()));
            return;
        }
        PlayerScaleData.setSize(event.getEntity(), size);
        RealisticModeData.setEnabled(event.getEntity(), RealisticModeData.isEnabled(event.getOriginal()));
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer player2 = (ServerPlayer)player;
        NetworkHandler.syncToAll(player2);
        NetworkHandler.syncRealisticToPlayer(player2);
        for (ServerPlayer other : player2.f_8924_.m_6846_().m_11314_()) {
            if (other == player2) continue;
            NetworkHandler.syncToPlayer(player2, other);
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity entity = event.getTarget();
        if (entity instanceof ServerPlayer) {
            ServerPlayer target = (ServerPlayer)entity;
            entity = event.getEntity();
            if (entity instanceof ServerPlayer) {
                ServerPlayer tracker = (ServerPlayer)entity;
                NetworkHandler.syncToPlayer(tracker, target);
            }
        }
    }
}

