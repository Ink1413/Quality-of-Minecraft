package net.inkium.quality.commands.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class delHomeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("delhome")
                .executes(delHomeCommand::execute));

    }

    private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {

        ServerPlayer player = ctx.getSource().getPlayerOrException();

        if (!landlord.hasHome(player.getUUID())) {

            player.sendSystemMessage(Component.literal("§cYou don't have a home to delete!"));
            return 0;

        }

        landlord.delHome(player.getUUID());
        player.sendSystemMessage(Component.literal("§aYour home has been deleted."));

        return 1;

    }

}
