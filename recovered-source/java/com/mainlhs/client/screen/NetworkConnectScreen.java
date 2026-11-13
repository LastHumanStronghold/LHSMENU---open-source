/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.EditBox
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
import com.mainlhs.client.screen.ServerConnector;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class NetworkConnectScreen
extends Screen {
    private final Screen parent;
    private EditBox addressBox;
    private Button connectButton;
    private Button cancelButton;
    private float time = 0.0f;
    private float fadeIn = 0.0f;

    public NetworkConnectScreen(Screen parent) {
        super((Component)Component.m_237113_((String)"Network Connect"));
        this.parent = parent;
    }

    protected void m_7856_() {
        int cx = this.f_96543_ / 2;
        int y = this.f_96544_ / 2 - 10;
        this.addressBox = new EditBox(this.f_96541_.f_91062_, cx - 130, y, 260, 22, (Component)Component.m_237113_((String)""));
        this.addressBox.m_94144_(ServerConnector.getServerAddress());
        this.m_142416_((GuiEventListener)this.addressBox);
        this.connectButton = new CustomButton(cx - 130, y + 30, 125, 24, (Component)Component.m_237113_((String)(MainMenuScreen.isRussian() ? "\u041f\u041e\u0414\u041a\u041b\u042e\u0427\u0418\u0422\u042c\u0421\u042f" : "CONNECT")), btn -> {
            String val = this.addressBox.m_94155_();
            if (val != null && !val.isBlank()) {
                ServerConnector.connect(this, val.trim());
            }
        }, false).setPrimary(true);
        this.m_142416_((GuiEventListener)this.connectButton);
        this.cancelButton = new CustomButton(cx + 5, y + 30, 125, 24, (Component)Component.m_237113_((String)(MainMenuScreen.isRussian() ? "\u041e\u0422\u041c\u0415\u041d\u0410" : "CANCEL")), btn -> {
            if (this.f_96541_ != null) {
                if (this.parent != null) {
                    this.f_96541_.m_91152_(this.parent);
                } else {
                    this.f_96541_.m_91152_((Screen)new MainMenuScreen());
                }
            }
        }, true);
        this.m_142416_((GuiEventListener)this.cancelButton);
        this.m_264313_((GuiEventListener)this.addressBox);
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
        int cardW = 290;
        int cardH = 110;
        int cardX = cx - cardW / 2;
        int cardY = cy - 56;
        int bgAlpha = (int)(120.0f * this.fadeIn);
        gfx.m_280509_(cardX, cardY, cardX + cardW, cardY + cardH, bgAlpha << 24 | 0xA0406);
        int borderAlpha = (int)(65.0f * this.fadeIn);
        int borderColor = borderAlpha << 24 | 0x66181E;
        gfx.m_280509_(cardX, cardY, cardX + cardW, cardY + 1, borderColor);
        gfx.m_280509_(cardX, cardY + cardH - 1, cardX + cardW, cardY + cardH, borderColor);
        gfx.m_280509_(cardX, cardY, cardX + 1, cardY + cardH, borderColor);
        gfx.m_280509_(cardX + cardW - 1, cardY, cardX + cardW, cardY + cardH, borderColor);
        String title = MainMenuScreen.isRussian() ? "\u041f\u0420\u042f\u041c\u041e\u0415 \u041f\u041e\u0414\u041a\u041b\u042e\u0427\u0415\u041d\u0418\u0415" : "DIRECT CONNECT";
        int titleAlpha = (int)(240.0f * this.fadeIn);
        gfx.m_280137_(font, title, cx, cardY + 12, Mth.m_14045_((int)titleAlpha, (int)0, (int)255) << 24 | 0xFFE0E5);
        String subtitle = MainMenuScreen.isRussian() ? "\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0430\u0434\u0440\u0435\u0441 \u0441\u0435\u0440\u0432\u0435\u0440\u0430 (IP:\u043f\u043e\u0440\u0442):" : "Enter server address (IP:port):";
        int subAlpha = (int)(140.0f * this.fadeIn);
        gfx.m_280137_(font, subtitle, cx, cardY + 26, Mth.m_14045_((int)subAlpha, (int)0, (int)255) << 24 | 0xB09095);
        RenderSystem.enableBlend();
        super.m_88315_(gfx, mouseX, mouseY, partialTick);
    }

    public boolean m_7933_(int keyCode, int scanCode, int modifiers) {
        String val;
        if ((keyCode == 257 || keyCode == 335) && this.addressBox != null && this.addressBox.m_93696_() && (val = this.addressBox.m_94155_()) != null && !val.isBlank()) {
            ServerConnector.connect(this, val.trim());
            return true;
        }
        return super.m_7933_(keyCode, scanCode, modifiers);
    }

    public boolean m_7043_() {
        return false;
    }
}

