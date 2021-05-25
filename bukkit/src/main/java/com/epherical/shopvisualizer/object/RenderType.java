package com.epherical.shopvisualizer.object;

import com.epherical.shopvisualizer.ShopVisualizerPlugin;
import com.epherical.shopvisualizer.pdc.ThreeFloatTagType;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.Predicate;

public enum RenderType {


    ITEM(new ThreeFloats(0f, 1.15f, 0f), new ThreeFloats(0.6f, 0.6f, 0.6f), new ThreeFloats(0f, 0, 0f), true),
    BLOCK(new ThreeFloats(0f, 1.15f, 0f), new ThreeFloats(0.6f, 0.6f, 0.6f), new ThreeFloats(0f, 0, 0f), false);

    private final ThreeFloats translation;
    private final ThreeFloats scale;
    private final ThreeFloats rotation;
    private final boolean applyRotation;


    RenderType(ThreeFloats translation, ThreeFloats scale, ThreeFloats rotation, boolean applyRotation) {
        this.translation = translation;
        this.scale = scale;
        this.rotation = rotation;
        this.applyRotation = applyRotation;
    }


    public void createRenderData(TileState state, Location chestLocation, boolean hasZMod, float yMod, ItemStack item) {
        // Going to limit this for now to be all controlled by the client, but in the future might want to open it back up.
        ThreeFloatTagType tagType = new ThreeFloatTagType();
        PersistentDataContainer container = state.getPersistentDataContainer();
        ThreeFloats floats = new ThreeFloats(chestLocation.getBlockX(), chestLocation.getBlockY(), chestLocation.getBlockZ());
        PersistentDataContainer threeFloats = tagType.toPrimitive(floats, container.getAdapterContext());
        container.set(ShopVisualizerPlugin.createKey("xyz"), PersistentDataType.TAG_CONTAINER, threeFloats);
        container.set(ShopVisualizerPlugin.createKey("item"), PersistentDataType.BYTE_ARRAY, ShopVisualizerPlugin.serializeItemStack(item));
        state.update(true);
    }

    // other potential is to make compare coordinates or whatever and ignore the direction completely
    // Items are in NORTH-WEST side of block.
    // If Facing north, to get to the center we need -0.5, -0.5 // sub 1 to Z
    // if facing south, to get to the center we need 0.5, 0.5 // sub 1 to Z
    // If Facing east, to get to the center we need 0.5, -0.5 // add 1 to Z
    // if facing west, to get to the center we need -0.5, 0.5 // add 1 to Z
    enum RenderDirection {
        NORTH(-0.5f, -0.5f, blockFace -> blockFace == BlockFace.NORTH, 0, -1),
        SOUTH(0.5f, 0.5f, blockFace -> blockFace == BlockFace.SOUTH, 180, -1),
        EAST(0.5f, -0.5f, blockFace -> blockFace == BlockFace.EAST, 90, 1),
        WEST(-0.5f, 0.5f, blockFace -> blockFace == BlockFace.WEST, -90, 1);

        final float x;
        final float z;
        final Predicate<BlockFace> blockFacePredicate;
        final float yRot;
        final float zMod;

        RenderDirection(float x, float z, Predicate<BlockFace> direction, float yRot, float zMod) {
            this.x = x;
            this.z = z;
            this.blockFacePredicate = direction;
            this.yRot = yRot;
            this.zMod = zMod;
        }
    }
}
