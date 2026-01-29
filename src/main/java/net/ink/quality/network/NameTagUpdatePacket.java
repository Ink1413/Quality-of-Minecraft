package net.ink.quality.network;

import net.ink.quality.gui.CustomContainerMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NameTagUpdatePacket {

    private final int slotIndex;
    private final String itemName;
    private final String itemLore;

    public NameTagUpdatePacket(int slotIndex, String itemName, String itemLore) {
        this.slotIndex = slotIndex;
        this.itemName = itemName;
        this.itemLore = itemLore;
    }

    public NameTagUpdatePacket(FriendlyByteBuf buf) {
        this.slotIndex = buf.readVarInt();
        this.itemName = buf.readUtf(256);
        this.itemLore = buf.readUtf(512);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(slotIndex);
        buf.writeUtf(itemName, 256);
        buf.writeUtf(itemLore, 512);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ItemStack stack = player.getInventory().getItem(slotIndex);

                // Verify it's a golden name tag (optional safety check)
                // if (!(stack.getItem() instanceof GoldenNameTagItem)) return;

                if (!stack.isEmpty()) {
                    CompoundTag tag = stack.getOrCreateTag();

                    // Save the custom name
                    if (!itemName.isEmpty()) {
                        tag.putString("CustomItemName", itemName);
                        stack.setHoverName(Component.literal(itemName));
                    } else {
                        tag.remove("CustomItemName");
                        stack.resetHoverName();
                    }

                    // Save the custom lore
                    if (!itemLore.isEmpty()) {
                        tag.putString("CustomItemLore", itemLore);
                    } else {
                        tag.remove("CustomItemLore");
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }

}
