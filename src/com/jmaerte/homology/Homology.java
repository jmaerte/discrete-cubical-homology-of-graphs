package com.jmaerte.homology;

import com.jmaerte.graph.Graph;
import com.jmaerte.hom.Homomorphism;
import com.jmaerte.hom.MapFactory;
import com.jmaerte.util.*;

import java.util.ArrayList;

public class Homology {

    private Graph graph;

    public Homology(Graph graph) {
        this.graph = graph;
    }

    /**Calculates the homology groups between min and max.
     * [H_min(graph), ... ,H_max(graph)]
     * @param min
     * @param max
     */
    public void homology(int min, int max) throws Exception {
        System.out.println("Calculating H_" + min + "(G),...,H_" + max + "(G), where G is the graph:");
        System.out.println(graph);

        if(0 >= min || min > max) throw new Exception("Invalid Boundaries!");
        IndexList<Homomorphism>[] cache = new IndexList[]{
                generate(min - 1),
                new IndexList(Homomorphism[].class)
        };
        Smith[] smithCache = new Smith[]{
                new Smith(0),
                new Smith(0)
        };

        String homology = "[";
        boolean overflowLastTime = false;

        System.out.println("Found " + cache[0].occupation() + " non-degenerated graph homomorphisms between the " + (min - 1) + "-Cube and the input graph.");

        for(int i = min; i <= max + 1; i++) {
            cache[1] = generate(i);
            System.out.println("Found " + cache[1].occupation() + " non-degenerated graph homomorphisms between the " + i + "-Cube and the input graph.");
            // use cache[0] and cache[1]
            try {
                smithCache[1] = smith(boundary(cache[0], cache[1], i), false);
                if(i > min) {
                    if(overflowLastTime) homology += "---" + (i == max + 1 ? "" : ", ");
                    else homology += Smith.calculateHom(cache[0].occupation(), smithCache) + (i == max + 1 ? "" : ", ");
                }
                System.out.println(Colors.PURPLE + "-- DONE! --" + Colors.RESET);
                overflowLastTime = false;
            }catch(Exception e) {
                e.printStackTrace();
                homology += "---" + (i == max + 1 ? "" : ", ");
                System.out.println("-- " + Colors.RED + "ERROR: " + Colors.RESET + "Overflow Exception. Please use Bignum-Version! --");
                overflowLastTime = true;
            }
            cache[0] = cache[1];
            smithCache[0] = smithCache[1];
        }
        homology += "]";
        System.out.println(homology);
    }

    public void homomorphisms(int min, int max) {
        System.out.println("Calculating C_" + min + "(G),...,C_" + max + "(G), where G is the graph:");
        System.out.println(graph);
        for(int i = min; i <= max; i++) {
            System.out.println("Found " + generate(i).occupation() + " non-degenerated graph homomorphisms between the " + i + "-Cube and the input graph.");
        }

    }


    private IndexList<Homomorphism> generate(int i) {
        MapFactory factory = new MapFactory(i, graph);
        IndexList<Homomorphism> result = factory.generate();
        return result;
    }

