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

import com.mainlhs.client.screen.AuthorsScreen;
import com.mainlhs.client.screen.CustomButton;
import com.mainlhs.client.screen.LanguageToggleWidget;
import com.mainlhs.client.screen.MenuBackgroundRenderer;
import com.mainlhs.client.screen.ServerConnector;
import com.mainlhs.client.screen.SettingsMenuScreen;
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
public class MainMenuScreen
extends Screen {
    private static boolean russian = true;
    private final boolean cinematicIntro;
    private float time = 0.0f;
    private float fadeIn = 0.0f;
    private float titleGlow = 0.0f;
    private final float[] buttonFade = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
    private CustomButton playButton;
    private CustomButton settingsButton;
    private CustomButton authorsButton;
    private CustomButton exitButton;
    private LanguageToggleWidget languageToggle;
    private static final int BTN_WIDTH = 210;
    private static final int BTN_HEIGHT = 26;
    private static final int BTN_GAP = 9;
    private long lastTickTime = System.currentTimeMillis();

    public MainMenuScreen() {
        this(false);
    }

    public MainMenuScreen(boolean cinematicIntro) {
        super((Component)Component.m_237113_((String)"LAST HUMAN STRONGHOLD"));
        this.cinematicIntro = cinematicIntro;
    }

    public static boolean isRussian() {
        return russian;
    }

    public static void setRussian(boolean value) {
        russian = value;
    }

    public static String text(String ru, String en) {
        return russian ? ru : en;
    }

    protected void m_7856_() {
        if (this.cinematicIntro && this.fadeIn < 0.01f) {
            this.fadeIn = 0.02f;
        }
        int centerX = this.f_96543_ / 2;
        int startY = Math.max(115, (int)((float)this.f_96544_ * 0.44f));
        this.playButton = new CustomButton(centerX - 105, startY, 210, 26, (Component)Component.m_237113_((String)MainMenuScreen.text("\u0418\u0413\u0420\u0410\u0422\u042c", "PLAY")), btn -> ServerConnector.connect(this), false).setPrimary(true);
        this.settingsButton = new CustomButton(centerX - 105, startY + 35, 210, 26, (Component)Component.m_237113_((String)MainMenuScreen.text("\u041d\u0410\u0421\u0422\u0420\u041e\u0419\u041a\u0418", "SETTINGS")), btn -> this.f_96541_.m_91152_((Screen)new SettingsMenuScreen(this)), false);
        this.authorsButton = new CustomButton(centerX - 105, startY + 70, 210, 26, (Component)Component.m_237113_((String)MainMenuScreen.text("\u0410\u0412\u0422\u041e\u0420\u042b", "AUTHORS")), btn -> this.f_96541_.m_91152_((Screen)new AuthorsScreen(this)), false);
        this.exitButton = new CustomButton(centerX - 105, startY + 105, 210, 26, (Component)Component.m_237113_((String)MainMenuScreen.text("\u0412\u042b\u0425\u041e\u0414", "EXIT")), btn -> this.f_96541_.m_91395_(), true);
        this.languageToggle = new LanguageToggleWidget(this.f_96543_ - 80, 14, () -> this.f_96541_.m_91152_((Screen)new MainMenuScreen()));
        this.m_142416_((GuiEventListener)this.playButton);
        this.m_142416_((GuiEventListener)this.settingsButton);
        this.m_142416_((GuiEventListener)this.authorsButton);
        this.m_142416_((GuiEventListener)this.exitButton);
        this.m_142416_((GuiEventListener)this.languageToggle);
        this.applyButtonAnimations();
        this.languageToggle.setFadeIn(this.fadeIn);
    }

    public void m_86600_() {
        float buttonStart;
        this.time += 0.05f;
        float fadeSpeed = this.cinematicIntro ? 0.035f : 0.05f;
        this.fadeIn = Math.min(this.fadeIn + fadeSpeed, 1.0f);
        this.titleGlow = (float)(Math.sin(this.time * 1.4f) * 0.5 + 0.5);
        float f = buttonStart = this.cinematicIntro ? 0.25f : 0.15f;
        if (this.fadeIn > buttonStart) {
            for (int i = 0; i < this.buttonFade.length; ++i) {
                float delay = (float)i * 0.12f;
                float progress = Math.max(0.0f, Math.min((this.fadeIn - buttonStart - delay) * 2.8f, 1.0f));
                this.buttonFade[i] = MainMenuScreen.easeOutCubic(progress);
            }
        }
        this.applyButtonAnimations();
        if (this.languageToggle != null) {
            this.languageToggle.setFadeIn(this.fadeIn);
            this.languageToggle.tick();
        }
    }

    private static float easeOutCubic(float x) {
        float inv = 1.0f - x;
        return 1.0f - inv * inv * inv;
    }

    private void applyButtonAnimations() {
        CustomButton[] buttons = new CustomButton[]{this.playButton, this.settingsButton, this.authorsButton, this.exitButton};
        for (int i = 0; i < buttons.length; ++i) {
            if (buttons[i] == null) continue;
            buttons[i].setAnimationAlpha(this.buttonFade[i]);
            buttons[i].setSlideOffset((1.0f - this.buttonFade[i]) * 14.0f);
        }
    }

    public void m_88315_(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        float renderTime = MenuBackgroundRenderer.getTime();
        MenuBackgroundRenderer.render(gfx, this.f_96543_, this.f_96544_, renderTime, this.fadeIn, mouseX, mouseY);
        this.drawHeader(gfx, renderTime);
        this.drawFooter(gfx);
        RenderSystem.enableBlend();
        super.m_88315_(gfx, mouseX, mouseY, partialTick);
    }

    private void drawHeader(GuiGraphics gfx, float renderTime) {
        Font font = this.f_96547_;
        int cx = this.f_96543_ / 2;
        int titleY = Math.max(28, (int)((float)this.f_96544_ * 0.18f));
        float alpha = MainMenuScreen.easeOutCubic(this.fadeIn);
        int baseAlpha = (int)(alpha * 255.0f);
        int glowAlpha = (int)(alpha * (75.0f + this.titleGlow * 105.0f));
        String title = "LAST HUMAN STRONGHOLD";
        int titleWidth = font.m_92895_(title);
        int shadowColor = Mth.m_14045_((int)glowAlpha, (int)0, (int)255) << 24 | 0xFF2A35;
        gfx.m_280056_(font, title, cx - titleWidth / 2, titleY + 1, shadowColor, false);
        int titleColor = Mth.m_14045_((int)baseAlpha, (int)0, (int)255) << 24 | 0xFFF2F4;
        gfx.m_280056_(font, title, cx - titleWidth / 2, titleY, titleColor, false);
        String sub = MainMenuScreen.text("\u041f\u041e\u0421\u041b\u0415\u0414\u041d\u0418\u0419 \u041e\u041f\u041b\u041e\u0422 \u0427\u0415\u041b\u041e\u0412\u0415\u0427\u0415\u0421\u0422\u0412\u0410", "THE LAST HUMAN STRONGHOLD");
        int subY = titleY + 14;
        int subAlpha = (int)(alpha * 165.0f);
        int subColor = Mth.m_14045_((int)subAlpha, (int)0, (int)255) << 24 | 0xD4757C;
        gfx.m_280137_(font, sub, cx, subY, subColor);
        int divY = subY + 16;
        int divHalfWidth = Math.min(140, this.f_96543_ / 4);
        int divAlpha = (int)(alpha * 80.0f);
        int divColor = divAlpha << 24 | 0x882025;
        gfx.m_280509_(cx - divHalfWidth, divY, cx - 12, divY + 1, divColor);
        gfx.m_280509_(cx + 12, divY, cx + divHalfWidth, divY + 1, divColor);
        int dotAlpha = (int)(alpha * (180.0f + this.titleGlow * 75.0f));
        int dotColor = Mth.m_14045_((int)dotAlpha, (int)0, (int)255) << 24 | 0xFF4D5A;
        gfx.m_280509_(cx - 2, divY - 1, cx + 2, divY + 2, dotColor);
    }

    private void drawFooter(GuiGraphics gfx) {
    }

    public void m_280273_(GuiGraphics gfx) {
    }

    public boolean m_6913_() {
        return false;
    }
}

