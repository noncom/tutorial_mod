package com.noncom.tutorial_mod.items

import com.noncom.tutorial_mod.TutorialMod
import com.noncom.tutorial_mod.setup.ModSetup
import net.minecraft.item.Item

class FirstItem : Item(Properties()
        .group(ModSetup.itemGroup)
        .maxStackSize(1)) {

    init {
        setRegistryName("firstitem")
    }

}