package com.jmaerte.util;

public class Vector4D<X, Y, Z, W> extends Vector3D<X, Y, Z> {

    public W w;

    public Vector4D(X x, Y y, Z z, W w) {
        super(x,y,z);
        this.w = w;
    }
}
