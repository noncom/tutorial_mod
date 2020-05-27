package com.noncom.tutorial_mod.blocks

import com.noncom.tutorial_mod.util.CustomEnergyStorage
import kotlinx.coroutines.flow.merge
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.IWorldPosCallable
import net.minecraft.util.IntReferenceHolder
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.World
import net.minecraftforge.energy.CapabilityEnergy
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.SlotItemHandler
import net.minecraftforge.items.wrapper.InvWrapper

class FirstBlockContainer(
        windowId: Int,
        val world: World,
        val position: BlockPos,
        playerInventory: PlayerInventory,
        val playerEntity: PlayerEntity)
    : Container(ModBlocks.FIRSTBLOCK_CONTAINER, windowId) {

    val tileEntity = world.getTileEntity(position)!!

    val playerInventory = InvWrapper(playerInventory)

    init {
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent { h ->
            addSlot(SlotItemHandler(h, 0, 64, 24))
        }

        layoutPlayerInventorySlots(10, 70)

        this.trackInt(object : IntReferenceHolder() {
            override fun get(): Int {
                return getEnergy()
            }

            override fun set(value: Int) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY)
                        .ifPresent { handler -> (handler as CustomEnergyStorage).setEnergy(value) }
            }

        })
    }

    fun getEnergy(): Int {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY)
                .map { handler -> handler.energyStored }.orElse(0)
    }

    override fun canInteractWith(playerIn: PlayerEntity): Boolean {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.world, tileEntity.pos), playerEntity, ModBlocks.FIRSTBLOCK)
    }

//    fun addSlot(handler: IItemHandler, index: Int, x: Int, y: Int) {
//        addSlot(SlotItemHandler(handler, index, x, y))
//    }

    fun addSlotRange(handler: IItemHandler, index: Int, x: Int, y: Int, amount: Int, dx: Int): Int {
        var vx = x
        var idx = index
        (0 until amount).forEach { i ->
            addSlot(SlotItemHandler(handler, idx, vx, y))
            vx += dx
            idx ++
        }
        return idx
    }

    fun addSlotBox(handler: IItemHandler, index: Int, x: Int, y: Int, horAmount: Int, dx: Int, verAmount: Int, dy: Int): Int {
        var vy = y
        var idx = index
        (0 until verAmount).forEach { j ->
            idx = addSlotRange(handler, idx, x, vy, horAmount, dx)
            vy += dy
        }
        return idx
    }

    fun  layoutPlayerInventorySlots(leftCol: Int, topRow: Int) {
        /** player inventory */
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18)

        /** hotbar */
        addSlotRange(playerInventory, 0, leftCol, topRow + 58, 9, 18)
    }

    override fun transferStackInSlot(playerIn: PlayerEntity, index: Int) : ItemStack {
        var stack = ItemStack.EMPTY
        val slot = inventorySlots[index]
        if(slot != null && slot.hasStack) {
            val slotStack = slot.stack
            stack = slotStack.copy()

            if(index == 0) {
                /** then it's the generator slot! -> move the item to any free player slot */
                if(mergeItemStack(slotStack, 1, 37, true)) {
                    return ItemStack.EMPTY
                }
                slot.onSlotChange(slotStack, stack)
            } else {
                /** then it's one of the player slots! move the item either to the generator, if it's
                 * a suitable fuel, or move between the players slots if it's not */
                if(slotStack.item == Items.DIAMOND) {
                    if(!mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (index < 28) {
                    if(!mergeItemStack(slotStack, 28, 37, false)) {
                        return ItemStack.EMPTY
                    }
                } else if (index < 37 && !mergeItemStack(slotStack, 1, 28, false)) {
                    return ItemStack.EMPTY
                }
            }

            if(slotStack.isEmpty) {
                slot.putStack(ItemStack.EMPTY)
            } else {
                slot.onSlotChanged()
            }

            if(stack.count == slotStack.count) {
                return ItemStack.EMPTY
            }

            slot.onTake(playerIn, slotStack)
        }

        return stack
    }
}