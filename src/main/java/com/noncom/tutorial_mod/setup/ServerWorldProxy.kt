package com.noncom.tutorial_mod.setup

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

class ServerWorldProxy : IWorldProxy {

    override fun init() {

    }

    override fun getClientWorld(): World {
         throw IllegalStateException("Server: calling `getClientWorld()` is only possible on client!")
    }

    override fun getClientPlayer(): PlayerEntity {
        throw IllegalStateException("Server: calling `getClientPlayer()` is only possible on client!")
    }

}