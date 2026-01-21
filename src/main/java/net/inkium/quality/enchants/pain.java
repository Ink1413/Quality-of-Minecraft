package net.inkium.quality.enchants;

import net.inkium.quality.enchants.smelt.smeltEnchant;
import net.inkium.quality.quality;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class pain {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, quality.MODID);

    public static final RegistryObject<Enchantment> MOLTEN =
            ENCHANTMENTS.register("molten", smeltEnchant::new);

    public static void register(IEventBus bus) {

        ENCHANTMENTS.register(bus);

    }
}
