package com.smate.center.batch.service.pdwh.prj;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.KeywordsHighAndNewTechDao;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

@Service("highTechKeywordsService")
@Transactional(rollbackFor = Exception.class)
public class HighTechKeywordsServiceImpl implements HighTechKeywordsService {

  @Autowired
  private KeywordsHighAndNewTechDao keywordsHighAndNewTechDao;
  @Autowired
  private PdwhPublicationService pdwhPublicationService;

  @Override
  public List<BigDecimal> getHighTechPubToHandleList(Integer size) {
    return this.keywordsHighAndNewTechDao.getHighTechPubToHandleList(size);
  }

  @Override
  public void updateHighTechPubStatus(Long pubId, Integer status) {
    keywordsHighAndNewTechDao.updateHighTechPubToHandleStatus(pubId, status);
  }

  @Override
  public BigDecimal getHighTechPubToHandleNum() {
    return keywordsHighAndNewTechDao.getHighTechPubToHandleNum();
  }

  @Override
  public Integer HighTechClassificationForPub(Long pubId) throws Exception {
    PubPdwhDetailDOM pubPdwh = pdwhPublicationService.getFullPdwhPubInfoById(pubId);
    if (pubPdwh == null) {
      return 9;
    }
    List<Map<String, Object>> rsList = new ArrayList<Map<String, Object>>();
    Set<String> extractKwStrings = new TreeSet<String>();
    // 马博士要求20190403-只使用自填关键词与title
    // extractKwStrings = getRsSetNew(pubPdwh.getTitle() + " " + pubPdwh.getSummary());
    extractKwStrings =
        getHighTechRsSetNew(pubPdwh.getTitle() + " " + pubPdwh.getSummary() + " " + pubPdwh.getKeywords());
    if (extractKwStrings == null || extractKwStrings.size() <= 0) {
      return 2;
    }
    /*
     * if (extractKwStrings != null && extractKwStrings.size() > 0) { System.out .println(pubId +
     * "==从标题中提取" + extractKwStrings.size() + "个关键词, 其为: " + extractKwStrings.toString() + "=="); } if
     * (StringUtils.isNotBlank(pubPdwh.getKeywords())) { String pubKw = pubPdwh.getKeywords(); pubKw =
     * pubKw.replace("，", ";"); pubKw = pubKw.replace(",", ";"); pubKw = pubKw.replace("；", ";");
     * String[] kws = pubKw.split(";"); if (kws != null && kws.length > 0) { for (String str : kws) { if
     * (StringUtils.isNotEmpty(str)) { str = StringUtils.trimToEmpty(str); str =
     * str.toLowerCase().replaceAll("\\s+", " "); extractKwStrings.add(str); } } } }
     */
    // 按照关键词所属分类的包含的不重复关键词个数排序
    rsList = this.keywordsHighAndNewTechDao.getHighTechClassification(extractKwStrings);
    if (rsList == null || rsList.size() <= 0) {
      return 2;
    }
    this.keywordsHighAndNewTechDao.deleteHighTechRs(pubId);
    for (Map<String, Object> cMap : rsList) {
      String category = (String) cMap.get("CATEGORY");
      Integer kwNum = ((BigDecimal) cMap.get("KWNUM")).intValue();
      this.keywordsHighAndNewTechDao.saveHighTechRs(pubId, category, kwNum);
    }
    return 1;
  }

  private Set<String> getHighTechRsSetNew(String content) {
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
      if ("hightech_kw".equals(t.getNatureStr())) {
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
    return extractKwStrings;
  }
}
