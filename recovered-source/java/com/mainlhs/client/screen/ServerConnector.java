/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.ConnectScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.client.multiplayer.resolver.ServerAddress
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.mainlhs.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public final class ServerConnector {
    private static final String SERVER_HOST = "144.31.136.75";
    private static final int SERVER_PORT = 25566;
    private static final String SERVER_ADDR = "144.31.136.75:25566";

    private ServerConnector() {
    }

    public static String getServerAddress() {
        return SERVER_ADDR;
    }

    public static void connect(Screen parent) {
        ServerConnector.connect(parent, SERVER_ADDR);
    }

    public static void connect(Screen parent, String address) {
        if (address == null || address.isBlank()) {
            return;
        }
        String addr = address.trim();
        ServerData serverData = new ServerData("Last Human Stronghold", addr, false);
        try {
            ConnectScreen.m_278792_((Screen)parent, (Minecraft)Minecraft.m_91087_(), (ServerAddress)ServerAddress.m_171864_((String)addr), (ServerData)serverData, (boolean)false);
        }
        catch (Exception e) {
            try {
                Object withPort = addr.contains(":") ? addr : addr + ":25566";
                ConnectScreen.m_278792_((Screen)parent, (Minecraft)Minecraft.m_91087_(), (ServerAddress)ServerAddress.m_171864_((String)withPort), (ServerData)serverData, (boolean)false);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }
}

