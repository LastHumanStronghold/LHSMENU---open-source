/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package com.mainlhs.client;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value=Dist.CLIENT)
public final class ClientScaleCache {
    private static final Map<UUID, Float> SIZES = new ConcurrentHashMap<UUID, Float>();

    private ClientScaleCache() {
    }

    public static float get(UUID playerId) {
        return SIZES.getOrDefault(playerId, Float.valueOf(1.0f)).floatValue();
    }

    public static void set(UUID playerId, float sizeParam) {
        if (Math.abs(sizeParam - 1.0f) < 1.0E-4f) {
            SIZES.remove(playerId);
        } else {
            SIZES.put(playerId, Float.valueOf(sizeParam));
        }
    }

    public static void clear(UUID playerId) {
        SIZES.remove(playerId);
    }
}

