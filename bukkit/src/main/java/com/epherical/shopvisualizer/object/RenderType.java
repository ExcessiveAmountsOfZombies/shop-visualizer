package com.epherical.shopvisualizer.object;

import com.epherical.shopvisualizer.ShopVisualizerPlugin;
import com.epherical.shopvisualizer.pdc.ThreeFloatTagType;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public enum RenderType {


    ITEM(new ThreeFloats(-0.5f, 1.15f, 0.5f), new ThreeFloats(0.6f, 0.6f, 0.6f), new ThreeFloats(0f, -90f, 0f), true),
    BLOCK(new ThreeFloats(-0.5f, 1.15f, 0.5f), new ThreeFloats(0.6f, 0.6f, 0.6f), new ThreeFloats(0f, 0, 0f), false);



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


    public void createRenderData(TileState state, String itemKey) {
        ThreeFloatTagType tagType = new ThreeFloatTagType();
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
}
