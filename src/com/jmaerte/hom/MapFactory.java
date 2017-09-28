package com.jmaerte.hom;

import com.jmaerte.graph.cube.Cube;
import com.jmaerte.graph.Graph;

/**
 * Created by Julian on 28/09/2017.
 */
public class MapFactory {

    private Cube preimage;
    private Graph image;

    public MapFactory(int n, Graph graph) {
        preimage = new Cube(n);
        image = graph;
    }

}
