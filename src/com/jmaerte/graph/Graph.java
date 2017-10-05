package com.jmaerte.graph;

import com.jmaerte.util.IndexList;

import java.util.Map;
import java.util.Set;

/**
 * Created by Julian on 28/09/2017.
 */
public class Graph {

    protected IndexList<Vertex> vertices;
    protected int edges = 0;
    public VertexFactory factory;

    public Graph(IndexList<Vertex> vertices) {
        this.vertices = vertices;
        this.factory = new VertexFactory();
    }

    public Graph() {
        this(new IndexList<>(Vertex[].class));
    }

    public Vertex addVertex(String label) {
        boolean exists = false;
        Set<Map.Entry<Integer, String>> entries = factory.labels.entrySet();
        for(Map.Entry<Integer, String> entry : entries) {
            if(entry.getValue().equals(label)) exists = true;
        }
        if(!exists){
            Vertex v = factory.genVertex(label);
            vertices.add(v);
            return v;
        }
        return null;
    }

    public Vertex getVertex(int i) {
        return (i < this.vertices.occupation() && i >= 0) ? this.vertices.list[i] : null;
    }

    public boolean addEdge(int i, int j) {
        return link(vertices.list[i], vertices.list[j]);
    }

    protected boolean link(Vertex a, Vertex b) {
        if(a.addEdge(b) && b.addEdge(a)) {
            edges++;
            return true;
        }
        return false;
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

    public static Graph getGon(int n) {
        IndexList<Vertex> vertices = new IndexList<Vertex>(Vertex[].class);
        VertexFactory factory = new VertexFactory();
        for(int i = 0; i < n; i++) {
            vertices.add(factory.genVertex((i + 1) + ""));
        }
        Graph result = new Graph(vertices);
        result.factory = factory;
        for(int i = 1; i < n; i++) {
            result.addEdge(i - 1, i);
        }
        result.addEdge(0, n - 1);
        return result;
    }
}
