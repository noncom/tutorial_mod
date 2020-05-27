package com.noncom.tutorial_mod.blocks

import com.noncom.tutorial_mod.Config
import com.noncom.tutorial_mod.util.CustomEnergyStorage
import com.noncom.tutorial_mod.util.KotlinFixes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.energy.EnergyStorage
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.ItemStackHandler
import java.util.concurrent.atomic.AtomicInteger

class FirstBlockTile : TileEntity(ModBlocks.FIRSTBLOCK_TILE), ITickableTileEntity, INamedContainerProvider {

    val handler = object : ItemStackHandler(1) {
        override fun onContentsChanged(slot: Int) {
            markDirty() /** mark that the entity needs saving */
            super.onContentsChanged(slot)
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return stack.item == Items.DIAMOND
        }

        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            if (stack.item != Items.DIAMOND) {
                /** return stack unmodified */
                return stack;
            }
            return super.insertItem(slot, stack, simulate)
        }
    }

    val energy = CustomEnergyStorage(Config.FIRSTBLOCK_MAX_POWER.get(), 0)

    val handlerOpt = LazyOptional.of { handler }
    val energyOpt = LazyOptional.of { energy }

    var counter = 0

    override fun tick() {
        if(world!!.isRemote) {
            /** Only do everything of this on the server side */
            return
        }

        /** try to spend a diamond and generate power */
        if (counter > 0) {
            counter--
            if (counter <= 0) {
                energy.addEnergy(Config.FIRSTBLOCK_POWER_GENERATION.get())
            }
            markDirty()
        }

        /** try to extract a new diamond */
        if (counter <= 0) {
            val stack = handler.getStackInSlot(0)
            if (stack.item == Items.DIAMOND) {
                handler.extractItem(0, 1, false)
                counter = Config.FIRSTBLOCK_TICKS.get()
                markDirty() /** not needed actually because `handler.extractItem()` does that already by calling our `handler.onItemChanged()` */
            }
        }

        val blockState =  world!!.getBlockState(pos)
        if(blockState.get(BlockStateProperties.POWERED) != counter > 0) {
            /** the `flag=3` in the call means that it is going to be synchronized with the client */
            world!!.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, counter > 0), 3)
        }

        sendPowerOut()
    }

    fun sendPowerOut() {
        if(energy.energyStored > 0) {
            val energyAvailable = AtomicInteger(energy.energyStored)

            KotlinFixes.directionValues.forEach { direction ->
                val te = world!!.getTileEntity(pos.offset(direction))
                if (te != null) {
                    te.getCapability(CapabilityEnergy.ENERGY, direction).ifPresent { teEnergyHandler ->
                        if (energyAvailable.get() >= 0 && teEnergyHandler.canReceive()) {
                            val consumed  = teEnergyHandler.receiveEnergy(Math.min(energyAvailable.get(), Config.FIRSTBLOCK_POWER_SEND.get()), false)
                            energyAvailable.addAndGet(- consumed)
                            energy.removeEnergy(consumed)
                            markDirty()
                        }
                    }
                }
            }
        }
    }

    override fun read(tag: CompoundNBT) {
        val inv = tag.getCompound("inv")
        handler.deserializeNBT(inv)
        val energyTag = tag.getCompound("energy")
        //energy.setEnergy(inv.getInt("energy"))
        energy.deserializeNBT(energyTag)
        super.read(tag)
    }

    override fun write(tag: CompoundNBT): CompoundNBT {
        val compound = handler.serializeNBT()
        tag.put("inv", compound)
        val energyCompound = energy.serializeNBT()
        //tag.putInt("energy", energy.energyStored)
        tag.put("energy", energyCompound)
        return super.write(tag)
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handlerOpt.cast()
        }
        if (cap == CapabilityEnergy.ENERGY) {
            return energyOpt.cast()
        }
        return super.getCapability(cap, side)
    }


    override fun createMenu(i: Int, playerInventory: PlayerInventory, playerEntity: PlayerEntity): Container? {
        return FirstBlockContainer(i, world!!, pos, playerInventory, playerEntity)
    }

    override fun getDisplayName(): ITextComponent {
        return StringTextComponent(type.registryName!!.path)
        /** todo: use localized name instead of this */
    }

}