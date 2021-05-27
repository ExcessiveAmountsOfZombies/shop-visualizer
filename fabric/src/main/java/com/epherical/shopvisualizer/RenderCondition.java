package com.epherical.shopvisualizer;

import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public abstract class RenderCondition {


    private final String compoundSearch;

    public RenderCondition(String compoundTagToSearchFor) {
        this.compoundSearch = compoundTagToSearchFor;
    }


    public CompoundTag getCompound(CompoundTag tag) {
        return tag.getCompound(compoundSearch);
    }

    public abstract ItemStack getItem(ShopBlockEntity blockEntity, ItemStack fallbackItem, CompoundTag tag);



    public interface RenderFactory<T extends RenderCondition> {
        T create(String compoundTagToSearchFor);
    }

}
