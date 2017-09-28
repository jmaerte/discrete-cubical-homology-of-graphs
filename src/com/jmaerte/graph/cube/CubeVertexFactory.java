package com.jmaerte.graph.cube;

import com.jmaerte.graph.Vertex;
import com.jmaerte.graph.VertexFactory;

/**
 * Created by Julian on 28/09/2017.
 */
public class CubeVertexFactory extends VertexFactory {

    public Vertex genVertex(String label) {
        return genVertex(label, MIN_SIZE);
    }

    public Vertex genVertex(String label, int initialCapacity) {
        int id = id(label);
        Vertex v = new Vertex(id, initialCapacity, this);
        labels.put(id, label);
        return v;
    }

    private static int id(String label) {
        int sum = 0;
        int addend = 1;
        for(int i = 0; i < label.length(); i++) {
            char c = label.charAt(i);
            if(c == '1') sum += addend;
            addend *= 2;
        }
        return sum;
    }

}
