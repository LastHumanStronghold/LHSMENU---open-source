/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.util.Mth
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.RenderGuiOverlayEvent$Post
 *  net.minecraftforge.client.event.ViewportEvent$ComputeCameraAngles
 *  net.minecraftforge.client.event.ViewportEvent$ComputeFov
 *  net.minecraftforge.client.gui.overlay.VanillaGuiOverlay
 *  net.minecraftforge.event.TickEvent$ClientTickEvent
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 */
package com.mainlhs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid="mainlhs", bus=Mod.EventBusSubscriber.Bus.FORGE, value={Dist.CLIENT})
public final class ClientRealisticMode {
    private static boolean enabled;
    private static float strength;
    private static float speedAmount;

    private ClientRealisticMode() {
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft minecraft = Minecraft.m_91087_();
        boolean inWorld = minecraft != null && minecraft.f_91073_ != null && minecraft.f_91074_ != null;
        float targetStrength = enabled && inWorld ? 1.0f : 0.0f;
        if ((strength += (targetStrength - strength) * 0.16f) < 0.001f) {
            strength = 0.0f;
        }
        if (!inWorld) {
            speedAmount = 0.0f;
            return;
        }
        double horizontalSpeed = minecraft.f_91074_.m_20184_().m_165924_();
        float targetSpeed = Mth.m_14036_((float)((float)horizontalSpeed * 8.0f), (float)0.0f, (float)1.0f);
        speedAmount += (targetSpeed - speedAmount) * 0.2f;
    }

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.m_91087_();
        if (!ClientRealisticMode.shouldRender(minecraft)) {
            return;
        }
        LocalPlayer player = minecraft.f_91074_;
        float tick = (float)player.f_19797_ + (float)event.getPartialTick();
        float move = Math.max(speedAmount, player.m_20142_() ? 0.65f : 0.0f);
        float breathing = (float)Math.sin(tick * 0.16f) * 0.28f;
        float runRoll = (float)Math.sin(tick * 0.72f) * move * 1.65f;
        float runPitch = (float)Math.sin(tick * 0.88f + 1.1f) * move * 0.75f;
        float runYaw = (float)Math.sin(tick * 0.58f) * move * 0.5f;
        event.setRoll(event.getRoll() + (breathing + runRoll) * strength);
        event.setPitch(event.getPitch() + runPitch * strength);
        event.setYaw(event.getYaw() + runYaw * strength);
    }

    @SubscribeEvent
    public static void onComputeFov(ViewportEvent.ComputeFov event) {
        Minecraft minecraft = Minecraft.m_91087_();
        if (!ClientRealisticMode.shouldRender(minecraft)) {
            return;
        }
        event.setFOV(event.getFOV() + (double)((7.0f + speedAmount * 4.0f) * strength));
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (!event.getOverlay().id().equals((Object)VanillaGuiOverlay.HOTBAR.id())) {
            return;
        }
        Minecraft minecraft = Minecraft.m_91087_();
        if (!ClientRealisticMode.shouldRender(minecraft)) {
            return;
        }
        GuiGraphics gfx = event.getGuiGraphics();
        int width = event.getWindow().m_85445_();
        int height = event.getWindow().m_85446_();
        int alpha = (int)(strength * 255.0f);
        int barAlpha = (int)(strength * 92.0f);
        gfx.m_280509_(0, 0, width, Math.max(12, height / 18), ClientRealisticMode.argb(barAlpha, 0));
        gfx.m_280509_(0, height - Math.max(12, height / 18), width, height, ClientRealisticMode.argb(barAlpha, 0));
        gfx.m_280509_(0, 0, Math.max(10, width / 42), height, ClientRealisticMode.argb((int)(strength * 38.0f), 0));
        gfx.m_280509_(width - Math.max(10, width / 42), 0, width, height, ClientRealisticMode.argb((int)(strength * 38.0f), 0));
        int textColor = ClientRealisticMode.argb(alpha, 0xE8E8E8);
        int accentColor = ClientRealisticMode.argb(alpha, 0xFF3030);
        boolean recBlink = minecraft.f_91074_.f_19797_ / 10 % 2 == 0;
        gfx.m_280056_(minecraft.f_91062_, "BODYCAM", 12, 10, textColor, false);
        gfx.m_280056_(minecraft.f_91062_, "MAINLHS-01", 12, 22, ClientRealisticMode.argb((int)((float)alpha * 0.65f), 0xB8B8B8), false);
        if (recBlink) {
            gfx.m_280509_(width - 54, 12, width - 48, 18, accentColor);
        }
        gfx.m_280056_(minecraft.f_91062_, "REC", width - 44, 10, accentColor, false);
        int centerX = width / 2;
        int centerY = height / 2;
        int lineColor = ClientRealisticMode.argb((int)((float)alpha * 0.22f), 0xFFFFFF);
        gfx.m_280509_(centerX - 18, centerY, centerX - 6, centerY + 1, lineColor);
        gfx.m_280509_(centerX + 6, centerY, centerX + 18, centerY + 1, lineColor);
        gfx.m_280509_(centerX, centerY - 18, centerX + 1, centerY - 6, lineColor);
        gfx.m_280509_(centerX, centerY + 6, centerX + 1, centerY + 18, lineColor);
    }

    private static boolean shouldRender(Minecraft minecraft) {
        return strength > 0.001f && minecraft != null && minecraft.f_91074_ != null && minecraft.f_91073_ != null && minecraft.f_91066_.m_92176_().m_90612_();
    }

    private static int argb(int alpha, int rgb) {
        return Mth.m_14045_((int)alpha, (int)0, (int)255) << 24 | rgb & 0xFFFFFF;
    }
}

