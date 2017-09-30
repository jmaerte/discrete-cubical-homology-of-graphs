package com.jmaerte.main;

import com.jmaerte.hom.Homomorphism;
import com.jmaerte.hom.MapFactory;
import com.jmaerte.homology.Homology;
import com.jmaerte.graph.cube.Cube;
import com.jmaerte.graph.Graph;

/**
 * Created by Julian on 28/09/2017.
 */
public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();
        // Outer Points
        graph.addVertex("a_out");
        graph.addVertex("b_out");
        graph.addVertex("c_out");
        graph.addVertex("d_out");
        graph.addVertex("e_out");

        // Outer edges
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        graph.addEdge(4,0);

        System.out.println(graph);

        long ms = System.currentTimeMillis();
        Cube cube = new Cube(3);
        long diff = System.currentTimeMillis() - ms;

        System.out.println(cube);
        System.out.println(diff);

        int[] val = new int[8];
        boolean[] hasVal = new boolean[8];
        val[0] = 4;
        hasVal[0] = true;
        val[1] = 4;
        hasVal[1] = true;
        val[2] = 0;
        hasVal[2] = true;
        val[3] = 4;
        hasVal[3] = true;
        val[4] = 3;
        hasVal[4] = true;
        val[6] = 4;
        hasVal[6] = true;
        val[7] = 0;
        hasVal[7] = true;

        int[] val2 = new int[4];
        boolean[] hasVal2 = new boolean[4];
        val2[0] = 4;
        hasVal2[0] = true;
        val2[1] = 0;
        hasVal2[1] = true;
        val2[2] = 3;
        hasVal2[2] = true;
        val2[3] = 4;
        hasVal2[3] = true;
//        try{
//            Homomorphism hom = new Homomorphism(cube, graph, val, hasVal);
//            long time = System.currentTimeMillis();
//            MapFactory.possibilities(hom, 5);
//            System.out.println(System.currentTimeMillis() - time);
//            Homomorphism hom2 = new Homomorphism(new Cube(2), graph, val2, hasVal2);
//            System.out.println(hom.compareToRestricted('-', 0, hom2));
//
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
        Homology homology = new Homology(graph);
        homology.homology(0, 3);
    }

}
