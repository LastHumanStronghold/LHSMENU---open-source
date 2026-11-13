/*
 * Decompiled with CFR 0.152.
 */
package com.mainlhs.common.scale;

public final class ScaleMath {
    public static final float DEFAULT_HEIGHT = 1.8f;

    private ScaleMath() {
    }

    public static float heightFromSizeParam(float sizeParam) {
        if (Math.abs(sizeParam - 1.0f) < 1.0E-4f) {
            return 1.8f;
        }
        return 1.8f + (sizeParam - 1.0f);
    }

    public static float getScaleFactor(float sizeParam) {
        return ScaleMath.heightFromSizeParam(sizeParam) / 1.8f;
    }
}

