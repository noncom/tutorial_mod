package com.noncom.tutorial_mod

import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.io.WritingMode
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import java.nio.file.Path

@Mod.EventBusSubscriber
object Config {

    val CATEGORY_GENERAL = "general"
    val CATEGORY_POWER = "power"

    val SUBCATEGORY_FIRSTBLOCK = "firstblock"

    val COMMON_BUILDER = ForgeConfigSpec.Builder()
    val CLIENT_BUILDER = ForgeConfigSpec.Builder()

    val COMMON_CONFIG: ForgeConfigSpec
    val CLIENT_CONFIG: ForgeConfigSpec

    lateinit var FIRSTBLOCK_MAX_POWER: ForgeConfigSpec.IntValue
    lateinit var FIRSTBLOCK_POWER_GENERATION: ForgeConfigSpec.IntValue
    lateinit var FIRSTBLOCK_POWER_SEND: ForgeConfigSpec.IntValue
    lateinit var FIRSTBLOCK_TICKS: ForgeConfigSpec.IntValue

    init {
        fun setupFirstBlockConfig() {
            COMMON_BUILDER.comment("FirstBlock settings").push(SUBCATEGORY_FIRSTBLOCK)
            FIRSTBLOCK_MAX_POWER = COMMON_BUILDER.comment("Maximum power for the FirstBlock generator")
                    .defineInRange("maxPower", 100_000, 0, Int.MAX_VALUE)
            FIRSTBLOCK_POWER_GENERATION = COMMON_BUILDER.comment("Power generation per diamond")
                    .defineInRange("generation", 1_000, 0, Int.MAX_VALUE)
            FIRSTBLOCK_POWER_SEND = COMMON_BUILDER.comment("Power amount that can be transmitted per tick")
                    .defineInRange("send", 100, 0, Int.MAX_VALUE)
            FIRSTBLOCK_TICKS = COMMON_BUILDER.comment("Amount of ticks needed to generate 1 power generation stage")
                    .defineInRange("ticks", 20, 0, Int.MAX_VALUE)
            COMMON_BUILDER.pop()
        }

        COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL)
        COMMON_BUILDER.pop()

        COMMON_BUILDER.comment("Power settings").push(CATEGORY_POWER)
        setupFirstBlockConfig()
        COMMON_BUILDER.pop()

        COMMON_CONFIG = COMMON_BUILDER.build()
        CLIENT_CONFIG = CLIENT_BUILDER.build()
    }

    fun loadConfig(spec: ForgeConfigSpec, path: Path) {
        val configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build()

        configData.load()
        spec.setConfig(configData)
    }

    @SubscribeEvent
    fun onLoad(configEvent: ModConfig.Loading) {

    }

    @SubscribeEvent
    fun onFileChange(configEvent: ModConfig.Reloading) {

    }

}