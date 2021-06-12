package com.epherical.shopvisualizer.mixin;

import com.epherical.shopvisualizer.ShopVisualizer;
import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TileEntityRendererDispatcher.class)
public class BlockEntityRenderMixin {

    @Shadow public ActiveRenderInfo renderInfo;
    private static ItemStack itemStack;

    @Inject(method = "renderTileEntity",
            at = @At("RETURN"))
    private <E extends TileEntity> void render(E tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, CallbackInfo ci) {
        World world = tileEntityIn.getWorld();
        if (world != null) {
            BlockState state = world.getBlockState(tileEntityIn.getPos());
            int light = WorldRenderer.getCombinedLight(world, tileEntityIn.getPos());
            if (state != null && tileEntityIn instanceof ShopBlockEntity) {
                ShopBlockEntity bukkitBlock = (ShopBlockEntity) tileEntityIn;
                ItemStack item = bukkitBlock.shop$getItemStack();
                CompoundNBT tag = bukkitBlock.shop$getShopTag();
                if (tag != null) {
                    matrixStackIn.push();
                    float direction = 0;
                    Direction dir = null;
                    if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                        dir = tileEntityIn.getBlockState().get(BlockStateProperties.HORIZONTAL_FACING);
                        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(direction));
                    }

                    directionTranslate(matrixStackIn, dir, 1, 1);
                    matrixStackIn.scale(0.45f, 0.45f, 0.45f);


                    if (item == null) {
                        item = bukkitBlock.shop$getRenderCondition().getItem(bukkitBlock, itemStack, tag);
                        bukkitBlock.shop$setItemStack(item);
                    }

                    if (ShopVisualizer.getHoveredShop().equals(tileEntityIn.getPos())) {
                        List<ITextComponent> text = item.getTooltip(Minecraft.getInstance().player, ITooltipFlag.TooltipFlags.NORMAL);
                        int decrement = -10;
                        int cur = -(text.size() * 10);
                        FontRenderer renderer = Minecraft.getInstance().fontRenderer;
                        float h = -40;
                        if (text.size() == 1) {
                            h = (float)(-renderer.getStringPropertyWidth(text.get(0)) / 2);
                        }

                        for (ITextComponent text1 : text) {
                            matrixStackIn.push();
                            matrixStackIn.scale(-0.02F, -0.02F, 0.025F);
                            directionTranslate(matrixStackIn, dir, 30, -15);

                            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(renderInfo.getYaw()));
                            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(direction));
                            renderer.func_243247_a(text1, h, (cur -= decrement) - 30, 0xFFFFFFFF, false, matrixStackIn.getLast().getMatrix(), bufferIn, false, 0, light);
                            matrixStackIn.pop();
                        }
                    }

                    Minecraft.getInstance().getItemRenderer().renderItem(item, ItemCameraTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
                    matrixStackIn.pop();
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

    private static void rotate(MatrixStack matrices, Vector3f vector, float angle) {
        matrices.rotate(vector.rotationDegrees(angle));
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
                    rotate(matrices, Vector3f.YP, 180);
                    break;
                case SOUTH:
                    // -0.5f, 0.5f sub 1 to z
                    matrices.translate(0.5f, 1.15f, -0.5f * multiplierOne);
                    rotate(matrices, Vector3f.YP, 180);
                    break;
                case EAST:
                    // -0.5f, -0.5f add 1 to z
                    matrices.translate(0.5f, 1.15f, 0.5f * multiplierTwo);
                    rotate(matrices, Vector3f.YP, 0);
                    break;
                case WEST:
                    // -0.5f, 0.5f add 1 to z
                    matrices.translate(-0.5f, 1.15f, 1.5f * multiplierTwo);
                    rotate(matrices, Vector3f.YP, 0);
                    break;
            }
        } else {
            matrices.translate(0.5f, 1.25f, 0.5f);
        }
    }

    static {
        itemStack = new ItemStack(Items.BARRIER);
        itemStack.setDisplayName(ITextComponent.getTextComponentOrEmpty("Shop is currently empty."));
    }
}
