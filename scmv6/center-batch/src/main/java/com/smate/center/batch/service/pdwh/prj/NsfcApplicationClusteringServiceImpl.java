package com.smate.center.batch.service.pdwh.prj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.model.pdwh.prj.EigenValueForSorting;
import com.smate.center.batch.model.pdwh.prj.KmeansBallTree;
import com.smate.center.batch.model.pdwh.prj.KmeansClusteringCenter;
import com.smate.center.batch.model.pdwh.prj.KmeansPoint;
import com.smate.center.batch.model.pdwh.prj.KmeansPointCollection;

import Jama.Matrix;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.EigenvalueDecomposition;

@Service("nsfcApplicationClusteringService")
@Transactional(rollbackFor = Exception.class)
public class NsfcApplicationClusteringServiceImpl implements NsfcApplicationClusteringService {

    public static ArrayList<KmeansClusteringCenter> CENTERS = new ArrayList<KmeansClusteringCenter>();
    public static ArrayList<KmeansPoint> INSTANCES = new ArrayList<KmeansPoint>();
    public static ArrayList<KmeansClusteringCenter> PRE_CENS;
    public static int DIMENSION;
    public static int MAX_INSTANCE_NUM_NOT_SPLIT = 5;
    public static KmeansPointCollection BALL_TREE;
    public static int TRY_TIMES = 30;
    // map cluster center results to its evaluation
    static ArrayList<Entry<ArrayList<KmeansClusteringCenter>, Double>> RESULTS =
            new ArrayList<Entry<ArrayList<KmeansClusteringCenter>, Double>>(TRY_TIMES);

    static boolean timeToEnd() {
        if (PRE_CENS == null)
            return false;
        for (KmeansClusteringCenter cc : NsfcApplicationClusteringServiceImpl.CENTERS) {
            if (!PRE_CENS.contains(cc))
                return false;
        }
        return true;
    }

    @Override
    public DenseDoubleMatrix2D getLaplacianMatrix(DenseDoubleMatrix2D matrix) throws Exception {
        DenseDoubleMatrix2D laplacian = new DenseDoubleMatrix2D(matrix.rows(), matrix.columns());
        for (int r = 0; r < laplacian.rows(); r++) {
            double sumRow = 0.0;
            for (int c = 0; c < laplacian.columns(); c++) {
                if (r != c) {
                    double wrc = matrix.getQuick(r, c);
                    sumRow += wrc;
                    laplacian.setQuick(r, c, -wrc);
                }
            }
            laplacian.setQuick(r, r, sumRow);
        }
        return laplacian;
    }

    @Override
    public void spetralClusteringViaColt(DenseDoubleMatrix2D similarityMatrix, int numCluster) throws Exception {
        for (int r = 0; r < similarityMatrix.rows(); r++) {
            for (int c = 0; c < similarityMatrix.columns(); c++) {
                System.out.print(similarityMatrix.getQuick(r, c) + " ");
            }
            System.out.println();
        }
        DenseDoubleMatrix2D weightedMatrix = (DenseDoubleMatrix2D) similarityMatrix.copy();
        DenseDoubleMatrix2D laplacianMatrix = this.getLaplacianMatrix(weightedMatrix);
        EigenvalueDecomposition eigenDecomposition = new EigenvalueDecomposition(laplacianMatrix);
        DoubleMatrix2D eigenVectors = eigenDecomposition.getV();
        DoubleMatrix1D eigenValues = eigenDecomposition.getRealEigenvalues();
        System.out.println("EigenValues:");
        EigenValueForSorting[] eigenvaluesToSort = new EigenValueForSorting[eigenValues.size()];
        for (int iEigen = 0; iEigen < eigenValues.size(); iEigen++) {
            eigenvaluesToSort[iEigen] = new EigenValueForSorting(eigenValues.getQuick(iEigen), iEigen);
            System.out.print(eigenValues.getQuick(iEigen) + " ");
        }
        System.out.println();
        Arrays.sort(eigenvaluesToSort);

        for (int iEigen = 0; iEigen < eigenValues.size(); iEigen++) {
            System.out.print(eigenvaluesToSort[iEigen].getIndex() + ":"
                    + eigenValues.getQuick(eigenvaluesToSort[iEigen].getIndex()) + " ");
            System.out.println();
        }
        System.out.println();
        System.out.println("EigenVectors:");
        double[][] pos = new double[eigenVectors.rows()][numCluster];
        for (int iEigen = 0; iEigen < eigenVectors.columns(); iEigen++) {
            // double[] pos = new double[eigenVectors.rows()];
            System.out.print("[");
            for (int r = 0; r < eigenVectors.rows(); r++) {
                System.out.print(eigenVectors.getQuick(r, eigenvaluesToSort[iEigen].getIndex()) + " ");
                // 加载row中的特征向量
                // pos[r] = eigenVectors.getQuick(r, eigenvaluesToSort[iEigen].getIndex());
                if (iEigen < numCluster) {
                    pos[r][iEigen] = eigenVectors.getQuick(r, eigenvaluesToSort[iEigen].getIndex());
                }
            }
            System.out.println("]");
        }

        // 加载结果，构建NxK的特征向量，其中K为预计的聚类数
        for (int i = 0; i < eigenVectors.rows(); i++) {
            double[] pp = new double[numCluster];
            for (int j = 0; j < numCluster; j++) {
                pp[j] = pos[i][j];
            }
            NsfcApplicationClusteringServiceImpl.INSTANCES.add(new KmeansPoint(pp));
        }
        NsfcApplicationClusteringServiceImpl.DIMENSION = numCluster;
    }

