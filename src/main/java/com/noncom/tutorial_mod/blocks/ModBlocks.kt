package com.noncom.tutorial_mod.blocks

import com.noncom.tutorial_mod.TutorialMod
import net.minecraft.inventory.container.ContainerType
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.registries.ObjectHolder

object ModBlocks {

    @ObjectHolder("tutorial_mod:firstblock")
    var FIRSTBLOCK: FirstBlock = FirstBlock()

    @ObjectHolder("tutorial_mod:firstblock")
    var FIRSTBLOCK_TILE: TileEntityType<FirstBlockTile> = TileEntityType.Builder.create({FirstBlockTile()}, FIRSTBLOCK).build(null)

    var FIRSTBLOCK_CONTAINER: ContainerType<FirstBlockContainer> =
            IForgeContainerType.create { windowId, playerInventory, data ->
                FirstBlockContainer(windowId, TutorialMod.worldProxy.getClientWorld(), data.readBlockPos(), playerInventory, TutorialMod.worldProxy.getClientPlayer())
            }
}