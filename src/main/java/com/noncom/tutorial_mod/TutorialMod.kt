package com.noncom.tutorial_mod

import com.noncom.tutorial_mod.blocks.FirstBlock
import com.noncom.tutorial_mod.blocks.ModBlocks
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(TutorialMod.ID)
object TutorialMod {

    val LOGGER = LogManager.getLogger()

    const val ID = "tutorial_mod"

    init {
        MOD_BUS.addGenericListener(::registerBlocks)
        MOD_BUS.addGenericListener(EventHandler::onBlockRegistry)
        MOD_BUS.addGenericListener(EventHandler::onItemRegistry)
    }

    fun registerBlocks(event: RegistryEvent.Register<Block>) {

    }

    @Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    object EventHandler {

        @SubscribeEvent
        fun onBlockRegistry(event: RegistryEvent.Register<Block>) {
            LOGGER.info("Registering blocks...")
            event.registry.register(FirstBlock())
        }

        @SubscribeEvent
        fun onItemRegistry(event: RegistryEvent.Register<Item>) {
            LOGGER.info("Registering items...")
            var bi = BlockItem(ModBlocks.FIRSTBLOCK, Item.Properties())
            bi.setRegistryName("firstblock")
            event.registry.register(bi)
        }

        @SubscribeEvent
        fun onServerStarting(event: FMLServerStartingEvent) {

        }
    }
}