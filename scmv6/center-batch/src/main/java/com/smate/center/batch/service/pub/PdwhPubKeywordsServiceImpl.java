package com.smate.center.batch.service.pub;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.prj.NsfcKwsTfCotfDao;
import com.smate.center.batch.dao.sns.pub.PdwhPubKeywordsCategoryDao;
import com.smate.center.batch.model.sns.pub.PdwhPubKeywordsCategory;
import com.smate.center.batch.util.pub.MessageDigestUtils;

@Service("pdwhPubKeywordsService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubKeywordsServiceImpl implements PdwhPubKeywordsService {
    @Autowired
    private PdwhPubKeywordsCategoryDao pdwhPubKeywordsCategoryDao;
    @Autowired
    private NsfcKwsTfCotfDao nsfcKwsTfCotfDao;

    @Override
    public List<PdwhPubKeywordsCategory> getPdwhPubKeywords(Integer size, Long startPubId, Long endPubId) {
        return pdwhPubKeywordsCategoryDao.getPdwhPubKeywords(size, startPubId, endPubId);
    }

    @Override
    public void saveOpResult(Long pubId, int status) {
        pdwhPubKeywordsCategoryDao.saveOpResult(pubId, status);
    }

    @Override
    public void saveOpListResult(List<Long> pubIdList, int status) {
        pdwhPubKeywordsCategoryDao.saveOpListResult(pubIdList, status);
    }

    @Override
    public StringBuilder conbinePubKeywords(Long categoryId, Set<String> keywordsSet, StringBuilder strBuilder) {
        if (keywordsSet != null && keywordsSet.size() > 0) {
            List<String> zhkeywordsList = new ArrayList<>(keywordsSet);
            for (int i = 0; i < zhkeywordsList.size() - 1; i++) {
                strBuilder.append(MessageDigestUtils.messageDigest(categoryId + zhkeywordsList.get(i)));
                strBuilder.append(" ");
                for (int j = i + 1; j < zhkeywordsList.size(); j++) {
                    strBuilder.append(MessageDigestUtils
                            .messageDigest(categoryId + zhkeywordsList.get(i) + zhkeywordsList.get(j)));
                    strBuilder.append(" ");
                }
            }
            strBuilder.append(MessageDigestUtils
                    .messageDigest(categoryId + zhkeywordsList.get(zhkeywordsList.size() - 1).toString()));
            strBuilder.append(" ");
        }
        return strBuilder;
    }

    @Override
    public TreeSet<String> handlePubKeywords(String keywords) {
        TreeSet<String> treeSet = new TreeSet<String>();
        String[] keywordsString = keywords.toLowerCase().split(";");
        for (String keyword : keywordsString) {
            String kw = keyword.replaceAll("\\s+", " ").trim();
            treeSet.add(MessageDigestUtils.messageDigest(kw));
        }
        return treeSet;
    }

    @Override
    public TreeSet<String> getSubsets(List<String> kwList, Integer size) {
        if (size == null) {
            return null;
        }
        switch (size) {
            case 2:
                return this.getSubsetsLengthTwo(kwList);
            case 3:
                return this.getSubsetsLengthThree(kwList);
            case 4:
                return this.getSubsetsLengthFour(kwList);
            case 5:
                return this.getSubsetsLengthFive(kwList);
            default:
                return null;
        }
    }

    @Override
    public TreeSet<String> getAllSubsets(List<String> kwList) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        TreeSet<String> all = new TreeSet<String>();
        TreeSet<String> subSet2 = this.getSubsetsLengthTwo(kwList);
        if (subSet2 != null && subSet2.size() > 0) {
            all.addAll(subSet2);
        }
        TreeSet<String> subSet3 = this.getSubsetsLengthThree(kwList);
        if (subSet3 != null && subSet3.size() > 0) {
            all.addAll(subSet3);
        }
        TreeSet<String> subSet4 = this.getSubsetsLengthFour(kwList);
        if (subSet4 != null && subSet4.size() > 0) {
            all.addAll(subSet4);
        }
        TreeSet<String> subSet5 = this.getSubsetsLengthFive(kwList);
        if (subSet5 != null && subSet5.size() > 0) {
            all.addAll(subSet5);
        }
        return all;
    }

    /*
     * 计算长度为5的子集,需要提前去重排序
     * 
     */
    private TreeSet<String> getSubsetsLengthFive(List<String> kwList) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Integer length = kwList.size();
        TreeSet<String> subSetString = new TreeSet<String>();
        for (int k = 0; k < length - 4; k++) {
            for (int k1 = k + 1; k1 < length - 3; k1++) {
                for (int k2 = k1 + 1; k2 < length - 2; k2++) {
                    for (int k3 = k2 + 1; k3 < length - 1; k3++) {
                        for (int k4 = k3 + 1; k4 < length; k4++) {
                            StringBuffer sb = new StringBuffer();
                            sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append(";")
                                    .append(kwList.get(k2)).append(";").append(kwList.get(k3)).append(";")
                                    .append(kwList.get(k4));
                            subSetString.add(sb.toString());
                        }
                    }
                }
            }
        }
        return subSetString;
    }

    /*
     * 计算长度为4的子集,需要提前去重排序
     * 
     */
    private TreeSet<String> getSubsetsLengthFour(List<String> kwList) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Integer length = kwList.size();
        TreeSet<String> subSetString = new TreeSet<String>();
        for (int k = 0; k < length - 3; k++) {
            for (int k1 = k + 1; k1 < length - 2; k1++) {
                for (int k2 = k1 + 1; k2 < length - 1; k2++) {
                    for (int k3 = k2 + 1; k3 < length; k3++) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append(";").append(kwList.get(k2))
                                .append(";").append(kwList.get(k3));
                        subSetString.add(sb.toString());
                    }
                }
            }
        }
        return subSetString;
    }

    /*
     * 计算长度为3的子集,需要提前去重排序
     * 
     */
    private TreeSet<String> getSubsetsLengthThree(List<String> kwList) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Integer length = kwList.size();
        TreeSet<String> subSetString = new TreeSet<String>();
        for (int k = 0; k < length - 2; k++) {
            for (int k1 = k + 1; k1 < length - 1; k1++) {
                for (int k2 = k1 + 1; k2 < length; k2++) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append(";").append(kwList.get(k2));
                    subSetString.add(sb.toString());
                }
            }
        }
        return subSetString;
    }

    /*
     * 计算长度为2的子集,需要提前去重排序
     * 
     */
    private TreeSet<String> getSubsetsLengthTwo(List<String> kwList) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Integer length = kwList.size();
        TreeSet<String> subSetString = new TreeSet<String>();
        for (int k = 0; k < length - 1; k++) {
            for (int k1 = k + 1; k1 < length; k1++) {
                StringBuffer sb = new StringBuffer();
                sb.append(kwList.get(k)).append(";").append(kwList.get(k1));
                subSetString.add(sb.toString());
            }
        }
        return subSetString;
    }


    // 尝试解决新建类过多gc的问题
    @Override
    public Map<String, Object> getAllSubsetsString(List<String> kwList, String discode, Long pubId) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Map<String, Object> rsMap = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer();
        Long num2 = this.getSubsetsStrLengthTwo(kwList, sb, discode, pubId);
        // System.out.println("长度2子集数为: " + num2);
        Long num3 = this.getSubsetsStrLengthThree(kwList, sb, discode, pubId);
        // System.out.println("长度3子集数为: " + num3);
        Long num4 = this.getSubsetsStrLengthFour(kwList, sb, discode, pubId);
        // System.out.println("长度4子集数为: " + num4);
        Long num5 = this.getSubsetsStrLengthFive(kwList, sb, discode, pubId);
        // System.out.println("长度5子集数为: " + num5);
        // System.out.println("总共子集数为: " + (num2 + num3 + num4 + num5));
        rsMap.put("subSetNum", num2 + num3 + num4 + num5);
        rsMap.put("subSetString", sb.toString());
        return rsMap;
    }

    /*
     * 计算长度为5的子集,需要提前去重排序
     * 
     */
    private Long getSubsetsStrLengthFive(List<String> kwList, StringBuffer sb, String discode, Long pubId) {
        Long subSetNum = 0L;
        if (kwList == null || kwList.size() <= 0) {
            return subSetNum;
        }
        Integer length = kwList.size();
        for (int k = 0; k < length - 4; k++) {
            for (int k1 = k + 1; k1 < length - 3; k1++) {
                for (int k2 = k1 + 1; k2 < length - 2; k2++) {
                    for (int k3 = k2 + 1; k3 < length - 1; k3++) {
                        for (int k4 = k3 + 1; k4 < length; k4++) {
                            if (pubId != null) {
                                sb.append(pubId.toString()).append("-!");
                            }
                            if (StringUtils.isNotEmpty(discode)) {
                                sb.append(discode).append("-!");
                            }
                            sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append(";")
                                    .append(kwList.get(k2)).append(";").append(kwList.get(k3)).append(";")
                                    .append(kwList.get(k4)).append("\r\n");
                            subSetNum++;
                        }
                    }
                }
            }
        }
        return subSetNum;
    }

    /*
     * 计算长度为4的子集,需要提前去重排序
     * 
     */
    private Long getSubsetsStrLengthFour(List<String> kwList, StringBuffer sb, String discode, Long pubId) {
        Long subSetNum = 0L;
        if (kwList == null || kwList.size() <= 0) {
            return subSetNum;
        }
        Integer length = kwList.size();
        for (int k = 0; k < length - 3; k++) {
            for (int k1 = k + 1; k1 < length - 2; k1++) {
                for (int k2 = k1 + 1; k2 < length - 1; k2++) {
                    for (int k3 = k2 + 1; k3 < length; k3++) {
                        if (pubId != null) {
                            sb.append(pubId.toString()).append("-!");
                        }
                        if (StringUtils.isNotEmpty(discode)) {
                            sb.append(discode).append("-!");
                        }
                        sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append(";").append(kwList.get(k2))
                                .append(";").append(kwList.get(k3)).append("\r\n");
                        subSetNum++;
                    }
                }
            }
        }
        return subSetNum;
    }

    /*
     * 计算长度为3的子集,需要提前去重排序
     * 
     */
    private Long getSubsetsStrLengthThree(List<String> kwList, StringBuffer sb, String discode, Long pubId) {
        Long subSetNum = 0L;
        if (kwList == null || kwList.size() <= 0) {
            return subSetNum;
        }
        Integer length = kwList.size();
        for (int k = 0; k < length - 2; k++) {
            for (int k1 = k + 1; k1 < length - 1; k1++) {
                for (int k2 = k1 + 1; k2 < length; k2++) {
                    if (pubId != null) {
                        sb.append(pubId.toString()).append("-!");
                    }
                    if (StringUtils.isNotEmpty(discode)) {
                        sb.append(discode).append("-!");
                    }
                    sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append(";").append(kwList.get(k2))
                            .append("\r\n");
                    subSetNum++;
                }
            }
        }
        return subSetNum;
    }

    /*
     * 计算长度为2的子集,需要提前去重排序
     * 
     */
    private Long getSubsetsStrLengthTwo(List<String> kwList, StringBuffer sb, String discode, Long pubId) {
        Long subSetNum = 0L;
        if (kwList == null || kwList.size() <= 0) {
            return subSetNum;
        }
        Integer length = kwList.size();
        for (int k = 0; k < length - 1; k++) {
            for (int k1 = k + 1; k1 < length; k1++) {
                if (pubId != null) {
                    sb.append(pubId.toString()).append("-!");
                }
                if (StringUtils.isNotEmpty(discode)) {
                    sb.append(discode).append("-!");
                }
                sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append("\r\n");
                subSetNum++;
            }
        }
        return subSetNum;
    }

    class myTask implements Runnable {
        Integer taskName;
        StringBuffer sb;
        CountDownLatch latch;

        public myTask(Integer taskName, StringBuffer sb, CountDownLatch latch) {
            this.taskName = taskName;
            this.sb = sb;
            this.latch = latch;
        }

        public Integer getTaskName() {
            return taskName;
        }

        public void setTaskName(Integer taskName) {
            this.taskName = taskName;
        }

        public StringBuffer getSb() {
            return sb;
        }

        public void setSb(StringBuffer sb) {
            this.sb = sb;
        }


        public CountDownLatch getLatch() {
            return latch;
        }

        public void setLatch(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            String name = String.valueOf(this.taskName);
            String type = name.substring(name.length() - 1);
            String filePath = "C:/Users/Administrator/Desktop/20181015open/pubtestfile/pubtest_";
            String fileName = filePath + type + ".txt";
            File file = new File(fileName);
            if (!file.exists()) {
                sb.append(name);
                sb.append("---文件不存在，文件名为：");
                sb.append(fileName);
                sb.append("\r\n");
                return;
            }

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                try {
                    String readline = "";
                    while ((readline = br.readLine()) != null) {
                        /*
                         * Long slp = new Random().nextInt(10) * 1L; Thread.sleep(slp); System.out.println(name +
                         * "任务读取数据：" + readline + "====等待时间: " + slp);
                         */
                        sb.append(name);
                        sb.append("---");
                        sb.append(readline);
                        sb.append("\r\n");
                    }
                } catch (Exception e) {
                    throw new Exception(e);
                } finally {
                    br.close();
                }
            } catch (Exception e) {
                sb.append(name);
                sb.append("---文件写入内存出错，文件名为：");
                sb.append(fileName);
                sb.append("\r\n");
            }
            System.out.println(name + "任务完成=====");
            latch.countDown();
        }
    }

    /*
     * 计算长度为3的子集,需要提前去重排序,且包含特定关键词
     * 
     */
    public TreeSet<String> getSubsetsLengthThree(List<String> kwList, String includedKw) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Integer length = kwList.size();
        TreeSet<String> subSetString = new TreeSet<String>();
        for (int k = 0; k < length - 2; k++) {
            for (int k1 = k + 1; k1 < length - 1; k1++) {
                for (int k2 = k1 + 1; k2 < length; k2++) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(kwList.get(k)).append(";").append(kwList.get(k1)).append(";").append(kwList.get(k2));
                    subSetString.add(sb.toString());
                }
            }
        }
        return subSetString;
    }

    /*
     * 计算长度为2的子集,需要提前去重排序
     * 
     */
    public TreeSet<String> getSubsetsLengthTwo(List<String> kwList, String includedKw) {
        if (kwList == null || kwList.size() <= 0) {
            return null;
        }
        Integer length = kwList.size();
        TreeSet<String> subSetString = new TreeSet<String>();
        for (int k = 0; k < length - 1; k++) {
            for (int k1 = k + 1; k1 < length; k1++) {
                StringBuffer sb = new StringBuffer();
                sb.append(kwList.get(k)).append(";").append(kwList.get(k1));
                subSetString.add(sb.toString());
            }
        }
        return subSetString;
    }

    /*
     * 计算长度为2的子集,需要提前去重排序,同时得到包含相关关键词的子集
     * 
     */
    @Override
    public TreeSet<String> getSubsetsLengthTwo(List<String> kwList, String kw, List<String> relatedKws) {
        if (kwList == null || kwList.size() <= 0 || StringUtils.isEmpty(kw)) {
            return null;
        }
        Collections.sort(kwList);
        relatedKws.add(kw);
        Integer length = kwList.size();
        TreeSet<String> subSetString = new TreeSet<String>();
        for (int k = 0; k < length - 1; k++) {
            for (int k1 = k + 1; k1 < length; k1++) {
                StringBuffer sb = new StringBuffer();
                if (kw.equalsIgnoreCase(kwList.get(k)) || kw.equalsIgnoreCase(kwList.get(k1))) {
                    sb.append(kwList.get(k)).append(";").append(kwList.get(k1));
                    subSetString.add(sb.toString());
                }
            }
        }
        // 使用知识库来过滤没有关系的关键词,保留对应顺序
        List<Object> kwsCotf2 = nsfcKwsTfCotfDao.getGroupedKws(null, subSetString);
        if (kwsCotf2 == null || kwsCotf2.size() <= 0) {
            return null;
        }
        for (Object kwStr : kwsCotf2) {
            if (((Object[]) kwStr)[0] != null) {
                String[] kws = ((String) ((Object[]) kwStr)[0]).split(";");
                if (kws.length == 2 && StringUtils.isNotEmpty(kws[0]) && StringUtils.isNotEmpty(kws[1])) {
                    if (!(kw.equalsIgnoreCase(kws[0]))) {
                        relatedKws.add(kws[0]);
                    }
                    if (!(kw.equalsIgnoreCase(kws[1]))) {
                        relatedKws.add(kws[1]);
                    }
                }
            }
        }
        return subSetString;
    }

    // 循环求所有组关键词的相关关键词，直到相关关键词数不再增加
    @Override
    public TreeSet<String> getGroupKwsByCotfTwo(List<String> kwList, String kw, List<String> relatedKws, Integer counts,
            List<Object> kwsCotf) {
        if (kwList == null || kwList.size() <= 0 || StringUtils.isEmpty(kw)) {
            return null;
        }
        Integer i = counts;
        Collections.sort(kwList);
        if (i == 0) {
            relatedKws.add(kw);
        }
        Integer length = kwList.size();
        TreeSet<String> subSetString = new TreeSet<String>();
        List<Object> kwsCotf2 = kwsCotf;
        // 只需要计算一次
        if (kwsCotf2 == null || kwsCotf2.size() <= 0) {
            for (int k = 0; k < length - 1; k++) {
                for (int k1 = k + 1; k1 < length; k1++) {
                    StringBuffer sb = new StringBuffer();
                    if (kw.equalsIgnoreCase(kwList.get(k)) || kw.equalsIgnoreCase(kwList.get(k1))) {
                        sb.append(kwList.get(k)).append(";").append(kwList.get(k1));
                        subSetString.add(sb.toString());
                    }
                }
            }
            // 使用知识库来过滤没有关系的关键词,保留对应顺序
            kwsCotf2 = nsfcKwsTfCotfDao.getGroupedKws(null, subSetString);
            if (kwsCotf2 == null || kwsCotf2.size() <= 0) {
                return null;
            }
        }

        for (Object kwStr : kwsCotf2) {
            if (((Object[]) kwStr)[0] != null) {
                String[] kws = ((String) ((Object[]) kwStr)[0]).split(";");
                if (kws.length == 2 && StringUtils.isNotEmpty(kws[0]) && StringUtils.isNotEmpty(kws[1])) {
                    if (!(kw.equalsIgnoreCase(kws[0])) && !(relatedKws.contains(kws[0]))) {
                        relatedKws.add(kws[0]);
                    }
                    if (!(kw.equalsIgnoreCase(kws[1])) && !(relatedKws.contains(kws[1]))) {
                        relatedKws.add(kws[1]);
                    }
                }
                /*
                 * TreeSet<String> relatedKwsTree = new TreeSet<String>(); for (String relatedkwStr : relatedKws) {
                 * relatedKwsTree.add(relatedkwStr); }
                 */
            }
        }
        i++;
        if (i <= (relatedKws.size() - 1)) {
            // return getGroupKwsByCotfTwo(kwList, relatedKws.get(i), relatedKws, i, kwsCotf2);
            System.out.println("第" + i + "次循环");
            System.out.println("相关词长度为：" + relatedKws.size());
            System.out.println("===============================");
            return getGroupKwsByCotfTwo(kwList, relatedKws.get(i), relatedKws, i, null);
        } else {
            // 去重
            return subSetString;
        }
    }

    public static void main(String[] args) {
        PdwhPubKeywordsServiceImpl pp = new PdwhPubKeywordsServiceImpl();

        String cStr =
                "Karib’ l Watar; Agatharchides de Cnide; aromates; nabaté en; Claude Ptolé mé e; pè lerinage; dromadaire; chameau; oasis; itiné raire; Pline l’ Ancien; Strabon; é conomie; Arché ologie; commerce; caravanes; Etat; miné en; royaume; Sabé en; Pé riple de la mer é rythré e; qatabanite; awsanite; hadramite; Darb Zubayda; K fa; Sahl Rukba; Mé diterrané e; Macoraba; La Mecque; ‘ Ukaz; Yathrib; Buwat; Umm Darb; Ra’ s Karkuma; Leukê K mê Dedan; al-Khurayba; al-‘ Ula; Hijra; Hé djaz; Tabala; &lsquo";
        String cStr1 =
                "Hybridization chain reaction; Isothermal amplification; Nanotechnology; Biosensor; Ultrahigh sensitivity; AbbreviationsAFP; alpha-fetoprotein; AgNPs; silver nanoparticles; ATP; adenosine triphosphate; AuNPs; gold nanoparticles; cDNA; complementary DNA; CEA; carcinoembryonic antigen; CHA; catalytic hairpin assembly; Con A; concanavalin A; CuNPs; copper nanoparticles; Cy5; cyanine-5; DOX-D; doxorubicin-modified dextran; dsDNA; double-stranded DNA; ECL; electrochemiluminescence; ELISA; enzyme-linked immunosorbent assay; FITC; fluorescein isothiocyanate; GOD; glucose oxidase; GQDs; graphene quantum dots; HCR; hybridization chain reaction; HDA; helicase-dependent amplification; HQ; hydroquinone; HRP; horseradish peroxidase; LAMP; loop-mediated isothermal amplification; ICP-MS; inductively coupled plasma mass spectrometry; LNA; locked nucleic acid; LOD; limit of detection; LSPR; localized surface plasmon resonance; mAb; monoclonal antibody; MB; methylene blue; miRNA; microRNA; MMPs; magnetic microparticles; MNPs; magnetic nanoparticles; mRNA; messenger RNA; MTase; methyltransferase; NASBA; nucleic acid sequence-based amplification; PAA; polyacrylic acid; PAH; poly(allylamine hydrochloride); PCR; polymerase chain reaction; PDGF-BB; platelet-derived growth factor BB; PNA; peptide nucleic acid; PSA; prostate specific antigen; PtNCs; Pt nanoclusters; R6G; rhodamine 6G; RCA; rolling circle amplification";
        String cStr2 =
                "Hybridization chain reaction; Isothermal amplification; Nanotechnology; Biosensor; Ultrahigh sensitivity; AbbreviationsAFP; alpha-fetoprotein; AgNPs; silver nanoparticles; ATP; adenosine triphosphate; AuNPs; gold nanoparticles; cDNA; complementary DNA; CEA; carcinoembryonic antigen; CHA; catalytic hairpin assembly; Con A; concanavalin A; CuNPs; copper nanoparticles; Cy5; cyanine-5; DOX-D; doxorubicin-modified dextran; dsDNA; double-stranded DNA; ECL; electrochemiluminescence; ELISA; enzyme-linked immunosorbent assay; FITC; fluorescein isothiocyanate; GOD; glucose oxidase; GQDs; graphene quantum dots; HCR; hybridization chain reaction; HDA; helicase-dependent amplification; HQ; hydroquinone; HRP; horseradish peroxidase; LAMP; loop-mediated isothermal amplification; ICP-MS; inductively coupled plasma mass spectrometry; LNA; locked nucleic acid; LOD; limit of detection; LSPR; localized surface plasmon resonance; mAb; monoclonal antibody; MB; methylene blue; miRNA; microRNA; MMPs; magnetic microparticles; MNPs; magnetic nanoparticles; mRNA; messenger RNA; MTase; methyltransferase; NASBA; nucleic acid sequence-based amplification; PAA; polyacrylic acid; PAH; poly(allylamine hydrochloride); PCR; polymerase chain reaction; PDGF-BB; platelet-derived growth factor BB;";

        Date start1 = new Date();
        System.out.println("关键词子集计算开始: " + start1);
        String[] c = cStr2.split(";");
        List<String> kwList = new ArrayList(Arrays.asList(c).subList(0, 60));
        Long size = (Long) pp.getAllSubsetsString(kwList, null, 10086L).get("subSetNum");
        String subsetString = (String) pp.getAllSubsetsString(kwList, null, 10086L).get("subSetString");
        System.out.println("总共子集数为: " + size);
        Date middle = new Date();
        if (kwList.size() > 15) {
            System.out.println("关键词长度为" + kwList.size() + ",其子集计算开始时间: " + start1 + ", 结束时间: " + middle + ", 计算用时: "
                    + (middle.getTime() - start1.getTime()));
        }

        try {
            File f1 = new File("C:/Users/Administrator/Desktop/20181015open/pubtestfile/rs.txt");
            if (!f1.exists()) {
                f1.createNewFile();
            }
            BufferedWriter newBw = new BufferedWriter(new FileWriter(f1, true));
            newBw.write(subsetString);
            newBw.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        File rsf = new File("C:/Users/Administrator/Desktop/20181015open/pubtestfile/rs.txt");
        if (rsf.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(rsf));
                Long count = 0L;
                String str;
                while ((str = br.readLine()) != null) {
                    /*
                     * if (StringUtils.isEmpty(str)) { continue; }
                     */
                    if (count < 1000) {
                        System.out.println("子集" + str);
                    }
                    count++;
                }
                System.out.println("最终写入行数为rowCounts=" + count);
                br.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        /*
         * Integer sum = 0; ThreadPoolExecutor tpe = new ThreadPoolExecutor(15, 25, 100L,
         * TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(20000)); Date start = new Date(); while
         * (sum < 1000) { Integer n = 2000; CountDownLatch latch = new CountDownLatch(n); Integer counts =
         * 0; StringBuffer sb = new StringBuffer(); while (counts < n) { tpe.execute(pp.new myTask(counts,
         * sb, latch)); System.out.println("=======第" + counts + "写入任务开始======="); counts++; }
         * 
         * try { latch.await(); System.out.println("写入任务开始======="); File f = new
         * File("C:/Users/Administrator/Desktop/20181015open/pubtestfile/rs.txt"); if (!f.exists()) {
         * f.createNewFile(); } BufferedWriter bw = new BufferedWriter(new FileWriter(f, true)); try {
         * bw.write(sb.toString()); bw.flush(); } catch (Exception e) { e.printStackTrace(); } finally {
         * bw.close(); } } catch (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
         * sum++; System.out.println("======OUT循环，第" + sum + "写入任务完毕=======");
         * 
         * String[] str = {"1", "2", "3", "4", "5", "6", "7", "8", "9"}; TreeSet<String> st =
         * pp.getSubsets(Arrays.asList(str), null); Integer num = 0; for (String s : st) { //
         * System.out.println(s); num++; } System.out.println(num);
         * 
         * } tpe.shutdown(); System.out.println("=======所有写入任务完毕=======");
         * 
         * try { BufferedReader rb = new BufferedReader( new FileReader(new
         * File("C:/Users/Administrator/Desktop/20181015open/pubtestfile/rs.txt"))); int rowCounts = 0;
         * while (rb.readLine() != null) { rowCounts++; } rb.close(); Date end = new Date();
         * System.out.println("最终写入行数为rowCounts=" + rowCounts + ", 用时为" + (end.getTime() - start.getTime())
         * / 1000); } catch (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
         */
    }
}
