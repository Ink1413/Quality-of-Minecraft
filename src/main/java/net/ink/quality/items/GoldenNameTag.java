package net.ink.quality.items;

import net.ink.quality.gui.CustomContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.logging.Level;

public class GoldenNameTag extends Item {

    public GoldenNameTag(Properties properties) {

        super(properties);

    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemStack = player.getItemInHand(hand);

        if (player instanceof ServerPlayer serverPlayer) {

            serverPlayer.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new CustomContainerMenu(id, inv, itemStack),
                    Component.literal("Golden Name Tag")
            ));

        }

        return InteractionResultHolder.success(itemStack);

    }
}
