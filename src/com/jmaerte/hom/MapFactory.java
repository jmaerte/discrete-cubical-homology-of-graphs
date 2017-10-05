package com.jmaerte.hom;

import com.jmaerte.graph.Vertex;
import com.jmaerte.graph.cube.Cube;
import com.jmaerte.graph.Graph;
import com.jmaerte.util.IndexList;
import com.jmaerte.util.Utils;

import java.lang.reflect.Array;
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
     * @param i phi(0)
     */
    public Homomorphism[] generate(int i) throws Exception {
        System.out.print("Generating all Homomorphisms phi, that satisfy phi(0) = " + i + ":\tCube-Vertex 1/" + preimage.size() + "\r");
        Homomorphism initial = new Homomorphism(new Homomorphism(preimage, image), i, 0);
        ArrayList<Homomorphism> homomorphisms = new ArrayList<>();
        homomorphisms.add(initial);
        for(int k = 1; k < preimage.size(); k++) {
            System.out.print("Generating all Homomorphisms phi, that satisfy phi(0) = " + i + ":\tCube-Vertex " + (k + 1) + "/" + preimage.size() + "\r");
            ArrayList<Homomorphism> next = new ArrayList<>();
            for(Homomorphism hom : homomorphisms) {
                int[] poss = new int[0];
                try {
                    poss = possibilities(hom, k);
                }catch(Exception e) {}
                for(int l = 0; l < poss.length; l++) {
                    Homomorphism homomorphism = new Homomorphism(hom, poss[l], k);
                    if(k + 1 == preimage.size()) {
                        if(!homomorphism.isZero()) {
                            homomorphism.finish();
                            next.add(homomorphism);
                        }
                    }else next.add(homomorphism);
                }
            }
            homomorphisms = next;
        }
        Homomorphism[] arr = Homomorphism[].class.cast(Array.newInstance(Homomorphism.class, homomorphisms.size()));
        for(int k = 0; k < homomorphisms.size(); k++) arr[k] = homomorphisms.get(k);
        Arrays.sort(arr);
        System.gc();
        return arr;
    }

    public IndexList<Homomorphism> generate() {
        IndexList<Homomorphism> results = new IndexList<>(Homomorphism[].class);
        Homomorphism[][] lists = new Homomorphism[image.size()][];
        int occ = 0;
        for(int i = 0; i < image.size(); i++) {
            try {
                lists[i] = generate(i);
                occ += lists[i].length;
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

        Homomorphism[] arr = Homomorphism[].class.cast(Array.newInstance(Homomorphism.class, occ));
        occ = 0;
        for(int i = 0; i < lists.length; i++) {
            System.arraycopy(lists[i], 0, arr, occ, lists[i].length);
            occ += lists[i].length;
        }
        results.list = arr;
        results.occupation = occ;
        return results;
    }

    /**
     *
     * @param hom
     * @param i >= 1
     * @return
     * @throws Exception
     */
    public static int[] possibilities(Homomorphism hom, int i) throws Exception {
        if(hom.hasValue != null && hom.hasValue[i] || hom.hasValue == null && i <= hom.layer) throw new Exception("Value set already.");
        Vertex v = hom.preimage.getVertex(i);
        int count = 0;
        for(int j = 0; j < v.adjacency.occupation(); j++) {
            if(hom.hasValue(v.adjacency.list[j].id)) count++;
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
            if(hom.hasValue(w.id)) {
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
