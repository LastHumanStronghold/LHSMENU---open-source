/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.Util
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.LoadingOverlay
 *  net.minecraft.server.packs.resources.ReloadInstance
 *  net.minecraft.util.Mth
 */
package com.mainlhs.client.loading;

import com.mainlhs.client.loading.LoadingVisuals;
import com.mainlhs.client.screen.MainMenuScreen;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Mth;

public class BrandedLoadingOverlay
extends LoadingOverlay {
    private final Minecraft minecraft;
    private final ReloadInstance reload;
    private final Consumer<Optional<Throwable>> onFinish;
    private float currentProgress;
    private long fadeOutStart = -1L;
    private boolean completed;

    public BrandedLoadingOverlay(Minecraft minecraft, ReloadInstance reload, Consumer<Optional<Throwable>> onFinish, boolean fadeIn) {
        super(minecraft, reload, onFinish, fadeIn);
        this.minecraft = minecraft;
        this.reload = reload;
        this.onFinish = onFinish;
    }

    public void setCurrentProgress(float currentProgress) {
        this.currentProgress = Mth.m_14036_((float)currentProgress, (float)0.0f, (float)1.0f);
    }

    public void m_88315_(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        long now = Util.m_137550_();
        this.currentProgress = Mth.m_14036_((float)(this.currentProgress * 0.86f + this.reload.m_7750_() * 0.14f), (float)0.0f, (float)1.0f);
        float fade = 1.0f;
        if (this.fadeOutStart >= 0L) {
            fade = 1.0f - Mth.m_14036_((float)((float)(now - this.fadeOutStart) / 280.0f), (float)0.0f, (float)1.0f);
        }
        String status = MainMenuScreen.isRussian() ? "\u043f\u043e\u0434\u0433\u043e\u0442\u043e\u0432\u043a\u0430 \u0440\u0435\u0441\u0443\u0440\u0441\u043e\u0432" : "preparing resources";
        LoadingVisuals.renderFullScreen(gfx, this.minecraft.f_91062_, this.minecraft.m_91268_().m_85445_(), this.minecraft.m_91268_().m_85446_(), (float)now / 1000.0f, status, this.currentProgress);
        if (fade < 1.0f) {
            gfx.m_280509_(0, 0, this.minecraft.m_91268_().m_85445_(), this.minecraft.m_91268_().m_85446_(), (int)((1.0f - fade) * 255.0f) << 24);
        }
        if (this.fadeOutStart < 0L && this.reload.m_7746_() && this.currentProgress >= 0.995f) {
            this.fadeOutStart = now;
        }
        if (!this.completed && this.fadeOutStart >= 0L && now - this.fadeOutStart > 280L) {
            this.completed = true;
            try {
                this.reload.m_7748_();
                this.onFinish.accept(Optional.empty());
            }
            catch (Throwable throwable) {
                this.onFinish.accept(Optional.of(throwable));
            }
            if (this.minecraft.m_91265_() == this) {
                this.minecraft.m_91150_(null);
            }
        }
    }
}

