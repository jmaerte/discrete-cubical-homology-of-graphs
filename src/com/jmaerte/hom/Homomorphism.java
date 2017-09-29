package com.jmaerte.hom;

import com.jmaerte.graph.Graph;
import com.jmaerte.graph.Vertex;

/**
 * Created by Julian on 28/09/2017.
 */
public class Homomorphism {

    private Graph preimage, image;
    private int[] values;
    private boolean[] hasValue;

    public Homomorphism(Graph preimage, Graph image) throws Exception {
        this(preimage, image, new int[preimage.size()], new boolean[preimage.size()]);
    }

    public Homomorphism(Graph preimage, Graph image, int[] values, boolean[] hasValue) throws Exception {
        if(this.preimage.size() != values.length) throw new Exception("Homomorphism must be defined on its pre-image!");
        this.preimage = preimage;
        this.image = image;
        this.values = values;
        this.hasValue = hasValue;
    }

    protected void setAt(int i, int j) {
        if(this.hasValue[i]) return;
        this.values[i] = j;
    }

    public int evaluate(int i) {
        return hasValue[i] ? values[i] : -1;
    }

    public int[] getPossibilities(int i) {
        Vertex v = preimage.getVertex(i);
        for(int k = 0; k < v.adjacency.occupation(); k++) {
            Vertex w = v.adjacency.list[k];
            if(hasValue[w.id]) {
                // when phi denotes this homomorphism, phi(i) has to be either phi(j) or one of the values in the adjacency list of phi(j)
                
            }else {

            }
        }
    }
}
