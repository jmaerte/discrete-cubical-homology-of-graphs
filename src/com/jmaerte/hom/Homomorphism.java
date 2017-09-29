package com.jmaerte.hom;

import com.jmaerte.graph.Graph;
import com.jmaerte.graph.Vertex;
import com.jmaerte.graph.cube.Cube;

/**
 * Created by Julian on 28/09/2017.
 */
public class Homomorphism implements Comparable<Homomorphism> {

    protected Cube preimage;
    protected Graph image;
    protected int[] values;
    protected boolean[] hasValue;


    protected Homomorphism parental;
    protected int value;
    protected int layer;

    /**
     *
     * @param parental
     * @param value
     * @param layer
     */
    public Homomorphism(Homomorphism parental, int value, int layer) {
        this.parental = parental;
        this.preimage = parental.preimage;
        this.image = parental.image;
        this.value = value;
        this.layer = layer;
    }

    public Homomorphism(Cube preimage, Graph image) throws Exception {
        this(preimage, image, new int[preimage.size()], new boolean[preimage.size()]);
    }

    public Homomorphism(Cube preimage, Graph image, int[] values, boolean[] hasValue) throws Exception {
        if(preimage.size() != values.length) throw new Exception("Homomorphism must be defined on its pre-image!");
        this.preimage = preimage;
        this.image = image;
        this.values = values;
        this.hasValue = hasValue;
    }

    protected void setValue(int i, int j) {
        if(hasValue[i]) return;
        values[i] = j;
    }

    public int get(int i) {
        if(0 > i || i > layer) return -1;
        if(values != null) {
            return hasValue[i] ? values[i] : -1;
        }else {
            return i == layer ? value : parental.get(i);
        }
    }

    public Vertex getVertex(int i) {
        return hasValue[i] ? image.getVertex(values[i]) : null;
    }

    /** We assume that both homomorphisms are complete.(hasValue[i] = true for all i)
     *
     * @param that
     * @return
     */
    public int compareTo(Homomorphism that) {
        if(this.preimage != that.preimage || this.image != that.image) System.exit(0);
        for(int i = 0; i < this.values.length; i++) {
            if(this.get(i) != that.get(i)) return this.get(i) - that.get(i);
        }
        return 0;
    }

    /** 0 <= i < n
     *
     * @param sign
     * @param i
     * @param that
     * @return
     */
    public int compareToRestricted(char sign, int i, Homomorphism that) {
        if(that.preimage.getDimension() != this.preimage.getDimension() - 1) System.exit(0);
        Cube pseudo = this.preimage.faces[2 * i + (sign == '-' ? 0 : 1)];
        System.out.println(pseudo);
        for(int k = 0; k < that.preimage.size(); k++) {
            if(this.get(pseudo.getVertex(k).id) != that.get(k)) return this.get(pseudo.getVertex(k).id) - that.get(k);
        }
        return 0;
    }

}
