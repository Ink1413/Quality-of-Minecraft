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

@Mod.EventBusSubscriber(modid = "quality")
public class AnvilEvent {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {

        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (right.getItem() == ItemsCurious.GOLDEN_NAMETAG.get() && right.hasTag()) {

            CompoundTag tag = right.getTag();

            String customName = tag.contains("CustomName") ? tag.getString("CustomName") : "";
            String customLore = tag.contains("CustomLore") ? tag.getString("CustomLore") : "";

            if (customName.isEmpty() && customLore.isEmpty()) {

                return;

            }

            ItemStack output = left.copy();

            if(!customName.isEmpty()) {

                output.setHoverName(Component.literal(customName));

            }

            if(!customLore.isEmpty()) {

                CompoundTag outputTag = output.getOrCreateTag();
                CompoundTag display = outputTag.getCompound("display");

                ListTag loreList = new ListTag();

                String[] loreLines = customLore.split("\\\\n|\\n");

                for (String line : loreLines) {

                    loreList.add(StringTag.valueOf(Component.Serializer.toJson(

                            Component.literal(line).withStyle(Style.EMPTY.withItalic(true))

                    )));

                }

                display.put("Lore", loreList);
                outputTag.put("display", display);

            }

            event.setOutput(output);
            event.setCost(0);
            event.setMaterialCost(1);

        }

    }

}
