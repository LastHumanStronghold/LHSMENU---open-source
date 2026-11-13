/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.RegisterClientReloadListenersEvent
 *  net.minecraftforge.eventbus.api.EventPriority
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
 *  net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent
 */
package com.mainlhs.compat;

import com.mainlhs.compat.LittleTilesCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(modid="mainlhs", bus=Mod.EventBusSubscriber.Bus.MOD, value={Dist.CLIENT})
public class ModpackCompat {
    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        LittleTilesCompat.ensureConfigLoaded();
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onClientSetup(FMLClientSetupEvent event) {
        LittleTilesCompat.ensureConfigLoaded();
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        LittleTilesCompat.ensureConfigLoaded();
    }
}

