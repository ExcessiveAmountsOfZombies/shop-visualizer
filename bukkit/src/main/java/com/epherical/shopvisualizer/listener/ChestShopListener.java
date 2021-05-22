package com.epherical.shopvisualizer.listener;

import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.epherical.shopvisualizer.object.RenderType;
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
        ItemStack item = chest.getInventory().getItem(firstItem);
        if (item != null) {
            Sign sign = event.getSign();
            RenderType.ITEM.createRenderData(sign, item.getType().getKey().toString());
        }



    }
}
