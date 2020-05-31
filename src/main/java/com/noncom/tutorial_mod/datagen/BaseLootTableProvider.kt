package com.noncom.tutorial_mod.datagen

import com.google.gson.GsonBuilder
import net.minecraft.block.Block
import net.minecraft.data.DataGenerator
import net.minecraft.data.DirectoryCache
import net.minecraft.data.IDataProvider
import net.minecraft.data.LootTableProvider
import net.minecraft.util.ResourceLocation
import net.minecraft.world.storage.loot.*
import net.minecraft.world.storage.loot.functions.CopyName
import net.minecraft.world.storage.loot.functions.CopyNbt
import net.minecraft.world.storage.loot.functions.SetContents
import java.util.logging.LogManager

abstract class BaseLootTableProvider(val dataGenerator: DataGenerator) : LootTableProvider(dataGenerator) {

    val LOGGER = LogManager.getLogManager().getLogger("LootTables")
    val GSON = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    val lootTables = mutableMapOf<Block, LootTable.Builder>()

    abstract fun addTables()

    fun createStandardTable(name: String, block: Block): LootTable.Builder {
        val builder = LootPool.builder()
                .name(name)
                .rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(block)
                        .acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY))
                        .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                .addOperation("inv", "BlockEntityTag.inv", CopyNbt.Action.REPLACE)
                                .addOperation("energy", "BlockEntityTag.energy", CopyNbt.Action.REPLACE))
                        .acceptFunction(SetContents.func_215920_b()
                                .func_216075_a(DynamicLootEntry.func_216162_a(ResourceLocation("minecraft", "contents")))))
        return LootTable.builder().addLootPool(builder)
    }

    override fun act(cache: DirectoryCache) {
        addTables()

        val tables = mutableMapOf<ResourceLocation, LootTable>()
        lootTables.entries.forEach { entry ->
            tables[entry.key.lootTable] = entry.value.setParameterSet(LootParameterSets.BLOCK).build()
        }

        writeTables(cache, tables)
    }

    private fun writeTables(cache: DirectoryCache, tables: Map<ResourceLocation, LootTable>) {
        val outputFolder = dataGenerator.outputFolder
        tables.forEach { key, lootTable ->
            val path = outputFolder.resolve("data/${key.namespace}/loot_tables/${key.path}.json" )
            try {
                IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path)
            } catch(e: Exception) {
                LOGGER.severe("Can't write loot table, path = $path, exception = ${e.localizedMessage}")
                throw Exception(e)
            }
        }
    }

    override fun getName(): String {
        return "TutorialMod LottTables"
    }
}