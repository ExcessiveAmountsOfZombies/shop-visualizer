package com.epherical.shopvisualizer.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public interface ShopBlockEntity {

    CompoundTag shop$getShopTag();

    void shop$setItemStack(ItemStack item);

    ItemStack shop$getItemStack();
}
