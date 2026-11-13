/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.FloatArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.server.permission.PermissionAPI
 *  net.minecraftforge.server.permission.nodes.PermissionDynamicContext
 */
package com.mainlhs.server.command;

import com.mainlhs.common.scale.PlayerScaleData;
import com.mainlhs.common.scale.ScaleMath;
import com.mainlhs.server.MainLHSPermissions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionDynamicContext;

public final class GenericScaleCommand {
    private GenericScaleCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.m_82127_((String)"genericscale").requires(GenericScaleCommand::hasPermission)).then(Commands.m_82129_((String)"target", (ArgumentType)EntityArgument.m_91466_()).then(Commands.m_82129_((String)"size", (ArgumentType)FloatArgumentType.floatArg((float)0.1f, (float)64.0f)).executes(ctx -> GenericScaleCommand.setScale((CommandSourceStack)ctx.getSource(), EntityArgument.m_91474_((CommandContext)ctx, (String)"target"), FloatArgumentType.getFloat((CommandContext)ctx, (String)"size"))))));
    }

    private static boolean hasPermission(CommandSourceStack source) {
        if (!source.m_230897_()) {
            return true;
        }
        try {
            ServerPlayer player = source.m_81375_();
            if (MainLHSPermissions.GENERIC_SCALE_COMMAND == null) {
                return source.m_6761_(2);
            }
            return (Boolean)PermissionAPI.getPermission((ServerPlayer)player, MainLHSPermissions.GENERIC_SCALE_COMMAND, (PermissionDynamicContext[])new PermissionDynamicContext[0]);
        }
        catch (CommandSyntaxException e) {
            return false;
        }
    }

    private static int setScale(CommandSourceStack source, ServerPlayer target, float size) {
        PlayerScaleData.setSize((Player)target, size);
        float height = ScaleMath.heightFromSizeParam(size);
        source.m_288197_(() -> Component.m_237110_((String)"commands.mainlhs.genericscale.success", (Object[])new Object[]{target.m_5446_(), String.format("%.2f", Float.valueOf(size)), String.format("%.2f", Float.valueOf(height))}), true);
        return 1;
    }
}

