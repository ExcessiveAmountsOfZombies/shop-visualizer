package com.epherical.shopvisualizer.interfaces;

import com.epherical.shopvisualizer.RenderCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public interface ShopBlockEntity {

    CompoundTag shop$getShopTag();

    RenderCondition shop$getRenderCondition();

    void shop$setItemStack(ItemStack item);

    ItemStack shop$getItemStack();
}
