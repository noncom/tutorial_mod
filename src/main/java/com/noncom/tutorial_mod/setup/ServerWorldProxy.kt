package com.noncom.tutorial_mod.setup

import net.minecraft.world.World

class ServerWorldProxy : IWorldProxy {


    override fun getClientWorld(): World {
         throw IllegalStateException("Server: calling `getClientWorld()` is only possible on client!")
    }

}