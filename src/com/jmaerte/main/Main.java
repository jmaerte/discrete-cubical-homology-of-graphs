package com.jmaerte.main;

import com.jmaerte.hom.Homomorphism;
import com.jmaerte.hom.MapFactory;
import com.jmaerte.homology.Homology;
import com.jmaerte.graph.cube.Cube;
import com.jmaerte.graph.Graph;
import com.jmaerte.util.IndexList;

/**
 * Created by Julian on 28/09/2017.
 */
public class Main {

    public static void main(String[] args) {
        Graph graph = Graph.getGon(5);

//        Graph graph = new Graph();
//        graph.addVertex("1");
//        graph.addVertex("2");
//        graph.addVertex("3");
//        graph.addVertex("4");
//        graph.addVertex("5");
//        graph.addVertex("6");
//        graph.addVertex("7");
//        graph.addVertex("8");
//
//        graph.addEdge(0, 1);
//        graph.addEdge(1, 2);
//        graph.addEdge(2, 3);
//        graph.addEdge(3, 4);
//        graph.addEdge(4, 5);
//        graph.addEdge(5, 1);
//        graph.addEdge(5, 6);
//        graph.addEdge(6, 7);
//        graph.addEdge(7, 0);


//        System.out.println(graph);

        Cube cube = new Cube(3);
        Cube square = new Cube(2);
//        System.out.println(cube);
//        System.out.println(square);

//        int[] val = new int[8];
//        boolean[] hasVal = new boolean[8];
//        val[0] = 4;
//        hasVal[0] = true;
//        val[1] = 4;
//        hasVal[1] = true;
//        val[2] = 0;
//        hasVal[2] = true;
//        val[3] = 4;
//        hasVal[3] = true;
//        val[4] = 3;
//        hasVal[4] = true;
//        val[6] = 4;
//        hasVal[6] = true;
//        val[7] = 0;
//        hasVal[7] = true;
//
//        int[] val2 = new int[4];
//        boolean[] hasVal2 = new boolean[4];
//        val2[0] = 4;
//        hasVal2[0] = true;
//        val2[1] = 0;
//        hasVal2[1] = true;
//        val2[2] = 3;
//        hasVal2[2] = true;
//        val2[3] = 4;
//        hasVal2[3] = true;
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

//        int[] val = new int[8];
//        boolean[] hasVal = new boolean[8];
//        val[0] = 0;
//        hasVal[0] = true;
//        val[1] = 0;
//        hasVal[1] = true;
//        val[2] = 0;
//        hasVal[2] = true;
//        val[3] = 0;
//        hasVal[3] = true;
//        val[4] = 0;
//        hasVal[4] = true;
//        val[5] = 0;
//        hasVal[5] = true;
//        val[6] = 0;
//        hasVal[6] = true;
//        val[7] = 1;
//        hasVal[7] = true;
//        try {
//            IndexList<Homomorphism> lower = (new MapFactory(2, graph)).generate();
//            Homomorphism hom = new Homomorphism(cube, graph, val, hasVal);
//            int sign = 1;
//            for(int j = 0; j < 3; j++) {
//                int m = Homology.binarySearch(lower, hom, j, '-');
//                int p = Homology.binarySearch(lower, hom, j, '+');
//                System.out.println(hom);
//                if(m < lower.occupation() && hom.compareToRestricted('-', j, lower.list[m]) == 0) {
//                    System.out.println(lower.list[m] + " " + sign);
//                }
//                if(p < lower.occupation() && hom.compareToRestricted('+', j, lower.list[p]) == 0) {
//                    System.out.println(lower.list[p] + " " + -sign);
//                }
//                sign *= -1;
//            }
//        }catch(Exception e) {
//            e.printStackTrace();
//        }

//        MapFactory factory = new MapFactory(3, graph);
//        System.out.println(factory.generate());

        long ms = System.currentTimeMillis();
        Homology homology = new Homology(graph);
        try {
            homology.homology(1, 3);
        }catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("Process finished after " + (System.currentTimeMillis() - ms) + "ms");

//        long ms2 = System.currentTimeMillis();
//        MapFactory factory = new MapFactory(1, graph);
//        System.out.println(factory.generate());
//        System.out.println(System.currentTimeMillis() - ms2);

    }

}
