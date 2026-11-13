/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.screens.ConnectScreen
 *  net.minecraft.client.gui.screens.LevelLoadingScreen
 *  net.minecraft.client.gui.screens.LoadingOverlay
 *  net.minecraft.client.gui.screens.OptionsScreen
 *  net.minecraft.client.gui.screens.OptionsSubScreen
 *  net.minecraft.client.gui.screens.Overlay
 *  net.minecraft.client.gui.screens.ProgressScreen
 *  net.minecraft.client.gui.screens.ReceivingLevelScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.TitleScreen
 *  net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen
 *  net.minecraft.client.gui.screens.packs.PackSelectionScreen
 *  net.minecraft.client.gui.screens.worldselection.SelectWorldScreen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.packs.resources.ReloadInstance
 *  net.minecraft.util.Mth
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.ScreenEvent$BackgroundRendered
 *  net.minecraftforge.client.event.ScreenEvent$MouseButtonPressed$Pre
 *  net.minecraftforge.client.event.ScreenEvent$Opening
 *  net.minecraftforge.client.event.ScreenEvent$Render$Post
 *  net.minecraftforge.client.event.ScreenEvent$Render$Pre
 *  net.minecraftforge.event.TickEvent$ClientTickEvent
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.eventbus.api.EventPriority
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  net.minecraftforge.fml.util.ObfuscationReflectionHelper
 */
package com.mainlhs.client;

import com.mainlhs.MainLHS;
import com.mainlhs.client.loading.BrandedLoadingOverlay;
import com.mainlhs.client.loading.LoadingVisuals;
import com.mainlhs.client.screen.IntroLoadingScreen;
import com.mainlhs.client.screen.MainMenuScreen;
import com.mainlhs.client.screen.MenuBackgroundRenderer;
import com.mainlhs.client.screen.SettingsMenuScreen;
import com.mainlhs.client.sound.ModSounds;
import java.lang.reflect.Field;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

@Mod.EventBusSubscriber(modid="mainlhs", bus=Mod.EventBusSubscriber.Bus.FORGE, value={Dist.CLIENT})
public final class MenuEvents {
    private static final Field LOADING_OVERLAY_RELOAD = ObfuscationReflectionHelper.findField(LoadingOverlay.class, (String)"f_96164_");
    private static final Field LOADING_OVERLAY_ON_FINISH = ObfuscationReflectionHelper.findField(LoadingOverlay.class, (String)"f_96165_");
    private static final Field LOADING_OVERLAY_FADE_IN = ObfuscationReflectionHelper.findField(LoadingOverlay.class, (String)"f_96166_");
    private static final Field LOADING_OVERLAY_PROGRESS = ObfuscationReflectionHelper.findField(LoadingOverlay.class, (String)"f_96167_");
    private static final Field CONNECT_STATUS = ObfuscationReflectionHelper.findField(ConnectScreen.class, (String)"f_95687_");
    private static final Field PROGRESS_HEADER = ObfuscationReflectionHelper.findField(ProgressScreen.class, (String)"f_96506_");
    private static final Field PROGRESS_STAGE = ObfuscationReflectionHelper.findField(ProgressScreen.class, (String)"f_96507_");
    private static final Field PROGRESS_VALUE = ObfuscationReflectionHelper.findField(ProgressScreen.class, (String)"f_96508_");
    private static boolean introShown;
    private static float loadingBtnHoverAnim;
    private static boolean loadingBtnHoverSoundPlayed;
    private static long loadingBtnLastRenderTime;

    private MenuEvents() {
    }

