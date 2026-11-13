/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.util.Mth
 */
package com.mainlhs.client.loading;

import com.mainlhs.client.screen.MainMenuScreen;
import com.mainlhs.client.screen.MenuBackgroundRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public final class LoadingVisuals {
    private LoadingVisuals() {
    }

    public static void renderFullScreen(GuiGraphics gfx, Font font, int width, int height, float time, String status, float progress) {
        LoadingVisuals.renderScreen(gfx, font, width, height, time, status);
    }

    public static void renderOverlay(GuiGraphics gfx, Font font, int width, int height, float time, String status, float progress, boolean showProgress, int bottomFadeHeight) {
        LoadingVisuals.renderScreen(gfx, font, width, height, time, status);
    }

    private static void renderScreen(GuiGraphics gfx, Font font, int width, int height, float time, String status) {
        MenuBackgroundRenderer.render(gfx, width, height, time, 1.0f);
        int cx = width / 2;
        int cy = height / 2 - 14;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        String displayStatus = LoadingVisuals.formatStatus(status, time);
        int glowColor = -1711330763;
        int textColor = 0xFFF2F5;
        gfx.m_280137_(font, displayStatus, cx, cy + 1, glowColor);
        gfx.m_280137_(font, displayStatus, cx, cy, textColor);
        LoadingVisuals.renderPulsingDots(gfx, cx, cy + 14, time);
    }

    private static void renderPulsingDots(GuiGraphics gfx, int cx, int y, float time) {
        int dotCount = 5;
        int gap = 8;
        int startX = cx - (dotCount - 1) * gap / 2;
        for (int i = 0; i < dotCount; ++i) {
            float phase = time * 5.0f - (float)i * 0.8f;
            float pulse = (float)(Math.sin(phase) * 0.5 + 0.5);
            int alpha = (int)(40.0f + pulse * 215.0f);
            int color = Mth.m_14045_((int)alpha, (int)0, (int)255) << 24 | 0xFF3545;
            int x = startX + i * gap;
            gfx.m_280509_(x - 1, y - 1, x + 2, y + 2, color);
        }
    }

    private static String formatStatus(String status, float time) {
        if (status == null || status.isBlank()) {
            int dots = (int)(time * 2.8f) % 3 + 1;
            return (MainMenuScreen.isRussian() ? "\u041f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435" : "Connecting") + ".".repeat(dots);
        }
        String lower = status.toLowerCase();
        int dots = (int)(time * 2.8f) % 3 + 1;
        String dotStr = ".".repeat(dots);
        if (lower.contains("\u043f\u043e\u0434\u043a\u043b\u044e\u0447") || lower.contains("connect")) {
            return (MainMenuScreen.isRussian() ? "\u041f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435" : "Connecting") + dotStr;
        }
        if (lower.contains("\u0434\u0430\u043d\u043d\u044b\u0445") || lower.contains("receiving")) {
            return (MainMenuScreen.isRussian() ? "\u041f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u0435 \u0434\u0430\u043d\u043d\u044b\u0445" : "Receiving data") + dotStr;
        }
        if (lower.contains("\u043f\u043e\u0441\u0442\u0440\u043e\u0435\u043d") || lower.contains("building") || lower.contains("\u043c\u0438\u0440")) {
            return (MainMenuScreen.isRussian() ? "\u041f\u043e\u0441\u0442\u0440\u043e\u0435\u043d\u0438\u0435 \u043c\u0438\u0440\u0430" : "Building world") + dotStr;
        }
        if (lower.contains("\u0440\u0435\u0441\u0443\u0440\u0441") || lower.contains("resource")) {
            return (MainMenuScreen.isRussian() ? "\u041f\u043e\u0434\u0433\u043e\u0442\u043e\u0432\u043a\u0430 \u0440\u0435\u0441\u0443\u0440\u0441\u043e\u0432" : "Preparing resources") + dotStr;
        }
        return status;
    }
}

