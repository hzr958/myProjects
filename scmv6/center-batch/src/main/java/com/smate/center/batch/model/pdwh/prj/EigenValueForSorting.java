package com.smate.center.batch.model.pdwh.prj;

/**
 * 矩阵特征值（本征值）
 * 
 */
public class EigenValueForSorting implements Comparable<EigenValueForSorting> {
    private double eigenValue;
    private int index;

    public EigenValueForSorting(double eigenValue, int index) {
        this.eigenValue = eigenValue;
        this.index = index;
    }

    public double getEigenValue() {
        return eigenValue;
    }

    public void setEigenValue(double eigenValue) {
        this.eigenValue = eigenValue;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    @Override
    public int compareTo(EigenValueForSorting o) {
        if (o instanceof EigenValueForSorting) {
            if (this.eigenValue < o.eigenValue) {
                return -1;
            }
            if (this.eigenValue > o.eigenValue) {
                return 1;
            }
            return 0;
        }
        return -1;

    }

}
