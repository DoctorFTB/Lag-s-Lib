package ftblag.lagslib.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IInventoryHelper extends IInventory {

    NonNullList<ItemStack> getInventory();

    @Override
    default int getSizeInventory() {
        return getInventory().size();
    }

    @Override
    default boolean isEmpty() {
        for (ItemStack itemstack : getInventory()) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default ItemStack getStackInSlot(int index) {
        return getInventory().get(index);
    }

    @Override
    default ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(getInventory(), index, count);
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
        return itemstack;
    }

    @Override
    default ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(getInventory(), index);
    }

    @Override
    default void setInventorySlotContents(int index, ItemStack stack) {
        getInventory().set(index, stack);
        if (stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
        this.markDirty();
    }

    @Override
    default void clear() {
        getInventory().clear();
    }
}
