package com.smate.center.batch.service.pub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.PatentCategpruNsfcDao;
import com.smate.center.batch.dao.pdwh.pub.PsnKeywordsSubsetsDao;
import com.smate.center.batch.dao.pdwh.pub.PubCategpruNsfcDao;
import com.smate.center.batch.dao.pdwh.pub.PubKeywordsSubsetDescDao;
import com.smate.center.batch.dao.pdwh.pub.PubKeywordsSubsetsClassificationRsDao;
import com.smate.center.batch.dao.pdwh.pub.PubKeywordsSubsetsCotfDao;
import com.smate.center.batch.dao.pdwh.pub.PubKeywordsSubsetsCotfHntDao;
import com.smate.center.batch.dao.pdwh.pub.PubKeywordsSubsetsDao;
import com.smate.center.batch.dao.pdwh.pub.PubKeywordsSubsetsHntDao;
import com.smate.center.batch.dao.pdwh.pub.PubKeywordsSubsetsHntTmpDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubXmlDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPublicationDao;
import com.smate.center.batch.dao.sns.prj.TmpDestDao;
import com.smate.center.batch.dao.sns.pub.KeywordsHighAndNewTechDao;
import com.smate.center.batch.dao.sns.pub.ProjectDataFiveYearDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.dao.sns.pub.ScmPubXmlDao;
import com.smate.center.batch.model.pdwh.prj.NsfcKwsTfCotfDetail;
import com.smate.center.batch.model.pdwh.pub.KeywordsHighAndNewTech;
import com.smate.center.batch.model.pdwh.pub.PatentCategpruNsfc;
import com.smate.center.batch.model.pdwh.pub.PsnKeywordsSubsets;
import com.smate.center.batch.model.pdwh.pub.PubCategpruNsfc;
import com.smate.center.batch.model.pdwh.pub.PubKeywordsSubsetDesc;
import com.smate.center.batch.model.pdwh.pub.PubKeywordsSubsets;
import com.smate.center.batch.model.pdwh.pub.PubKeywordsSubsetsHntTmp;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXml;
import com.smate.center.batch.model.sns.pub.ProjectDataFiveYear;
import com.smate.center.batch.model.sns.pub.ScmPubXml;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.solr.task.SolrIndexDifService;
import com.smate.center.batch.util.pub.MessageDigestUtils;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.common.MD5Util;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;

