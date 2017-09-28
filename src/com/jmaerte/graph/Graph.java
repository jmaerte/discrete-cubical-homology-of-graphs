package com.jmaerte.graph;

import com.jmaerte.util.IndexList;

/**
 * Created by Julian on 28/09/2017.
 */
public class Graph {

    private IndexList<Vertex> vertices;
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

    public void addEdge(int i, int j) {
        link(vertices.list[i], vertices.list[j]);
    }

    private void link(Vertex a, Vertex b) {
        if(a.addEdge(b) && b.addEdge(a)) edges++;
    }

    public String toString() {
        String s = "Graph: [\n";
        for(int i = 0; i < vertices.occupation(); i++) {
            s+= "\t" + vertices.list[i] + "\n";
        }
        return s + "]";
    }
}
