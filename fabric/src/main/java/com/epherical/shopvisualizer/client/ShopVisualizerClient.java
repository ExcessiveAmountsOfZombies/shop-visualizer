package com.epherical.shopvisualizer.client;

import com.mojang.serialization.Dynamic;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.datafixer.Schemas;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.World;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public class ShopVisualizerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
    }

    public static ItemStack getItemStackFromBukkitContainer(byte[] tag, World world, ItemStack fallbackItem) {
        try {
            CompoundTag compound = NbtIo.readCompressed(new ByteArrayInputStream(tag));
            int dataVersion = compound.getInt("DataVersion");
            Dynamic<Tag> dynamicTag = Schemas.getFixer().update(TypeReferences.ITEM_STACK, new Dynamic<>(NbtOps.INSTANCE, compound), dataVersion, SharedConstants.getGameVersion().getWorldVersion());
            return ItemStack.fromTag((CompoundTag) dynamicTag.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fallbackItem;
    }
}
