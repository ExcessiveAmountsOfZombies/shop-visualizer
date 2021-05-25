package com.epherical.shopvisualizer.mixin;

import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.Camera;
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
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderMixin {

    @Shadow public Camera camera;
    private static ItemStack itemStack = new ItemStack(Items.STONE);

    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V",
            at = @At("RETURN"))
    private <E extends BlockEntity> void render(E blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        World world = blockEntity.getWorld();
        if (world != null) {
            BlockState state = world.getBlockState(blockEntity.getPos());
            int light = WorldRenderer.getLightmapCoordinates(world, blockEntity.getPos());
            if (state != null && blockEntity instanceof ShopBlockEntity) {
                ShopBlockEntity bukkitBlock = (ShopBlockEntity) blockEntity;
                ItemStack item = bukkitBlock.shop$getItemStack();
                CompoundTag tag = bukkitBlock.shop$getShopTag();
                if (tag != null) {
                    matrices.push();
                    float direction = 0;
                    Direction dir = null;
                    if (state.contains(Properties.HORIZONTAL_FACING)) {
                        dir = blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING);
                        direction = dir.asRotation();
                        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction));
                    }

                    if (tag.contains("shop-visualizer:trnl")) {
                        setTranslation(matrices, tag.getCompound("shop-visualizer:trnl"));
                    }

                    if (tag.contains("shop-visualizer:rot")) {
                        setRotation(matrices, tag.getCompound("shop-visualizer:rot"), direction);
                    }

                    if (tag.contains("shop-visualizer:scl")) {
                        setScale(matrices, tag.getCompound("shop-visualizer:scl"));
                    }

                    // gunpowder compat, fabric mod //todo: i want to improve this if i can
                    if (tag.contains("owner")) {
                        directionTranslate(matrices, dir, 1);
                        matrices.scale(0.5f, 0.5f, 0.5f);
                    }

                    if (item == null) {
                        // if it's null at this point it's probably a fabric based shop. todo: improve this...
                        item = ItemStack.fromTag((CompoundTag) tag.get("item"));
                        bukkitBlock.shop$setItemStack(item);
                    }

                    List<Text> text = item.getTooltip(null, TooltipContext.Default.ADVANCED);
                    int decrement = -10;
                    int cur = -(text.size() * 10);
                    for (Text text1 : text) {
                        matrices.push();
                        matrices.scale(-0.02F, -0.02F, 0.025F);
                        directionTranslate(matrices, dir, -15);
                        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw()));
                        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(direction));
                        MinecraftClient.getInstance().textRenderer.draw(text1, -35, (cur -= decrement) - 25, 0xFFFFFFFF, false, matrices.peek().getModel(), vertexConsumers, false, 0, light);
                        matrices.pop();
                    }

                    MinecraftClient.getInstance().getItemRenderer().renderItem(item, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
                    matrices.pop();
                }
            }
        }
    }

    private static void setTranslation(MatrixStack matrices, CompoundTag tag) {
        matrices.translate(tag.getFloat("shop-visualizer:x"), tag.getFloat("shop-visualizer:y"), tag.getFloat("shop-visualizer:z"));
    }

    private static void setRotation(MatrixStack matrices, CompoundTag tag, float rot) {
        rotate(matrices, Vector3f.POSITIVE_X, tag.getFloat("shop-visualizer:x"));
        rotate(matrices, Vector3f.POSITIVE_Y, tag.getFloat("shop-visualizer:y") + rot);
        rotate(matrices, Vector3f.POSITIVE_Z, tag.getFloat("shop-visualizer:z"));
    }

    private static void rotate(MatrixStack matrices, Vector3f vector, float angle) {
        matrices.multiply(vector.getDegreesQuaternion(angle));
    }

    private static void setScale(MatrixStack matrices, CompoundTag tag) {
        matrices.scale(tag.getFloat("shop-visualizer:x"), tag.getFloat("shop-visualizer:y"), tag.getFloat("shop-visualizer:z"));
    }

    private static void directionTranslate(MatrixStack matrices, Direction dir, int multiplier) {
        if (dir != null) {
            switch (dir) {
                case NORTH:
                    matrices.translate(-0.5f * multiplier, 1.15f, -1.5f * multiplier);
                    break;
                case SOUTH:
                    matrices.translate(0.5f * multiplier, 1.15f, -0.5f * multiplier);
                    break;
                case EAST:
                    matrices.translate(0.5f * multiplier, 1.15f, 0.5f * multiplier);
                    break;
                case WEST:
                    matrices.translate(-0.5f * multiplier, 1.15f, 1.5f * multiplier);
                    break;
            }
        } else {
            matrices.translate(0.5f, 1.25f, 0.5f);
        }
    }
}