    @SubscribeEvent
    public static void onBackgroundRender(ScreenEvent.BackgroundRendered event) {
        Screen screen = event.getScreen();
        if (screen instanceof OptionsScreen || screen instanceof OptionsSubScreen || screen instanceof SelectWorldScreen || screen instanceof PackSelectionScreen || screen instanceof JoinMultiplayerScreen) {
            if (screen instanceof SettingsMenuScreen) {
                return;
            }
            float time = MenuBackgroundRenderer.getTime();
            MenuBackgroundRenderer.render(event.getGuiGraphics(), screen.f_96543_, screen.f_96544_, time, 1.0f);
        }
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onScreenOpen(ScreenEvent.Opening event) {
        Screen screen;
        Screen screen2 = screen = event.getScreen() != null ? event.getScreen() : event.getNewScreen();
        if (screen == null) {
            return;
        }
        if (screen instanceof TitleScreen && !(screen instanceof MainMenuScreen)) {
            event.setNewScreen(MenuEvents.createReplacementScreen());
        } else if (screen instanceof OptionsScreen && !(screen instanceof SettingsMenuScreen) && !(Minecraft.m_91087_().f_91080_ instanceof SettingsMenuScreen)) {
            event.setNewScreen((Screen)new SettingsMenuScreen(Minecraft.m_91087_().f_91080_));
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft minecraft = Minecraft.m_91087_();
        if (minecraft == null) {
            return;
        }
        Screen screen = minecraft.f_91080_;
        if (MenuEvents.shouldReplaceWithCustomMenu(screen)) {
            minecraft.m_91152_(MenuEvents.createReplacementScreen());
        }
        MenuEvents.ensureBrandedLoadingOverlay(minecraft);
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onScreenRenderPre(ScreenEvent.Render.Pre event) {
        Minecraft minecraft = Minecraft.m_91087_();
        if (minecraft == null) {
            return;
        }
        Screen screen = event.getScreen();
        if (screen instanceof ConnectScreen || screen instanceof ProgressScreen || screen instanceof ReceivingLevelScreen || screen instanceof LevelLoadingScreen) {
            event.setCanceled(true);
            int width = minecraft.m_91268_().m_85445_();
            int height = minecraft.m_91268_().m_85446_();
            float time = MenuBackgroundRenderer.getTime();
            GuiGraphics gfx = event.getGuiGraphics();
            if (screen instanceof ConnectScreen) {
                ConnectScreen connectScreen = (ConnectScreen)screen;
                status = MenuEvents.getComponentText(MenuEvents.readComponent(CONNECT_STATUS, connectScreen));
                if (status.isBlank()) {
                    status = MainMenuScreen.isRussian() ? "\u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0435 \u043a \u0441\u0435\u0440\u0432\u0435\u0440\u0443" : "connecting to server";
                }
                LoadingVisuals.renderOverlay(gfx, minecraft.f_91062_, width, height, time, status, 0.0f, false, 60);
            } else if (screen instanceof ProgressScreen) {
                ProgressScreen progressScreen = (ProgressScreen)screen;
                String header = MenuEvents.getComponentText(MenuEvents.readComponent(PROGRESS_HEADER, progressScreen));
                String stage = MenuEvents.getComponentText(MenuEvents.readComponent(PROGRESS_STAGE, progressScreen));
                int progressValue = MenuEvents.readInt(PROGRESS_VALUE, progressScreen);
                String status = MenuEvents.joinStatus(header, stage, MainMenuScreen.isRussian() ? "\u0432\u044b\u043f\u043e\u043b\u043d\u044f\u0435\u0442\u0441\u044f \u043e\u043f\u0435\u0440\u0430\u0446\u0438\u044f" : "processing task");
                LoadingVisuals.renderOverlay(gfx, minecraft.f_91062_, width, height, time, status, Mth.m_14036_((float)((float)progressValue / 100.0f), (float)0.0f, (float)1.0f), true, 0);
            } else if (screen instanceof ReceivingLevelScreen) {
                status = MainMenuScreen.isRussian() ? "\u043f\u043e\u043b\u0443\u0447\u0435\u043d\u0438\u0435 \u0434\u0430\u043d\u043d\u044b\u0445 \u043c\u0438\u0440\u0430" : "receiving world data";
                LoadingVisuals.renderOverlay(gfx, minecraft.f_91062_, width, height, time, status, 0.0f, false, 0);
            } else if (screen instanceof LevelLoadingScreen) {
                status = MainMenuScreen.isRussian() ? "\u043f\u043e\u0441\u0442\u0440\u043e\u0435\u043d\u0438\u0435 \u043c\u0438\u0440\u0430" : "building world";
                LoadingVisuals.renderOverlay(gfx, minecraft.f_91062_, width, height, time, status, 0.0f, false, 0);
            }
            int mouseX = event.getMouseX();
            int mouseY = event.getMouseY();
            for (Renderable renderable : screen.f_169369_) {
                if (renderable instanceof AbstractWidget) {
                    AbstractWidget widget = (AbstractWidget)renderable;
                    int btnW = Math.min(130, width - 40);
                    int btnH = 22;
                    int btnX = width / 2 - btnW / 2;
                    int btnY = height / 2 + 24;
                    widget.m_252865_(btnX);
                    widget.m_253211_(btnY);
                    widget.m_93674_(btnW);
                    widget.setHeight(btnH);
                    MenuEvents.renderCustomLoadingButton(gfx, minecraft.f_91062_, widget, mouseX, mouseY, time);
                    continue;
                }
                renderable.m_88315_(gfx, mouseX, mouseY, event.getPartialTick());
            }
        }
    }

    private static void renderCustomLoadingButton(GuiGraphics gfx, Font font, AbstractWidget widget, int mouseX, int mouseY, float time) {
        String text;
        Component msg;
        if (!widget.f_93624_) {
            return;
        }
        long now = System.currentTimeMillis();
        float dt = Math.min((float)(now - loadingBtnLastRenderTime) / 1000.0f, 0.1f);
        loadingBtnLastRenderTime = now;
        boolean hovered = widget.m_198029_();
        if (hovered && !loadingBtnHoverSoundPlayed) {
            ModSounds.playHoverSound();
            loadingBtnHoverSoundPlayed = true;
        } else if (!hovered) {
            loadingBtnHoverSoundPlayed = false;
        }
        float targetHover = hovered ? 1.0f : 0.0f;
        float speed = hovered ? 12.0f : 7.0f;
        loadingBtnHoverAnim += (targetHover - loadingBtnHoverAnim) * Math.min(1.0f, dt * speed);
        int x = widget.m_252754_();
        int y = widget.m_252907_();
        int w = widget.m_5711_();
        int h = widget.m_93694_();
        float pulse = (float)(Math.sin(time * 3.0f) * 0.5 + 0.5);
        if (loadingBtnHoverAnim > 0.02f) {
            int glowAlpha = (int)(loadingBtnHoverAnim * (24.0f + pulse * 14.0f));
            gfx.m_280509_(x - 2, y - 2, x + w + 2, y + h + 2, glowAlpha << 24 | 0xAA2028);
        }
        int baseAlpha = (int)(130.0f + loadingBtnHoverAnim * 85.0f);
        int baseRgb = hovered ? 2229511 : 918791;
        gfx.m_280509_(x, y, x + w, y + h, baseAlpha << 24 | baseRgb);
        int barW = 3;
        int barH = Math.max(4, (int)((float)h * (0.4f + loadingBtnHoverAnim * 0.6f)));
        int barY = y + (h - barH) / 2;
        int barAlpha = (int)(130.0f + loadingBtnHoverAnim * 125.0f);
        int barRgb = hovered ? 16725317 : 11149352;
        gfx.m_280509_(x, barY, x + barW, barY + barH, barAlpha << 24 | barRgb);
        int borderAlpha = (int)(65.0f + loadingBtnHoverAnim * 150.0f);
        int borderRgb = hovered ? 16726088 : 6690846;
        int borderColor = borderAlpha << 24 | borderRgb;
        gfx.m_280509_(x, y, x + w, y + 1, borderColor);
        gfx.m_280509_(x, y + h - 1, x + w, y + h, borderColor);
        gfx.m_280509_(x, y, x + 1, y + h, borderColor);
        gfx.m_280509_(x + w - 1, y, x + w, y + h, borderColor);
        if (loadingBtnHoverAnim > 0.08f) {
            int cornerAlpha = (int)(loadingBtnHoverAnim * 230.0f);
            int cornerColor = cornerAlpha << 24 | 0xFFFF4450;
            gfx.m_280509_(x + w - 4, y, x + w, y + 1, cornerColor);
            gfx.m_280509_(x + w - 1, y, x + w, y + 4, cornerColor);
            gfx.m_280509_(x, y + h - 1, x + 4, y + h, cornerColor);
            gfx.m_280509_(x, y + h - 4, x + 1, y + h, cornerColor);
        }
        String string = (msg = widget.m_6035_()) == null ? (MainMenuScreen.isRussian() ? "\u041e\u0422\u041c\u0415\u041d\u0410" : "CANCEL") : (text = msg.getString());
        if (text.equalsIgnoreCase("cancel") && MainMenuScreen.isRussian()) {
            text = "\u041e\u0422\u041c\u0415\u041d\u0410";
        }
        int textShift = (int)(loadingBtnHoverAnim * 3.0f);
        int textColor = hovered ? 0xFFFFFF : 0xDDCCD0;
        int textX = x + w / 2 + textShift;
        int textY = y + (h - 8) / 2;
        if (loadingBtnHoverAnim > 0.15f) {
            int shadowAlpha = (int)(loadingBtnHoverAnim * 95.0f);
            gfx.m_280137_(font, text, textX + 1, textY + 1, shadowAlpha << 24 | 0x880000);
        }
        gfx.m_280137_(font, text, textX, textY, textColor);
    }

    @SubscribeEvent
    public static void onScreenMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
        Screen screen = event.getScreen();
        if (screen instanceof ConnectScreen || screen instanceof ProgressScreen || screen instanceof ReceivingLevelScreen || screen instanceof LevelLoadingScreen) {
            double mx = event.getMouseX();
            double my = event.getMouseY();
            for (Renderable renderable : screen.f_169369_) {
                if (!(renderable instanceof AbstractWidget)) continue;
                AbstractWidget widget = (AbstractWidget)renderable;
                if (!widget.f_93624_ || !widget.f_93623_ || !widget.m_5953_(mx, my)) continue;
                ModSounds.playClickSound();
                break;
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onScreenRender(ScreenEvent.Render.Post event) {
    }

    private static Screen createReplacementScreen() {
        if (!introShown) {
            introShown = true;
            return new IntroLoadingScreen();
        }
        return new MainMenuScreen();
    }

    private static boolean shouldReplaceWithCustomMenu(Screen screen) {
        return screen instanceof TitleScreen && !(screen instanceof MainMenuScreen);
    }

    private static void ensureBrandedLoadingOverlay(Minecraft minecraft) {
        LoadingOverlay overlay;
        Overlay overlay2 = minecraft.m_91265_();
        if (!(overlay2 instanceof LoadingOverlay) || (overlay = (LoadingOverlay)overlay2) instanceof BrandedLoadingOverlay) {
            return;
        }
        try {
            ReloadInstance reload = (ReloadInstance)LOADING_OVERLAY_RELOAD.get(overlay);
            Consumer onFinish = (Consumer)LOADING_OVERLAY_ON_FINISH.get(overlay);
            boolean fadeIn = LOADING_OVERLAY_FADE_IN.getBoolean(overlay);
            float progress = LOADING_OVERLAY_PROGRESS.getFloat(overlay);
            BrandedLoadingOverlay customOverlay = new BrandedLoadingOverlay(minecraft, reload, onFinish, fadeIn);
            customOverlay.setCurrentProgress(progress);
            minecraft.m_91150_((Overlay)customOverlay);
        }
        catch (IllegalAccessException exception) {
            MainLHS.LOGGER.error("Failed to replace loading overlay", (Throwable)exception);
        }
    }

    private static Component readComponent(Field field, Object target) {
        try {
            MutableComponent mutableComponent;
            Object value = field.get(target);
            if (value instanceof Component) {
                Component component = (Component)value;
                mutableComponent = component;
            } else {
                mutableComponent = Component.m_237119_();
            }
            return mutableComponent;
        }
        catch (IllegalAccessException exception) {
            return Component.m_237119_();
        }
    }

    private static int readInt(Field field, Object target) {
        try {
            return field.getInt(target);
        }
        catch (IllegalAccessException exception) {
            return 0;
        }
    }

    private static String getComponentText(Component component) {
        return component == null ? "" : component.getString().trim();
    }

    private static String joinStatus(String first, String second, String fallback) {
        if (!first.isBlank() && !second.isBlank()) {
            return first + " / " + second;
        }
        if (!first.isBlank()) {
            return first;
        }
        if (!second.isBlank()) {
            return second;
        }
        return fallback;
    }

    static {
        loadingBtnHoverAnim = 0.0f;
        loadingBtnHoverSoundPlayed = false;
        loadingBtnLastRenderTime = System.currentTimeMillis();
    }
}

