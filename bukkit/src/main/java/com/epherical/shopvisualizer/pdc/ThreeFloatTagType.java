package com.epherical.shopvisualizer.pdc;

import com.epherical.shopvisualizer.ShopVisualizerPlugin;
import com.epherical.shopvisualizer.object.ThreeFloats;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ThreeFloatTagType implements PersistentDataType<PersistentDataContainer, ThreeFloats> {

    public static final NamespacedKey X = ShopVisualizerPlugin.createKey("x");
    public static final NamespacedKey Y = ShopVisualizerPlugin.createKey("y");
    public static final NamespacedKey Z = ShopVisualizerPlugin.createKey("z");

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<ThreeFloats> getComplexType() {
        return ThreeFloats.class;
    }

    @Override
    public PersistentDataContainer toPrimitive(ThreeFloats complex, PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        container.set(X, FLOAT, complex.getX());
        container.set(Y, FLOAT, complex.getY());
        container.set(Z, FLOAT, complex.getZ());
        return container;
    }

    @Override
    public ThreeFloats fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
        Float x = primitive.get(X, FLOAT);
        Float y = primitive.get(Y, FLOAT);
        Float z = primitive.get(Z, FLOAT);
        return new ThreeFloats(x, y, z);
    }
}
