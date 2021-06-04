package com.epherical.shopvisualizer.listener;

import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.epherical.shopvisualizer.ShopVisualizerPlugin;
import com.epherical.shopvisualizer.object.RenderType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.maxgamer.quickshop.event.ShopCreateEvent;

public class ShopListeners implements Listener {


    @EventHandler
    public void onShopCreate(ShopCreatedEvent event) {
        Container chest = event.getContainer();

        if (chest == null || chest.getInventory().isEmpty()) {
            return;
        }

        int firstItem = chest.getInventory().firstEmpty() - 1;
        firstItem = Math.max(firstItem, 0);
        ItemStack item = chest.getInventory().getItem(firstItem);
        if (item != null) {
            Block container = event.getContainer().getBlock();
            Sign sign = event.getSign();

            Location chestLocation = container.getLocation();
            Location signLocation = sign.getLocation();

            createShop(item, sign, signLocation, chestLocation);
        }
    }


    @EventHandler
    public void onQuickShopCreate(ShopCreateEvent event) {
        Bukkit.getScheduler().runTaskLater(ShopVisualizerPlugin.getProvidingPlugin(ShopVisualizerPlugin.class), () -> {
            ItemStack item = event.getShop().getItem();
            Sign sign = event.getShop().getSigns().get(0);
            Location signLocation = sign.getLocation();
            Location chestLocation = event.getShop().getLocation();

            createShop(item, sign, signLocation, chestLocation);

        }, 5L);
    }

    private void createShop(ItemStack item, Sign sign, Location signLocation, Location chestLocation) {
        int xDiff = signLocation.getBlockX() - chestLocation.getBlockX();
        int yDiff = signLocation.getBlockY() - chestLocation.getBlockY();
        int zDiff = signLocation.getBlockZ() - chestLocation.getBlockZ();

        boolean hasZMod = xDiff != zDiff;

        RenderType.ITEM.createRenderData(sign, chestLocation, hasZMod, -Math.abs(yDiff), item);
    }
}
