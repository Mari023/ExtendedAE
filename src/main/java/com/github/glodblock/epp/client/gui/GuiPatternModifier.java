package com.github.glodblock.epp.client.gui;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.Icon;
import appeng.client.gui.style.PaletteColor;
import appeng.client.gui.style.ScreenStyle;
import appeng.core.AppEng;
import com.github.glodblock.epp.client.button.ActionEPPButton;
import com.github.glodblock.epp.client.button.EPPIcon;
import com.github.glodblock.epp.container.ContainerPatternModifier;
import com.github.glodblock.epp.network.EPPNetworkHandler;
import com.github.glodblock.epp.network.packet.CGenericPacket;
import com.github.glodblock.epp.network.packet.CUpdatePage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class GuiPatternModifier extends AEBaseScreen<ContainerPatternModifier> {

    private final ActionEPPButton goMulti;
    private final ActionEPPButton goClone;
    private final ActionEPPButton clone;
    private final List<Button> multiBtns = new ArrayList<>();

    public GuiPatternModifier(ContainerPatternModifier menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.goMulti = new ActionEPPButton(b -> EPPNetworkHandler.INSTANCE.sendToServer(new CUpdatePage(0)), Icon.SCHEDULING_DEFAULT.getBlitter());
        this.goClone = new ActionEPPButton(b -> EPPNetworkHandler.INSTANCE.sendToServer(new CUpdatePage(1)), Icon.SCHEDULING_DEFAULT.getBlitter());
        this.clone = new ActionEPPButton(b -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("clone")), EPPIcon.RIGHT);
        this.goMulti.setMessage(Component.translatable("gui.expatternprovider.pattern_modifier.change"));
        this.goClone.setMessage(Component.translatable("gui.expatternprovider.pattern_modifier.change"));
        this.clone.setMessage(Component.translatable("gui.expatternprovider.pattern_modifier.clone.desc"));
        addToLeftToolbar(this.goMulti);
        addToLeftToolbar(this.goClone);
        this.multiBtns.add(
                Button.builder(Component.literal("x2"), b -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("modify", 2, 1)))
                        .size(23, 18)
                        .tooltip(Tooltip.create(Component.translatable("gui.expatternprovider.pattern_modifier.multi.desc", 2)))
                        .build()
        );
        this.multiBtns.add(
                Button.builder(Component.literal("x10"), b -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("modify", 10, 1)))
                        .size(23, 18)
                        .tooltip(Tooltip.create(Component.translatable("gui.expatternprovider.pattern_modifier.multi.desc", 10)))
                        .build()
        );
        this.multiBtns.add(
                Button.builder(Component.literal("÷2"), b -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("modify", 2, 0)))
                        .size(23, 18)
                        .tooltip(Tooltip.create(Component.translatable("gui.expatternprovider.pattern_modifier.div.desc", 2)))
                        .build()
        );
        this.multiBtns.add(
                Button.builder(Component.literal("÷10"), b -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("modify", 10, 0)))
                        .size(23, 18)
                        .tooltip(Tooltip.create(Component.translatable("gui.expatternprovider.pattern_modifier.div.desc", 10)))
                        .build()
        );
        this.multiBtns.add(
                Button.builder(Component.literal("Clear"), b -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("clear")))
                        .size(36, 18)
                        .tooltip(Tooltip.create(Component.translatable("gui.expatternprovider.pattern_modifier.clear.desc")))
                        .build()
        );
        this.imageHeight = 192;
    }

    @Override
    public void drawFG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY) {
        guiGraphics.drawString(
                this.font,
                Component.translatable("gui.expatternprovider.pattern_modifier", this.menu.page == 0 ?
                                Component.translatable("gui.expatternprovider.pattern_modifier.multiply") :
                                Component.translatable("gui.expatternprovider.pattern_modifier.clone")
                        ),
                8,
                6,
                style.getColor(PaletteColor.DEFAULT_TEXT_COLOR).toARGB(),
                false
        );
        if (this.menu.page == 1) {
            guiGraphics.drawString(
                    this.font,
                    Component.translatable("gui.expatternprovider.pattern_modifier.blank"),
                    52,
                    57,
                    style.getColor(PaletteColor.DEFAULT_TEXT_COLOR).toARGB(),
                    false
            );
            guiGraphics.drawString(
                    this.font,
                    Component.translatable("gui.expatternprovider.pattern_modifier.target"),
                    52,
                    25,
                    style.getColor(PaletteColor.DEFAULT_TEXT_COLOR).toARGB(),
                    false
            );
        }
    }

    @Override
    public void init() {
        super.init();
        this.multiBtns.get(0).setPosition(this.leftPos + 7, this.topPos + 19);
        this.multiBtns.get(1).setPosition(this.leftPos + 37, this.topPos + 19);
        this.multiBtns.get(2).setPosition(this.leftPos + 67, this.topPos + 19);
        this.multiBtns.get(3).setPosition(this.leftPos + 97, this.topPos + 19);
        this.multiBtns.get(4).setPosition(this.leftPos + 130, this.topPos + 19);
        this.multiBtns.forEach(this::addRenderableWidget);
        this.clone.setPosition(this.leftPos + 79, this.topPos + 35);
        this.addRenderableWidget(this.clone);
    }

    @Override
    protected void updateBeforeRender() {
        super.updateBeforeRender();
        this.menu.showPage(this.menu.page);
        if (this.menu.page == 0) {
            this.goMulti.setVisibility(false);
            this.goClone.setVisibility(true);
            this.clone.setVisibility(false);
            this.multiBtns.forEach(b -> b.visible = true);
        } else {
            this.goMulti.setVisibility(true);
            this.goClone.setVisibility(false);
            this.clone.setVisibility(true);
            this.multiBtns.forEach(b -> b.visible = false);
        }
    }

    @Override
    public void drawBG(GuiGraphics guiGraphics, int offsetX, int offsetY, int mouseX, int mouseY, float partialTicks) {
        if (this.menu.page == 0) {
            guiGraphics.blit(AppEng.makeId("textures/guis/pattern_editor_1.png"), offsetX, offsetY, 0, 0, 176, 192);
        } else {
            guiGraphics.blit(AppEng.makeId("textures/guis/pattern_editor_2.png"), offsetX, offsetY, 0, 0, 176, 192);
        }
        super.drawBG(guiGraphics, offsetX, offsetY, mouseX, mouseY, partialTicks);
    }

}