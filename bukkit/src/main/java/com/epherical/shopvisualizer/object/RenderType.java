package com.epherical.shopvisualizer.object;

import com.epherical.shopvisualizer.ShopVisualizerPlugin;
import com.epherical.shopvisualizer.pdc.ThreeFloatTagType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Directional;
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


    public void createRenderData(TileState state, boolean hasZMod, float yMod, String itemKey) {
        ThreeFloatTagType tagType = new ThreeFloatTagType();
        ThreeFloats translation = this.translation;
        ThreeFloats rotation = this.rotation;
        if (state.getBlockData() instanceof Directional) {
            Directional directional = (Directional) state.getBlockData();
            for (RenderDirection value : RenderDirection.values()) {
                if (value.blockFacePredicate.test(directional.getFacing())) {
                    float z = value.z;
                    if (hasZMod) {
                        z += value.zMod;
                    }
                    translation = translation.clone().add(value.x, yMod, z);
                    rotation = rotation.clone().add(0, value.yRot, 0);
                    break;
                }
            }
        } else {
            translation =  translation.clone().add(0.5f, 0, 0.5f);
        }
        PersistentDataContainer container = state.getPersistentDataContainer();
        PersistentDataContainer threeFloats = tagType.toPrimitive(translation, container.getAdapterContext());
        container.set(ShopVisualizerPlugin.createKey("trnl"), PersistentDataType.TAG_CONTAINER, threeFloats);
        threeFloats = tagType.toPrimitive(scale, container.getAdapterContext());
        container.set(ShopVisualizerPlugin.createKey("scl"), PersistentDataType.TAG_CONTAINER, threeFloats);
        if (applyRotation) {
            threeFloats = tagType.toPrimitive(rotation, container.getAdapterContext());
            container.set(ShopVisualizerPlugin.createKey("rot"), PersistentDataType.TAG_CONTAINER, threeFloats);
        }

        container.set(ShopVisualizerPlugin.createKey("itm"), PersistentDataType.STRING, itemKey.toLowerCase());
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
