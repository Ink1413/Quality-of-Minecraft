package net.inkium.quality.commands.tpa;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class tpaDeny {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("tpadeny")
                .executes(tpaDeny::execute));

    }

    private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {

        ServerPlayer denier = ctx.getSource().getPlayerOrException();

        if (!tpaManager.gotRequest(denier.getUUID())) {

            denier.sendSystemMessage(Component.literal("§cYou have no pending teleport requests!"));
            return 0;

        }

        tpaManager.tpaRequest request = tpaManager.removeRequest(denier.getUUID());
        ServerPlayer sender = ctx.getSource().getServer().getPlayerList().getPlayer(request.getSenderId());

        denier.sendSystemMessage(Component.literal("§cTeleport request denied."));

        if (sender != null) {

            sender.sendSystemMessage(Component.literal("§c" + denier.getDisplayName().getString() + " denied your teleport request."));

        }

        return 1;

    }

}
