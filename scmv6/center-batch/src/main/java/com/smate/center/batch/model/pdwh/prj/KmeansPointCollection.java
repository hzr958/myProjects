package com.smate.center.batch.model.pdwh.prj;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import com.smate.center.batch.service.pdwh.prj.NsfcApplicationClusteringServiceImpl;

public class KmeansPointCollection extends KmeansPoint {

    private double radius;
    private LinkedList<Integer> instances;
    private KmeansPointCollection[] children;
    private double[] sumOfPoints;
    public static int COUNT = 0, ALL_COUNT = 0;

    public KmeansPointCollection(KmeansPoint center, double r, LinkedList<Integer> ins) {
        super(center.pos);
        this.radius = r;
        this.instances = ins;
        sumOfPoints = new double[NsfcApplicationClusteringServiceImpl.DIMENSION];
    }

    public KmeansPointCollection() {
        super(new double[NsfcApplicationClusteringServiceImpl.DIMENSION]);
        instances = new LinkedList<Integer>();
        sumOfPoints = new double[NsfcApplicationClusteringServiceImpl.DIMENSION];
    }

    public void addInstance(int index) {
        instances.add(index);
        double[] pos = NsfcApplicationClusteringServiceImpl.INSTANCES.get(index).getPosition();
        for (int i = 0; i < NsfcApplicationClusteringServiceImpl.DIMENSION; i++) {
            sumOfPoints[i] += pos[i];
        }
    }

    public void endAdding() {
        int size = instances.size();
        for (int i = 0; i < NsfcApplicationClusteringServiceImpl.DIMENSION; i++) {
            this.pos[i] = this.sumOfPoints[i] / size;
        }
        this.radius = KmeansPoint.euclideanDistance(this,
                NsfcApplicationClusteringServiceImpl.INSTANCES.get(this.getFarestPoint(this)));
    }

    public int size() {
        return instances.size();
    }

    public double maxDistance(KmeansPoint p) {
        return radius + KmeansPoint.euclideanDistance(p, this);
    }

    public double minDistance(KmeansPoint p) {
        return KmeansPoint.euclideanDistance(p, this) - radius;
    }

    // 如果不落在单独的cluster中，就返回-1 否则返回cluster center的index
    public int isInSingleCluster() {
        ALL_COUNT++;
        PriorityQueue<Entry<Integer, Double>> maxpq = new PriorityQueue<Entry<Integer, Double>>(
                NsfcApplicationClusteringServiceImpl.CENTERS.size(), new Comparator<Entry<Integer, Double>>() {
                    public int compare(Entry<Integer, Double> e1, Entry<Integer, Double> e2) {
                        double d1 = e1.getValue(), d2 = e2.getValue();
                        if (d1 > d2) {
                            return 1;
                        }
                        if (d1 < d2) {
                            return -1;
                        }
                        return 0;
                    }
                });
        PriorityQueue<Entry<Integer, Double>> minpq = new PriorityQueue<Entry<Integer, Double>>(
                NsfcApplicationClusteringServiceImpl.CENTERS.size(), new Comparator<Entry<Integer, Double>>() {
                    public int compare(Entry<Integer, Double> e1, Entry<Integer, Double> e2) {
                        double d1 = e1.getValue(), d2 = e2.getValue();
                        if (d1 > d2) {
                            return 1;
                        }
                        if (d1 < d2) {
                            return -1;
                        }
                        return 0;
                    }
                });
        int index = 0;
        for (KmeansClusteringCenter cen : NsfcApplicationClusteringServiceImpl.CENTERS) {
            maxpq.add(new SimpleEntry<Integer, Double>(index, this.maxDistance(cen)));
            minpq.add(new SimpleEntry<Integer, Double>(index, this.minDistance(cen)));
            index++;
        }
        Entry<Integer, Double> the = maxpq.poll(), comp;
        index = the.getKey();
        double theDist = the.getValue();
        while ((comp = minpq.poll()) != null) {
            int ind = comp.getKey();
            double dis = comp.getValue();
            if (theDist < dis) {
                if (ind != index) {
                    COUNT++;
                    return index;
                } else
                    continue;
            } else {
                if (ind == index)
                    continue;
                return -1;
            }
        }
        return -1;
    }

    private int getFarestPoint(KmeansPoint p) {
        double maxDist = 0.0;
        int maxIndex = -1;
        for (int i : this.instances) {
            KmeansPoint pp = NsfcApplicationClusteringServiceImpl.INSTANCES.get(i);
            double dist = KmeansPoint.euclideanDistance(p, pp);
            if (dist >= maxDist) {
                maxDist = dist;
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    // split and store it to this node's children field, & return the children.
    public KmeansPointCollection[] split() {
        int firstCenter = this.getFarestPoint(this);
        KmeansPoint fir = NsfcApplicationClusteringServiceImpl.INSTANCES.get(firstCenter);
        int secondCenter = this.getFarestPoint(fir);
        KmeansPoint sec = NsfcApplicationClusteringServiceImpl.INSTANCES.get(secondCenter);
        this.children = new KmeansPointCollection[2];
        this.children[0] = new KmeansPointCollection();
        this.children[1] = new KmeansPointCollection();
        this.children[0].addInstance(firstCenter);
        this.children[1].addInstance(secondCenter);
        for (int i : this.instances) {
            if (i == firstCenter || i == secondCenter)
                continue;
            KmeansPoint p = NsfcApplicationClusteringServiceImpl.INSTANCES.get(i);
            double dist1 = KmeansPoint.euclideanDistance(p, fir), dist2 = KmeansPoint.euclideanDistance(p, sec);
            if (dist1 < dist2) {
                this.children[0].addInstance(i);
            } else {
                this.children[1].addInstance(i);
            }
        }
        this.children[0].endAdding();
        this.children[1].endAdding();
        return this.children;
    }

    public KmeansPointCollection[] getChildren() {
        return this.children;
    }

    public static void locateAndAssign(KmeansPointCollection hp) {
        int clusterIndex = hp.isInSingleCluster();
        if (clusterIndex != -1) {
            KmeansClusteringCenter cc = NsfcApplicationClusteringServiceImpl.CENTERS.get(clusterIndex);
            for (int pi : hp.instances) {
                cc.addPointToCluster(pi);
            }
            return;
        }
        if (hp.children == null) {
            for (int pi : hp.instances) {
                KmeansPoint p = NsfcApplicationClusteringServiceImpl.INSTANCES.get(pi);
                double minDist = Double.MAX_VALUE;
                int minCenIndex = 0, index = 0;
                for (KmeansClusteringCenter cc : NsfcApplicationClusteringServiceImpl.CENTERS) {
                    double dist = KmeansPoint.euclideanDistance(p, cc);
                    if (dist < minDist) {
                        minDist = dist;
                        minCenIndex = index;
                    }
                    index++;
                }
                KmeansClusteringCenter cen = NsfcApplicationClusteringServiceImpl.CENTERS.get(minCenIndex);
                cen.addPointToCluster(pi);
            }
        } else {
            for (KmeansPointCollection chp : hp.children) {
                KmeansPointCollection.locateAndAssign(chp);
            }
        }
    }
}
