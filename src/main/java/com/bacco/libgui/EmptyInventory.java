package com.bacco.libgui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * An empty inventory that cannot hold any items.
 */
public class EmptyInventory implements Container {
    public static final EmptyInventory INSTANCE = new EmptyInventory();

    private EmptyInventory() {
    }

    @Override
    public void clearContent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int count) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setChanged() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

}
