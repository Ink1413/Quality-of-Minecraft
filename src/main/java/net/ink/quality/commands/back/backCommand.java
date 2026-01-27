package net.ink.quality.commands.back;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class backCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("back")
                .executes(backCommand::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();

        // Check if player has a last location
        if (!jailor.hasLastLocation(player.getUUID())) {
            player.sendSystemMessage(Component.literal("§cYou don't have a previous location to return to!"));
            return 0;
        }

        jailor.BackLocation lastLocation = jailor.getLastLocation(player.getUUID());
        ServerLevel targetLevel = ctx.getSource().getServer().getLevel(lastLocation.getDimension());

        // Check if the dimension still exists
        if (targetLevel == null) {
            player.sendSystemMessage(Component.literal("§cYour previous dimension no longer exists!"));
            return 0;
        }

        // Save current location before teleporting back
        jailor.setLastLocation(
                player.getUUID(),
                player.serverLevel().dimension(),
                player.blockPosition(),
                player.getYRot(),
                player.getXRot()
        );

        // Teleport player to their last location
        player.teleportTo(
                targetLevel,
                lastLocation.getPosition().getX() + 0.5,
                lastLocation.getPosition().getY(),
                lastLocation.getPosition().getZ() + 0.5,
                lastLocation.getYaw(),
                lastLocation.getPitch()
        );

        player.sendSystemMessage(Component.literal("§aTeleported back to your previous location!"));

        return 1;
    }

}
