package net.ink.quality.commands.back;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = net.ink.quality.quality.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class backDeath {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        // Check if the entity that died is a player
        if (event.getEntity() instanceof ServerPlayer player) {
            // Save the player's death location
            jailor.setLastLocation(
                    player.getUUID(),
                    player.serverLevel().dimension(),
                    player.blockPosition(),
                    player.getYRot(),
                    player.getXRot()
            );
        }
    }

}
