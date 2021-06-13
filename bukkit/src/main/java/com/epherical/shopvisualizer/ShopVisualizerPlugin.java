package com.epherical.shopvisualizer;

//import co.aikar.commands.BukkitCommandManager;
//import com.epherical.shopvisualizer.command.VisualizeCommand;
import com.epherical.shopvisualizer.listener.ShopListeners;
import com.epherical.shopvisualizer.nms.NMSHandler;
import com.epherical.shopvisualizer.nms.NMS_116;
import com.epherical.shopvisualizer.nms.NMS_117;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public final class ShopVisualizerPlugin extends JavaPlugin {

    public static final String NAMESPACE = "shop-visualizer";

    private static final String serverPackage = Bukkit.getServer().getClass().getPackage().getName();
    private static final String version = serverPackage.substring(serverPackage.lastIndexOf(".") + 1);
    private static NMSHandler handler;
    //private BukkitCommandManager commandManager;

    @Override
    public void onEnable() {
        //this.commandManager = new BukkitCommandManager(this);
        //this.commandManager.registerCommand(new VisualizeCommand());

        getServer().getPluginManager().registerEvents(new ShopListeners(), this);
        if (version.equals("v1_16_R3")) {
            handler = new NMS_116();
        } else {
            handler = new NMS_117();
        }
    }

    @Override
    public void onDisable() {

    }


    public static NamespacedKey createKey(String key) {
        return new NamespacedKey(NAMESPACE, key);
    }

    public static byte[] serializeItemStack(ItemStack itemStack) {
        return handler.serializeItemStack(itemStack);
    }
}
