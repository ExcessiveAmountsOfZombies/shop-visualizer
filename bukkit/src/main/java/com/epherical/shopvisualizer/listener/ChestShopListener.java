package com.epherical.shopvisualizer.listener;

import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.epherical.shopvisualizer.object.RenderType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ChestShopListener implements Listener {


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

            int xDiff = signLocation.getBlockX() - chestLocation.getBlockX();
            int yDiff = signLocation.getBlockY() - chestLocation.getBlockY();
            int zDiff = signLocation.getBlockZ() - chestLocation.getBlockZ();
            // We already have the offsets determined in RenderType.RenderDirection, we just need to know if
            // the offsets need to be applied. They don't need to be applied when the sign is above or directly below the chest.
            boolean hasZMod = xDiff != zDiff;

            RenderType.ITEM.createRenderData(sign, hasZMod, -Math.abs(yDiff), item.getType().getKey().toString());
        }



    }
}