    @Override
    public void spetralClusteringViaJama(DenseDoubleMatrix2D similarityMatrix, int numCluster) throws Exception {
        for (int r = 0; r < similarityMatrix.rows(); r++) {
            for (int c = 0; c < similarityMatrix.columns(); c++) {
                System.out.print(similarityMatrix.getQuick(r, c) + " ");
            }
            System.out.println();
        }
        DenseDoubleMatrix2D weightedMatrix = (DenseDoubleMatrix2D) similarityMatrix.copy();
        DenseDoubleMatrix2D laplacianMatrix = this.getLaplacianMatrix(weightedMatrix);
        Matrix jamaLaplacianMatrix = new Matrix(laplacianMatrix.toArray());
        Jama.EigenvalueDecomposition eigenDecomposition = jamaLaplacianMatrix.eig();
        // 特征向量
        Matrix eigenVectors = eigenDecomposition.getV();
        // 特征值
        Matrix eigenValues = eigenDecomposition.getD();
        EigenValueForSorting[] eigenvaluesToSort = new EigenValueForSorting[eigenValues.getColumnDimension()];
        for (int iEigen = 0; iEigen < eigenValues.getColumnDimension(); iEigen++) {
            // 特征值处于方阵的主对角线
            eigenvaluesToSort[iEigen] = new EigenValueForSorting(eigenValues.get(iEigen, iEigen), iEigen);
            System.out.print(eigenValues.get(iEigen, iEigen) + " ");
        }
        System.out.println();
        Arrays.sort(eigenvaluesToSort);
        for (int iEigen = 0; iEigen < eigenValues.getColumnDimension(); iEigen++) {
            System.out.print(eigenvaluesToSort[iEigen].getIndex() + ":"
                    + eigenValues.get(eigenvaluesToSort[iEigen].getIndex(), eigenvaluesToSort[iEigen].getIndex())
                    + " ");
            System.out.println();
        }
        System.out.println();
        System.out.println("特征向量矩阵");
        for (int iEigen = 0; iEigen < eigenVectors.getRowDimension(); iEigen++) {
            System.out.print("[");
            for (int r = 0; r < eigenVectors.getColumnDimension(); r++) {
                System.out.print(eigenVectors.get(iEigen, r) + " ");
            }
            System.out.println("]");
        }
        System.out.println();
        double[][] pos = new double[eigenVectors.getColumnDimension()][numCluster];
        // 从小到大，选取不超过聚类的簇数，加载至kmeans所需的n*numCluster矩阵中
        for (int iEigen = 0; iEigen < eigenVectors.getColumnDimension(); iEigen++) {
            System.out.print("[");
            for (int r = 0; r < eigenVectors.getRowDimension(); r++) {
                System.out.print(eigenVectors.get(r, eigenvaluesToSort[iEigen].getIndex()) + " ");
                // 加载row中的特征向量
                if (iEigen < numCluster) {
                    pos[r][iEigen] = eigenVectors.get(r, eigenvaluesToSort[iEigen].getIndex());
                }
            }
            System.out.println("]");
        }
        // row方向归一化
        Matrix ppM = this.NormalizeL2RowDirection(new Matrix(pos));
        System.out.println("第二范式归一化矩阵");
        ppM.print(ppM.getColumnDimension(), 8);
        // 加载结果，构建NxK的特征向量，其中K为预计的聚类数
        for (int i = 0; i < eigenVectors.getRowDimension(); i++) {
            double[] pp = new double[numCluster];
            for (int j = 0; j < numCluster; j++) {
                pp[j] = ppM.get(i, j);
            }
            NsfcApplicationClusteringServiceImpl.INSTANCES.add(new KmeansPoint(pp));
        }
        NsfcApplicationClusteringServiceImpl.DIMENSION = numCluster;
    }

