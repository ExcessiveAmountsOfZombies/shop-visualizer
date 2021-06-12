package com.epherical.shopvisualizer.mixin;

import com.epherical.shopvisualizer.RenderCondition;
import com.epherical.shopvisualizer.ShopVisualizer;
import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.SignTileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignTileEntity.class)
public abstract class SignBlockEntityMixin implements ShopBlockEntity {

    private CompoundNBT shop$shopTag;
    private RenderCondition shop$condition;
    private ItemStack shop$itemStack;

    @Inject(method = "read", at = {@At("HEAD")})
    public void fromTag(BlockState state, CompoundNBT tag, CallbackInfo ci) {
        for (String key : tag.keySet()) {
            if (ShopVisualizer.renderers.containsKey(key) && tag.getCompound(key) != null) {
                shop$shopTag = tag.getCompound(key);
                shop$condition = ShopVisualizer.renderers.get(key);
            }
        }
        /*if (tag.contains("PublicBukkitValues")) {
            shop$shopTag = tag.getCompound("PublicBukkitValues");
            // Item it set instead in the rendermixin, hopefully all the block entities are loaded in by then
            //setItemFromTag(shop$shopTag, "shop-visualizer:itm");
        } else if (tag.contains("link")) {
            shop$shopTag = tag.getCompound("link");
        }*/
    }

    @Override
    public CompoundNBT shop$getShopTag() {
        return shop$shopTag;
    }

    @Override
    public RenderCondition shop$getRenderCondition() {
        return shop$condition;
    }

    public void shop$setItemStack(ItemStack itemStack) {
        this.shop$itemStack = itemStack;
    }

    @Override
    public ItemStack shop$getItemStack() {
        return shop$itemStack;
    }

}
