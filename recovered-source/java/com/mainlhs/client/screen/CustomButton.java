/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.network.chat.Component
 *  net.minecraft.util.Mth
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.mainlhs.client.screen;

import com.mainlhs.client.screen.MenuBackgroundRenderer;
import com.mainlhs.client.sound.ModSounds;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class CustomButton
extends Button {
    private final boolean dangerStyle;
    private boolean primaryStyle = false;
    private float hoverAnim = 0.0f;
    private float clickAnim = 0.0f;
    private float animAlpha = 1.0f;
    private float slideOffset = 0.0f;
    private boolean hoverSoundPlayed = false;
    private boolean disableHoverEffects = false;
    private long lastRenderTime = System.currentTimeMillis();

    public CustomButton(int x, int y, int width, int height, Component text, Button.OnPress onPress, boolean dangerStyle) {
        super(x, y, width, height, text, btn -> {
            if (btn instanceof CustomButton) {
                CustomButton custom = (CustomButton)btn;
                custom.clickAnim = 1.0f;
            }
            onPress.m_93750_(btn);
        }, f_252438_);
        this.dangerStyle = dangerStyle;
    }

    public CustomButton setPrimary(boolean primary) {
        this.primaryStyle = primary;
        return this;
    }

    public void setAnimationAlpha(float alpha) {
        this.animAlpha = Mth.m_14036_((float)alpha, (float)0.0f, (float)1.0f);
        this.f_93623_ = this.f_93624_ = this.animAlpha > 0.02f;
    }

    public void setSlideOffset(float offset) {
        this.slideOffset = offset;
    }

    public void setDisableHoverEffects(boolean disable) {
        this.disableHoverEffects = disable;
    }

    protected void m_87963_(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        if (!this.f_93624_ || this.animAlpha <= 0.02f) {
            return;
        }
        long now = System.currentTimeMillis();
        float dt = Math.min((float)(now - this.lastRenderTime) / 1000.0f, 0.1f);
        this.lastRenderTime = now;
        float time = MenuBackgroundRenderer.getTime();
        boolean hovered = this.m_274382_() && !this.disableHoverEffects;
        float targetHover = hovered ? 1.0f : 0.0f;
        float hoverSpeed = hovered ? 12.0f : 8.0f;
        this.hoverAnim += (targetHover - this.hoverAnim) * Math.min(1.0f, dt * hoverSpeed);
        if (this.clickAnim > 0.0f) {
            this.clickAnim = Math.max(0.0f, this.clickAnim - dt * 6.0f);
        }
        if (hovered && !this.hoverSoundPlayed) {
            ModSounds.playHoverSound();
            this.hoverSoundPlayed = true;
        } else if (!hovered) {
            this.hoverSoundPlayed = false;
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int x = this.m_252754_();
        int y = this.m_252907_() + (int)this.slideOffset + (this.clickAnim > 0.05f ? 1 : 0);
        int w = this.m_5711_();
        int h = this.m_93694_();
        float pulse = (float)(Math.sin(time * 3.2f) * 0.5 + 0.5);
        if (this.primaryStyle || this.hoverAnim > 0.02f) {
            float strength = (this.primaryStyle ? 0.35f : 0.0f) + this.hoverAnim * 0.85f;
            int auraLayers = this.primaryStyle && hovered ? 3 : 2;
            for (int l = 1; l <= auraLayers; ++l) {
                int spread = l * 2;
                int auraAlpha = (int)(strength * (18.0f + pulse * 12.0f) / (float)l * this.animAlpha);
                int auraRgb = this.dangerStyle ? 0xAA1818 : (this.primaryStyle ? 14492978 : 8921125);
                gfx.m_280509_(x - spread, y - spread, x + w + spread, y + h + spread, Mth.m_14045_((int)auraAlpha, (int)0, (int)255) << 24 | auraRgb);
            }
        }
        int baseAlpha = (int)((140.0f + this.hoverAnim * 80.0f + (this.primaryStyle ? 35.0f : 0.0f)) * this.animAlpha);
        int baseRgb = this.dangerStyle ? (hovered ? 3803662 : 2098440) : (this.primaryStyle ? (hovered ? 4328980 : 2623504) : (hovered ? 3279888 : 1837072));
        gfx.m_280509_(x, y, x + w, y + h, Mth.m_14045_((int)baseAlpha, (int)0, (int)255) << 24 | baseRgb);
        if (this.primaryStyle && this.hoverAnim > 0.1f) {
            int sweepW = 35;
            int totalTravel = w + sweepW * 2;
            int sweepX = x + (int)(time * 160.0f) % totalTravel - sweepW;
            int startX = Math.max(x, sweepX);
            int endX = Math.min(x + w, sweepX + sweepW);
            if (endX > startX) {
                int shimmerAlpha = (int)(this.hoverAnim * 55.0f * this.animAlpha);
                gfx.m_280509_(startX, y + 1, endX, y + h - 1, shimmerAlpha << 24 | 0xFF8899);
            }
        }
        int barWidth = 3;
        int barAlpha = (int)((this.primaryStyle ? 230.0f + this.hoverAnim * 25.0f : 160.0f + this.hoverAnim * 95.0f) * this.animAlpha);
        int barRgb = this.dangerStyle ? (hovered ? 16728136 : 13379632) : (this.primaryStyle ? (hovered ? 16735594 : 15613256) : (hovered ? 16733536 : 12268864));
        int barHeight = Math.max(4, (int)((float)h * (this.primaryStyle ? 0.6f + this.hoverAnim * 0.4f : 0.4f + this.hoverAnim * 0.6f)));
        int barY = y + (h - barHeight) / 2;
        gfx.m_280509_(x, barY, x + barWidth, barY + barHeight, Mth.m_14045_((int)barAlpha, (int)0, (int)255) << 24 | barRgb);
        if (this.primaryStyle && this.hoverAnim > 0.15f) {
            int chevAlpha = (int)(this.hoverAnim * (220.0f + pulse * 35.0f) * this.animAlpha);
            int chevColor = Mth.m_14045_((int)chevAlpha, (int)0, (int)255) << 24 | 0xFF6677;
            int chevX = x + 10 + (int)(this.hoverAnim * 2.0f);
            int chevY = y + h / 2;
            gfx.m_280509_(chevX, chevY - 3, chevX + 1, chevY + 4, chevColor);
            gfx.m_280509_(chevX + 1, chevY - 2, chevX + 2, chevY + 3, chevColor);
            gfx.m_280509_(chevX + 2, chevY - 1, chevX + 3, chevY + 2, chevColor);
            gfx.m_280509_(chevX + 3, chevY, chevX + 4, chevY + 1, chevColor);
        }
        int borderAlpha = (int)((130.0f + this.hoverAnim * 125.0f + (this.primaryStyle ? 45.0f : 0.0f)) * this.animAlpha);
        int borderRgb = this.dangerStyle ? (hovered ? 0xFF3838 : 11149352) : (this.primaryStyle ? (hovered ? 16735598 : 14499402) : (hovered ? 14501970 : 0x883038));
        int borderColor = Mth.m_14045_((int)borderAlpha, (int)0, (int)255) << 24 | borderRgb;
        gfx.m_280509_(x, y, x + w, y + 1, borderColor);
        gfx.m_280509_(x, y + h - 1, x + w, y + h, borderColor);
        gfx.m_280509_(x, y, x + 1, y + h, borderColor);
        gfx.m_280509_(x + w - 1, y, x + w, y + h, borderColor);
        if (this.hoverAnim > 0.08f) {
            int cornerAlpha = (int)(this.hoverAnim * 245.0f * this.animAlpha);
            int cornerColor = Mth.m_14045_((int)cornerAlpha, (int)0, (int)255) << 24 | (this.dangerStyle ? 0xFF5555 : (this.primaryStyle ? 0xFF8899 : 0xFF6677));
            gfx.m_280509_(x + w - 5, y, x + w, y + 1, cornerColor);
            gfx.m_280509_(x + w - 1, y, x + w, y + 5, cornerColor);
            gfx.m_280509_(x, y + h - 1, x + 5, y + h, cornerColor);
            gfx.m_280509_(x, y + h - 5, x + 1, y + h, cornerColor);
        }
        if (hovered && (this.primaryStyle || this.hoverAnim > 0.5f)) {
            int lineAlpha = (int)(this.hoverAnim * 200.0f * this.animAlpha);
            int lineW = (int)((float)w * (0.4f + this.hoverAnim * 0.4f));
            int lineX = x + (w - lineW) / 2;
            gfx.m_280509_(lineX, y + h - 1, lineX + lineW, y + h, Mth.m_14045_((int)lineAlpha, (int)0, (int)255) << 24 | (this.primaryStyle ? 16735594 : 15615312));
        }
        Font font = this.font();
        int textShift = (int)(this.hoverAnim * (this.primaryStyle ? 4.0f : 3.0f));
        int textAlpha = (int)((240.0f + this.hoverAnim * 15.0f) * this.animAlpha);
        int textColor = this.dangerStyle ? (hovered ? 0xFFFFFF : 0xEECCCC) : (this.primaryStyle ? (hovered ? 0xFFFFFF : 0xFFEEF0) : (hovered ? 0xFFFFFF : 0xEEDDDF));
        int textX = x + w / 2 + textShift;
        int textY = y + (h - 8) / 2;
        if (this.hoverAnim > 0.15f || this.primaryStyle) {
            int shadowAlpha = (int)((this.primaryStyle ? 50.0f : 0.0f + this.hoverAnim * 110.0f) * this.animAlpha);
            int shadowColor = Mth.m_14045_((int)shadowAlpha, (int)0, (int)255) << 24 | (this.dangerStyle ? 0xBB0000 : (this.primaryStyle ? 11141144 : 0x880000));
            gfx.m_280653_(font, this.m_6035_(), textX + 1, textY + 1, shadowColor);
        }
        gfx.m_280653_(font, this.m_6035_(), textX, textY, Mth.m_14045_((int)textAlpha, (int)0, (int)255) << 24 | textColor);
    }

    private Font font() {
        return Minecraft.m_91087_().f_91062_;
    }

    public void m_7435_(SoundManager soundManager) {
        ModSounds.playClickSound();
    }
}

