package com.noncom.tutorial_mod.setup

import net.minecraft.client.Minecraft
import net.minecraft.world.World

class ClientWorldProxy : IWorldProxy {

    override fun getClientWorld(): World {
        return Minecraft.getInstance().world!!
    }

}