@Service("projectDataFiveYearService")
@Transactional(rollbackFor = Exception.class)
public class ProjectDataFiveYearServiceImpl implements ProjectDataFiveYearService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ProjectDataFiveYearDao projectDataFiveYearDao;
  @Autowired
  private PubKeywordsSubsetsDao pubKeywordsSubsetsDao;
  @Autowired
  private PubKeywordsSubsetsCotfDao pubKeywordsSubsetsCotfDao;
  @Autowired
  private PubKeywordsSubsetsClassificationRsDao pubKeywordsSubsetsClassificationRsDao;
  @Autowired
  private KeywordsHighAndNewTechDao keywordsHighAndNewTechDao;
  @Autowired
  private PubKeywordsSubsetsHntDao pubKeywordsSubsetsHntDao;
  @Autowired
  private PubKeywordsSubsetsHntTmpDao pubKeywordsSubsetsHntTmpDao;
  @Autowired
  private PubKeywordsSubsetsCotfHntDao pubKeywordsSubsetsCotfHntDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private PsnKeywordsSubsetsDao psnKeywordsSubsetsDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private PatentCategpruNsfcDao patentCategpruNsfcDao;
  @Autowired
  private PubKeywordsSubsetDescDao pubKeywordsSubsetDescDao;
  @Autowired
  private TmpDestDao tmpDestDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private PubCategpruNsfcDao pubCategpruNsfcDao;
  @Autowired
  private SolrIndexDifService solrIndexDifService;

  @Override
  public List<Object> findPatentList(Integer size, Long startPubId, Long endPubId) {
    return pdwhPublicationDao.findListByIdBet(size, startPubId, endPubId);
  }

  @Override
  public List<ProjectDataFiveYear> getProjectDataList(Integer size, Long id) {
    return projectDataFiveYearDao.getProjectDataList(size, id);
  }

  @Override
  public List<Object> findPubList(Integer size, Long startPubId, Long endPubId) {
    return pdwhPublicationDao.findPubListByIdBet(size, startPubId, endPubId);
  }

  @Override
  public Long getTotalCounts() {
    return projectDataFiveYearDao.getTotalCount();
  }

  @Override
  public Set<String> handlePubKeywords(String keywords) {
    TreeSet<String> treeSet = new TreeSet<String>();
    String[] keywordsString = keywords.replace("；", ";").toLowerCase().split(";");
    for (String keyword : keywordsString) {
      String kw = keyword.replaceAll("\\s+", " ").trim();
      treeSet.add(MessageDigestUtils.messageDigest(kw));
    }
    return treeSet;
  }

  @Override
  public Map<Integer, Map<Integer, String>> getPubKwSubsets(String kws) {
    String[] kwString = kws.toLowerCase().split(",");
    String[] newKwString;
    // set自动去重
    Set<String> newSet = new TreeSet<String>();
    for (String keyword : kwString) {
      if (StringUtils.isNotEmpty(keyword)) {
        keyword = keyword.replaceAll("\\s+", " ").trim();
        newSet.add(keyword);
      }
    }
    if (newSet.size() > 0) {
      newKwString = newSet.toArray(new String[newSet.size()]);
      Arrays.sort(newKwString);
    } else {
      return null;
    }
    Map<Integer, Map<Integer, String>> mp = this.getAllSubSets(newKwString);
    return mp;
  }

  @Override
  public void classifyByKeywords(Map<Integer, Map<Integer, String>> mp, String approveCode, Integer language) {
    if (mp == null || mp.size() == 0) {
      return;
    }
    // 只取长度为5到1的关键词组合
    List<String> md5List = new ArrayList<String>();
    for (int i = 5; i >= 0; i--) {
      Map<Integer, String> itSub = mp.get(i);
      if (itSub == null || itSub.size() == 0) {
        continue;
      }
      Set<Entry<Integer, String>> itSubEntrySet = itSub.entrySet();
      for (Entry<Integer, String> itSubEntry : itSubEntrySet) {
        String kwStr = itSubEntry.getValue();
        if (StringUtils.isEmpty(kwStr)) {
          continue;
        }
        String kwContentHd5 = MD5Util.string2MD5(kwStr);
        md5List.add(kwContentHd5);
      }
    }
    // 返回已经排序完成的discode 最多只读前3个
    List<String> discodeList = this.pubKeywordsSubsetsCotfDao.getPubKeywordsSubsetsCotfByContentMd5(md5List);
    if (discodeList != null && discodeList.size() > 3) {
      discodeList = discodeList.stream().limit(3).collect(Collectors.toList());
    }
    this.saveClassificationRs(discodeList, approveCode, language);
  }

  @Override
  public void pubClassifyByKeywords(Map<Integer, Map<Integer, String>> mp, String approveCode, Integer language) {
    if (mp == null || mp.size() == 0) {
      return;
    }
    // 只取长度为5到1的关键词组合
    List<String> md5List = new ArrayList<String>();
    for (int i = 5; i >= 0; i--) {
      Map<Integer, String> itSub = mp.get(i);
      if (itSub == null || itSub.size() == 0) {
        continue;
      }
      Set<Entry<Integer, String>> itSubEntrySet = itSub.entrySet();
      for (Entry<Integer, String> itSubEntry : itSubEntrySet) {
        String kwStr = itSubEntry.getValue();
        if (StringUtils.isEmpty(kwStr)) {
          continue;
        }
        String kwContentHd5 = MD5Util.string2MD5(kwStr);
        md5List.add(kwContentHd5);
      }
    }
    // 返回已经排序完成的discode 最多只读前3个
    List<String> discodeList = this.pubKeywordsSubsetsCotfDao.getPubKeywordsSubsetsCotfByContentMd5(md5List);
    if (discodeList != null && discodeList.size() > 3) {
      discodeList = discodeList.stream().limit(3).collect(Collectors.toList());
    }
    this.savePubClassificationRs(discodeList, approveCode, language);
  }

  public static void main(String[] args) {
    List<String> list = new ArrayList<String>();
    list.add("1");
    list.add("2");
    list.add("3");
    List<String> list2 = list.stream().limit(2).collect(Collectors.toList());
    System.out.println(list2);
  }

  private void saveClassificationRs(List<String> discodeList, String approveCode, Integer language) {
    if (StringUtils.isEmpty(approveCode)) {
      return;
    }
    PatentCategpruNsfc pcn;
    if (CollectionUtils.isEmpty(discodeList)) {
      pcn = new PatentCategpruNsfc(approveCode, "0", 0, language, 2);
      patentCategpruNsfcDao.save(pcn);
    }
    int i = 1;
    for (String discode : discodeList) {
      if (StringUtils.isEmpty(discode)) {
        continue;
      }
      pcn = new PatentCategpruNsfc(approveCode, discode, i, language, 2);
      patentCategpruNsfcDao.save(pcn);
      i++;
    }
  }

  private void savePubClassificationRs(List<String> discodeList, String approveCode, Integer language) {
    if (StringUtils.isEmpty(approveCode)) {
      return;
    }
    PubCategpruNsfc pcn;
    if (CollectionUtils.isEmpty(discodeList)) {
      pcn = new PubCategpruNsfc(approveCode, "0", 0, language, 2);
      pubCategpruNsfcDao.save(pcn);
    }
    int i = 1;
    for (String discode : discodeList) {
      if (StringUtils.isEmpty(discode)) {
        continue;
      }
      pcn = new PubCategpruNsfc(approveCode, discode, i, language, 2);
      pubCategpruNsfcDao.save(pcn);
      i++;
    }
  }

  @Override
  public void saveAllSubsets(Map<Integer, Map<Integer, String>> mp, String disCode, String id) {
    if (mp == null || mp.size() == 0) {
      return;
    }
    Set<Entry<Integer, Map<Integer, String>>> st = mp.entrySet();
    for (Entry<Integer, Map<Integer, String>> en : st) {
      Integer size = en.getKey();
      Map<Integer, String> itSub = (Map<Integer, String>) en.getValue();
      if (itSub == null || itSub.size() == 0) {
        continue;
      }
      Set<Entry<Integer, String>> itSubEntrySet = itSub.entrySet();
      for (Entry<Integer, String> itSubEntry : itSubEntrySet) {
        String kwContent = itSubEntry.getValue();
        if (StringUtils.isEmpty(kwContent)) {
          continue;
        }
        if (kwContent.length() > 500) {
          kwContent = kwContent.substring(0, 500);
        }
        if (disCode.length() > 3) {
          disCode = disCode.substring(0, 3);
        }
        String kwContentHd5 = MD5Util.string2MD5(kwContent);
        PubKeywordsSubsets pks = new PubKeywordsSubsets();
        pks.setContent(kwContent);
        pks.setContentMd5Code(kwContentHd5);
        pks.setSize(size);
        pks.setDiscode(disCode);
        pks.setBranchMarkId(id);
        // 来源nsfc项目
        pks.setOrigin(1);
        if (XmlUtil.containZhChar(kwContent)) {
          // 中文
          pks.setLanguage(1);
        } else {
          pks.setLanguage(2);
        }
        this.pubKeywordsSubsetsDao.save(pks);
      }
    }
  }

  private Map<Integer, Map<Integer, String>> getAllSubSets(String[] kws) {
    if (kws == null || kws.length == 0) {
      return null;
    }
    Integer length = (2 << kws.length - 1) - 1;
    Map<Integer, Map<Integer, String>> rsMap = new HashMap<Integer, Map<Integer, String>>();
    for (int i = 1; i <= length; i++) {
      int start = i;
      TreeSet<String> subset = new TreeSet<String>();
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
      Map<Integer, String> subMap = rsMap.get(subset.size());
      if (subMap != null && subMap.size() != 0) {
        subMap.put(i, subsetStr);
      } else {
        subMap = new HashMap<Integer, String>();
        subMap.put(i, subsetStr);
      }
      rsMap.put(subset.size(), subMap);
    }
    return rsMap;
  }

  @Override
  public StringBuilder conbinePubKeywords(String applicationCode, Set<String> keywordsSet, StringBuilder strBuilder) {
    if (keywordsSet != null && keywordsSet.size() > 0) {
      List<String> zhkeywordsList = new ArrayList<>(keywordsSet);
      for (int i = 0; i < zhkeywordsList.size() - 1; i++) {
        strBuilder.append(MessageDigestUtils.messageDigest(applicationCode + zhkeywordsList.get(i)));
        strBuilder.append(" ");
        for (int j = i + 1; j < zhkeywordsList.size(); j++) {
          strBuilder.append(
              MessageDigestUtils.messageDigest(applicationCode + zhkeywordsList.get(i) + zhkeywordsList.get(j)));
          strBuilder.append(" ");
        }
      }
      strBuilder.append(
          MessageDigestUtils.messageDigest(applicationCode + zhkeywordsList.get(zhkeywordsList.size() - 1).toString()));
      strBuilder.append(" ");
    }
    return strBuilder;
  }

  @Override
  public void saveKeywordsHighAndNewTech(KeywordsHighAndNewTech keywordsHighAndNewTech) {
    this.keywordsHighAndNewTechDao.save(keywordsHighAndNewTech);
  }

  @Override
  public List<String> getKwStrsByCategory(String category) {
    return tmpDestDao.finListByCateGory(category);
    // return
    // this.keywordsHighAndNewTechDao.getAllKwStrByCategory(category);
  }

  @Override
  public List<String> getKwStrs() {
    return this.keywordsHighAndNewTechDao.getAllKwStr();
  }

  @Override
  public List<String> getNsfcKwByLanguage(Integer language) {
    return this.pubKeywordsSubsetsDao.getNsfcKwsByLanguage(language);
  }

  @Override
  public List<String> getKwStrsByCategoryByLanguage(String category, Integer language) {
    return this.keywordsHighAndNewTechDao.getAllKwStrByCategoryByLanguage(category, language);
  }

  @Override
  public Map<Integer, Map<Integer, String>> getKwSubsetsFromPubContent(String kws) {
    if (!XmlUtil.containZhChar(kws)) {
      kws = kws.toLowerCase().replaceAll("\\s+", "空格");
    }
    Result kwRs = DicAnalysis.parse(kws);
    Set<String> kwStrings = new TreeSet<String>();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }
      /*
       * if ("userDefine".equals(t.getNatureStr()) || "userdefine".equals(t.getNatureStr()) ||
       * "userDefined".equals(t.getNatureStr())) {
       */
      if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
        if (StringUtils.isNotEmpty(t.getName())) {
          kwStrings.add(t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim());
        }
      }
    }
    String[] newKwString;
    if (kwStrings.size() > 0) {
      kwStrings = filterKw(kwStrings);
      newKwString = kwStrings.toArray(new String[kwStrings.size()]);
      // 重新排序，保证生成的关键词组合之中关键词顺序一致，方便最后统一计算co-tf
      Arrays.sort(newKwString);
    } else {
      return null;
    }
    Map<Integer, Map<Integer, String>> mp = this.getAllSubSets(newKwString);
    return mp;
  }

  @Override
  public Map<Integer, Map<Integer, String>> getKwSubsetsFromPubContentNsfc(String kws) {
    if (!XmlUtil.containZhChar(kws)) {
      kws = kws.toLowerCase().replaceAll("\\s+", "空格");
    }
    Result kwRs = DicAnalysis.parse(kws);
    Set<String> kwStrings = new TreeSet<String>();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }
      /*
       * if ("userDefine".equals(t.getNatureStr()) || "userdefine".equals(t.getNatureStr()) ||
       * "userDefined".equals(t.getNatureStr())) {
       */
      if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
        if (StringUtils.isNotEmpty(t.getName())) {
          kwStrings.add(t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim());
        }
      }
    }
    String[] newKwString;
    if (kwStrings.size() > 0) {
      kwStrings = filterKw(kwStrings);
      newKwString = kwStrings.toArray(new String[kwStrings.size()]);
      // 重新排序，保证生成的关键词组合之中关键词顺序一致，方便最后统一计算co-tf
      Arrays.sort(newKwString);
    } else {
      return null;
    }
    Map<Integer, Map<Integer, String>> mp = this.getAllSubSets(newKwString);
    return mp;
  }

  @Override
  public String[] getAllNsfcKws(String kws) {
    if (!XmlUtil.containZhChar(kws)) {
      kws = kws.toLowerCase().replaceAll("\\s+", "空格");
    }
    Result kwRs = DicAnalysis.parse(kws);
    Set<String> kwStrings = new TreeSet<String>();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }
      if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
        if (StringUtils.isNotEmpty(t.getName())) {
          kwStrings.add(t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim());
        }
      }
    }
    String[] newKwString;
    if (kwStrings.size() > 0) {
      kwStrings = filterKwNew(kwStrings);
      newKwString = kwStrings.toArray(new String[kwStrings.size()]);
      // 重新排序，保证生成的关键词组合之中关键词顺序一致，方便最后统一计算co-tf
      Arrays.sort(newKwString);
    } else {
      return null;
    }
    return newKwString;
  }

  private Set<String> filterKw(Set<String> newSet) {
    if (newSet.size() <= 20) {
      return newSet;
    }
    Set<String> subSetsShort = new TreeSet<String>();
    Set<String> subSetsLong = new TreeSet<String>();
    if (newSet.size() > 20) {
      for (String str : newSet) {
        if (StringUtils.isNotEmpty(str)) {
          if (str.length() > 2) {
            subSetsLong.add(str);
            if (subSetsLong.size() >= 20) {
              return subSetsLong;
            }
          } else {
            subSetsShort.add(str);
          }
        }
      }
    }

    if (subSetsLong.size() < 20) {
      int i = 0;
      for (String str : subSetsShort) {
        if (i <= 20 - subSetsLong.size()) {
          subSetsLong.add(str);
          i++;
        } else {
          break;
        }
      }
    }
    newSet = null;
    subSetsShort = null;
    return subSetsLong;
  }

  private Set<String> filterKwNew(Set<String> newSet) {
    if (newSet.size() <= 30) {
      return newSet;
    }
    Set<String> subSetsShort = new TreeSet<String>();
    Set<String> subSetsLong = new TreeSet<String>();
    if (newSet.size() > 30) {
      for (String str : newSet) {
        if (StringUtils.isNotEmpty(str)) {
          if (str.length() > 2) {
            subSetsLong.add(str);
            if (subSetsLong.size() >= 30) {
              return subSetsLong;
            }
          } else {
            subSetsShort.add(str);
          }
        }
      }
    }

    if (subSetsLong.size() < 30) {
      int i = 0;
      for (String str : subSetsShort) {
        if (i <= 30 - subSetsLong.size()) {
          subSetsLong.add(str);
          i++;
        } else {
          break;
        }
      }
    }
    newSet = null;
    subSetsShort = null;
    return subSetsLong;
  }

  @Override
  public void saveAllSubsetsHnt(Map<Integer, Map<Integer, String>> mp, String disCode, String id) {
    if (mp == null || mp.size() == 0) {
      return;
    }
    Set<Entry<Integer, Map<Integer, String>>> st = mp.entrySet();
    for (Entry<Integer, Map<Integer, String>> en : st) {
      Integer size = en.getKey();
      Map<Integer, String> itSub = (Map<Integer, String>) en.getValue();
      if (itSub == null || itSub.size() == 0) {
        continue;
      }
      Set<Entry<Integer, String>> itSubEntrySet = itSub.entrySet();
      for (Entry<Integer, String> itSubEntry : itSubEntrySet) {
        String kwContent = itSubEntry.getValue();
        if (StringUtils.isEmpty(kwContent)) {
          continue;
        }
        if (kwContent.length() > 500) {
          kwContent = kwContent.substring(0, 500);
        }
        if (disCode.length() > 3) {
          disCode = disCode.substring(0, 3);
        }
        Long hashValue = HashUtils.getStrHashCode(kwContent);
        PubKeywordsSubsetDesc pks = new PubKeywordsSubsetDesc();
        pks.setContent(kwContent);
        pks.setContentHashValue(hashValue);
        pks.setSize(size);
        pks.setDiscode(disCode);
        pks.setBranchMarkId(id);
        // 关键词来源nsfc项目的中文分词结果
        pks.setOrigin(1);
        if (XmlUtil.containZhChar(kwContent)) {
          // 中文
          pks.setLanguage(1);
        } else {
          pks.setLanguage(2);
        }
        pubKeywordsSubsetDescDao.save(pks);
      }
    }
  }

  @Override
  public void saveAllSubsetsHntNsfc(Map<Integer, Map<Integer, String>> mp, String disCode, String id) {
    if (mp == null || mp.size() == 0) {
      return;
    }
    Set<Entry<Integer, Map<Integer, String>>> st = mp.entrySet();
    for (Entry<Integer, Map<Integer, String>> en : st) {
      Integer size = en.getKey();
      Map<Integer, String> itSub = (Map<Integer, String>) en.getValue();
      if (itSub == null || itSub.size() == 0) {
        continue;
      }
      Set<Entry<Integer, String>> itSubEntrySet = itSub.entrySet();
      for (Entry<Integer, String> itSubEntry : itSubEntrySet) {
        String kwContent = itSubEntry.getValue();
        if (StringUtils.isEmpty(kwContent)) {
          continue;
        }
        if (kwContent.length() > 500) {
          kwContent = kwContent.substring(0, 500);
        }

        Long hashValue = HashUtils.getStrHashCode(kwContent);
        PubKeywordsSubsetDesc pks = new PubKeywordsSubsetDesc();
        pks.setContent(kwContent);
        pks.setContentHashValue(hashValue);
        pks.setSize(size);
        pks.setDiscode(disCode);
        pks.setBranchMarkId(id);
        // 关键词来源nsfc项目的中文分词结果
        pks.setOrigin(7);
        if (XmlUtil.containZhChar(kwContent)) {
          // 中文
          pks.setLanguage(1);
        } else {
          pks.setLanguage(2);
        }
        pubKeywordsSubsetDescDao.save(pks);
      }
    }
  }

  @Override
  public void saveRs(PubKeywordsSubsetsHntTmp pt) {
    this.pubKeywordsSubsetsHntTmpDao.updateCategory(pt);
  }

  @Override
  public List<PubKeywordsSubsetsHntTmp> getCategory(Integer status) {
    return this.pubKeywordsSubsetsHntTmpDao.getCategory(status);
  }

  @Override
  public void classifyByKeywordsHnt(Map<Integer, Map<Integer, String>> mp, String approveCode, Integer language) {
    if (mp == null || mp.size() == 0) {
      return;
    }
    // 只取长度为5到1的关键词组合
    List<Long> hashValueList = new ArrayList<Long>();
    for (int i = 5; i >= 0; i--) {
      Map<Integer, String> itSub = mp.get(i);
      if (itSub == null || itSub.size() == 0) {
        continue;
      }
      Set<Entry<Integer, String>> itSubEntrySet = itSub.entrySet();
      for (Entry<Integer, String> itSubEntry : itSubEntrySet) {
        String kwStr = itSubEntry.getValue();
        if (StringUtils.isEmpty(kwStr)) {
          continue;
        }
        Long kwContentHash = HashUtils.getStrHashCode(kwStr);
        hashValueList.add(kwContentHash);
      }
    }
    // 返回已经排序完成的discode
    List<String> discodeList =
        this.pubKeywordsSubsetsCotfHntDao.getPubKeywordsSubsetsCotfHntByContentHash(hashValueList);
    this.saveClassificationRs(discodeList, approveCode, language);
  }

  /**
   * 获取个人所属的所有成果关键词，与成果数
   */
  @Override
  public List<String> getPubContent(Long psnId) {
    List<String> pubList = new ArrayList<String>();
    List<Long> pubIdList = this.publicationDao.getPubIdListByPsnId(psnId);
    if (CollectionUtils.isNotEmpty(pubIdList)) {
      for (Long pubId : pubIdList) {
        StringBuilder sb = new StringBuilder();
        ScmPubXml scmPubXml = scmPubXmlDao.get(pubId);
        if (scmPubXml != null) {
          String pubXml = scmPubXml.getPubXml();

          if (StringUtils.isNotEmpty(pubXml)) {
            try {
              PubXmlDocument xmlDoc = new PubXmlDocument(pubXml);
              String zhAbstract =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract"));
              String zhKws =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords"));
              String zhTitle =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title"));
              if (StringUtils.isNotEmpty(zhAbstract)) {
                sb.append(zhAbstract);
              }
              if (StringUtils.isNotEmpty(zhKws)) {
                sb.append(zhKws);
              }
              if (StringUtils.isNotEmpty(zhTitle)) {
                sb.append(zhTitle);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        String contentStr = sb.toString();
        if (StringUtils.isNotEmpty(contentStr)) {
          pubList.add(contentStr);
        }
      }
    }
    return pubList;
  }

  @Override
  public Map<Long, String> getPubZhContentMap(Long psnId) {
    Map<Long, String> pubList = new HashMap<Long, String>();
    List<Long> pubIdList = this.publicationDao.getPubIdListByPsnId(psnId);
    if (CollectionUtils.isNotEmpty(pubIdList)) {
      for (Long pubId : pubIdList) {
        StringBuilder sb = new StringBuilder();
        ScmPubXml scmPubXml = scmPubXmlDao.get(pubId);
        if (scmPubXml != null) {
          String pubXml = scmPubXml.getPubXml();

          if (StringUtils.isNotEmpty(pubXml)) {
            try {
              PubXmlDocument xmlDoc = new PubXmlDocument(pubXml);
              String zhAbstract =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_abstract"));
              String zhKws =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords"));
              String zhTitle =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_title"));
              if (StringUtils.isNotEmpty(zhAbstract)) {
                sb.append(zhAbstract);
              }
              if (StringUtils.isNotEmpty(zhKws)) {
                sb.append(zhKws);
              }
              if (StringUtils.isNotEmpty(zhTitle)) {
                sb.append(zhTitle);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        String contentStr = sb.toString();
        if (StringUtils.isNotEmpty(contentStr)) {
          pubList.put(pubId, contentStr);
        }
      }
    }
    return pubList;
  }

  @Override
  public Map<Long, String> getPubEnContentMap(Long psnId) {
    Map<Long, String> pubList = new HashMap<Long, String>();
    List<Long> pubIdList = this.publicationDao.getPubIdListByPsnId(psnId);
    if (CollectionUtils.isNotEmpty(pubIdList)) {
      for (Long pubId : pubIdList) {
        StringBuilder sb = new StringBuilder();
        ScmPubXml scmPubXml = scmPubXmlDao.get(pubId);
        if (scmPubXml != null) {
          String pubXml = scmPubXml.getPubXml();

          if (StringUtils.isNotEmpty(pubXml)) {
            try {
              PubXmlDocument xmlDoc = new PubXmlDocument(pubXml);
              String zhAbstract =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_abstract"));
              String zhKws =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords"));
              String zhTitle =
                  StringUtils.trimToEmpty(xmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_title"));
              if (StringUtils.isNotEmpty(zhAbstract)) {
                sb.append(zhAbstract);
              }
              if (StringUtils.isNotEmpty(zhKws)) {
                sb.append(zhKws);
              }
              if (StringUtils.isNotEmpty(zhTitle)) {
                sb.append(zhTitle);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        String contentStr = sb.toString();
        if (StringUtils.isNotEmpty(contentStr)) {
          pubList.put(pubId, contentStr.replaceAll("\\s+", "空格").trim());
        }
      }
    }
    return pubList;
  }

  @Override
  public void saveAllSubsetsPsn(Map<Integer, Map<Integer, String>> mp, Long psnId, Integer language) {
    if (mp == null || mp.size() == 0) {
      return;
    }
    Set<Entry<Integer, Map<Integer, String>>> st = mp.entrySet();
    for (Entry<Integer, Map<Integer, String>> en : st) {
      Integer size = en.getKey();
      if (size > 5) {
        continue;
      }
      Map<Integer, String> itSub = (Map<Integer, String>) en.getValue();
      if (itSub == null || itSub.size() == 0) {
        continue;
      }
      Set<Entry<Integer, String>> itSubEntrySet = itSub.entrySet();
      for (Entry<Integer, String> itSubEntry : itSubEntrySet) {
        String kwContent = itSubEntry.getValue();
        if (StringUtils.isEmpty(kwContent)) {
          continue;
        }
        if (kwContent.length() > 500) {
          kwContent = kwContent.substring(0, 500);
        }
        Long hashValue = HashUtils.getStrHashCode(kwContent);
        PsnKeywordsSubsets pks = new PsnKeywordsSubsets();
        pks.setContent(kwContent);
        pks.setContentHashValue(hashValue);
        pks.setSize(size);
        pks.setPsnId(psnId);
        // 关键词来源nsfc项目的中文分词结果
        pks.setOrigin(1);
        if (XmlUtil.containZhChar(kwContent)) {
          // 中文
          pks.setLanguage(1);
        } else {
          pks.setLanguage(2);
        }
        this.psnKeywordsSubsetsDao.save(pks);
      }
    }
  }

  @Override
  public void saveAllSubsetsPsn(Map<Integer, Map<Integer, String>> mp, Long psnId, Long pubId, Integer origin) {
    if (mp == null || mp.size() == 0) {
      return;
    }
    Set<Entry<Integer, Map<Integer, String>>> st = mp.entrySet();
    for (Entry<Integer, Map<Integer, String>> en : st) {
      Integer size = en.getKey();
      if (size > 5) {
        continue;
      }
      Map<Integer, String> itSub = (Map<Integer, String>) en.getValue();
      if (itSub == null || itSub.size() == 0) {
        continue;
      }
      Set<Entry<Integer, String>> itSubEntrySet = itSub.entrySet();
      for (Entry<Integer, String> itSubEntry : itSubEntrySet) {
        String kwContent = itSubEntry.getValue();
        if (StringUtils.isEmpty(kwContent)) {
          continue;
        }
        if (kwContent.length() > 500) {
          kwContent = kwContent.substring(0, 500);
        }
        Long hashValue = HashUtils.getStrHashCode(kwContent);
        PsnKeywordsSubsets pks = new PsnKeywordsSubsets();
        pks.setContent(kwContent);
        pks.setContentHashValue(hashValue);
        pks.setSize(size);
        pks.setPsnId(psnId);
        pks.setPubId(pubId);
        // 关键词来源nsfc项目的中文分词结果
        pks.setOrigin(origin);
        if (XmlUtil.containZhChar(kwContent)) {
          // 中文
          pks.setLanguage(1);
        } else {
          pks.setLanguage(2);
        }
        this.psnKeywordsSubsetsDao.save(pks);
      }
    }
  }

  @Override
  public String findPatenXml(Long pubId) {
    PdwhPubXml xml = pdwhPubXmlDao.get(pubId);
    if (xml != null && StringUtils.isNotBlank(xml.getXml())) {
      return xml.getXml();
    }
    return null;
  }

  @Override
  public void updatePatentLog(Long resId, Integer resStatus, String resMsg) {
    pdwhPublicationDao.updatePatentBet(resId, resStatus, resMsg);
  }

  @Override
  public void updatePubLog(Long resId, Integer resStatus, String resMsg) {
    pdwhPublicationDao.updatePubRs(resId, resStatus, resMsg);
  }

  @Override
  public List<String> getNsfcKwStrsDiscipline() {
    return pubKeywordsSubsetDescDao.getNsfcKwStrsDiscipline();
  }

  @Override
  public List<String> getNsfcKwStrsDiscipline(Integer language) {
    return pubKeywordsSubsetDescDao.getNsfcKwStrsDiscipline(language);
  }

  @Override
  public List<ProjectDataFiveYear> getProjectDataList(Integer size, Long id, String category) {
    return projectDataFiveYearDao.getProjectDataList(size, id, category);
  }

  @Override
  public void saveAllNsfcKw(String[] kws, String disCode, String id) {
    if (kws == null || kws.length == 0) {
      return;
    }
    String subsetStr = "";
    // 转换为字符
    for (String kw : kws) {
      if (StringUtils.isEmpty(kw)) {
        continue;
      }
      subsetStr = subsetStr + kw + ";";
    }
    subsetStr = subsetStr.substring(0, subsetStr.length() - 1);
    if (subsetStr.length() > 500) {
      subsetStr = subsetStr.substring(0, 500);
      subsetStr = subsetStr.substring(0, subsetStr.lastIndexOf(";"));
    }
    pubKeywordsSubsetDescDao.saveNsfcKwFromPrj(id, subsetStr, disCode, kws.length);;
  }

  @Override
  public List<BigDecimal> getTohandlePdwhPubJournal() {
    return this.pubKeywordsSubsetDescDao.getTohandlePdwhPubJournal();
  }

  @Override
  public void updateTohandlePdwhPubJournal(Integer status, Long pubId) {
    this.pubKeywordsSubsetDescDao.updateTohandlePdwhPubJournal(status, pubId);
  }

  @Override
  public void updateTohandlePdwhPubJournalJnlId(Long jnlId, Long pubId) {
    this.pubKeywordsSubsetDescDao.updateTohandlePdwhPubJournalJnlId(jnlId, pubId);
  }

  @Override
  public String getXmlStr(Long pubId) {
    return this.pdwhPubXmlDao.getXmlStringByPubId(pubId);
  }

  @Override
  public Integer saveKwStrToSolr(SolrServer server, String content, String disCode, String id) throws Exception {
    if (!XmlUtil.containZhChar(content)) {
      content = content.toLowerCase().replaceAll("\\s+", "空格");
    }
    Result kwRs = DicAnalysis.parse(content);
    Set<String> kwStrings = new TreeSet<String>();
    for (Term t : kwRs.getTerms()) {
      if (t == null) {
        continue;
      }
      if ("nsfc_kw_discipline".equals(t.getNatureStr())) {
        if (StringUtils.isNotEmpty(t.getName())) {
          kwStrings.add(t.getName().replaceAll("空格", " ").replaceAll("\\s+", " ").trim());
        }
      }
    }
    String[] newKwString;
    if (kwStrings.size() > 0) {
      newKwString = kwStrings.toArray(new String[kwStrings.size()]);
      // 重新排序，保证生成的关键词组合之中关键词顺序一致，方便最后统一计算co-tf
      Arrays.sort(newKwString);
    } else {
      return 2;
    }

    this.saveAllNsfcKwToSolrForTf(server, newKwString, disCode, id);
    this.saveKwCoTfToSolr(server, newKwString, disCode, id);
    return 1;
  }


  public void saveAllNsfcKwToSolrForTf(SolrServer server, String[] kws, String disCode, String id) throws Exception {
    if (kws == null || kws.length == 0) {
      return;
    }
    String subsetStr = "";
    // 转换为字符
    for (String kw : kws) {
      if (StringUtils.isEmpty(kw)) {
        continue;
      }
      subsetStr = subsetStr + kw + ";";
    }
    subsetStr = subsetStr.substring(0, subsetStr.length() - 1);
    solrIndexDifService.saveKwStrForTf(server, subsetStr, disCode, id);
  }

  private void saveKwCoTfToSolr(SolrServer server, String[] kws, String disCode, String id) throws Exception {
    // List<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
    int k = 1;
    for (int i = 0; i < kws.length - 1; i++) {
      for (int j = i + 1; j < kws.length; j++) {
        solrIndexDifService.saveKwStrForCoTf(server, kws[i], kws[j], disCode, id, k);
        k++;
        // docList.add(doc);
      }
    }
    // solrIndexDifService.saveKwStrForCoTf(server, docList);
  }
}
