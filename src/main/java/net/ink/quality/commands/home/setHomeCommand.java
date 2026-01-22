package net.ink.quality.commands.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class setHomeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("sethome")
                .executes(setHomeCommand::executes));

    }

    private static int executes(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {

        ServerPlayer player = ctx.getSource().getPlayerOrException();

        landlord.setHome(

                player.getUUID(),
                player.serverLevel(),
                player.blockPosition(),
                player.getYRot(),
                player.getXRot()

        );

        player.sendSystemMessage(Component.literal("§aHome set at your current location!"));

        return 1;

    }

}
