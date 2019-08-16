package com.smate.center.batch.service.pdwh.prj;

import java.math.BigDecimal;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.prj.NsfcCategoryForKwdicUpdateDao;
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
import com.smate.center.batch.dao.tmppdwh.NsfcKwsCotfTwoDao;
import com.smate.center.batch.model.pdwh.prj.NsfcCategoryForKwdicUpdate;
import com.smate.center.batch.model.pdwh.prj.NsfcKwForSorting;
import com.smate.center.batch.model.pdwh.prj.NsfcKwScoreForSorting;
import com.smate.center.batch.model.pdwh.prj.NsfcKwTfCotfForSorting;
import com.smate.center.batch.model.pdwh.prj.NsfcKwsTfCotfDetail;
import com.smate.center.batch.model.pdwh.prj.NsfcPrjKwByAuthor;
import com.smate.center.batch.model.pdwh.pub.KGPubCooperation;
import com.smate.center.batch.model.pdwh.pub.PdwhPubAddrInsRecord;
import com.smate.center.batch.model.sns.pub.ProjectDataFiveYear;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;
import com.smate.center.batch.service.pub.ProjectDataFiveYearService;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

@Service("nsfcKeywordsService")
@Transactional(rollbackFor = Exception.class)
public class NsfcKeywordsServiceImpl implements NsfcKeywordsService {
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
    private NsfcCategoryForKwdicUpdateDao nsfcCategoryForKwdicUpdateDao;
    @Autowired
    private NsfcKwsCotfTwoDao nsfcKwsCotfTwoDao;

    private String[] filterKws = {"预测方法", "评估方法", "统一建模", "质量提升", "新理论", "算法研究", "问题研究", "实际应用", "新方法", "应用研究", "新方法",
            "正确性", "系统性", "科学问题", "技术研究", "理论基础", "设计理论", "理论模型", "理论模拟", "基础理论", "学习理论", "理论分析", "理论与方法", "理论计算",
            "篇章理论", "系统研究", "研究热点", "方法研究", "研究方法", "研究成果", "其他研究", "实验研究", "机制研究", "功能研究", "机理研究", "跨学科交叉研究", "试验研究",
            "队列研究", "理论研究", "基础研究", "实证研究", "原位研究", "性能研究", "关联研究", "纵向研究", "比较研究", "定量研究", "对比研究", "战略研究", "前瞻性队列研究",
            "临床研究", "质性研究", "案例研究"};

    private String[] filterKws1 = {"机理", "作用机制", "形成机理", "影响因素", "标准", "指标", "高端", "研究", "推广", "提取", "健康", "应用示范", "工艺",
            "装备", "问题", "理论", "基础", "模型", "系统", "体系", "建立", "提出", "处置", "分析", "指标", "产业", "管理", "产品", "尺度", "制备", "复合",
            "检测", "研制", "存储"};

    @Override
    public List<NsfcCategoryForKwdicUpdate> getNsfcDisciplineList(Integer status, Integer length, Integer size) {
        return nsfcCategoryForKwdicUpdateDao.getToHandleTask(100);
    }

    @Override
    public void updateNsfcCategoryForKwdicUpdate(NsfcCategoryForKwdicUpdate nsfcCategoryForKwdicUpdate) {
        // TODO Auto-generated method stub

    }

    @Override
    public Integer getNsfcDisciplineKwCounts(String discipline) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getNsfcDisciplineKw(String discipline) {
        return this.nsfcKwsTfCotfDao.getNsfcKw(discipline);
    }

    @Override
    public void saveNsfcKws(Set<String> nsfcKws, Integer from, String applicationCode) {
        // TODO Auto-generated method stub
    }

    private Long getTfOrCotf(String kw, String category, Integer length) {
        Long count = this.nsfcKwsTfCotfDao.getCounts(kw, category, length);
        return count;
    }

    @Override
    public List<String> getNsfcKwSupplement(String discipline, Integer size) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<BigDecimal> getToHandleKwList(Integer size) {
        return this.nsfcKwsTfCotfDao.getToHandleList(size);
    }

    @Override
    public void handleSubsets(Long prjId) {
        this.handleSubsetsByLanguage(prjId, 2);
        this.handleSubsetsByLanguage(prjId, 1);
    }


    @Override
    public void saveNsfcCategoryForKwdicUpdate(NsfcCategoryForKwdicUpdate nsfcCategoryForKwdicUpdate, Integer status) {
        nsfcCategoryForKwdicUpdate.setStatus(status);
        this.nsfcCategoryForKwdicUpdateDao.save(nsfcCategoryForKwdicUpdate);
    }

    // 1英文，2中文
    private void handleSubsetsByLanguage(Long prjId, Integer language) {
        List<String> kwList = this.nsfcPrjKeywordsDao.getKwStrByPrjIdAndLanguage(prjId, language);
        NsfcPrjKwByAuthor npa = nsfcPrjKwByAuthorDao.get(prjId);
        if (kwList == null || kwList.size() == 0 || npa == null) {
            return;
        }
        Collections.sort(kwList);
        for (int i = 0; i < kwList.size() - 1; i++) {
            for (int j = i + 1; j < kwList.size(); j++) {
                NsfcKwsTfCotfDetail nkt = new NsfcKwsTfCotfDetail();
                nkt.setKwFirst(kwList.get(i));
                nkt.setKwSecond(kwList.get(j));
                nkt.setPrjId(prjId);
                nkt.setDiscode(npa.getNsfcCategroy());
                nkt.setCounts(1);
                nkt.setLanguage(language);
                this.nsfcKwsTfCotfDetailDao.save(nkt);
            }
        }
        for (String str : kwList) {
            NsfcKwsTfCotfDetail nkt = new NsfcKwsTfCotfDetail();
            nkt.setKwFirst(str);
            nkt.setPrjId(prjId);
            nkt.setDiscode(npa.getNsfcCategroy());
            nkt.setCounts(1);
            nkt.setLanguage(language);
            this.nsfcKwsTfCotfDetailDao.save(nkt);
        }
    }

    @Override
    public void updateStatus(Long prjId, Integer status) {
        nsfcKwsTfCotfDao.updateStatus(prjId, status);
    }


    /** 1 成功，2 英文为空，4 中文为空，8 中英文关键词都为空 **/

    @Override
    public Integer sortingKwInDiscode(String category, Integer[] lastThreeYears) {
        try {
            Integer enRs = this.sortKwInDiscodeByLanguage(category, 1, lastThreeYears);// 英文关键词
            Integer zhRs = this.sortKwInDiscodeByLanguage(category, 2, lastThreeYears);// 中文关键词
            return enRs * zhRs;
        } catch (Exception e) {
            logger.error("计算关键词排序出错", e);
            return 3;
        }
    }

