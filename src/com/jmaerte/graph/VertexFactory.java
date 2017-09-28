package com.jmaerte.graph;

import java.util.HashMap;

/**
 * Created by Julian on 28/09/2017.
 */
public class VertexFactory {

    protected static final int MIN_SIZE = 16;

    public HashMap<Integer, String> labels;
    private int id;

    public VertexFactory() {
        id = 0;
        labels = new HashMap<>();
    }

    public Vertex genVertex(String label) {
        return genVertex(label, MIN_SIZE);
    }

    public Vertex genVertex(String label, int initialCapacity) {
        Vertex v = new Vertex(id, initialCapacity, this);
        labels.put(id, label);
        id++;
        return v;
    }
}
