package com.epherical.shopvisualizer.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderMixin {

    private static final ItemStack itemStack = new ItemStack(Items.STONE);

    @Inject(method = "render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V",
            at = @At("RETURN"))
    private static <T extends BlockEntity> void render(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta,
                                                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        World world = blockEntity.getWorld();
        if (world != null) {
            BlockState state = world.getBlockState(blockEntity.getPos());
            int light = WorldRenderer.getLightmapCoordinates(world, blockEntity.getPos());
            if (state != null) {
                Direction dir = blockEntity.getCachedState().get(ChestBlock.FACING);
                matrices.push();
                float direction = -dir.asRotation();
                matrices.translate(0.5D, 2.5D, 0.5D);
                //matrices.scale(0.375F, 0.375F, 0.375F);
                matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction));
                MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
                matrices.pop();
            }
        }
    }
}
