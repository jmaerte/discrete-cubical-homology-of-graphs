package com.jmaerte.graph;

import com.jmaerte.util.IndexList;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Julian on 28/09/2017.
 */
public class Vertex implements Comparable<Vertex> {

    private VertexFactory factory;
    public int id;
    public IndexList<Vertex> adjacency;

    public Vertex(int id, int initial, VertexFactory factory) {
        this.factory = factory;
        this.id = id;
        adjacency = new IndexList<>(Vertex[].class, initial);
    }

    public boolean addEdge(Vertex that) {
        if(that.id == this.id) return false;
        return adjacency.add(that);
    }

    public int compareTo(Vertex that) {
        if(this.factory != that.factory) {
            new Exception("Not comparable!").printStackTrace();
            System.exit(0);
        }
        return this.id - that.id;
    }

    public String toString() {
        String s = factory.labels.get(id) + " | " + id + " -> [";
        for(int i = 0; i < adjacency.occupation(); i++) {
            s+= adjacency.list[i].id + (i == adjacency.occupation() - 1 ? "" : ", ");
        }
        return s + "]";
    }
}
