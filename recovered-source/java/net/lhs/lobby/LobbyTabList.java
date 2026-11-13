/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.PlayerFaceRenderer
 *  net.minecraft.client.multiplayer.ClientPacketListener
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.GameType
 *  net.minecraft.world.scores.PlayerTeam
 *  net.minecraft.world.scores.Team
 *  net.minecraftforge.client.event.ClientPlayerNetworkEvent$LoggingIn
 *  net.minecraftforge.client.event.ClientPlayerNetworkEvent$LoggingOut
 *  net.minecraftforge.client.event.RenderGuiOverlayEvent$Post
 *  net.minecraftforge.client.event.RenderGuiOverlayEvent$Pre
 *  net.minecraftforge.client.gui.overlay.VanillaGuiOverlay
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 */
package net.lhs.lobby;

import com.mainlhs.client.screen.MainMenuScreen;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class LobbyTabList {
    private static boolean lobbyActive;
    private static boolean tabOpen;
    private static final Comparator<PlayerInfo> ORDER;
    private static final int C_PANEL = -267778556;
    private static final int C_HEADER = -1072559353;
    private static final int C_BORDER = -10075624;
    private static final int C_MUTED = 11575184;
    private static final int C_ORANGE = 16751164;
    private static final int C_ORANGE_LIGHT = 16760968;

    private LobbyTabList() {
    }

    public static void setLobbyActive(boolean bl) {
        lobbyActive = bl;
        if (!bl) {
            tabOpen = false;
        }
    }

    public static void closeTab() {
        tabOpen = false;
    }

    public static void toggleTab() {
        if (lobbyActive) {
            tabOpen = !tabOpen;
        }
    }

    @SubscribeEvent
    public static void onHudRender(RenderGuiOverlayEvent.Post post) {
        Minecraft minecraft;
        if (lobbyActive && tabOpen && post.getOverlay().id().equals((Object)VanillaGuiOverlay.HOTBAR.id()) && (minecraft = Minecraft.m_91087_()) != null && minecraft.f_91074_ != null && minecraft.f_91074_.f_108617_ != null) {
            LobbyTabList.render(post.getGuiGraphics(), minecraft);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre pre) {
        if (!lobbyActive || !pre.getOverlay().id().equals((Object)VanillaGuiOverlay.PLAYER_LIST.id())) {
            return;
        }
        pre.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLogin(ClientPlayerNetworkEvent.LoggingIn loggingIn) {
        LobbyTabList.setLobbyActive(true);
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut loggingOut) {
        LobbyTabList.setLobbyActive(false);
    }

    private static void render(GuiGraphics guiGraphics, Minecraft minecraft) {
        int n;
        int n2;
        ClientPacketListener clientPacketListener = minecraft.f_91074_.f_108617_;
        ArrayList<PlayerInfo> arrayList = new ArrayList<PlayerInfo>(clientPacketListener.m_246170_());
        arrayList.sort(ORDER);
        Font font = minecraft.f_91062_;
        int n3 = 60;
        for (n2 = 0; n2 < arrayList.size(); ++n2) {
            n3 = Math.max(n3, font.m_92852_((FormattedText)LobbyTabList.name((PlayerInfo)arrayList.get(n2))));
        }
        n3 = Math.min(n3, 150);
        n2 = arrayList.size();
        int n4 = 1;
        int n5 = n2;
        while (n5 > 20) {
            n5 = (n2 + ++n4 - 1) / n4;
        }
        int n6 = 16;
        int n7 = 21;
        int n8 = 13;
        int n9 = n6 + 8 + n3 + 9 + n8 + 14;
        int n10 = 7;
        int n11 = 10;
        int n12 = 31;
        int n13 = 17;
        String string = "LHS  //  LOBBY";
        String string2 = MainMenuScreen.text("\u041e\u041d\u041b\u0410\u0419\u041d  ", "ONLINE  ") + String.format("%02d", n2);
        String string3 = MainMenuScreen.text("\u0421\u041f\u0418\u0421\u041e\u041a \u0418\u0413\u0420\u041e\u041a\u041e\u0412", "PLAYER LIST");
        String string4 = MainMenuScreen.text("\u041d\u0410\u0416\u041c\u0418 TAB", "PRESS TAB");
        int n14 = n4 * n9 + (n4 - 1) * n10;
        int n15 = Math.max(font.m_92895_(string) + font.m_92895_(string2) + 40, font.m_92895_(string3) + 24);
        int n16 = Math.max(n14 + n11 * 2, n15);
        int n17 = n12 + 6 + n5 * n7 + n13;
        int n18 = guiGraphics.m_280182_();
        n16 = Math.min(n16, n18 - 8);
        int n19 = Math.max(4, (n18 - n16) / 2);
        int n20 = 8;
        int n21 = n19 + n16;
        int n22 = n20 + n17;
        int n23 = n19 + (n16 - n14) / 2;
        guiGraphics.m_280509_(n19 + 3, n20 + 4, n21 + 3, n22 + 4, 0x77000000);
        guiGraphics.m_280509_(n19 - 1, n20 - 1, n21 + 1, n22 + 1, -12900071);
        guiGraphics.m_280509_(n19, n20, n21, n22, -10075624);
        guiGraphics.m_280509_(n19, n20, n21, n22, -267778556);
        guiGraphics.m_280509_(n19, n20, n21, n20 + n12, -1072559353);
        guiGraphics.m_280509_(n19, n20, n19 + 3, n20 + n12, 16751164);
        guiGraphics.m_280509_(n19 + 3, n20 + n12 - 2, n21 - 3, n20 + n12 - 1, -12965864);
        guiGraphics.m_280509_(n19 + 3, n20 + n12 - 1, n21 - 3, n20 + n12, 16751164);
        guiGraphics.m_280056_(font, string, n19 + 12, n20 + 6, 16751164, true);
        int n24 = n21 - 12 - font.m_92895_(string2);
        guiGraphics.m_280056_(font, string2, n24, n20 + 6, 11575184, true);
        guiGraphics.m_280056_(font, string3, n19 + 12, n20 + 19, 11575184, false);
        for (n = 0; n < n2; ++n) {
            int n25 = n / n5;
            int n26 = n % n5;
            int n27 = n23 + n25 * (n9 + n10);
            int n28 = n20 + n12 + 5 + n26 * n7;
            PlayerInfo playerInfo = (PlayerInfo)arrayList.get(n);
            guiGraphics.m_280509_(n27, n28, n27 + n9, n28 + n7 - 1, (n & 1) == 0 ? 974589963 : 605491211);
            guiGraphics.m_280509_(n27, n28, n27 + 2, n28 + n7 - 1, 16760968);
            int n29 = n28 + (n7 - 1 - n6) / 2;
            PlayerFaceRenderer.m_280029_((GuiGraphics)guiGraphics, (ResourceLocation)playerInfo.m_105337_(), (int)(n27 + 5), (int)n29, (int)n6, (boolean)false, (boolean)false);
            int n30 = n27 + 5 + n6 + 7;
            int n31 = n28 + (n7 - 8) / 2;
            guiGraphics.m_280430_(font, LobbyTabList.name(playerInfo), n30, n31, playerInfo.m_105325_() == GameType.SPECTATOR ? 11575184 : -791587);
            int n32 = playerInfo.m_105330_();
            int n33 = LobbyTabList.pingBars(n32);
            int n34 = n27 + n9 - 6 - 11;
            int n35 = n28 + n7 - 4;
            int n36 = LobbyTabList.pingColor(n32);
            for (int i = 0; i < 5; ++i) {
                int n37 = 3 + i * 2;
                int n38 = i < n33 ? n36 : 1429877549;
                guiGraphics.m_280509_(n34 + i * 2, n35 - n37, n34 + i * 2 + 1, n35, n38);
            }
        }
        n = n22 - n13;
        guiGraphics.m_280509_(n19 + 8, n, n21 - 8, n + 1, 1715087384);
        guiGraphics.m_280056_(font, "LHS NETWORK", n19 + 11, n + 5, -8229544, false);
        guiGraphics.m_280056_(font, string4, n21 - 11 - font.m_92895_(string4), n + 5, 16760968, false);
    }

    private static Component name(PlayerInfo playerInfo) {
        MutableComponent mutableComponent = playerInfo.m_105342_() != null ? playerInfo.m_105342_().m_6881_() : PlayerTeam.m_83348_((Team)playerInfo.m_105340_(), (Component)Component.m_237113_((String)playerInfo.m_105312_().getName()));
        return playerInfo.m_105325_() == GameType.SPECTATOR ? mutableComponent.m_130940_(ChatFormatting.ITALIC) : mutableComponent;
    }

    private static int pingIndex(int n) {
        if (n < 0) {
            return 5;
        }
        if (n < 150) {
            return 0;
        }
        if (n < 300) {
            return 1;
        }
        if (n < 600) {
            return 2;
        }
        return n < 1000 ? 3 : 4;
    }

    private static int pingBars(int n) {
        if (n < 0) {
            return 1;
        }
        if (n < 150) {
            return 5;
        }
        if (n < 300) {
            return 4;
        }
        if (n < 600) {
            return 3;
        }
        if (n < 1000) {
            return 2;
        }
        return 1;
    }

    private static int pingColor(int n) {
        switch (LobbyTabList.pingIndex(n)) {
            case 0: {
                return -8658821;
            }
            case 1: {
                return -859812;
            }
            case 2: {
                return -20420;
            }
            case 3: {
                return -1873318;
            }
        }
        return -3784640;
    }

    static {
        ORDER = Comparator.comparingInt(playerInfo -> playerInfo.m_105325_() == GameType.SPECTATOR ? 1 : 0).thenComparing(playerInfo -> playerInfo.m_105312_().getName(), String.CASE_INSENSITIVE_ORDER);
    }
}
