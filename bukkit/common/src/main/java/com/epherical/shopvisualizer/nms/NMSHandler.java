package com.epherical.shopvisualizer.nms;

import org.bukkit.inventory.ItemStack;

public abstract class NMSHandler {

    public abstract byte[] serializeItemStack(ItemStack itemStack);
}
