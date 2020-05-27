package com.noncom.tutorial_mod.setup

import com.noncom.tutorial_mod.blocks.ModBlocks
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack

object ModSetup {

    val itemGroup = object : ItemGroup("tutorial_mod") {
        override fun createIcon(): ItemStack {
            return ItemStack(ModBlocks.FIRSTBLOCK)
        }
    }

    fun init() {

    }
}