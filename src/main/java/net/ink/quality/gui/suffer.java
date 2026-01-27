package net.ink.quality.gui;

import net.ink.quality.quality;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class suffer {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, quality.MODID);

    public static final RegistryObject<MenuType<GoldenNameTagMenu>> GOLDEN_NAMETAG_MENU =
            MENUS.register("golden_nametag_menu", () ->
                    IForgeMenuType.create(GoldenNameTagMenu::new));

    public static final RegistryObject<MenuType<CustomContainerMenu>> CUSTOM_MENU =
            MENUS.register("custom_menu", () ->
                    IForgeMenuType.create((windowId, inv, data) -> new CustomContainerMenu(windowId, inv)));

    public static void register(IEventBus eventBus) {

        MENUS.register(eventBus);

    }

}
