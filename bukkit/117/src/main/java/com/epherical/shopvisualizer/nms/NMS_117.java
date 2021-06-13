package com.epherical.shopvisualizer.nms;

import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class NMS_117 extends NMSHandler {
    @Override
    public byte[] serializeItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        CraftItemStack stack = (CraftItemStack) itemStack;
        try {
            Field field = stack.getClass().getDeclaredField("handle");
            field.setAccessible(true);
            net.minecraft.world.item.ItemStack handle = (net.minecraft.world.item.ItemStack) field.get(stack);
            NBTTagCompound compound = handle.save(new NBTTagCompound());
            compound.setInt("DataVersion", Bukkit.getUnsafe().getDataVersion());
            NBTCompressedStreamTools.a(compound, stream);
        } catch (IllegalAccessException | NoSuchFieldException | IOException e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }
}
