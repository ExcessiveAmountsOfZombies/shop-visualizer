package com.epherical.shopvisualizer.mixin;

import com.epherical.shopvisualizer.interfaces.BukkitBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements BukkitBlockEntity {

    private CompoundTag shop$publicBukkitValues;

    @Inject(method = "fromTag", at = {@At("HEAD")})
    public void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("PublicBukkitValues")) {
            shop$publicBukkitValues = tag.getCompound("PublicBukkitValues");
        }
    }

    @Override
    public CompoundTag getBukkitValues() {
        return shop$publicBukkitValues;
    }
}
