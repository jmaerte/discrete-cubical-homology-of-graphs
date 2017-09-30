package com.jmaerte.hom;

import com.jmaerte.graph.Vertex;
import com.jmaerte.graph.cube.Cube;
import com.jmaerte.graph.Graph;
import com.jmaerte.util.IndexList;
import com.jmaerte.util.Utils;

import java.util.ArrayList;
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
    public void generate(ArrayList<Homomorphism> list, int i) throws Exception {
        Homomorphism initial = new Homomorphism(new Homomorphism(preimage, image), i, 0);
        ArrayList<Homomorphism> homomorphisms = new ArrayList<>();
        homomorphisms.add(initial);
        for(int k = 1; k < preimage.size(); k++) {
            ArrayList<Homomorphism> next = new ArrayList<>();
            for(Homomorphism hom : homomorphisms) {
                int[] poss = possibilities(hom, k);
                for(int l = 0; l < poss.length; l++) {
                    Homomorphism homomorphism = new Homomorphism(hom, poss[l], k);
                    if(k + 1 == preimage.size()) {
                        boolean isZero = false;
                        for(int j = 0; j < preimage.dimension; j++) {
                            isZero |= homomorphism.isZero(j);
                        }
                        if(!isZero) next.add(homomorphism);
                    }else next.add(homomorphism);
                }
            }
            homomorphisms = next;
        }
        // check if any of the homomorphisms is zero.
        for(Homomorphism hom : homomorphisms) {
            hom.finish();
            list.add(hom);
        }
        System.gc();
    }

    public IndexList<Homomorphism> generate() {
        ArrayList<Homomorphism> list = new ArrayList<>();
        for(int i = 0; i < image.size(); i++) {
            try {
                generate(list, i);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
//        Collections.sort(list);
        IndexList<Homomorphism> result = new IndexList<>(Homomorphism[].class, list.size());
        for(Homomorphism hom : list) result.add(hom);
        return result;
    }

    public static int[] possibilities(Homomorphism hom, int i) throws Exception {
        if(hom.hasValue != null && hom.hasValue[i] || hom.hasValue == null && i <= hom.layer) throw new Exception("Value set already.");
        Vertex v = hom.preimage.getVertex(i);
        int count = 0;
        for(int j = 0; j < v.adjacency.occupation(); j++) {
            if(hom.hasValue != null && hom.hasValue[v.adjacency.list[j].id] || hom.hasValue == null && v.adjacency.list[j].id <= hom.layer) count++;
        }
        if(count == 0) {
            // return all vertex indices.
            int[] res = new int[hom.image.size()];
            for(int k = 0; k < res.length; k++) {
                res[k] = hom.image.getVertex(k).id;
            }
            return res;
        }
        int[][] poss = new int[count][];
        count = 0;
        for(int j = 0; j < v.adjacency.occupation(); j++) {
            Vertex w = v.adjacency.list[j];
            if(hom.hasValue != null && hom.hasValue[w.id] || hom.hasValue == null && w.id <= hom.layer) {
                Vertex imw = hom.image.getVertex(hom.get(w.id));// imw = image w under hom
                // now we got the possibilities to choose either hom(v) = imw or hom(v) in imw.adjacency
                int[] possibilitiesW = new int[imw.adjacency.occupation() + 1];
                possibilitiesW[0] = hom.get(w.id);
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