    public Vector4D<Integer, int[], SparseVector[], ArrayList<SparseVector>> boundary(IndexList<Homomorphism> lower, IndexList<Homomorphism> higher, int n) throws Exception {
        long ms = System.currentTimeMillis();
        ArrayList<SparseVector> remaining = new ArrayList<>();
        int[] doneCols = new int[higher.occupation()];
        SparseVector[] rows = new SparseVector[doneCols.length];
        int done = 0;

        for(int i = 0; i < higher.occupation(); i++) {
            if(i % 500 == 0) System.out.print(Colors.RED_BOLD + "Generating boundary matrices " + Colors.RESET + Colors.CYAN + "" + i + "/" + higher.occupation() + " rows done!" + Colors.RESET + "\r");
            Homomorphism hom = higher.list[i];
            SparseVector vector = new SparseVector(lower.occupation(), Math.min(lower.occupation(), 10 * hom.values.length));
            int sign = 1;
            for(int j = 0; j < n; j++) {
                int m = binarySearch(lower, hom, j, '-');
                int p = binarySearch(lower, hom, j, '+');
                if(m < lower.occupation() && hom.compareToRestricted('-', j, lower.list[m]) == 0) {
                    vector.set(m, sign);
                }
                if(p < lower.occupation() && hom.compareToRestricted('+', j, lower.list[p]) == 0) {
                    vector.set(p, -sign);
                }
                sign *= -1;
            }

            for(int k = 0; k < vector.occupation; ) {
                int p = binarySearch(doneCols, vector.indices[k], done);
                if(p < done && doneCols[p] == vector.indices[k]) {
                    vector.add(rows[p], - vector.values[k] * rows[p].values[0]);
                }else k++;
            }
            if(vector.occupation == 0) continue;
            if(vector.values[0] == 1 || vector.values[0] == -1) {
                int p = binarySearch(doneCols, vector.indices[0], done);
                System.arraycopy(doneCols, p, doneCols, p + 1, done - p);
                doneCols[p] = vector.indices[0];
                System.arraycopy(rows, p, rows, p + 1, done - p);
                rows[p] = vector;
                for(SparseVector v : remaining) {
                    int j = v.index(vector.indices[0]);
                    if(j < v.occupation && v.indices[j] == vector.indices[0]) {
                        v.add(vector, - v.values[j] * vector.values[0]);
                    }
                }
                done++;
            }else {
                remaining.add(vector);
            }
        }
        return new Vector4D<>(done, doneCols, rows, remaining);
    }

