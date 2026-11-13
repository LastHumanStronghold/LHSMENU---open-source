/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.Style
 *  net.minecraft.util.FormattedCharSequence
 */
package ru.lhs.auth.client;

import com.mainlhs.client.screen.CustomButton;
import com.mainlhs.client.screen.MenuBackgroundRenderer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import ru.lhs.auth.client.AuthApi;
import ru.lhs.auth.client.AuthStore;
import ru.lhs.auth.client.LHSAuthClient;

public class AuthScreen
extends Screen {
    private static final int ACCENT = 0xFF8818;
    private static final int GOLD = 16756768;
    private static final int TEXT = 0xF4F4F7;
    private static final int MUTED = 0x858599;
    private static final int ERROR = 0xFF5A5A;
    private static final int OK = 5635978;
    private static final int PANEL = 789522;
    private static final int PW = 280;
    private static final int PAD = 22;
    private static long lastAutoFailMs = 0L;
    private EditBox username;
    private EditBox password;
    private EditBox confirm;
    private CustomButton action;
    private CustomButton switchMode;
    private CustomButton passwordToggle;
    private CustomButton confirmToggle;
    private CustomButton rememberBtn;
    private CustomButton cancelAuto;
    private boolean registerMode;
    private boolean busy;
    private boolean statusError;
    private boolean passwordVisible;
    private boolean confirmVisible;
    private boolean remember = true;
    private String status = "";
    private final long openNs = System.nanoTime();
    private long shakeNs = -1L;
    private long successNs = -1L;
    private boolean closed;
    private boolean auto;
    private boolean autoStarted;
    private boolean ignoreResult;
    private AuthStore.Creds saved;
    private String uText = "";
    private String pText = "";
    private String cText = "";
    private int px;
    private int py;
    private int ph;
    private final List<AbstractWidget> ws = new ArrayList<AbstractWidget>();
    private final List<int[]> base = new ArrayList<int[]>();

    public AuthScreen() {
        super((Component)Component.m_237113_((String)"LHS Authentication"));
        this.saved = AuthStore.load();
        if (this.saved != null) {
            boolean bl = System.currentTimeMillis() - lastAutoFailMs < 20000L;
            this.auto = !bl;
            this.uText = this.saved.user;
            if (bl) {
                this.pText = this.saved.pass;
            }
        }
    }

    private static void mask(EditBox editBox) {
        editBox.m_94149_((string, n) -> FormattedCharSequence.m_13714_((String)"\u25cf".repeat(string.length()), (Style)Style.f_131099_));
    }

    private static void reveal(EditBox editBox) {
        editBox.m_94149_((string, n) -> FormattedCharSequence.m_13714_((String)string, (Style)Style.f_131099_));
    }

    private static float clamp01(float f) {
        return f < 0.0f ? 0.0f : Math.min(f, 1.0f);
    }

    private static float ease(float f) {
        float f2 = 1.0f - AuthScreen.clamp01(f);
        return 1.0f - f2 * f2 * f2;
    }

    private static int lerp(int n, int n2, float f) {
        int n3 = n >> 16 & 0xFF;
        int n4 = n >> 8 & 0xFF;
        int n5 = n & 0xFF;
        int n6 = n2 >> 16 & 0xFF;
        int n7 = n2 >> 8 & 0xFF;
        int n8 = n2 & 0xFF;
        return (int)((float)n3 + (float)(n6 - n3) * f) << 16 | (int)((float)n4 + (float)(n7 - n4) * f) << 8 | (int)((float)n5 + (float)(n8 - n5) * f);
    }

    private static int argb(float f, int n) {
        int n2 = (int)(AuthScreen.clamp01(f) * 255.0f);
        return n2 << 24 | n & 0xFFFFFF;
    }

    private void rect(GuiGraphics guiGraphics, int n, int n2, int n3, int n4, int n5) {
        if (n3 <= 0 || n4 <= 0) {
            return;
        }
        guiGraphics.m_280509_(n, n2, n + n3, n2 + n4, n5);
    }

    private void text(GuiGraphics guiGraphics, String string, int n, int n2, int n3, float f) {
        if (f < 0.05f) {
            return;
        }
        guiGraphics.m_280056_(this.f_96547_, string, n, n2, AuthScreen.argb(f, n3), false);
    }

    private void textC(GuiGraphics guiGraphics, String string, int n, int n2, int n3, float f) {
        this.text(guiGraphics, string, n - this.f_96547_.m_92895_(string) / 2, n2, n3, f);
    }

    private <T extends AbstractWidget> T add(T t) {
        this.m_142416_((GuiEventListener)t);
        this.ws.add(t);
        this.base.add(new int[]{t.m_252754_(), t.m_252907_()});
        return t;
    }

    private void captureText() {
        if (this.username != null) {
            this.uText = this.username.m_94155_();
        }
        if (this.password != null) {
            this.pText = this.password.m_94155_();
        }
        if (this.confirm != null) {
            this.cText = this.confirm.m_94155_();
        }
    }

    private void setControls(boolean bl) {
        for (AbstractWidget abstractWidget : this.ws) {
            abstractWidget.f_93623_ = bl;
        }
    }

    protected void m_7856_() {
        this.captureText();
        this.ws.clear();
        this.base.clear();
        this.username = null;
        this.password = null;
        this.confirm = null;
        this.action = null;
        this.switchMode = null;
        this.passwordToggle = null;
        this.confirmToggle = null;
        this.rememberBtn = null;
        this.cancelAuto = null;
        int n = 236;
        this.ph = this.auto ? 214 : (this.registerMode ? 322 : 282);
        this.px = (this.f_96543_ - 280) / 2;
        this.py = Math.max(8, (this.f_96544_ - this.ph) / 2);
        if (this.auto) {
            this.cancelAuto = this.add(new CustomButton(this.px + 22, this.py + this.ph - 46, n, 22, (Component)Component.m_237113_((String)"\u0414\u0420\u0423\u0413\u041e\u0419 \u0410\u041a\u041a\u0410\u0423\u041d\u0422"), button -> this.cancelAutoLogin(), true));
            return;
        }
        int n2 = this.py + 78;
        this.username = this.add(new EditBox(this.f_96547_, this.px + 22, n2 + 11, n, 20, (Component)Component.m_237113_((String)"\u041b\u043e\u0433\u0438\u043d")));
        this.username.m_94199_(32);
        this.username.m_257771_((Component)Component.m_237113_((String)"\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043b\u043e\u0433\u0438\u043d"));
        this.username.m_94144_(this.uText);
        this.password = this.add(new EditBox(this.f_96547_, this.px + 22, n2 + 51, n - 24, 20, (Component)Component.m_237113_((String)"\u041f\u0430\u0440\u043e\u043b\u044c")));
        this.password.m_94199_(64);
        this.password.m_257771_((Component)Component.m_237113_((String)"\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043f\u0430\u0440\u043e\u043b\u044c"));
        if (this.passwordVisible) {
            AuthScreen.reveal(this.password);
        } else {
            AuthScreen.mask(this.password);
        }
        this.password.m_94144_(this.pText);
        this.passwordToggle = this.add(new CustomButton(this.px + 22 + n - 20, n2 + 51, 20, 20, (Component)Component.m_237113_((String)(this.passwordVisible ? "\u25cb" : "\u25cf")), button -> this.togglePassword(false), false));
        int n3 = n2 + 84;
        if (this.registerMode) {
            this.confirm = this.add(new EditBox(this.f_96547_, this.px + 22, n2 + 91, n - 24, 20, (Component)Component.m_237113_((String)"\u041f\u043e\u0432\u0442\u043e\u0440 \u043f\u0430\u0440\u043e\u043b\u044f")));
            this.confirm.m_94199_(64);
            this.confirm.m_257771_((Component)Component.m_237113_((String)"\u041f\u043e\u0432\u0442\u043e\u0440\u0438\u0442\u0435 \u043f\u0430\u0440\u043e\u043b\u044c"));
            if (this.confirmVisible) {
                AuthScreen.reveal(this.confirm);
            } else {
                AuthScreen.mask(this.confirm);
            }
            this.confirm.m_94144_(this.cText);
            this.confirmToggle = this.add(new CustomButton(this.px + 22 + n - 20, n2 + 91, 20, 20, (Component)Component.m_237113_((String)(this.confirmVisible ? "\u25cb" : "\u25cf")), button -> this.togglePassword(true), false));
            n3 = n2 + 124;
        }
        this.rememberBtn = this.add(new CustomButton(this.px + 22, n3, n, 18, this.rememberLabel(), button -> this.toggleRemember(), false));
        this.action = this.add(new CustomButton(this.px + 22, n3 + 26, n, 26, (Component)Component.m_237113_((String)(this.registerMode ? "\u0421\u041e\u0417\u0414\u0410\u0422\u042c \u0410\u041a\u041a\u0410\u0423\u041d\u0422" : "\u0412\u041e\u0419\u0422\u0418")), button -> this.submit(), false).setPrimary(true));
        this.switchMode = this.add(new CustomButton(this.px + 22, n3 + 60, n, 20, (Component)Component.m_237113_((String)(this.registerMode ? "\u0423\u0436\u0435 \u0435\u0441\u0442\u044c \u0430\u043a\u043a\u0430\u0443\u043d\u0442?  \u0412\u043e\u0439\u0442\u0438" : "\u0421\u043e\u0437\u0434\u0430\u0442\u044c \u043d\u043e\u0432\u044b\u0439 \u0430\u043a\u043a\u0430\u0443\u043d\u0442")), button -> this.toggleMode(), false));
        this.m_264313_((GuiEventListener)this.username);
        this.setControls(!this.busy);
    }

    private Component rememberLabel() {
        return Component.m_237113_((String)(this.remember ? "\u2714  \u0417\u0430\u043f\u043e\u043c\u043d\u0438\u0442\u044c \u043c\u0435\u043d\u044f (\u0430\u0432\u0442\u043e\u0432\u0445\u043e\u0434)" : "\u2716  \u041d\u0435 \u0437\u0430\u043f\u043e\u043c\u0438\u043d\u0430\u0442\u044c"));
    }

    private void toggleRemember() {
        boolean bl = this.remember = !this.remember;
        if (this.rememberBtn != null) {
            this.rememberBtn.m_93666_(this.rememberLabel());
        }
    }

    private void togglePassword(boolean bl) {
        if (bl) {
            boolean bl2 = this.confirmVisible = !this.confirmVisible;
            if (this.confirmVisible) {
                AuthScreen.reveal(this.confirm);
            } else {
                AuthScreen.mask(this.confirm);
            }
            this.confirmToggle.m_93666_((Component)Component.m_237113_((String)(this.confirmVisible ? "\u25cb" : "\u25cf")));
        } else {
            boolean bl3 = this.passwordVisible = !this.passwordVisible;
            if (this.passwordVisible) {
                AuthScreen.reveal(this.password);
            } else {
                AuthScreen.mask(this.password);
            }
            this.passwordToggle.m_93666_((Component)Component.m_237113_((String)(this.passwordVisible ? "\u25cb" : "\u25cf")));
        }
    }

    private void toggleMode() {
        this.captureText();
        this.registerMode = !this.registerMode;
        this.status = "";
        this.statusError = false;
        this.cText = "";
        this.m_232761_();
    }

    private void fail(String string) {
        this.status = string;
        this.statusError = true;
        this.shakeNs = System.nanoTime();
    }

    private void submit() {
        if (this.busy || this.auto || this.successNs >= 0L) {
            return;
        }
        String string = this.username.m_94155_().trim();
        String string2 = this.password.m_94155_();
        if (!string.matches("[A-Za-z0-9_]{3,32}")) {
            this.fail("\u041b\u043e\u0433\u0438\u043d: 3\u201332 \u0441\u0438\u043c\u0432\u043e\u043b\u0430, \u0442\u043e\u043b\u044c\u043a\u043e A-Z, 0-9 \u0438 _");
            return;
        }
        if (string2.length() < 8 || string2.length() > 64 || string2.contains(" ")) {
            this.fail("\u041f\u0430\u0440\u043e\u043b\u044c \u0434\u043e\u043b\u0436\u0435\u043d \u0431\u044b\u0442\u044c \u043e\u0442 8 \u0434\u043e 64 \u0441\u0438\u043c\u0432\u043e\u043b\u043e\u0432 \u0431\u0435\u0437 \u043f\u0440\u043e\u0431\u0435\u043b\u043e\u0432");
            return;
        }
        if (this.registerMode && !string2.equals(this.confirm.m_94155_())) {
            this.fail("\u041f\u0430\u0440\u043e\u043b\u0438 \u043d\u0435 \u0441\u043e\u0432\u043f\u0430\u0434\u0430\u044e\u0442");
            return;
        }
        this.startRequest(this.registerMode ? "register" : "login", string, string2, false);
    }

    private void startRequest(String string, String string2, String string4, boolean bl) {
        this.busy = true;
        this.setControls(false);
        this.statusError = false;
        this.status = bl ? "\u041f\u0440\u043e\u0432\u0435\u0440\u044f\u0435\u043c \u0441\u043e\u0445\u0440\u0430\u043d\u0451\u043d\u043d\u044b\u0439 \u0432\u0445\u043e\u0434\u2026" : (this.registerMode ? "\u0421\u043e\u0437\u0434\u0430\u0451\u043c \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u2026" : "\u041f\u0440\u043e\u0432\u0435\u0440\u044f\u0435\u043c \u0434\u0430\u043d\u043d\u044b\u0435\u2026");
        AuthApi.authenticate(string, string2, string4, (bl2, string3, jsonObject) -> Minecraft.m_91087_().execute(() -> this.onResult(bl2, string3, bl, string2, string4)));
    }

    private static boolean isTransient(String string) {
        return string == null || string.startsWith("\u041d\u0435 \u0443\u0434\u0430\u043b\u043e\u0441\u044c \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0438\u0442\u044c\u0441\u044f") || string.startsWith("\u041d\u0435\u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u044b\u0439 \u043e\u0442\u0432\u0435\u0442");
    }

    private void onResult(boolean bl, String string, boolean bl2, String string2, String string3) {
        if (this.ignoreResult) {
            if (bl) {
                AuthApi.clearSession();
            }
            return;
        }
        this.busy = false;
        if (bl) {
            if (this.remember) {
                AuthStore.save(string2, string3);
            } else {
                AuthStore.clear();
            }
            this.status = bl2 ? "\u0421 \u0432\u043e\u0437\u0432\u0440\u0430\u0449\u0435\u043d\u0438\u0435\u043c, " + string2 : (this.registerMode ? "\u0410\u043a\u043a\u0430\u0443\u043d\u0442 \u0441\u043e\u0437\u0434\u0430\u043d" : "\u0412\u0445\u043e\u0434 \u0432\u044b\u043f\u043e\u043b\u043d\u0435\u043d");
            this.statusError = false;
            this.successNs = System.nanoTime();
            LHSAuthClient.setAuthenticated(true);
            return;
        }
        if (bl2) {
            boolean bl3 = AuthScreen.isTransient(string);
            if (!bl3) {
                AuthStore.clear();
            }
            lastAutoFailMs = System.currentTimeMillis();
            this.auto = false;
            this.uText = string2;
            String string4 = this.pText = bl3 ? string3 : "";
            this.fail(bl3 ? (string == null ? "\u041d\u0435\u0442 \u0441\u0432\u044f\u0437\u0438 \u0441 \u0441\u0435\u0440\u0432\u0435\u0440\u043e\u043c" : string) : "\u0421\u043e\u0445\u0440\u0430\u043d\u0451\u043d\u043d\u044b\u0439 \u0432\u0445\u043e\u0434 \u0431\u043e\u043b\u044c\u0448\u0435 \u043d\u0435 \u0434\u0435\u0439\u0441\u0442\u0432\u0443\u0435\u0442 \u2014 \u0432\u043e\u0439\u0434\u0438\u0442\u0435 \u0437\u0430\u043d\u043e\u0432\u043e");
            this.m_232761_();
            return;
        }
        this.setControls(true);
        this.fail(string == null || string.isEmpty() ? "\u041e\u0448\u0438\u0431\u043a\u0430 \u0430\u0432\u0442\u043e\u0440\u0438\u0437\u0430\u0446\u0438\u0438" : string);
    }

    private void cancelAutoLogin() {
        if (!this.auto) {
            return;
        }
        if (this.busy) {
            this.ignoreResult = true;
            this.busy = false;
        }
        AuthStore.clear();
        lastAutoFailMs = System.currentTimeMillis();
        this.auto = false;
        this.status = "";
        this.statusError = false;
        this.pText = "";
        this.m_232761_();
        this.ignoreResult = false;
    }

    public boolean m_6913_() {
        return false;
    }

    public boolean m_7933_(int n, int n2, int n3) {
        if (this.successNs >= 0L) {
            return true;
        }
        if (this.auto && n == 256) {
            this.cancelAutoLogin();
            return true;
        }
        if (!(n != 257 && n != 335 || this.auto || this.busy)) {
            this.submit();
            return true;
        }
        return super.m_7933_(n, n2, n3);
    }

    public void m_88315_(GuiGraphics guiGraphics, int n, int n2, float f) {
        float f2;
        long l = System.nanoTime();
        float f3 = (float)(l - this.openNs) / 1.0E9f;
        float f4 = AuthScreen.ease(f3 / 0.7f);
        float f5 = 1.0f;
        float f6 = 0.0f;
        if (this.successNs >= 0L) {
            f6 = (float)(l - this.successNs) / 1.0E9f;
            f5 = 1.0f - AuthScreen.clamp01((f6 - 0.55f) / 0.45f);
        }
        float f7 = f4 * f5;
        if (this.auto && !this.autoStarted && !this.busy && f3 > 0.35f && this.saved != null) {
            this.autoStarted = true;
            this.startRequest("login", this.saved.user, this.saved.pass, true);
        }
        MenuBackgroundRenderer.render(guiGraphics, this.f_96543_, this.f_96544_, MenuBackgroundRenderer.getTime(), Math.min(1.0f, f3 / 0.8f), n, n2);
        this.rect(guiGraphics, 0, 0, this.f_96543_, this.f_96544_, AuthScreen.argb(0.38f * Math.min(1.0f, f3 / 0.5f), 0));
        int n3 = 0;
        if (this.shakeNs >= 0L && (f2 = (float)(l - this.shakeNs) / 1.0E9f) < 0.5f) {
            n3 = Math.round((float)Math.sin(f2 * 48.0f) * 7.0f * (1.0f - f2 / 0.5f));
        }
        int n4 = Math.round((1.0f - f4) * 18.0f);
        int n5 = this.px + n3;
        int n6 = this.py + n4;
        boolean bl = f4 >= 0.8f && this.successNs < 0L;
        for (int i = 0; i < this.ws.size(); ++i) {
            AbstractWidget abstractWidget = this.ws.get(i);
            int[] nArray = this.base.get(i);
            abstractWidget.m_252865_(nArray[0] + n3);
            abstractWidget.m_253211_(nArray[1] + n4);
            boolean bl2 = abstractWidget instanceof EditBox;
            boolean bl3 = this.successNs < 0L && (bl2 ? bl : f4 > 0.25f) ? true : (abstractWidget.f_93624_ = false);
            if (!(abstractWidget instanceof CustomButton)) continue;
            CustomButton customButton = (CustomButton)abstractWidget;
            customButton.setAnimationAlpha(f7);
        }
        this.drawPanel(guiGraphics, n5, n6, f7, f3);
        this.drawHeader(guiGraphics, n5, n6, f7, f3);
        if (this.successNs >= 0L) {
            this.drawSuccess(guiGraphics, n5, n6, f7, f6);
        } else if (this.auto) {
            this.drawAuto(guiGraphics, n5, n6, f7, f3);
        } else {
            this.drawForm(guiGraphics, n5, n6, f7, f3);
        }
        super.m_88315_(guiGraphics, n, n2, f);
        this.textC(guiGraphics, "LAST HUMAN STRONGHOLD  \u00b7  SECURE LINK", this.f_96543_ / 2, this.f_96544_ - 14, 0x858599, 0.55f * AuthScreen.clamp01(f3 / 1.2f) * f5);
        if (this.successNs >= 0L && f6 >= 1.0f && !this.closed) {
            this.closed = true;
            Minecraft.m_91087_().m_91152_(null);
        }
    }

    private void drawPanel(GuiGraphics guiGraphics, int n, int n2, float f, float f2) {
        int n3;
        float f3;
        int n4;
        float f4 = 0.55f + 0.45f * (float)Math.sin(f2 * 2.2f);
        int n5 = this.successNs >= 0L ? 5635978 : 0xFF8818;
        for (int i = 1; i <= 6; ++i) {
            float f5 = f * f4 * 0.16f * (float)(7 - i) / 6.0f;
            n4 = AuthScreen.argb(f5, n5);
            this.rect(guiGraphics, n - i, n2 - i, 280 + i * 2, 1, n4);
            this.rect(guiGraphics, n - i, n2 + this.ph + i - 1, 280 + i * 2, 1, n4);
            this.rect(guiGraphics, n - i, n2 - i + 1, 1, this.ph + i * 2 - 2, n4);
            this.rect(guiGraphics, n + 280 + i - 1, n2 - i + 1, 1, this.ph + i * 2 - 2, n4);
        }
        this.rect(guiGraphics, n, n2, 280, this.ph, AuthScreen.argb(0.92f * f, 789522));
        this.rect(guiGraphics, n, n2, 280, 1, AuthScreen.argb(0.3f * f, n5));
        this.rect(guiGraphics, n, n2 + this.ph - 1, 280, 1, AuthScreen.argb(0.3f * f, n5));
        this.rect(guiGraphics, n, n2, 1, this.ph, AuthScreen.argb(0.3f * f, n5));
        this.rect(guiGraphics, n + 280 - 1, n2, 1, this.ph, AuthScreen.argb(0.3f * f, n5));
        float f6 = f2 * 0.55f % 1.5f - 0.25f;
        int n6 = (int)(f6 * 280.0f);
        for (n4 = -45; n4 <= 45; n4 += 3) {
            int n7 = n6 + n4;
            if (n7 < 0 || n7 >= 278) continue;
            f3 = 1.0f - (float)Math.abs(n4) / 46.0f;
            this.rect(guiGraphics, n + n7, n2, 3, 2, AuthScreen.argb(f * f3 * 0.95f, 16756768));
            n3 = 277 - n7;
            if (n3 < 0 || n3 >= 278) continue;
            this.rect(guiGraphics, n + n3, n2 + this.ph - 2, 3, 2, AuthScreen.argb(f * f3 * 0.55f, 0xFF8818));
        }
        n4 = AuthScreen.argb(f, n5);
        this.rect(guiGraphics, n - 2, n2 - 2, 9, 2, n4);
        this.rect(guiGraphics, n - 2, n2 - 2, 2, 9, n4);
        this.rect(guiGraphics, n + 280 - 7, n2 - 2, 9, 2, n4);
        this.rect(guiGraphics, n + 280, n2 - 2, 2, 9, n4);
        this.rect(guiGraphics, n - 2, n2 + this.ph, 9, 2, n4);
        this.rect(guiGraphics, n - 2, n2 + this.ph - 7, 2, 9, n4);
        this.rect(guiGraphics, n + 280 - 7, n2 + this.ph, 9, 2, n4);
        this.rect(guiGraphics, n + 280, n2 + this.ph - 7, 2, 9, n4);
        if (this.busy) {
            float f7 = f2 * 0.9f % 1.0f;
            f3 = f7 < 0.5f ? 2.0f * f7 * f7 : 1.0f - 2.0f * (1.0f - f7) * (1.0f - f7);
            n3 = 70;
            int n8 = n + 1 + (int)(f3 * (float)(278 - n3));
            this.rect(guiGraphics, n + 1, n2 + this.ph - 3, 278, 2, AuthScreen.argb(0.12f * f, 0xFF8818));
            this.rect(guiGraphics, n8, n2 + this.ph - 3, n3, 2, AuthScreen.argb(f, 16756768));
        }
    }

    private void drawHeader(GuiGraphics guiGraphics, int n, int n2, float f, float f2) {
        int n3;
        int n4 = n + 140;
        float f3 = 0.6f + 0.4f * (float)Math.sin(f2 * 3.0f);
        this.textC(guiGraphics, "\u2726", n4, n2 + 16, this.successNs >= 0L ? 5635978 : 16756768, f * f3);
        String string = "LAST HUMAN STRONGHOLD";
        int n5 = 0;
        for (n3 = 0; n3 < string.length(); ++n3) {
            n5 += this.f_96547_.m_92895_(String.valueOf(string.charAt(n3))) + 1;
        }
        n3 = n4 - n5 / 2;
        for (int i = 0; i < string.length(); ++i) {
            String string2 = String.valueOf(string.charAt(i));
            float f4 = ((float)Math.sin(f2 * 2.6f - (float)i * 0.38f) + 1.0f) / 2.0f;
            int n6 = AuthScreen.lerp(0xF4F4F7, this.successNs >= 0L ? 5635978 : 0xFF8818, f4 * f4 * 0.9f);
            this.text(guiGraphics, string2, n3, n2 + 32, n6, f);
            n3 += this.f_96547_.m_92895_(string2) + 1;
        }
        float f5 = AuthScreen.ease((f2 - 0.2f) / 0.8f);
        int n7 = (int)(150.0f * f5);
        this.rect(guiGraphics, n4 - n7 / 2, n2 + 47, n7, 1, AuthScreen.argb(0.75f * f, this.successNs >= 0L ? 5635978 : 0xFF8818));
        String string3 = this.successNs >= 0L ? "\u0414\u041e\u0421\u0422\u0423\u041f \u0420\u0410\u0417\u0420\u0415\u0428\u0401\u041d" : (this.auto ? "\u0421 \u0412\u041e\u0417\u0412\u0420\u0410\u0429\u0415\u041d\u0418\u0415\u041c" : (this.registerMode ? "\u041d\u041e\u0412\u042b\u0419 \u0410\u041a\u041a\u0410\u0423\u041d\u0422" : "\u0414\u041e\u0411\u0420\u041e \u041f\u041e\u0416\u0410\u041b\u041e\u0412\u0410\u0422\u042c"));
        this.textC(guiGraphics, string3, n4, n2 + 54, 0x858599, f * AuthScreen.clamp01((f2 - 0.4f) / 0.5f));
    }

    private void drawForm(GuiGraphics guiGraphics, int n, int n2, float f, float f2) {
        int n3;
        int n4 = n + 22;
        int n5 = n2 + 78;
        this.text(guiGraphics, "\u041b\u041e\u0413\u0418\u041d", n4, n5, this.usernameFocused() ? 0xFF8818 : 0x858599, f);
        this.text(guiGraphics, "\u041f\u0410\u0420\u041e\u041b\u042c", n4, n5 + 40, this.passwordFocused() ? 0xFF8818 : 0x858599, f);
        if (this.registerMode) {
            this.text(guiGraphics, "\u041f\u041e\u0412\u0422\u041e\u0420 \u041f\u0410\u0420\u041e\u041b\u042f", n4, n5 + 80, this.confirmFocused() ? 0xFF8818 : 0x858599, f);
        }
        this.focusGlow(guiGraphics, this.username, f, f2);
        this.focusGlow(guiGraphics, this.password, f, f2);
        if (this.registerMode) {
            this.focusGlow(guiGraphics, this.confirm, f, f2);
        }
        int n6 = n3 = this.switchMode == null ? n2 + this.ph - 26 : this.switchMode.m_252907_() + 28;
        if (!this.status.isEmpty()) {
            int n7 = this.statusError ? 0xFF5A5A : 0xF4F4F7;
            int n8 = this.f_96547_.m_92895_(this.status);
            int n9 = n + 140 - n8 / 2;
            if (this.busy) {
                this.spinner(guiGraphics, n9 - 12, n3 + 4, f, f2);
                n9 += 6;
            }
            if (n8 > 256) {
                String[] stringArray = this.splitStatus(this.status);
                this.textC(guiGraphics, stringArray[0], n + 140, n3 - 5, n7, f);
                this.textC(guiGraphics, stringArray[1], n + 140, n3 + 6, n7, f);
            } else {
                this.text(guiGraphics, this.status, n9, n3, n7, f);
            }
        }
    }

    private String[] splitStatus(String string) {
        int n = string.length() / 2;
        int n2 = string.lastIndexOf(32, n);
        if (n2 < 0) {
            n2 = string.indexOf(32, n);
        }
        if (n2 < 0) {
            return new String[]{string, ""};
        }
        return new String[]{string.substring(0, n2), string.substring(n2 + 1)};
    }

    private boolean usernameFocused() {
        return this.username != null && this.username.m_93696_();
    }

    private boolean passwordFocused() {
        return this.password != null && this.password.m_93696_();
    }

    private boolean confirmFocused() {
        return this.confirm != null && this.confirm.m_93696_();
    }

    private void focusGlow(GuiGraphics guiGraphics, EditBox editBox, float f, float f2) {
        if (editBox == null || !editBox.f_93624_ || !editBox.m_93696_()) {
            return;
        }
        float f3 = 0.65f + 0.35f * (float)Math.sin(f2 * 5.0f);
        int n = editBox.m_252754_();
        int n2 = editBox.m_252907_();
        int n3 = editBox.m_5711_();
        int n4 = editBox.m_93694_();
        for (int i = 2; i <= 4; ++i) {
            int n5 = AuthScreen.argb(f * f3 * 0.3f * (float)(5 - i) / 3.0f, 0xFF8818);
            this.rect(guiGraphics, n - i, n2 - i, n3 + i * 2, 1, n5);
            this.rect(guiGraphics, n - i, n2 + n4 + i - 1, n3 + i * 2, 1, n5);
            this.rect(guiGraphics, n - i, n2 - i + 1, 1, n4 + i * 2 - 2, n5);
            this.rect(guiGraphics, n + n3 + i - 1, n2 - i + 1, 1, n4 + i * 2 - 2, n5);
        }
    }

    private void spinner(GuiGraphics guiGraphics, int n, int n2, float f, float f2) {
        int n3 = 10;
        for (int i = 0; i < n3; ++i) {
            double d = (double)i * Math.PI * 2.0 / (double)n3 - 1.5707963267948966;
            int n4 = (int)Math.round(Math.cos(d) * 6.0);
            int n5 = (int)Math.round(Math.sin(d) * 6.0);
            float f3 = (f2 * 1.4f - (float)i / (float)n3) % 1.0f;
            if (f3 < 0.0f) {
                f3 += 1.0f;
            }
            float f4 = 1.0f - f3;
            this.rect(guiGraphics, n + n4 - 1, n2 + n5 - 1, 2, 2, AuthScreen.argb(f * (0.15f + 0.85f * f4), 16756768));
        }
    }

    private void drawAuto(GuiGraphics guiGraphics, int n, int n2, float f, float f2) {
        int n3 = n + 140;
        String string = this.saved != null ? this.saved.user : "";
        this.textC(guiGraphics, "\u0410\u0412\u0422\u041e\u0412\u0425\u041e\u0414", n3, n2 + 84, 0x858599, f);
        this.textC(guiGraphics, string, n3, n2 + 98, 0xFF8818, f);
        this.spinner(guiGraphics, n3, n2 + 132, f, f2);
        int n4 = 1 + (int)(f2 * 2.5f % 3.0f);
        this.textC(guiGraphics, "\u041f\u0440\u043e\u0432\u0435\u0440\u044f\u0435\u043c \u0441\u043e\u0445\u0440\u0430\u043d\u0451\u043d\u043d\u044b\u0439 \u0432\u0445\u043e\u0434" + ".".repeat(n4), n3, n2 + 152, 0xF4F4F7, f * 0.9f);
        int n5 = 236;
        int n6 = n + 22;
        this.rect(guiGraphics, n6, n2 + 170, n5, 2, AuthScreen.argb(0.18f * f, 0xFF8818));
        float f3 = f2 * 0.8f % 1.0f;
        int n7 = (int)((float)n5 * AuthScreen.ease(f3));
        this.rect(guiGraphics, n6, n2 + 170, n7, 2, AuthScreen.argb(f, 16756768));
        this.textC(guiGraphics, "ESC \u2014 \u043e\u0442\u043c\u0435\u043d\u0430", n3, n2 + this.ph - 16, 0x858599, f * 0.6f);
        if (!this.status.isEmpty() && this.statusError) {
            this.textC(guiGraphics, this.status, n3, n2 + 182, 0xFF5A5A, f);
        }
    }

    private void drawSuccess(GuiGraphics guiGraphics, int n, int n2, float f, float f2) {
        int n3 = n + 140;
        int n4 = n2 + this.ph / 2 + 22;
        for (int i = 0; i < 3; ++i) {
            float f3 = AuthScreen.clamp01(f2 * 1.4f - (float)i * 0.18f);
            if (f3 <= 0.0f || f3 >= 1.0f) continue;
            int n5 = (int)(10.0f + AuthScreen.ease(f3) * 46.0f);
            int n6 = AuthScreen.argb(f * (1.0f - f3) * 0.8f, 5635978);
            this.rect(guiGraphics, n3 - n5, n4 - n5, n5 * 2, 1, n6);
            this.rect(guiGraphics, n3 - n5, n4 + n5, n5 * 2, 1, n6);
            this.rect(guiGraphics, n3 - n5, n4 - n5, 1, n5 * 2, n6);
            this.rect(guiGraphics, n3 + n5, n4 - n5, 1, n5 * 2, n6);
        }
        this.textC(guiGraphics, "\u2714", n3, n4 - 4, 5635978, f);
        this.textC(guiGraphics, this.status, n3, n4 + 26, 0xF4F4F7, f);
    }
}
