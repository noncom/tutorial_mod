package com.noncom.tutorial_mod.util

import net.minecraft.util.Direction

object KotlinFixes {

    /** this is a temporary workaround because in Kotlin 1.4M-1 `Direction.values()` would give a
     * compilation error (but would still work nevertheless), so to avoid error marks in the code,
     * use this for now, before 1.4M-2 which is going to fix this, becomes available */
    val directionValues = listOf<Direction>(Direction.DOWN, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.WEST)
}