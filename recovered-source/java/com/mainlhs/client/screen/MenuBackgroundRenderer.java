/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.util.Mth
 */
package com.mainlhs.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

public final class MenuBackgroundRenderer {
    private static final long START_TIME = System.currentTimeMillis();
    private static final int PARTICLE_COUNT = 70;

    private MenuBackgroundRenderer() {
    }

    public static float getTime() {
        return (float)(System.currentTimeMillis() - START_TIME) / 1000.0f;
    }

    public static void render(GuiGraphics gfx, int width, int height, float time, float fade) {
        MenuBackgroundRenderer.render(gfx, width, height, time, fade, width / 2, height / 2);
    }

    public static void render(GuiGraphics gfx, int width, int height, float time, float fade, int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float clampedFade = Mth.m_14036_((float)fade, (float)0.0f, (float)1.0f);
        float px = ((float)mouseX / (float)Math.max(1, width) - 0.5f) * 20.0f;
        float py = ((float)mouseY / (float)Math.max(1, height) - 0.5f) * 14.0f;
        gfx.m_280509_(0, 0, width, height, -16645887);
        MenuBackgroundRenderer.renderRadialGradient(gfx, width, height, time, clampedFade, px, py);
        MenuBackgroundRenderer.renderEmbers(gfx, width, height, time, clampedFade, px, py);
        MenuBackgroundRenderer.renderCornerAccents(gfx, width, height, clampedFade);
    }

    private static void renderRadialGradient(GuiGraphics gfx, int width, int height, float time, float fade, float px, float py) {
        if (fade <= 0.01f) {
            return;
        }
        float cx = (float)width * 0.5f + px * 0.4f;
        float cy = (float)height * 0.42f + py * 0.4f;
        float pulse = (float)(Math.sin(time * 0.8f) * 0.5 + 0.5);
        float radiusX = (float)width * 0.7f;
        float radiusY = (float)height * 0.65f;
        for (int y = 0; y < height; ++y) {
            float dy = ((float)y - cy) / radiusY;
            float dy2 = dy * dy;
            if (dy2 >= 1.0f) continue;
            float horizFactor = (float)Math.sqrt(1.0f - dy2);
            int halfSpan = (int)(radiusX * horizFactor);
            int left = (int)cx - halfSpan;
            int right = (int)cx + halfSpan;
            left = Math.max(0, left);
            if ((right = Math.min(width, right)) <= left) continue;
            float vertDist = Math.abs(dy);
            float vertFade = 1.0f - vertDist;
            vertFade = vertFade * vertFade * (3.0f - 2.0f * vertFade);
            float intensity = vertFade * (0.28f + pulse * 0.08f) * fade;
            int red = (int)(intensity * 255.0f);
            int green = (int)(intensity * 92.0f);
            int blue = (int)(intensity * 12.0f);
            int alpha = (int)(intensity * 255.0f);
            red = Mth.m_14045_((int)red, (int)0, (int)255);
            green = Mth.m_14045_((int)green, (int)0, (int)255);
            blue = Mth.m_14045_((int)blue, (int)0, (int)255);
            if ((alpha = Mth.m_14045_((int)alpha, (int)0, (int)255)) <= 0) continue;
            int color = alpha << 24 | red << 16 | green << 8 | blue;
            gfx.m_280509_(left, y, right, y + 1, color);
        }
    }

    private static void renderEmbers(GuiGraphics gfx, int width, int height, float time, float fade, float px, float py) {
        if (fade <= 0.01f) {
            return;
        }
        float totalH = (float)height + 40.0f;
        float totalW = (float)width + 60.0f;
        for (int i = 0; i < 70; ++i) {
            float rx = MenuBackgroundRenderer.fract((double)(i + 1) * 0.618033988749895);
            float ry = MenuBackgroundRenderer.fract((double)(i + 1) * 0.754877666246693);
            float rd = MenuBackgroundRenderer.fract((double)(i + 1) * 0.569840296317204);
            float depth = 0.2f + rd * 0.8f;
            float speedY = 12.0f + rd * 26.0f;
            float windDrift = (float)Math.sin((float)(i + 1) * 13.37f) * (3.0f + rd * 7.0f);
            float startY = ry * totalH;
            float travelY = (startY + time * speedY) % totalH;
            float y = (float)height + 20.0f - travelY + py * depth * 0.7f;
            float baseX = rx * totalW;
            float swayFreq = 0.55f + rd * 0.6f;
            float swayAmp = 12.0f + rd * 14.0f;
            float sway = (float)Math.sin(time * swayFreq + (float)i * 2.39f) * swayAmp;
            float travelX = (baseX + sway + time * windDrift) % totalW;
            if (travelX < 0.0f) {
                travelX += totalW;
            }
            float x = travelX - 30.0f + px * depth * 0.7f;
            if (y < -10.0f || y > (float)(height + 10) || x < -10.0f || x > (float)(width + 10)) continue;
            float flicker = (float)(Math.sin(time * 3.8f + (float)i * 2.1f) * (double)0.3f + (double)0.7f);
            int alpha = (int)((35.0f + depth * 140.0f) * flicker * fade);
            alpha = Mth.m_14045_((int)alpha, (int)0, (int)255);
            int size = depth > 0.8f ? 2 : 1;
            int colorType = i % 4;
            int rgb = colorType == 0 ? 0xFF9A32 : (colorType == 1 ? 0xFF6A1A : (colorType == 2 ? 0xFFC05A : 0xD94A12));
            int color = alpha << 24 | rgb;
            gfx.m_280509_((int)x, (int)y, (int)x + size, (int)y + size, color);
            if (!(depth > 0.8f) || alpha <= 60) continue;
            int haloAlpha = alpha / 4;
            gfx.m_280509_((int)x - 1, (int)y - 1, (int)x + size + 1, (int)y + size + 1, haloAlpha << 24 | rgb);
        }
    }

    private static float fract(double v) {
        return (float)(v - Math.floor(v));
    }

    private static void renderCornerAccents(GuiGraphics gfx, int width, int height, float fade) {
        int alpha = (int)(30.0f * fade);
        if (alpha <= 0) {
            return;
        }
        int color = alpha << 24 | 0x66300D;
        int margin = 12;
        int len = 16;
        gfx.m_280509_(margin, margin, margin + len, margin + 1, color);
        gfx.m_280509_(margin, margin, margin + 1, margin + len, color);
        gfx.m_280509_(width - margin - len, margin, width - margin, margin + 1, color);
        gfx.m_280509_(width - margin - 1, margin, width - margin, margin + len, color);
        gfx.m_280509_(margin, height - margin - 1, margin + len, height - margin, color);
        gfx.m_280509_(margin, height - margin - len, margin + 1, height - margin, color);
        gfx.m_280509_(width - margin - len, height - margin - 1, width - margin, height - margin, color);
        gfx.m_280509_(width - margin - 1, height - margin - len, width - margin, height - margin, color);
    }
}

