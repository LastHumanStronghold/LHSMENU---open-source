/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  net.minecraftforge.server.permission.events.PermissionGatherEvent$Nodes
 *  net.minecraftforge.server.permission.nodes.PermissionDynamicContextKey
 *  net.minecraftforge.server.permission.nodes.PermissionNode
 *  net.minecraftforge.server.permission.nodes.PermissionTypes
 */
package com.mainlhs.server;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContextKey;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;

@Mod.EventBusSubscriber(modid="mainlhs", bus=Mod.EventBusSubscriber.Bus.FORGE)
public final class MainLHSPermissions {
    public static PermissionNode<Boolean> GENERIC_SCALE_COMMAND;
    public static PermissionNode<Boolean> REALISTIC_COMMAND;

    private MainLHSPermissions() {
    }

    @SubscribeEvent
    public static void onPermissionGather(PermissionGatherEvent.Nodes event) {
        GENERIC_SCALE_COMMAND = new PermissionNode("mainlhs", "command.genericscale", PermissionTypes.BOOLEAN, (player, uuid, context) -> player != null && player.m_20310_(2), new PermissionDynamicContextKey[0]);
        event.addNodes(new PermissionNode[]{GENERIC_SCALE_COMMAND});
        REALISTIC_COMMAND = new PermissionNode("mainlhs", "command.realistic", PermissionTypes.BOOLEAN, (player, uuid, context) -> player != null && player.m_20310_(2), new PermissionDynamicContextKey[0]);
        event.addNodes(new PermissionNode[]{REALISTIC_COMMAND});
    }
}

