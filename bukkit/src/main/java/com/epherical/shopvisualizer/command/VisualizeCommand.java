/*
package com.epherical.shopvisualizer.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.epherical.shopvisualizer.ShopVisualizerPlugin;
import com.epherical.shopvisualizer.object.ThreeFloats;
import com.epherical.shopvisualizer.pdc.ThreeFloatTagType;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@CommandAlias("vis")
public class VisualizeCommand extends BaseCommand {

    @CommandPermission("vis.command.create")
    @Description("Set the rotation for the item on the shop")
    @Subcommand("rotation")
    private void setRotation(Player source, float x, float y, float z) {
        Block block = source.getTargetBlockExact(10);
        if (block != null && block.getState() instanceof TileState) {
            TileState state = (TileState) block.getState();
            ThreeFloatTagType tagType = new ThreeFloatTagType();
            PersistentDataContainer container = state.getPersistentDataContainer();
            PersistentDataContainer threeFloats = tagType.toPrimitive(new ThreeFloats(x, y, z), container.getAdapterContext());
            container.set(ShopVisualizerPlugin.createKey("rot"), PersistentDataType.TAG_CONTAINER, threeFloats);
            state.update(true);
        }
    }

    @CommandPermission("vis.command.create")
    @Description("Set the translation on the x, y, or z coordinates.")
    @Subcommand("translate")
    private void setTranslation(Player source, float x, float y, float z) {
        Block block = source.getTargetBlockExact(10);
        if (block != null && block.getState() instanceof TileState) {
            TileState state = (TileState) block.getState();
            ThreeFloatTagType tagType = new ThreeFloatTagType();
            PersistentDataContainer container = state.getPersistentDataContainer();
            PersistentDataContainer threeFloats = tagType.toPrimitive(new ThreeFloats(x, y, z), container.getAdapterContext());
            container.set(ShopVisualizerPlugin.createKey("trnl"), PersistentDataType.TAG_CONTAINER, threeFloats);
            state.update(true);
        }
    }

    @CommandPermission("vis.command.create")
    @Description("Set the item that will float above the shop. use the KEY of the item. namespace:item")
    @Subcommand("item")
    private void setItem(Player source, String key) {
        Block block = source.getTargetBlockExact(10);
        if (block != null && block.getState() instanceof TileState) {
            TileState state = (TileState) block.getState();
            PersistentDataContainer container = state.getPersistentDataContainer();
            container.set(ShopVisualizerPlugin.createKey("itm"), PersistentDataType.STRING, key);
            state.update(true);
        }
    }

    @CommandPermission("vis.command.create")
    @Description("Set the item that will float above the shop. use the KEY of the item. namespace:item")
    @Subcommand("scale")
    private void setScale(Player source, float x, float y, float z) {
        Block block = source.getTargetBlockExact(10);
        if (block != null && block.getState() instanceof TileState) {
            TileState state = (TileState) block.getState();
            ThreeFloatTagType tagType = new ThreeFloatTagType();
            PersistentDataContainer container = state.getPersistentDataContainer();
            PersistentDataContainer threeFloats = tagType.toPrimitive(new ThreeFloats(x, y, z), container.getAdapterContext());
            container.set(ShopVisualizerPlugin.createKey("scl"), PersistentDataType.TAG_CONTAINER, threeFloats);
            state.update(true);
        }
    }

}
*/
