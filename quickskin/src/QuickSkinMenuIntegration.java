package net.lhs.lobby;

import com.mainlhs.client.screen.MainMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Constructor;

/** Client-only Quick Skin entry points for the combined LHS menu. */
@Mod.EventBusSubscriber(modid = "mainlhs", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class QuickSkinMenuIntegration {
    private static final String QUICK_SKIN_SCREEN =
            "com.quickskin.mod.client.gui.screen.PlayerSkinMenuScreen";

    private QuickSkinMenuIntegration() {}

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof MainMenuScreen) && !(screen instanceof PauseScreen)) return;
        if (!ModList.get().isLoaded("quickskin")) return;

        int buttonWidth = 112;
        int buttonHeight = 20;
        int x = screen.f_96543_ - buttonWidth - 8;
        int y = screen.f_96544_ - buttonHeight - 8;

        event.addListener(Button.builder(Component.m_237115_("Quick Skin"), button -> openQuickSkin(screen))
                .bounds(x, y, buttonWidth, buttonHeight)
                .build());
    }

    private static void openQuickSkin(Screen parent) {
        try {
            Class<?> screenClass = Class.forName(QUICK_SKIN_SCREEN);
            Constructor<?> constructor = screenClass.getConstructor(Screen.class);
            Object skinScreen = constructor.newInstance(parent);
            if (skinScreen instanceof Screen target) {
                Minecraft.getInstance().setScreen(target);
            }
        } catch (ReflectiveOperationException | LinkageError exception) {
            System.err.println("[LHS] Unable to open Quick Skin screen: " + exception.getMessage());
        }
    }
}