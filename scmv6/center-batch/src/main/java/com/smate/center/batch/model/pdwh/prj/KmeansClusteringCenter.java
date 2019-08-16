package com.smate.center.batch.model.pdwh.prj;

import java.util.ArrayList;

import com.smate.center.batch.service.pdwh.prj.NsfcApplicationClusteringServiceImpl;

public class KmeansClusteringCenter extends KmeansPoint {

    private ArrayList<Integer> clusterPoints;
    private double[] sumOfPoints;

    public KmeansClusteringCenter(KmeansPoint p) {
        super(p.pos);
        clusterPoints = new ArrayList<Integer>();
        this.sumOfPoints = new double[this.dimension];
    }

    public void addPointToCluster(int index) {
        KmeansPoint p = NsfcApplicationClusteringServiceImpl.INSTANCES.get(index);
        clusterPoints.add(index);
        double[] po = p.getPosition();
        for (int i = 0; i < this.dimension; ++i) {
            sumOfPoints[i] += po[i];
        }
    }

    public KmeansClusteringCenter getNewCenter() {
        double[] pos = new double[NsfcApplicationClusteringServiceImpl.DIMENSION];
        for (int i = 0; i < this.dimension; ++i) {
            pos[i] = sumOfPoints[i] / this.clusterPoints.size();
        }
        return new KmeansClusteringCenter(new KmeansPoint(pos));
    }

    public double evaluate() {
        double ret = 0.0;
        for (int in : clusterPoints) {
            ret += KmeansPoint.squareDistance(NsfcApplicationClusteringServiceImpl.INSTANCES.get(in), this);
        }
        return ret;
    }

    public ArrayList<Integer> belongedPoints() {
        return new ArrayList<Integer>(this.clusterPoints);
    }
}
