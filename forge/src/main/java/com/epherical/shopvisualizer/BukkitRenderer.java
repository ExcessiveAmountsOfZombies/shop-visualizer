package com.epherical.shopvisualizer;


import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class BukkitRenderer extends RenderCondition {


    public BukkitRenderer(String compoundTagToSearchFor) {
        super(compoundTagToSearchFor);
    }

    @Override
    public ItemStack getItem(ShopBlockEntity blockEntity, ItemStack fallbackItem, CompoundNBT tag) {
        return ShopVisualizer.getItemStackFromBukkitContainer(tag.getByteArray("shop-visualizer:item"), fallbackItem);
    }
}
