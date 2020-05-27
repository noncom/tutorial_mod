package com.noncom.tutorial_mod

import com.noncom.tutorial_mod.blocks.FirstBlockContainer
import com.noncom.tutorial_mod.blocks.ModBlocks
import com.noncom.tutorial_mod.items.ModItems
import com.noncom.tutorial_mod.setup.ClientWorldProxy
import com.noncom.tutorial_mod.setup.ModSetup
import com.noncom.tutorial_mod.setup.ServerWorldProxy
import net.minecraft.block.Block
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.loading.FMLPaths
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.util.function.Supplier

@Mod(TutorialMod.ID)
object TutorialMod {

    val LOGGER = LogManager.getLogger()

    const val ID = "tutorial_mod"

    var worldProxy = DistExecutor.runForDist({ -> Supplier{ -> ClientWorldProxy() }}, { -> Supplier{ -> ServerWorldProxy() }})

    init {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG)
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG)

        MOD_BUS.addGenericListener(EventHandler::onBlockRegistry)
        MOD_BUS.addGenericListener(EventHandler::onItemRegistry)
        MOD_BUS.addGenericListener(EventHandler::onTileEntityRegistry)
        MOD_BUS.addGenericListener(EventHandler::onContainerRegistry)

        MOD_BUS.addListener(::setup)

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("tutorial_mod-client.toml"))
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("tutorial_mod-common.toml"))
    }

    fun setup(event: FMLCommonSetupEvent) {
        println("!!!SETUP: $event")
        ModSetup.init()
        worldProxy.init()
    }

    @Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    object EventHandler {

        @SubscribeEvent
        fun onBlockRegistry(event: RegistryEvent.Register<Block>) {
            LOGGER.info("Registering blocks...")
            event.registry.register(ModBlocks.FIRSTBLOCK)
        }

        @SubscribeEvent
        fun onItemRegistry(event: RegistryEvent.Register<Item>) {
            LOGGER.info("Registering items...")
            val properties = Item.Properties().group(ModSetup.itemGroup)

            event.registry.register(BlockItem(ModBlocks.FIRSTBLOCK, properties).setRegistryName("firstblock"))
            event.registry.register(ModItems.FIRSTITEM)
        }

        fun onTileEntityRegistry(event: RegistryEvent.Register<TileEntityType<*>>) {
            event.registry.register(ModBlocks.FIRSTBLOCK_TILE.setRegistryName("firstblock"))
        }

        fun onContainerRegistry(event: RegistryEvent.Register<ContainerType<*>>) {
            event.registry.register(ModBlocks.FIRSTBLOCK_CONTAINER.setRegistryName("firstblock"))
        }

        @SubscribeEvent
        fun onServerStarting(event: FMLServerStartingEvent) {

        }
    }
}