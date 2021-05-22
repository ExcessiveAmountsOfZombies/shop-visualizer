package com.epherical.shopvisualizer;

import co.aikar.commands.BukkitCommandManager;
import com.epherical.shopvisualizer.command.VisualizeCommand;
import com.epherical.shopvisualizer.listener.ChestShopListener;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShopVisualizerPlugin extends JavaPlugin {

    public static final String NAMESPACE = "shop-visualizer";

    private BukkitCommandManager commandManager;

    @Override
    public void onEnable() {

        this.commandManager = new BukkitCommandManager(this);
        this.commandManager.registerCommand(new VisualizeCommand());

        getServer().getPluginManager().registerEvents(new ChestShopListener(), this);
    }

    @Override
    public void onDisable() {

    }


    public static NamespacedKey createKey(String key) {
        return new NamespacedKey(NAMESPACE, key);
    }
}
