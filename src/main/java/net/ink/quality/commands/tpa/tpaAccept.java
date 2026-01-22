package net.ink.quality.commands.tpa;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class tpaAccept {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("tpaccept")
                .executes(tpaAccept::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer accepter = ctx.getSource().getPlayerOrException();

        // Check if there's a pending request
        if (!tpaManager.gotRequest(accepter.getUUID())) {
            accepter.sendSystemMessage(Component.literal("§cYou have no pending teleport requests!"));
            return 0;
        }

        // Get and remove the request
        tpaManager.tpaRequest request = tpaManager.removeRequest(accepter.getUUID());
        ServerPlayer sender = ctx.getSource().getServer().getPlayerList().getPlayer(request.getSenderId());

        // Check if the sender is still online
        if (sender == null) {
            accepter.sendSystemMessage(Component.literal("§cThe player who sent the request is no longer online!"));
            return 0;
        }

        // Teleport the sender to the accepter
        sender.teleportTo(accepter.serverLevel(), accepter.getX(), accepter.getY(), accepter.getZ(),
                accepter.getYRot(), accepter.getXRot());

        // Send confirmation messages
        sender.sendSystemMessage(Component.literal("§aTeleport request accepted! Teleporting..."));
        accepter.sendSystemMessage(Component.literal("§aTeleport request accepted!"));

        return 1;
    }
}
