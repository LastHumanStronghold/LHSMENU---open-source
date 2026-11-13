/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.narration.NarrationElementOutput
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.mainlhs.client.screen;

import com.mainlhs.client.screen.MainMenuScreen;
import com.mainlhs.client.sound.ModSounds;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class LanguageToggleWidget
extends AbstractWidget {
    private final Runnable onLanguageChanged;
    private float fadeIn = 0.0f;
    private float slideAnim = MainMenuScreen.isRussian() ? 0.0f : 1.0f;
    private float pulseAnim = 0.0f;
    private boolean ruHoverPlayed = false;
    private boolean enHoverPlayed = false;
    private long lastRenderTime = System.currentTimeMillis();

    public LanguageToggleWidget(int x, int y, Runnable onLanguageChanged) {
        super(x, y, 68, 18, (Component)Component.m_237119_());
        this.onLanguageChanged = onLanguageChanged;
    }

    public void setFadeIn(float fadeIn) {
        this.fadeIn = Mth.m_14036_((float)fadeIn, (float)0.0f, (float)1.0f);
    }

    public void tick() {
    }

    protected void m_87963_(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        boolean enHovered;
        if (this.fadeIn <= 0.02f) {
            return;
        }
        long now = System.currentTimeMillis();
        float dt = Math.min((float)(now - this.lastRenderTime) / 1000.0f, 0.1f);
        this.lastRenderTime = now;
        float targetSlide = MainMenuScreen.isRussian() ? 0.0f : 1.0f;
        this.slideAnim += (targetSlide - this.slideAnim) * Math.min(1.0f, dt * 14.0f);
        if (this.pulseAnim > 0.0f) {
            this.pulseAnim = Math.max(0.0f, this.pulseAnim - dt * 4.0f);
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int x = this.m_252754_();
        int y = this.m_252907_();
        int w = this.m_5711_();
        int h = this.m_93694_();
        int halfW = (w - 4) / 2;
        boolean ruHovered = mouseX >= x && mouseX < x + w / 2 && mouseY >= y && mouseY < y + h;
        boolean bl = enHovered = mouseX >= x + w / 2 && mouseX < x + w && mouseY >= y && mouseY < y + h;
        if (ruHovered && !this.ruHoverPlayed) {
            Minecraft.m_91087_().m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_119755_((SoundEvent)((SoundEvent)ModSounds.BUTTON_HOVER.get()), (float)0.3f, (float)1.1f));
            this.ruHoverPlayed = true;
        } else if (!ruHovered) {
            this.ruHoverPlayed = false;
        }
        if (enHovered && !this.enHoverPlayed) {
            Minecraft.m_91087_().m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_119755_((SoundEvent)((SoundEvent)ModSounds.BUTTON_HOVER.get()), (float)0.3f, (float)1.1f));
            this.enHoverPlayed = true;
        } else if (!enHovered) {
            this.enHoverPlayed = false;
        }
        int bgAlpha = (int)(120.0f * this.fadeIn);
        gfx.m_280509_(x, y, x + w, y + h, bgAlpha << 24 | 0xA0406);
        int borderAlpha = (int)(60.0f * this.fadeIn);
        int borderColor = borderAlpha << 24 | 0x66181E;
        gfx.m_280509_(x, y, x + w, y + 1, borderColor);
        gfx.m_280509_(x, y + h - 1, x + w, y + h, borderColor);
        gfx.m_280509_(x, y, x + 1, y + h, borderColor);
        gfx.m_280509_(x + w - 1, y, x + w, y + h, borderColor);
        int indicatorX = x + 2 + (int)(this.slideAnim * (float)halfW);
        int indAlpha = (int)((160.0f + this.pulseAnim * 80.0f) * this.fadeIn);
        indAlpha = Mth.m_14045_((int)indAlpha, (int)0, (int)255);
        int indColor = indAlpha << 24 | 0x8B1820;
        gfx.m_280509_(indicatorX, y + 2, indicatorX + halfW, y + h - 2, indColor);
        int glowLineAlpha = (int)(220.0f * this.fadeIn);
        gfx.m_280509_(indicatorX + 2, y + h - 3, indicatorX + halfW - 2, y + h - 2, glowLineAlpha << 24 | 0xFF4450);
        Font font = Minecraft.m_91087_().f_91062_;
        int textY = y + (h - 8) / 2;
        boolean isRu = MainMenuScreen.isRussian();
        int ruTextColor = this.colorForText(isRu, ruHovered, this.fadeIn);
        int enTextColor = this.colorForText(!isRu, enHovered, this.fadeIn);
        int ruCenterX = x + 2 + halfW / 2;
        int enCenterX = x + 2 + halfW + halfW / 2;
        gfx.m_280137_(font, "RU", ruCenterX, textY, ruTextColor);
        gfx.m_280137_(font, "EN", enCenterX, textY, enTextColor);
    }

    private int colorForText(boolean active, boolean hovered, float fade) {
        int alpha = (int)((float)(active ? 255 : (hovered ? 210 : 120)) * fade);
        int rgb = active ? 0xFFFFFF : (hovered ? 0xDDDDDD : 0x888888);
        return Mth.m_14045_((int)alpha, (int)0, (int)255) << 24 | rgb;
    }

    public void m_5716_(double mouseX, double mouseY) {
        int halfW = this.m_5711_() / 2;
        if (mouseX >= (double)this.m_252754_() && mouseX < (double)(this.m_252754_() + halfW)) {
            if (!MainMenuScreen.isRussian()) {
                MainMenuScreen.setRussian(true);
                this.pulseAnim = 1.0f;
                Minecraft.m_91087_().m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_119755_((SoundEvent)((SoundEvent)ModSounds.BUTTON_CLICK.get()), (float)0.95f, (float)1.0f));
                if (this.onLanguageChanged != null) {
                    this.onLanguageChanged.run();
                }
            }
        } else if (mouseX >= (double)(this.m_252754_() + halfW) && mouseX < (double)(this.m_252754_() + this.m_5711_()) && MainMenuScreen.isRussian()) {
            MainMenuScreen.setRussian(false);
            this.pulseAnim = 1.0f;
            Minecraft.m_91087_().m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_119755_((SoundEvent)((SoundEvent)ModSounds.BUTTON_CLICK.get()), (float)0.95f, (float)1.0f));
            if (this.onLanguageChanged != null) {
                this.onLanguageChanged.run();
            }
        }
    }

    protected void m_168797_(NarrationElementOutput output) {
        this.m_168802_(output);
    }
}

