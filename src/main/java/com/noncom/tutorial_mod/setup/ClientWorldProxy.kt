package com.noncom.tutorial_mod.setup

import com.noncom.tutorial_mod.blocks.FirstBlockScreen
import com.noncom.tutorial_mod.blocks.ModBlocks
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScreenManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

class ClientWorldProxy : IWorldProxy {
    override fun init() {
//        ScreenManager.registerFactory(ModBlocks.FIRSTBLOCK_CONTAINER) { container, playerInventory, windowTitle ->
//            FirstBlockScreen(container, playerInventory, windowTitle)
//        }
        ScreenManager.registerFactory(ModBlocks.FIRSTBLOCK_CONTAINER, ::FirstBlockScreen)
    }

    override fun getClientWorld(): World {
        return Minecraft.getInstance().world!!
    }

    override fun getClientPlayer(): PlayerEntity {
        return Minecraft.getInstance().player!!
    }

}