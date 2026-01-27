package net.ink.quality.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomContainerMenu extends AbstractContainerMenu {

    private final Container inputSlots = new SimpleContainer(1) {

        @Override
        public void setChanged() {

            super.setChanged();
            CustomContainerMenu.this.slotsChanged(this);

        }

    };

    private final Container resultSlots = new SimpleContainer(1) {

        @Override
        public void setChanged() {

            super.setChanged();
            CustomContainerMenu.this.slotsChanged(this);

        }

    };

    private String titleText = "";
    private String subtitleText = "";

    public CustomContainerMenu(int containerId, Inventory playerInventory) {

        this(containerId, playerInventory, ItemStack.EMPTY);

    }

    public CustomContainerMenu(int containerId, Inventory playerInventory, ItemStack heldItem) {

        super(suffer.CUSTOM_MENU.get(), containerId);

        // Input slot (center-left area) - x=27, y=47
        this.addSlot(new Slot(inputSlots, 0, 27, 47) {

            @Override
            public boolean mayPlace(ItemStack stack) {

                // TODO: Check for your golden name tag item
                return stack.getItem() instanceof net.ink.quality.items.GoldenNameTag;

            }

        });

        // Output slot (center-right area) - x=134, y=47
        this.addSlot(new Slot(resultSlots, 0, 134, 47) {

            @Override
            public boolean mayPlace(ItemStack stack) {

                return false;

            }

            @Override
            public void onTake(Player player, ItemStack stack) {

                inputSlots.removeItem(0, 1);
                CustomContainerMenu.this.titleText = "";
                CustomContainerMenu.this.subtitleText = "";
                super.onTake(player, stack);

            }
        });

        // Player inventory (3 rows × 9 columns)
        for (int row = 0; row < 3; ++row) {

            for (int col = 0; col < 9; ++col) {

                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 7 + col * 18, 84 + row * 18));

            }
        }

        // Player hotbar
        for (int col = 0; col < 9; ++col) {

            this.addSlot(new Slot(playerInventory, col, 7 + col * 18, 142));

        }

        // If opened with item in hand, put it in input slot
        if (!heldItem.isEmpty()) {

            this.inputSlots.setItem(0, heldItem.copy());

        }
    }

    public void setTitleText(String text) {

        this.titleText = text;
        this.createResult();

    }

    public void setSubtitleText(String text) {

        this.subtitleText = text;
        this.createResult();

    }

    public String getTitleText() {

        return this.titleText;

    }

    public String getSubtitleText() {

        return this.subtitleText;

    }

    private void createResult() {

        ItemStack input = this.inputSlots.getItem(0);

        if (input.isEmpty()) {

            this.resultSlots.setItem(0, ItemStack.EMPTY);
            return;

        }

        ItemStack result = input.copy();

        // Set the display name if provided
        if (!this.titleText.isEmpty()) {

            result.setHoverName(Component.literal(this.titleText));

        }

        // Set lore if provided
        if (!this.subtitleText.isEmpty()) {

            List<Component> lore = new ArrayList<>();
            lore.add(Component.literal(this.subtitleText));

            result.getOrCreateTagElement("display").putString("Lore",
                    "[\"" + Component.Serializer.toJson(Component.literal(this.subtitleText)) + "\"]");

        }

        this.resultSlots.setItem(0, result);

    }

    @Override
    public void slotsChanged(Container container) {

        super.slotsChanged(container);

        if (container == this.inputSlots) {

            this.createResult();

        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {

            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == 1) {

                // Taking from output slot
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {

                    return ItemStack.EMPTY;

                }

                slot.onQuickCraft(itemstack1, itemstack);

            } else if (index != 0) {

                // Moving to input slot
                if (itemstack1.getItem() instanceof net.ink.quality.items.GoldenNameTag) {

                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {

                        return ItemStack.EMPTY;

                    }

                } else if (index < 29) {

                    if (!this.moveItemStackTo(itemstack1, 29, 38, false)) {

                        return ItemStack.EMPTY;

                    }

                } else if (index < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false)) {

                    return ItemStack.EMPTY;

                }

            } else if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {

                return ItemStack.EMPTY;

            }

            if (itemstack1.isEmpty()) {

                slot.setByPlayer(ItemStack.EMPTY);

            } else {

                slot.setChanged();

            }

            if (itemstack1.getCount() == itemstack.getCount()) {

                return ItemStack.EMPTY;

            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;

    }

    @Override
    public boolean stillValid(Player player) {

        return true;

    }

    @Override
    public void removed(Player player) {

        super.removed(player);
        if (!player.level().isClientSide) {

            ItemStack itemstack = this.inputSlots.removeItemNoUpdate(0);

            if (!itemstack.isEmpty()) {

                player.getInventory().placeItemBackInInventory(itemstack);

            }
        }
    }
}

