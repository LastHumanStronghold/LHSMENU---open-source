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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

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

        try {
            event.addListener(createButton(
                    Component.m_237115_("Quick Skin"),
                    button -> openQuickSkin(screen),
                    x, y, buttonWidth, buttonHeight));
        } catch (ReflectiveOperationException | LinkageError exception) {
            System.err.println("[LHS] Unable to create Quick Skin button: " + exception);
        }
    }

    /*
     * The combined JAR is already SRG-remapped. This integration class is
     * injected after that step, so direct calls to mapped Button methods would
     * retain their development names and fail at runtime. Resolve the methods
     * by their stable signatures instead of linking to their names.
     */
    private static Button createButton(
            Component label, Button.OnPress action,
            int x, int y, int width, int height) throws ReflectiveOperationException {

        Method factory = Arrays.stream(Button.class.getDeclaredMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getParameterCount() == 2)
                .filter(method -> method.getParameterTypes()[0].isAssignableFrom(label.getClass()))
                .filter(method -> method.getParameterTypes()[1].isAssignableFrom(action.getClass())
                        || method.getParameterTypes()[1].isInstance(action))
                .filter(method -> method.getReturnType().getName().contains("Button$Builder"))
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Button builder factory"));

        factory.setAccessible(true);
        Object builder = factory.invoke(null, label, action);
        Method bounds = Arrays.stream(builder.getClass().getDeclaredMethods())
                .filter(method -> method.getParameterCount() == 4)
                .filter(method -> Arrays.stream(method.getParameterTypes())
                        .allMatch(type -> type == int.class))
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Button builder bounds"));
        bounds.setAccessible(true);
        Object positionedBuilder = bounds.invoke(builder, x, y, width, height);

        Method build = Arrays.stream(positionedBuilder.getClass().getDeclaredMethods())
                .filter(method -> method.getParameterCount() == 0)
                .filter(method -> Button.class.isAssignableFrom(method.getReturnType()))
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("Button builder build"));
        build.setAccessible(true);
        return (Button) build.invoke(positionedBuilder);
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