package com.smate.center.batch.quartz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.prj.NsfcCategoryForKwdicUpdate;
import com.smate.center.batch.model.pdwh.prj.NsfcKwTfCotfForSorting;
import com.smate.center.batch.service.TaskMarkerService;
import com.smate.center.batch.service.pdwh.prj.NsfcKeywordsService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * 4个来源：1.学科主任维护；2.评议人关键词；3.申请书关键词；4.结题项目关联成果关键词
 * 
 * 
 **/
public class NsfcBaseKeywordsTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final static Integer SIZE = 5000; // 每次刷新获取的个数

    @Autowired
    private TaskMarkerService taskMarkerService;
    @Autowired
    private NsfcKeywordsService nsfcKeywordsService;


    public void run() throws BatchTaskException {
        logger.debug("====================================NsfcBaseKeywordsTask===开始运行");
        if (isRun() == false) {
            logger.debug("====================================NsfcBaseKeywordsTask===开关关闭");
            return;
        } else {
            try {
                doRun();
            } catch (BatchTaskException e) {
                logger.error("NsfcBaseKeywordsTask,运行异常", e);
            }
        }
    }

    public void doRun() throws BatchTaskException {
        List<NsfcCategoryForKwdicUpdate> toHandleList = this.nsfcKeywordsService.getNsfcDisciplineList(0, 7, SIZE);
        if (toHandleList == null || toHandleList.size() == 0) {
            logger.info("=====NsfcBaseKeywordsTask处理完毕=====");
            return;
        }
        for (NsfcCategoryForKwdicUpdate nfku : toHandleList) {
            try {
                // 加载对应学科下的文本信息
                Map<String, Map<Integer, Double>> kwsTfMap = new HashMap<String, Map<Integer, Double>>();
                // 加载学科项目自填关键词
                List<Map<String, Object>> prjInfoList =
                        this.nsfcKeywordsService.getNsfcPrjInfo(nfku.getNsfcApplicationCode(), 1);
                this.getKwTfByWeight(kwsTfMap, prjInfoList, 2);
                // 加载学科申请书自填关键词
                List<Map<String, Object>> prpInfoList =
                        this.nsfcKeywordsService.getNsfcPrjInfo(nfku.getNsfcApplicationCode(), 0);
                this.getKwTfByWeight(kwsTfMap, prpInfoList, 3);
                // 先获取对应学科下所有项目资助号， 加载学科项目关联成果自填关键词
                List<Map<String, Object>> pubInfoList =
                        this.nsfcKeywordsService.getNsfcPrjRelatedPub(nfku.getNsfcApplicationCode());
                this.getKwTfByWeight(kwsTfMap, pubInfoList, 4);
                // 将学科主任维护关键词全部加入词库
                List<String> discKwList = this.nsfcKeywordsService.getNsfcDisciplineKw(nfku.getNsfcApplicationCode());
                this.markKwFromNsfcDiscipline(kwsTfMap, discKwList, 1);
                this.nsfcKeywordsService.saveNsfcKws(kwsTfMap, nfku.getNsfcApplicationCode());
                this.nsfcKeywordsService.saveNsfcCategoryForKwdicUpdate(nfku, 1);
            } catch (Exception e) {
                logger.error("计算NSFC关键词出错，对应学部为" + nfku.getNsfcApplicationCode(), e);
                this.nsfcKeywordsService.saveNsfcCategoryForKwdicUpdate(nfku, 3);
            }
        }
    }

    public boolean isRun() throws BatchTaskException {
        // 任务开关控制逻辑
        return true;
        // return taskMarkerService.getApplicationQuartzSettingValue("PubToPubsimpleTask2") == 1;
    }

    // type=1：学科主任维护，type=2：批准项目，type=3: 申请书，type=4:结题项目成果，type=5：评议人关键词
    private void getKwTfByWeight(Map<String, Map<Integer, Double>> kwsTfMap, List<Map<String, Object>> infoList,
            Integer Type) {
        if (infoList == null || infoList.size() <= 0) {
            return;
        }
        for (Map<String, Object> info : infoList) {
            String zhKws = info.get("ZH_KWS") == null ? "" : (String) info.get("ZH_KWS");
            String enKws = info.get("EN_KWS") == null ? "" : (String) info.get("EN_KWS");
            if (StringUtils.isBlank(enKws) && StringUtils.isBlank(zhKws)) {
                continue;
            }
            // 如果无年份，默认为9999
            Integer year = 9999;
            if (info.get("YEAR") != null) {
                year = ((BigDecimal) info.get("YEAR")).intValue();
            }
            Double wScore = 1.0;
            switch (Type) {
                case 1:
                    year = 99999; // 标记为学科主任维护关键词
                    break;
                case 2:
                    wScore = wScore * 1.6;
                    break;
                case 3:
                    wScore = wScore * 0.8;
                    break;
                case 4:
                    wScore = wScore * 0.2;
                    break;
                default:
                    return;
            }
            // 中文
            if (!StringUtils.isBlank(zhKws)) {
                TreeSet<String> zhkwSet = this.cleanKws(zhKws);
                for (String zhKw : zhkwSet) {
                    if (kwsTfMap.get(zhKw) == null) {
                        Map<Integer, Double> yearTf = new HashMap<Integer, Double>();
                        yearTf.put(year, wScore);
                        kwsTfMap.put(zhKw, yearTf);
                    } else if (kwsTfMap.get(zhKw).get(year) == null) {
                        kwsTfMap.get(zhKw).put(year, wScore);
                    } else {
                        // 学科主任关键词，只标记，不计入wtf的计算
                        if (year == 99999) {
                            kwsTfMap.get(zhKw).put(year, 1.0);
                        } else {
                            Double oTf = kwsTfMap.get(zhKw).get(year) + wScore;
                            System.out.println("计算中文关键词为: " + zhKw + " ，年份：" + year + "，目前得分：oTf = " + oTf + "="
                                    + kwsTfMap.get(zhKw).get(year) + "+" + wScore + "====");
                            kwsTfMap.get(zhKw).put(year, oTf);
                        }
                    }
                }
            }
            // 英文
            if (!StringUtils.isBlank(enKws)) {
                TreeSet<String> enkwSet = this.cleanKws(enKws);
                for (String enKw : enkwSet) {
                    if (kwsTfMap.get(enKw) == null) {
                        Map<Integer, Double> yearTf = new HashMap<Integer, Double>();
                        yearTf.put(year, wScore);
                        kwsTfMap.put(enKw, yearTf);
                    } else if (kwsTfMap.get(enKw).get(year) == null) {
                        kwsTfMap.get(enKw).put(year, wScore);
                    } else {
                        // 学科主任关键词，只标记，不计入wtf的计算
                        if (year == 99999) {
                            kwsTfMap.get(enKw).put(year, 1.0);
                        } else {
                            Double oTf = kwsTfMap.get(enKw).get(year) + wScore;
                            System.out.println("计算英文关键词为: " + enKw + " ，年份：" + year + "，目前得分：oTf = " + oTf + "="
                                    + kwsTfMap.get(enKw).get(year) + "+" + wScore + "====");
                            kwsTfMap.get(enKw).put(year, oTf);
                        }
                    }
                }
            }

        }
    }

    // 标记学科主任维护关键词
    private void markKwFromNsfcDiscipline(Map<String, Map<Integer, Double>> kwsTfMap, List<String> kwList,
            Integer Type) {
        if (kwList == null || kwList.size() <= 0) {
            return;
        }
        for (String kw : kwList) {
            if (StringUtils.isBlank(kw)) {
                continue;
            }
            // 学科主任关键词，只标记，不计入wtf的计算
            Integer year = 99999;
            kw = XmlUtil.cToe(kw);
            if (kwsTfMap.get(kw) == null) {
                Map<Integer, Double> yearTf = new HashMap<Integer, Double>();
                yearTf.put(year, 1.0);
                kwsTfMap.put(kw, yearTf);
            } else {
                kwsTfMap.get(kw).put(year, 1.0);
            }
        }
    }

    // 分割去重, 过滤特殊符号
    private TreeSet<String> cleanKws(String kwStr) {
        TreeSet<String> kwsSet = new TreeSet<String>();
        kwStr = kwStr.toLowerCase();
        kwStr = XmlUtil.cToe(kwStr);
        String[] kws = kwStr.split(";");
        if (kws == null || kws.length <= 0) {
            kws = kwStr.split(",");
            if (kws == null || kws.length <= 0) {
                return null;
            }
        }
        // 处理关键词中的括号
        TreeSet<String> newKwsSet = new TreeSet<String>();
        for (String kw : kws) {
            if (StringUtils.isBlank(kw)) {
                continue;
            }
            if (kw.indexOf("(") < 0 && kw.indexOf("[") < 0 && kw.indexOf("{") < 0) {
                kw = this.cleanKw(kw);
                newKwsSet.add(kw);
            } else {
                Set<String> subKws = this.nsfcKeywordsService.getKwFromBracket(kw);
                if (subKws != null && subKws.size() > 0) {
                    for (String subkw : subKws) {
                        if (StringUtils.isNotBlank(subkw)) {
                            subkw = this.cleanKw(subkw);
                            newKwsSet.add(subkw);
                        }
                    }
                }
            }
        }
        return newKwsSet;
    }

    private String cleanKw(String kw) {
        return kw.replaceAll("\n", " ").replaceAll("\r", " ").replaceAll("(&nbsp;)+", " ").replaceAll("&amp;", "&")
                .replaceAll("\\-+", "-").replaceAll("\\s*\\.\\s*", ".").replaceAll("\\s*\\-\\s*", "-")
                .replaceAll("\\,+", ",").replaceAll(";+", ";").replaceAll("\\.+", ".").replaceAll("\\s+", " ").trim();
    }

    public static void main(String[] args) {
        String kkk = "多,,,孔介;;;质；夹芯......结构；变形；静动     -----   态响应；破坏~#$！#￥……*——、·？~#$！#￥……*——、·？";
        String regEx = "`~#$！#￥……*——、·？";
        kkk = kkk.replaceAll(regEx, "");
        kkk = kkk.replaceAll(regEx, "").replaceAll("\n", " ").replaceAll("\r", " ").replaceAll("(&nbsp;)+", " ")
                .replaceAll("\\-+", "-").replaceAll("&amp;", "&").replaceAll("\\s*\\.\\s*", ".")
                .replaceAll("\\s*\\-\\s*", "-").replaceAll("\\,+", ",").replaceAll(";+", ";").replaceAll("\\.+", ".")
                .replaceAll("\\s+", " ").trim();
        System.out.println(kkk);
        List<NsfcKwTfCotfForSorting> sortList = new ArrayList<NsfcKwTfCotfForSorting>();
        NsfcKwTfCotfForSorting sktffs1 = new NsfcKwTfCotfForSorting("C04", "kw1", 1L, 4L);
        sortList.add(sktffs1);
        sortList.add(new NsfcKwTfCotfForSorting("C04", "kw2", 4L, 4L));
        sortList.add(new NsfcKwTfCotfForSorting("C04", "kw3", 6L, 4L));
        sortList.add(new NsfcKwTfCotfForSorting("C04", "kw4", 2L, 4L));
        Collections.sort(sortList);
        for (NsfcKwTfCotfForSorting n : sortList) {
            System.out.println(n);
        }
    }

}
