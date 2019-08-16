package com.smate.center.batch.service.pdwh.prj;

import java.util.Map.Entry;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;

public interface NsfcApplicationClusteringService {
    public DenseDoubleMatrix2D getLaplacianMatrix(DenseDoubleMatrix2D matrix) throws Exception;

    public Entry<Integer[], Double> nsfcCluster(int k);

    public void spetralClusteringViaJama(DenseDoubleMatrix2D similarityMatrix, int numCluster) throws Exception;

    public void spetralClusteringViaColt(DenseDoubleMatrix2D similarityMatrix, int numCluster) throws Exception;
}
