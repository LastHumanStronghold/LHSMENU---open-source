/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.RenderPlayerEvent$Post
 *  net.minecraftforge.client.event.RenderPlayerEvent$Pre
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.mainlhs.client;

import com.mainlhs.common.scale.PlayerScaleData;
import com.mainlhs.common.scale.ScaleMath;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="mainlhs", bus=Mod.EventBusSubscriber.Bus.FORGE, value={Dist.CLIENT})
public final class ClientScaleEvents {
    private ClientScaleEvents() {
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        float sizeParam = PlayerScaleData.getSize(player);
        if (Math.abs(sizeParam - 1.0f) < 1.0E-4f) {
            return;
        }
        float factor = ScaleMath.getScaleFactor(sizeParam);
        PoseStack poseStack = event.getPoseStack();
        poseStack.m_85841_(factor, factor, factor);
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();
        float sizeParam = PlayerScaleData.getSize(player);
        if (Math.abs(sizeParam - 1.0f) < 1.0E-4f) {
            return;
        }
        float factor = 1.0f / ScaleMath.getScaleFactor(sizeParam);
        PoseStack poseStack = event.getPoseStack();
        poseStack.m_85841_(factor, factor, factor);
    }
}

