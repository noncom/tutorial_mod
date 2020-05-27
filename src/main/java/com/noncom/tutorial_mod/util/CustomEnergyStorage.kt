package com.noncom.tutorial_mod.util

import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.INBT
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.energy.EnergyStorage

class CustomEnergyStorage(capacity: Int, maxTransfer: Int) : EnergyStorage(capacity, maxTransfer), INBTSerializable<CompoundNBT> {

    fun setEnergy(energy: Int) {
        this.energy = energy
    }

    fun addEnergy(energy: Int) {
        this.energy += energy
        if(this.energy > maxEnergyStored) {
            this.energy = maxEnergyStored
        }
    }

    fun removeEnergy(energy: Int) {
        this.energy -= energy
        if(this.energy < 0) {
            this.energy = 0
        }
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        setEnergy(nbt.getInt("energy"))
    }

    override fun serializeNBT(): CompoundNBT {
        val tag = CompoundNBT()
        tag.putInt("energy", energyStored)
        return tag
    }
}