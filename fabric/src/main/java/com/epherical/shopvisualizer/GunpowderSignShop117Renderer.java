package com.epherical.shopvisualizer;

import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class GunpowderSignShop117Renderer extends RenderCondition {



    public GunpowderSignShop117Renderer(String compoundTagToSearchFor) {
        super(compoundTagToSearchFor);
    }

    @Override
    public ItemStack getItem(ShopBlockEntity blockEntity, ItemStack fallbackItem, NbtCompound tag) {
        ItemStack item = fallbackItem;
        if (tag.get("gp:sign") != null) {
            NbtCompound compound = tag.getCompound("gp:sign");
            try {
                item = ItemStack.fromNbt(compound.getCompound("link").getCompound("item"));
            } catch (NullPointerException ignored) {
                item = fallbackItem;
            }
        }
        return item;
    }
}
