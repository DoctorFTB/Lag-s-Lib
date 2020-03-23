package ftblag.lagslib.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public abstract class BaseContainer extends Container {

    protected final PlayerInventory playerInv;
    protected final IInventory tile;

    public BaseContainer(ContainerType<?> type, int id, PlayerInventory playerInv, PacketBuffer buffer) {
        this(type, id, playerInv, buffer.readBlockPos());
    }

    public BaseContainer(ContainerType<?> type, int id, PlayerInventory playerInv, BlockPos pos) {
        super(type, id);
        this.playerInv = playerInv;
        this.tile = (IInventory) playerInv.player.world.getTileEntity(pos);
        addSlots();
    }

    protected abstract void addSlots();

    protected void addPlayerSlots(int xS, int yS) {
        addPlayerSlots(xS, yS, 58);
    }

    protected void addPlayerSlots(int xS, int yS, int yChange) {
        for (int i = 0; i < 3; ++i) {
            for (int k = 0; k < 9; ++k) {
                addSlot(new Slot(playerInv, k + i * 9 + 9, xS + k * 18, i * 18 + yS));
            }
        }
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInv, i, xS + i * 18, yS + yChange));
        }
    }

    public IInventory getTileInventory() {
        return tile;
    }

    @Override
    public boolean canInteractWith(PlayerEntity p) {
        return tile.isUsableByPlayer(p);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.tile.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, this.tile.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.tile.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
