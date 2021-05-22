package com.epherical.shopvisualizer.mixin;

import com.epherical.shopvisualizer.interfaces.BukkitBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderMixin {

    private static ItemStack itemStack = new ItemStack(Items.STONE);

    @Inject(method = "render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V",
            at = @At("RETURN"))
    private static <T extends BlockEntity> void render(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta,
                                                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        World world = blockEntity.getWorld();
        if (world != null) {
            BlockState state = world.getBlockState(blockEntity.getPos());
            int light = WorldRenderer.getLightmapCoordinates(world, blockEntity.getPos());
            if (state != null && blockEntity instanceof BukkitBlockEntity) {
                BukkitBlockEntity bukkitBlock = (BukkitBlockEntity) blockEntity;
                CompoundTag tag = bukkitBlock.getBukkitValues();
                if (tag != null) {
                    matrices.push();
                    if (tag.contains("shop-visualizer:trnl")) {
                        setTranslation(matrices, tag.getCompound("shop-visualizer:trnl"));
                    }

                    if (tag.contains("shop-visualizer:rot")) {
                        setRotation(matrices, tag.getCompound("shop-visualizer:rot"));
                    }

                    if (tag.contains("shop-visualizer:scl")) {
                        setScale(matrices, tag.getCompound("shop-visualizer:scl"));
                    }

                    if (tag.contains("shop-visualizer:itm")) {
                        setItem(tag);
                    }

                    MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
                    matrices.pop();
                }
            }
        }
    }

    private static void setTranslation(MatrixStack matrices, CompoundTag tag) {
        matrices.translate(tag.getFloat("shop-visualizer:x"), tag.getFloat("shop-visualizer:y"), tag.getFloat("shop-visualizer:z"));
    }

    private static void setRotation(MatrixStack matrices, CompoundTag tag) {
        rotate(matrices, Vector3f.POSITIVE_X, tag.getFloat("shop-visualizer:x"));
        rotate(matrices, Vector3f.POSITIVE_Y, tag.getFloat("shop-visualizer:y"));
        rotate(matrices, Vector3f.POSITIVE_Z, tag.getFloat("shop-visualizer:z"));
    }

    private static void rotate(MatrixStack matrices, Vector3f vector, float angle) {
        matrices.multiply(vector.getDegreesQuaternion(angle));
    }

    private static void setScale(MatrixStack matrices, CompoundTag tag) {
        matrices.scale(tag.getFloat("shop-visualizer:x"), tag.getFloat("shop-visualizer:y"), tag.getFloat("shop-visualizer:z"));
    }

    private static void setItem(CompoundTag tag) {
        itemStack = new ItemStack(Registry.ITEM.get(new Identifier(tag.getString("shop-visualizer:itm"))));
    }
}
