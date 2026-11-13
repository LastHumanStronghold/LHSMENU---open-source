/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.util.Mth
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.mainlhs.client.screen;

import com.mainlhs.client.screen.CustomButton;
import com.mainlhs.client.screen.MainMenuScreen;
import com.mainlhs.client.screen.MenuBackgroundRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class UpdateInfoScreen
extends Screen {
    private final String info;
    private float time = 0.0f;
    private float fadeIn = 0.0f;

    public UpdateInfoScreen(String info) {
        super((Component)Component.m_237113_((String)"Updates"));
        this.info = info == null ? "No update info available" : info;
    }

    protected void m_7856_() {
        int cx = this.f_96543_ / 2;
        int y = this.f_96544_ / 2 + 50;
        this.m_142416_((GuiEventListener)new CustomButton(cx - 60, y, 120, 22, (Component)Component.m_237113_((String)(MainMenuScreen.isRussian() ? "\u0417\u0410\u041a\u0420\u042b\u0422\u042c" : "CLOSE")), btn -> this.f_96541_.m_91152_((Screen)new MainMenuScreen()), false));
    }

    public void m_86600_() {
        this.time += 0.05f;
        this.fadeIn = Math.min(this.fadeIn + 0.08f, 1.0f);
    }

    public void m_88315_(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        float renderTime = this.time + partialTick * 0.05f;
        MenuBackgroundRenderer.render(gfx, this.f_96543_, this.f_96544_, renderTime, this.fadeIn, mouseX, mouseY);
        Font font = this.f_96547_;
        int cx = this.f_96543_ / 2;
        int cy = this.f_96544_ / 2;
        int cardW = 320;
        int cardH = 150;
        int cardX = cx - cardW / 2;
        int cardY = cy - 75;
        int bgAlpha = (int)(120.0f * this.fadeIn);
        gfx.m_280509_(cardX, cardY, cardX + cardW, cardY + cardH, bgAlpha << 24 | 0xA0406);
        int borderAlpha = (int)(65.0f * this.fadeIn);
        int borderColor = borderAlpha << 24 | 0x66181E;
        gfx.m_280509_(cardX, cardY, cardX + cardW, cardY + 1, borderColor);
        gfx.m_280509_(cardX, cardY + cardH - 1, cardX + cardW, cardY + cardH, borderColor);
        gfx.m_280509_(cardX, cardY, cardX + 1, cardY + cardH, borderColor);
        gfx.m_280509_(cardX + cardW - 1, cardY, cardX + cardW, cardY + cardH, borderColor);
        String title = MainMenuScreen.isRussian() ? "\u0418\u041d\u0424\u041e\u0420\u041c\u0410\u0426\u0418\u042f \u041e\u0411 \u041e\u0411\u041d\u041e\u0412\u041b\u0415\u041d\u0418\u0418" : "UPDATE INFORMATION";
        int titleAlpha = (int)(240.0f * this.fadeIn);
        gfx.m_280137_(font, title, cx, cardY + 14, Mth.m_14045_((int)titleAlpha, (int)0, (int)255) << 24 | 0xFFE0E5);
        String[] lines = this.info.split("\\n");
        int textAlpha = (int)(180.0f * this.fadeIn);
        for (int i = 0; i < lines.length; ++i) {
            gfx.m_280137_(font, lines[i], cx, cardY + 34 + i * 14, Mth.m_14045_((int)textAlpha, (int)0, (int)255) << 24 | 0xDDCCCC);
        }
        RenderSystem.enableBlend();
        super.m_88315_(gfx, mouseX, mouseY, partialTick);
    }

    public boolean m_7043_() {
        return false;
    }
}

