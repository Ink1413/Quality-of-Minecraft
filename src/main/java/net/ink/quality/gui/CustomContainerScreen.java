package net.ink.quality.gui;

import com.mojang.blaze3d.systems.RenderSystem;
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

    private static final ResourceLocation TEXTURE = new ResourceLocation("quality", "textures/gui/golden_name_gui.png");

    // GUI dimensions (adjust these to match your texture's actual GUI area)
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 166;

    // Text field positions relative to the GUI (based on your texture measurements)
    // Name field: Top-left (59,20) to Bottom-right (168,35)
    private static final int NAME_FIELD_X = 59;
    private static final int NAME_FIELD_Y = 20;
    private static final int NAME_FIELD_WIDTH = 109;  // 168 - 59
    private static final int NAME_FIELD_HEIGHT = 15;  // 35 - 20

    // Lore field: Top-left (59,39) to Bottom-right (168,54)
    private static final int LORE_FIELD_X = 59;
    private static final int LORE_FIELD_Y = 39;
    private static final int LORE_FIELD_WIDTH = 109;  // 168 - 59
    private static final int LORE_FIELD_HEIGHT = 15;  // 54 - 39

    // Brown bar texture positions (at the bottom of your 256x256 texture)
    // Same size as red bars: 109x15
    // Milk brown bar (unfocused) - the lighter top one
    private static final int UNFOCUSED_BAR_U = 0;
    private static final int UNFOCUSED_BAR_V = 166;  // Adjust if needed

    // Dark brown bar (focused) - the darker bottom one
    private static final int FOCUSED_BAR_U = 0;
    private static final int FOCUSED_BAR_V = 182;    // Adjust if needed (166 + 15 = 181)

    private static final int BAR_WIDTH = 109;
    private static final int BAR_HEIGHT = 15;

    private EditBox nameField;
    private EditBox loreField;

    public CustomContainerScreen(CustomContainerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();

        // Create name text field (with 2px padding inside the bar)
        this.nameField = new EditBox(this.font, this.leftPos + NAME_FIELD_X + 2, this.topPos + NAME_FIELD_Y + 3,
                NAME_FIELD_WIDTH - 4, NAME_FIELD_HEIGHT - 4, Component.literal("Name"));
        this.nameField.setCanLoseFocus(true);
        this.nameField.setTextColor(-1);
        this.nameField.setTextColorUneditable(-1);
        this.nameField.setBordered(false); // We use custom background texture
        this.nameField.setMaxLength(50);
        this.nameField.setValue(this.menu.getItemName()); // Load existing name
        this.nameField.setResponder(this::onNameChanged);
        this.addWidget(this.nameField);

        // Create lore text field (with 2px padding inside the bar)
        this.loreField = new EditBox(this.font, this.leftPos + LORE_FIELD_X + 2, this.topPos + LORE_FIELD_Y + 3,
                LORE_FIELD_WIDTH - 4, LORE_FIELD_HEIGHT - 4, Component.literal("Lore"));
        this.loreField.setCanLoseFocus(true);
        this.loreField.setTextColor(-1);
        this.loreField.setTextColorUneditable(-1);
        this.loreField.setBordered(false); // We use custom background texture
        this.loreField.setMaxLength(100);
        this.loreField.setValue(this.menu.getItemLore()); // Load existing lore
        this.loreField.setResponder(this::onLoreChanged);
        this.addWidget(this.loreField);

        // Set initial focus to name field
        this.setInitialFocus(this.nameField);
    }

    private void onNameChanged(String name) {
        this.menu.setItemName(name);
    }

    private void onLoreChanged(String lore) {
        this.menu.setItemLore(lore);
    }

    @Override
    public void resize(net.minecraft.client.Minecraft minecraft, int width, int height) {
        String name = this.nameField.getValue();
        String lore = this.loreField.getValue();
        super.resize(minecraft, width, height);
        this.nameField.setValue(name);
        this.loreField.setValue(lore);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Allow typing in text fields
        if (this.nameField.isFocused()) {
            if (keyCode == 256) { // Escape
                this.nameField.setFocused(false);
                return true;
            }
            return this.nameField.keyPressed(keyCode, scanCode, modifiers);
        }
        if (this.loreField.isFocused()) {
            if (keyCode == 256) { // Escape
                this.loreField.setFocused(false);
                return true;
            }
            return this.loreField.keyPressed(keyCode, scanCode, modifiers);
        }

        // Tab to switch between fields
        if (keyCode == 258) { // Tab
            if (this.nameField.isFocused()) {
                this.nameField.setFocused(false);
                this.loreField.setFocused(true);
            } else {
                this.loreField.setFocused(false);
                this.nameField.setFocused(true);
            }
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.nameField.mouseClicked(mouseX, mouseY, button)) {
            this.loreField.setFocused(false);
            return true;
        }
        if (this.loreField.mouseClicked(mouseX, mouseY, button)) {
            this.nameField.setFocused(false);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // Draw main GUI background
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        // Draw text field backgrounds based on focus state
        // Name field background - draw at the name field position
        if (this.nameField.isFocused()) {
            guiGraphics.blit(TEXTURE, this.leftPos + NAME_FIELD_X, this.topPos + NAME_FIELD_Y,
                    FOCUSED_BAR_U, FOCUSED_BAR_V, BAR_WIDTH, BAR_HEIGHT);
        } else {
            guiGraphics.blit(TEXTURE, this.leftPos + NAME_FIELD_X, this.topPos + NAME_FIELD_Y,
                    UNFOCUSED_BAR_U, UNFOCUSED_BAR_V, BAR_WIDTH, BAR_HEIGHT);
        }

        // Lore field background - draw at the lore field position
        if (this.loreField.isFocused()) {
            guiGraphics.blit(TEXTURE, this.leftPos + LORE_FIELD_X, this.topPos + LORE_FIELD_Y,
                    FOCUSED_BAR_U, FOCUSED_BAR_V, BAR_WIDTH, BAR_HEIGHT);
        } else {
            guiGraphics.blit(TEXTURE, this.leftPos + LORE_FIELD_X, this.topPos + LORE_FIELD_Y,
                    UNFOCUSED_BAR_U, UNFOCUSED_BAR_V, BAR_WIDTH, BAR_HEIGHT);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // Render text fields
        this.nameField.render(guiGraphics, mouseX, mouseY, partialTick);
        this.loreField.render(guiGraphics, mouseX, mouseY, partialTick);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Optional: Draw labels for the text fields
        // guiGraphics.drawString(this.font, "Name:", 8, NAME_FIELD_Y, 4210752, false);
        // guiGraphics.drawString(this.font, "Lore:", 8, LORE_FIELD_Y, 4210752, false);

        // Don't draw the default title and inventory labels if you don't want them
        // super.renderLabels(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void onClose() {
        // Save the name and lore to the item when closing
        this.menu.saveToItem();
        super.onClose();
    }

}
