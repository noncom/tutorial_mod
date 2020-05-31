package com.noncom.tutorial_mod.datagen

import com.noncom.tutorial_mod.blocks.ModBlocks
import net.minecraft.data.DataGenerator

class LootTables(dataGenerator: DataGenerator) : BaseLootTableProvider(dataGenerator) {

    override fun addTables() {
        lootTables[ModBlocks.FIRSTBLOCK] = createStandardTable("firstblock", ModBlocks.FIRSTBLOCK)
    }
}