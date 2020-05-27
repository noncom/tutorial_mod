package com.noncom.tutorial_mod.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.item.ItemStack
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks

class FirstBlock : Block(Properties
        .create(Material.IRON)
        .sound(SoundType.METAL)
        .hardnessAndResistance(2.0f)
        .lightValue(14)
) {

    init {
        setRegistryName("firstblock")
    }

    override fun hasTileEntity(state: BlockState) = true

    override fun getLightValue(state: BlockState): Int {
        if(state.get(BlockStateProperties.POWERED)) {
            return super.getLightValue(state)
        } else {
            return 0
        }
    }

    override fun createTileEntity(state: BlockState?, world: IBlockReader?): TileEntity? {
        return FirstBlockTile()
    }

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        if(placer != null){
            worldIn.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, placer)), 2)
        }
    }

    fun getFacingFromEntity(pos: BlockPos, entity: LivingEntity): Direction {
        return Direction.getFacingFromVector(entity.posX - pos.x, entity.posY - pos.y, entity.posZ - pos.z)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED)
    }

    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, player: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult): ActionResultType {
        if(!worldIn?.isRemote) {
            val tileEntity = worldIn!!.getTileEntity(pos)
            if(tileEntity is INamedContainerProvider) {
                NetworkHooks.openGui(player as ServerPlayerEntity, tileEntity as INamedContainerProvider, tileEntity.pos)
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit)
    }
}