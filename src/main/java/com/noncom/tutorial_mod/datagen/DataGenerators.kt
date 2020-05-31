package com.noncom.tutorial_mod.datagen

import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
object DataGenerators {


    @SubscribeEvent
    fun gatherData(event: GatherDataEvent){
        val generator = event.generator
        if(event.includeServer()) {
            generator.addProvider(Recipes(generator))
            generator.addProvider(LootTables(generator))
        }
        if(event.includeClient()) {
            //generator.addProvider(BlockStates(generator, event.existingFileHelper))
        }
    }
}