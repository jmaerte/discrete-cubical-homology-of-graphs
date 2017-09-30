package com.jmaerte.util;

import java.util.Vector;

public class Vector3D<X,Y,Z> extends Vector2D<X,Y> {

    public Z z;

    public Vector3D(X x, Y y, Z z) {
        super(x,y);
        this.z = z;
    }

}
