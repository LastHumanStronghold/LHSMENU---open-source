/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package com.mainlhs.client.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import net.minecraft.client.Minecraft;

public final class ClientConfig {
    private static final String FILE_NAME = "mainlhs.properties";
    private static final String KEY_MENU_VOL = "menuMusicVolume";
    private static final String KEY_GRAPHICS_MODE = "graphicsMode";
    private static float menuMusicVolume = 0.7f;
    private static int graphicsMode = 2;

    private ClientConfig() {
    }

    public static void load() {
        try {
            String graphics;
            File gameDir = Minecraft.m_91087_().f_91069_;
            File cfg = new File(new File(gameDir, "config"), FILE_NAME);
            if (!cfg.exists()) {
                return;
            }
            Properties p = new Properties();
            try (FileInputStream in = new FileInputStream(cfg);){
                p.load(in);
            }
            String v = p.getProperty(KEY_MENU_VOL);
            if (v != null) {
                menuMusicVolume = Float.parseFloat(v);
            }
            if ((graphics = p.getProperty(KEY_GRAPHICS_MODE)) != null) {
                graphicsMode = ClientConfig.clampGraphicsMode(Integer.parseInt(graphics));
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void save() {
        try {
            File gameDir = Minecraft.m_91087_().f_91069_;
            File cfgDir = new File(gameDir, "config");
            if (!cfgDir.exists()) {
                cfgDir.mkdirs();
            }
            File cfg = new File(cfgDir, FILE_NAME);
            Properties p = new Properties();
            p.setProperty(KEY_MENU_VOL, Float.toString(menuMusicVolume));
            p.setProperty(KEY_GRAPHICS_MODE, Integer.toString(graphicsMode));
            try (FileOutputStream out = new FileOutputStream(cfg);){
                p.store(out, "MainLHS client config");
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    public static float getMenuMusicVolume() {
        return menuMusicVolume;
    }

    public static void setMenuMusicVolume(float vol) {
        menuMusicVolume = Math.max(0.0f, Math.min(1.0f, vol));
    }

    public static int getGraphicsMode() {
        return graphicsMode;
    }

    public static void setGraphicsMode(int mode) {
        graphicsMode = ClientConfig.clampGraphicsMode(mode);
    }

    public static int nextGraphicsMode() {
        graphicsMode = (graphicsMode + 1) % 4;
        return graphicsMode;
    }

    public static String getGraphicsModeName(boolean russian) {
        return switch (graphicsMode) {
            case 0 -> {
                if (russian) {
                    yield "\u0412\u044b\u043a\u043b";
                }
                yield "Off";
            }
            case 1 -> {
                if (russian) {
                    yield "\u041b\u0451\u0433\u043a\u0430\u044f";
                }
                yield "Low";
            }
            case 3 -> {
                if (russian) {
                    yield "\u041a\u0438\u043d\u043e";
                }
                yield "Cinematic";
            }
            default -> russian ? "\u0411\u0430\u043b\u0430\u043d\u0441" : "Balanced";
        };
    }

    private static int clampGraphicsMode(int mode) {
        return Math.max(0, Math.min(3, mode));
    }
}

