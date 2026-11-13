/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.fml.DistExecutor
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.IForgeRegistry
 *  net.minecraftforge.registries.RegistryObject
 */
package com.mainlhs.client.sound;

import com.mainlhs.client.sound.ClientSoundHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create((IForgeRegistry)ForgeRegistries.SOUND_EVENTS, (String)"mainlhs");
    public static final RegistryObject<SoundEvent> BUTTON_HOVER = ModSounds.register("ui.btn_hover");
    public static final RegistryObject<SoundEvent> BUTTON_CLICK = ModSounds.register("ui.btn_click");
    public static final RegistryObject<SoundEvent> MENU_BGM = ModSounds.register("music.menu_bgm");

    private ModSounds() {
    }

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.m_262824_((ResourceLocation)new ResourceLocation("mainlhs", name)));
    }

    public static void playHoverSound() {
        DistExecutor.unsafeRunWhenOn((Dist)Dist.CLIENT, () -> ClientSoundHelper::playHoverSound);
    }

    public static void playClickSound() {
        DistExecutor.unsafeRunWhenOn((Dist)Dist.CLIENT, () -> ClientSoundHelper::playClickSound);
    }
}

