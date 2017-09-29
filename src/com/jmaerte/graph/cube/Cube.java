package com.jmaerte.graph.cube;

import com.jmaerte.graph.Graph;
import com.jmaerte.graph.Vertex;
import com.jmaerte.graph.VertexFactory;
import com.jmaerte.util.IndexList;
import com.jmaerte.util.Shifter;

/**
 * Created by Julian on 28/09/2017.
 *
 * Generates the n-th order cube graph Q_n, meaning:
 * Q_n := (V, E), where V = {0,1}^n, E = {{(x1,...,xn), (y1,...,yn)} in binomial({0,1}^n, 2) | it exists exactly one i such that xi != yi}
 * and binomial(A, k) for a set A describes the subsets of A with cardinality k.
 *
 */
public class Cube extends Graph {

    Cube[] faces; // the i-th cube is the one, we get by setting the i/2-th entry of the vertices to either 0, if i is even oder 1 if i is odd.

    public Cube() {
        faces = new Cube[0];
        factory = new CubeVertexFactory();
        vertices = new IndexList<>(Vertex[].class);
    }

    public Cube(int n) {
        factory = new CubeVertexFactory();
        vertices = new IndexList<Vertex>(Vertex[].class);
        faces = new Cube[2 * n]; // the cube has 2n faces of dimension n-1.
        // generate with all the underlying cubes and don't use the super functions, because we don't want to use the other factory.
        Shifter[] shifters = new Shifter[n + 1];
        boolean[] gotMax = new boolean[n + 1]; // this array says if we got to poll the value of shifters[i], if shifters[i].isMax() == true, and gotMax[i] == false at the same time. We set it to gotMax[i] to true after.
        for(int i = 0; i <= n; i++) {
            shifters[i] = new Shifter(n);
            shifters[i].reset(i);
            gotMax[i] = false;
        }
        IndexList<Vertex>[] faces = new IndexList[2 * n];
        for(int i = 0; i < 2 * n; i++) {
            faces[i] = new IndexList<>(Vertex[].class);
        }
        while(true) {
            boolean ran = false;
            for(int i = 0; i < shifters.length; i++) {
                if(gotMax[i]) continue;
                ran = true;
                if(shifters[i].isMax()) gotMax[i] = true;
                // generate
                Vertex v = factory.genVertex(shifters[i].get());
                // add vertex to its underlying cubes:
                vertices.add(v);
                for(int k = 0; k < n; k++) {
                    if(shifters[i].shift[k]) {
                        // add v to faces[2 * k + 1]
                        faces[2 * k + 1].add(v);
                    }else {
                        // add v to faces[2 * k]
                        faces[2 * k].add(v);
                    }
                }
                if(!shifters[i].isMax()) shifters[i].shift();
            }
            if(!ran) break;
        }
        for(int i = 0; i < 2 * n; i++) {
            this.faces[i] = new Cube();
            this.faces[i].vertices = faces[i];
        }
        for(int i = 0; i < vertices.occupation(); i++) {
            Vertex v = vertices.list[i];
            for(int k = i + 1; k < vertices.occupation(); k++) {
                Vertex w = vertices.list[k];
                int diff = v.id ^ w.id;
                if(diff == (diff & -diff)) link(v, w);
            }
        }
    }

    @Override
    public String toString() {
        String s = "Cube with " + vertices.occupation() + " Vertices, " + edges + " Edges and " + faces.length + " Facets: [\n";
        for(int i = 0; i < vertices.occupation(); i++) {
            s+= "\t" + vertices.list[i] + "\n";
        }
        s += "]\n";
        if(faces.length > 0) {
            s += "Facets: [" + "\n";
            for(int i = 0; i < faces.length; i++) {
                s+= faces[i];
            }
            s += "]\n";
        }
        return s + "]\n";
    }

}
