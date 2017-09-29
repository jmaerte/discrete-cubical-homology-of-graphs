package com.jmaerte.graph;

import com.jmaerte.util.IndexList;

/**
 * Created by Julian on 28/09/2017.
 */
public class Graph {

    protected IndexList<Vertex> vertices;
    protected int edges = 0;
    protected VertexFactory factory;

    public Graph(IndexList<Vertex> vertices) {
        this.vertices = vertices;
        this.factory = new VertexFactory();
    }

    public Graph() {
        this(new IndexList<>(Vertex[].class));
    }

    public void addVertex(String label) {
        vertices.add(factory.genVertex(label));
    }

    public Vertex getVertex(int i) {
        return (i < this.vertices.occupation() && i >= 0) ? this.vertices.list[i] : null;
    }

    public void addEdge(int i, int j) {
        link(vertices.list[i], vertices.list[j]);
    }

    protected void link(Vertex a, Vertex b) {
        if(a.addEdge(b) && b.addEdge(a)) edges++;
    }

    public int size() {
        return vertices.occupation();
    }

    public String toString() {
        String s = "Graph: [\n";
        for(int i = 0; i < vertices.occupation(); i++) {
            s+= "\t" + vertices.list[i] + "\n";
        }
        return s + "]";
    }
}
