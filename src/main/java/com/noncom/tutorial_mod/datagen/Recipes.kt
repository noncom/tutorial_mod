package com.noncom.tutorial_mod.datagen

import com.noncom.tutorial_mod.blocks.ModBlocks
import net.minecraft.advancements.criterion.InventoryChangeTrigger
import net.minecraft.data.DataGenerator
import net.minecraft.data.IFinishedRecipe
import net.minecraft.data.RecipeProvider
import net.minecraft.data.ShapedRecipeBuilder
import net.minecraft.item.Items
import net.minecraftforge.common.Tags
import java.util.function.Consumer

class Recipes(generator: DataGenerator) : RecipeProvider(generator) {

    override fun registerRecipes(consumer: Consumer<IFinishedRecipe>) {
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.FIRSTBLOCK)
                .patternLine("sis")
                .patternLine("idi")
                .patternLine("sis")
                .key('i', Items.IRON_INGOT)
                .key('s', Items.STONE)
                .key('d', Tags.Items.DYES_GRAY)
                .setGroup("tutorial_mod")
                .addCriterion("iron_ingot", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
                .build(consumer)
    }

}