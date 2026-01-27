package net.ink.quality.gui;

import net.ink.quality.network.NameTagUpdatePacket;
import net.ink.quality.network.networking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.ink.quality.quality;


public class CustomContainerScreen extends AbstractContainerScreen<CustomContainerMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(quality.MODID, "textures/gui/custom_container.png");

    private EditBox nameEdit;
    private EditBox loreEdit;

    public CustomContainerScreen(CustomContainerMenu menu, Inventory playerIventory, Component title) {

        super(menu, playerIventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;

    }

    @Override
    protected void init() {

        super.init();

        this.nameEdit = new EditBox(this.font, this. leftPos + 28, this.topPos + 14, 120, 12, Component.literal("Name"));
        this.nameEdit.setCanLoseFocus(false);
        this.nameEdit.setTextColor(-1);
        this.nameEdit.setTextColorUneditable(-1);
        this.nameEdit.setBordered(false);
        this.nameEdit.setMaxLength(50);
        this.nameEdit.setResponder(this::onNameChanged);
        this.nameEdit.setValue(this.menu.getTitleText());
        this.addWidget(this.nameEdit);
        this.setInitialFocus(this.nameEdit);

        this.loreEdit = new EditBox(this.font, this.leftPos + 28, this.topPos + 14, 120, 12, Component.literal("Lore"));
        this.loreEdit.setCanLoseFocus(false);
        this.loreEdit.setTextColor(-1);
        this.loreEdit.setTextColorUneditable(-1);
        this.loreEdit.setBordered(false);
        this.loreEdit.setMaxLength(50);
        this.loreEdit.setResponder(this::onLoreChanged);
        this.loreEdit.setValue(this.menu.getSubtitleText());
        this.addWidget(this.loreEdit);

    }

    @Override
    public void resize(net.minecraft.client.Minecraft minecraft, int width, int height) {

        String name = this.nameEdit.getValue();
        String lore = this.loreEdit.getValue();

        super.resize(minecraft, width, height);

        this.nameEdit.setValue(name);
        this.loreEdit.setValue(lore);

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {

        if (keyCode == 256) {

            this.minecraft.player.closeContainer();
            return true;

        }

        if (keyCode == 258) {

            if (this.nameEdit.isFocused()) {

                this.nameEdit.setFocused(false);
                this.loreEdit.setFocused(true);

            } else {

                this.loreEdit.setFocused(false);
                this.nameEdit.setFocused(true);

            }

            return true;

        }

        return this.nameEdit.keyPressed(keyCode, scanCode, modifiers)
                || this.nameEdit.canConsumeInput()
                || this.loreEdit.keyPressed(keyCode, scanCode, modifiers)
                || this.loreEdit.canConsumeInput()
                || super.keyPressed(keyCode, scanCode, modifiers);

    }

    private void onNameChanged(String newName) {

        networking.sendToServer(new NameTagUpdatePacket(newName, this.loreEdit.getValue()));

    }

    private void onLoreChanged(String newLore) {

        networking.sendToServer(new NameTagUpdatePacket(this.nameEdit.getValue(), newLore));

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {

        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {

        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.nameEdit.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.loreEdit.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}

}
