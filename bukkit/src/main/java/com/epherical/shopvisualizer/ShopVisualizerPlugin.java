package com.epherical.shopvisualizer;

//import co.aikar.commands.BukkitCommandManager;
//import com.epherical.shopvisualizer.command.VisualizeCommand;
import com.epherical.shopvisualizer.listener.ShopListeners;
import net.minecraft.server.v1_16_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public final class ShopVisualizerPlugin extends JavaPlugin {

    public static final String NAMESPACE = "shop-visualizer";

    //private BukkitCommandManager commandManager;

    @Override
    public void onEnable() {
        //this.commandManager = new BukkitCommandManager(this);
        //this.commandManager.registerCommand(new VisualizeCommand());

        getServer().getPluginManager().registerEvents(new ShopListeners(), this);
    }

    @Override
    public void onDisable() {

    }


    public static NamespacedKey createKey(String key) {
        return new NamespacedKey(NAMESPACE, key);
    }

    public static byte[] serializeItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        CraftItemStack stack = (CraftItemStack) itemStack;
        try {
            Field field = stack.getClass().getDeclaredField("handle");
            field.setAccessible(true);
            net.minecraft.server.v1_16_R3.ItemStack handle = (net.minecraft.server.v1_16_R3.ItemStack) field.get(stack);
            NBTTagCompound compound = handle.save(new NBTTagCompound());
            compound.setInt("DataVersion", Bukkit.getUnsafe().getDataVersion());
            NBTCompressedStreamTools.a(compound, stream);
        } catch (IllegalAccessException | NoSuchFieldException | IOException e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }
}
