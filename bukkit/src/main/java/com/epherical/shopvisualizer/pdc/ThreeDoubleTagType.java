package com.epherical.shopvisualizer.pdc;

import com.epherical.shopvisualizer.ShopVisualizerPlugin;
import com.epherical.shopvisualizer.object.ThreeDoubles;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ThreeDoubleTagType implements PersistentDataType<PersistentDataContainer, ThreeDoubles> {

    public static final NamespacedKey X = ShopVisualizerPlugin.createKey("x");
    public static final NamespacedKey Y = ShopVisualizerPlugin.createKey("y");
    public static final NamespacedKey Z = ShopVisualizerPlugin.createKey("z");

    @Override
    public Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public Class<ThreeDoubles> getComplexType() {
        return ThreeDoubles.class;
    }

    @Override
    public PersistentDataContainer toPrimitive(ThreeDoubles complex, PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        container.set(X, DOUBLE, complex.getX());
        container.set(Y, DOUBLE, complex.getY());
        container.set(Z, DOUBLE, complex.getZ());
        return container;
    }

    @Override
    public ThreeDoubles fromPrimitive(PersistentDataContainer primitive, PersistentDataAdapterContext context) {
        Double x = primitive.get(X, DOUBLE);
        Double y = primitive.get(Y, DOUBLE);
        Double z = primitive.get(Z, DOUBLE);
        return new ThreeDoubles(x, y, z);
    }
}
