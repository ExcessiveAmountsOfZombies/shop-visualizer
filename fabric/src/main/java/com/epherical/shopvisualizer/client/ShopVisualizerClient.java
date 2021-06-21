package com.epherical.shopvisualizer.client;

import com.epherical.shopvisualizer.BukkitRenderer;
import com.epherical.shopvisualizer.GunpowderRenderer;
import com.epherical.shopvisualizer.GunpowderSignShop117Renderer;
import com.epherical.shopvisualizer.RenderCondition;
import com.epherical.shopvisualizer.interfaces.ShopBlockEntity;
import com.mojang.serialization.Dynamic;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.SharedConstants;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.datafixer.Schemas;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ShopVisualizerClient implements ClientModInitializer {

    private static BlockPos hoveredShop = BlockPos.ORIGIN;

    public static Map<String, RenderCondition> renderers = new HashMap<>();

    public static int tick = 0;

    public static <T extends RenderCondition> void registerRenderer(String nbtKey, RenderCondition.RenderFactory<T> condition) {
        renderers.put(nbtKey, condition.create(nbtKey));
    }

    @Override
    public void onInitializeClient() {
        registerRenderer("PublicBukkitValues", BukkitRenderer::new);
        registerRenderer("link", GunpowderRenderer::new);
        registerRenderer("gprops", GunpowderSignShop117Renderer::new);

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            tick++;
        });

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

    public static ItemStack getItemStackFromBukkitContainer(byte[] tag, ItemStack fallbackItem) {
        try {
            if (tag == null) {
                return fallbackItem;
            }
            NbtCompound compound = NbtIo.readCompressed(new ByteArrayInputStream(tag));
            int dataVersion = compound.getInt("DataVersion");
            Dynamic<NbtElement> dynamicTag = Schemas.getFixer().update(TypeReferences.ITEM_STACK, new Dynamic<>(NbtOps.INSTANCE, compound), dataVersion, SharedConstants.getGameVersion().getWorldVersion());
            return ItemStack.fromNbt((NbtCompound) dynamicTag.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fallbackItem;
    }

    public static BlockPos getHoveredShop() {
        return hoveredShop;
    }
}
