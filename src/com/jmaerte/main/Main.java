package com.jmaerte.main;

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
        // Inner Points
        graph.addVertex("a_in");
        graph.addVertex("b_in");
        graph.addVertex("c_in");
        graph.addVertex("d_in");
        graph.addVertex("e_in");

        // Outer edges
        graph.addEdge(0,1);
        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        graph.addEdge(4,0);
        // Inner Edges
        graph.addEdge(5,6);
        graph.addEdge(6,7);
        graph.addEdge(7,8);
        graph.addEdge(8,9);
        graph.addEdge(9,5);
        // Connecting Edges
        graph.addEdge(0, 5);
        graph.addEdge(1, 6);
        graph.addEdge(2, 7);
        graph.addEdge(3, 8);
        graph.addEdge(4, 9);

        System.out.println(graph);

        long ms = System.currentTimeMillis();
        Cube cube = new Cube(3);
        long diff = System.currentTimeMillis() - ms;

//        System.out.println(cube);
        System.out.println(diff);
    }

}
