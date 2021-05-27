package com.epherical.shopvisualizer;

import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class GunpowderRenderer extends RenderCondition {

    public GunpowderRenderer(String compoundTagToSearchFor) {
        super(compoundTagToSearchFor);
    }

    @Override
    public ItemStack getItem(ShopBlockEntity blockEntity, ItemStack fallbackItem, CompoundTag tag) {
        return tag.get("item") != null ? ItemStack.fromTag((CompoundTag) tag.get("item")) : fallbackItem;
    }
}
