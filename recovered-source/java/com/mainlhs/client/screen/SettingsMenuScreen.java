/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.Options
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.AccessibilityOptionsScreen
 *  net.minecraft.client.gui.screens.ChatOptionsScreen
 *  net.minecraft.client.gui.screens.LanguageSelectScreen
 *  net.minecraft.client.gui.screens.MouseSettingsScreen
 *  net.minecraft.client.gui.screens.OnlineOptionsScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.SkinCustomizationScreen
 *  net.minecraft.client.gui.screens.SoundOptionsScreen
 *  net.minecraft.client.gui.screens.VideoSettingsScreen
 *  net.minecraft.client.gui.screens.controls.ControlsScreen
 *  net.minecraft.client.gui.screens.packs.PackSelectionScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.packs.repository.PackRepository
 *  net.minecraft.util.Mth
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.mainlhs.client.screen;

import com.mainlhs.client.config.ClientConfig;
import com.mainlhs.client.screen.CustomButton;
import com.mainlhs.client.screen.CustomSlider;
import com.mainlhs.client.screen.LanguageToggleWidget;
import com.mainlhs.client.screen.MainMenuScreen;
import com.mainlhs.client.screen.MenuBackgroundRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.ChatOptionsScreen;
import net.minecraft.client.gui.screens.LanguageSelectScreen;
import net.minecraft.client.gui.screens.MouseSettingsScreen;
import net.minecraft.client.gui.screens.OnlineOptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SkinCustomizationScreen;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public class SettingsMenuScreen
extends Screen {
    private final Screen parent;
    private float time = 0.0f;
    private float fadeIn = 0.0f;
    private float titleGlow = 0.0f;
    private final List<AbstractWidget> menuWidgets = new ArrayList<AbstractWidget>();
    private CustomButton backButton;
    private LanguageToggleWidget languageToggle;
    private static final int WIDGET_WIDTH = 200;
    private static final int WIDGET_HEIGHT = 24;
    private static final int COL_GAP = 20;
    private static final int ROW_GAP = 28;

    public SettingsMenuScreen(Screen parent) {
        super((Component)Component.m_237113_((String)"Settings Menu"));
        this.parent = parent;
    }

    private void openVideoSettings(Minecraft minecraft) {
        try {
            Class<?> sodiumClass = Class.forName("me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI");
            Constructor<?> constructor = sodiumClass.getConstructor(Screen.class);
            Screen sodiumScreen = (Screen)constructor.newInstance(new Object[]{this});
            minecraft.m_91152_(sodiumScreen);
            return;
        }
        catch (Throwable sodiumClass) {
            try {
                Class<?> embeddiumClass = Class.forName("org.embeddedt.embeddium.client.gui.options.EmbeddiumVideoOptionsScreen");
                Constructor<?> constructor = embeddiumClass.getConstructor(Screen.class);
                Screen embeddiumScreen = (Screen)constructor.newInstance(new Object[]{this});
                minecraft.m_91152_(embeddiumScreen);
                return;
            }
            catch (Throwable throwable) {
                minecraft.m_91152_((Screen)new VideoSettingsScreen((Screen)this, minecraft.f_91066_));
                return;
            }
        }
    }

    protected void m_7856_() {
        Minecraft minecraft = this.f_96541_;
        if (minecraft == null) {
            return;
        }
        this.menuWidgets.clear();
        int leftX = this.f_96543_ / 2 - 200 - 10;
        int rightX = this.f_96543_ / 2 + 10;
        int startY = Math.max(70, (int)((float)this.f_96544_ * 0.2f));
        int rowGap = Math.max(24, Math.min(28, (this.f_96544_ - startY - 44) / 7));
        this.backButton = new CustomButton(16, 14, 90, 20, (Component)Component.m_237113_((String)this.text("\u041d\u0410\u0417\u0410\u0414", "BACK")), btn -> this.m_7379_(), false);
        this.m_142416_((GuiEventListener)this.backButton);
        double initialFovVal = ((double)((Integer)minecraft.f_91066_.m_231837_().m_231551_()).intValue() - 30.0) / 80.0;
        CustomSlider fovSlider = new CustomSlider(leftX, startY, 200, 24, this.text("\u041f\u043e\u043b\u0435 \u0437\u0440\u0435\u043d\u0438\u044f: ", "FOV: "), initialFovVal, false, 30.0, 110.0, val -> {
            int mappedVal = (int)Math.round(30.0 + val * 80.0);
            minecraft.f_91066_.m_231837_().m_231514_((Object)mappedVal);
        });
        this.menuWidgets.add((AbstractWidget)fovSlider);
        this.m_142416_((GuiEventListener)fovSlider);
        this.addButton(leftX, startY + rowGap, this.text("\u0412\u0418\u0414\u0415\u041e", "VIDEO"), btn -> this.openVideoSettings(minecraft));
        this.addButton(leftX, startY + rowGap * 2, this.text("\u0423\u041f\u0420\u0410\u0412\u041b\u0415\u041d\u0418\u0415", "CONTROLS"), btn -> minecraft.m_91152_((Screen)new ControlsScreen((Screen)this, minecraft.f_91066_)));
        this.addButton(leftX, startY + rowGap * 3, this.text("\u0427\u0410\u0422", "CHAT"), btn -> minecraft.m_91152_((Screen)new ChatOptionsScreen((Screen)this, minecraft.f_91066_)));
        this.addButton(leftX, startY + rowGap * 4, this.text("\u0421\u041a\u0418\u041d", "SKIN"), btn -> minecraft.m_91152_((Screen)new SkinCustomizationScreen((Screen)this, minecraft.f_91066_)));
        this.addButton(leftX, startY + rowGap * 5, this.text("\u0420\u0415\u0421\u0423\u0420\u0421\u041f\u0410\u041a\u0418", "RESOURCE PACKS"), btn -> minecraft.m_91152_(this.createPackScreen(minecraft)));
        this.addButton(rightX, startY, this.text("\u0417\u0412\u0423\u041a", "SOUND"), btn -> minecraft.m_91152_((Screen)new SoundOptionsScreen((Screen)this, minecraft.f_91066_)));
        this.addButton(rightX, startY + rowGap, this.text("\u041c\u042b\u0428\u042c", "MOUSE"), btn -> minecraft.m_91152_((Screen)new MouseSettingsScreen((Screen)this, minecraft.f_91066_)));
        this.addButton(rightX, startY + rowGap * 2, this.text("\u042f\u0417\u042b\u041a", "LANGUAGE"), btn -> minecraft.m_91152_((Screen)new LanguageSelectScreen((Screen)this, minecraft.f_91066_, minecraft.m_91102_())));
        this.addButton(rightX, startY + rowGap * 3, this.text("\u0421\u041f\u0415\u0426\u0418\u0410\u041b\u042c\u041d\u042b\u0415 \u0412\u041e\u0417\u041c\u041e\u0416\u041d\u041e\u0421\u0422\u0418", "ACCESSIBILITY"), btn -> minecraft.m_91152_((Screen)new AccessibilityOptionsScreen((Screen)this, minecraft.f_91066_)));
        this.addButton(rightX, startY + rowGap * 4, this.text("\u0421\u0415\u0422\u042c", "ONLINE"), btn -> minecraft.m_91152_((Screen)OnlineOptionsScreen.m_260896_((Minecraft)minecraft, (Screen)this, (Options)minecraft.f_91066_)));
        this.languageToggle = new LanguageToggleWidget(this.f_96543_ - 80, 14, () -> this.f_96541_.m_91152_((Screen)new SettingsMenuScreen(this.parent)));
        this.m_142416_((GuiEventListener)this.languageToggle);
        this.applyAnimations();
        this.languageToggle.setFadeIn(this.fadeIn);
    }

    private void addButton(int x, int y, String label, Button.OnPress onPress) {
        CustomButton button = new CustomButton(x, y, 200, 24, (Component)Component.m_237113_((String)label), onPress, false);
        this.menuWidgets.add((AbstractWidget)button);
        this.m_142416_((GuiEventListener)button);
    }

    private String text(String ru, String en) {
        return MainMenuScreen.isRussian() ? ru : en;
    }

    private Screen createPackScreen(Minecraft minecraft) {
        PackRepository repository = minecraft.m_91099_();
        return new PackSelectionScreen(repository, updatedRepository -> {
            minecraft.f_91066_.m_274546_(updatedRepository);
            minecraft.m_91391_();
            minecraft.m_91152_((Screen)new SettingsMenuScreen(this.parent));
        }, minecraft.m_245161_(), (Component)Component.m_237113_((String)this.text("\u0420\u0415\u0421\u0423\u0420\u0421\u041f\u0410\u041a\u0418", "RESOURCE PACKS")));
    }

    private void applyAnimations() {
        if (this.backButton != null) {
            this.backButton.setAnimationAlpha(this.fadeIn);
            this.backButton.setSlideOffset((1.0f - this.fadeIn) * 10.0f);
        }
        for (int i = 0; i < this.menuWidgets.size(); ++i) {
            float delay = (float)i * 0.025f;
            float rawProgress = Math.max(0.0f, Math.min((this.fadeIn - 0.05f - delay) * 3.2f, 1.0f));
            float alpha = SettingsMenuScreen.easeOutCubic(rawProgress);
            AbstractWidget widget = this.menuWidgets.get(i);
            if (widget instanceof CustomButton) {
                CustomButton cb = (CustomButton)widget;
                cb.setAnimationAlpha(alpha);
                cb.setSlideOffset((1.0f - alpha) * 12.0f);
            } else if (widget instanceof CustomSlider) {
                CustomSlider cs = (CustomSlider)widget;
                cs.setAnimationAlpha(alpha);
                cs.setSlideOffset((1.0f - alpha) * 12.0f);
            }
            widget.m_93650_(alpha);
        }
    }

    private static float easeOutCubic(float x) {
        float inv = 1.0f - x;
        return 1.0f - inv * inv * inv;
    }

    public void m_86600_() {
        this.time += 0.05f;
        this.fadeIn = Math.min(this.fadeIn + 0.06f, 1.0f);
        this.titleGlow = (float)(Math.sin(this.time * 1.4f) * 0.5 + 0.5);
        this.applyAnimations();
        if (this.languageToggle != null) {
            this.languageToggle.setFadeIn(this.fadeIn);
            this.languageToggle.tick();
        }
    }

    public void m_88315_(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        float renderTime = this.time + partialTick * 0.05f;
        MenuBackgroundRenderer.render(gfx, this.f_96543_, this.f_96544_, renderTime, this.fadeIn, mouseX, mouseY);
        this.drawHeader(gfx);
        RenderSystem.enableBlend();
        super.m_88315_(gfx, mouseX, mouseY, partialTick);
    }

    public void m_280273_(GuiGraphics gfx) {
    }

    private void drawHeader(GuiGraphics gfx) {
        Font font = this.f_96547_;
        int cx = this.f_96543_ / 2;
        int titleY = Math.max(14, (int)((float)this.f_96544_ * 0.06f));
        float alpha = SettingsMenuScreen.easeOutCubic(this.fadeIn);
        int baseAlpha = (int)(alpha * 255.0f);
        int glowAlpha = (int)(alpha * (80.0f + this.titleGlow * 90.0f));
        String title = "LAST HUMAN STRONGHOLD";
        int titleW = font.m_92895_(title);
        int shadowColor = Mth.m_14045_((int)glowAlpha, (int)0, (int)255) << 24 | 0xFF2A35;
        gfx.m_280056_(font, title, cx - titleW / 2, titleY + 1, shadowColor, false);
        int titleColor = Mth.m_14045_((int)baseAlpha, (int)0, (int)255) << 24 | 0xFFF2F4;
        gfx.m_280056_(font, title, cx - titleW / 2, titleY, titleColor, false);
        String sub = this.text("\u2014 \u0426\u0415\u041d\u0422\u0420 \u041d\u0410\u0421\u0422\u0420\u041e\u0415\u041a \u2014", "\u2014 CONTROL CENTER \u2014");
        int subAlpha = (int)(alpha * 160.0f);
        gfx.m_280137_(font, sub, cx, titleY + 12, Mth.m_14045_((int)subAlpha, (int)0, (int)255) << 24 | 0xD4757C);
    }

    public boolean m_6913_() {
        return true;
    }

    public void m_7379_() {
        if (this.f_96541_ != null) {
            this.f_96541_.f_91066_.m_92169_();
            ClientConfig.save();
            if (this.parent != null) {
                this.f_96541_.m_91152_(this.parent);
            } else {
                this.f_96541_.m_91152_((Screen)new MainMenuScreen());
            }
        }
    }
}

