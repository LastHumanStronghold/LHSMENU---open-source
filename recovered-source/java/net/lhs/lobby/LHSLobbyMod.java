/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.fml.DistExecutor
 *  net.minecraftforge.fml.common.Mod
 */
package net.lhs.lobby;

import net.lhs.lobby.LHSLobbyClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(value="lhs_lobby")
public final class LHSLobbyMod {
    public static final String MOD_ID = "lhs_lobby";
    public static final String OPEN_MARKER = "LHS_LOBBY_OPEN_4F91";

    public LHSLobbyMod() {
        DistExecutor.safeRunWhenOn((Dist)Dist.CLIENT, () -> LHSLobbyClient::init);
    }
}
