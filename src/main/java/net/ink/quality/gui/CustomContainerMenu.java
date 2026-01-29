package net.ink.quality.gui;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CustomContainerMenu extends AbstractContainerMenu {

    private final ItemStack goldenNameTag;
    private final int slotIndex; // The slot index where the golden name tag is held
    private String itemName = "";
    private String itemLore = "";

    // Client-side constructor (called when opening from packet)
    public CustomContainerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, extraData.readVarInt());
    }

    // Server-side constructor
    public CustomContainerMenu(int containerId, Inventory playerInventory, int slotIndex) {
        super(suffer.CUSTOM_MENU.get(), containerId);
        this.slotIndex = slotIndex;
        this.goldenNameTag = playerInventory.getItem(slotIndex);

        // Load existing name and lore from the item's NBT
        loadFromItem();

        // Add player inventory slots
        // First slot at x=7, y=83 (adjusted by 1 pixel)
        int inventoryStartX = 8;
        int inventoryStartY = 84;

        // Main inventory (3 rows of 9 slots)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        inventoryStartX + col * 18, inventoryStartY + row * 18));
            }
        }

        // Hotbar (1 row of 9 slots) - with 4 pixel gap below main inventory
        int hotbarY = inventoryStartY + (3 * 18) + 4; // 84 + 54 + 4 = 142
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, inventoryStartX + col * 18, hotbarY));
        }
    }

    private void loadFromItem() {
        if (!goldenNameTag.isEmpty() && goldenNameTag.hasTag()) {
            CompoundTag tag = goldenNameTag.getTag();
            if (tag != null) {
                // Load custom name
                if (tag.contains("CustomItemName")) {
                    this.itemName = tag.getString("CustomItemName");
                }
                // Load custom lore
                if (tag.contains("CustomItemLore")) {
                    this.itemLore = tag.getString("CustomItemLore");
                }
            }
        }
    }

    public void saveToItem() {
        if (!goldenNameTag.isEmpty()) {
            CompoundTag tag = goldenNameTag.getOrCreateTag();

            // Save the name
            if (!itemName.isEmpty()) {
                tag.putString("CustomItemName", itemName);
            } else {
                tag.remove("CustomItemName");
            }

            // Save the lore
            if (!itemLore.isEmpty()) {
                tag.putString("CustomItemLore", itemLore);
            } else {
                tag.remove("CustomItemLore");
            }

            // Also update the item's display name so it shows in inventory
            if (!itemName.isEmpty()) {
                goldenNameTag.setHoverName(net.minecraft.network.chat.Component.literal(itemName));
            } else {
                goldenNameTag.resetHoverName();
            }
        }
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String name) {
        this.itemName = name;
    }

    public String getItemLore() {
        return itemLore;
    }

    public void setItemLore(String lore) {
        this.itemLore = lore;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Standard shift-click behavior for inventory
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            // Since we only have player inventory, shift-click moves between main inventory and hotbar
            if (index < 27) {
                // From main inventory to hotbar
                if (!this.moveItemStackTo(slotStack, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From hotbar to main inventory
                if (!this.moveItemStackTo(slotStack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        // Check if the player still has the golden name tag
        return !goldenNameTag.isEmpty() && player.getInventory().getItem(slotIndex) == goldenNameTag;
    }
}