    // gives your dataset's path and this function will build the internal data structures.
    public static void loadData(String path) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
        String line;
        while ((line = r.readLine()) != null) {
            String[] fs = line.split(" +");
            double[] pos = new double[fs.length];
            int i = 0;
            for (String s : fs) {
                pos[i++] = Double.valueOf(s + ".0");
            }
            NsfcApplicationClusteringServiceImpl.DIMENSION = fs.length;
            NsfcApplicationClusteringServiceImpl.INSTANCES.add(new KmeansPoint(pos));
        }
        r.close();
        BALL_TREE = KmeansBallTree.getOneInstance(null);
    }

    static double evaluate(ArrayList<KmeansClusteringCenter> cens) {
        double ret = 0.0;
        for (KmeansClusteringCenter cc : cens) {
            ret += cc.evaluate();
        }
        return ret;
    }

    @Override
    public Entry<Integer[], Double> nsfcCluster(int k) {
        for (int t = 0; t < NsfcApplicationClusteringServiceImpl.TRY_TIMES; t++) {
            // 随机选择聚类中心
            CENTERS.clear();
            if (PRE_CENS != null)
                PRE_CENS = null;
            Random rand = new Random();
            HashSet<Integer> rSet = new HashSet<Integer>();
            int size = INSTANCES.size();
            while (rSet.size() < k) {
                rSet.add(rand.nextInt(size));
            }
            for (int index : rSet) {
                NsfcApplicationClusteringServiceImpl.CENTERS
                        .add(new KmeansClusteringCenter(NsfcApplicationClusteringServiceImpl.INSTANCES.get(index)));
            }

            // 循环计算簇中心距离，直到不再变化
            int iterate = 0;
            while (!timeToEnd()) {
                KmeansPointCollection.locateAndAssign(BALL_TREE);
                PRE_CENS = new ArrayList<KmeansClusteringCenter>(CENTERS);
                ArrayList<KmeansClusteringCenter> newCenters = new ArrayList<KmeansClusteringCenter>();
                for (KmeansClusteringCenter cc : CENTERS) {
                    cc = cc.getNewCenter();
                    newCenters.add(cc);
                }
                CENTERS = newCenters;
                iterate++;
            }
            NsfcApplicationClusteringServiceImpl.RESULTS.add(new SimpleEntry<ArrayList<KmeansClusteringCenter>, Double>(
                    PRE_CENS, NsfcApplicationClusteringServiceImpl.evaluate(PRE_CENS)));
            KmeansPointCollection.ALL_COUNT = 0;
            KmeansPointCollection.COUNT = 0;
            System.out.println("第" + t + "次聚类的迭代次数为：" + iterate);
        }

        // 寻找多次中的最小值
        double minEvaluate = Double.MAX_VALUE;
        int minIndex = 0, i = 0;

        for (Entry<ArrayList<KmeansClusteringCenter>, Double> entry : RESULTS) {
            double e = entry.getValue();
            System.out.println("第" + i + "次的中心点距离之和为：" + e);
            if (e < minEvaluate) {
                minEvaluate = e;
                minIndex = i;
            }
            i++;
        }

        CENTERS = RESULTS.get(minIndex).getKey();
        double evaluate = RESULTS.get(minIndex).getValue();
        System.out.println("最有分类为第" + minIndex + "次：" + evaluate);
        // 返回聚类编号
        Integer[] ret = new Integer[INSTANCES.size()];
        for (int cNum = 0; cNum < CENTERS.size(); cNum++) {
            KmeansClusteringCenter cc = CENTERS.get(cNum);
            for (int pi : cc.belongedPoints()) {
                ret[pi] = cNum;
            }
        }
        return new SimpleEntry<Integer[], Double>(ret, evaluate);
    }

    /**
     * @param k the initial number of clustering centers
     * @return an entry:the key is the result of clustering.The label starts from 0.The value is the
     *         evaluation of the clustering result
     */
    public static Entry<Integer[], Double> cluster(int k) {
        for (int t = 0; t < NsfcApplicationClusteringServiceImpl.TRY_TIMES; t++) {
            // 随机选择聚类中心
            CENTERS.clear();
            if (PRE_CENS != null)
                PRE_CENS = null;
            Random rand = new Random();
            HashSet<Integer> rSet = new HashSet<Integer>();
            int size = INSTANCES.size();
            while (rSet.size() < k) {
                rSet.add(rand.nextInt(size));
            }
            for (int index : rSet) {
                NsfcApplicationClusteringServiceImpl.CENTERS
                        .add(new KmeansClusteringCenter(NsfcApplicationClusteringServiceImpl.INSTANCES.get(index)));
            }

            // 循环计算簇中心距离，直到不再变化
            while (!timeToEnd()) {
                KmeansPointCollection.locateAndAssign(BALL_TREE);
                PRE_CENS = new ArrayList<KmeansClusteringCenter>(CENTERS);
                ArrayList<KmeansClusteringCenter> newCenters = new ArrayList<KmeansClusteringCenter>();
                for (KmeansClusteringCenter cc : CENTERS) {
                    cc = cc.getNewCenter();
                    newCenters.add(cc);
                }
                CENTERS = newCenters;
            }
            NsfcApplicationClusteringServiceImpl.RESULTS.add(new SimpleEntry<ArrayList<KmeansClusteringCenter>, Double>(
                    PRE_CENS, NsfcApplicationClusteringServiceImpl.evaluate(PRE_CENS)));
            KmeansPointCollection.ALL_COUNT = 0;
            KmeansPointCollection.COUNT = 0;
        }

        // 寻找多次中的最小值
        double minEvaluate = Double.MAX_VALUE;
        int minIndex = 0, i = 0;
        for (Entry<ArrayList<KmeansClusteringCenter>, Double> entry : RESULTS) {
            double e = entry.getValue();
            if (e < minEvaluate) {
                minEvaluate = e;
                minIndex = i;
            }
            i++;
        }
        CENTERS = RESULTS.get(minIndex).getKey();
        double evaluate = RESULTS.get(minIndex).getValue();
        // 返回聚类编号
        Integer[] ret = new Integer[INSTANCES.size()];
        for (int cNum = 0; cNum < CENTERS.size(); cNum++) {
            KmeansClusteringCenter cc = CENTERS.get(cNum);
            for (int pi : cc.belongedPoints()) {
                ret[pi] = cNum;
            }
        }
        return new SimpleEntry<Integer[], Double>(ret, evaluate);
    }

    /**
     * gives the evaluation and differential of each k in specific range.you can use these infos to
     * choose a good k for your clustering
     * 
     * @param startK gives the start point of k for the our try on k(inclusive)
     * @param endK gives the end point(exclusive)
     * @return Entry's key is the evaluation of clustering of each k.The value is the differential of
     *         the evaluations--evaluation of k(i) - evaluation of k(i+1) for i in range(startK, endK -
     *         1)
     */
    public static Entry<ArrayList<Double>, ArrayList<Double>> cluster(int startK, int endK) {
        ArrayList<Integer[]> results = new ArrayList<Integer[]>();
        ArrayList<Double> evals = new ArrayList<Double>();
        for (int k = startK; k < endK; k++) {
            System.out.println("now k = " + k);
            Entry<Integer[], Double> en = NsfcApplicationClusteringServiceImpl.cluster(k);
            results.add(en.getKey());
            evals.add(en.getValue());
        }

        ArrayList<Double> subs = new ArrayList<Double>();
        for (int i = 0; i < evals.size() - 1; i++) {
            subs.add(evals.get(i) - evals.get(i + 1));
        }
        return new SimpleEntry<ArrayList<Double>, ArrayList<Double>>(evals, subs);
    }

    // row方向第二范式归一化，用于Kmean聚类
    private Matrix NormalizeL2RowDirection(Matrix m) {
        Matrix normalizedM = new Matrix(m.getRowDimension(), m.getColumnDimension());
        // 按行进行标准化-第二范式
        for (int i = 0; i < m.getRowDimension(); i++) {
            double xDenom = 0;
            for (int j = 0; j < m.getColumnDimension(); j++) {
                xDenom += Math.pow(m.get(i, j), 2);
            }
            xDenom = (xDenom > 0) ? 1.0 / Math.sqrt(xDenom) : 0;
            for (int j = 0; j < m.getColumnDimension(); j++) {
                double val = m.get(i, j) * xDenom;
                normalizedM.set(i, j, val);
            }
        }
        return normalizedM;
    }

    // 对列向量归一化
    private Matrix zScoreNormalize(Matrix m) {
        Matrix normalizedM = new Matrix(m.getRowDimension(), m.getColumnDimension());
        for (int c = 0; c < m.getColumnDimension(); c++) {
            // 列平均值
            double sum = 0.0;
            for (int r = 0; r < m.getRowDimension(); r++) {
                sum += m.get(r, c);
            }
            double cMean = sum / m.getRowDimension();
            // 标准差
            double sSum = 0.0;
            for (int r = 0; r < m.getRowDimension(); r++) {
                sSum += Math.pow((m.get(r, c) - cMean), 2);
            }
            double sd = Math.pow((sSum / m.getRowDimension()), 0.5);
            for (int r = 0; r < m.getRowDimension(); r++) {
                double org = (m.get(r, c) - cMean) / sd;
                System.out.println(org);
                normalizedM.set(r, c, org);
            }
        }
        return normalizedM;
    }

    // 注意此矩阵为编辑距离的计算过程矩阵，与真实数据有差别
    public static void main(String[] args) {
        double[][] dd1 = {{4, 2, -5}, {6, 4, -9}, {5, 3, -7}};
        Matrix laplacian = new Matrix(dd1);
        Matrix laplacian2 = new Matrix(3, 3);
        Matrix laplacian3 = new Matrix(3, 3);
        System.out.println("laplacian:");
        laplacian.print(laplacian.getColumnDimension(), 3);
        System.out.println();
        System.out.println("laplacian2:");
        laplacian2.print(laplacian2.getColumnDimension(), 3);
        for (int c = 0; c < laplacian.getColumnDimension(); c++) {
            // 行平均值
            double sum = 0.0;
            for (int r = 0; r < laplacian.getRowDimension(); r++) {
                sum += laplacian.get(r, c);
            }
            double cMean = sum / laplacian.getRowDimension();
            // 标准差
            double sSum = 0.0;
            for (int r = 0; r < laplacian.getRowDimension(); r++) {
                sSum += Math.pow((laplacian.get(r, c) - cMean), 2);
            }
            double sd = Math.pow((sSum / laplacian.getRowDimension()), 0.5);
            for (int r = 0; r < laplacian.getRowDimension(); r++) {
                double org = (laplacian.get(r, c) - cMean) / sd;
                System.out.println(org);
                laplacian2.set(r, c, org);
            }
        }
        laplacian.print(laplacian.getColumnDimension(), 3);
        laplacian2.print(laplacian2.getColumnDimension(), 3);

        // 按行进行标准化-第二范式
        for (int i = 0; i < laplacian.getRowDimension(); i++) {
            double xDenom = 0;
            for (int j = 0; j < laplacian.getColumnDimension(); j++) {
                xDenom += Math.pow(laplacian.get(i, j), 2);
            }
            xDenom = (xDenom > 0) ? 1.0 / Math.sqrt(xDenom) : 0;
            for (int j = 0; j < laplacian.getColumnDimension(); j++) {
                double val = laplacian.get(i, j) * xDenom;
                laplacian3.set(i, j, val);
            }
        }
        laplacian3.print(laplacian3.getColumnDimension(), 3);
        NsfcApplicationClusteringServiceImpl nacsl = new NsfcApplicationClusteringServiceImpl();
        /*
         * double[][] dd = {{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
         * 22, 23}, {1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22},
         * {2, 1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21}, {3, 2, 1,
         * 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}, {4, 3, 2, 1, 0, 1, 2,
         * 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19}, {5, 4, 3, 2, 1, 0, 1, 2, 3, 4, 5,
         * 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18}, {6, 5, 4, 3, 2, 1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
         * 10, 11, 12, 13, 14, 15, 16, 17}, {7, 6, 5, 4, 3, 2, 1, 1, 2, 3, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
         * 13, 14, 15, 16}, {8, 7, 6, 5, 4, 3, 2, 2, 2, 3, 4, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
         * {9, 8, 7, 6, 5, 4, 3, 3, 3, 3, 4, 4, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, {10, 9, 8, 7, 6,
         * 5, 4, 4, 4, 4, 4, 5, 5, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, {11, 10, 9, 8, 7, 6, 5, 5, 5, 5,
         * 5, 5, 5, 6, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, {12, 11, 10, 9, 8, 7, 6, 6, 6, 6, 6, 6, 6, 5, 6,
         * 7, 8, 9, 10, 11, 12, 13, 14, 15}, {13, 12, 11, 10, 9, 8, 7, 7, 7, 7, 7, 7, 7, 6, 5, 6, 7, 8, 9,
         * 10, 11, 12, 13, 14}, {14, 13, 12, 11, 10, 9, 8, 8, 8, 8, 8, 8, 8, 7, 6, 5, 6, 7, 8, 9, 10, 11,
         * 12, 13}, {15, 14, 13, 12, 11, 10, 9, 9, 9, 9, 9, 9, 9, 8, 7, 6, 5, 6, 7, 8, 9, 10, 11, 12}, {16,
         * 15, 14, 13, 12, 11, 10, 10, 10, 10, 10, 10, 10, 9, 8, 7, 6, 5, 6, 7, 8, 9, 10, 11}, {17, 16, 15,
         * 14, 13, 12, 11, 11, 11, 11, 11, 11, 11, 10, 9, 8, 7, 6, 5, 6, 7, 8, 9, 10}, {18, 17, 16, 15, 14,
         * 13, 12, 12, 12, 12, 12, 12, 12, 11, 10, 9, 8, 7, 6, 5, 6, 7, 8, 9}, {19, 18, 17, 16, 15, 14, 13,
         * 13, 13, 13, 13, 13, 13, 12, 11, 10, 9, 8, 7, 6, 5, 6, 7, 8}, {20, 19, 18, 17, 16, 15, 14, 14, 14,
         * 14, 14, 14, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 6, 7}, {21, 20, 19, 18, 17, 16, 15, 15, 15, 15,
         * 15, 15, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 6}, {22, 21, 20, 19, 18, 17, 16, 16, 16, 16, 16,
         * 16, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5}, {23, 21, 20, 19, 18, 17, 16, 16, 16, 16, 16, 16,
         * 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5}};
         */



        double[][] dd =
                {{341, 212.8638955, 98.45546898}, {212.8638955, 341, 269.447868}, {98.45546898, 269.447868, 341}};
        // double[][] dd = {{4, 2, -5}, {6, 4, -9}, {5, 3, -7}};
        // double[][] dd = {{1, 2}, {3, 6}};

        Matrix a = new Matrix(dd);
        a.print(a.getColumnDimension(), 6);
        Jama.EigenvalueDecomposition eig = a.eig();
        Matrix d = eig.getD();
        Matrix v = eig.getV();
        d.print(d.getColumnDimension(), 6);
        v.print(v.getColumnDimension(), 6);

        for (int i = 0; i < d.getColumnDimension(); i++) {
            System.out.print("第一个特征值为" + d.get(i, i) + "====对应特征向量为: ");
            for (int j = 0; j < d.getRowDimension(); j++) {
                System.out.print(v.get(j, i) + "; ");
            }
            System.out.println();
        }

        DenseDoubleMatrix2D ddm2 = new DenseDoubleMatrix2D(dd);
        try {
            nacsl.spetralClusteringViaJama(ddm2, 4);
            // nacsl.spetralClusteringViaColt(ddm2, 4);
            BALL_TREE = KmeansBallTree.getOneInstance(null);
            Entry<Integer[], Double> rs = nacsl.cluster(4);
            System.out.println("min evaluation is =:" + rs.getValue());
            int i = 0;
            System.out.println("Clustering result as following : ");
            for (Integer group : rs.getKey()) {
                System.out.println(i + " : " + group);
                i++;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
