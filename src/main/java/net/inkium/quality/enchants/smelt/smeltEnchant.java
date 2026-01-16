package net.inkium.quality.enchants.smelt;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class smeltEnchant extends Enchantment {

    public smeltEnchant() {

        super(Rarity.VERY_RARE, EnchantmentCategory.DIGGER, new EquipmentSlot[] { EquipmentSlot.MAINHAND });

    }

    @Override
    public int getMinCost(int level) {

        return 15 + (level - 1) * 9;

    }

    @Override
    public int getMaxCost(int level) {

        return getMinCost(level) + 50;

    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isTreasureOnly() {

        return true;

    }

    @Override
    public boolean isDiscoverable() {

        return true;

    }

}
