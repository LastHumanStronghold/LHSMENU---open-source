/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraftforge.client.event.ClientPlayerNetworkEvent$LoggingIn
 *  net.minecraftforge.client.event.ClientPlayerNetworkEvent$LoggingOut
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.TickEvent$ClientTickEvent
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 */
package ru.lhs.auth.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.lhs.auth.client.AuthApi;
import ru.lhs.auth.client.AuthScreen;

public final class LHSAuthClient {
    private static boolean authenticated;

    private LHSAuthClient() {
    }

    public static void init() {
        authenticated = false;
        MinecraftForge.EVENT_BUS.register(LHSAuthClient.class);
    }

    public static void setAuthenticated(boolean value) {
        authenticated = value;
    }

    @SubscribeEvent
    public static void onJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        authenticated = false;
        Minecraft.m_91087_().execute(() -> Minecraft.m_91087_().m_91152_((Screen)new AuthScreen()));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.m_91087_();
        if (mc.f_91074_ != null && !authenticated && !(mc.f_91080_ instanceof AuthScreen)) {
            mc.m_91152_((Screen)new AuthScreen());
        }
    }

    @SubscribeEvent
    public static void onLeave(ClientPlayerNetworkEvent.LoggingOut event) {
        authenticated = false;
        AuthApi.clearSession();
    }
}
