package com.epherical.shopvisualizer.object;

public class ThreeFloats {

    private float x;
    private float y;
    private float z;

    public ThreeFloats(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public ThreeFloats add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    protected ThreeFloats clone() {
        return new ThreeFloats(x, y, z);
    }
}
