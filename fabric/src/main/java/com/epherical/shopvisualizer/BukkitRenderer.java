package com.epherical.shopvisualizer;


import com.epherical.shopvisualizer.client.ShopVisualizerClient;
import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class BukkitRenderer extends RenderCondition {


    public BukkitRenderer(String compoundTagToSearchFor) {
        super(compoundTagToSearchFor);
    }

    @Override
    public ItemStack getItem(ShopBlockEntity blockEntity, ItemStack fallbackItem, CompoundTag tag) {
        return ShopVisualizerClient.getItemStackFromBukkitContainer(tag.getByteArray("shop-visualizer:item"), fallbackItem);
    }
}
