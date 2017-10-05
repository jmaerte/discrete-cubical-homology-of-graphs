package com.jmaerte.main;

import com.jmaerte.hom.Homomorphism;
import com.jmaerte.hom.MapFactory;
import com.jmaerte.homology.Homology;
import com.jmaerte.graph.cube.Cube;
import com.jmaerte.graph.Graph;
import com.jmaerte.io.IO;
import com.jmaerte.util.IndexList;
import com.jmaerte.util.Vector3D;

/**
 * Created by Julian on 28/09/2017.
 */
public class Main {

    public static Graph graph = null;

    public static void main(String[] args) {
        graph = IO.fromConsole(graph);
        main(args);
    }

}
