package com.smate.center.batch.service.pdwh.prj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.smate.center.batch.dao.pdwh.prj.NsfcKwForSortingDao;
import com.smate.center.batch.dao.pdwh.prj.NsfcKwsTfCotfDao;
import com.smate.center.batch.dao.pdwh.prj.NsfcKwsTfCotfDetailDao;
import com.smate.center.batch.dao.pdwh.prj.NsfcPrjKeywordsDao;
import com.smate.center.batch.dao.pdwh.prj.NsfcPrjKwByAuthorDao;
import com.smate.center.batch.dao.pdwh.pub.KGPubCooperationDao;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubAddrInsRecordDao;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubDuplicateDAO;
import com.smate.center.batch.dao.sns.pub.ProjectDataFiveYearDao;
import com.smate.center.batch.dao.sns.wechat.OpenUserUnionDao;
import com.smate.center.batch.model.pdwh.prj.KmeansBallTree;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;
import com.smate.center.batch.service.pub.ProjectDataFiveYearService;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

@Service("nsfcApplicationAutoAssignService")
@Transactional(rollbackFor = Exception.class)
public class NsfcApplicationAutoAssignServiceImpl implements NsfcApplicationAutoAssignService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NsfcKwsTfCotfDao nsfcKwsTfCotfDao;
    @Autowired
    private NsfcPrjKeywordsDao nsfcPrjKeywordsDao;
    @Autowired
    private NsfcKwsTfCotfDetailDao nsfcKwsTfCotfDetailDao;
    @Autowired
    private NsfcPrjKwByAuthorDao nsfcPrjKwByAuthorDao;
    @Autowired
    private NsfcKwForSortingDao nsfcKwForSortingDao;
    @Autowired
    private PdwhPublicationService pdwhPublicationService;
    @Autowired
    private ProjectDataFiveYearService projectDataFiveYearService;
    @Autowired
    private PdwhPubAddrInsRecordDao pdwhPubAddrInsRecordDao;
    @Autowired
    private KGPubCooperationDao kGPubCooperationDao;
    @Autowired
    private ProjectDataFiveYearDao projectDataFiveYearDao;
    @Autowired
    private OpenUserUnionDao openUserUnionDao;
    @Autowired
    private PdwhPubDuplicateDAO pdwhPubDuplicateDAO;
    @Autowired
    private NsfcApplicationClusteringService nsfcApplicationClusteringService;

    private String[] filterKws = {"预测方法", "评估方法", "统一建模", "质量提升", "新理论", "算法研究", "问题研究", "实际应用", "新方法", "应用研究", "新方法",
            "正确性", "系统性", "科学问题", "技术研究", "理论基础", "设计理论", "理论模型", "理论模拟", "基础理论", "学习理论", "理论分析", "理论与方法", "理论计算",
            "篇章理论", "系统研究", "研究热点", "方法研究", "研究方法", "研究成果", "其他研究", "实验研究", "机制研究", "功能研究", "机理研究", "跨学科交叉研究", "试验研究",
            "队列研究", "理论研究", "基础研究", "实证研究", "原位研究", "性能研究", "关联研究", "纵向研究", "比较研究", "定量研究", "对比研究", "战略研究", "前瞻性队列研究",
            "临床研究", "质性研究", "案例研究"};

    private String[] filterKws1 = {"机理", "作用机制", "形成机理", "影响因素", "标准", "指标", "高端", "研究", "推广", "提取", "健康", "应用示范", "工艺",
            "装备", "问题", "理论", "基础", "模型", "系统", "体系", "建立", "提出", "处置", "分析", "指标", "产业", "管理", "产品", "尺度", "制备", "复合",
            "检测", "研制", "存储"};

    /**
     * 
     * 动态规划，求X与Y最长公共子序列
     * 
     * C[i,j] = null (if i = 0 OR j = 0) C[i,j] = C[i-1, j-1] + 1 (if X[i-1]=Y[j-1]) C[i,j] = Max(c[i][j
     * - 1], c[i - 1][j]) (if X[i-1] !=Y[j-1])
     * 
     */
    public int getLongestCommonKwSequence(String applicationKwsStr, String userPubKwsStr) {
        if (StringUtils.isEmpty(applicationKwsStr) || StringUtils.isEmpty(userPubKwsStr)) {
            return 0;
        }
        List<String> applicationKws = new ArrayList<String>();
        List<String> userPubKws = new ArrayList<String>();
        String[] applicationKwsOld = applicationKwsStr.split(";");
        for (String aStr : applicationKwsOld) {
            if (StringUtils.isEmpty(aStr)) {
                continue;
            }
            applicationKws.add(aStr.toLowerCase().trim());
        }
        String[] userPubKwsOld = userPubKwsStr.split(";");
        for (String uStr : userPubKwsOld) {
            if (StringUtils.isEmpty(uStr)) {
                continue;
            }
            userPubKws.add(uStr.toLowerCase().trim());
        }
        int[][] c = new int[applicationKws.size() + 1][userPubKws.size() + 1];
        // 从0开始，初始化矩阵
        for (int row = 0; row <= applicationKws.size(); row++) {
            c[row][0] = 0;
        }
        for (int column = 0; column <= userPubKws.size(); column++) {
            c[0][column] = 0;
        }
        for (int i = 1; i <= applicationKws.size(); i++)
            for (int j = 1; j <= userPubKws.size(); j++) {
                if ((applicationKws.get(i - 1)).equalsIgnoreCase(userPubKws.get(j - 1))) { // c[i][j] = c[i - 1][j - 1]
                                                                                           // + 1
                    c[i][j] = c[i - 1][j - 1] + 1;
                } else if (c[i][j - 1] > c[i - 1][j]) { // 如果c[i][j - 1] ！=c[i - 1][j]， c[i][j] = Max(c[i][j - 1], c[i -
                                                        // 1][j])
                    c[i][j] = c[i][j - 1];
                } else {
                    c[i][j] = c[i - 1][j];
                }
            }

        List<String> longestCommonSeq = new ArrayList<String>();
        // 回溯输出子序列
        for (int i = applicationKws.size(); i > 0; i--) {
            for (int j = userPubKws.size(); j > 0; j--) {
                if (c[i][j] > c[i - 1][j] && c[i][j] > c[i][j - 1]) {
                    if (c[i][j] == 0) {
                        break;
                    }
                    longestCommonSeq.add(applicationKws.get(i - 1));
                }
            }
        }
        return c[applicationKws.size()][userPubKws.size()];
    }

    public ArrayList<String> getLongestCommonKwSequenceList(String applicationKwsStr, String userPubKwsStr) {
        if (StringUtils.isEmpty(applicationKwsStr) || StringUtils.isEmpty(userPubKwsStr)) {
            return null;
        }
        List<String> applicationKws = new ArrayList<String>();
        List<String> userPubKws = new ArrayList<String>();
        String[] applicationKwsOld = applicationKwsStr.split(";");
        for (String aStr : applicationKwsOld) {
            if (StringUtils.isEmpty(aStr)) {
                continue;
            }
            applicationKws.add(aStr.toLowerCase().trim());
        }
        String[] userPubKwsOld = userPubKwsStr.split(";");
        for (String uStr : userPubKwsOld) {
            if (StringUtils.isEmpty(uStr)) {
                continue;
            }
            userPubKws.add(uStr.toLowerCase().trim());
        }
        int[][] c = new int[applicationKws.size() + 1][userPubKws.size() + 1];
        // 从0开始，初始化矩阵
        for (int row = 0; row <= applicationKws.size(); row++) {
            c[row][0] = 0;
        }
        for (int column = 0; column <= userPubKws.size(); column++) {
            c[0][column] = 0;
        }
        for (int i = 1; i <= applicationKws.size(); i++)
            for (int j = 1; j <= userPubKws.size(); j++) {
                if ((applicationKws.get(i - 1)).equalsIgnoreCase(userPubKws.get(j - 1))) { // c[i][j] = c[i - 1][j - 1]
                                                                                           // + 1
                    c[i][j] = c[i - 1][j - 1] + 1;
                } else if (c[i][j - 1] > c[i - 1][j]) { // 如果c[i][j - 1] ！=c[i - 1][j]， c[i][j] = Max(c[i][j - 1], c[i -
                                                        // 1][j])
                    c[i][j] = c[i][j - 1];
                } else {
                    c[i][j] = c[i - 1][j];
                }
            }

        ArrayList<String> longestCommonSeq = new ArrayList<String>();
        // 回溯输出子序列
        for (int i = applicationKws.size(); i > 0; i--) {
            for (int j = userPubKws.size(); j > 0; j--) {
                if (c[i][j] > c[i - 1][j] && c[i][j] > c[i][j - 1]) {
                    if (c[i][j] == 0) {
                        break;
                    }
                    longestCommonSeq.add(applicationKws.get(i - 1));
                }
            }
        }
        return longestCommonSeq;
    }

    @Override
    public void calculateSimularity(Long applicaitonId) throws Exception {
        List<String> prjKwsList = this.nsfcKwsTfCotfDao.getPrjCodeInfo(null);
        // prjKwsList.add("互联网金融;价值发现;众筹;体系;分析技术;基;多层次;多维度;实证分析;展示;影响;影响因素;影响路径;情感;情感分析;投资决策;投资意愿;投资者;投资者参与;文本挖掘;经济学;计量模型;计量经济;语言特征;项目融资;预测能力");
        int i = 1;
        Date allStart = new Date();
        System.out.println("计算开始=======" + allStart.getTime());
        for (String prjcode : prjKwsList) {
            Map<String, Integer> cotfSimularity = new HashMap<String, Integer>();
            String prjKws = this.nsfcKwsTfCotfDao.getPrjKwInfoByCode(prjcode);
            Date start = new Date();
            cotfSimularity = this.getCotfSimularity(prjKws, null);
            if (cotfSimularity == null) {
                continue;
            }
            System.out.println("第" + i + "个项目================================================");
            System.out.println("项目prjcode为：" + prjcode);
            for (Entry<String, Integer> et : cotfSimularity.entrySet()) {
                if (et.getValue() < 2) {
                    continue;
                }
                System.out.println("公共关键词为==" + et.getKey() + "==，cotf：" + et.getValue());
            }
            Date end = new Date();
            System.out.println("第" + i + "个项目================================================,用时为（毫秒）:"
                    + (end.getTime() - start.getTime()));
            i++;
        }
        Date allEnd = new Date();
        System.out.println("所有项目计算结束================================================,用时为（毫秒）:"
                + (allEnd.getTime() - allStart.getTime()));
    }


    public Map<String, Integer> getCotfSimularity(String prjKws, Long psnId) {
        Map<String, Integer> rsMap = new HashMap<String, Integer>();
        List<String> psnPubKws = this.nsfcKwsTfCotfDao.getPsnKwsInfo(psnId);
        if (psnPubKws == null || psnPubKws.size() <= 0) {
            return null;
        }
        for (String kws : psnPubKws) {
            ArrayList<String> lst = this.getLongestCommonKwSequenceList(prjKws, kws);
            StringBuilder sb = new StringBuilder();
            if (lst != null && lst.size() > 1) {
                Collections.sort(lst);
                for (String kw : lst) {
                    sb.append(kw);
                    sb.append(";");
                }
                String cotfStr = sb.subSequence(0, sb.length() - 1).toString();
                Integer cotf = rsMap.get(cotfStr);
                if (cotf == null || cotf == 0) {
                    rsMap.put(cotfStr, 1);
                } else {
                    rsMap.put(cotfStr, cotf + 1);
                }
            }
        }
        return rsMap;
    }

    /**
     * DP求解edit distance编辑距离, 用于申请书分类；修改，移动，删除，新增都算是一次编辑操作 状态转移方程:
     * 
     * 
     * 
     */
    public Integer getTextEditDistance(String oText, String cText) {
        if (StringUtils.isEmpty(oText) || StringUtils.isEmpty(cText)) {
            return null;
        }
        // 对比两个文档相似度，预先准备
        List<String> oTextKws = new ArrayList<String>();
        List<String> cTextKws = new ArrayList<String>();
        String[] oTextKwsOld = oText.split(";");
        for (String aStr : oTextKwsOld) {
            if (StringUtils.isEmpty(aStr)) {
                continue;
            }
            oTextKws.add(aStr.toLowerCase().trim());
        }
        String[] cTextKwsOld = cText.split(";");
        for (String uStr : cTextKwsOld) {
            if (StringUtils.isEmpty(uStr)) {
                continue;
            }
            cTextKws.add(uStr.toLowerCase().trim());
        }
        // 初始化矩阵
        int[][] distanceM = new int[oTextKws.size() + 1][cTextKws.size() + 1];
        for (int row = 0; row <= oTextKws.size(); row++) {
            distanceM[row][0] = row;
        }
        for (int column = 0; column <= cTextKws.size(); column++) {
            distanceM[0][column] = column;
        }
        // 状态转移方程：上一个最优解到下个最优解，只需要删除，添加或者置换操作
        // d[i,j] = 0; if i =0 or j=0
        // d[i,j] = min(d[i][j-1]+1,d[i-1][j]+1,d[i-1][j-1]); if Xi = Yj
        // d[i,j] = min(d[i][j-1]+1,d[i-1][j]+1,d[i-1][j-1]+1); if Xi != Yj
        for (int i = 1; i <= oTextKws.size(); i++) {
            for (int j = 1; j <= cTextKws.size(); j++) {
                if (oTextKws.get(i - 1).equals(cTextKws.get(j - 1))) {
                    distanceM[i][j] =
                            getMinValue(distanceM[i - 1][j] + 1, distanceM[i][j - 1] + 1, distanceM[i - 1][j - 1]);
                } else {
                    distanceM[i][j] =
                            getMinValue(distanceM[i - 1][j] + 1, distanceM[i][j - 1] + 1, distanceM[i - 1][j - 1] + 1);
                }
            }
        }
        // 打印出矩阵
        /*
         * for (int i = 0; i <= oTextKws.size(); i++) { for (int j = 0; j <= cTextKws.size(); j++) {
         * System.out.print(distanceM[i][j] + ";"); } System.out.print("\n"); }
         */
        return distanceM[oTextKws.size()][cTextKws.size()];
    }

    // 采用字符对比的方式来查找文本相似度
    public Integer getTextEditDistanceByContent(String oText, String cText) {
        if (StringUtils.isEmpty(oText) || StringUtils.isEmpty(cText)) {
            return null;
        }
        // 对比两个文档相似度，预先准备
        List<String> oTextKws = new ArrayList<String>();
        List<String> cTextKws = new ArrayList<String>();
        char[] oTextChar = oText.toCharArray();
        for (char aStrChar : oTextChar) {
            String aStr = Character.toString(aStrChar);
            if (StringUtils.isEmpty(aStr)) {
                continue;
            }
            oTextKws.add(aStr.toLowerCase().trim());
        }
        char[] cTextKwsOld = cText.toCharArray();
        for (char uStrChar : cTextKwsOld) {
            String uStr = Character.toString(uStrChar);
            if (StringUtils.isEmpty(uStr)) {
                continue;
            }
            cTextKws.add(uStr.toLowerCase().trim());
        }
        // 初始化矩阵
        int[][] distanceM = new int[oTextKws.size() + 1][cTextKws.size() + 1];
        for (int row = 0; row <= oTextKws.size(); row++) {
            distanceM[row][0] = row;
        }
        for (int column = 0; column <= cTextKws.size(); column++) {
            distanceM[0][column] = column;
        }
        // 状态转移方程：上一个最优解到下个最优解，只需要删除，添加或者置换操作
        // d[i,j] = 0; if i =0 or j=0
        // d[i,j] = min(d[i][j-1]+1,d[i-1][j]+1,d[i-1][j-1]); if Xi = Yj
        // d[i,j] = min(d[i][j-1]+1,d[i-1][j]+1,d[i-1][j-1]+1); if Xi != Yj
        for (int i = 1; i <= oTextKws.size(); i++) {
            for (int j = 1; j <= cTextKws.size(); j++) {
                if (oTextKws.get(i - 1).equals(cTextKws.get(j - 1))) {
                    distanceM[i][j] =
                            getMinValue(distanceM[i - 1][j] + 1, distanceM[i][j - 1] + 1, distanceM[i - 1][j - 1]);
                } else {
                    distanceM[i][j] =
                            getMinValue(distanceM[i - 1][j] + 1, distanceM[i][j - 1] + 1, distanceM[i - 1][j - 1] + 1);
                }
            }
        }
        // 打印出矩阵
        /*
         * for (int i = 0; i <= oTextKws.size(); i++) { for (int j = 0; j <= cTextKws.size(); j++) {
         * System.out.print(distanceM[i][j] + ";"); } System.out.print("\n"); }
         */
        return distanceM[oTextKws.size()][cTextKws.size()];
    }

    // 比较大小的同时，保证了路径生成的稳定
    private Integer getMinValue(Integer a, Integer b, Integer c) {
        Integer rs = a;
        if (b < rs) {
            rs = b;
        }
        if (c < rs) {
            rs = c;
        }
        return rs;
    }

    // 通过文本编辑距离构建邻接矩阵
    public double[][] constructAdjecencyMatrixByEditDistance(List<String> extractPrjKws) {
        // List<String> extractPrjKws = new ArrayList<String>();
        double[][] adm = new double[extractPrjKws.size()][extractPrjKws.size()];
        // 邻接矩阵为对称矩阵，可以先只计算一半
        for (int row = 0; row < extractPrjKws.size(); row++) {
            for (int column = row; column < extractPrjKws.size(); column++) {
                if (row == column) {
                    adm[row][column] = 0;
                } else {
                    adm[row][column] = this.getTextEditDistance(extractPrjKws.get(row), extractPrjKws.get(column));
                    // adm[row][column] =this.getTextEditDistanceByContent(extractPrjKws.get(row),
                    // extractPrjKws.get(column));
                }
            }
        }
        // 将上三角矩阵填充至下三角矩阵
        for (int row = 0; row < extractPrjKws.size(); row++) {
            for (int column = 0; column < extractPrjKws.size(); column++) {
                if (row > column) {
                    adm[row][column] = adm[column][row];
                }
            }
        }
        for (int row = 0; row < extractPrjKws.size(); row++) {
            for (int column = 0; column < extractPrjKws.size(); column++) {
                if (String.valueOf(adm[row][column]).length() == 5) {
                    System.out.print(adm[row][column] + " ");
                } else if (String.valueOf(adm[row][column]).length() == 4) {
                    System.out.print(" " + adm[row][column] + " ");
                } else if (String.valueOf(adm[row][column]).length() == 3) {
                    System.out.print("  " + adm[row][column] + " ");
                }
            }
            System.out.println();
        }
        return adm;
    }

    @Override
    public void spetralClustering() throws Exception {
        List<String> prjCode = this.nsfcKwsTfCotfDao.getPrjCodeInfo(null);
        List<String> prjKwsInfo = this.nsfcKwsTfCotfDao.getPrjExtractKwsInfo();
        // 预设对200个项目划分到10组里边去
        double[][] dd = this.constructAdjecencyMatrixByEditDistance(prjKwsInfo);
        DenseDoubleMatrix2D ddm2 = new DenseDoubleMatrix2D(dd);
        try {
            // nsfcApplicationClusteringService.spetralClustering(ddm2, 4);
            nsfcApplicationClusteringService.spetralClusteringViaJama(ddm2, 2);
            NsfcApplicationClusteringServiceImpl.BALL_TREE = KmeansBallTree.getOneInstance(null);
            Entry<Integer[], Double> rs = nsfcApplicationClusteringService.nsfcCluster(2);
            this.nsfcKwsTfCotfDao.deleteGroupRs();
            System.out.println("min evaluation is =:" + rs.getValue());
            int i = 0;
            System.out.println("Clustering result as following : ");
            for (Integer group : rs.getKey()) {
                this.nsfcKwsTfCotfDao.insertIntoGroupRs(prjCode.get(i), group);
                System.out.print(i + " : " + group);
                System.out.println("====" + prjCode.get(i) + "====" + prjKwsInfo.get(i));
                i++;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DoubleMatrix2D dm2 = new DenseDoubleMatrix2D(3, 4);
        // matrix = new SparseDoubleMatrix2D(3,4); // 稀疏矩阵
        // matrix = new RCDoubleMatrix2D(3,4); // 稀疏行压缩矩阵
        System.out.println("初始矩阵");
        System.out.println(dm2);
        System.out.println("填充");
        dm2.assign(new double[][] {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}});
        System.out.println(dm2);
        System.out.println("转置");
        DoubleMatrix2D transpose = Algebra.DEFAULT.transpose(dm2);
        System.out.println(transpose);
        System.out.println("矩阵乘法");
        System.out.println(Algebra.DEFAULT.mult(dm2, transpose));
        String article1 =
                "体内;作用;修饰基因;其他;利用;功能;基因敲除;基因连锁;微卫星不稳定;机制研究;点突变;生物学效应;生物学特性;甲基化;相关基因;相关基因表达;磷酸化;致突变;药物敏感性;表型性状;表达变化;预后判断";
        String article2 =
                "体内;作用;修饰基因;其他;利用;功能;发生规律;基因修饰;基因修饰动物;生物学效应;生物学特性;甲基化;相关基因;相关基因表达;磷酸化;致突变;药物敏感性;表型性状;表达变化;预后判断";
        String article3 =
                "体内;作用;修饰基因;其他;利用;功能;发生规律;基因修饰;基因修饰动物;基因敲除;生物学效应;生物学特性;甲基化;相关基因;相关基因表达;磷酸化;致突变;药物敏感性;表型性状;表达变化;预后判断";
        String article4 =
                "体内;作用;修饰基因;其他;利用;功能;发生规律;基因修饰;基因修饰动物;基因敲除;基因连锁;点突变;生物学效应;生物学特性;甲基化;相关基因;相关基因表达;磷酸化;致突变;药物敏感性;表型性状;表达变化;预后判断";
        String article5 =
                "体内;作用;修饰基因;其他;利用;功能;发生规律;基因修饰;基因连锁;点突变;生物学效应;生物学特性;甲基化;相关基因;相关基因表达;磷酸化;致突变;药物敏感性;表型性状;表达变化;预后判断";
        String article6 =
                "体内;作用;修饰基因;其他;利用;功能;发生规律;基因修饰;基因修饰动物;基因敲除;基因连锁;点突变;生物学效应;生物学特性;甲基化;相关基因;相关基因表达;磷酸化;致突变;药物敏感性;表型性状;表达变化;预后判断";
        String article7 = "体内;作用;修饰基因;其他;利用;功能;相关基因表达;磷酸化;致突变;药物敏感性;表型性状;表达变化;预后判断";
        String article8 = "体内;作用;修饰基因;其他;利用;功能;发生规律;基因修饰;基因修饰动物;磷酸化;致突变;药物敏感性;表型性状;表达变化;预后判断";
        String article9 =
                "体内;作用;修饰基因;其他;利用;功能;发生规律;基因修饰;基因修饰动物;基因敲除;基因连锁;点突变;生物学效应;生物学特性;甲基化;相关基因;相关基因表达;磷酸化;致突变;药物敏感性;表型性状;表达变化;预后判断";
        String article10 = "体内;作用;修饰基因;其他;基因连锁;点突变;生物学效应;生物学特性;甲基化;相关基因;相关基因表达;磷酸化;致突变;药物敏感性";
        List<String> extractPrjKws = new ArrayList<String>();
        extractPrjKws.add(article1);
        extractPrjKws.add(article2);
        extractPrjKws.add(article3);
        extractPrjKws.add(article4);
        extractPrjKws.add(article5);
        extractPrjKws.add(article6);
        extractPrjKws.add(article7);
        extractPrjKws.add(article8);
        extractPrjKws.add(article9);
        extractPrjKws.add(article10);
        NsfcApplicationAutoAssignServiceImpl naa = new NsfcApplicationAutoAssignServiceImpl();
        NsfcApplicationClusteringServiceImpl nacsl = new NsfcApplicationClusteringServiceImpl();
        // int distance = naa.getTextEditDistance(article1, article2);
        // System.out.println("Edit distance is : " + distance);
        double[][] dd = naa.constructAdjecencyMatrixByEditDistance(extractPrjKws);
        DenseDoubleMatrix2D ddm2 = new DenseDoubleMatrix2D(dd);
        try {
            nacsl.spetralClusteringViaColt(ddm2, 4);
            nacsl.BALL_TREE = KmeansBallTree.getOneInstance(null);
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
