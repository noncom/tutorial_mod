package com.noncom.tutorial_mod.setup

import net.minecraft.world.World

interface IWorldProxy {

    fun getClientWorld() : World

}