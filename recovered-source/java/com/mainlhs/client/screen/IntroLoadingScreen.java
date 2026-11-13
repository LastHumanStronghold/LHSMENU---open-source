/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.mainlhs.client.screen;

import com.mainlhs.client.loading.LoadingVisuals;
import com.mainlhs.client.screen.MainMenuScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class IntroLoadingScreen
extends Screen {
    private float time;
    private float progress;
    private boolean switched;

    public IntroLoadingScreen() {
        super((Component)Component.m_237113_((String)"Loading"));
    }

    public void m_86600_() {
        this.time += 0.05f;
        this.progress = Math.min(this.progress + 0.018f, 1.0f);
        if (!this.switched && this.progress >= 1.0f && this.f_96541_ != null) {
            this.switched = true;
            this.f_96541_.m_91152_((Screen)new MainMenuScreen(true));
        }
    }

    public void m_88315_(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        float visualTime = this.time + partialTick * 0.05f;
        String status = MainMenuScreen.isRussian() ? "\u0438\u043d\u0438\u0446\u0438\u0430\u043b\u0438\u0437\u0430\u0446\u0438\u044f \u0438\u043d\u0442\u0435\u0440\u0444\u0435\u0439\u0441\u0430" : "initializing interface";
        LoadingVisuals.renderFullScreen(gfx, this.f_96547_, this.f_96543_, this.f_96544_, visualTime, status, IntroLoadingScreen.ease(this.progress));
    }

    private static float ease(float value) {
        float inverse = 1.0f - value;
        return 1.0f - inverse * inverse * inverse;
    }

    public boolean m_6913_() {
        return false;
    }
}

