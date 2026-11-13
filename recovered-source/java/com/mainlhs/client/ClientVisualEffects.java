/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.PostChain
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.mainlhs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="mainlhs", bus=Mod.EventBusSubscriber.Bus.FORGE, value={Dist.CLIENT})
public final class ClientVisualEffects {
    private ClientVisualEffects() {
    }

    public static void refresh() {
        Minecraft minecraft = Minecraft.m_91087_();
        if (minecraft == null) {
            return;
        }
        PostChain current = minecraft.f_91063_.m_109149_();
        if (current != null && current.m_110022_() != null && current.m_110022_().contains("lhs_graphics")) {
            minecraft.f_91063_.m_109086_();
        }
    }
}

