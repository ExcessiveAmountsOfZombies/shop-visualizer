package com.epherical.shopvisualizer.client;

import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import com.mojang.serialization.Dynamic;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.SharedConstants;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.datafixer.Schemas;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public class ShopVisualizerClient implements ClientModInitializer {

    private static BlockPos hoveredShop = BlockPos.ORIGIN;

    @Override
    public void onInitializeClient() {
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, hitResult) -> {
            if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) {
                hoveredShop = BlockPos.ORIGIN;
            }
            return true;
        });

        WorldRenderEvents.BLOCK_OUTLINE.register((worldRenderContext, blockOutlineContext) -> {
            BlockEntity entity = worldRenderContext.world().getBlockEntity(blockOutlineContext.blockPos());
            if (entity instanceof ShopBlockEntity) {
                hoveredShop = blockOutlineContext.blockPos();
            } else {
                hoveredShop = BlockPos.ORIGIN;
            }
            return true;
        });
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

    public static BlockPos getHoveredShop() {
        return hoveredShop;
    }
}
