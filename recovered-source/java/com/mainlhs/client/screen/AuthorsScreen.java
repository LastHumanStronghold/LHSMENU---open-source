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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class AuthorsScreen
extends Screen {
    private static final AuthorEntry[] ENTRIES = new AuthorEntry[]{new AuthorEntry("\u041e\u0441\u043d\u043e\u0432\u0430\u0442\u0435\u043b\u044c", "Founder", "Beer_Borman"), new AuthorEntry("\u0412\u043b\u0430\u0434\u0435\u043b\u0435\u0446 \u0445\u043e\u0441\u0442\u0438\u043d\u0433\u0430", "Hosting Owner", "ElgerElg"), new AuthorEntry("\u041a\u0443\u0440\u0430\u0442\u043e\u0440", "Curator", "TablurExpan"), new AuthorEntry("\u0422\u0435\u0445\u043d\u0438\u0447\u0435\u0441\u043a\u0438\u0439 \u0410\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440", "Technical Administrator", "1mN1kO")};
    private final Screen parent;
    private float time = 0.0f;
    private float fadeIn = 0.0f;
    private CustomButton backButton;

    public AuthorsScreen(Screen parent) {
        super((Component)Component.m_237113_((String)"Authors"));
        this.parent = parent;
    }

    protected void m_7856_() {
        this.backButton = new CustomButton(16, 14, 90, 20, (Component)Component.m_237113_((String)(MainMenuScreen.isRussian() ? "\u041d\u0410\u0417\u0410\u0414" : "BACK")), btn -> this.closeScreen(), false);
        this.m_142416_((GuiEventListener)this.backButton);
        this.applyAnimations();
    }

    public void m_86600_() {
        this.time += 0.05f;
        this.fadeIn = Math.min(this.fadeIn + 0.06f, 1.0f);
        this.applyAnimations();
    }

    private void applyAnimations() {
        if (this.backButton != null) {
            this.backButton.setAnimationAlpha(this.fadeIn);
            this.backButton.setSlideOffset((1.0f - AuthorsScreen.easeOutCubic(this.fadeIn)) * 10.0f);
        }
    }

    private static float easeOutCubic(float x) {
        float inv = 1.0f - x;
        return 1.0f - inv * inv * inv;
    }

    public void m_88315_(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        float renderTime = this.time + partialTick * 0.05f;
        MenuBackgroundRenderer.render(gfx, this.f_96543_, this.f_96544_, renderTime, this.fadeIn, mouseX, mouseY);
        this.drawContent(gfx);
        RenderSystem.enableBlend();
        super.m_88315_(gfx, mouseX, mouseY, partialTick);
    }

    public void m_280273_(GuiGraphics gfx) {
    }

    private void drawContent(GuiGraphics gfx) {
        Font font = this.f_96547_;
        int cx = this.f_96543_ / 2;
        float alpha = AuthorsScreen.easeOutCubic(this.fadeIn);
        int baseAlpha = (int)(alpha * 255.0f);
        boolean ru = MainMenuScreen.isRussian();
        int titleY = Math.max(20, (int)((float)this.f_96544_ * 0.12f));
        String title = ru ? "\u0421\u041e\u0417\u0414\u0410\u0422\u0415\u041b\u0418 \u041f\u0420\u041e\u0415\u041a\u0422\u0410" : "PROJECT AUTHORS";
        int titleW = font.m_92895_(title);
        int glowAlpha = (int)(alpha * 120.0f);
        int shadowColor = glowAlpha << 24 | 0xFF2A35;
        gfx.m_280056_(font, title, cx - titleW / 2, titleY + 1, shadowColor, false);
        gfx.m_280056_(font, title, cx - titleW / 2, titleY, baseAlpha << 24 | 0xFFF2F4, false);
        String subtitle = "LAST HUMAN STRONGHOLD";
        int subAlpha = (int)(alpha * 150.0f);
        gfx.m_280137_(font, subtitle, cx, titleY + 12, subAlpha << 24 | 0xD4757C);
        int cardWidth = Math.min(380, this.f_96543_ - 32);
        int cardX = cx - cardWidth / 2;
        int cardY = titleY + 30;
        int rowHeight = 24;
        int cardHeight = ENTRIES.length * rowHeight + 14;
        int bgAlpha = (int)(110.0f * alpha);
        gfx.m_280509_(cardX, cardY, cardX + cardWidth, cardY + cardHeight, bgAlpha << 24 | 0xA0406);
        int borderAlpha = (int)(65.0f * alpha);
        int borderColor = borderAlpha << 24 | 0x66181E;
        gfx.m_280509_(cardX, cardY, cardX + cardWidth, cardY + 1, borderColor);
        gfx.m_280509_(cardX, cardY + cardHeight - 1, cardX + cardWidth, cardY + cardHeight, borderColor);
        gfx.m_280509_(cardX, cardY, cardX + 1, cardY + cardHeight, borderColor);
        gfx.m_280509_(cardX + cardWidth - 1, cardY, cardX + cardWidth, cardY + cardHeight, borderColor);
        int cornerAlpha = (int)(180.0f * alpha);
        int cornerColor = cornerAlpha << 24 | 0xFF4450;
        int tickLen = 6;
        gfx.m_280509_(cardX, cardY, cardX + tickLen, cardY + 1, cornerColor);
        gfx.m_280509_(cardX, cardY, cardX + 1, cardY + tickLen, cornerColor);
        gfx.m_280509_(cardX + cardWidth - tickLen, cardY, cardX + cardWidth, cardY + 1, cornerColor);
        gfx.m_280509_(cardX + cardWidth - 1, cardY, cardX + cardWidth, cardY + tickLen, cornerColor);
        gfx.m_280509_(cardX, cardY + cardHeight - 1, cardX + tickLen, cardY + cardHeight, cornerColor);
        gfx.m_280509_(cardX, cardY + cardHeight - tickLen, cardX + 1, cardY + cardHeight, cornerColor);
        gfx.m_280509_(cardX + cardWidth - tickLen, cardY + cardHeight - 1, cardX + cardWidth, cardY + cardHeight, cornerColor);
        gfx.m_280509_(cardX + cardWidth - 1, cardY + cardHeight - tickLen, cardX + cardWidth, cardY + cardHeight, cornerColor);
        int curY = cardY + 8;
        for (int i = 0; i < ENTRIES.length; ++i) {
            AuthorEntry entry = ENTRIES[i];
            String role = ru ? entry.roleRu() : entry.roleEn();
            String name = entry.name();
            int roleAlpha = (int)(alpha * 190.0f);
            int roleColor = roleAlpha << 24 | 0xE65A65;
            gfx.m_280056_(font, role, cardX + 14, curY + 4, roleColor, false);
            int nameAlpha = (int)(alpha * 240.0f);
            int nameColor = nameAlpha << 24 | 0xFFFFFF;
            int nameW = font.m_92895_(name);
            gfx.m_280056_(font, name, cardX + cardWidth - 14 - nameW, curY + 4, nameColor, false);
            if (i < ENTRIES.length - 1) {
                int lineAlpha = (int)(25.0f * alpha);
                gfx.m_280509_(cardX + 10, curY + rowHeight - 1, cardX + cardWidth - 10, curY + rowHeight, lineAlpha << 24 | 0xFFFFFF);
            }
            curY += rowHeight;
        }
    }

    private void closeScreen() {
        if (this.f_96541_ != null) {
            this.f_96541_.m_91152_((Screen)(this.parent == null ? new MainMenuScreen() : this.parent));
        }
    }

    public void m_7379_() {
        this.closeScreen();
    }

    public boolean m_7043_() {
        return false;
    }

    private record AuthorEntry(String roleRu, String roleEn, String name) {
    }
}