    public Smith smith(Vector4D<Integer, int[], SparseVector[], ArrayList<SparseVector>> boundary, boolean print) throws Exception {
        long ms = System.currentTimeMillis();
        int done = boundary.x;
        int[] doneCols = boundary.y;
        SparseVector[] rows = boundary.z;
        SparseVector[] matrix = new SparseVector[boundary.w.size()];
        int n = matrix.length;
        for(int i = 0; i < boundary.w.size(); i++) {
            if(boundary.w.get(i).occupation == 0) {
                n--;
            }else {
                matrix[i - (matrix.length - n)] = boundary.w.get(i);
            }
        }
        Smith smith = new Smith(16);
        smith.addTo(1, done);
        for(int t = 0; t < n; t++) {
            if(t % 100 == 0) System.out.print(Colors.RED_BOLD + "Calculating smith normal form " + Colors.RESET + Colors.CYAN + "" + (t + done) + "/" + (n + done) + " rows done!" + Colors.RESET + "\r");
            Indexer idx = new Indexer(n-t);

            if(print) {
                System.out.println("Prepare " + t + ":");
                for(SparseVector vec : matrix) System.out.println(vec);
                System.out.println(idx);
            }

            int j = -1;
            for(int i = t; i < n; i++) {
                if(matrix[i].occupation == 0) {
                    SparseVector v = matrix[i];
                    matrix[i] = matrix[--n];
                    matrix[n] = v;
                    i--;
                }else if(matrix[i].indices[0] < j || j < 0) {
                    idx.empty();
                    idx.add(i);
                    j = matrix[i].indices[0];
                }else if(matrix[i].indices[0] == j) {
                    idx.add(i);
                }
            }
            if(j < 0) break; // maybe also catch if idx.isEmpty()
            boolean col = true;
            while(true) {

                if(print) {
                    System.out.println("Begin while, col: " + col + " t: " + t);
                    for(SparseVector vec : matrix) System.out.println(vec);
                    System.out.println(idx);
                }

                if(col) {
                    // Pivotization:
                    int k = -1; // is row index of pivot row.
                    int h = 0; // h is index of indexer, where k lays.
                    for(int l = 0; l < idx.occupation; l++) {
                        int i = idx.indices[l];
                        if (k < 0 || Math.abs(matrix[k].values[0]) > Math.abs(matrix[i].values[0])) {
                            k = i;
                            h = l;
                        }
                    }
                    if(k < 0) return smith;
                    SparseVector temp = matrix[t];
                    matrix[t] = matrix[k];
                    matrix[k] = temp;
                    if(idx.indices[0] != t) {
                        idx.removePos(h);
                    }
                    if(idx.indices[0] == t) idx.removePos(0);
                    Indexer nextIdx = new Indexer(idx.indices.length - 1);
                    for(int l = 0; l < idx.occupation; l++) {
                        int i = idx.indices[l];
                        int lambda = matrix[i].values[0] / matrix[t].values[0];
                        matrix[i].add(matrix[t], - lambda);
                        if(matrix[i].indices[0] == matrix[t].indices[0]) nextIdx.add(i);
                    }
                    idx = nextIdx;
                    if(nextIdx.occupation > 0) {
                        if(matrix[t].occupation > 0) idx.add(t);
                        col = true;
                    }else {
                        col = false;
                    }

                    if(print) {
                        System.out.println("End while");
                        for(SparseVector vec : matrix) System.out.println(vec);
                        System.out.println(idx);
                    }

                }else {
                    for(int i = 1; i < matrix[t].occupation; i++) {
                        matrix[t].values[i] %= matrix[t].values[0];
                        if(matrix[t].values[i] == 0) {
                            matrix[t].remove(i);
                            i--;
                        }
                    }
                    if(matrix[t].occupation == 1) {
                        smith.addTo(Math.abs(matrix[t].values[0]), 1);
                        break;
                    }else {
                        int k = -1;
                        for(int i = 0; i < matrix[t].occupation; i++) {
                            if(k < 0 || Math.abs(matrix[t].values[k]) > Math.abs(matrix[t].values[i])) {
                                k = i;
                            }
                        }
                        // because occupation > 1 and for every h: matrix[t].values[h] < matrix[t].values[0], k != 0.
                        int curr = matrix[t].values[0];
                        matrix[t].values[0] = matrix[t].values[k];
                        matrix[t].values[k] = curr;
                        for(int i = t + 1; i < n; i++) {
                            int l = matrix[i].index(matrix[t].indices[k]);
                            if(l < matrix[i].occupation && matrix[i].indices[l] == matrix[t].indices[k]) {
                                curr = matrix[i].values[l];
                                matrix[i].remove(l);
                                matrix[i].insert(0, matrix[t].indices[0], curr);
                                idx.add(i);
                            }
                        }
                        if(idx.isEmpty()) col = false;
                        else {
                            idx.add(t);
                            col = true;
                        }
                    }

                    if(print) {
                        System.out.println("End while");
                        for(SparseVector vec : matrix) System.out.println(vec);
                        System.out.println(idx);
                    }
                }
            }
        }
        System.out.println(Colors.RED_BOLD + "Calculating smith normal form " + Colors.RESET + Colors.CYAN + "" + (n + done) + "/" + (n + done) + " rows done!" + Colors.RESET + "\r");
        return smith;
    }


    public int binarySearch(int[] arr, int i, int max) {
        return binarySearch(arr, i, 0, max);
    }

    public int binarySearch(int[] arr, int i, int min, int max) {
        if(max == 0 || i > arr[max - 1]) return max;
        int left = min;
        int right = max;
        while(left < right) {
            int mid = (left + right) / 2;
            if(arr[mid] > i) right = mid;
            else if(arr[mid] < i) left = mid + 1;
            else return mid;
        }
        return left;
    }

    public static int binarySearch(IndexList<Homomorphism> lower, Homomorphism hom, int i, char sign) {
        int left = 0;
        int right = lower.occupation();
        while(left < right) {
            int mid = (left + right) / 2;
            if(hom.compareToRestricted(sign, i, lower.list[mid]) < 0) right = mid;
            else if(hom.compareToRestricted(sign, i, lower.list[mid]) > 0) left = mid + 1;
            else return mid;
        }
        return left;
    }
}
