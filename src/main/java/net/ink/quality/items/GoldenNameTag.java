package net.ink.quality.items;

import net.ink.quality.gui.CustomContainerMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GoldenNameTag extends Item {

    public GoldenNameTag(Properties properties) {

        super(properties);

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {

            int slotIndex = hand == InteractionHand.MAIN_HAND ?
                    player.getInventory().selected : Inventory.SLOT_OFFHAND;

            NetworkHooks.openScreen(serverPlayer, new MenuProvider() {

                @Override
                public Component getDisplayName() {

                    return Component.literal("Golden Name Tag");

                }

                @Override
                public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {

                    return new CustomContainerMenu(containerId, playerInventory, slotIndex);

                }

            }, buf -> buf.writeVarInt(slotIndex));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());

    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {

        super.appendHoverText(stack, level, tooltip, flag);

        if (stack.hasTag()) {

            CompoundTag tag = stack.getTag();

            // Show stored name
            if (tag.contains("CustomItemName")) {

                String name = tag.getString("CustomItemName");

                if (!name.isEmpty()) {

                    tooltip.add(Component.literal("§7Name: §f" + name));

                }
            }

            // Show stored lore
            if (tag.contains("CustomItemLore")) {

                String lore = tag.getString("CustomItemLore");

                if (!lore.isEmpty()) {

                    tooltip.add(Component.literal("§7Lore: §f" + lore));

                }
            }
        }

        // Usage hint
        tooltip.add(Component.literal("§8Right-click to edit"));
        tooltip.add(Component.literal("§8Use in anvil to apply"));
    }
}
