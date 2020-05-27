package com.noncom.tutorial_mod.blocks

import com.mojang.blaze3d.platform.GlStateManager
import com.noncom.tutorial_mod.TutorialMod
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import kotlin.math.min

class FirstBlockScreen(container: FirstBlockContainer, playerInventory: PlayerInventory, iTextComponent: ITextComponent)
    : ContainerScreen<FirstBlockContainer>(container, playerInventory, iTextComponent) {

    val GUI = ResourceLocation(TutorialMod.ID, "textures/gui/firstblock_gui.png")

    override fun render(mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.renderBackground()
        super.render(mouseX, mouseY, partialTicks)
        this.renderHoveredToolTip(mouseX, mouseY)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        font.drawString(title.formattedText, 8f, 6f, 4210752)
        drawString(Minecraft.getInstance().fontRenderer, "Energy: ${container.getEnergy()}", 10, 10, 0xffffff)
        font.drawString(playerInventory.displayName.formattedText, 8f, (ySize - 96 + 2).toFloat(), 4210752)
    }


    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        //GlStateManager.color4f(1f, 1f, 1f, 1f)
        minecraft!!.textureManager.bindTexture(GUI)
        val xs = xSize
        val ys = ySize
//        val xs = 820// xSize
//        val ys = 550// ySize
//        println("xs = $xs, ys = $ys")
        val relX = (width - xs) / 2
        val relY = (height - ys) / 2
        blit(relX, relY, 0, 0, xs, ys)
    }

}