package com.epherical.shopvisualizer.interfaces;

import com.epherical.shopvisualizer.RenderCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public interface ShopBlockEntity {

    CompoundNBT shop$getShopTag();

    RenderCondition shop$getRenderCondition();

    void shop$setItemStack(ItemStack item);

    ItemStack shop$getItemStack();
}
