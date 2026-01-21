package net.inkium.quality.enchants.smelt;

import net.inkium.quality.enchants.pain;
import net.inkium.quality.quality;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = quality.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class smeltEnchantHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {

        if (event.getPlayer() == null) return;

        ItemStack tool = event.getPlayer().getMainHandItem();

        int moltenLevel = EnchantmentHelper.getItemEnchantmentLevel(
                pain.MOLTEN.get(), tool);

        if (moltenLevel == 0) return;

        BlockState state = event.getState();
        BlockPos pos = event.getPos();

        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;


        List<ItemStack> drops = Block.getDrops(state, serverLevel, pos,
                event.getLevel().getBlockEntity(pos), event.getPlayer(), tool);


        for (ItemStack drop : drops) {

            Optional<SmeltingRecipe> recipe = serverLevel.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(drop), serverLevel);

            if (recipe.isPresent()) {

                ItemStack result = recipe.get().getResultItem(serverLevel.registryAccess()).copy();
                result.setCount(drop.getCount());


                Block.popResource(serverLevel, pos, result);

            } else {

                Block.popResource(serverLevel, pos, drop);
            }
        }


        event.setCanceled(true);


        serverLevel.destroyBlock(pos, false, event.getPlayer());
    }

    private static ItemStack getSmeltedResult(ItemStack drop, BlockState state, ServerLevel level, BlockPos pos) {

        if(state.is(Blocks.STONE)) {

            return new ItemStack(Blocks.SMOOTH_STONE);

        }

        if (state.is(Blocks.SANDSTONE)) {

            return new ItemStack(Blocks.SMOOTH_SANDSTONE);

        }

        if (state.is(Blocks.RED_SANDSTONE)) {

            return new ItemStack(Blocks.SMOOTH_RED_SANDSTONE);

        }

        if (state.is(Blocks.QUARTZ_BLOCK)) {

            return new ItemStack(Blocks.SMOOTH_QUARTZ);

        }

        Optional<SmeltingRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(drop), level);

        return recipe.map(smeltingRecipe -> smeltingRecipe.getResultItem(level.registryAccess()).copy())
                .orElse(null);

    }

}
