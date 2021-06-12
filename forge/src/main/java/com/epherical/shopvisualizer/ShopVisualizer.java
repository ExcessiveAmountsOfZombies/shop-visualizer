package com.epherical.shopvisualizer;

import com.mojang.serialization.Dynamic;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Mod("shop-visualizer")
public class ShopVisualizer {

    public static Map<String, RenderCondition> renderers = new HashMap<>();


    public static <T extends RenderCondition> void registerRenderer(String nbtKey, RenderCondition.RenderFactory<T> condition) {
        renderers.put(nbtKey, condition.create(nbtKey));
    }

    public ShopVisualizer() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientInit);
    }

    private void onClientInit(final FMLClientSetupEvent event) {
        registerRenderer("PublicBukkitValues", BukkitRenderer::new);
    }

    public static ItemStack getItemStackFromBukkitContainer(byte[] tag, ItemStack fallbackItem) {
        try {
            if (tag == null) {
                return fallbackItem;
            }

            CompoundNBT compound = CompressedStreamTools.readCompressed(new ByteArrayInputStream(tag));
            int dataVersion = compound.getInt("DataVersion");
            Dynamic<INBT> dynamicTag = DataFixesManager.getDataFixer().update(TypeReferences.ITEM_STACK, new Dynamic<>(NBTDynamicOps.INSTANCE, compound), dataVersion, SharedConstants.getVersion().getWorldVersion());
            return ItemStack.read((CompoundNBT) dynamicTag.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fallbackItem;
    }

    public static boolean isHovering(BlockPos tileEntityPos) {
        RayTraceResult result = Minecraft.getInstance().objectMouseOver;
        if (result != null && result.getType() == RayTraceResult.Type.MISS) {
            return false;
        } else {
            return tileEntityPos.equals(((BlockRayTraceResult)result).getPos());
        }
    }
}
