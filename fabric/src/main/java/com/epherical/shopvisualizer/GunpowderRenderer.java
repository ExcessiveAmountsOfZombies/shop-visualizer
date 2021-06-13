package com.epherical.shopvisualizer;

import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class GunpowderRenderer extends RenderCondition {

    public GunpowderRenderer(String compoundTagToSearchFor) {
        super(compoundTagToSearchFor);
    }

    @Override
    public ItemStack getItem(ShopBlockEntity blockEntity, ItemStack fallbackItem, NbtCompound tag) {
        return tag.get("item") != null ? ItemStack.fromNbt((NbtCompound) tag.get("item")) : fallbackItem;
    }
}
