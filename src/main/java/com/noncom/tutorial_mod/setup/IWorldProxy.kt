package com.noncom.tutorial_mod.setup

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

interface IWorldProxy {

    fun init();

    fun getClientWorld() : World

    fun getClientPlayer(): PlayerEntity
}