package net.ink.quality.misc;

import net.ink.quality.items.ItemsCurious;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "quality", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnvilEvent {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();   // The item to be renamed
        ItemStack right = event.getRight(); // The golden name tag

        // Safety check - make sure left item exists
        if (left.isEmpty()) {
            return;
        }

        // Check if right slot contains our golden name tag with custom data
        if (right.getItem() == ItemsCurious.GOLDEN_NAMETAG.get() && right.hasTag()) {
            CompoundTag tag = right.getTag();

            if (tag == null) {
                return;
            }

            String customName = tag.contains("CustomItemName") ? tag.getString("CustomItemName") : "";
            String customLore = tag.contains("CustomItemLore") ? tag.getString("CustomItemLore") : "";

            // Only proceed if there's something to apply
            if (customName.isEmpty() && customLore.isEmpty()) {
                return;
            }

            // Create the output item
            ItemStack output = left.copy();

            // Apply the custom name
            if (!customName.isEmpty()) {
                // You can customize the style here (color, italic, etc.)
                output.setHoverName(Component.literal(customName));
            }

            // Apply the custom lore
            if (!customLore.isEmpty()) {
                CompoundTag outputTag = output.getOrCreateTag();
                CompoundTag display;
                if (outputTag.contains("display")) {
                    display = outputTag.getCompound("display");
                } else {
                    display = new CompoundTag();
                }

                ListTag loreList = new ListTag();
                // Split by newline if you want multi-line lore support
                String[] loreLines = customLore.split("\\\\n|\\n");
                for (String line : loreLines) {
                    // Lore needs to be stored as JSON text components
                    loreList.add(StringTag.valueOf(Component.Serializer.toJson(
                            Component.literal(line).withStyle(Style.EMPTY.withItalic(false))
                    )));
                }

                display.put("Lore", loreList);
                outputTag.put("display", display);
            }

            // Set the output
            event.setOutput(output);

            // Set the XP cost (adjust as needed)
            event.setCost(1);

            // Consume only 1 golden name tag
            event.setMaterialCost(1);
        }
    }

}
