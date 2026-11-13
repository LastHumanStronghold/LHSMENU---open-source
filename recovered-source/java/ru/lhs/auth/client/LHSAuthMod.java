/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.fml.DistExecutor
 *  net.minecraftforge.fml.common.Mod
 */
package ru.lhs.auth.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import ru.lhs.auth.client.LHSAuthClient;

@Mod(value="lhs_auth")
public final class LHSAuthMod {
    public LHSAuthMod() {
        DistExecutor.safeRunWhenOn((Dist)Dist.CLIENT, () -> LHSAuthClient::init);
    }
}
