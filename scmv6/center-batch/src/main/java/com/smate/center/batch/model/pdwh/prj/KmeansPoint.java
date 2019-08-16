package com.smate.center.batch.model.pdwh.prj;

import java.util.ArrayList;

public class KmeansPoint {
    protected double[] pos;
    protected int dimension;

    public KmeansPoint(int size) {
        pos = new double[size];
        this.dimension = size;
    }

    public KmeansPoint(double[] p) {
        this.pos = p;
        this.dimension = pos.length;
    }

    public int getDimension() {
        return this.dimension;
    }

    public double[] getPosition() {
        return pos.clone();
    }

    public static double euclideanDistance(KmeansPoint p1, KmeansPoint p2) {
        if (p1.pos.length != p2.pos.length) {
            return -1.0;
        }
        double[] p = new double[p1.pos.length];
        for (int i = 0; i < p1.pos.length; ++i) {
            p[i] = p1.pos[i] - p2.pos[i];
        }
        double sum = 0.0;
        for (int i = 0; i < p1.pos.length; ++i) {
            sum += Math.pow(p[i], 2.0);
        }
        return Math.sqrt(sum);
    }

    public static double squareDistance(KmeansPoint p1, KmeansPoint p2) {
        if (p1.pos.length != p2.pos.length) {
            return -1.0;
        }
        double[] p = new double[p1.pos.length];
        for (int i = 0; i < p1.pos.length; ++i) {
            p[i] = p1.pos[i] - p2.pos[i];
        }
        double sum = 0.0;
        for (int i = 0; i < p1.pos.length; ++i) {
            sum += Math.pow(p[i], 2.0);
        }
        return sum;
    }

    public boolean equals(Object o) {
        KmeansPoint p = (KmeansPoint) o;
        if (this.dimension != p.dimension) {
            return false;
        }
        for (int i = 0; i < this.dimension; i++) {
            if (this.pos[i] != p.pos[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        double[] a = {1.0, 2.0, 3.0, 4.0}, b = {2.0, 3.0, 4.0, 2.0};
        KmeansPoint p1 = new KmeansPoint(a), p2 = new KmeansPoint(b), p3 = new KmeansPoint(a);
        ArrayList<KmeansPoint> l = new ArrayList<KmeansPoint>();
        l.add(p1);
        l.add(p2);
        System.out.println(l.contains(p3));
    }

}
