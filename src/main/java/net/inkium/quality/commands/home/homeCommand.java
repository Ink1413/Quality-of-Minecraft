package net.inkium.quality.commands.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class homeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(Commands.literal("home")
                .executes(homeCommand::execute));

    }

    private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {

        ServerPlayer player = ctx.getSource().getPlayerOrException();

        if (!landlord.hasHome(player.getUUID())) {

            player.sendSystemMessage(Component.literal("§cYou don't have a home set! Use /sethome first."));
            return 0;

        }

        landlord.HomeLoco home = landlord.getHome(player.getUUID());
        ServerLevel targetLevel = ctx.getSource().getServer().getLevel(home.getDimension());

        if (targetLevel == null) {

            player.sendSystemMessage(Component.literal("§cYour home dimension no longer exists!"));
            return 0;

        }

        player.teleportTo(

                targetLevel,
                home.getPosition().getX() + 0.5,
                home.getPosition().getY(),
                home.getPosition().getZ() + 0.5,
                home.getYaw(),
                home.getPitch()

        );

        player.sendSystemMessage(Component.literal("§aWelcome home!"));
        return 1;

    }

}
