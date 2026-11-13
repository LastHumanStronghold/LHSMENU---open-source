/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractSliderButton
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 */
package com.mainlhs.client.screen;

import com.mainlhs.client.screen.MainMenuScreen;
import com.mainlhs.client.sound.ModSounds;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;

public class CustomSlider
extends AbstractSliderButton {
    private final Consumer<Double> onValueChanged;
    private final String prefix;
    private final boolean percent;
    private final double minValue;
    private final double maxValue;
    private float animationHover = 0.0f;
    private float animationAlpha = 1.0f;
    private float slideOffset = 0.0f;
    private boolean hoverSoundPlayed = false;
    private long lastRenderTime = System.currentTimeMillis();

    public CustomSlider(int x, int y, int width, int height, String prefix, double initialValue, boolean percent, double minValue, double maxValue, Consumer<Double> onValueChanged) {
        super(x, y, width, height, (Component)Component.m_237119_(), initialValue);
        this.prefix = prefix;
        this.percent = percent;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.onValueChanged = onValueChanged;
        this.m_5695_();
    }

    public CustomSlider(int x, int y, int width, int height, String prefix, double initialValue, boolean percent, Consumer<Double> onValueChanged) {
        this(x, y, width, height, prefix, initialValue, percent, 0.0, 1.0, onValueChanged);
    }

    protected void m_5695_() {
        int mappedVal;
        Object valText = this.percent ? Math.round(this.f_93577_ * 100.0) + "%" : ((mappedVal = (int)Math.round(this.minValue + this.f_93577_ * (this.maxValue - this.minValue))) == 70 ? mappedVal + " (" + (MainMenuScreen.isRussian() ? "\u041d\u043e\u0440\u043c" : "Normal") + ")" : (mappedVal == 110 ? "Quake Pro" : String.valueOf(mappedVal)));
        this.m_93666_((Component)Component.m_237113_((String)(this.prefix + (String)valText)));
    }

    protected void m_5697_() {
        if (this.onValueChanged != null) {
            this.onValueChanged.accept(this.f_93577_);
        }
    }

    public void setAnimationAlpha(float alpha) {
        this.animationAlpha = Mth.m_14036_((float)alpha, (float)0.0f, (float)1.0f);
        this.m_93650_(this.animationAlpha);
        this.f_93623_ = this.f_93624_ = this.animationAlpha > 0.02f;
    }

    public void setSlideOffset(float offset) {
        this.slideOffset = offset;
    }

    public void m_87963_(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        if (this.animationAlpha < 0.02f) {
            return;
        }
        long now = System.currentTimeMillis();
        float dt = Math.min((float)(now - this.lastRenderTime) / 1000.0f, 0.1f);
        this.lastRenderTime = now;
        boolean hovered = this.m_198029_();
        if (hovered && !this.hoverSoundPlayed) {
            Minecraft.m_91087_().m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_119755_((SoundEvent)((SoundEvent)ModSounds.BUTTON_HOVER.get()), (float)0.3f, (float)1.05f));
            this.hoverSoundPlayed = true;
        } else if (!hovered) {
            this.hoverSoundPlayed = false;
        }
        float targetHover = hovered ? 1.0f : 0.0f;
        this.animationHover += (targetHover - this.animationHover) * Math.min(1.0f, dt * 10.0f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        int x = this.m_252754_();
        int y = this.m_252907_() + (int)this.slideOffset;
        int w = this.m_5711_();
        int h = this.m_93694_();
        int bgAlpha = (int)((80.0f + this.animationHover * 50.0f) * this.animationAlpha);
        gfx.m_280509_(x, y, x + w, y + h, bgAlpha << 24 | 0xE0507);
        int trackMargin = 3;
        int trackX = x + trackMargin;
        int trackY = y + trackMargin;
        int trackW = w - trackMargin * 2;
        int trackH = h - trackMargin * 2;
        int fillW = (int)(this.f_93577_ * (double)trackW);
        gfx.m_280509_(trackX, trackY, trackX + trackW, trackY + trackH, (int)(60.0f * this.animationAlpha) << 24 | 0x1A090C);
        if (fillW > 0) {
            int fillAlpha = (int)((160.0f + this.animationHover * 60.0f) * this.animationAlpha);
            gfx.m_280509_(trackX, trackY, trackX + fillW, trackY + trackH, fillAlpha << 24 | 0x8C161E);
            gfx.m_280509_(trackX, trackY, trackX + fillW, trackY + 1, (int)(200.0f * this.animationAlpha) << 24 | 0xFF4D5A);
        }
        int handleW = 4;
        int handleX = trackX + fillW - handleW / 2;
        handleX = Mth.m_14045_((int)handleX, (int)trackX, (int)(trackX + trackW - handleW));
        int handleAlpha = (int)(255.0f * this.animationAlpha);
        int handleRgb = hovered ? 0xFF5566 : 14496576;
        gfx.m_280509_(handleX, y + 1, handleX + handleW, y + h - 1, handleAlpha << 24 | handleRgb);
        gfx.m_280509_(handleX + 1, y + 2, handleX + handleW - 1, y + h - 2, handleAlpha << 24 | 0xFFFFFF);
        int borderAlpha = (int)((45.0f + this.animationHover * 80.0f) * this.animationAlpha);
        int borderColor = borderAlpha << 24 | 0x66181E;
        gfx.m_280509_(x, y, x + w, y + 1, borderColor);
        gfx.m_280509_(x, y + h - 1, x + w, y + h, borderColor);
        gfx.m_280509_(x, y, x + 1, y + h, borderColor);
        gfx.m_280509_(x + w - 1, y, x + w, y + h, borderColor);
        Font font = Minecraft.m_91087_().f_91062_;
        int textAlpha = (int)((190.0f + this.animationHover * 65.0f) * this.animationAlpha);
        int textColor = hovered ? 0xFFFFFF : 14669528;
        int textY = y + (h - 8) / 2;
        gfx.m_280653_(font, this.m_6035_(), x + w / 2, textY, Mth.m_14045_((int)textAlpha, (int)0, (int)255) << 24 | textColor);
    }

    public void m_7435_(SoundManager soundManager) {
        soundManager.m_120367_((SoundInstance)SimpleSoundInstance.m_119755_((SoundEvent)((SoundEvent)ModSounds.BUTTON_CLICK.get()), (float)0.92f, (float)1.0f));
    }
}

