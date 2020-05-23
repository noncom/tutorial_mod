package com.noncom.tutorial_mod.blocks

import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material

class FirstBlock : Block(Properties
        .create(Material.IRON)
        .sound(SoundType.METAL)
        .hardnessAndResistance(2.0f)
//        .lightValue(24)
) {

    init {
        setRegistryName("firstblock")
    }

}