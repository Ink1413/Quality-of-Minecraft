package net.ink.quality.qol;

import net.ink.quality.quality;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = net.ink.quality.quality.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class noVoidTrident {

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        // Only run on server side
        if (!(event.getEntity().level() instanceof ServerLevel serverLevel)) return;

        // Check all trident entities in the world
        serverLevel.getEntitiesOfClass(ThrownTrident.class,
                        event.getEntity().getBoundingBox().inflate(500))
                .forEach(trident -> {
                    // Check if trident is below the void (Y < -64 for overworld)
                    if (trident.getY() < serverLevel.getMinBuildHeight()) {
                        // Get the owner of the trident
                        if (trident.getOwner() instanceof ServerPlayer player) {
                            // Get the trident item stack
                            ItemStack tridentStack = getTridentItemStack(trident);

                            if (tridentStack != null && !tridentStack.isEmpty()) {
                                // Add the trident back to the player's inventory
                                if (!player.getInventory().add(tridentStack.copy())) {
                                    // If inventory is full, drop it at player's feet
                                    player.drop(tridentStack.copy(), false);
                                }
                            }

                            // Remove the trident entity
                            trident.discard();
                        }
                    }
                });
    }

    private static ItemStack getTridentItemStack(ThrownTrident trident) {
        try {
            // Try to access the trident item field via reflection
            for (Field field : ThrownTrident.class.getDeclaredFields()) {
                if (field.getType() == ItemStack.class) {
                    field.setAccessible(true);
                    return (ItemStack) field.get(trident);
                }
            }
        } catch (Exception e) {
            // If reflection fails, return a default trident
            return new ItemStack(Items.TRIDENT);
        }
        return new ItemStack(Items.TRIDENT);
    }
}

