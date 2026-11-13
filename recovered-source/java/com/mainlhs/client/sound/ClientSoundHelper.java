/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance$Attenuation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.mainlhs.client.sound;

import com.mainlhs.client.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public final class ClientSoundHelper {
    private ClientSoundHelper() {
    }

    public static void playHoverSound() {
        Minecraft mc = Minecraft.m_91087_();
        if (mc == null || mc.m_91106_() == null) {
            return;
        }
        try {
            float master = mc.f_91066_.m_92147_(SoundSource.MASTER);
            if (master > 0.001f) {
                mc.m_91106_().m_120367_((SoundInstance)new SimpleSoundInstance(((SoundEvent)ModSounds.BUTTON_HOVER.get()).m_11660_(), SoundSource.MASTER, 1.0f, 1.0f, SoundInstance.m_235150_(), false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true));
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void playClickSound() {
        Minecraft mc = Minecraft.m_91087_();
        if (mc == null || mc.m_91106_() == null) {
            return;
        }
        try {
            float master = mc.f_91066_.m_92147_(SoundSource.MASTER);
            if (master > 0.001f) {
                mc.m_91106_().m_120367_((SoundInstance)new SimpleSoundInstance(((SoundEvent)ModSounds.BUTTON_CLICK.get()).m_11660_(), SoundSource.MASTER, 1.0f, 1.0f, SoundInstance.m_235150_(), false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true));
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}

