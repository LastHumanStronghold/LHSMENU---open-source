/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.KeyMapping
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.ClientChatReceivedEvent$System
 *  net.minecraftforge.client.event.InputEvent$Key
 *  net.minecraftforge.client.event.RegisterKeyMappingsEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
 */
package net.lhs.lobby;

import net.lhs.lobby.LobbyScreen;
import net.lhs.lobby.LobbyTabList;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class LHSLobbyClient {
    private static KeyMapping openKey;

    private LHSLobbyClient() {
    }

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(LHSLobbyClient::registerKeys);
        MinecraftForge.EVENT_BUS.register(LobbyTabList.class);
    }

    private static void registerKeys(RegisterKeyMappingsEvent registerKeyMappingsEvent) {
        openKey = new KeyMapping("key.lhs_lobby.open", 77, "key.categories.lhs_lobby");
        registerKeyMappingsEvent.register(openKey);
    }

    @Mod.EventBusSubscriber(modid="lhs_lobby", bus=Mod.EventBusSubscriber.Bus.FORGE, value={Dist.CLIENT})
    public static final class ClientEvents {
        @SubscribeEvent
        public static void keyInput(InputEvent.Key key) {
            Minecraft minecraft = Minecraft.m_91087_();
            if (key.getKey() == 258 && key.getAction() == 1 && minecraft.f_91080_ == null) {
                LobbyTabList.toggleTab();
            }
            if (openKey != null && openKey.m_90859_() && Minecraft.m_91087_().f_91080_ == null) {
                LobbyTabList.closeTab();
                Minecraft.m_91087_().m_91152_((Screen)new LobbyScreen());
            }
        }

        @SubscribeEvent
        public static void receiveSystemChat(ClientChatReceivedEvent.System system) {
            String string = system.getMessage().getString();
            if ("LHS_LOBBY_OPEN_4F91".equals(string)) {
                system.setCanceled(true);
                Minecraft minecraft = Minecraft.m_91087_();
                minecraft.execute(() -> {
                    if (minecraft.f_91074_ != null) {
                        LobbyTabList.setLobbyActive(true);
                        LobbyTabList.closeTab();
                        minecraft.m_91152_((Screen)new LobbyScreen());
                    }
                });
            }
        }
    }
}
