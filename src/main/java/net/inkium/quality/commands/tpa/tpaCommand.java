package net.inkium.quality.commands.tpa;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class tpaCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("tpa")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(tpaCommand::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {

        ServerPlayer sender = ctx.getSource().getPlayerOrException();
        ServerPlayer target = EntityArgument.getPlayer(ctx, "player");

        if (sender.equals(target)) {

            sender.sendSystemMessage(Component.literal("§cYou are already in that player!"));
            return 0;

        }

        if (tpaManager.hasPendingTPAFrom(sender.getUUID(), target.getUUID())) {

            sender.sendSystemMessage(Component.literal("§cYou already have a pending request to this player!"));
            return 0;

        }

        tpaManager.addedRequest(sender, target);

        sender.sendSystemMessage(Component.literal("§aTeleport request sent to " + target.getDisplayName().getString()));
        target.sendSystemMessage(Component.literal("§e" + sender.getDisplayName().getString() + " has requested to teleport to you."));
        target.sendSystemMessage(Component.literal("§eType /tpaccept to accept or /tpdeny to deny."));

        return 1;

    }

}