    @Override
    public Set<String> nsfcKwSortingByTfCotf(List<String> list, String discipline, Integer limit,
            Set<String> discKwNonDup) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        Set<String> discKwSupplementNonDup = new HashSet<String>();
        List<NsfcKwTfCotfForSorting> newList = new ArrayList<NsfcKwTfCotfForSorting>();
        for (String kw : list) {
            if (!StringUtils.isEmpty(kw)) {
                kw = XmlUtil.cToe(kw);
                kw = kw.toLowerCase().trim();
            }
            Long tf = this.getTfOrCotf(kw, discipline, 1);
            Long cotf = this.getTfOrCotf(kw, discipline, 2);
            NsfcKwTfCotfForSorting nsfs = new NsfcKwTfCotfForSorting(discipline, kw, tf, cotf);
            newList.add(nsfs);
        }
        Collections.sort(list);
        int i = 0;
        for (NsfcKwTfCotfForSorting kwN : newList) {
            if (!StringUtils.isEmpty(kwN.getKeywords()) && !discKwNonDup.contains(kwN.getKeywords())) {
                discKwSupplementNonDup.add(kwN.getKeywords());
                i++;
            }
            if (i > limit) {
                break;
            }
        }
        return discKwSupplementNonDup;
    }

    @Override
    public void updateDiscodeStatus(Integer status, String discode) {
        this.nsfcKwForSortingDao.saveNsfcDiscode(status, discode);
    }

    private Integer sortKwInDiscodeByLanguage(String category, Integer language, Integer[] lastThreeYears) {
        List<String> kwList = this.nsfcPrjKeywordsDao.getKwByDiscode(category, language);
        if (kwList == null || kwList.size() == 0) {
            if (1 == language) {
                return 2;
            } else {
                return 4;
            }
        }
        List<NsfcKwForSorting> nfksList = new ArrayList<NsfcKwForSorting>();
        for (String kw : kwList) {
            if (StringUtils.isEmpty(kw)) {
                continue;
            }
            int cotf = 0;
            List<Map<String, Object>> kwCountsByPrj = this.getCotfSum(kw, category, language);
            for (Map<String, Object> mpp : kwCountsByPrj) {
                cotf = cotf + ((BigDecimal) mpp.get("COUNTS")).intValue() - 1;
            }
            int tf = 0;
            tf = this.nsfcPrjKeywordsDao.getTf(kw, category, language).intValue();
            NsfcKwForSorting nfks = new NsfcKwForSorting(category, kw, tf, cotf, language);
            nfksList.add(nfks);
        }
        Collections.sort(nfksList);
        this.saveKwRs(nfksList, category, lastThreeYears);
        return 1;
    }

    private List<Map<String, Object>> getCotfSum(String kw, String discode, Integer language) {
        return this.nsfcPrjKeywordsDao.getCotfSum(kw, discode, language);
    }

    private void saveKwRs(List<NsfcKwForSorting> rfksList, String category, Integer[] lastThreeYears) {
        if (rfksList == null || rfksList.size() == 0) {
            return;
        }
        // 通过申请数获取
        Long limit = this.nsfcPrjKeywordsDao.getApplicationNum(category, lastThreeYears) == null ? 0L
                : this.nsfcPrjKeywordsDao.getApplicationNum(category, lastThreeYears).longValue();
        // 如果申请书为0，则通过项目数来限制
        if (limit == 0) {
            limit = this.nsfcPrjKeywordsDao.getProjectNum(category, lastThreeYears) == null ? 0L
                    : this.nsfcPrjKeywordsDao.getProjectNum(category, lastThreeYears).longValue();
            limit = limit * 5;// 申请书与项目比例大概为5:1
        }
        int i = 1;
        for (NsfcKwForSorting nkfs : rfksList) {
            nkfs.setSeq(i);
            this.nsfcKwForSortingDao.save(nkfs);
            i++;
            if (limit != 0 && i > limit * 2.5) {
                break;
            }
        }
    }

    @Override
    public List<BigDecimal> getPdwhPubToHandleKwList(Integer size) {
        return this.nsfcKwsTfCotfDao.getPdwhPubToHandleList(size);
    }

    @Override
    public List<BigDecimal> getSubsetsToHandleKwList(Integer size, Integer type) {
        return this.nsfcKwsTfCotfDao.getSubsetsToHandleList(size, type);
    }

    @Override
    public List<BigDecimal> getPsnToHandleKwList(Integer size) {
        return this.nsfcKwsTfCotfDao.getPsnToHandleList(size);
    }

    @Override
    public List<BigDecimal> getPsnToHandleKwList(Integer size, Integer status) {
        return this.nsfcKwsTfCotfDao.getPsnToHandleList(size, status);
    }

    @Override
    public List<BigDecimal> getNsfcPrjToHandleKwList(Integer size) {
        return this.nsfcKwsTfCotfDao.getNsfcPrjToHandleList(size);
    }

    @Override
    public List<String> getNsfcCategoryToHandleKwList(Integer size) {
        return this.nsfcKwsTfCotfDao.getNsfcCategoryToHandleList(size);
    }

    @Override
    public List<String> getNsfcCategoryToHandleKwList(Integer status, Integer size) {
        return this.nsfcKwsTfCotfDao.getNsfcCategoryToHandleList(status, size);
    }

    @Override
    public void updatePdwhPubStatus(Long pubId, Integer status) {
        nsfcKwsTfCotfDao.updatePdwhPubStatus(pubId, status);
    }

    @Override
    public void updateScmPubSubsetsStatus(Long pubId, Integer type, Integer status, Long subSetSize) {
        nsfcKwsTfCotfDao.updateSubsetsStatus(pubId, type, status, subSetSize);
    }

    @Override
    public void updateNsfcPrjStatus(Long pubId, Integer status) {
        nsfcKwsTfCotfDao.updateNsfcPrjStatus(pubId, status);
    }

    @Override
    public void updateNsfcCategoryStatus(String category, Integer status) {
        nsfcKwsTfCotfDao.updateNsfcCategoryStatus(category, status);
    }

    @Override
    public void updatePsnStatus(Long pubId, Integer status) {
        nsfcKwsTfCotfDao.updatePsnStatus(pubId, status);
    }

    @Override
    public Integer extractPdwhPubKws(Long pubId) {
        Integer rs = 1;
        try {
            PubPdwhDetailDOM pubPdwh = pdwhPublicationService.getFullPdwhPubInfoById(pubId);
            if (pubPdwh == null) {
                return 4;
            }
            List<NsfcKwScoreForSorting> kwsSet =
                    this.getPubKws(pubPdwh.getTitle(), pubPdwh.getSummary(), pubPdwh.getKeywords());
            if (kwsSet == null || kwsSet.size() <= 0) {
                rs = 2;
            } else {
                StringBuilder sb = new StringBuilder();
                Integer kwNum = kwsSet.size();
                for (NsfcKwScoreForSorting kw : kwsSet) {
                    if (StringUtils.isEmpty(kw.getKeywords())) {
                        continue;
                    }
                    sb.append(kw.getKeywords());
                    sb.append("; ");
                }
                String kwsStr = sb.substring(0, sb.length() - 2);
                if (kwsStr.length() > 1000) {
                    int last = sb.lastIndexOf(";");
                    kwsStr.substring(0, last - 1);
                    kwNum--;
                }
                this.saveOrUpdateKGPubkw(pubId, kwsStr, kwNum);
            }
        } catch (Exception e) {
            logger.error("基准关键词画像--关键词提取出错：", e);
            rs = 3;
        }
        return rs;
    }

    @Override
    public List<BigDecimal> getJiangxiPrjKwsToHandleKwList(Integer size) {
        return this.nsfcKwsTfCotfDao.getJiangxiToHandleList();
    }

    @Override
    public Integer extractJiangxiPrjKws(Long pubId) {
        Integer rs = 9;
        try {
            Map<String, Object> kwInfo = nsfcKwsTfCotfDao.getJiangxiPrjkw(pubId);
            if (kwInfo == null) {
                return 4;
            }
            Set<String> kwsSet = this.getRsSetNew((String) kwInfo.get("KEYWORD"));
            if (kwsSet == null || kwsSet.size() <= 0) {
                rs = 2;
            } else {
                for (String kw : kwsSet) {
                    if (StringUtils.isEmpty(kw)) {
                        continue;
                    }
                    this.nsfcKwsTfCotfDao.updateJiangxiPrjkw(((BigDecimal) kwInfo.get("KW_ID")).longValue(),
                            (String) kwInfo.get("INS_ID"), (String) kwInfo.get("INS_NAME"), kw,
                            ((BigDecimal) kwInfo.get("SEQ_NO")).intValue(), 6);
                }
                this.nsfcKwsTfCotfDao.updateJiangxiPrjkw(((BigDecimal) kwInfo.get("KW_ID")).longValue(),
                        (String) kwInfo.get("INS_ID"), (String) kwInfo.get("INS_NAME"), (String) kwInfo.get("KEYWORD"),
                        ((BigDecimal) kwInfo.get("SEQ_NO")).intValue(), 0);
            }
        } catch (Exception e) {
            logger.error("基准关键词画像--关键词提取出错：", e);
            rs = 3;
        }
        this.nsfcKwsTfCotfDao.updateJiangxiPrjkwStatus(pubId, rs);
        return rs;
    }

    private void saveOrUpdateKGPubkw(Long pubId, String kws, Integer num) {
        this.nsfcKwForSortingDao.saveOrUpdateKGPubKws(pubId, kws, num);
    }

    private List<String> getPdwhPubKws(Long pubId) throws Exception {
        // PubPdwhDetailDOM pdwhPub =
        // pdwhPublicationService.getFullPdwhPubInfoById(pubId);
        // String kwStr = pdwhPub.getKeywords();
        // String title = pdwhPub.getTitle();
        // String pubAbstract = pdwhPub.getSummary();

        String xml = projectDataFiveYearService.findPatenXml(pubId);
        PubXmlDocument doc = new PubXmlDocument(xml);
        Element ele = (Element) doc.getNode(PubXmlConstants.PUBLICATION_XPATH);

        String title = StringUtils.trimToEmpty(ele.attributeValue("ctitle"));
        String pubAbstract = StringUtils.trimToEmpty(ele.attributeValue("cabstract"));
        // String kwStr = StringUtils.trimToEmpty(ele.attributeValue("ckeywords"));
        String kwStr = "";
        if (StringUtils.isBlank(title)) {
            title = StringUtils.trimToEmpty(ele.attributeValue("etitle"));
        }
        if (StringUtils.isBlank(pubAbstract)) {
            pubAbstract = StringUtils.trimToEmpty(ele.attributeValue("eabstract"));
        }
        /*
         * if (StringUtils.isBlank(kwStr)) { kwStr =
         * StringUtils.trimToEmpty(ele.attributeValue("ekeywords")); }
         * 
         * String[] kws = kwStr.split(";"); Set<String> kwSet = new TreeSet<String>(); if (kws != null ||
         * kws.length > 0) { for (String str : kws) { if (StringUtils.isNotEmpty(str)) { str =
         * StringUtils.trimToEmpty(str); str = str.toLowerCase().replaceAll("\\s+", " "); kwSet.add(str); }
         * } } if (kwSet.size() > 25) { return kwSet; }
         */
        List<String> rsList = new ArrayList<String>();
        String content = title + " " + pubAbstract;
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        content = content.toLowerCase().replaceAll("\\s+", "空格");
        Result kwRs = DicAnalysis.parse(content);
        Set<String> extractKwStrings = new TreeSet<String>();
        for (Term t : kwRs.getTerms()) {
            if (t == null) {
                continue;
            }
            if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
                if (StringUtils.isNotEmpty(t.getName())) {
                    String kw = t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim();
                    if (StringUtils.isNotEmpty(kw) && kw.length() > 2) {
                        if (!XmlUtil.isChinese(kw) && kw.length() < 4) {
                            continue;
                        }
                        // 只有不包含在字符串里边，才往里边加入
                        extractKwStrings.add(kw);
                    }
                }
            }
        }

        // 超长之后按照关键词提取的关键词排序
        List<String> kwList = this.sortExtractKwString(extractKwStrings);
        if (kwList != null && kwList.size() > 0) {
            for (String str : kwList) {
                if (StringUtils.isEmpty(str)
                        || (str.indexOf(" ") == -1 && !XmlUtil.isChinese(str) && str.length() < 12)) {// 英文单词过短，且只有一个词，放弃
                    continue;
                }
                if (rsList.size() >= 25) {
                    break;
                }
                int flag = 1;
                for (String rskwStr : rsList) {
                    if (rskwStr.length() > str.length()) {
                        if (rskwStr.indexOf(str) > -1) {
                            flag = 0;
                            break;
                        }
                    } else {
                        if (str.indexOf(rskwStr) > -1) {
                            flag = 0;
                            break;
                        }
                    }
                }
                if (flag == 1) {
                    rsList.add(str);
                }
            }
        }
        return rsList;
    }

    // 直接使用数据库排序
    private List<String> sortExtractKwString(Set<String> extractKwStrings) {
        return this.nsfcKwForSortingDao.getSortedKws(extractKwStrings);
    }

    private List<String> getPdwhPubKwsFromTitleAndAbs(Long pubId) throws Exception {
        // PubPdwhDetailDOM pdwhPub =
        // pdwhPublicationService.getFullPdwhPubInfoById(pubId);
        // String kwStr = pdwhPub.getKeywords();
        // String title = pdwhPub.getTitle();
        // String pubAbstract = pdwhPub.getSummary();
        ProjectDataFiveYear prj = this.projectDataFiveYearDao.get(pubId);
        // String xml = projectDataFiveYearService.findPatenXml(pubId);
        // PubXmlDocument doc = new PubXmlDocument(xml);
        // Element ele = (Element) doc.getNode(PubXmlConstants.PUBLICATION_XPATH);
        // String title = StringUtils.trimToEmpty(ele.attributeValue("ctitle"));
        // String pubAbstract = StringUtils.trimToEmpty(ele.attributeValue("cabstract"));
        // String kwStr =
        // StringUtils.trimToEmpty(ele.attributeValue("ckeywords"));
        String title = prj.getZhTitle();
        String pubAbstract = prj.getZhAbstract();

        String kwStr = "";
        if (StringUtils.isBlank(title)) {
            // title = StringUtils.trimToEmpty(ele.attributeValue("etitle"));
            title = prj.getEnTitle();
        }
        if (StringUtils.isBlank(pubAbstract)) {
            // pubAbstract = StringUtils.trimToEmpty(ele.attributeValue("eabstract"));
            pubAbstract = prj.getEnAbstract();
        }
        /*
         * if (StringUtils.isBlank(kwStr)) { kwStr =
         * StringUtils.trimToEmpty(ele.attributeValue("ekeywords")); }
         * 
         * String[] kws = kwStr.split(";"); Set<String> kwSet = new TreeSet<String>(); if (kws != null ||
         * kws.length > 0) { for (String str : kws) { if (StringUtils.isNotEmpty(str)) { str =
         * StringUtils.trimToEmpty(str); str = str.toLowerCase().replaceAll("\\s+", " "); kwSet.add(str); }
         * } } if (kwSet.size() > 25) { return kwSet; }
         */
        List<String> rsList = new ArrayList<String>();
        String content = title + " " + pubAbstract;
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        extractTitleKwStrings = this.getRsSetNew(title);
        extractAbsKwStrings = this.getRsSetNew(pubAbstract);
        // Set<String> rsKwSet = new TreeSet<String>();
        // 先去除，然后比较是否超长
        // rsKwSet.addAll(kwSet);
        // rsKwSet.addAll(extractTitleKwStrings);
        // rsKwSet.addAll(extractAbsKwStrings);
        // if (rsKwSet.size() <= 15) {
        // return rsKwSet;
        // }
        // 超长之后按照关键词提取的关键词排序
        List<String> kwList = this.getExtractKwStringNew(extractTitleKwStrings, extractAbsKwStrings);
        // List<String> rsKwList = new ArrayList<String>();
        if (kwList != null && kwList.size() > 0) {
            for (String str : kwList) {
                if (StringUtils.isEmpty(str)) {
                    continue;
                }
                if (rsList.size() >= 25) {
                    break;
                }
                int flag = 1;
                for (String rskwStr : rsList) {
                    if (rskwStr.length() > str.length()) {
                        if (rskwStr.indexOf(str) > -1) {
                            flag = 0;
                            break;
                        }
                    } else {
                        if (str.indexOf(rskwStr) > -1) {
                            flag = 0;
                            break;
                        }
                    }
                }
                if (flag == 1) {
                    rsList.add(str);
                }
            }
        }
        return rsList;
    }

    private Set<String> getRsSetNew(String content) {
        Set<String> extractKwStrings = new TreeSet<String>();
        if (StringUtils.isBlank(content)) {
            return extractKwStrings;
        }
        if (!XmlUtil.isChinese(content)) {
            content = content.toLowerCase().replaceAll("\\s+", "空格");
        }
        Result kwRs = DicAnalysis.parse(content);
        for (Term t : kwRs.getTerms()) {
            if (t == null) {
                continue;
            }
            if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
                if (StringUtils.isNotEmpty(t.getName())) {
                    String kw = t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim();
                    // if (StringUtils.isNotEmpty(kw) && kw.length() > 2) {
                    if (StringUtils.isNotEmpty(kw)) {
                        if (!XmlUtil.isChinese(kw) && kw.length() < 3) {
                            continue;
                        }
                        // 只有不包含在字符串里边，才往里边加入
                        extractKwStrings.add(kw);
                    }
                }
            }
        }
        return extractKwStrings;
    }

    private Set<String> getRsSetNew(String content, Long pubId) {
        Set<String> extractKwStrings = new TreeSet<String>();
        if (StringUtils.isEmpty(content)) {
            System.out.println(pubId + "==标题为空==");
            return extractKwStrings;
        }
        System.out.println(pubId + "==标题为==" + content + "==");
        if (!XmlUtil.isChinese(content)) {
            content = content.toLowerCase().replaceAll("\\s+", "空格");
        }
        Result kwRs = DicAnalysis.parse(content);
        System.out.println(pubId + "==标题提取关键词结果==" + kwRs.toString() + "==");
        for (Term t : kwRs.getTerms()) {
            if (t == null) {
                continue;
            }
            if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
                if (StringUtils.isNotEmpty(t.getName())) {
                    String kw = t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim();
                    if (StringUtils.isNotEmpty(kw) && kw.length() > 2) {
                        if (!XmlUtil.isChinese(kw) && kw.length() < 4) {
                            continue;
                        }
                        // 只有不包含在字符串里边，才往里边加入
                        extractKwStrings.add(kw);
                    }
                }
            }
        }
        if (extractKwStrings == null || extractKwStrings.size() <= 0) {
            System.out.println(pubId + "==标题未提取到关键词==");
        }
        return extractKwStrings;
    }

    private Map<String, Object> extractKwAndTfInContent(String content) {
        Set<String> extractKwStrings = new TreeSet<String>();
        Map<String, Object> tfMp = new HashMap<String, Object>();
        if (StringUtils.isEmpty(content)) {
            return tfMp;
        }
        if (!XmlUtil.isChinese(content)) {
            content = content.toLowerCase().replaceAll("\\s+", "空格");
        }
        Integer i = 0;
        Result kwRs = DicAnalysis.parse(content);
        for (Term t : kwRs.getTerms()) {
            if (t == null) {
                continue;
            }
            if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
                if (StringUtils.isNotEmpty(t.getName())) {
                    String kw = t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim();
                    if (StringUtils.isNotEmpty(kw) && kw.length() > 2) {
                        if (!XmlUtil.isChinese(kw) && kw.length() < 4) {
                            continue;
                        }
                        for (String filter : filterKws) {
                            if (kw.indexOf(filter) >= 0) {
                                continue;
                            }
                        }
                        // 只有不包含在字符串里边，才往里边加入
                        extractKwStrings.add(kw);
                        if (i > 200) {
                            break;
                        }
                        i++;
                        Integer tf = t.natrue().allFrequency;
                        Integer ltf = (Integer) tfMp.get(kw);
                        if (ltf == null || 0 == ltf) {
                            tfMp.put(kw, tf);
                        } else {
                            tf = tf + ltf;
                            tfMp.put(kw, tf);
                        }
                    }
                }
            }
            tfMp.put("EXTRACT_KWS_STRSETS", extractKwStrings);
        }
        return tfMp;
    }

    private Map<String, Object> extractKwAndTfInContentEn(String content) {
        Set<String> extractKwStrings = new TreeSet<String>();
        Map<String, Object> tfMp = new HashMap<String, Object>();
        if (StringUtils.isEmpty(content)) {
            return tfMp;
        }
        content = content.toLowerCase().replaceAll("\\s+", "空格");
        Result kwRs = DicAnalysis.parse(content);
        for (Term t : kwRs.getTerms()) {
            if (t == null) {
                continue;
            }
            if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
                if (StringUtils.isNotEmpty(t.getName())) {
                    String kw = t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim();
                    if (StringUtils.isNotEmpty(kw) && kw.length() > 2) {
                        if (!XmlUtil.isChinese(kw) && kw.length() < 4) {
                            continue;
                        }
                        // 只有不包含在字符串里边，才往里边加入
                        extractKwStrings.add(kw);
                        Integer tf = t.natrue().allFrequency;
                        Integer ltf = (Integer) tfMp.get(kw);
                        if (ltf == null || 0 == ltf) {
                            tfMp.put(kw, tf);
                        } else {
                            tf = tf + ltf;
                            tfMp.put(kw, tf);
                        }
                    }
                }
            }
            tfMp.put("EXTRACT_KWS_STRSETS", extractKwStrings);
        }
        return tfMp;
    }

    private List<String> getExtractKwStringNew(Set<String> titleSet, Set<String> absSet) {
        Set<String> rsSet = new TreeSet<String>();
        List<NsfcKwTfCotfForSorting> rsList = new ArrayList<NsfcKwTfCotfForSorting>();
        List<String> rsStringList = new ArrayList<String>();
        if (titleSet != null && titleSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listTitle = sortExtractKwStringNew(titleSet, 1.5);
            if (listTitle != null && listTitle.size() > 0) {
                rsList.addAll(listTitle);
                // 如果在标题中出现过了，就不在abs中重复计算
                if (absSet != null && absSet.size() > 0) {
                    for (String str : absSet) {
                        if (!titleSet.contains(str)) {
                            rsSet.add(str);
                        }
                    }
                }
            }
        }
        if (rsSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listAbstract = sortExtractKwStringNew(rsSet, 1.0);
            if (listAbstract != null && listAbstract.size() > 0) {
                rsList.addAll(listAbstract);
            }
        } else if (absSet != null && absSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listAbstract = sortExtractKwStringNew(absSet, 1.0);
            if (listAbstract != null && listAbstract.size() > 0) {
                rsList.addAll(listAbstract);
            }
        }
        if (rsList.size() > 0) {
            Collections.sort(rsList);
            for (NsfcKwTfCotfForSorting nsfcKw : rsList) {
                if (StringUtils.isEmpty(nsfcKw.getKeywords())) {
                    continue;
                }
                rsStringList.add(nsfcKw.getKeywords());
            }
        }
        return rsStringList;
    }

    private Map<String, Double> getExtractKwStringNew1(Set<String> titleSet, Set<String> absSet) {
        Set<String> rsSet = new TreeSet<String>();
        List<NsfcKwTfCotfForSorting> rsList = new ArrayList<NsfcKwTfCotfForSorting>();
        List<String> rsStringList = new ArrayList<String>();
        if (titleSet != null && titleSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listTitle = sortExtractKwStringNew(titleSet, 1.5);
            if (listTitle != null && listTitle.size() > 0) {
                rsList.addAll(listTitle);
                // 如果在标题中出现过了，就不在abs中重复计算
                if (absSet != null && absSet.size() > 0) {
                    for (String str : absSet) {
                        if (!titleSet.contains(str)) {
                            rsSet.add(str);
                        }
                    }
                }
            }
        }
        if (rsSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listAbstract = sortExtractKwStringNew(rsSet, 1.0);
            if (listAbstract != null && listAbstract.size() > 0) {
                rsList.addAll(listAbstract);
            }
        } else if (absSet != null && absSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listAbstract = sortExtractKwStringNew(absSet, 1.0);
            if (listAbstract != null && listAbstract.size() > 0) {
                rsList.addAll(listAbstract);
            }
        }
        Map<String, Double> rsMap = new HashMap<String, Double>();
        if (rsList.size() > 0) {
            Collections.sort(rsList);
            for (NsfcKwTfCotfForSorting nsfcKw : rsList) {
                if (StringUtils.isEmpty(nsfcKw.getKeywords())) {
                    continue;
                }
                rsMap.put(nsfcKw.getKeywords(), nsfcKw.getIsiTf());
            }
        }
        return rsMap;
    }

    // 计算项目不加权重，需要加入category限制
    private List<String> getExtractKwStringNew(Set<String> titleSet, Set<String> absSet, String category) {
        Set<String> rsSet = new TreeSet<String>();
        List<NsfcKwTfCotfForSorting> rsList = new ArrayList<NsfcKwTfCotfForSorting>();
        List<String> rsStringList = new ArrayList<String>();
        if (titleSet != null && titleSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listTitle = sortExtractKwStringNewNoWeight(titleSet, 1.5, category);
            if (listTitle != null && listTitle.size() > 0) {
                rsList.addAll(listTitle);
                // 如果在标题中出现过了，就不在abs中重复计算
                if (absSet != null && absSet.size() > 0) {
                    for (String str : absSet) {
                        if (!titleSet.contains(str)) {
                            rsSet.add(str);
                        }
                    }
                }
            }
        }
        if (rsSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listAbstract = sortExtractKwStringNewNoWeight(rsSet, 1.0, category);
            if (listAbstract != null && listAbstract.size() > 0) {
                rsList.addAll(listAbstract);
            }
        } else if (absSet != null && absSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listAbstract = sortExtractKwStringNewNoWeight(absSet, 1.0, category);
            if (listAbstract != null && listAbstract.size() > 0) {
                rsList.addAll(listAbstract);
            }
        }
        if (rsList.size() > 0) {
            Collections.sort(rsList);
            for (NsfcKwTfCotfForSorting nsfcKw : rsList) {
                if (StringUtils.isEmpty(nsfcKw.getKeywords())) {
                    continue;
                }
                rsStringList.add(nsfcKw.getKeywords());
            }
        }
        return rsStringList;
    }

    // 计算项目不加权重，需要加入category限制
    private Map<String, Double> getExtractKwStringNew1(Set<String> titleSet, Set<String> absSet, String category) {
        Set<String> rsSet = new TreeSet<String>();
        List<NsfcKwTfCotfForSorting> rsList = new ArrayList<NsfcKwTfCotfForSorting>();
        List<String> rsStringList = new ArrayList<String>();
        if (titleSet != null && titleSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listTitle = sortExtractKwStringNewNoWeight(titleSet, 1.5, category);
            if (listTitle != null && listTitle.size() > 0) {
                rsList.addAll(listTitle);
                // 如果在标题中出现过了，就不在abs中重复计算
                if (absSet != null && absSet.size() > 0) {
                    for (String str : absSet) {
                        if (!titleSet.contains(str)) {
                            rsSet.add(str);
                        }
                    }
                }
            }
        }
        if (rsSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listAbstract = sortExtractKwStringNewNoWeight(rsSet, 1.0, category);
            if (listAbstract != null && listAbstract.size() > 0) {
                rsList.addAll(listAbstract);
            }
        } else if (absSet != null && absSet.size() > 0) {
            List<NsfcKwTfCotfForSorting> listAbstract = sortExtractKwStringNewNoWeight(absSet, 1.0, category);
            if (listAbstract != null && listAbstract.size() > 0) {
                rsList.addAll(listAbstract);
            }
        }
        Map<String, Double> rsMap = new HashMap<String, Double>();
        if (rsList.size() > 0) {
            Collections.sort(rsList);
            for (NsfcKwTfCotfForSorting nsfcKw : rsList) {
                if (StringUtils.isEmpty(nsfcKw.getKeywords())) {
                    continue;
                }
                rsMap.put(nsfcKw.getKeywords(), nsfcKw.getIsiTf());
            }
        }
        return rsMap;
    }

    // 1.表示来自
    private List<NsfcKwTfCotfForSorting> sortExtractKwStringNew(Set<String> extractKwStrings, Double type) {
        List<NsfcKwTfCotfForSorting> rsSet = new ArrayList<NsfcKwTfCotfForSorting>();
        // List<Map<String, Object>> rsTitle = nsfcKwForSortingDao.getKwsInfo(extractKwStrings);
        List<Map<String, Object>> rsTitle = projectDataFiveYearDao.getKwsInfo(extractKwStrings);
        if (rsTitle != null && rsTitle.size() > 0) {
            for (Map<String, Object> map : rsTitle) {
                if (map.get("KW_STR") == null) {
                    continue;
                }
                String kwStr = (String) map.get("KW_STR");
                Long tf = map.get("TF") == null ? 0L : ((BigDecimal) map.get("TF")).longValue();
                Integer length = map.get("KW_LENGTH") == null ? 0 : ((BigDecimal) map.get("KW_LENGTH")).intValue();
                Integer kwType = map.get("KW_TYPE") == null ? 0 : ((BigDecimal) map.get("KW_TYPE")).intValue();
                NsfcKwTfCotfForSorting kw;
                if (kwType == 1) {
                    kw = new NsfcKwTfCotfForSorting(kwStr, length, tf * type);
                } else {
                    // 非项目关键词，得分减少一半
                    kw = new NsfcKwTfCotfForSorting(kwStr, length, tf * type * 0.5);
                }
                if (length == 1) {
                    kw.setIsiTf(kw.getIsiTf() / 0.6);
                    // continue;
                }
                rsSet.add(kw);
            }
        }

        return rsSet;
    }

    // 提取关键词加入权重
    private List<NsfcKwTfCotfForSorting> sortExtractKwStringNew(Set<String> extractKwStrings, Double type,
            String category) {
        List<NsfcKwTfCotfForSorting> rsSet = new ArrayList<NsfcKwTfCotfForSorting>();
        // List<Map<String, Object>> rsTitle = nsfcKwForSortingDao.getKwsInfo(extractKwStrings);
        List<Map<String, Object>> rsTitle = projectDataFiveYearDao.getKwsInfo(extractKwStrings, category);
        if (rsTitle != null && rsTitle.size() > 0) {
            for (Map<String, Object> map : rsTitle) {
                if (map.get("KW_STR") == null) {
                    continue;
                }
                String kwStr = (String) map.get("KW_STR");
                Long tf = map.get("TF") == null ? 0L : ((BigDecimal) map.get("TF")).longValue();
                Integer length = map.get("KW_LENGTH") == null ? 0 : ((BigDecimal) map.get("KW_LENGTH")).intValue();
                Integer kwType = map.get("KW_TYPE") == null ? 0 : ((BigDecimal) map.get("KW_TYPE")).intValue();
                NsfcKwTfCotfForSorting kw;
                if (kwType == 1) {
                    kw = new NsfcKwTfCotfForSorting(kwStr, length, tf * type);
                } else {
                    // 非项目关键词，得分减少一半
                    kw = new NsfcKwTfCotfForSorting(kwStr, length, tf * type * 0.5);
                }
                if (length == 1) {
                    kw.setIsiTf(kw.getIsiTf() / 0.6);
                    // continue;
                }
                rsSet.add(kw);
            }
        }

        return rsSet;
    }

    private List<NsfcKwTfCotfForSorting> sortExtractKwStringNewNoWeight(Set<String> extractKwStrings, Double type,
            String category) {
        List<NsfcKwTfCotfForSorting> rsSet = new ArrayList<NsfcKwTfCotfForSorting>();
        // List<Map<String, Object>> rsTitle = nsfcKwForSortingDao.getKwsInfo(extractKwStrings);
        List<Map<String, Object>> rsTitle;
        if (StringUtils.isNotEmpty(category)) {
            rsTitle = projectDataFiveYearDao.getKwsInfo(extractKwStrings, category);
        } else {
            rsTitle = nsfcKwForSortingDao.getKwsInfo(extractKwStrings);
        }
        if (rsTitle != null && rsTitle.size() > 0) {
            for (Map<String, Object> map : rsTitle) {
                if (map.get("KW_STR") == null) {
                    continue;
                }
                String kwStr = (String) map.get("KW_STR");
                Long tf = map.get("TF") == null ? 0L : ((BigDecimal) map.get("TF")).longValue();
                if (tf <= 1L) {
                    continue;
                }
                Integer length = map.get("KW_LENGTH") == null ? 0 : ((BigDecimal) map.get("KW_LENGTH")).intValue();
                Integer kwType = map.get("KW_TYPE") == null ? 0 : ((BigDecimal) map.get("KW_TYPE")).intValue();
                NsfcKwTfCotfForSorting kw = new NsfcKwTfCotfForSorting(kwStr, length, tf * type);
                /*
                 * if (kwType == 1) { kw = new NsfcKwTfCotfForSorting(kwStr, length, tf * type); } else { //
                 * 非项目关键词，得分减少一半 kw = new NsfcKwTfCotfForSorting(kwStr, length, tf * type * 0.5); }
                 */
                if (length == 1) {
                    kw.setIsiTf(kw.getIsiTf() / 0.6);
                    // continue;
                }
                rsSet.add(kw);
            }
        }

        return rsSet;
    }

    /**
     * 成果合作
     * 
     */
    public void getPdwhPubCooperation(Long pdwhPubId) {
        List<PdwhPubAddrInsRecord> addrList = this.pdwhPubAddrInsRecordDao.getPubAddrInsRecordByPubId(pdwhPubId);
        KGPubCooperation kGPubCooperation = kGPubCooperationDao.get(pdwhPubId);

        if (kGPubCooperation == null) {
            kGPubCooperation = new KGPubCooperation(pdwhPubId);
        }

        if (addrList != null) {
            if (addrList.size() == 1) {
                kGPubCooperation.setInstitute(1);
            }
            for (PdwhPubAddrInsRecord ppair : addrList) {
                Long insId = ppair.getInsId();
            }
        }

        this.kGPubCooperationDao.save(kGPubCooperation);
    }

    @Override
    public List<String> getToHandleDiscode(Integer size) {
        return this.nsfcKwForSortingDao.getToHandleDiscode(size);
    }

    // 包含标题，摘要；同时保留所有的成果关键词
    private Set<String> getPdwhPubKwsAll(Long pubId) throws Exception {
        // PubPdwhDetailDOM pdwhPub =
        // pdwhPublicationService.getFullPdwhPubInfoById(pubId);
        // String kwStr = pdwhPub.getKeywords();
        // String title = pdwhPub.getTitle();
        // String pubAbstract = pdwhPub.getSummary();

        String xml = projectDataFiveYearService.findPatenXml(pubId);
        PubXmlDocument doc = new PubXmlDocument(xml);
        Element ele = (Element) doc.getNode(PubXmlConstants.PUBLICATION_XPATH);

        String title = StringUtils.trimToEmpty(ele.attributeValue("ctitle"));
        String pubAbstract = StringUtils.trimToEmpty(ele.attributeValue("cabstract"));
        String kwStr = StringUtils.trimToEmpty(ele.attributeValue("ckeywords"));
        if (StringUtils.isBlank(title)) {
            title = StringUtils.trimToEmpty(ele.attributeValue("etitle"));
        }
        if (StringUtils.isBlank(pubAbstract)) {
            pubAbstract = StringUtils.trimToEmpty(ele.attributeValue("eabstract"));
        }
        if (StringUtils.isBlank(kwStr)) {
            kwStr = StringUtils.trimToEmpty(ele.attributeValue("ekeywords"));
        }

        String[] kws = kwStr.split(";");
        Set<String> kwSet = new TreeSet<String>();
        if (kws != null || kws.length > 0) {
            for (String str : kws) {
                if (StringUtils.isNotEmpty(str)) {
                    str = StringUtils.trimToEmpty(str);
                    str = str.toLowerCase().replaceAll("\\s+", " ");
                    kwSet.add(str);
                }
            }
        }
        if (kwSet.size() > 25) {
            return kwSet;
        }
        String content = title + " " + pubAbstract;
        if (StringUtils.isEmpty(content)) {
            return kwSet;
        }
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        extractTitleKwStrings = this.getRsSetNew(title);
        extractAbsKwStrings = this.getRsSetNew(pubAbstract);
        Set<String> rsKwSet = new TreeSet<String>();
        // 先去除，然后比较是否超长
        rsKwSet.addAll(kwSet);
        rsKwSet.addAll(extractTitleKwStrings);
        rsKwSet.addAll(extractAbsKwStrings);
        if (rsKwSet.size() <= 15) {
            return rsKwSet;
        }
        // 超长之后按照关键词提取的关键词排序
        List<String> kwList = this.getExtractKwStringNew(extractTitleKwStrings, extractAbsKwStrings);
        if (kwList != null && kwList.size() > 0) {
            for (String str : kwList) {
                if (kwSet.size() >= 15) {
                    break;
                }
                kwSet.add(str);
            }
        }
        return kwSet;
    }

    // 从标题，自填关键词中得到关键词，然后利用co-tf计算abstract中对应
    private List<String> getPdwhPubKwsFromTitleAndAbsByCotf(Long pubId) throws Exception {
        ProjectDataFiveYear prj = this.projectDataFiveYearDao.get(pubId);
        String title = prj.getZhTitle();
        String pubAbstract = prj.getZhAbstract();
        String pubKw = prj.getZhKeywords();
        if (StringUtils.isBlank(title)) {
            title = prj.getEnTitle();
        }
        if (StringUtils.isBlank(pubAbstract)) {
            pubAbstract = prj.getEnAbstract();
        }
        if (StringUtils.isBlank(pubKw)) {
            pubKw = prj.getEnKeywords();
        }
        List<String> rsList = new ArrayList<String>();
        String content = title + " " + pubAbstract;
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        extractTitleKwStrings = this.getRsSetNew(title);
        extractAbsKwStrings = this.getRsSetNew(pubAbstract);
        List<String> kwList_1 = new ArrayList<String>();
        for (String keyTitle : extractTitleKwStrings) {
            kwList_1.add(keyTitle);
        }

        if (StringUtils.isNotBlank(pubKw)) {
            pubKw = pubKw.replace("；", ";");
            String[] kws = pubKw.split(";");
            if (kws != null || kws.length > 0) {
                for (String str : kws) {
                    if (StringUtils.isNotEmpty(str)) {
                        str = StringUtils.trimToEmpty(str);
                        str = str.toLowerCase().replaceAll("\\s+", " ");
                        extractTitleKwStrings.add(str);
                    }
                }
            }
        }
        // List<String> kwList = this.getExtractKwStringNew(extractTitleKwStrings, extractAbsKwStrings);
        List<String> kwList = new ArrayList<String>();
        List<String> titleKwList = new ArrayList<String>();
        // List<String> titleKwList = this.getExtractKwStringNew(null, extractTitleKwStrings);
        for (String keyTitle : extractTitleKwStrings) {
            titleKwList.add(keyTitle);
        }
        List<String> absKwList;
        Map<String, Double> absKwMap;
        String category = prj.getApplicationCode();
        // nsfc项目需要使用学科代码限制
        if (StringUtils.isNotEmpty(category)) {
            if (category.length() > 5) {
                // 只限制到2级比如G0102
                category = category.substring(0, 5);
            }
            absKwList = this.getExtractKwStringNew(null, extractAbsKwStrings, category);
            absKwMap = this.getExtractKwStringNew1(null, extractAbsKwStrings, category);
        } else {
            absKwList = this.getExtractKwStringNew(null, extractAbsKwStrings);
            absKwMap = this.getExtractKwStringNew1(null, extractAbsKwStrings);
        }
        if (CollectionUtils.isNotEmpty(titleKwList)) {
            kwList.addAll(titleKwList);
        }
        Long pkId = this.projectDataFiveYearDao.getMaxId() + 2;
        if (CollectionUtils.isNotEmpty(kwList_1)) {
            this.projectDataFiveYearDao.deleteTitleKwNew(pubId);
            for (String kw : kwList_1) {
                pkId++;
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.projectDataFiveYearDao.insertIntoTitleKwNew(pkId, pubId,
                        StringUtils.trimToEmpty(prj.getApplyYear()), language, StringUtils.trimToEmpty(kw), category);
            }
        }

        if (CollectionUtils.isNotEmpty(titleKwList) && CollectionUtils.isNotEmpty(absKwList)) {
            this.projectDataFiveYearDao.deleteAbsKw(pubId);
            this.projectDataFiveYearDao.deleteTitleKw(pubId);
            this.projectDataFiveYearDao.deleteAbsKwNew(pubId);

            for (String kw : titleKwList) {
                pkId++;
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.projectDataFiveYearDao.insertIntoTitleKw(pkId, pubId, StringUtils.trimToEmpty(prj.getApplyYear()),
                        language, StringUtils.trimToEmpty(kw), category);
            }
            for (String kw : absKwList) {
                pkId++;
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.projectDataFiveYearDao.insertIntoAbsKw(pkId, pubId, StringUtils.trimToEmpty(prj.getApplyYear()),
                        language, StringUtils.trimToEmpty(kw), category, absKwMap.get(kw));
            }
            List<String> extractKws = this.projectDataFiveYearDao.getKwByTf(titleKwList, absKwList, category);
            List<Map<String, Object>> rsMap =
                    this.projectDataFiveYearDao.getKwByCoTfCout(titleKwList, absKwList, category);
            int i = 0;
            for (String kw : extractKws) {
                Map<String, Object> map = rsMap.get(i);
                Integer cotf = 0;
                if (map != null && map.size() > 0) {
                    cotf = ((BigDecimal) map.get("COUNTS")).intValue();
                }
                pkId++;
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.projectDataFiveYearDao.insertIntoAbsKwNew(pkId, pubId, StringUtils.trimToEmpty(prj.getApplyYear()),
                        language, StringUtils.trimToEmpty(kw), category, cotf);
                i++;
            }
            if (CollectionUtils.isNotEmpty(extractKws)) {
                kwList.addAll(extractKws);
            }
        }

        if (kwList != null && kwList.size() > 0) {
            for (String str : kwList) {
                if (StringUtils.isEmpty(str)) {
                    continue;
                }
                /*
                 * if (rsList.size() >= 25) { break; }
                 */
                int flag = 1;
                for (String rskwStr : rsList) {
                    if (rskwStr.length() > str.length()) {
                        if (rskwStr.indexOf(str) > -1) {
                            flag = 0;
                            break;
                        }
                    } else {
                        /*
                         * if (str.indexOf(rskwStr) > -1) { flag = 0; break; }
                         */}
                }
                if (flag == 1) {
                    rsList.add(str);
                }
            }
        }
        return rsList;
    }

    /**
     * 替换常见字符
     * 
     * @param string
     * @return
     * @author LIJUN
     * @date 2018年3月26日
     */
    public String replaceChars(String string) {
        string = string.replace("，", ",").replace("（", "(").replace("）", ")").replace("。", ".");
        string = string.replace(" ", "空格").replace(",", "逗号").replace(".", "点符号").replace("(", "左括号")
                .replace(")", "右括号").replace("&", "和符号").replace("'", "撇号").replace("-", "杠符号").replace("《", "前书名号")
                .replace("》", "后书名号");
        return string;

    }

    /**
     * 还原字符
     * 
     * @param string
     * @return
     * @author LIJUN
     * @date 2018年3月26日
     */
    public String resetChars(String string) {
        string = string.replace("空格", " ").replace("逗号", ",").replace("点符号", ".").replace("左括号", "(")
                .replace("右括号", ")").replace("和符号", "&").replace("撇号", "'").replace("杠符号", "-").replace("前书名号", "《")
                .replace("后书名号", "》");
        return string;
    }

    @Override
    public Integer extractPsnKwsFromPrjAndPub(Long PsnId) {
        Integer rs = 1;
        try {
            // List<String> kwsSet = this.getPsnPrjAndPubKws(PsnId);
            List<String> kwsSet = getPsnPrjAndPubKwsByContentTf(PsnId);
            if (kwsSet == null || kwsSet.size() <= 0) {
                rs = 2;
            } else {
                /*
                 * StringBuilder sb = new StringBuilder(); Integer kwNum = kwsSet.size(); for (String kw : kwsSet) {
                 * sb.append(kw); sb.append("; "); } String kwsStr = sb.substring(0, sb.length() - 2); if
                 * (kwsStr.length() > 2000) { kwsStr = kwsStr.substring(0, 2000); int last =
                 * kwsStr.lastIndexOf(";"); kwsStr = kwsStr.substring(0, last - 1); kwNum =
                 * kwsStr.split(";").length; } this.nsfcKwForSortingDao.saveOrUpdateKGPsnKws(PsnId, kwsStr, kwNum);
                 */}
        } catch (Exception e) {
            logger.error("基准关键词画像--关键词提取出错：", e);
            rs = 3;
        }
        return rs;
    }

    @Override
    public Integer extractPsnKwsFromPrjAndPubExcludeTf(Long PsnId) {
        Integer rs = 1;
        try {
            // List<String> kwsSet = this.getPsnKwFromPrjAndPubForGaoxiao(PsnId); 昌盛组
            // List<String> kwsSet = this.getPsnPrjAndPubKws(PsnId);
            List<String> kwsSet = getPsnPrjAndPubKwsForIsis(PsnId, 2);
            List<String> kwsSet2 = getPsnPrjAndPubKwsForIsis(PsnId, 1);
            // if ((kwsSet == null || kwsSet.size() <= 0) && (kwsSet2 == null || kwsSet2.size() <= 0)) {
            if (kwsSet == null || kwsSet.size() <= 0) {
                rs = 2;
            } else {
                /*
                 * StringBuilder sb = new StringBuilder(); Integer kwNum = kwsSet.size(); for (String kw : kwsSet) {
                 * sb.append(kw); sb.append("; "); } String kwsStr = sb.substring(0, sb.length() - 2); if
                 * (kwsStr.length() > 2000) { kwsStr = kwsStr.substring(0, 2000); int last =
                 * kwsStr.lastIndexOf(";"); kwsStr = kwsStr.substring(0, last - 1); kwNum =
                 * kwsStr.split(";").length; } this.nsfcKwForSortingDao.saveOrUpdateKGPsnKws(PsnId, kwsStr, kwNum);
                 */}
        } catch (Exception e) {
            logger.error("基准关键词画像--关键词提取出错：", e);
            rs = 3;
        }
        return rs;
    }

    private List<String> getPsnPrjAndPubKws(Long openId) {
        // 通过SNS-TOKEN, OPENID获得SNS_PSN_ID
        Long snsPsnId = this.openUserUnionDao.getPsnIdByOpenIdAndToken(openId, "2bcca485");
        List<Long> isisPsnId = this.kGPubCooperationDao.getPsnIsisId(openId);
        if (snsPsnId == null || snsPsnId == 0L) {
            return null;
        }
        String psnName = this.kGPubCooperationDao.getPsnIsisName(openId);
        List<String> prjIdList = new ArrayList<String>();
        // 设置为测试账号
        prjIdList = this.kGPubCooperationDao.getPsnNsfcPrjToHandleList(isisPsnId);
        if (prjIdList.size() <= 0) {
            return null;
        }
        this.kGPubCooperationDao.deletePsnPrjPubKw(openId);
        // 项目关键词
        Set<String> extractPrjKwStrings = new TreeSet<String>();
        Set<String> extractPrjTitleKwStrings = new TreeSet<String>();
        for (String prjId : prjIdList) {
            Map<String, Object> mp = this.kGPubCooperationDao.getNsfcPrjInfo(prjId);
            String title = mp.get("ZH_TITLE") == null ? "" : (String) mp.get("ZH_TITLE");
            String kwsStr = mp.get("ZH_KEYWORDS") == null ? "" : (String) mp.get("ZH_KEYWORDS");
            Set<String> extractKwStrings = new TreeSet<String>();
            Set<String> extractTitleKwStrings = new TreeSet<String>();
            Set<String> extractKwsKwStrings = new TreeSet<String>();
            extractTitleKwStrings = this.getRsSetNew(title);
            extractPrjTitleKwStrings.addAll(extractTitleKwStrings);
            if (StringUtils.isNotBlank(kwsStr)) {
                kwsStr = kwsStr.replace("；", ";");
                String[] kws = kwsStr.split(";");
                if (kws != null) {
                    for (String str : kws) {
                        if (StringUtils.isNotEmpty(str)) {
                            str = StringUtils.trimToEmpty(str);
                            str = str.toLowerCase().replaceAll("\\s+", " ");
                            extractKwsKwStrings.add(str);
                        }
                    }
                }
            }
            extractKwStrings.addAll(extractTitleKwStrings);
            extractKwStrings.addAll(extractKwsKwStrings);
            extractPrjKwStrings.addAll(extractKwStrings);
            // 插入项目提取关键词
            for (String kw : extractTitleKwStrings) {
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.kGPubCooperationDao.insertIntoPsnRrjTitleKw(openId, prjId, language, StringUtils.trimToEmpty(kw),
                        0);
            }
            for (String kw : extractKwsKwStrings) {
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.kGPubCooperationDao.insertIntoPsnRrjSplitKw(openId, prjId, language, StringUtils.trimToEmpty(kw));
            }
            for (String kw : extractKwStrings) {
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.kGPubCooperationDao.insertIntoPsnRrjKw(openId, prjId, language, StringUtils.trimToEmpty(kw));
            }
        }
        for (String kw : extractPrjKwStrings) {
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            this.kGPubCooperationDao.insertIntoPsnRrjKwAll(openId, language, StringUtils.trimToEmpty(kw), 0);
        }


        // 计算成果关键词
        List<BigDecimal> pubIdList = this.projectDataFiveYearDao.getPdwhPubListByPsnId(snsPsnId);
        Set<String> extractPubKwStrings = new TreeSet<String>();
        List<NsfcKwTfCotfForSorting> listPubAll = new ArrayList<NsfcKwTfCotfForSorting>();
        if (pubIdList != null && pubIdList.size() > 0) {
            for (BigDecimal pubIdDe : pubIdList) {
                Long pubId = pubIdDe.longValue();
                try {
                    String xml = projectDataFiveYearService.findPatenXml(pubId);
                    if (StringUtils.isEmpty(xml)) {
                        continue;
                    }
                    PubXmlDocument doc = new PubXmlDocument(xml);
                    Element ele = (Element) doc.getNode(PubXmlConstants.PUBLICATION_XPATH);

                    // 获取成果作者数
                    Integer authorNum = 1;
                    String authors = StringUtils.trimToEmpty(ele.attributeValue("author_names"));
                    if (StringUtils.isBlank(authors)) {
                        authors = StringUtils.trimToEmpty(ele.attributeValue("authors_names_spec"));
                    }
                    if (StringUtils.isNotEmpty(authors)) {
                        // 中文作者没有本人，则继续不作计算
                        /*
                         * if (authors.indexOf(psnName) < 0) { continue; }
                         */
                        authors = authors.replace("；", ";");
                        String[] authorCollection = authors.split(";");
                        if (authorCollection == null || authorCollection.length == 1) {
                            authors = authors.replace("，", ",");
                            authorCollection = authors.split(",");
                        }
                        authorNum = authorCollection.length;
                    }

                    String ctitle = StringUtils.trimToEmpty(ele.attributeValue("ctitle"));
                    String ckeywords = StringUtils.trimToEmpty(ele.attributeValue("ckeywords"));
                    if (StringUtils.isBlank(ctitle) || !XmlUtil.isChinese(ctitle)) {
                        if (XmlUtil.isChinese(StringUtils.trimToEmpty(ele.attributeValue("etitle")))) {
                            ctitle = StringUtils.trimToEmpty(ele.attributeValue("etitle"));
                        }
                    }
                    if (StringUtils.isBlank(ckeywords) || !XmlUtil.isChinese(ckeywords)) {
                        if (XmlUtil.isChinese(StringUtils.trimToEmpty(ele.attributeValue("ckeywords")))) {
                            ckeywords = StringUtils.trimToEmpty(ele.attributeValue("ckeywords"));
                        }
                    }
                    String content = ctitle + ckeywords;
                    if (StringUtils.isEmpty(content)) {
                        continue;
                    }
                    // 按照authorNum计算TF
                    Set<String> extractKwStrings = new TreeSet<String>();
                    extractKwStrings = this.getRsSetNew(content);
                    Double type = 1.000 / authorNum;
                    List<NsfcKwTfCotfForSorting> listPub = sortExtractKwStringNewNoWeight(extractKwStrings, type, null);
                    if (CollectionUtils.isNotEmpty(listPub)) {
                        // 插入成果关键词提取结果
                        listPubAll.addAll(listPub);
                        for (NsfcKwTfCotfForSorting nsfckw : listPub) {
                            Integer language = XmlUtil.isChinese(nsfckw.getKeywords()) ? 0 : 1;
                            this.kGPubCooperationDao.insertIntoPsnPubKw(openId, pubId, language,
                                    StringUtils.trimToEmpty(nsfckw.getKeywords()), nsfckw.getIsiTf());
                        }
                    }

                } catch (Exception e) {
                    logger.error("计算个人关键词--提取成果关键词组合出错  pubid ：" + pubId, e);
                }
            }
        }
        // 集中最后成果的TF与结果
        Map<String, Double> rsPubMap = new HashMap<String, Double>();
        if (listPubAll.size() > 0) {
            for (NsfcKwTfCotfForSorting nsfcKw : listPubAll) {
                if (extractPubKwStrings.size() >= 500) {
                    break;
                }
                if (StringUtils.isEmpty(nsfcKw.getKeywords())) {
                    continue;
                }
                Double tf = rsPubMap.get(nsfcKw.getKeywords());
                extractPubKwStrings.add(nsfcKw.getKeywords());
                if (tf == null) {
                    rsPubMap.put(nsfcKw.getKeywords(), nsfcKw.getIsiTf());
                } else {
                    tf = tf + nsfcKw.getIsiTf();
                    rsPubMap.put(nsfcKw.getKeywords(), tf);
                }
            }
        }
        for (Entry<String, Double> kw : rsPubMap.entrySet()) {
            Integer language = XmlUtil.isChinese(kw.getKey()) ? 0 : 1;
            this.kGPubCooperationDao.insertIntoPsnPubKwAll(openId, language, StringUtils.trimToEmpty(kw.getKey()),
                    kw.getValue());
        }

        // 计算项目关键词与成果补充关键词的COTF，用于过滤成果关键词
        List<String> extractKws = this.projectDataFiveYearDao.getKwByTf(extractPrjKwStrings, extractPubKwStrings, 2);
        List<Map<String, Object>> rsMap =
                this.projectDataFiveYearDao.getKwByCoTfCout(extractPrjKwStrings, extractPubKwStrings, 2);
        for (Map<String, Object> kwMp : rsMap) {
            String kw = (String) kwMp.get("KW_STR");
            double cotf = kwMp.get("COUNTS") == null ? 0.0 : ((BigDecimal) kwMp.get("COUNTS")).doubleValue();
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            this.kGPubCooperationDao.insertIntoPsnPubKwAllCotf(openId, language, StringUtils.trimToEmpty(kw), cotf);
        }

        List<String> rs = new ArrayList<String>();
        // 优先添加项目关键词
        for (String kw : extractPrjKwStrings) {
            rs.add(kw);
        }
        for (String kw : extractKws) {
            rs.add(kw);
        }
        return rs;
    }

    // 为ISIS提供成果自填关键词，特殊处理
    private List<String> getPsnPrjAndPubKwsByContentTf(Long openId) {
        // 通过SNS-TOKEN, OPENID获得SNS_PSN_ID
        Long snsPsnId = this.openUserUnionDao.getPsnIdByOpenIdAndToken(openId, "2bcca485");
        List<Long> isisPsnId = this.kGPubCooperationDao.getPsnIsisId(openId);
        if (snsPsnId == null || snsPsnId == 0L) {
            return null;
        }
        Integer everagePubNum = this.kGPubCooperationDao.getPsnCategoriesByNsfcPrjToHandleList(isisPsnId);
        // 如果为0，则设置为10成果平均最大值
        everagePubNum = everagePubNum == 0 ? 70 : everagePubNum;
        /*
         * List<String> prjIdList = new ArrayList<String>(); // 设置为测试账号 prjIdList =
         * this.kGPubCooperationDao.getPsnNsfcPrjToHandleList(isisPsnId); if (prjIdList.size() <= 0) {
         * return null; }
         */
        this.kGPubCooperationDao.deletePsnPdwhPubKws(openId);

        // 计算成果关键词：优先取近5年成果，如果成果数量不够，再近10年成果（不超过统计的5年成果平均数）
        Integer pastYear = Calendar.getInstance().get(Calendar.YEAR) - 5;
        Long pubNum = this.projectDataFiveYearDao.getPdwhPubNumByPsnId(snsPsnId, pastYear);
        List<BigDecimal> pubIdList = this.projectDataFiveYearDao.getPdwhPubListByPsnId(snsPsnId, pastYear, null);
        if (pubNum < everagePubNum) {
            pastYear = pastYear - 5;
            // 最多取到前10年的成果
            pubIdList = this.projectDataFiveYearDao.getPdwhPubListByPsnId(snsPsnId, pastYear, everagePubNum);
            if (pubIdList != null && pubIdList.size() < everagePubNum) {
                pubIdList = this.projectDataFiveYearDao.getPdwhPubListByPsnId(snsPsnId, everagePubNum);
            }
        }
        Set<String> extractPubKwStrings = new TreeSet<String>();
        Map<String, Double> extractPubKwMapAll = new HashMap<String, Double>();
        if (pubIdList != null && pubIdList.size() > 0) {
            for (BigDecimal pubIdDe : pubIdList) {
                Long pubId = pubIdDe.longValue();
                try {
                    String xml = projectDataFiveYearService.findPatenXml(pubId);
                    if (StringUtils.isEmpty(xml)) {
                        continue;
                    }
                    PubXmlDocument doc = new PubXmlDocument(xml);
                    Element ele = (Element) doc.getNode(PubXmlConstants.PUBLICATION_XPATH);

                    // 获取成果作者数
                    Integer authorNum = 1;
                    String authors = StringUtils.trimToEmpty(ele.attributeValue("author_names"));
                    if (StringUtils.isBlank(authors)) {
                        authors = StringUtils.trimToEmpty(ele.attributeValue("authors_names_spec"));
                    }
                    if (StringUtils.isNotEmpty(authors)) {
                        // 中文作者没有本人，则继续不作计算
                        /*
                         * if (authors.indexOf(psnName) < 0) { continue; }
                         */
                        authors = authors.replace("；", ";");
                        String[] authorCollection = authors.split(";");
                        if (authorCollection == null || authorCollection.length == 1) {
                            authors = authors.replace("，", ",");
                            authorCollection = authors.split(",");
                        }
                        authorNum = authorCollection.length;
                    }
                    Double auNum = authorNum.doubleValue();
                    String ctitle = "";
                    /*
                     * String ctitle = StringUtils.trimToEmpty(ele.attributeValue("ctitle")); if
                     * (StringUtils.isBlank(ctitle) || !XmlUtil.isChinese(ctitle)) { if
                     * (XmlUtil.isChinese(StringUtils.trimToEmpty(ele.attributeValue("etitle")))) { ctitle =
                     * StringUtils.trimToEmpty(ele.attributeValue("etitle")); } }
                     */
                    String ckeywords = StringUtils.trimToEmpty(ele.attributeValue("ckeywords"));
                    // if (StringUtils.isBlank(ckeywords) || !XmlUtil.isChinese(ckeywords)) {
                    if (StringUtils.isBlank(ckeywords)) {
                        ckeywords = StringUtils.trimToEmpty(ele.attributeValue("ekeywords"));
                        /*
                         * if (XmlUtil.isChinese(StringUtils.trimToEmpty(ele.attributeValue("ekeywords")))) { ckeywords
                         * = StringUtils.trimToEmpty(ele.attributeValue("ekeywords")); }
                         */
                    }
                    if (StringUtils.isEmpty(ctitle) && StringUtils.isEmpty(ckeywords)) {
                        continue;
                    }
                    // 按照authorNum计算TF
                    Set<String> extractKwsKwStrings = new TreeSet<String>();
                    Map<String, Double> extractKwMap = new HashMap<String, Double>();
                    if (StringUtils.isNotBlank(ckeywords)) {
                        ckeywords = ckeywords.replace("；", ";");
                        ckeywords = ckeywords.replace("，", ",");
                        String[] kws = ckeywords.split(";");
                        if (kws == null || kws.length <= 0) {
                            kws = ckeywords.split(",");
                        }
                        if (kws != null) {
                            for (String str : kws) {
                                if (StringUtils.isNotEmpty(str)) {
                                    str = StringUtils.trimToEmpty(str);
                                    str = str.toLowerCase().replaceAll("\\s+", " ");
                                    extractKwsKwStrings.add(str);
                                }
                            }
                        }
                    }
                    // 添加成果自填关键词
                    for (String kwsKw : extractKwsKwStrings) {
                        Double tf = extractKwMap.get(kwsKw) == null ? 0.0 : extractKwMap.get(kwsKw);
                        Double wtf = new BigDecimal(1 / auNum).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                        if (tf == 0.0 || tf.equals(0.0)) {
                            extractKwMap.put(kwsKw, wtf);
                        } else {
                            extractKwMap.put(kwsKw, tf + wtf);
                        }
                        // 插入成果自填关键词
                        Integer language = XmlUtil.isChinese(kwsKw) ? 0 : 1;
                        this.kGPubCooperationDao.insertIntoPsnPubKeywordsKw(openId, pubId, language, kwsKw, authorNum);
                    }
                    // 合并以后的成果标题提取关键词与成果自填关键词
                    for (Entry<String, Double> et : extractKwMap.entrySet()) {
                        Double tf = et.getValue();
                        String kw = et.getKey();
                        Double sumTf = extractPubKwMapAll.get(kw);
                        if (sumTf == null || sumTf == 0 || sumTf.equals(0.0)) {
                            extractPubKwMapAll.put(kw, tf);
                        } else {
                            sumTf = tf + sumTf;
                            extractPubKwMapAll.put(kw, sumTf);
                        }
                        // 插入合并以后的成果关键词, 在此处不做字符长度惩罚
                        Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                        this.kGPubCooperationDao.insertIntoPsnPubKw(openId, pubId, language, kw, tf);
                    }
                } catch (Exception e) {
                    logger.error("计算个人关键词--提取成果关键词组合出错  pubid ：" + pubId, e);
                }
            }
        }
        // 集中最后成果的TF与结果
        List<String> rs = new ArrayList<String>();
        for (Entry<String, Double> kw : extractPubKwMapAll.entrySet()) {
            Integer language = XmlUtil.isChinese(kw.getKey()) ? 0 : 1;
            String kwStr = kw.getKey();
            if (StringUtils.isEmpty(kwStr)) {
                continue;
            }
            Double wtf = kw.getValue();
            /*
             * if (language == 0 && kwStr.length() <= 9 && kwStr.length() >= 2) { wtf = kw.getValue() * 6; }
             */
            extractPubKwStrings.add(kwStr);
            rs.add(kwStr);
            this.kGPubCooperationDao.insertIntoPsnPubKwAll(openId, language, StringUtils.trimToEmpty(kw.getKey()), wtf);
        }
        return rs;
    }

    // 正常通过cotf计算获取关键词信息--备份;前一个作为为ISIS提供成果自填关键词，特别处理过。
    private List<String> getPsnPrjAndPubKwsByContentTf_BAK(Long openId) {
        // 通过SNS-TOKEN, OPENID获得SNS_PSN_ID
        Long snsPsnId = this.openUserUnionDao.getPsnIdByOpenIdAndToken(openId, "2bcca485");
        List<Long> isisPsnId = this.kGPubCooperationDao.getPsnIsisId(openId);
        if (snsPsnId == null || snsPsnId == 0L) {
            return null;
        }
        String psnName = this.kGPubCooperationDao.getPsnIsisName(openId);
        List<String> prjIdList = new ArrayList<String>();
        // 设置为测试账号
        prjIdList = this.kGPubCooperationDao.getPsnNsfcPrjToHandleList(isisPsnId);
        if (prjIdList.size() <= 0) {
            return null;
        }
        this.kGPubCooperationDao.deletePsnPrjPubKw(openId);
        // 项目关键词
        Set<String> extractPrjKwStrings = new TreeSet<String>();
        Set<String> extractPrjTitleKwStrings = new TreeSet<String>();
        Set<String> extractPrjSelfKwStrings = new TreeSet<String>();
        Map<String, Integer> extractTitleKwMapAll = new HashMap<String, Integer>();
        for (String prjId : prjIdList) {
            Map<String, Object> mp = this.kGPubCooperationDao.getNsfcPrjInfo(prjId);
            String title = mp.get("ZH_TITLE") == null ? "" : (String) mp.get("ZH_TITLE");
            String kwsStr = mp.get("ZH_KEYWORDS") == null ? "" : (String) mp.get("ZH_KEYWORDS");
            Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
            Set<String> extractKwStrings = new TreeSet<String>();
            Set<String> extractTitleKwStrings = new TreeSet<String>();
            Set<String> extractKwsKwStrings = new TreeSet<String>();
            extractTitleKwMap = this.extractKwAndTfInContent(title);
            extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");
            extractPrjTitleKwStrings.addAll(extractTitleKwStrings);
            if (StringUtils.isNotBlank(kwsStr)) {
                kwsStr = kwsStr.replace("；", ";");
                String[] kws = kwsStr.split(";");
                if (kws != null) {
                    for (String str : kws) {
                        if (StringUtils.isNotEmpty(str)) {
                            str = StringUtils.trimToEmpty(str);
                            str = str.toLowerCase().replaceAll("\\s+", " ");
                            extractKwsKwStrings.add(str);
                        }
                    }
                }
            }
            extractKwStrings.addAll(extractTitleKwStrings);
            extractKwStrings.addAll(extractKwsKwStrings);
            extractPrjSelfKwStrings.addAll(extractKwsKwStrings);
            extractPrjKwStrings.addAll(extractKwStrings);
            // 对所有项目标题计算合并真实tf
            for (String titleKw : extractTitleKwStrings) {
                Integer tf = extractTitleKwMapAll.get(titleKw);
                if (tf != null) {
                    tf = tf + (extractTitleKwMap.get(titleKw) == null ? 0 : (Integer) extractTitleKwMap.get(titleKw));
                } else {
                    tf = extractTitleKwMap.get(titleKw) == null ? 0 : (Integer) extractTitleKwMap.get(titleKw);
                }
                extractTitleKwMapAll.put(titleKw, tf);
            }
            // 合并项目自填关键词
            for (String kwsKw : extractKwsKwStrings) {
                Integer tf = extractTitleKwMapAll.get(kwsKw);
                if (tf != null) {
                    tf = tf + 1;
                } else {
                    tf = 1;
                }
                extractTitleKwMapAll.put(kwsKw, tf);
            }

            // 插入项目提取关键词与TF
            for (String kw : extractTitleKwStrings) {
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.kGPubCooperationDao.insertIntoPsnRrjTitleKw(openId, prjId, language, StringUtils.trimToEmpty(kw),
                        extractTitleKwMap.get(kw) == null ? 0 : (Integer) extractTitleKwMap.get(kw));
            }
            for (String kw : extractKwsKwStrings) {
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.kGPubCooperationDao.insertIntoPsnRrjSplitKw(openId, prjId, language, StringUtils.trimToEmpty(kw));
            }
            for (String kw : extractKwStrings) {
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.kGPubCooperationDao.insertIntoPsnRrjKw(openId, prjId, language, StringUtils.trimToEmpty(kw));
            }
        }
        List<NsfcKwTfCotfForSorting> prjKws = new ArrayList<NsfcKwTfCotfForSorting>();
        for (String kw : extractPrjKwStrings) {
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            Integer tf = extractTitleKwMapAll.get(kw) == null ? 0 : extractTitleKwMapAll.get(kw);
            if (language == 0 && kw.length() <= 8 && kw.length() >= 2) {
                tf = tf * 6;
            }
            if (extractPrjSelfKwStrings.contains(kw)) {
                tf = tf * 1000;
            }
            this.kGPubCooperationDao.insertIntoPsnRrjKwAll(openId, language, StringUtils.trimToEmpty(kw), tf);
            // 用于排序
            NsfcKwTfCotfForSorting nsKw = new NsfcKwTfCotfForSorting(kw, kw.length(), tf.doubleValue());
            prjKws.add(nsKw);
        }


        // 计算成果关键词
        List<BigDecimal> pubIdList = this.projectDataFiveYearDao.getPdwhPubListByPsnId(snsPsnId);
        Set<String> extractPubKwStrings = new TreeSet<String>();
        Map<String, Double> extractPubKwMapAll = new HashMap<String, Double>();
        if (pubIdList != null && pubIdList.size() > 0) {
            for (BigDecimal pubIdDe : pubIdList) {
                Long pubId = pubIdDe.longValue();
                try {
                    String xml = projectDataFiveYearService.findPatenXml(pubId);
                    if (StringUtils.isEmpty(xml)) {
                        continue;
                    }
                    PubXmlDocument doc = new PubXmlDocument(xml);
                    Element ele = (Element) doc.getNode(PubXmlConstants.PUBLICATION_XPATH);

                    // 获取成果作者数
                    Integer authorNum = 1;
                    String authors = StringUtils.trimToEmpty(ele.attributeValue("author_names"));
                    if (StringUtils.isBlank(authors)) {
                        authors = StringUtils.trimToEmpty(ele.attributeValue("authors_names_spec"));
                    }
                    if (StringUtils.isNotEmpty(authors)) {
                        // 中文作者没有本人，则继续不作计算
                        /*
                         * if (authors.indexOf(psnName) < 0) { continue; }
                         */
                        authors = authors.replace("；", ";");
                        String[] authorCollection = authors.split(";");
                        if (authorCollection == null || authorCollection.length == 1) {
                            authors = authors.replace("，", ",");
                            authorCollection = authors.split(",");
                        }
                        authorNum = authorCollection.length;
                    }
                    Double auNum = authorNum.doubleValue();
                    String ctitle = "";
                    /*
                     * String ctitle = StringUtils.trimToEmpty(ele.attributeValue("ctitle")); if
                     * (StringUtils.isBlank(ctitle) || !XmlUtil.isChinese(ctitle)) { if
                     * (XmlUtil.isChinese(StringUtils.trimToEmpty(ele.attributeValue("etitle")))) { ctitle =
                     * StringUtils.trimToEmpty(ele.attributeValue("etitle")); } }
                     */
                    String ckeywords = StringUtils.trimToEmpty(ele.attributeValue("ckeywords"));
                    // if (StringUtils.isBlank(ckeywords) || !XmlUtil.isChinese(ckeywords)) {
                    if (StringUtils.isBlank(ckeywords)) {
                        ckeywords = StringUtils.trimToEmpty(ele.attributeValue("ekeywords"));
                        /*
                         * if (XmlUtil.isChinese(StringUtils.trimToEmpty(ele.attributeValue("ekeywords")))) { ckeywords
                         * = StringUtils.trimToEmpty(ele.attributeValue("ekeywords")); }
                         */
                    }
                    if (StringUtils.isEmpty(ctitle) && StringUtils.isEmpty(ckeywords)) {
                        continue;
                    }
                    // 按照authorNum计算TF
                    Set<String> extractKwStrings = new TreeSet<String>();
                    Set<String> extractTitleKwStrings = new TreeSet<String>();
                    Set<String> extractKwsKwStrings = new TreeSet<String>();
                    Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
                    Map<String, Double> extractKwMap = new HashMap<String, Double>();
                    extractTitleKwMap = this.extractKwAndTfInContent(ctitle);
                    if (extractTitleKwMap.get("EXTRACT_KWS_STRSETS") != null) {
                        extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");
                    }
                    if (StringUtils.isNotBlank(ckeywords)) {
                        ckeywords = ckeywords.replace("；", ";");
                        ckeywords = ckeywords.replace("，", ",");
                        String[] kws = ckeywords.split(";");
                        if (kws == null || kws.length <= 0) {
                            kws = ckeywords.split(",");
                        }
                        if (kws != null) {
                            for (String str : kws) {
                                if (StringUtils.isNotEmpty(str)) {
                                    str = StringUtils.trimToEmpty(str);
                                    str = str.toLowerCase().replaceAll("\\s+", " ");
                                    extractKwsKwStrings.add(str);
                                }
                            }
                        }
                    }
                    if (extractTitleKwStrings != null && extractTitleKwStrings.size() > 0) {
                        extractKwStrings.addAll(extractTitleKwStrings);
                    }
                    if (extractKwsKwStrings != null && extractKwsKwStrings.size() > 0) {
                        extractKwStrings.addAll(extractKwsKwStrings);
                    }
                    // extractPubKwStrings.addAll(extractKwStrings);
                    // 添加成果Title关键词与TF
                    for (String titleKw : extractTitleKwStrings) {
                        Integer tf =
                                extractTitleKwMap.get(titleKw) == null ? 0 : (Integer) (extractTitleKwMap.get(titleKw));
                        Double wtf = new BigDecimal(tf / auNum).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                        if (tf == 0) {
                            continue;
                        }
                        extractKwMap.put(titleKw, wtf);
                        // 插入单个成果关键词
                        Integer language = XmlUtil.isChinese(titleKw) ? 0 : 1;
                        this.kGPubCooperationDao.insertIntoPsnPubTitleKw(openId, pubId, language, titleKw, wtf);
                    }
                    // 添加成果自填关键词
                    for (String kwsKw : extractKwsKwStrings) {

                        /*
                         * if (StringUtils.isEmpty(kwsKw) || !XmlUtil.isChinese(kwsKw)) { continue; }
                         */

                        Double tf = extractKwMap.get(kwsKw) == null ? 0.0 : extractKwMap.get(kwsKw);
                        Double wtf = new BigDecimal(1 / auNum).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                        if (tf == 0.0 || tf.equals(0.0)) {
                            extractKwMap.put(kwsKw, wtf);
                        } else {
                            extractKwMap.put(kwsKw, tf + wtf);
                        }
                        // 插入成果自填关键词
                        Integer language = XmlUtil.isChinese(kwsKw) ? 0 : 1;
                        this.kGPubCooperationDao.insertIntoPsnPubKeywordsKw(openId, pubId, language, kwsKw, authorNum);
                    }
                    // 合并以后的成果标题提取关键词与成果自填关键词
                    for (Entry<String, Double> et : extractKwMap.entrySet()) {
                        Double tf = et.getValue();
                        String kw = et.getKey();
                        Double sumTf = extractPubKwMapAll.get(kw);
                        if (sumTf == null || sumTf == 0 || sumTf.equals(0.0)) {
                            extractPubKwMapAll.put(kw, tf);
                        } else {
                            sumTf = tf + sumTf;
                            extractPubKwMapAll.put(kw, sumTf);
                        }
                        // 插入合并以后的成果关键词
                        Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                        this.kGPubCooperationDao.insertIntoPsnPubKw(openId, pubId, language, kw, tf);
                        /*
                         * if (language == 0 && kw.length() <= 9 && kw.length() >= 2) {
                         * this.kGPubCooperationDao.insertIntoPsnPubKw(openId, pubId, language, kw, tf * 6); } else {
                         * this.kGPubCooperationDao.insertIntoPsnPubKw(openId, pubId, language, kw, tf); }
                         */

                    }
                } catch (Exception e) {
                    logger.error("计算个人关键词--提取成果关键词组合出错  pubid ：" + pubId, e);
                }
            }
        }
        // 集中最后成果的TF与结果
        Map<String, Double> pubKwMapAllFilter = new HashMap<String, Double>();
        for (Entry<String, Double> kw : extractPubKwMapAll.entrySet()) {
            Integer language = XmlUtil.isChinese(kw.getKey()) ? 0 : 1;
            String kwStr = kw.getKey();
            if (StringUtils.isEmpty(kwStr)) {
                continue;
            }
            Double wtf = kw.getValue();
            /*
             * if (language == 0 && kwStr.length() <= 9 && kwStr.length() >= 2) { wtf = kw.getValue() * 6; }
             */
            // 标记在项目成果中已经存在的关键词
            if (extractPrjKwStrings.contains(kwStr)) {
                // wtf = wtf * 1000;
            } else {
                // 已经在标题中出现的，不再计算
                extractPubKwStrings.add(kwStr);
            }
            this.kGPubCooperationDao.insertIntoPsnPubKwAll(openId, language, StringUtils.trimToEmpty(kw.getKey()), wtf);
            pubKwMapAllFilter.put(kwStr, wtf);
        }

        // 计算项目关键词与成果补充关键词的COTF，用于过滤成果关键词
        List<NsfcKwTfCotfForSorting> listPubKwsWithCotf = new ArrayList<NsfcKwTfCotfForSorting>();
        List<String> extractKws = new ArrayList<String>();
        List<Map<String, Object>> rsMap = new ArrayList<Map<String, Object>>();
        if (extractPrjKwStrings.size() != 0 || extractPubKwStrings.size() != 0) {
            extractKws = this.projectDataFiveYearDao.getKwByTf(extractPrjKwStrings, extractPubKwStrings, 2);
            rsMap = this.projectDataFiveYearDao.getKwByCoTfCout(extractPrjKwStrings, extractPubKwStrings, 2);
        }
        for (Map<String, Object> kwMp : rsMap) {
            String kw = (String) kwMp.get("KW_STR");
            double cotf =
                    kwMp.get("COUNTS") == null ? 0.0 : ((BigDecimal) kwMp.get("COUNTS")).setScale(3).doubleValue();
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            this.kGPubCooperationDao.insertIntoPsnPubKwAllCotf(openId, language, StringUtils.trimToEmpty(kw), cotf);
            listPubKwsWithCotf.add(new NsfcKwTfCotfForSorting(kw, kw.length(),
                    pubKwMapAllFilter.get(kw) == null ? 0.0 : pubKwMapAllFilter.get(kw), cotf));
        }
        List<NsfcKwTfCotfForSorting> listPubKwsWithoutCotf = new ArrayList<NsfcKwTfCotfForSorting>();
        for (String pubKw : extractPubKwStrings) {
            // 剔除出已经有COTF的词，参与后续排序
            if (extractKws.contains(pubKw)) {
                continue;
            }
            Double tf = pubKwMapAllFilter.get(pubKw);
            if (tf == null) {
                continue;
            }
            NsfcKwTfCotfForSorting nsKw = new NsfcKwTfCotfForSorting(pubKw, pubKw.length(), tf);
            listPubKwsWithoutCotf.add(nsKw);
        }

        if (listPubKwsWithoutCotf.size() > 0) {
            Collections.sort(listPubKwsWithoutCotf);
        }
        if (listPubKwsWithCotf.size() > 0) {
            Collections.sort(listPubKwsWithCotf);
        }
        if (prjKws.size() > 0) {
            Collections.sort(prjKws);
        }

        List<String> rs = new ArrayList<String>();
        // 优先添加项目关键词
        for (NsfcKwTfCotfForSorting kw : prjKws) {
            rs.add(kw.getKeywords());
        }
        for (NsfcKwTfCotfForSorting kw : listPubKwsWithCotf) {
            rs.add(kw.getKeywords());
        }
        for (NsfcKwTfCotfForSorting kw : listPubKwsWithoutCotf) {
            rs.add(kw.getKeywords());
        }
        return rs;
    }

    private List<String> getPsnPdwhPubKws(Long openId) {

        // 通过SNS-TOKEN, OPENID获得SNS_PSN_ID
        Long snsPsnId = this.openUserUnionDao.getPsnIdByOpenIdAndToken(openId, "2bcca485");
        List<Long> isisPsnId = this.kGPubCooperationDao.getPsnIsisId(openId);
        if (snsPsnId == null || snsPsnId == 0L) {
            return null;
        }
        String psnName = this.kGPubCooperationDao.getPsnIsisName(openId);
        List<String> prjIdList = new ArrayList<String>();
        // 设置为测试账号
        prjIdList = this.kGPubCooperationDao.getPsnNsfcPrjToHandleList(isisPsnId);
        if (prjIdList.size() <= 0) {
            return null;
        }
        this.kGPubCooperationDao.deletePsnPrjPubKw(openId);
        // 项目关键词
        Set<String> extractPrjKwStrings = new TreeSet<String>();
        Set<String> extractPrjTitleKwStrings = new TreeSet<String>();
        Set<String> extractPrjSelfKwStrings = new TreeSet<String>();
        Map<String, Integer> extractTitleKwMapAll = new HashMap<String, Integer>();
        for (String prjId : prjIdList) {
            Map<String, Object> mp = this.kGPubCooperationDao.getNsfcPrjInfo(prjId);
            String title = mp.get("ZH_TITLE") == null ? "" : (String) mp.get("ZH_TITLE");
            String kwsStr = mp.get("ZH_KEYWORDS") == null ? "" : (String) mp.get("ZH_KEYWORDS");
            Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
            Set<String> extractKwStrings = new TreeSet<String>();
            Set<String> extractTitleKwStrings = new TreeSet<String>();
            Set<String> extractKwsKwStrings = new TreeSet<String>();
            extractTitleKwMap = this.extractKwAndTfInContent(title);
            extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");
            extractPrjTitleKwStrings.addAll(extractTitleKwStrings);
            if (StringUtils.isNotBlank(kwsStr)) {
                kwsStr = kwsStr.replace("；", ";");
                String[] kws = kwsStr.split(";");
                if (kws != null) {
                    for (String str : kws) {
                        if (StringUtils.isNotEmpty(str)) {
                            str = StringUtils.trimToEmpty(str);
                            str = str.toLowerCase().replaceAll("\\s+", " ");
                            extractKwsKwStrings.add(str);
                        }
                    }
                }
            }
            extractKwStrings.addAll(extractTitleKwStrings);
            extractKwStrings.addAll(extractKwsKwStrings);
            extractPrjSelfKwStrings.addAll(extractKwsKwStrings);
            extractPrjKwStrings.addAll(extractKwStrings);
            // 对所有项目标题计算合并真实tf
            for (String titleKw : extractTitleKwStrings) {
                Integer tf = extractTitleKwMapAll.get(titleKw);
                if (tf != null) {
                    tf = tf + (extractTitleKwMap.get(titleKw) == null ? 0 : (Integer) extractTitleKwMap.get(titleKw));
                } else {
                    tf = extractTitleKwMap.get(titleKw) == null ? 0 : (Integer) extractTitleKwMap.get(titleKw);
                }
                extractTitleKwMapAll.put(titleKw, tf);
            }
            // 合并项目自填关键词
            for (String kwsKw : extractKwsKwStrings) {
                Integer tf = extractTitleKwMapAll.get(kwsKw);
                if (tf != null) {
                    tf = tf + 1;
                } else {
                    tf = 1;
                }
                extractTitleKwMapAll.put(kwsKw, tf);
            }

            // 插入项目提取关键词与TF
            for (String kw : extractTitleKwStrings) {
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.kGPubCooperationDao.insertIntoPsnRrjTitleKw(openId, prjId, language, StringUtils.trimToEmpty(kw),
                        extractTitleKwMap.get(kw) == null ? 0 : (Integer) extractTitleKwMap.get(kw));
            }
            for (String kw : extractKwsKwStrings) {
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.kGPubCooperationDao.insertIntoPsnRrjSplitKw(openId, prjId, language, StringUtils.trimToEmpty(kw));
            }
            for (String kw : extractKwStrings) {
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.kGPubCooperationDao.insertIntoPsnRrjKw(openId, prjId, language, StringUtils.trimToEmpty(kw));
            }
        }
        List<NsfcKwTfCotfForSorting> prjKws = new ArrayList<NsfcKwTfCotfForSorting>();
        for (String kw : extractPrjKwStrings) {
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            Integer tf = extractTitleKwMapAll.get(kw) == null ? 0 : extractTitleKwMapAll.get(kw);
            if (language == 0 && kw.length() <= 8 && kw.length() >= 2) {
                tf = tf * 6;
            }
            if (extractPrjSelfKwStrings.contains(kw)) {
                tf = tf * 1000;
            }
            this.kGPubCooperationDao.insertIntoPsnRrjKwAll(openId, language, StringUtils.trimToEmpty(kw), tf);
            // 用于排序
            NsfcKwTfCotfForSorting nsKw = new NsfcKwTfCotfForSorting(kw, kw.length(), tf.doubleValue());
            prjKws.add(nsKw);
        }

        // 计算成果关键词：优先取近5年成果，如果成果数量不够，再近10年成果
        Integer everagePubNum = 10;
        Integer pastYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 5;
        Long pubNum = this.projectDataFiveYearDao.getPdwhPubNumByPsnId(snsPsnId, pastYear);
        List<BigDecimal> pubIdList = this.projectDataFiveYearDao.getPdwhPubListByPsnId(snsPsnId, pastYear, null);
        if (pubNum < everagePubNum) {
            pastYear = pastYear - 5;
            pubIdList = this.projectDataFiveYearDao.getPdwhPubListByPsnId(snsPsnId, pastYear, everagePubNum);
        }
        Set<String> extractPubKwStrings = new TreeSet<String>();
        Map<String, Double> extractPubKwMapAll = new HashMap<String, Double>();
        if (pubIdList != null && pubIdList.size() > 0) {
            for (BigDecimal pubIdDe : pubIdList) {
                Long pubId = pubIdDe.longValue();
                try {
                    String xml = projectDataFiveYearService.findPatenXml(pubId);
                    if (StringUtils.isEmpty(xml)) {
                        continue;
                    }
                    PubXmlDocument doc = new PubXmlDocument(xml);
                    Element ele = (Element) doc.getNode(PubXmlConstants.PUBLICATION_XPATH);

                    // 获取成果作者数
                    Integer authorNum = 1;
                    String authors = StringUtils.trimToEmpty(ele.attributeValue("author_names"));
                    if (StringUtils.isBlank(authors)) {
                        authors = StringUtils.trimToEmpty(ele.attributeValue("authors_names_spec"));
                    }
                    if (StringUtils.isNotEmpty(authors)) {
                        // 中文作者没有本人，则继续不作计算
                        /*
                         * if (authors.indexOf(psnName) < 0) { continue; }
                         */
                        authors = authors.replace("；", ";");
                        String[] authorCollection = authors.split(";");
                        if (authorCollection == null || authorCollection.length == 1) {
                            authors = authors.replace("，", ",");
                            authorCollection = authors.split(",");
                        }
                        authorNum = authorCollection.length;
                    }
                    Double auNum = authorNum.doubleValue();
                    String ctitle = StringUtils.trimToEmpty(ele.attributeValue("ctitle"));
                    String ckeywords = StringUtils.trimToEmpty(ele.attributeValue("ckeywords"));
                    if (StringUtils.isBlank(ctitle) || !XmlUtil.isChinese(ctitle)) {
                        if (XmlUtil.isChinese(StringUtils.trimToEmpty(ele.attributeValue("etitle")))) {
                            ctitle = StringUtils.trimToEmpty(ele.attributeValue("etitle"));
                        }
                    }
                    if (StringUtils.isBlank(ckeywords) || !XmlUtil.isChinese(ckeywords)) {
                        if (XmlUtil.isChinese(StringUtils.trimToEmpty(ele.attributeValue("ekeywords")))) {
                            ckeywords = StringUtils.trimToEmpty(ele.attributeValue("ekeywords"));
                        }
                    }
                    if (StringUtils.isEmpty(ctitle) && StringUtils.isEmpty(ckeywords)) {
                        continue;
                    }
                    // 按照authorNum计算TF
                    Set<String> extractKwStrings = new TreeSet<String>();
                    Set<String> extractTitleKwStrings = new TreeSet<String>();
                    Set<String> extractKwsKwStrings = new TreeSet<String>();
                    Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
                    Map<String, Double> extractKwMap = new HashMap<String, Double>();
                    extractTitleKwMap = this.extractKwAndTfInContent(ctitle);
                    extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");

                    if (StringUtils.isNotBlank(ckeywords)) {
                        ckeywords = ckeywords.replace("；", ";");
                        ckeywords = ckeywords.replace("，", ",");
                        String[] kws = ckeywords.split(";");
                        if (kws == null || kws.length <= 0) {
                            kws = ckeywords.split(",");
                        }
                        if (kws != null) {
                            for (String str : kws) {
                                if (StringUtils.isNotEmpty(str)) {
                                    str = StringUtils.trimToEmpty(str);
                                    str = str.toLowerCase().replaceAll("\\s+", " ");
                                    extractKwsKwStrings.add(str);
                                }
                            }
                        }
                    }
                    extractKwStrings.addAll(extractTitleKwStrings);
                    extractKwStrings.addAll(extractKwsKwStrings);
                    // extractPubKwStrings.addAll(extractKwStrings);
                    // 添加成果Title关键词与TF
                    for (String titleKw : extractTitleKwStrings) {
                        Integer tf =
                                extractTitleKwMap.get(titleKw) == null ? 0 : (Integer) (extractTitleKwMap.get(titleKw));
                        Double wtf = new BigDecimal(tf / auNum).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                        if (tf == 0) {
                            continue;
                        }
                        extractKwMap.put(titleKw, wtf);
                        // 插入单个成果关键词
                        Integer language = XmlUtil.isChinese(titleKw) ? 0 : 1;
                        this.kGPubCooperationDao.insertIntoPsnPubTitleKw(openId, pubId, language, titleKw, wtf);
                    }
                    // 添加成果自填关键词
                    for (String kwsKw : extractKwsKwStrings) {

                        if (StringUtils.isEmpty(kwsKw) || !XmlUtil.isChinese(kwsKw)) {
                            continue;
                        }

                        Double tf = extractKwMap.get(kwsKw) == null ? 0.0 : extractKwMap.get(kwsKw);
                        Double wtf = new BigDecimal(1 / auNum).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                        if (tf == 0.0 || tf.equals(0.0)) {
                            extractKwMap.put(kwsKw, wtf);
                        } else {
                            extractKwMap.put(kwsKw, tf + wtf);
                        }
                        // 插入成果自填关键词
                        Integer language = XmlUtil.isChinese(kwsKw) ? 0 : 1;
                        this.kGPubCooperationDao.insertIntoPsnPubKeywordsKw(openId, pubId, language, kwsKw);
                    }
                    // 合并以后的成果标题提取关键词与成果自填关键词
                    for (Entry<String, Double> et : extractKwMap.entrySet()) {
                        Double tf = et.getValue();
                        String kw = et.getKey();
                        Double sumTf = extractPubKwMapAll.get(kw);
                        if (sumTf == null || sumTf == 0 || sumTf.equals(0.0)) {
                            extractPubKwMapAll.put(kw, tf);
                        } else {
                            sumTf = tf + sumTf;
                            extractPubKwMapAll.put(kw, sumTf);
                        }
                        // 插入合并以后的成果关键词
                        Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                        if (language == 0 && kw.length() <= 9 && kw.length() >= 2) {
                            this.kGPubCooperationDao.insertIntoPsnPubKw(openId, pubId, language, kw, tf * 6);
                        } else {
                            this.kGPubCooperationDao.insertIntoPsnPubKw(openId, pubId, language, kw, tf);
                        }

                    }
                } catch (Exception e) {
                    logger.error("计算个人关键词--提取成果关键词组合出错  pubid ：" + pubId, e);
                }
            }
        }
        // 集中最后成果的TF与结果
        Map<String, Double> pubKwMapAllFilter = new HashMap<String, Double>();
        for (Entry<String, Double> kw : extractPubKwMapAll.entrySet()) {
            Integer language = XmlUtil.isChinese(kw.getKey()) ? 0 : 1;
            String kwStr = kw.getKey();
            if (StringUtils.isEmpty(kwStr)) {
                continue;
            }
            Double wtf = kw.getValue();
            if (language == 0 && kwStr.length() <= 9 && kwStr.length() >= 2) {
                wtf = kw.getValue() * 6;
            }
            // 标记在项目成果中已经存在的关键词
            if (extractPrjKwStrings.contains(kwStr)) {
                wtf = wtf * 1000;
            } else {
                // 已经在标题中出现的，不再计算
                extractPubKwStrings.add(kwStr);
            }
            this.kGPubCooperationDao.insertIntoPsnPubKwAll(openId, language, StringUtils.trimToEmpty(kw.getKey()), wtf);
            pubKwMapAllFilter.put(kwStr, wtf);
        }

        // 计算项目关键词与成果补充关键词的COTF，用于过滤成果关键词
        List<NsfcKwTfCotfForSorting> listPubKwsWithCotf = new ArrayList<NsfcKwTfCotfForSorting>();
        List<String> extractKws = this.projectDataFiveYearDao.getKwByTf(extractPrjKwStrings, extractPubKwStrings, 2);
        List<Map<String, Object>> rsMap =
                this.projectDataFiveYearDao.getKwByCoTfCout(extractPrjKwStrings, extractPubKwStrings, 2);
        for (Map<String, Object> kwMp : rsMap) {
            String kw = (String) kwMp.get("KW_STR");
            double cotf =
                    kwMp.get("COUNTS") == null ? 0.0 : ((BigDecimal) kwMp.get("COUNTS")).setScale(3).doubleValue();
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            this.kGPubCooperationDao.insertIntoPsnPubKwAllCotf(openId, language, StringUtils.trimToEmpty(kw), cotf);
            listPubKwsWithCotf.add(new NsfcKwTfCotfForSorting(kw, kw.length(),
                    pubKwMapAllFilter.get(kw) == null ? 0.0 : pubKwMapAllFilter.get(kw), cotf));
        }
        List<NsfcKwTfCotfForSorting> listPubKwsWithoutCotf = new ArrayList<NsfcKwTfCotfForSorting>();
        for (String pubKw : extractPubKwStrings) {
            // 剔除出已经有COTF的词，参与后续排序
            if (extractKws.contains(pubKw)) {
                continue;
            }
            Double tf = pubKwMapAllFilter.get(pubKw);
            if (tf == null) {
                continue;
            }
            NsfcKwTfCotfForSorting nsKw = new NsfcKwTfCotfForSorting(pubKw, pubKw.length(), tf);
            listPubKwsWithoutCotf.add(nsKw);
        }

        Collections.sort(listPubKwsWithoutCotf);
        Collections.sort(listPubKwsWithCotf);
        Collections.sort(prjKws);

        List<String> rs = new ArrayList<String>();
        // 优先添加项目关键词
        for (NsfcKwTfCotfForSorting kw : prjKws) {
            rs.add(kw.getKeywords());
        }
        for (NsfcKwTfCotfForSorting kw : listPubKwsWithCotf) {
            rs.add(kw.getKeywords());
        }
        for (NsfcKwTfCotfForSorting kw : listPubKwsWithoutCotf) {
            rs.add(kw.getKeywords());
        }
        return rs;
    }


    @Override
    public Integer extractPrjKwsFromTitleAndAbsByTfForGaoxiao(Long pubId) {
        Integer rs = 1;
        try {
            List<NsfcKwScoreForSorting> kwsSet = this.extractKwsByCotfRestrictToContentZh(pubId);
            if (kwsSet == null || kwsSet.size() <= 0) {
                rs = 2;
            } else {
                this.kGPubCooperationDao.deleteGaoXiaoPrjKw(pubId);
                StringBuilder sb = new StringBuilder();
                Integer kwNum = kwsSet.size();
                for (NsfcKwScoreForSorting kw : kwsSet) {
                    if (StringUtils.isEmpty(kw.getKeywords())) {
                        continue;
                    }
                    sb.append(kw.getKeywords());
                    sb.append("; ");
                    Integer language = XmlUtil.isChinese(kw.getKeywords()) ? 0 : 1;
                    this.kGPubCooperationDao.insertIntoTitleAndKws(pubId, "1000", language, kw.getKeywords(),
                            kw.getPrjkwScore(), "GX");
                }
                String kwsStr = sb.substring(0, sb.length() - 2);
                if (kwsStr.length() > 1000) {
                    int last = sb.lastIndexOf(";");
                    kwsStr.substring(0, last - 1);
                    kwNum--;
                }
                this.nsfcKwForSortingDao.saveOrUpdateKGPrjKws(pubId, kwsStr, kwNum);
            }
        } catch (Exception e) {
            logger.error("基准关键词画像--关键词提取出错：", e);
            rs = 3;
        }
        return rs;
    }

    @Override
    public Integer extractPrjKwsFromTitleAndAbsByTf(Long pubId) {
        Integer rs = 1;
        try {
            List<String> kwsSet = this.extractKwsByCotfRestrictToContent(pubId);
            if (kwsSet == null || kwsSet.size() <= 0) {
                rs = 2;
            } else {
                StringBuilder sb = new StringBuilder();
                Integer kwNum = kwsSet.size();
                for (String kw : kwsSet) {
                    sb.append(kw);
                    sb.append("; ");
                }
                String kwsStr = sb.substring(0, sb.length() - 2);
                if (kwsStr.length() > 1000) {
                    int last = sb.lastIndexOf(";");
                    kwsStr.substring(0, last - 1);
                    kwNum--;
                }
                this.nsfcKwForSortingDao.saveOrUpdateKGPrjKws(pubId, kwsStr, kwNum);
            }
        } catch (Exception e) {
            logger.error("基准关键词画像--关键词提取出错：", e);
            rs = 3;
        }
        return rs;
    }

    @Override
    public Integer extractPrjKwsFromTitleAndAbsByTfEn(Long pubId) {
        Integer rs = 1;
        try {
            List<String> kwsSet = this.extractKwsByCotfRestrictToContentEn(pubId);
            if (kwsSet == null || kwsSet.size() <= 0) {
                rs = 2;
            } else {
                StringBuilder sb = new StringBuilder();
                Integer kwNum = kwsSet.size();
                for (String kw : kwsSet) {
                    sb.append(kw);
                    sb.append("; ");
                }
                String kwsStr = sb.substring(0, sb.length() - 2);
                if (kwsStr.length() > 1000) {
                    int last = sb.lastIndexOf(";");
                    kwsStr.substring(0, last - 1);
                    kwNum--;
                }
                this.nsfcKwForSortingDao.saveOrUpdateKGPrjKws(pubId, kwsStr, kwNum);
            }
        } catch (Exception e) {
            logger.error("基准关键词画像--关键词提取出错：", e);
            rs = 3;
        }
        return rs;
    }

    // 从标题，自填关键词中得到关键词全部保留并计算TF，然后从ABSTRACT中获取关键词与TF
    private List<String> extractKwsByCotfRestrictToContent(Long pubId) throws Exception {
        ProjectDataFiveYear prj = this.projectDataFiveYearDao.get(pubId);
        String title = prj.getZhTitle();
        String pubAbstract = prj.getZhAbstract();
        String pubKw = prj.getZhKeywords();
        if (StringUtils.isBlank(title)) {
            title = prj.getEnTitle();
        }
        if (StringUtils.isBlank(pubAbstract)) {
            pubAbstract = prj.getEnAbstract();
        }
        if (StringUtils.isBlank(pubKw)) {
            pubKw = prj.getEnKeywords();
        }
        List<String> rsList = new ArrayList<String>();
        String content = title + " " + pubAbstract;
        if (StringUtils.isEmpty(content)) {
            return null;
        }

        this.kGPubCooperationDao.deleteNsfPrjKw(pubId);

        Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
        Map<String, Object> extractAbsKwMap = new HashMap<String, Object>();
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        extractTitleKwMap = this.extractKwAndTfInContent(title);
        extractAbsKwMap = this.extractKwAndTfInContent(pubAbstract);
        extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");
        extractAbsKwStrings = (Set<String>) extractAbsKwMap.get("EXTRACT_KWS_STRSETS");
        List<String> kwListOnlyFromTitle = new ArrayList<String>();
        for (String keyTitle : extractTitleKwStrings) {
            kwListOnlyFromTitle.add(keyTitle);
        }

        if (StringUtils.isNotBlank(pubKw)) {
            pubKw = pubKw.replace("；", ";");
            String[] kws = pubKw.split(";");
            if (kws != null || kws.length > 0) {
                for (String str : kws) {
                    if (StringUtils.isNotEmpty(str)) {
                        str = StringUtils.trimToEmpty(str);
                        str = str.toLowerCase().replaceAll("\\s+", " ");
                        extractTitleKwStrings.add(str);
                    }
                }
            }
        }
        // 存储所有提取到的关键词，并保留相应排序
        List<String> kwList = new ArrayList<String>();
        // 存储标题与自填关键词
        List<String> titleKwList = new ArrayList<String>();
        for (String keyTitle : extractTitleKwStrings) {
            titleKwList.add(keyTitle);
        }
        String category = prj.getApplicationCode();
        if (CollectionUtils.isNotEmpty(titleKwList)) {
            kwList.addAll(titleKwList);
        }
        if (CollectionUtils.isNotEmpty(kwListOnlyFromTitle)) {
            for (String kw : kwListOnlyFromTitle) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                Integer tfInt = extractTitleKwMap.get(kw) == null ? 0 : (Integer) extractTitleKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长在4到6之间，加强TF为6倍
                if (language == 0 && kw.length() >= 4 && kw.length() <= 6) {
                    tfInt = tfInt * 6;
                }
                this.kGPubCooperationDao.insertIntoTitle(pubId, StringUtils.trimToEmpty(prj.getApplyYear()), language,
                        kw, tfInt, category);
            }

            // 存储标题与自填关键词
            for (String kw : titleKwList) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                // 在此处0为自填关键词
                Integer tfInt = extractTitleKwMap.get(kw) == null ? 0 : (Integer) extractTitleKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长在4到6之间，加强TF为6倍
                if (language == 0 && kw.length() >= 4 && kw.length() <= 6) {
                    tfInt = tfInt * 6;
                }
                this.kGPubCooperationDao.insertIntoTitleAndKws(pubId, StringUtils.trimToEmpty(prj.getApplyYear()),
                        language, kw, tfInt, category);
            }
        }

        if (CollectionUtils.isNotEmpty(extractTitleKwStrings) && CollectionUtils.isNotEmpty(extractAbsKwStrings)) {
            List<String> absKwList = new ArrayList<String>();
            Set<String> coKwSet = new TreeSet<String>();
            for (String kw : extractAbsKwStrings) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                // 在此处0为自填关键词
                Integer tfInt = extractAbsKwMap.get(kw) == null ? 0 : (Integer) extractAbsKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长从4到9，加强TF为6倍;
                if (language == 0 && kw.length() >= 4 && kw.length() <= 9) {
                    tfInt = tfInt * 5;
                } else if (language == 0 && kw.length() == 3) {
                    tfInt = tfInt * 3;
                }
                // 当标题与自填关键词中有对应关键词，则提取出来
                if (!extractTitleKwStrings.contains(kw)) {
                    absKwList.add(kw);
                } else {
                    coKwSet.add(kw);
                    tfInt = 1000 * tfInt;
                }
                this.kGPubCooperationDao.insertIntoAbsTf(pubId, StringUtils.trimToEmpty(prj.getApplyYear()), language,
                        kw, tfInt, category);
            }
            // 将共同出现的词排在最前边,存储
            for (String kw : coKwSet) {
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.kGPubCooperationDao.insertIntoKwCoExistsTitleAbs(pubId,
                        StringUtils.trimToEmpty(prj.getApplyYear()), language, kw, category);
            }

            if (absKwList.size() > 0) {
                List<String> extractKws = this.projectDataFiveYearDao.getKwByTf(titleKwList, absKwList, category);
                if (extractKws != null && extractKws.size() > 0) {
                    List<Map<String, Object>> rsMap =
                            this.projectDataFiveYearDao.getKwByCoTfCout(titleKwList, absKwList, category);
                    int i = 0;
                    for (String kw : extractKws) {
                        Map<String, Object> map = rsMap.get(i);
                        Integer cotf = 0;
                        if (map != null && map.size() > 0) {
                            cotf = ((BigDecimal) map.get("COUNTS")).intValue();
                        }
                        Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                        this.kGPubCooperationDao.insertIntoAbsCotf(pubId, StringUtils.trimToEmpty(prj.getApplyYear()),
                                language, StringUtils.trimToEmpty(kw), cotf, category);
                        i++;
                    }
                    if (CollectionUtils.isNotEmpty(extractKws)) {
                        kwList.addAll(extractKws);
                    }
                    // 存储具体co-tf信息
                    for (String absKw : extractKws) {
                        for (String titleKw : titleKwList) {
                            Map cotf = this.projectDataFiveYearDao.getCoTfByKw(titleKw, absKw, category);
                            if (cotf != null) {
                                Integer language = XmlUtil.isChinese(absKw) ? 0 : 1;
                                this.kGPubCooperationDao.insertIntoAbsCotfDetails(pubId,
                                        StringUtils.trimToEmpty(prj.getApplyYear()), language, absKw, titleKw,
                                        ((BigDecimal) cotf.get("COUNTS")).intValue(), category);
                            }
                        }
                    }
                }
            }
        }
        return kwList;
    }

    // 从标题，自填关键词中得到英文关键词全部保留并计算TF，然后从ABSTRACT中获取关键词与TF
    private List<String> extractKwsByCotfRestrictToContentEn(Long pubId) throws Exception {
        ProjectDataFiveYear prj = this.projectDataFiveYearDao.get(pubId);
        String title = prj.getEnTitle();
        String pubAbstract = prj.getEnAbstract();
        String pubKw = prj.getEnKeywords();
        if (StringUtils.isBlank(title)) {
            title = prj.getZhTitle();
        }
        if (StringUtils.isBlank(pubAbstract)) {
            pubAbstract = prj.getZhAbstract();
        }
        if (StringUtils.isBlank(pubKw)) {
            pubKw = prj.getZhKeywords();
        }
        List<String> rsList = new ArrayList<String>();
        String content = title + " " + pubAbstract;
        if (StringUtils.isEmpty(content)) {
            return null;
        }

        this.kGPubCooperationDao.deleteNsfPrjKw(pubId);

        Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
        Map<String, Object> extractAbsKwMap = new HashMap<String, Object>();
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        extractTitleKwMap = this.extractKwAndTfInContentEn(title);
        extractAbsKwMap = this.extractKwAndTfInContentEn(pubAbstract);
        extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");
        extractAbsKwStrings = (Set<String>) extractAbsKwMap.get("EXTRACT_KWS_STRSETS");
        List<String> kwListOnlyFromTitle = new ArrayList<String>();
        for (String keyTitle : extractTitleKwStrings) {
            kwListOnlyFromTitle.add(keyTitle);
        }

        if (StringUtils.isNotBlank(pubKw)) {
            pubKw = pubKw.replace("；", ";");
            String[] kws = pubKw.split(";");
            if (kws != null || kws.length > 0) {
                for (String str : kws) {
                    if (StringUtils.isNotEmpty(str)) {
                        str = StringUtils.trimToEmpty(str);
                        str = str.toLowerCase().replaceAll("\\s+", " ");
                        extractTitleKwStrings.add(str);
                    }
                }
            }
        }
        // 存储所有提取到的关键词，并保留相应排序
        List<String> kwList = new ArrayList<String>();
        // 存储标题与自填关键词
        List<String> titleKwList = new ArrayList<String>();
        for (String keyTitle : extractTitleKwStrings) {
            titleKwList.add(keyTitle);
        }
        String category = prj.getApplicationCode();
        if (CollectionUtils.isNotEmpty(titleKwList)) {
            kwList.addAll(titleKwList);
        }
        if (CollectionUtils.isNotEmpty(kwListOnlyFromTitle)) {
            for (String kw : kwListOnlyFromTitle) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                Integer tfInt = extractTitleKwMap.get(kw) == null ? 0 : (Integer) extractTitleKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 英文关键词词数在4到6之间，加强TF为6倍
                Integer length = kw.split(" ") == null ? 0 : kw.split(" ").length;
                if (language == 1 && length >= 2 && length <= 6) {
                    tfInt = tfInt * 6;
                }
                this.kGPubCooperationDao.insertIntoTitle(pubId, StringUtils.trimToEmpty(prj.getApplyYear()), language,
                        kw, tfInt, category);
            }

            // 存储标题与自填关键词
            for (String kw : titleKwList) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                // 在此处0为自填关键词
                Integer tfInt = extractTitleKwMap.get(kw) == null ? 0 : (Integer) extractTitleKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 英文关键词词数在4到6之间，加强TF为6倍
                Integer length = kw.split(" ") == null ? 0 : kw.split(" ").length;
                if (language == 1 && length >= 2 && length <= 6) {
                    tfInt = tfInt * 6;
                }
                this.kGPubCooperationDao.insertIntoTitleAndKws(pubId, StringUtils.trimToEmpty(prj.getApplyYear()),
                        language, kw, tfInt, category);
            }
        }

        if (CollectionUtils.isNotEmpty(extractTitleKwStrings) && CollectionUtils.isNotEmpty(extractAbsKwStrings)) {
            List<String> absKwList = new ArrayList<String>();
            Set<String> coKwSet = new TreeSet<String>();
            for (String kw : extractAbsKwStrings) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                // 在此处0为自填关键词
                Integer tfInt = extractAbsKwMap.get(kw) == null ? 0 : (Integer) extractAbsKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 英文关键词词数在4到6之间，加强TF为6倍
                Integer length = kw.split(" ") == null ? 0 : kw.split(" ").length;
                if (language == 1 && length >= 2 && length <= 6) {
                    tfInt = tfInt * 6;
                }
                // 当标题与自填关键词中有对应关键词，则提取出来
                if (!extractTitleKwStrings.contains(kw)) {
                    absKwList.add(kw);
                } else {
                    coKwSet.add(kw);
                    tfInt = 1000 * tfInt;
                }
                this.kGPubCooperationDao.insertIntoAbsTf(pubId, StringUtils.trimToEmpty(prj.getApplyYear()), language,
                        kw, tfInt, category);
            }
            // 将共同出现的词排在最前边,存储
            for (String kw : coKwSet) {
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                this.kGPubCooperationDao.insertIntoKwCoExistsTitleAbs(pubId,
                        StringUtils.trimToEmpty(prj.getApplyYear()), language, kw, category);
            }

            if (absKwList.size() > 0) {
                List<String> extractKws = this.projectDataFiveYearDao.getKwByTf(titleKwList, absKwList, category);
                if (extractKws != null && extractKws.size() > 0) {
                    List<Map<String, Object>> rsMap =
                            this.projectDataFiveYearDao.getKwByCoTfCout(titleKwList, absKwList, category);
                    int i = 0;
                    for (String kw : extractKws) {
                        Map<String, Object> map = rsMap.get(i);
                        Integer cotf = 0;
                        if (map != null && map.size() > 0) {
                            cotf = ((BigDecimal) map.get("COUNTS")).intValue();
                        }
                        Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                        this.kGPubCooperationDao.insertIntoAbsCotf(pubId, StringUtils.trimToEmpty(prj.getApplyYear()),
                                language, StringUtils.trimToEmpty(kw), cotf, category);
                        i++;
                    }
                    if (CollectionUtils.isNotEmpty(extractKws)) {
                        kwList.addAll(extractKws);
                    }
                    // 存储具体co-tf信息
                    for (String absKw : extractKws) {
                        for (String titleKw : titleKwList) {
                            Map cotf = this.projectDataFiveYearDao.getCoTfByKw(titleKw, absKw, category);
                            if (cotf != null) {
                                Integer language = XmlUtil.isChinese(absKw) ? 0 : 1;
                                this.kGPubCooperationDao.insertIntoAbsCotfDetails(pubId,
                                        StringUtils.trimToEmpty(prj.getApplyYear()), language, absKw, titleKw,
                                        ((BigDecimal) cotf.get("COUNTS")).intValue(), category);
                            }
                        }
                    }
                }
            }
        }
        return kwList;
    }

    // 为ISIS计算人员关键词（项目自填关键词与成果自填关键词）language=1.英文，2中文
    private List<String> getPsnPrjAndPubKwsForIsis(Long openId, Integer lang) {
        // 通过SNS-TOKEN, OPENID获得SNS_PSN_ID
        Long snsPsnId = this.openUserUnionDao.getPsnIdByOpenIdAndToken(openId, "2bcca485");
        List<Long> isisPsnId = this.kGPubCooperationDao.getPsnIsisId(openId);
        if (snsPsnId == null || snsPsnId == 0L) {
            return null;
        }
        String psnName = this.kGPubCooperationDao.getPsnIsisName(openId);
        List<String> prjIdList = new ArrayList<String>();
        // 设置为测试账号
        prjIdList = this.kGPubCooperationDao.getPsnNsfcPrjToHandleList10years(isisPsnId);
        if (prjIdList.size() <= 0) {
            return null;
        }
        BigDecimal dimiCh = new BigDecimal(0.2);
        BigDecimal dimiChThree = new BigDecimal(0.6);
        BigDecimal dimiEn = new BigDecimal(0.3);
        BigDecimal mulTiPrj = new BigDecimal(1000000000000L);
        BigDecimal mulTiPubCotf = new BigDecimal(100000000L);
        // BigDecimal mulTiPubTf = new BigDecimal(10000L);

        // 项目关键词
        Set<String> extractPrjKwStrings = new TreeSet<String>();
        Map<String, Integer> extractTitleKwMapAll = new HashMap<String, Integer>();
        Set<String> categories = new TreeSet<String>();
        for (String prjId : prjIdList) {
            Map<String, Object> mp = this.kGPubCooperationDao.getNsfcPrjInfo10Years(prjId);
            String kwsStr = "";
            if (lang == 2) {
                kwsStr = mp.get("ZH_KEYWORDS") == null ? "" : (String) mp.get("ZH_KEYWORDS");
            } else {
                kwsStr = mp.get("EN_KEYWORDS") == null ? "" : (String) mp.get("EN_KEYWORDS");
            }
            if (StringUtils.isEmpty(kwsStr)) {
                continue;
            }
            Set<String> extractKwsKwStrings = new TreeSet<String>();
            if (StringUtils.isNotBlank(kwsStr)) {
                kwsStr = kwsStr.replace("；", ";");
                String[] kws = kwsStr.split(";");
                if (kws != null) {
                    for (String str : kws) {
                        if (StringUtils.isNotEmpty(str)) {
                            str = StringUtils.trimToEmpty(str);
                            str = str.toLowerCase().replaceAll("\\s+", " ");
                            extractKwsKwStrings.add(str);
                        }
                    }
                }
            }
            if (mp.get("DISCODE") != null || StringUtils.isNotEmpty((String) mp.get("DISCODE"))) {
                String discode =
                        ((String) mp.get("DISCODE")).length() > 3 ? ((String) mp.get("DISCODE")).substring(0, 3)
                                : (String) mp.get("DISCODE");
                categories.add(discode);
            }
            extractPrjKwStrings.addAll(extractKwsKwStrings);
            // 合并项目自填关键词
            for (String kwsKw : extractKwsKwStrings) {
                Integer tf = extractTitleKwMapAll.get(kwsKw);
                if (tf != null) {
                    tf = tf + 1;
                } else {
                    tf = 1;
                }
                extractTitleKwMapAll.put(kwsKw, tf);
            }
        }
        List<NsfcKwTfCotfForSorting> prjKws = new ArrayList<NsfcKwTfCotfForSorting>();
        Map<String, Double> kwAll = new HashMap<String, Double>();
        for (String kw : extractPrjKwStrings) {
            Integer wordNum = 1;
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            Integer tf = extractTitleKwMapAll.get(kw) == null ? 0 : extractTitleKwMapAll.get(kw);
            BigDecimal bdTf = new BigDecimal(tf);
            Double tfDo = bdTf.multiply(mulTiPrj).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            // 中文长度小于等于2或大于10，TF*0.2 || 英文长度等于1或大于6，tf*0.6
            wordNum = kw.length();
            if (language == 0 && (kw.length() > 10 || kw.length() <= 2)) {
                tfDo = bdTf.multiply(dimiCh).multiply(mulTiPrj).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            } else if (language == 1) {
                wordNum = kw.split(" ").length;
                if (wordNum == 1 || wordNum > 6) {
                    tfDo = bdTf.multiply(dimiEn).multiply(mulTiPrj).setScale(3, BigDecimal.ROUND_HALF_DOWN)
                            .doubleValue();
                }
            } else if (language == 0 && kw.length() == 3) {
                tfDo = bdTf.multiply(dimiChThree).multiply(mulTiPrj).setScale(3, BigDecimal.ROUND_HALF_DOWN)
                        .doubleValue();
            }
            // this.kGPubCooperationDao.insertIntoPsnRrjKwAll(openId, language, StringUtils.trimToEmpty(kw),
            // tf);
            // 用于排序,项目tf 10^20
            NsfcKwTfCotfForSorting nsKw = new NsfcKwTfCotfForSorting(kw, wordNum, tfDo);
            prjKws.add(nsKw);
            kwAll.put(kw, tfDo);
        }

        // 计算成果关键词(直接选用extractKwsByCotfRestrictToContentEn方法已经算好的个人数据，节省时间，切与ISIS保持一致)
        List<Map<String, Object>> pubKwSumList =
                this.kGPubCooperationDao.getPsnPdwhPubTfSumByOpenId(openId, lang == 2 ? 0 : 1);
        Set<String> extractPubKwStrings = new TreeSet<String>();
        // 集中最后成果的TF与结果
        Map<String, Double> pubKwMapAllFilter = new HashMap<String, Double>();
        if (pubKwSumList != null && pubKwSumList.size() > 0) {
            for (Map<String, Object> kw : pubKwSumList) {
                if (kw.get("KW_STR") == null) {
                    continue;
                }
                String kwStr = (String) kw.get("KW_STR");
                Double wtf = kw.get("TF") == null ? 0.0 : ((BigDecimal) kw.get("TF")).doubleValue();
                extractPubKwStrings.add(kwStr);
                pubKwMapAllFilter.put(kwStr, wtf);
            }
        }

        // 计算项目关键词与成果补充关键词的COTF，用于过滤成果关键词
        List<String> extractKws = new ArrayList<String>();
        List<Map<String, Object>> rsMap = new ArrayList<Map<String, Object>>();
        if (extractPrjKwStrings.size() != 0 && extractPubKwStrings.size() != 0) {
            if (categories.size() > 0) {
                extractKws = this.kGPubCooperationDao.getKwByTfExcludePubKwByCategory(extractPrjKwStrings,
                        extractPubKwStrings, lang, categories);
                rsMap = this.kGPubCooperationDao.getKwByCoTfCoutExcludePubKwByCategory(extractPrjKwStrings,
                        extractPubKwStrings, lang, categories);
            } else {
                extractKws =
                        this.kGPubCooperationDao.getKwByTfExcludePubKw(extractPrjKwStrings, extractPubKwStrings, lang);
                rsMap = this.kGPubCooperationDao.getKwByCoTfCoutExcludePubKw(extractPrjKwStrings, extractPubKwStrings,
                        lang);
            }
        }
        for (Map<String, Object> kwMp : rsMap) {
            if (kwMp.get("KW2") == null || StringUtils.isEmpty((String) kwMp.get("KW2"))) {
                continue;
            }
            Integer wordNum = 1;
            String kw = (String) kwMp.get("KW2");
            double cotf = kwMp.get("COUNTS") == null ? 0.0
                    : ((BigDecimal) kwMp.get("COUNTS")).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            // this.kGPubCooperationDao.insertIntoPsnPubKwAllCotf(openId, language, StringUtils.trimToEmpty(kw),
            // cotf);
            wordNum = kw.length();
            Double tfDo = pubKwMapAllFilter.get(kw);
            BigDecimal bdTf = new BigDecimal(tfDo);
            BigDecimal bdCotf = new BigDecimal(cotf);
            Double bigTf = tfDo;
            if (language == 0 && (kw.length() > 10 || kw.length() <= 2)) {
                bigTf = bdTf.multiply(dimiCh).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            } else if (language == 1) {
                wordNum = kw.split(" ").length;
                if (wordNum == 1 || wordNum > 6) {
                    bigTf = bdTf.multiply(dimiEn).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
            } else if (language == 0 && kw.length() == 3) {
                bigTf = bdTf.multiply(dimiChThree).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            }
            Double sum = bigTf + bdCotf.multiply(mulTiPubCotf).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            prjKws.add(new NsfcKwTfCotfForSorting(kw, wordNum, sum));
            if (kwAll.get(kw) != null) {
                Double score = kwAll.get(kw) + sum;
                kwAll.put(kw, score);
            } else {
                kwAll.put(kw, sum);
            }
        }

        for (String kw : extractPubKwStrings) {
            Integer wordNum = 1;
            // 剔除出已经有COTF的词，参与后续排序
            if (extractKws.contains(kw)) {
                continue;
            }
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            wordNum = kw.length();
            Double tfDo = pubKwMapAllFilter.get(kw);
            if (tfDo == null) {
                continue;
            }
            BigDecimal bdTf = new BigDecimal(tfDo);
            Double tf = tfDo;
            if (language == 0 && (kw.length() > 10 || kw.length() <= 2)) {
                tf = bdTf.multiply(dimiCh).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            } else if (language == 1) {
                wordNum = kw.split(" ").length;
                if (wordNum == 1 || wordNum > 6) {
                    tf = bdTf.multiply(dimiEn).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
            } else if (language == 0 && kw.length() == 3) {
                tf = bdTf.multiply(dimiChThree).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            }

            NsfcKwTfCotfForSorting nsKw = new NsfcKwTfCotfForSorting(kw, wordNum, tf);
            prjKws.add(nsKw);
            if (kwAll.get(kw) != null) {
                Double score = kwAll.get(kw) + tf;
                kwAll.put(kw, score);
            } else {
                kwAll.put(kw, tf);
            }
        }

        this.kGPubCooperationDao.deletePsnScore(openId, lang == 2 ? 0 : 1);
        List<String> rs = new ArrayList<String>();
        // 优先添加项目关键词
        for (NsfcKwTfCotfForSorting kw : prjKws) {
            this.kGPubCooperationDao.insertIntoPsnScore(openId, XmlUtil.isChinese(kw.getKeywords()) ? 0 : 1,
                    kw.getKeywords(), kw.getBigTf());
            rs.add(kw.getKeywords());
        }
        for (Entry<String, Double> et : kwAll.entrySet()) {
            if (StringUtils.isEmpty(et.getKey())) {
                continue;
            }
            this.kGPubCooperationDao.insertIntoPsnScoreAll(openId, XmlUtil.isChinese(et.getKey()) ? 0 : 1, et.getKey(),
                    et.getValue());
        }

        return rs;
    }

    @Override
    public Integer getKwSubsetsCotf(String category, Integer lang) {
        try {
            List<BigDecimal> prjIdList = new ArrayList<BigDecimal>();
            prjIdList = this.kGPubCooperationDao.getNsfcPrjToHandleListByCategory(category);
            if (prjIdList.size() <= 0) {
                return 2;
            }
            HashMap<String, Integer> rsMapByCategory = new HashMap<String, Integer>();
            for (BigDecimal prjId : prjIdList) {
                Long prjIdLong = prjId.longValue();
                Map<String, Object> mp = this.kGPubCooperationDao.getNsfcPrjInfo(prjIdLong);
                String kwsStr = "";
                String titleStr = "";
                if (lang == 2) {
                    kwsStr = mp.get("ZH_KEYWORDS") == null ? "" : (String) mp.get("ZH_KEYWORDS");
                    titleStr = mp.get("ZH_TITLE") == null ? "" : (String) mp.get("ZH_TITLE");
                } else {
                    kwsStr = mp.get("EN_KEYWORDS") == null ? "" : (String) mp.get("EN_KEYWORDS");
                    titleStr = mp.get("EN_TITLE") == null ? "" : (String) mp.get("EN_TITLE");
                }
                if (StringUtils.isEmpty(kwsStr) && StringUtils.isEmpty(titleStr)) {
                    continue;
                }
                Set<String> extractKwsKwStrings = new TreeSet<String>();
                if (StringUtils.isNotBlank(kwsStr)) {
                    kwsStr = kwsStr.replace("；", ";");
                    String[] kws = kwsStr.split(";");
                    if (kws != null) {
                        for (String str : kws) {
                            if (StringUtils.isNotEmpty(str)) {
                                str = StringUtils.trimToEmpty(str);
                                str = str.toLowerCase().replaceAll("\\s+", " ");
                                extractKwsKwStrings.add(str);
                            }
                        }
                    }
                }
                Set<String> extractTitleKwStrings = new TreeSet<String>();
                if (lang == 2) {
                    extractTitleKwStrings = getRsSetNewByCategory(titleStr, category);
                }
                extractTitleKwStrings.addAll(extractKwsKwStrings);
                if (extractTitleKwStrings.size() > 0) {
                    List<String> kwList = new ArrayList<String>();
                    StringBuilder sb = new StringBuilder();
                    for (String kw : extractTitleKwStrings) {
                        kwList.add(kw);
                    }
                    Collections.sort(kwList);
                    for (String kw : kwList) {
                        if (StringUtils.isEmpty(kw)) {
                            continue;
                        }
                        sb.append(kw);
                        sb.append(";");
                    }
                    this.kGPubCooperationDao.insertIntoPrpKws(sb.toString(), kwList.size(), prjIdLong);

                    Map<Integer, Set<String>> rsMp = this.getAllSubSets(kwList.toArray(new String[kwList.size()]));
                    for (Entry<Integer, Set<String>> et : rsMp.entrySet()) {
                        Integer size = et.getKey();
                        Set<String> kwStrSet = et.getValue();
                        if (kwStrSet == null || kwStrSet.size() <= 0) {
                            continue;
                        }
                        for (String kwSubsetStr : kwStrSet) {
                            Long hashValue = HashUtils.getStrHashCode(kwSubsetStr);
                            this.kGPubCooperationDao.insertIntoSubSetsFromPrp(kwSubsetStr, size, prjIdLong, hashValue);
                            Integer cotf = rsMapByCategory.get(kwSubsetStr);
                            if (cotf == null || cotf == 0) {
                                rsMapByCategory.put(kwSubsetStr, 1);
                            } else {
                                rsMapByCategory.put(kwSubsetStr, 1 + cotf);
                            }
                        }
                    }
                }
            }
            if (rsMapByCategory.size() > 0) {
                for (Entry<String, Integer> rsEt : rsMapByCategory.entrySet()) {
                    String kws = rsEt.getKey();
                    Integer cotf = rsEt.getValue();
                    Integer size = kws.split(";") == null ? 0 : kws.split(";").length;
                    Long hashValue = HashUtils.getStrHashCode(kws);
                    this.kGPubCooperationDao.insertIntoSubSetsFinalRsFromPrp(kws, size, category, hashValue, cotf);
                }
            }
            return 1;
        } catch (Exception e) {
            logger.error("getKwSubsetsCotf, 获取nsfc项目计算cotf出错", e);
            return 3;
        }
    }

    private Map<Integer, Set<String>> getAllSubSets(String[] kws) {
        if (kws == null || kws.length == 0) {
            return null;
        }
        Integer length = (2 << kws.length - 1) - 1;
        Map<Integer, Set<String>> rsMap = new HashMap<Integer, Set<String>>();
        for (int i = 1; i <= length; i++) {
            int start = i;
            List<String> subset = new ArrayList<String>();
            for (int j = 0; j <= kws.length - 1; j++) {
                // 通过与运算，按二进制选择需要的元素
                if ((start & 1) == 1) {
                    subset.add(kws[j]);
                }
                start >>= 1;
            }
            if (subset.size() > 5) {
                continue;
            }
            String subsetStr = "";
            // 转换为字符
            for (String st : subset) {
                subsetStr = subsetStr + st + ";";
            }
            subsetStr = subsetStr.substring(0, subsetStr.length() - 1);
            // System.out.println("i = " + i + " " + subsetStr);
            Set<String> kwSet = rsMap.get(subset.size());
            if (kwSet != null && kwSet.size() != 0) {
                kwSet.add(subsetStr);
                rsMap.put(subset.size(), kwSet);
            } else {
                Set<String> kwSetNew = new TreeSet<String>();
                kwSetNew.add(subsetStr);
                rsMap.put(subset.size(), kwSetNew);
            }
        }
        return rsMap;
    }

    private Set<String> getRsSetNewByCategory(String content, String category) {
        Set<String> extractKwStrings = new TreeSet<String>();
        if (StringUtils.isEmpty(content)) {
            return extractKwStrings;
        }
        if (!XmlUtil.isChinese(content)) {
            content = content.toLowerCase().replaceAll("\\s+", "空格");
        }
        Result kwRs = DicAnalysis.parse(content);
        for (Term t : kwRs.getTerms()) {
            if (t == null) {
                continue;
            }
            // 按照学科与对应其学科主任维护关键词提取
            if (category.equals(t.getNatureStr())) {
                if (StringUtils.isNotEmpty(t.getName())) {
                    String kw = t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim();
                    if (StringUtils.isNotEmpty(kw) && kw.length() > 2) {
                        if (!XmlUtil.isChinese(kw) && kw.length() < 4) {
                            continue;
                        }
                        extractKwStrings.add(kw);
                    }
                }
            }
        }
        return extractKwStrings;
    }

    private List<NsfcKwScoreForSorting> extractKwsByCotfRestrictToContentZh(Long pubId) throws Exception {
        List<NsfcKwScoreForSorting> rsList = new ArrayList<NsfcKwScoreForSorting>();
        Map<String, Object> prj = this.kGPubCooperationDao.getNsfcPrjInfoForGaoXiao(pubId);
        if (prj == null) {
            return rsList;
        }
        String title = prj.get("ZH_TITLE") == null ? "" : (String) (prj.get("ZH_TITLE"));
        String pubAbstract = "";
        if (prj.get("PRJ_XML") != null) {
            Clob pa = (Clob) (prj.get("PRJ_XML"));
            pubAbstract = pa.getSubString(1, (int) pa.length());
        }
        String pubKw = prj.get("KEY_WORDS") == null ? "" : (String) (prj.get("KEY_WORDS"));
        String content = title + " " + pubAbstract;
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        Map<String, Integer> rsKwMap = new HashMap<String, Integer>();
        Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
        Map<String, Object> extractAbsKwMap = new HashMap<String, Object>();
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        extractTitleKwMap = this.extractKwAndTfInContent(title);
        extractAbsKwMap = this.extractKwAndTfInContent(pubAbstract);
        if (extractTitleKwMap.get("EXTRACT_KWS_STRSETS") != null) {
            extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");
        }
        if (extractAbsKwMap.get("EXTRACT_KWS_STRSETS") != null) {
            extractAbsKwStrings = (Set<String>) extractAbsKwMap.get("EXTRACT_KWS_STRSETS");
        }
        if (StringUtils.isNotBlank(pubKw)) {
            pubKw = pubKw.replace("；", ";");
            String[] kws = pubKw.split(";");
            if (kws != null || kws.length > 0) {
                for (String str : kws) {
                    if (StringUtils.isNotEmpty(str)) {
                        str = StringUtils.trimToEmpty(str);
                        str = str.toLowerCase().replaceAll("\\s+", " ");
                        extractTitleKwStrings.add(str);
                        // 合并标题与自填关键词tf
                        Integer tf = extractTitleKwMap.get(str) == null ? 0 : (Integer) extractTitleKwMap.get(str);
                        if (0 == tf) {
                            extractTitleKwMap.put(str, 1);
                        } else {
                            tf = tf + 1;
                            extractTitleKwMap.put(str, tf);
                        }
                    }
                }
            }
        }
        // 存储标题与自填关键词
        List<String> titleKwList = new ArrayList<String>();
        for (String keyTitle : extractTitleKwStrings) {
            titleKwList.add(keyTitle);
        }
        if (CollectionUtils.isNotEmpty(titleKwList)) {
            for (String kw : titleKwList) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                Integer tfInt = extractTitleKwMap.get(kw) == null ? 0 : (Integer) extractTitleKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长从4到9，加强TF为6倍;
                if (language == 0 && kw.length() >= 4 && kw.length() <= 9) {
                    tfInt = tfInt * 5;
                } else if (language == 0 && kw.length() == 3) {
                    tfInt = tfInt * 3;
                } else if (language == 1) {
                    Integer lengthEn = kw.split(" ").length;
                    if (lengthEn > 1 && lengthEn <= 6) {
                        tfInt = tfInt * 3;
                    }
                }
                rsKwMap.put(kw, tfInt * 100000000);
            }
        }

        if (CollectionUtils.isNotEmpty(extractAbsKwStrings)) {
            List<String> absKwList = new ArrayList<String>();
            for (String kw : extractAbsKwStrings) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                // 在此处0为自填关键词
                Integer tfInt = extractAbsKwMap.get(kw) == null ? 0 : (Integer) extractAbsKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长从4到9，加强TF为6倍;
                if (language == 0 && kw.length() >= 4 && kw.length() <= 9) {
                    tfInt = tfInt * 5;
                } else if (language == 0 && kw.length() == 3) {
                    tfInt = tfInt * 3;
                } else if (language == 1) {
                    Integer lengthEn = kw.split(" ").length;
                    if (lengthEn > 1 && lengthEn <= 6) {
                        tfInt = tfInt * 3;
                    }
                }
                // 当标题与自填关键词中有对应关键词，则提取出来
                if (!extractTitleKwStrings.contains(kw)) {
                    absKwList.add(kw);
                }
                // 合并标题自填关键词与摘要关键词tf
                Integer tf = rsKwMap.get(kw) == null ? 0 : (Integer) rsKwMap.get(kw);
                if (0 == tf) {
                    rsKwMap.put(kw, tfInt);
                } else {
                    tf = tf + tfInt;
                    rsKwMap.put(kw, tf);
                }
            }

            if (absKwList.size() > 0 && titleKwList.size() > 0) {
                List<String> extractKws = this.kGPubCooperationDao.getKwByTfExcludePubKw(titleKwList, absKwList, 2);
                if (extractKws != null && extractKws.size() > 0) {
                    List<Map<String, Object>> rsMap =
                            this.kGPubCooperationDao.getKwByCoTfCoutExcludePubKw(titleKwList, absKwList, 2);
                    int i = 0;
                    for (String kw : extractKws) {
                        Map<String, Object> map = rsMap.get(i);
                        Integer cotf = 0;
                        if (map != null && map.size() > 0) {
                            cotf = ((BigDecimal) map.get("COUNTS")).intValue();
                        }
                        // 合并标题自填关键词与摘要关键词cotf得分
                        Integer tf = rsKwMap.get(kw) == null ? 0 : (Integer) rsKwMap.get(kw);
                        if (0 == tf) {
                            rsKwMap.put(kw, cotf * 10000);
                        } else {
                            tf = tf + cotf * 10000;
                            rsKwMap.put(kw, tf);
                        }
                        i++;
                    }
                }
            }
        }
        if (rsKwMap.size() > 0) {
            for (Entry<String, Integer> et : rsKwMap.entrySet()) {
                if (StringUtils.isEmpty(et.getKey())) {
                    continue;
                }
                NsfcKwScoreForSorting nkfs =
                        new NsfcKwScoreForSorting(et.getKey(), et.getKey().length(), et.getValue());
                rsList.add(nkfs);
            }
            Collections.sort(rsList);
        }
        return rsList;
    }

    // 为高校（昌盛组）计算个人关键词，实时计算个人成果（与基准库有关系的个人成果）
    private List<String> getPsnKwFromPrjAndPubForGaoxiao(Long openId, Integer lang) {
        // 通过SNS-TOKEN, OPENID获得SNS_PSN_ID
        Long snsPsnId = this.openUserUnionDao.getPsnIdByOpenIdAndToken(openId, "2bcca485");
        List<Long> isisPsnId = this.kGPubCooperationDao.getPsnIsisId(openId);
        if (snsPsnId == null || snsPsnId == 0L) {
            return null;
        }
        String psnName = this.kGPubCooperationDao.getPsnIsisName(openId);
        List<String> prjIdList = new ArrayList<String>();
        // 设置为测试账号
        prjIdList = this.kGPubCooperationDao.getPsnNsfcPrjToHandleList10years(isisPsnId);
        if (prjIdList.size() <= 0) {
            return null;
        }
        BigDecimal dimiCh = new BigDecimal(0.2);
        BigDecimal dimiChThree = new BigDecimal(0.6);
        BigDecimal dimiEn = new BigDecimal(0.3);
        BigDecimal mulTiPrj = new BigDecimal(1000000000000L);
        BigDecimal mulTiPubCotf = new BigDecimal(100000000L);

        // 项目关键词
        Set<String> extractPrjKwStrings = new TreeSet<String>();
        Map<String, Integer> extractTitleKwMapAll = new HashMap<String, Integer>();
        Set<String> categories = new TreeSet<String>();
        for (String prjId : prjIdList) {
            Map<String, Object> mp = this.kGPubCooperationDao.getNsfcPrjInfo10Years(prjId);
            String kwsStr = "";
            String titleStr = "";
            if (lang == 2) {
                kwsStr = mp.get("ZH_KEYWORDS") == null ? "" : (String) mp.get("ZH_KEYWORDS");
                titleStr = mp.get("ZH_TITLE") == null ? "" : (String) mp.get("ZH_TITLE");
            } else {
                kwsStr = mp.get("EN_KEYWORDS") == null ? "" : (String) mp.get("EN_KEYWORDS");
                titleStr = mp.get("EN_TITLE") == null ? "" : (String) mp.get("EN_TITLE");
            }
            if (StringUtils.isEmpty(kwsStr) && StringUtils.isEmpty(titleStr)) {
                continue;
            }
            Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
            Set<String> extractTitleKwStrings = new TreeSet<String>();
            extractTitleKwMap = this.extractKwAndTfInContent(titleStr);
            if (extractTitleKwMap.get("EXTRACT_KWS_STRSETS") != null) {
                extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");
            }

            Set<String> extractKwsKwStrings = new TreeSet<String>();
            if (StringUtils.isNotBlank(kwsStr)) {
                kwsStr = kwsStr.replace("；", ";");
                String[] kws = kwsStr.split(";");
                if (kws != null) {
                    for (String str : kws) {
                        if (StringUtils.isNotEmpty(str)) {
                            str = StringUtils.trimToEmpty(str);
                            str = str.toLowerCase().replaceAll("\\s+", " ");
                            extractKwsKwStrings.add(str);
                        }
                    }
                }
            }
            if (mp.get("DISCODE") != null || StringUtils.isNotEmpty((String) mp.get("DISCODE"))) {
                String discode =
                        ((String) mp.get("DISCODE")).length() > 3 ? ((String) mp.get("DISCODE")).substring(0, 3)
                                : (String) mp.get("DISCODE");
                categories.add(discode);
            }
            extractPrjKwStrings.addAll(extractKwsKwStrings);
            extractPrjKwStrings.addAll(extractTitleKwStrings);
            // 合并项目自填关键词
            for (String kwsKw : extractKwsKwStrings) {
                Integer tf = extractTitleKwMapAll.get(kwsKw);
                if (tf != null) {
                    tf = tf + 1;
                } else {
                    tf = 1;
                }
                extractTitleKwMapAll.put(kwsKw, tf);
            }
            for (String titleKw : extractTitleKwStrings) {
                Integer tf = (Integer) extractTitleKwMap.get(titleKw);
                if (extractTitleKwMapAll.get(titleKw) != null) {
                    tf = tf + extractTitleKwMapAll.get(titleKw);
                }
                extractTitleKwMapAll.put(titleKw, tf);
            }
        }
        List<NsfcKwTfCotfForSorting> prjKws = new ArrayList<NsfcKwTfCotfForSorting>();
        Map<String, Double> kwAll = new HashMap<String, Double>();
        for (String kw : extractPrjKwStrings) {
            Integer wordNum = 1;
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            Integer tf = extractTitleKwMapAll.get(kw) == null ? 0 : extractTitleKwMapAll.get(kw);
            BigDecimal bdTf = new BigDecimal(tf);
            Double tfDo = bdTf.multiply(mulTiPrj).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            // 中文长度小于等于2或大于10，TF*0.2 || 英文长度等于1或大于6，tf*0.6
            wordNum = kw.length();
            if (language == 0 && (kw.length() > 10 || kw.length() <= 2)) {
                tfDo = bdTf.multiply(dimiCh).multiply(mulTiPrj).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            } else if (language == 1) {
                wordNum = kw.split(" ").length;
                if (wordNum == 1 || wordNum > 6) {
                    tfDo = bdTf.multiply(dimiEn).multiply(mulTiPrj).setScale(3, BigDecimal.ROUND_HALF_DOWN)
                            .doubleValue();
                }
            } else if (language == 0 && kw.length() == 3) {
                tfDo = bdTf.multiply(dimiChThree).multiply(mulTiPrj).setScale(3, BigDecimal.ROUND_HALF_DOWN)
                        .doubleValue();
            }
            NsfcKwTfCotfForSorting nsKw = new NsfcKwTfCotfForSorting(kw, wordNum, tfDo);
            prjKws.add(nsKw);
            kwAll.put(kw, tfDo);
        }

        // 计算成果关键词
        List<BigDecimal> pubIdList = this.projectDataFiveYearDao.getPdwhPubListByPsnId(snsPsnId);
        Set<String> extractPubKwStrings = new TreeSet<String>();
        Map<String, Double> extractPubTitleKwMapAll = new HashMap<String, Double>();
        if (pubIdList != null && pubIdList.size() > 0) {
            for (BigDecimal pubIdDe : pubIdList) {
                Long pubId = pubIdDe.longValue();
                try {
                    PubPdwhDetailDOM pubPdwh = pdwhPublicationService.getFullPdwhPubInfoById(pubId);
                    if (pubPdwh == null) {
                        continue;
                    }
                    // 获取成果作者数
                    Integer authorNum = 1;
                    String authors = StringUtils.trimToEmpty(pubPdwh.getAuthorNames());
                    if (StringUtils.isNotEmpty(authors)) {
                        // 中文作者没有本人，则继续不作计算
                        /*
                         * if (authors.indexOf(psnName) < 0) { continue; }
                         */
                        authors = authors.replace("；", ";");
                        String[] authorCollection = authors.split(";");
                        if (authorCollection == null || authorCollection.length == 1) {
                            authors = authors.replace("，", ",");
                            authorCollection = authors.split(",");
                        }
                        authorNum = authorCollection.length;
                    }

                    String ctitle = StringUtils.trimToEmpty(pubPdwh.getTitle());
                    String ckeywords = StringUtils.trimToEmpty(pubPdwh.getKeywords());
                    String content = ctitle + ckeywords;
                    if (StringUtils.isEmpty(content)) {
                        continue;
                    }
                    // 按照authorNum计算TF
                    Map<String, Object> extractPubTitleKwMap = new HashMap<String, Object>();
                    Set<String> extractTitleKwStrings = new TreeSet<String>();
                    extractPubTitleKwMap = this.extractKwAndTfInContent(ctitle);
                    if (extractPubTitleKwMap.get("EXTRACT_KWS_STRSETS") != null) {
                        extractTitleKwStrings = (Set<String>) extractPubTitleKwMap.get("EXTRACT_KWS_STRSETS");
                    }
                    Set<String> extractKwsKwStrings = new TreeSet<String>();
                    if (StringUtils.isNotBlank(ckeywords)) {
                        ckeywords = ckeywords.replace("；", ";");
                        String[] kws = ckeywords.split(";");
                        if (kws != null) {
                            for (String str : kws) {
                                if (StringUtils.isNotEmpty(str)) {
                                    str = StringUtils.trimToEmpty(str);
                                    str = str.toLowerCase().replaceAll("\\s+", " ");
                                    extractKwsKwStrings.add(str);
                                }
                            }
                        }
                    }
                    extractPubKwStrings.addAll(extractKwsKwStrings);
                    extractPubKwStrings.addAll(extractTitleKwStrings);
                    // 合并成果自填关键词与标题抽取关键词, 需要按照成果作者数来决定权重
                    BigDecimal anb = new BigDecimal(authorNum);
                    Double stf = new BigDecimal(1).divide(anb, 3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                    for (String kwsKw : extractKwsKwStrings) {
                        Double tf = extractPubTitleKwMapAll.get(kwsKw);
                        if (tf != null) {
                            tf = tf + stf;
                        } else {
                            tf = stf;
                        }
                        extractPubTitleKwMapAll.put(kwsKw, tf);
                    }
                    for (String titleKw : extractTitleKwStrings) {
                        Double tf = new BigDecimal((Integer) extractPubTitleKwMap.get(titleKw))
                                .divide(anb, 3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        if (extractPubTitleKwMapAll.get(titleKw) != null) {
                            tf = tf + extractPubTitleKwMapAll.get(titleKw);
                        }
                        extractPubTitleKwMapAll.put(titleKw, tf);
                    }
                } catch (Exception e) {
                    logger.error("计算个人关键词--提取成果关键词组合出错  pubid ：" + pubId, e);
                }
            }
        }

        // 计算项目关键词与成果补充关键词的COTF，用于过滤成果关键词
        List<String> extractKws = new ArrayList<String>();
        List<Map<String, Object>> rsMap = new ArrayList<Map<String, Object>>();
        if (extractPrjKwStrings.size() != 0 && extractPubKwStrings.size() != 0) {
            if (categories.size() > 0) {
                extractKws = this.kGPubCooperationDao.getKwByTfExcludePubKwByCategory(extractPrjKwStrings,
                        extractPubKwStrings, lang, categories);
                rsMap = this.kGPubCooperationDao.getKwByCoTfCoutExcludePubKwByCategory(extractPrjKwStrings,
                        extractPubKwStrings, lang, categories);
            } else {
                extractKws =
                        this.kGPubCooperationDao.getKwByTfExcludePubKw(extractPrjKwStrings, extractPubKwStrings, lang);
                rsMap = this.kGPubCooperationDao.getKwByCoTfCoutExcludePubKw(extractPrjKwStrings, extractPubKwStrings,
                        lang);
            }
        }
        for (Map<String, Object> kwMp : rsMap) {
            if (kwMp.get("KW2") == null || StringUtils.isEmpty((String) kwMp.get("KW2"))) {
                continue;
            }
            Integer wordNum = 1;
            String kw = (String) kwMp.get("KW2");
            double cotf = kwMp.get("COUNTS") == null ? 0.0
                    : ((BigDecimal) kwMp.get("COUNTS")).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            wordNum = kw.length();
            Double tfDo = extractPubTitleKwMapAll.get(kw);
            BigDecimal bdTf = new BigDecimal(tfDo);
            BigDecimal bdCotf = new BigDecimal(cotf);
            Double bigTf = tfDo;
            if (language == 0 && (kw.length() > 10 || kw.length() <= 2)) {
                bigTf = bdTf.multiply(dimiCh).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            } else if (language == 1) {
                wordNum = kw.split(" ").length;
                if (wordNum == 1 || wordNum > 6) {
                    bigTf = bdTf.multiply(dimiEn).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
            } else if (language == 0 && kw.length() == 3) {
                bigTf = bdTf.multiply(dimiChThree).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            }
            Double sum = bigTf + bdCotf.multiply(mulTiPubCotf).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            prjKws.add(new NsfcKwTfCotfForSorting(kw, wordNum, sum));
            if (kwAll.get(kw) != null) {
                Double score = kwAll.get(kw) + sum;
                kwAll.put(kw, score);
            } else {
                kwAll.put(kw, sum);
            }
        }

        for (String kw : extractPubKwStrings) {
            Integer wordNum = 1;
            // 剔除出已经有COTF的词，参与后续排序
            if (extractKws.contains(kw)) {
                continue;
            }
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            wordNum = kw.length();
            Double tfDo = extractPubTitleKwMapAll.get(kw);
            if (tfDo == null) {
                continue;
            }
            BigDecimal bdTf = new BigDecimal(tfDo);
            Double tf = tfDo;
            if (language == 0 && (kw.length() > 10 || kw.length() <= 2)) {
                tf = bdTf.multiply(dimiCh).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            } else if (language == 1) {
                wordNum = kw.split(" ").length;
                if (wordNum == 1 || wordNum > 6) {
                    tf = bdTf.multiply(dimiEn).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                }
            } else if (language == 0 && kw.length() == 3) {
                tf = bdTf.multiply(dimiChThree).setScale(3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            }
            NsfcKwTfCotfForSorting nsKw = new NsfcKwTfCotfForSorting(kw, wordNum, tf);
            prjKws.add(nsKw);
            if (kwAll.get(kw) != null) {
                Double score = kwAll.get(kw) + tf;
                kwAll.put(kw, score);
            } else {
                kwAll.put(kw, tf);
            }
        }

        this.kGPubCooperationDao.deletePsnScore(openId, lang == 2 ? 0 : 1);
        List<String> rs = new ArrayList<String>();
        // 优先添加项目关键词
        Collections.sort(prjKws);
        for (NsfcKwTfCotfForSorting kw : prjKws) {
            rs.add(kw.getKeywords());
        }
        for (Entry<String, Double> et : kwAll.entrySet()) {
            if (StringUtils.isEmpty(et.getKey())) {
                continue;
            }
            this.kGPubCooperationDao.insertIntoPsnScoreAll(openId, XmlUtil.isChinese(et.getKey()) ? 0 : 1, et.getKey(),
                    et.getValue());
        }
        return rs;
    }

    private List<NsfcKwScoreForSorting> getPrjKws(String title, String pubAbstract, String pubKw) {
        List<NsfcKwScoreForSorting> rsList = new ArrayList<NsfcKwScoreForSorting>();
        String content = title + " " + pubAbstract;
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        Map<String, Integer> rsKwMap = new HashMap<String, Integer>();
        Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
        Map<String, Object> extractAbsKwMap = new HashMap<String, Object>();
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        extractTitleKwMap = this.extractKwAndTfInContent(title);
        extractAbsKwMap = this.extractKwAndTfInContent(pubAbstract);
        if (extractTitleKwMap.get("EXTRACT_KWS_STRSETS") != null) {
            extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");
        }
        if (extractAbsKwMap.get("EXTRACT_KWS_STRSETS") != null) {
            extractAbsKwStrings = (Set<String>) extractAbsKwMap.get("EXTRACT_KWS_STRSETS");
        }
        if (StringUtils.isNotBlank(pubKw)) {
            pubKw = pubKw.replace("；", ";");
            String[] kws = pubKw.split(";");
            if (kws != null || kws.length > 0) {
                for (String str : kws) {
                    if (StringUtils.isNotEmpty(str)) {
                        str = StringUtils.trimToEmpty(str);
                        str = str.toLowerCase().replaceAll("\\s+", " ");
                        extractTitleKwStrings.add(str);
                        // 合并标题与自填关键词tf
                        Integer tf = extractTitleKwMap.get(str) == null ? 0 : (Integer) extractTitleKwMap.get(str);
                        if (0 == tf) {
                            extractTitleKwMap.put(str, 1);
                        } else {
                            tf = tf + 1;
                            extractTitleKwMap.put(str, tf);
                        }
                    }
                }
            }
        }
        // 存储标题与自填关键词
        List<String> titleKwList = new ArrayList<String>();
        for (String keyTitle : extractTitleKwStrings) {
            titleKwList.add(keyTitle);
        }
        if (CollectionUtils.isNotEmpty(titleKwList)) {
            for (String kw : titleKwList) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                Integer tfInt = extractTitleKwMap.get(kw) == null ? 0 : (Integer) extractTitleKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长从4到9，加强TF为6倍;
                if (language == 0 && kw.length() >= 4 && kw.length() <= 9) {
                    tfInt = tfInt * 5;
                } else if (language == 0 && kw.length() == 3) {
                    tfInt = tfInt * 3;
                } else if (language == 1) {
                    Integer lengthEn = kw.split(" ").length;
                    if (lengthEn > 1 && lengthEn <= 6) {
                        tfInt = tfInt * 3;
                    }
                }
                rsKwMap.put(kw, tfInt * 100000000);
            }
        }

        if (CollectionUtils.isNotEmpty(extractAbsKwStrings)) {
            List<String> absKwList = new ArrayList<String>();
            for (String kw : extractAbsKwStrings) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                // 在此处0为自填关键词
                Integer tfInt = extractAbsKwMap.get(kw) == null ? 0 : (Integer) extractAbsKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长从4到9，加强TF为6倍;
                if (language == 0 && kw.length() >= 4 && kw.length() <= 9) {
                    tfInt = tfInt * 5;
                } else if (language == 0 && kw.length() == 3) {
                    tfInt = tfInt * 3;
                } else if (language == 1) {
                    Integer lengthEn = kw.split(" ").length;
                    if (lengthEn > 1 && lengthEn <= 6) {
                        tfInt = tfInt * 3;
                    }
                }
                // 当标题与自填关键词中有对应关键词，则提取出来
                if (!extractTitleKwStrings.contains(kw)) {
                    absKwList.add(kw);
                }
                // 合并标题自填关键词与摘要关键词tf
                Integer tf = rsKwMap.get(kw) == null ? 0 : (Integer) rsKwMap.get(kw);
                if (0 == tf) {
                    rsKwMap.put(kw, tfInt);
                } else {
                    tf = tf + tfInt;
                    rsKwMap.put(kw, tf);
                }
            }

            if (absKwList.size() > 0 && titleKwList.size() > 0) {
                List<String> extractKws = this.kGPubCooperationDao.getKwByTfExcludePubKw(titleKwList, absKwList, 2);
                if (extractKws != null && extractKws.size() > 0) {
                    List<Map<String, Object>> rsMap =
                            this.kGPubCooperationDao.getKwByCoTfCoutExcludePubKw(titleKwList, absKwList, 2);
                    int i = 0;
                    for (String kw : extractKws) {
                        Map<String, Object> map = rsMap.get(i);
                        Integer cotf = 0;
                        if (map != null && map.size() > 0) {
                            cotf = ((BigDecimal) map.get("COUNTS")).intValue();
                        }
                        // 合并标题自填关键词与摘要关键词cotf得分
                        Integer tf = rsKwMap.get(kw) == null ? 0 : (Integer) rsKwMap.get(kw);
                        if (0 == tf) {
                            rsKwMap.put(kw, cotf * 10000);
                        } else {
                            tf = tf + cotf * 10000;
                            rsKwMap.put(kw, tf);
                        }
                        i++;
                    }
                }
            }
        }
        if (rsKwMap.size() > 0) {
            for (Entry<String, Integer> et : rsKwMap.entrySet()) {
                if (StringUtils.isEmpty(et.getKey())) {
                    continue;
                }
                NsfcKwScoreForSorting nkfs =
                        new NsfcKwScoreForSorting(et.getKey(), et.getKey().length(), et.getValue());
                rsList.add(nkfs);
            }
            Collections.sort(rsList);
        }
        return rsList;
    }

    private List<NsfcKwScoreForSorting> getPubKws(String title, String pubAbstract, String pubKw) {
        List<NsfcKwScoreForSorting> rsList = new ArrayList<NsfcKwScoreForSorting>();
        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(pubKw)) {
            return null;
        }
        Map<String, Integer> rsKwMap = new HashMap<String, Integer>();
        Map<String, Object> extractTitleKwMap = new HashMap<String, Object>();
        Map<String, Object> extractAbsKwMap = new HashMap<String, Object>();
        Set<String> extractTitleKwStrings = new TreeSet<String>();
        Set<String> extractAbsKwStrings = new TreeSet<String>();
        extractTitleKwMap = this.extractKwAndTfInContent(title);
        extractAbsKwMap = this.extractKwAndTfInContent(pubAbstract);
        if (extractTitleKwMap.get("EXTRACT_KWS_STRSETS") != null) {
            extractTitleKwStrings = (Set<String>) extractTitleKwMap.get("EXTRACT_KWS_STRSETS");
        }
        if (extractAbsKwMap.get("EXTRACT_KWS_STRSETS") != null) {
            extractAbsKwStrings = (Set<String>) extractAbsKwMap.get("EXTRACT_KWS_STRSETS");
        }
        if (StringUtils.isNotBlank(pubKw)) {
            pubKw = pubKw.replace("，", ";");
            pubKw = pubKw.replace(",", ";");
            pubKw = pubKw.replace("；", ";");
            String[] kws = pubKw.split(";");
            if (kws != null || kws.length > 0) {
                for (String str : kws) {
                    if (StringUtils.isNotEmpty(str)) {
                        str = StringUtils.trimToEmpty(str);
                        str = str.toLowerCase().replaceAll("\\s+", " ");
                        extractTitleKwStrings.add(str);
                        // 合并标题与自填关键词tf
                        Integer tf = extractTitleKwMap.get(str) == null ? 0 : (Integer) extractTitleKwMap.get(str);
                        if (0 == tf) {
                            extractTitleKwMap.put(str, 1);
                        } else {
                            tf = tf + 1;
                            extractTitleKwMap.put(str, tf);
                        }
                    }
                }
            }
        }
        // 存储标题与自填关键词
        List<String> titleKwList = new ArrayList<String>();
        for (String keyTitle : extractTitleKwStrings) {
            titleKwList.add(keyTitle);
        }
        if (CollectionUtils.isNotEmpty(titleKwList)) {
            for (String kw : titleKwList) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                Integer tfInt = extractTitleKwMap.get(kw) == null ? 0 : (Integer) extractTitleKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长从4到9，加强TF为6倍;
                if (language == 0 && kw.length() >= 4 && kw.length() <= 9) {
                    tfInt = tfInt * 5;
                } else if (language == 0 && kw.length() == 3) {
                    tfInt = tfInt * 3;
                } else if (language == 1) {
                    Integer lengthEn = kw.split(" ").length;
                    if (lengthEn > 1 && lengthEn <= 6) {
                        tfInt = tfInt * 3;
                    }
                }
                rsKwMap.put(kw, tfInt * 100000000);
            }
        }

        if (CollectionUtils.isNotEmpty(extractAbsKwStrings)) {
            List<String> absKwList = new ArrayList<String>();
            for (String kw : extractAbsKwStrings) {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                // 在此处0为自填关键词
                Integer tfInt = extractAbsKwMap.get(kw) == null ? 0 : (Integer) extractAbsKwMap.get(kw);
                Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
                // 中文关键词词长从4到9，加强TF为6倍;
                if (language == 0 && kw.length() >= 4 && kw.length() <= 9) {
                    tfInt = tfInt * 5;
                } else if (language == 0 && kw.length() == 3) {
                    tfInt = tfInt * 3;
                } else if (language == 1) {
                    Integer lengthEn = kw.split(" ").length;
                    if (lengthEn > 1 && lengthEn <= 6) {
                        tfInt = tfInt * 3;
                    }
                }
                // 当标题与自填关键词中有对应关键词，则提取出来
                if (!extractTitleKwStrings.contains(kw)) {
                    absKwList.add(kw);
                }
                // 合并标题自填关键词与摘要关键词tf
                Integer tf = rsKwMap.get(kw) == null ? 0 : (Integer) rsKwMap.get(kw);
                if (0 == tf) {
                    rsKwMap.put(kw, tfInt);
                } else {
                    tf = tf + tfInt;
                    rsKwMap.put(kw, tf);
                }
            }

            if (absKwList.size() > 0 && titleKwList.size() > 0) {
                Integer lang = 2;
                List<String> extractKws = this.kGPubCooperationDao.getKwByTfExcludePubKw(titleKwList, absKwList, lang);
                if (extractKws == null || extractKws.size() <= 0) {
                    lang = 1;
                    extractKws = this.kGPubCooperationDao.getKwByTfExcludePubKw(titleKwList, absKwList, lang);
                }
                if (extractKws != null && extractKws.size() > 0) {
                    List<Map<String, Object>> rsMap =
                            this.kGPubCooperationDao.getKwByCoTfCoutExcludePubKw(titleKwList, absKwList, lang);
                    int i = 0;
                    for (String kw : extractKws) {
                        Map<String, Object> map = rsMap.get(i);
                        Integer cotf = 0;
                        if (map != null && map.size() > 0) {
                            cotf = ((BigDecimal) map.get("COUNTS")).intValue();
                        }
                        // 合并标题自填关键词与摘要关键词cotf得分
                        Integer tf = rsKwMap.get(kw) == null ? 0 : (Integer) rsKwMap.get(kw);
                        if (0 == tf) {
                            rsKwMap.put(kw, cotf * 10000);
                        } else {
                            tf = tf + cotf * 10000;
                            rsKwMap.put(kw, tf);
                        }
                        i++;
                    }
                }
            }
        }
        if (rsKwMap.size() > 0) {
            for (Entry<String, Integer> et : rsKwMap.entrySet()) {
                if (StringUtils.isEmpty(et.getKey())) {
                    continue;
                }
                NsfcKwScoreForSorting nkfs =
                        new NsfcKwScoreForSorting(et.getKey(), et.getKey().length(), et.getValue());
                rsList.add(nkfs);
            }
            Collections.sort(rsList);
        }
        return rsList;
    }

    @Override
    public void getScmPubKwsByNsfcBaseKws(String category, Integer language) {
        List<String> nsfcKws = this.getNsfcBaseKwByCategory(category, language);
        // 获取包含关键词的成果id
        List<String> pubKws = new ArrayList<String>();
        if (nsfcKws != null && nsfcKws.size() > 0) {
            pubKws = this.getPubKws(nsfcKws, category, 1);
        }
        for (String kw : pubKws) {
            // 计算TF与COTF
            Integer tf = this.nsfcKwsTfCotfDao.getPubKwTf(category, kw);
            Integer cotf = this.nsfcKwsTfCotfDao.getPubKwCotf(category, pubKws, kw, language);
        }
    }

    @Override
    public List<String> getPubKws(List<String> kws, String category, Integer language) {
        List<String> pubKws = this.nsfcKwsTfCotfDao.getRelatedPubKws(category, kws, language);
        List<String> rsList = new ArrayList<String>();
        for (String kw : pubKws) {
            if (!kws.contains(kw)) {
                rsList.add(kw);
            }
        }
        return rsList;
    }

    @Override
    public List<String> getNsfcBaseKwByCategory(String category, Integer language) {
        return this.nsfcKwsTfCotfDao.getBaseNsfcKwsByCategory(category, language);
    }

    @Override
    public Integer getPubKwTf(String category, String kw) {
        return this.nsfcKwsTfCotfDao.getPubKwTf(category, kw);
    }

    @Override
    public Integer getPubKwCotf(String category, List<String> categoryNsfcKws, String kw, Integer language) {
        return this.nsfcKwsTfCotfDao.getPubKwCotf(category, categoryNsfcKws, kw, language);
    }

    @Override
    public void saveScmKwsInfo(String category, String kw, Integer tf, Integer cotf) {
        this.nsfcKwsTfCotfDao.saveScmPubKwsInfo(category, kw, tf, cotf, XmlUtil.isChinese(kw) ? 0 : 1);
    }

    @Override
    public void deleteScmKwsByCategory(String category, Integer language) {
        this.nsfcKwsTfCotfDao.deleteScmPubKwsInfo(category, language);
    }

    @Override
    public List<Map<String, Object>> getEncodedPubIdList(Integer size, Long status) {
        return nsfcKwsTfCotfDao.getPubIdStr(size, status);
    }

    @Override
    public Long getPdwhPubId(String title) {
        Long journalTitleHash = PubHashUtils.cleanTitleHash(title);
        return pdwhPubDuplicateDAO.getPubPdwhIdByTitleHash(journalTitleHash);
    }

    @Override
    public List<String> getPdwhPubKwsFromDb(Long pubId) {
        return this.nsfcKwsTfCotfDao.getPubKws(pubId);
    }

    @Override
    public void saveKws(List<String> kws, Long pubId, String category) {
        List<String> existkws = this.nsfcKwsTfCotfDao.hasPubKwsRecords(pubId, category);
        if (existkws != null && existkws.size() >= kws.size()) {
            return;
        }
        this.nsfcKwsTfCotfDao.deletehasPubKwsRecords(pubId, category);
        for (String kw : kws) {
            try {
                kw = StringUtils.trimToEmpty(kw);
                if (StringUtils.isEmpty(kw)) {
                    continue;
                }
                kw = kw.toLowerCase().replace("-", " ").replaceAll("\\s+", " ");
                this.nsfcKwsTfCotfDao.saveScmPubKws(pubId, XmlUtil.isChinese(kw) ? 0 : 1, kw, category);
            } catch (Exception e) {
                logger.error("存储关键词出错，pdwhpubid=" + pubId + "===============", e);
            }
        }
    }

    @Override
    public void updateNsfcPrjPubStatus(Long pubId, Integer status) {
        this.nsfcKwsTfCotfDao.updateNsfcPrjPubStatus(pubId, status);
    }

    /**
     * 为成果提取标准关键词，无限制,限定不超过55个；否则计算长度为5的子集时可能会内存溢出。60个关键词，长度为5的子集已经有5461512个
     * 
     */
    @Override
    public List<String> getExtractKwsFromStr(Long pubId) {
        PubPdwhDetailDOM pubPdwh = pdwhPublicationService.getFullPdwhPubInfoById(pubId);
        if (pubPdwh == null) {
            return null;
        }
        List<String> rsList = new ArrayList<String>();
        Set<String> extractKwStrings = new TreeSet<String>();
        // 马博士要求20190403-只使用自填关键词与title
        // extractKwStrings = getRsSetNew(pubPdwh.getTitle() + " " + pubPdwh.getSummary());
        extractKwStrings = getRsSetNew(pubPdwh.getTitle());
        /*
         * if (extractKwStrings != null && extractKwStrings.size() > 0) { System.out .println(pubId +
         * "==从标题中提取" + extractKwStrings.size() + "个关键词, 其为: " + extractKwStrings.toString() + "=="); }
         */
        if (StringUtils.isNotBlank(pubPdwh.getKeywords())) {
            String pubKw = pubPdwh.getKeywords();
            pubKw = pubKw.replace("，", ";");
            pubKw = pubKw.replace(",", ";");
            pubKw = pubKw.replace("；", ";");
            String[] kws = pubKw.split(";");
            if (kws != null && kws.length > 0) {
                for (String str : kws) {
                    if (StringUtils.isNotEmpty(str)) {
                        str = StringUtils.trimToEmpty(str);
                        str = str.toLowerCase().replaceAll("\\s+", " ");
                        extractKwStrings.add(str);
                    }
                }
            }
        }
        if (extractKwStrings != null && extractKwStrings.size() > 0) {
            Integer size = 1;
            for (String kw : extractKwStrings) {
                if (size > 55) {
                    break;
                }
                rsList.add(kw);
                size++;
            }
            Collections.sort(rsList);
        }
        return rsList;
    }

    /**
     * 为项目提取标准关键词，无限制,限定不超过55个；否则计算长度为5的子集时可能会内存溢出。60个关键词，长度为5的子集已经有5461512个
     * 
     */
    @Override
    public Map<String, List<String>> getExtractZhKwsFromPrjStr(Long prjId) {
        Map<String, Object> prjInfo = this.nsfcKwsTfCotfDao.getNsfcPrpInfo10Years(prjId);
        if (prjInfo == null) {
            return null;
        }
        String zhTitle = prjInfo.get("ZH_TITLE") == null ? "" : (String) prjInfo.get("ZH_TITLE");
        String enTitle = prjInfo.get("EN_TITLE") == null ? "" : (String) prjInfo.get("EN_TITLE");
        String zhKw = prjInfo.get("ZH_KEYWORDS") == null ? "" : (String) prjInfo.get("ZH_KEYWORDS");
        String enKw = prjInfo.get("EN_KEYWORDS") == null ? "" : (String) prjInfo.get("EN_KEYWORDS");
        String category = prjInfo.get("DISCODE") == null ? "" : (String) prjInfo.get("DISCODE");
        List<String> zhExtractKws = this.extractKwsForCotf(zhTitle, zhKw);
        List<String> enExtractKws = this.extractKwsForCotf(enTitle, enKw);
        List<String> categoryList = new ArrayList<String>();
        Map<String, List<String>> rsMap = new HashMap<String, List<String>>();
        if (zhExtractKws != null && zhExtractKws.size() > 0) {
            rsMap.put("zhKws", zhExtractKws);
        }
        if (enExtractKws != null && enExtractKws.size() > 0) {
            rsMap.put("enKws", enExtractKws);
        }
        if (StringUtils.isNotBlank(category) && category.length() >= 3) {
            category = category.substring(0, 3);
            categoryList.add(category);
            rsMap.put("category", categoryList);
        }
        return rsMap;
    }

    private List<String> extractKwsForCotf(String title, String pubKw) {
        List<String> rsList = new ArrayList<String>();
        Set<String> extractKwStrings = new TreeSet<String>();
        // 马博士要求20190403-只使用自填关键词与title
        // extractKwStrings = getRsSetNew(pubPdwh.getTitle() + " " + pubPdwh.getSummary());
        extractKwStrings = getRsSetNew(title);
        /*
         * if (extractKwStrings != null && extractKwStrings.size() > 0) { System.out .println(pubId +
         * "==从标题中提取" + extractKwStrings.size() + "个关键词, 其为: " + extractKwStrings.toString() + "=="); }
         */
        if (StringUtils.isNotBlank(pubKw)) {
            pubKw = pubKw.replace("，", ";");
            pubKw = pubKw.replace(",", ";");
            pubKw = pubKw.replace("；", ";");
            String[] kws = pubKw.split(";");
            if (kws != null && kws.length > 0) {
                for (String str : kws) {
                    if (StringUtils.isNotEmpty(str)) {
                        str = StringUtils.trimToEmpty(str);
                        str = str.toLowerCase().replaceAll("\\s+", " ");
                        extractKwStrings.add(str);
                    }
                }
            }
        }
        if (extractKwStrings != null && extractKwStrings.size() > 0) {
            Integer size = 1;
            for (String kw : extractKwStrings) {
                if (size > 55) {
                    break;
                }
                rsList.add(kw);
                size++;
            }
            Collections.sort(rsList);
        }
        return rsList;
    }

    @Override
    public List<String> getGrpToHandleKwList() {
        return this.nsfcKwsTfCotfDao.getGrpKwsList();
    }

    /**
     * 1.项目（批准号不为空）；2申请书（批准号为空）
     * 
     */
    @Override
    public List<Map<String, Object>> getNsfcPrjInfo(String nsfcDisc, Integer type) {
        if (StringUtils.isBlank(nsfcDisc)) {
            return null;
        }
        if (type == 1) {
            return this.nsfcKwsTfCotfDao.getNsfcPrjInfoByDiscLast10Year(nsfcDisc);
        } else {
            return this.nsfcKwsTfCotfDao.getNsfcPrpInfoByDiscLast10Year(nsfcDisc);
        }
    }

    /**
     * 项目相关成果信息
     * 
     */
    @Override
    public List<Map<String, Object>> getNsfcPrjRelatedPub(String nsfcDisc) {
        // 获取此discode下所有项目批准号
        if (StringUtils.isBlank(nsfcDisc)) {
            return null;
        }
        List<String> approveNumList = this.nsfcKwsTfCotfDao.getNsfcPrjApproveNumByDiscLast10Year(nsfcDisc);
        if (approveNumList == null || approveNumList.size() <= 0) {
            return null;
        }
        List<BigDecimal> relatedPubId = this.nsfcKwsTfCotfDao.getPubInfoByApproveNum(approveNumList);

        List<Map<String, Object>> rsMapList = new ArrayList<Map<String, Object>>();
        for (BigDecimal pubId : relatedPubId) {
            List<String> pubPdwh = this.nsfcKwsTfCotfDao.getPubKws(pubId.longValue());
            if (pubPdwh == null || pubPdwh.size() <= 0) {
                continue;
            }
            Map<String, Object> pubMap = new HashMap<String, Object>();
            StringBuilder sb = new StringBuilder();
            for (String kw : pubPdwh) {
                if (StringUtils.isNotBlank(kw)) {
                    // 中英文用括号括起来的直接去掉
                    sb.append(kw);
                    sb.append(";");
                    // 如果是先中文，再英文，吧
                }
            }
            pubMap.put("ZH_KWS", sb.toString());
            Object yearO = this.nsfcKwsTfCotfDao.getPubYear(pubId.longValue());
            if (yearO != null) {
                pubMap.put("YEAR", (BigDecimal) yearO);
            }
            rsMapList.add(pubMap);
        }
        return rsMapList;
    }

    // 提取括号中的内容
    @Override
    public Set<String> getKwFromBracket(String kw) {
        Set<String> kwsSet = new TreeSet<String>();
        String separatorStart = "";
        String separatorEnd = "";
        if (kw.indexOf("(") < 0 && kw.indexOf("[") < 0 && kw.indexOf("{") < 0) {
            kwsSet.add(kw);
        } else {
            int i = 0;
            int start = 0;
            char[] kwChar = kw.toCharArray();
            while (i < kwChar.length) {
                if (kwChar[i] == '(') {
                    separatorStart = "(";
                    separatorEnd = ")";
                } else if (kwChar[i] == '[') {
                    separatorStart = "[";
                    separatorEnd = "]";
                } else if (kwChar[i] == '{') {
                    separatorStart = "{";
                    separatorEnd = "}";
                } else {
                    i++;
                    continue;
                }
                Integer end = kw.indexOf(separatorEnd, i + 1);
                if (end < 0) {
                    if (i - start > 1) {
                        kwsSet.add(kw.substring(start, i));
                    }
                    start = ++i;
                } else {
                    kwsSet.add(kw.substring(start, i));
                    kwsSet.add(kw.substring(i + 1, end));
                    i = ++end;
                    start = i;
                }
            }
        }
        return kwsSet;
    }

    /**
     * 保存关键词结果
     * 
     */
    @Override
    public void saveNsfcKws(Map<String, Map<Integer, Double>> rsMap, String applicationCode) {
        if (rsMap == null || rsMap.size() <= 0) {
            return;
        }
        for (Entry<String, Map<Integer, Double>> et : rsMap.entrySet()) {
            String kw = et.getKey();
            if (kw.length() > 149) {
                continue;
            }
            Map<Integer, Double> tfMap = et.getValue();
            Double ytf = 0.0;
            Long kwId = this.nsfcKwsTfCotfDao.getNsfcKwPk();
            Integer language = XmlUtil.isChinese(kw) ? 0 : 1;
            Integer type = tfMap.get(99999) != null ? 1 : 0;
            Integer thisYear = Calendar.getInstance().get(Calendar.YEAR);
            // 按年份存储wtf
            for (Entry<Integer, Double> kwEt : tfMap.entrySet()) {
                Integer kwYear = kwEt.getKey();
                Double kwWtf = kwEt.getValue();
                this.nsfcKwsTfCotfDao.insertIntoNsfcwtf(kwId, applicationCode, kw, language, kwYear, kwWtf);
                if (kwYear != 9999) {
                    // yTF = Log10(10-(This year-Year)+1)*wTF
                    Integer a = 11 - (thisYear - kwYear);
                    if (a <= 0) {
                        // 部分成果可能日期有误或者引用很早的成果，直接跳过
                        continue;
                    }
                    kwWtf = Math.log10(a) * kwWtf;
                    ytf = ytf + kwWtf;
                }
            }
            this.nsfcKwsTfCotfDao.insertIntoNsfcYtf(kwId, applicationCode, kw, language, ytf, type);
        }
    }

    @Override
    public void saveNsfcKwsCotf(String kwCotfStr) throws Exception {
        if (StringUtils.isBlank(kwCotfStr)) {
            return;
        }
        String[] kwStrs = kwCotfStr.split(";");
        if (kwStrs == null || kwStrs.length > 5 || kwStrs.length <= 1) {
            return;
        }

        String[] start = kwStrs[0].split("-!");
        String discode = "";
        String firstKw = "";
        Integer subStrStart = 0;
        if (start.length > 1) {
            discode = start[0];
            firstKw = start[1];
            subStrStart = kwStrs[0].indexOf("-!") + 2;
        } else {
            firstKw = kwStrs[0];
        }
        String end = kwStrs[kwStrs.length - 1];
        Integer laseSpace = end.lastIndexOf("\t");
        if (laseSpace == -1) {
            return;
        }
        String tfStr = StringUtils.trimToEmpty(end.substring(laseSpace, end.length()));
        if (StringUtils.isBlank(tfStr)) {
            return;
        }
        Integer cotf = Integer.parseInt(tfStr);
        String lastKw = StringUtils.trimToEmpty(end.substring(0, laseSpace));
        if (StringUtils.isBlank(discode)) {
            discode = "9999";
        }
        Integer language = XmlUtil.isChinese(kwCotfStr) ? 0 : 1;
        Integer size = kwStrs.length;
        Integer subStrEnd = kwCotfStr.lastIndexOf("\t");
        switch (size) { // nsfcKwsTfCotfDao，存储的位置从调整至tmppdwh临时库
            case 2:
                this.nsfcKwsCotfTwoDao.insertIntoNsfcCotfLength2(discode, kwCotfStr.substring(subStrStart, subStrEnd),
                        firstKw, lastKw, cotf, language);
                break;
            case 3:
                this.nsfcKwsCotfTwoDao.insertIntoNsfcCotfLength3(discode, kwCotfStr.substring(subStrStart, subStrEnd),
                        firstKw, kwStrs[1], lastKw, cotf, language);
                break;
            case 4:
                this.nsfcKwsCotfTwoDao.insertIntoNsfcCotfLength4(discode, kwCotfStr.substring(subStrStart, subStrEnd),
                        firstKw, kwStrs[1], kwStrs[2], lastKw, cotf, language);
                break;
            case 5:
                this.nsfcKwsCotfTwoDao.insertIntoNsfcCotfLength5(discode, kwCotfStr.substring(subStrStart, subStrEnd),
                        firstKw, kwStrs[1], kwStrs[2], kwStrs[3], lastKw, cotf, language);
                break;
            default:
                return;
        }
    }

    @Override
    public String getPubNsfcCategory(Long pubId) {
        return this.nsfcKwsTfCotfDao.getPubNsfcCategory(pubId);
    }

    @Override
    public Long getTaskId() {
        return this.nsfcKwsTfCotfDao.getTaskId();
    }

    @Override
    public void insertIntoNsfcTaskStatus(Long taskId, String dataSection, Date startTime) {
        this.nsfcKwsTfCotfDao.insertIntoNsfcTaskStatus(taskId, dataSection, startTime);
    }

    @Override
    public void updateNsfcTaskStatus(Long taskId, Date endTime) {
        this.nsfcKwsTfCotfDao.updateNsfcTaskStatus(taskId, endTime);
    }

    @Override
    public Integer getTaskStatusByDataSection(String name) {
        return this.nsfcKwsTfCotfDao.getTaskStatusByDataSection(name);
    }

    @Override
    public Integer getTranslateNsfcPrjKwsBySeq(Long pId) {
        Map<String, Object> prjInfo = this.nsfcKwsTfCotfDao.getNsfcPrpInfo10Years(pId);
        if (prjInfo == null) {
            return 2;
        }
        String zhKw = prjInfo.get("ZH_KEYWORDS") == null ? "" : (String) prjInfo.get("ZH_KEYWORDS");
        String enKw = prjInfo.get("EN_KEYWORDS") == null ? "" : (String) prjInfo.get("EN_KEYWORDS");
        String category = prjInfo.get("DISCODE") == null ? "" : (String) prjInfo.get("DISCODE");
        List<String> enKwsList = new ArrayList<String>();
        List<String> zhKwsList = new ArrayList<String>();
        if (StringUtils.isNotBlank(zhKw)) {
            zhKw = zhKw.replace("，", ";");
            zhKw = zhKw.replace(",", ";");
            zhKw = zhKw.replace("；", ";");
            String[] zhKws = zhKw.split(";");
            for (String kw : zhKws) {
                if (StringUtils.isNotBlank(kw)) {
                    zhKwsList.add(kw);
                }
            }
        }
        if (StringUtils.isNotBlank(enKw)) {
            enKw = enKw.toLowerCase();
            enKw = enKw.replace("，", ";");
            enKw = enKw.replace(",", ";");
            enKw = enKw.replace("；", ";");
            String[] enKws = enKw.split(";");
            for (String kw : enKws) {
                if (StringUtils.isNotBlank(kw)) {
                    enKwsList.add(kw);
                }
            }
        }
        if (enKwsList != null && zhKwsList != null && enKwsList.size() > 0 && zhKwsList.size() > 0) {
            if (enKwsList.size() != zhKwsList.size()) {
                return 4;
            }
            for (int i = 0; i < enKwsList.size(); i++) {
                this.nsfcKwsTfCotfDao.insertIntoNsfcKwsTranslation(pId, zhKwsList.get(i), enKwsList.get(i), category);
            }
            return 1;
        } else {
            return 5;
        }

    }

    public static void main(String[] args) {
        Integer laseSpace = "Subset_Size==2;Cotf==1 6".lastIndexOf(" ");
        System.out.println(laseSpace);
    }
}
