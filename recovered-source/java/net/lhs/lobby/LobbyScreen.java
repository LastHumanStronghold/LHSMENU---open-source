/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.util.Mth
 */
package net.lhs.lobby;

import com.mainlhs.client.screen.MainMenuScreen;
import com.mainlhs.client.sound.ModSounds;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.lhs.lobby.LobbyTabList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public final class LobbyScreen
extends Screen {
    private static final long START = System.currentTimeMillis();
    private static final int PARTICLES = 56;
    private static final int C_BG = 656900;
    private static final int C_CARD = 1182471;
    private static final int C_CARD_HOT = 2758924;
    private static final int C_BORDER = 6701592;
    private static final int C_TEXT = 0xFFF6EE;
    private static final int C_MUTED = 11575184;
    private static final int C_SUB = 13937013;
    private static final int C_ORANGE = 16751164;
    private static final int C_ORANGE_LIGHT = 16760968;
    private static final String[] FISH = new String[]{"...DDD........", "..AAAAAA....D.", ".AAAAAAAAA.DD.", "AEKAAAAAAAADD.", "ABBBBBBBBBADD.", ".BBBBBBBBB.DD.", "..BBBBBB....D."};
    private static final String[] ZOMBIE = new String[]{"............", ".GGGGGGGGGG.", ".GHHGGGGHHG.", ".GKKGGGGKKG.", ".GRRGGGGRRG.", ".GGGGDDGGGG.", ".GGGDDDDGGG.", ".GGDDDDDDGG.", ".GGDWDDWDGG.", ".GGGDDDDGGG.", "..GGGGGGGG..", "............"};
    private static final String[] BUNKER = new String[]{"............", "....CCCC....", "..CCCCCCCC..", ".CCCCCCCCCC.", ".CCDDDDDDCC.", ".CCDYYYYDCC.", ".CCDYYYYDCC.", ".CCDYYHYDCC.", ".CCDYYYYDCC.", ".CCDDDDDDCC.", "CCCCCCCCCCCC", "............"};
    private static final String[] SWORDS = new String[]{"BB........BB", ".BB......BB.", "..BB....BB..", "...BB..BB...", "....BBBB....", ".....BB.....", "....BBBB....", "..HHBBBBHH..", ".HH.DBBD.HH.", "HH...DD...HH", "D..........D", "............"};
    private static final int N = 4;
    private final Mode[] modes;
    private final float[] hover = new float[4];
    private final boolean[] wasActive = new boolean[4];
    private int focus = -1;
    private int lastMx = -1;
    private int lastMy = -1;
    private long openedAt = -1L;
    private long lastFrame = System.currentTimeMillis();
    private int px;
    private int py;
    private int pw;
    private int ph;
    private int cw;
    private int ch;
    private int headerH;
    private final int[] cx = new int[4];
    private final int[] cy = new int[4];

    public LobbyScreen() {
        super((Component)Component.m_237113_((String)"LHS // SELECT MODE"));
        this.modes = new Mode[]{new Mode("fish", "LHS FISHING", MainMenuScreen.text("\u0420\u044b\u0431\u0430\u043b\u043a\u0430 \u043d\u0430 \u0440\u0435\u0430\u043a\u0446\u0438\u044e \u0438 \u043a\u043e\u043d\u0442\u0440\u043e\u043b\u044c", "Fishing built on reaction and control"), MainMenuScreen.text("\u041e\u0422\u041a\u0420\u042b\u0422\u042c \u041c\u0418\u0420 FISH", "OPEN FISH WORLD"), 16751164, FISH, false, false), new Mode("zombievoln", "ZOMBIEVOLN", MainMenuScreen.text("\u0412\u044b\u0436\u0438\u0432\u0438 \u043f\u0440\u043e\u0442\u0438\u0432 \u0432\u043e\u043b\u043d \u0437\u043e\u043c\u0431\u0438", "Survive the zombie waves"), MainMenuScreen.text("\u041f\u0415\u0420\u0415\u0419\u0422\u0418 \u041a \u0412\u042b\u0416\u0418\u0412\u0410\u041d\u0418\u042e", "ENTER SURVIVAL"), 16739898, ZOMBIE, true, false), new Mode("bunker", "BUNKER", MainMenuScreen.text("\u0420\u0435\u0436\u0438\u043c \u0433\u043e\u0442\u043e\u0432\u0438\u0442\u0441\u044f \u043a \u0437\u0430\u043f\u0443\u0441\u043a\u0443", "Mode is getting ready"), "", 13148256, BUNKER, false, true), new Mode("crossbattle", "CROSS-BATTLE", MainMenuScreen.text("\u0420\u0435\u0436\u0438\u043c \u0433\u043e\u0442\u043e\u0432\u0438\u0442\u0441\u044f \u043a \u0437\u0430\u043f\u0443\u0441\u043a\u0443", "Mode is getting ready"), "", 13148256, SWORDS, false, true)};
    }

    private void layout() {
        this.pw = Math.min(560, this.f_96543_ - 20);
        this.ph = Math.min(300, this.f_96544_ - 16);
        this.px = (this.f_96543_ - this.pw) / 2;
        this.py = (this.f_96544_ - this.ph) / 2;
        this.headerH = this.ph >= 230 ? 78 : 62;
        int n = this.py + this.headerH;
        int n2 = this.py + this.ph - 28;
        int n3 = Math.max(40, n2 - n);
        this.cw = (this.pw - 32 - 10) / 2;
        this.ch = (n3 - 10) / 2;
        for (int i = 0; i < 4; ++i) {
            this.cx[i] = this.px + 16 + i % 2 * (this.cw + 10);
            this.cy[i] = n + i / 2 * (this.ch + 10);
        }
    }

    private int cardAt(double d, double d2) {
        for (int i = 0; i < 4; ++i) {
            if (!(d >= (double)this.cx[i]) || !(d <= (double)(this.cx[i] + this.cw)) || !(d2 >= (double)this.cy[i]) || !(d2 <= (double)(this.cy[i] + this.ch))) continue;
            return i;
        }
        return -1;
    }

    private static float ease(float f) {
        float f2 = Mth.m_14036_((float)f, (float)0.0f, (float)1.0f);
        float f3 = 1.0f - f2;
        return 1.0f - f3 * f3 * f3;
    }

    private static int al(float f) {
        return Mth.m_14045_((int)((int)f), (int)0, (int)255);
    }

    private static int col(float f, int n) {
        return LobbyScreen.al(f) << 24 | n;
    }

    private static int mix(int n, int n2, float f) {
        float f2 = Mth.m_14036_((float)f, (float)0.0f, (float)1.0f);
        int n3 = (int)((float)(n >> 16 & 0xFF) * (1.0f - f2) + (float)(n2 >> 16 & 0xFF) * f2);
        int n4 = (int)((float)(n >> 8 & 0xFF) * (1.0f - f2) + (float)(n2 >> 8 & 0xFF) * f2);
        int n5 = (int)((float)(n & 0xFF) * (1.0f - f2) + (float)(n2 & 0xFF) * f2);
        return n3 << 16 | n4 << 8 | n5;
    }

    private void txt(GuiGraphics guiGraphics, String string, int n, int n2, int n3, float f) {
        int n4 = LobbyScreen.al(f);
        if (n4 >= 10) {
            guiGraphics.m_280056_(this.f_96547_, string, n, n2, n4 << 24 | n3, true);
        }
    }

    private void ctxt(GuiGraphics guiGraphics, String string, int n, int n2, int n3, float f) {
        this.txt(guiGraphics, string, n - this.f_96547_.m_92895_(string) / 2, n2, n3, f);
    }

    private List<String> wrap(String string, int n, int n2) {
        ArrayList<String> arrayList = new ArrayList<String>();
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : string.split(" ")) {
            String string3;
            String string4 = string3 = stringBuilder.length() == 0 ? string2 : String.valueOf(stringBuilder) + " " + string2;
            if (stringBuilder.length() != 0 && this.f_96547_.m_92895_(string3) > n) {
                arrayList.add(stringBuilder.toString());
                stringBuilder = new StringBuilder(string2);
                continue;
            }
            stringBuilder = new StringBuilder(string3);
        }
        if (stringBuilder.length() > 0) {
            arrayList.add(stringBuilder.toString());
        }
        while (arrayList.size() > n2) {
            arrayList.remove(arrayList.size() - 1);
        }
        return arrayList;
    }

    private void box(GuiGraphics guiGraphics, int n, int n2, int n3, int n4, int n5) {
        guiGraphics.m_280509_(n, n2, n3, n2 + 1, n5);
        guiGraphics.m_280509_(n, n4 - 1, n3, n4, n5);
        guiGraphics.m_280509_(n, n2, n + 1, n4, n5);
        guiGraphics.m_280509_(n3 - 1, n2, n3, n4, n5);
    }

    public void m_88315_(GuiGraphics guiGraphics, int n, int n2, float f) {
        float f2;
        int n3;
        int n4;
        long l = System.currentTimeMillis();
        if (this.openedAt < 0L) {
            this.openedAt = l;
        }
        float f3 = Math.min((float)(l - this.lastFrame) / 1000.0f, 0.1f);
        this.lastFrame = l;
        float f4 = (float)(l - this.openedAt) / 1000.0f;
        float f5 = (float)(l - START) / 1000.0f;
        float f6 = LobbyScreen.ease(f4 / 0.3f);
        this.layout();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (n != this.lastMx || n2 != this.lastMy) {
            this.focus = this.cardAt(n, n2);
            this.lastMx = n;
            this.lastMy = n2;
        }
        for (n4 = 0; n4 < 4; ++n4) {
            int n5 = n3 = this.focus == n4 ? 1 : 0;
            if (n3 != 0 && !this.wasActive[n4] && !this.modes[n4].locked) {
                ModSounds.playHoverSound();
            }
            this.wasActive[n4] = n3;
            f2 = n3 != 0 ? 1.0f : 0.0f;
            this.hover[n4] = this.hover[n4] + (f2 - this.hover[n4]) * Math.min(1.0f, f3 * (n3 != 0 ? 12.0f : 8.0f));
        }
        this.drawBackdrop(guiGraphics, f5, f6, n, n2);
        n4 = (int)((1.0f - f6) * 10.0f);
        this.py += n4;
        for (n3 = 0; n3 < 4; ++n3) {
            this.cy[n3] = this.cy[n3] + n4;
        }
        this.drawPanel(guiGraphics, f5, f6);
        this.drawHeader(guiGraphics, f5, f6);
        for (n3 = 0; n3 < 4; ++n3) {
            f2 = LobbyScreen.ease((f4 - 0.1f - 0.1f * (float)n3) / 0.35f);
            this.drawCard(guiGraphics, n3, f2 * f6, f5);
        }
        this.drawFooter(guiGraphics, f6);
        this.py -= n4;
        for (n3 = 0; n3 < 4; ++n3) {
            this.cy[n3] = this.cy[n3] - n4;
        }
    }

    private void drawBackdrop(GuiGraphics guiGraphics, float f, float f2, int n, int n2) {
        int n3;
        float f3;
        float f4;
        float f5;
        int n4 = this.f_96543_;
        int n5 = this.f_96544_;
        guiGraphics.m_280509_(0, 0, n4, n5, LobbyScreen.col(180.0f * f2, 328450));
        float f6 = ((float)n / (float)Math.max(1, n4) - 0.5f) * 20.0f;
        float f7 = ((float)n2 / (float)Math.max(1, n5) - 0.5f) * 14.0f;
        float f8 = (float)n4 * 0.5f + f6 * 0.4f;
        float f9 = (float)n5 * 0.46f + f7 * 0.4f;
        float f10 = (float)(Math.sin(f * 0.8f) * 0.5 + 0.5);
        float f11 = (float)n4 * 0.7f;
        float f12 = (float)n5 * 0.65f;
        for (int i = 0; i < n5; i += 2) {
            f5 = ((float)i - f9) / f12;
            float f13 = f5 * f5;
            if (f13 >= 1.0f) continue;
            int n6 = (int)((double)f11 * Math.sqrt(1.0f - f13));
            int n7 = Math.max(0, (int)f8 - n6);
            int n8 = Math.min(n4, (int)f8 + n6);
            if (n8 <= n7) continue;
            f4 = 1.0f - Math.abs(f5);
            f3 = (f4 = f4 * f4 * (3.0f - 2.0f * f4)) * (0.2f + f10 * 0.06f) * f2;
            int n9 = LobbyScreen.col(f3 * 255.0f, (int)(f3 * 140.0f * 1.4f) << 16 | (int)(f3 * 90.0f * 1.4f) << 8 | (int)(f3 * 23.0f * 1.4f));
            if (n9 >>> 24 <= 0) continue;
            guiGraphics.m_280509_(n7, i, n8, i + 2, n9);
        }
        float f14 = (float)n5 + 40.0f;
        f5 = (float)n4 + 60.0f;
        for (n3 = 0; n3 < 56; ++n3) {
            int n10;
            float f15 = LobbyScreen.fract((double)(n3 + 1) * 0.618033988749895);
            float f16 = LobbyScreen.fract((double)(n3 + 1) * 0.754877666246693);
            float f17 = LobbyScreen.fract((double)(n3 + 1) * 0.569840296317204);
            f4 = 0.2f + f17 * 0.8f;
            f3 = 12.0f + f17 * 26.0f;
            float f18 = (float)Math.sin((float)(n3 + 1) * 13.37f) * (3.0f + f17 * 7.0f);
            float f19 = (f16 * f14 + f * f3) % f14;
            float f20 = (float)n5 + 20.0f - f19 + f7 * f4 * 0.7f;
            float f21 = (float)Math.sin(f * (0.55f + f17 * 0.6f) + (float)n3 * 2.39f) * (12.0f + f17 * 14.0f);
            float f22 = (f15 * f5 + f21 + f * f18) % f5;
            if (f22 < 0.0f) {
                f22 += f5;
            }
            float f23 = f22 - 30.0f + f6 * f4 * 0.7f;
            if (f20 < -10.0f || f20 > (float)(n5 + 10) || f23 < -10.0f || f23 > (float)(n4 + 10)) continue;
            float f24 = (float)(Math.sin(f * 3.8f + (float)n3 * 2.1f) * (double)0.3f + (double)0.7f);
            float f25 = (35.0f + f4 * 140.0f) * f24 * f2;
            int n11 = f4 > 0.8f ? 2 : 1;
            switch (n3 % 4) {
                case 0: {
                    n10 = 12822340;
                    break;
                }
                case 1: {
                    int n12 = 13392430;
                    break;
                }
                case 2: {
                    int n13 = 11162912;
                    break;
                }
                default: {
                    int n14 = 8942880;
                }
            }
            n10 = n3 % 2 == 0 ? 16751164 : (n3 % 4 == 1 ? 16760968 : 13132826);
            guiGraphics.m_280509_((int)f23, (int)f20, (int)f23 + n11, (int)f20 + n11, LobbyScreen.col(f25, n10));
            if (!(f4 > 0.8f) || !(f25 > 60.0f)) continue;
            guiGraphics.m_280509_((int)f23 - 1, (int)f20 - 1, (int)f23 + n11 + 1, (int)f20 + n11 + 1, LobbyScreen.col(f25 / 4.0f, n10));
        }
        n3 = LobbyScreen.col(40.0f * f2, 8936224);
        int n15 = 12;
        int n16 = 16;
        guiGraphics.m_280509_(n15, n15, n15 + n16, n15 + 1, n3);
        guiGraphics.m_280509_(n15, n15, n15 + 1, n15 + n16, n3);
        guiGraphics.m_280509_(n4 - n15 - n16, n15, n4 - n15, n15 + 1, n3);
        guiGraphics.m_280509_(n4 - n15 - 1, n15, n4 - n15, n15 + n16, n3);
        guiGraphics.m_280509_(n15, n5 - n15 - 1, n15 + n16, n5 - n15, n3);
        guiGraphics.m_280509_(n15, n5 - n15 - n16, n15 + 1, n5 - n15, n3);
        guiGraphics.m_280509_(n4 - n15 - n16, n5 - n15 - 1, n4 - n15, n5 - n15, n3);
        guiGraphics.m_280509_(n4 - n15 - 1, n5 - n15 - n16, n4 - n15, n5 - n15, n3);
    }

    private void drawPanel(GuiGraphics guiGraphics, float f, float f2) {
        int n;
        int n2;
        int n3 = this.px;
        int n4 = this.py;
        int n5 = this.px + this.pw;
        int n6 = this.py + this.ph;
        for (n2 = 3; n2 >= 1; --n2) {
            guiGraphics.m_280509_(n3 - n2 * 2, n4 - n2 * 2, n5 + n2 * 2, n6 + n2 * 2, LobbyScreen.col(26.0f * f2 / (float)n2 * 1.4f, 0));
        }
        guiGraphics.m_280509_(n3, n4, n5, n6, LobbyScreen.col(110.0f * f2, 6701592));
        guiGraphics.m_280509_(n3 + 1, n4 + 1, n5 - 1, n6 - 1, LobbyScreen.col(236.0f * f2, 656900));
        n2 = Math.min(40, this.ph / 4);
        for (n = 0; n < n2; ++n) {
            float f3 = 1.0f - (float)n / (float)n2;
            guiGraphics.m_280509_(n3 + 1, n6 - 2 - n, n5 - 1, n6 - 1 - n, LobbyScreen.col(f3 * f3 * 14.0f * f2, 16751164));
        }
        guiGraphics.m_280509_(n3 + 5, n4 + 4, n5 - 5, n4 + 6, LobbyScreen.col(235.0f * f2, 16751164));
        n = Math.max(1, this.pw - 60);
        int n7 = n3 + 8 + (int)(f * 25.0f % (float)n);
        guiGraphics.m_280509_(n7, n4 + 4, Math.min(n7 + 42, n5 - 5), n4 + 6, LobbyScreen.col(200.0f * f2, 16771280));
        int n8 = LobbyScreen.col(235.0f * f2, 16756301);
        int n9 = 9;
        guiGraphics.m_280509_(n3, n4, n3 + n9, n4 + 1, n8);
        guiGraphics.m_280509_(n3, n4, n3 + 1, n4 + n9, n8);
        guiGraphics.m_280509_(n5 - n9, n4, n5, n4 + 1, n8);
        guiGraphics.m_280509_(n5 - 1, n4, n5, n4 + n9, n8);
        guiGraphics.m_280509_(n3, n6 - 1, n3 + n9, n6, n8);
        guiGraphics.m_280509_(n3, n6 - n9, n3 + 1, n6, n8);
        guiGraphics.m_280509_(n5 - n9, n6 - 1, n5, n6, n8);
        guiGraphics.m_280509_(n5 - 1, n6 - n9, n5, n6, n8);
    }

    private void drawHeader(GuiGraphics guiGraphics, float f, float f2) {
        int n = this.px + this.pw / 2;
        int n2 = this.py + 14;
        float f3 = (float)(Math.sin(f * 1.4f) * 0.5 + 0.5);
        String string = "LHS  //  MINI-GAMES";
        this.ctxt(guiGraphics, string, n, n2, 16756301, 235.0f * f2);
        String string2 = MainMenuScreen.text("\u0412\u042b\u0411\u0415\u0420\u0418 \u0420\u0415\u0416\u0418\u041c", "SELECT MODE");
        int n3 = this.f_96547_.m_92895_(string2);
        this.txt(guiGraphics, string2, n - n3 / 2, n2 + 15, 0xFFA22A, (75.0f + f3 * 105.0f) * f2);
        guiGraphics.m_280056_(this.f_96547_, string2, n - n3 / 2, n2 + 14, LobbyScreen.col(255.0f * f2, 0xFFF8F2), false);
        int n4 = n2 + 28;
        if (this.headerH >= 78) {
            this.ctxt(guiGraphics, MainMenuScreen.text("\u0427\u0415\u0422\u042b\u0420\u0415 \u041c\u0418\u0420\u0410. \u0427\u0415\u0422\u042b\u0420\u0415 \u0418\u0421\u041f\u042b\u0422\u0410\u041d\u0418\u042f.", "FOUR WORLDS. FOUR TRIALS."), n, n4, 13937013, 165.0f * f2);
            n4 += 14;
        }
        int n5 = Math.min(140, this.pw / 4);
        guiGraphics.m_280509_(n - n5, n4, n - 12, n4 + 1, LobbyScreen.col(80.0f * f2, 8936224));
        guiGraphics.m_280509_(n + 12, n4, n + n5, n4 + 1, LobbyScreen.col(80.0f * f2, 8936224));
        guiGraphics.m_280509_(n - 2, n4 - 1, n + 2, n4 + 2, LobbyScreen.col((180.0f + f3 * 75.0f) * f2, 16756301));
        Minecraft minecraft = Minecraft.m_91087_();
        Object object = minecraft != null && minecraft.f_91074_ != null && minecraft.f_91074_.f_108617_ != null ? MainMenuScreen.text("\u0418\u0413\u0420\u041e\u041a\u041e\u0412: ", "ONLINE: ") + minecraft.f_91074_.f_108617_.m_246170_().size() : MainMenuScreen.text("\u041b\u041e\u0411\u0411\u0418 \u0421\u0415\u0422\u042c", "LOBBY NET");
        this.txt(guiGraphics, (String)object, this.px + this.pw - 16 - this.f_96547_.m_92895_((String)object), n2, 9075309, 195.0f * f2);
    }

    private void drawFooter(GuiGraphics guiGraphics, float f) {
        String[][] stringArrayArray;
        int n = this.py + this.ph - 18;
        guiGraphics.m_280509_(this.px + 16, n - 7, this.px + this.pw - 16, n - 6, LobbyScreen.col(70.0f * f, 8936224));
        int n2 = this.px + 16;
        for (String[] stringArray : stringArrayArray = new String[][]{{"M / ESC", MainMenuScreen.text("\u0417\u0410\u041a\u0420\u042b\u0422\u042c", "CLOSE")}, {"1 - 4", MainMenuScreen.text("\u0412\u042b\u0411\u041e\u0420", "SELECT")}, {"TAB", MainMenuScreen.text("\u0418\u0413\u0420\u041e\u041a\u0418", "PLAYERS")}}) {
            this.txt(guiGraphics, stringArray[0], n2, n, 16751164, 230.0f * f);
            this.txt(guiGraphics, stringArray[1], n2 += this.f_96547_.m_92895_(stringArray[0]) + 5, n, 11575184, 190.0f * f);
            n2 += this.f_96547_.m_92895_(stringArray[1]) + 14;
        }
        String string = "LHS NETWORK  \u2022  SELECTOR";
        this.txt(guiGraphics, string, this.px + this.pw - 16 - this.f_96547_.m_92895_(string), n, 9075309, 170.0f * f);
    }

    private void drawCard(GuiGraphics guiGraphics, int n, float f, float f2) {
        if (!(f <= 0.01f)) {
            int n2;
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            float f3;
            Mode mode = this.modes[n];
            boolean bl = mode.locked;
            float f4 = bl ? Math.min(this.hover[n], 0.35f) : this.hover[n];
            int n8 = mode.accent;
            int n9 = this.cx[n];
            int n10 = this.cw;
            int n11 = this.ch;
            int n12 = this.cy[n] + (int)((1.0f - f) * 14.0f);
            float f5 = (float)(Math.sin(f2 * 3.2f) * 0.5 + 0.5);
            float f6 = f3 = bl ? 0.62f : 1.0f;
            if (f4 > 0.02f && !bl) {
                for (n7 = 1; n7 <= 2; ++n7) {
                    n6 = n7 * 2;
                    guiGraphics.m_280509_(n9 - n6, n12 - n6, n9 + n10 + n6, n12 + n11 + n6, LobbyScreen.col(f4 * (16.0f + f5 * 12.0f) / (float)n7 * f, n8));
                }
            }
            guiGraphics.m_280509_(n9, n12, n9 + n10, n12 + n11, LobbyScreen.col((150.0f + f4 * 80.0f) * f, LobbyScreen.mix(1182471, 2758924, f4)));
            n7 = Math.min(26, n11 / 3);
            for (n6 = 0; n6 < n7; ++n6) {
                float f7 = 1.0f - (float)n6 / (float)n7;
                guiGraphics.m_280509_(n9 + 1, n12 + 1 + n6, n9 + n10 - 1, n12 + 2 + n6, LobbyScreen.col((10.0f + f4 * 34.0f) * f7 * f7 * f * f3, n8));
            }
            if (f4 > 0.1f && !bl) {
                n6 = 38;
                int n13 = n10 + n6 * 2;
                n5 = n9 + (int)(f2 * 150.0f) % n13 - n6;
                n4 = Math.max(n9 + 1, n5);
                n3 = Math.min(n9 + n10 - 1, n5 + n6);
                if (n3 > n4) {
                    guiGraphics.m_280509_(n4, n12 + 1, n3, n12 + n11 - 1, LobbyScreen.col(f4 * 26.0f * f, 16760968));
                }
            }
            guiGraphics.m_280509_(n9, n12, n9 + 3, n12 + n11, LobbyScreen.col((bl ? 120.0f : 210.0f + f4 * 45.0f) * f, n8));
            this.box(guiGraphics, n9, n12, n9 + n10, n12 + n11, LobbyScreen.col((bl ? 80.0f + f4 * 60.0f : 110.0f + f4 * 145.0f) * f, LobbyScreen.mix(6701592, n8, f4)));
            String string = String.valueOf(n + 1);
            int n14 = 12;
            n5 = n9 + n10 - n14 - 5;
            n4 = n12 + 5;
            guiGraphics.m_280509_(n5, n4, n5 + n14, n4 + n14, LobbyScreen.col((bl ? 90.0f : 150.0f + f4 * 95.0f) * f, LobbyScreen.mix(788229, n8, f4)));
            this.box(guiGraphics, n5, n4, n5 + n14, n4 + n14, LobbyScreen.col((bl ? 85.0f : 120.0f + f4 * 130.0f) * f, LobbyScreen.mix(6701592, n8, f4)));
            this.ctxt(guiGraphics, string, n5 + n14 / 2, n4 + 3, LobbyScreen.mix(11575184, 328450, f4), (bl ? 140.0f : 235.0f) * f);
            if (f4 > 0.08f && !bl) {
                n3 = LobbyScreen.col(f4 * 245.0f * f, 16760968);
                guiGraphics.m_280509_(n9 + n10 - 6, n12, n9 + n10, n12 + 1, n3);
                guiGraphics.m_280509_(n9 + n10 - 1, n12, n9 + n10, n12 + 6, n3);
                guiGraphics.m_280509_(n9, n12 + n11 - 1, n9 + 6, n12 + n11, n3);
                guiGraphics.m_280509_(n9, n12 + n11 - 6, n9 + 1, n12 + n11, n3);
            }
            int n15 = (n3 = Math.min(44, n11 - 14)) >= 44 ? 3 : 2;
            int n16 = n9 + 14;
            int n17 = n12 + (n11 - n3) / 2;
            if (!bl) {
                guiGraphics.m_280509_(n16 - 2, n17 - 2, n16 + n3 + 2, n17 + n3 + 2, LobbyScreen.col(f4 * 40.0f * f, n8));
            }
            guiGraphics.m_280509_(n16, n17, n16 + n3, n17 + n3, LobbyScreen.col(235.0f * f, 788229));
            this.box(guiGraphics, n16, n17, n16 + n3, n17 + n3, LobbyScreen.col((bl ? 90.0f : 120.0f + f4 * 135.0f) * f, LobbyScreen.mix(6701592, n8, f4)));
            this.drawIcon(guiGraphics, mode, n16, n17, n3, n15, f4, f * (bl ? 0.55f : 1.0f), f2);
            int n18 = n16 + n3 + 10;
            int n19 = n9 + n10 - 12 - n18;
            int n20 = n17 + 1;
            guiGraphics.m_280056_(this.f_96547_, mode.title, n18, n20 - 1, LobbyScreen.col(250.0f * f * (bl ? 0.7f : 1.0f), LobbyScreen.mix(0xFFF8F2, n8, f4 * 0.22f)), false);
            boolean bl2 = n11 >= 76;
            int n21 = LobbyScreen.mix(11575184, 13937013, f4);
            if (bl && !bl2) {
                this.drawSoon(guiGraphics, n18, n20 + 13, n19, f, f5);
            } else {
                List<String> list = this.wrap(mode.desc, Math.max(40, n19 - 14), 2);
                for (n2 = 0; n2 < list.size(); ++n2) {
                    this.txt(guiGraphics, list.get(n2), n18, n20 + 13 + n2 * 10, n21, (bl ? 150.0f : 210.0f) * f);
                }
                if (bl2) {
                    if (bl) {
                        this.drawSoon(guiGraphics, n18, n12 + n11 - 17, n19, f, f5);
                    } else {
                        this.txt(guiGraphics, mode.cta, n18, n12 + n11 - 16, LobbyScreen.mix(16757606, 0xFFFFFF, f4), (190.0f + f4 * 60.0f) * f);
                    }
                }
            }
            if (!bl) {
                int n22 = LobbyScreen.col((90.0f + f4 * 165.0f) * f, LobbyScreen.mix(6701592, 16757606, f4));
                n2 = n9 + n10 - 16 + (int)(f4 * 3.0f);
                int n23 = n12 + n11 / 2;
                guiGraphics.m_280509_(n2, n23 - 4, n2 + 2, n23 + 5, n22);
                guiGraphics.m_280509_(n2 + 2, n23 - 3, n2 + 4, n23 + 4, n22);
                guiGraphics.m_280509_(n2 + 4, n23 - 2, n2 + 6, n23 + 3, n22);
                guiGraphics.m_280509_(n2 + 6, n23 - 1, n2 + 8, n23 + 2, n22);
            }
        }
    }

    private void drawSoon(GuiGraphics guiGraphics, int n, int n2, int n3, float f, float f2) {
        String string = MainMenuScreen.text("\u0412 \u0420\u0410\u0417\u0420\u0410\u0411\u041e\u0422\u041a\u0415", "IN DEVELOPMENT");
        if (this.f_96547_.m_92895_(string) + 16 > n3) {
            string = MainMenuScreen.text("\u0421\u041a\u041e\u0420\u041e", "SOON");
        }
        int n4 = this.f_96547_.m_92895_(string) + 16;
        guiGraphics.m_280509_(n, n2 - 2, n + n4, n2 + 11, LobbyScreen.col(60.0f * f, 3811860));
        this.box(guiGraphics, n, n2 - 2, n + n4, n2 + 11, LobbyScreen.col(150.0f * f, 9071152));
        guiGraphics.m_280509_(n + 4, n2 + 2, n + 7, n2 + 5, LobbyScreen.col((110.0f + f2 * 145.0f) * f, 16757606));
        this.txt(guiGraphics, string, n + 11, n2, 13937013, 225.0f * f);
    }

    private void drawIcon(GuiGraphics guiGraphics, Mode mode, int n, int n2, int n3, int n4, float f, float f2, float f3) {
        String[] stringArray = mode.icon;
        int n5 = stringArray[0].length() * n4;
        int n6 = stringArray.length * n4;
        int n7 = (int)Math.round(Math.sin(f3 * 2.4f) * 1.0 * (double)f);
        int n8 = n + (n3 - n5) / 2;
        int n9 = n2 + (n3 - n6) / 2 + n7;
        float f4 = (float)(Math.sin(f3 * 4.0f) * 0.5 + 0.5);
        float f5 = 0.78f + f * 0.22f;
        for (int i = 0; i < stringArray.length; ++i) {
            String string = stringArray[i];
            for (int j = 0; j < string.length(); ++j) {
                char c = string.charAt(j);
                if (c == '.') continue;
                int n10 = switch (c) {
                    case 'A' -> 16751164;
                    case 'B' -> {
                        if (mode.id.equals("crossbattle")) {
                            yield 13160664;
                        }
                        yield 16763018;
                    }
                    case 'C' -> 9078654;
                    case 'D' -> {
                        if (mode.zombie) {
                            yield 3033630;
                        }
                        if (mode.id.equals("bunker")) {
                            yield 2828582;
                        }
                        if (mode.id.equals("crossbattle")) {
                            yield 8014366;
                        }
                        yield 12607514;
                    }
                    case 'E' -> 0xFFFFFF;
                    default -> 0xFFFFFF;
                    case 'G' -> 7315018;
                    case 'H' -> {
                        if (mode.zombie) {
                            yield 9421413;
                        }
                        yield 16757606;
                    }
                    case 'K' -> 1182214;
                    case 'R' -> LobbyScreen.mix(11542549, 16730680, f4 * (0.4f + f * 0.6f));
                    case 'W' -> 13227704;
                    case 'Y' -> 4867905;
                };
                int n11 = n8 + j * n4;
                int n12 = n9 + i * n4;
                guiGraphics.m_280509_(n11, n12, n11 + n4, n12 + n4, LobbyScreen.col(255.0f * f2 * f5, n10));
            }
        }
    }

    public boolean m_6375_(double d, double d2, int n) {
        if (n == 0) {
            this.layout();
            int n2 = this.cardAt(d, d2);
            if (n2 >= 0) {
                this.select(n2);
                return true;
            }
        }
        return super.m_6375_(d, d2, n);
    }

    public boolean m_7933_(int n, int n2, int n3) {
        switch (n) {
            case 32: 
            case 257: 
            case 335: {
                if (this.focus >= 0) {
                    this.select(this.focus);
                    return true;
                }
                return true;
            }
            case 49: 
            case 321: {
                this.select(0);
                return true;
            }
            case 50: 
            case 322: {
                this.select(1);
                return true;
            }
            case 51: 
            case 323: {
                this.select(2);
                return true;
            }
            case 52: 
            case 324: {
                this.select(3);
                return true;
            }
            case 77: 
            case 256: {
                this.m_7379_();
                return true;
            }
            case 258: {
                this.focus = (this.focus + 1) % 4;
                return true;
            }
            case 262: {
                this.focus = this.focus < 0 ? 0 : (this.focus % 2 == 0 ? this.focus + 1 : this.focus);
                return true;
            }
            case 263: {
                this.focus = this.focus < 0 ? 0 : (this.focus % 2 == 1 ? this.focus - 1 : this.focus);
                return true;
            }
            case 264: {
                this.focus = this.focus < 0 ? 0 : (this.focus < 2 ? this.focus + 2 : this.focus);
                return true;
            }
            case 265: {
                this.focus = this.focus < 0 ? 0 : (this.focus >= 2 ? this.focus - 2 : this.focus);
                return true;
            }
        }
        return super.m_7933_(n, n2, n3);
    }

    private void select(int n) {
        if (!this.modes[n].locked) {
            ModSounds.playClickSound();
            LobbyTabList.setLobbyActive(false);
            Minecraft minecraft = Minecraft.m_91087_();
            if (minecraft.f_91074_ != null) {
                minecraft.f_91074_.f_108617_.m_246623_("lhslobby select " + this.modes[n].id);
            }
            this.m_7379_();
        }
    }

    public void m_7379_() {
        Minecraft.m_91087_().m_91152_(null);
    }

    public boolean m_7043_() {
        return false;
    }

    private static float fract(double d) {
        return (float)(d - Math.floor(d));
    }

    private static final class Mode {
        final String id;
        final String title;
        final String desc;
        final String cta;
        final int accent;
        final String[] icon;
        final boolean zombie;
        final boolean locked;

        Mode(String string, String string2, String string3, String string4, int n, String[] stringArray, boolean bl, boolean bl2) {
            this.locked = bl2;
            this.id = string;
            this.title = string2;
            this.desc = string3;
            this.cta = string4;
            this.accent = n;
            this.icon = stringArray;
            this.zombie = bl;
        }
    }
}
