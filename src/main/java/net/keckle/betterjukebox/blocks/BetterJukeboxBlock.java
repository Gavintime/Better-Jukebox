package net.keckle.betterjukebox.blocks;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.keckle.betterjukebox.MainInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BetterJukeboxBlock extends Block implements BlockEntityProvider {

    public BetterJukeboxBlock(Settings settings) {
        super(settings);
    }


    //this creates an instance of the better jukebox entity when the better jukebox block is placed
    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new BetterJukeboxEntity();
    }


    // Scatter the items in the chest when it is removed. stop audio
    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BetterJukeboxEntity) {

                //stops any disc music playing
                ((BetterJukeboxEntity) blockEntity).stop();

                ItemScatterer.spawn(world, pos, (BetterJukeboxInventory) blockEntity);
                // update comparators
                world.updateHorizontalAdjacent(pos, this);
            }
            super.onBlockRemoved(state, world, pos, newState, moved);
        }
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.PASS;

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof BetterJukeboxEntity) {
            ContainerProviderRegistry.INSTANCE.openContainer(MainInit.BETTER_JUKEBOX, player, (packetByteBuf -> packetByteBuf.writeBlockPos(pos)));
        }

        return ActionResult.SUCCESS;
    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BetterJukeboxEntity) {
                ((BetterJukeboxEntity) blockEntity).setCustomName(itemStack.getName());
            }
        }
    }


    //TODO: when is this called?
    @Override
    public boolean onBlockAction(BlockState state, World world, BlockPos pos, int type, int data) {
        super.onBlockAction(state, world, pos, type, data);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.onBlockAction(type, data);
    }


    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }


    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        //TODO: calculate comparator output based on current music disc at index?
        return Container.calculateComparatorOutput(world.getBlockEntity(pos));
    }
}
