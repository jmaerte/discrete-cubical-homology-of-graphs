package com.jmaerte.hom;

import com.jmaerte.graph.Vertex;
import com.jmaerte.graph.cube.Cube;
import com.jmaerte.graph.Graph;
import com.jmaerte.util.IndexList;
import com.jmaerte.util.Utils;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Julian on 28/09/2017.
 */
public class MapFactory {

    private Cube preimage;
    private Graph image;

    public MapFactory(int n, Graph graph) {
        preimage = new Cube(n);
        image = graph;
    }

    /** Generates all homomorphisms phi: cube -> graph that satisfy phi(0) = i
     *
     * @param list list to fill
     * @param i phi(0)
     */
    private void generate(IndexList<Homomorphism> list, int i) throws Exception {
        Homomorphism initial = new Homomorphism(new Homomorphism(preimage, image), i, 0);

    }

    public static int[] possibilities(Homomorphism hom, int i) throws Exception {
        if(hom.hasValue[i]) throw new Exception("Value set already.");
        Vertex v = hom.preimage.getVertex(i);
        int count = 0;
        for(int j = 0; j < v.adjacency.occupation(); j++) {
            if(hom.hasValue[v.adjacency.list[j].id]) count++;
        }
        if(count == 0) {
            // return all vertex indices.
        }
        int[][] poss = new int[count][];
        count = 0;
        for(int j = 0; j < v.adjacency.occupation(); j++) {
            Vertex w = v.adjacency.list[j];
            if(hom.hasValue[w.id]) {
                Vertex imw = hom.image.getVertex(hom.values[w.id]);// imw = image w under hom
                // now we got the possibilities to choose either hom(v) = imw or hom(v) in imw.adjacency
                int[] possibilitiesW = new int[imw.adjacency.occupation() + 1];
                possibilitiesW[0] = hom.values[w.id];
                for(int k = 0; k < imw.adjacency.occupation(); k++) {
                    possibilitiesW[k + 1] = imw.adjacency.list[k].id;
                }
                poss[count] = possibilitiesW;
                count++;
            }
        }
        if(poss.length == 1) {
            if(poss[0].length == 0) throw new Exception("No possibilities.");
            return poss[0];
        }
        Arrays.sort(poss, new SizeSorter());
        for(int j = 0; j < poss.length - 1; j++) {
            if(poss[j].length == 0) throw new Exception("No possibilities.");
            poss[j + 1] = Utils.intersect(poss[j], poss[j + 1]);
        }
        return poss[poss.length - 1];
    }

}

class SizeSorter implements Comparator<int[]> {
    public int compare(int[] a, int[] b) {
        return a.length - b.length;
    }
}
