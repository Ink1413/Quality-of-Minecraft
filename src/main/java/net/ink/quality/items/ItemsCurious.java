package net.ink.quality.items;

import net.ink.quality.quality;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemsCurious {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "quality");

    public static final RegistryObject<Item> GOLDEN_NAMETAG =
            ITEMS.register("golden_nametag",
                    () -> new GoldenNameTag(new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.RARE)
                    ));

    public static void register(IEventBus eventBus) {

        ITEMS.register(eventBus);

    }

}
