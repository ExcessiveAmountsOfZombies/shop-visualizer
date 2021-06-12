package com.epherical.shopvisualizer;

import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public abstract class RenderCondition {


    private final String compoundSearch;

    public RenderCondition(String compoundTagToSearchFor) {
        this.compoundSearch = compoundTagToSearchFor;
    }


    public CompoundNBT getCompound(CompoundNBT tag) {
        return tag.getCompound(compoundSearch);
    }

    public abstract ItemStack getItem(ShopBlockEntity blockEntity, ItemStack fallbackItem, CompoundNBT tag);



    public interface RenderFactory<T extends RenderCondition> {
        T create(String compoundTagToSearchFor);
    }

}
