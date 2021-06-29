package com.epherical.shopvisualizer.mixin;

import com.epherical.shopvisualizer.client.ShopVisualizerClient;
import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.epherical.shopvisualizer.client.ShopVisualizerClient.tick;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderMixin {

    @Shadow public Camera camera;
    private static ItemStack itemStack;

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
                NbtCompound tag = bukkitBlock.shop$getShopTag();
                if (tag != null && !tag.isEmpty()) {
                    matrices.push();
                    float direction = 0;
                    Direction dir = null;
                    if (state.contains(Properties.HORIZONTAL_FACING)) {
                        dir = blockEntity.getCachedState().get(Properties.HORIZONTAL_FACING);
                        direction = dir.asRotation();
                        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(direction));
                    }

                    directionTranslate(matrices, dir, 1, 1);
                    matrices.scale(0.45f, 0.45f, 0.45f);


                    if (item == null || (tick % 60 == 0 && item.isItemEqualIgnoreDamage(itemStack))) {
                        item = bukkitBlock.shop$getRenderCondition().getItem(bukkitBlock, itemStack, tag);
                        bukkitBlock.shop$setItemStack(item);
                        tick = 0;
                    }

                    if (ShopVisualizerClient.getHoveredShop().equals(blockEntity.getPos())) {
                        List<Text> text = item.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL);
                        int decrement = -10;
                        int cur = -(text.size() * 10);
                        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
                        float h = -40;
                        if (text.size() == 1) {
                            h = (float)(-renderer.getWidth(text.get(0)) / 2);
                        }

                        for (Text text1 : text) {
                            matrices.push();
                            matrices.scale(-0.02F, -0.02F, 0.025F);
                            directionTranslate(matrices, dir, 30, -15);

                            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw()));
                            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(direction));
                            renderer.draw(text1, h, (cur -= decrement) - 30, 0xFFFFFFFF, false, matrices.peek().getModel(), vertexConsumers, false, 0, light);
                            matrices.pop();
                        }
                    }
                    MinecraftClient.getInstance().getItemRenderer().renderItem(item, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 1);
                    matrices.pop();
                }
            }
        }
    }

    /*private static void setTranslation(MatrixStack matrices, CompoundTag tag) {
        matrices.translate(tag.getFloat("shop-visualizer:x"), tag.getFloat("shop-visualizer:y"), tag.getFloat("shop-visualizer:z"));
    }

    private static void setRotation(MatrixStack matrices, CompoundTag tag, float rot) {
        rotate(matrices, Vector3f.POSITIVE_X, tag.getFloat("shop-visualizer:x"));
        rotate(matrices, Vector3f.POSITIVE_Y, tag.getFloat("shop-visualizer:y") + rot);
        rotate(matrices, Vector3f.POSITIVE_Z, tag.getFloat("shop-visualizer:z"));
    }*/

    private static void rotate(MatrixStack matrices, Vec3f vector, float angle) {
        matrices.multiply(vector.getDegreesQuaternion(angle));
    }

    /*private static void setScale(MatrixStack matrices, CompoundTag tag) {
        matrices.scale(tag.getFloat("shop-visualizer:x"), tag.getFloat("shop-visualizer:y"), tag.getFloat("shop-visualizer:z"));
    }*/

    private static void directionTranslate(MatrixStack matrices, Direction dir, int multiplierOne, int multiplierTwo) {
        if (dir != null) {
            switch (dir) {
                case NORTH:
                    // -0.5f, -0.5f sub 1 to z
                    matrices.translate(-0.5f, 1.15f, -1.5f * multiplierOne);
                    rotate(matrices, Vec3f.POSITIVE_Y, 180);
                    break;
                case SOUTH:
                    // -0.5f, 0.5f sub 1 to z
                    matrices.translate(0.5f, 1.15f, -0.5f * multiplierOne);
                    rotate(matrices, Vec3f.POSITIVE_Y, 180);
                    break;
                case EAST:
                    // -0.5f, -0.5f add 1 to z
                    matrices.translate(0.5f, 1.15f, 0.5f * multiplierTwo);
                    rotate(matrices, Vec3f.POSITIVE_Y, 0);
                    break;
                case WEST:
                    // -0.5f, 0.5f add 1 to z
                    matrices.translate(-0.5f, 1.15f, 1.5f * multiplierTwo);
                    rotate(matrices, Vec3f.POSITIVE_Y, 0);
                    break;
            }
        } else {
            matrices.translate(0.5f, 1.25f, 0.5f);
        }
    }

    static {
        itemStack = new ItemStack(Items.BARRIER);
        itemStack.setCustomName(Text.of("Shop is currently empty."));
    }
}
