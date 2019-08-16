package com.smate.center.batch.service.pdwh.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.batch.dao.pdwh.pub.CrossrefOtherInfoDao;
import com.smate.center.batch.dao.pdwh.pub.OriginalPdwhPubRelationDao;
import com.smate.center.batch.dao.pdwh.pub.PdwhPubDuplicateDAO;
import com.smate.center.batch.dao.pdwh.pub.PubCategoryCrossrefDao;
import com.smate.center.batch.dao.pdwh.pub.PubOriginalDataDAO;
import com.smate.center.batch.dao.pdwh.pub.PubReferenceDao;
import com.smate.center.batch.dao.sns.pub.ConstRegionDao;
import com.smate.center.batch.model.pdwh.pub.CrossrefOtherInfo;
import com.smate.center.batch.model.pdwh.pub.OriginalPdwhPubRelation;
import com.smate.center.batch.model.pdwh.pub.PubCategoryCrossref;
import com.smate.center.batch.model.pdwh.pub.PubReference;
import com.smate.center.batch.model.sns.pub.ConstRegion;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.enums.PubBookTypeEnum;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;
import com.smate.core.base.pub.enums.PubThesisDegreeEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.xss.XssUtils;
import com.smate.web.v8pub.dom.pdwh.PubOriginalDataDOM;
import com.smate.web.v8pub.dto.BookInfoDTO;
import com.smate.web.v8pub.dto.ConferencePaperDTO;
import com.smate.web.v8pub.dto.JournalInfoDTO;
import com.smate.web.v8pub.dto.MemberInsDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.dto.ThesisInfoDTO;

@Service("savePdwhPubDataFromCrossrefService")
@Transactional(rollbackFor = Exception.class)
public class SavePdwhPubDataFromCrossrefServiceImpl implements SavePdwhPubDataFromCrossrefService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String scmDomain;
  @Autowired
  private OriginalPdwhPubRelationDao originalPdwhPubRelationDao;
  @Autowired
  private PubOriginalDataDAO pubOriginalDataDAO;
  @Autowired
  private PubCategoryCrossrefDao pubCategoryCrossrefDao;
  @Autowired
  private PubReferenceDao pubReferenceDao;
  @Autowired
  private CrossrefOtherInfoDao crossrefOtherInfoDao;
  @Autowired
  private PdwhPubDuplicateDAO pdwhPubDuplicateDAO;
  @Autowired
  private ConstRegionDao constRegionDao;


  @Override
  public void handleOriginalPubData(Long originalId) {
    PubOriginalDataDOM pubDOM = pubOriginalDataDAO.findById(originalId);
    OriginalPdwhPubRelation originalPdwhPubRelation = originalPdwhPubRelationDao.get(originalId);
    if (pubDOM == null) {
      logger.error("保存基准库成果出错----没找到对应的原始数据");
      originalPdwhPubRelation.setStatus(9);
      originalPdwhPubRelation.setErrorMsg("在V_PUB_ORIGINAL_DATA中没找到对应的原始数据");
    } else {
      try {
        Map<String, Object> DataMap = JacksonUtils.jsonToMap(pubDOM.getPubData());
        String type =
            "report,peer-review,reference-entry,component,report-series,standard,posted-content,dataset,journal-issue,journal-volume,journal,proceedings-series,book-set,book-series";// 这些数据暂时不处理
        if (type.contains(String.valueOf(DataMap.get("type")))) {
          originalPdwhPubRelation.setStatus(9);
          originalPdwhPubRelation.setUpdateDate(new Date());
          originalPdwhPubRelation.setErrorMsg("crossref原始数据type对不上 ,不处理");
        } else {
          Map<String, Object> resultMap = savePdwhPub(DataMap, originalPdwhPubRelation.getInsId(),
              originalPdwhPubRelation.getPsnId(), originalPdwhPubRelation.getRecordFrom());
          if (resultMap == null) {
            originalPdwhPubRelation.setStatus(9);
            originalPdwhPubRelation.setUpdateDate(new Date());
            originalPdwhPubRelation.setErrorMsg("crossref原始数据title为空 ,不处理");
          } else {
            String result = (String) resultMap.get("result");
            String status = JacksonUtils.jsonToMap(result).get("status").toString();
            if ("SUCCESS".equals(status)) {
              String des3pubId = JacksonUtils.jsonToMap(result).get("de3PubId").toString();
              originalPdwhPubRelation.setPdwhPubId(Long.valueOf(Des3Utils.decodeFromDes3(des3pubId)));
              // 处理需要另外存储的数据
              // 1.备份单条数据到mogodb
              Long pubId = Long.valueOf(Des3Utils.decodeFromDes3(des3pubId));
              // 2.保存分类数据
              if (DataMap.get("subject") != null) {
                List<String> subject = (List<String>) DataMap.get("subject");
                this.savePubCategory(pubId, subject);
              }
              // 3.保存参考文献
              if (DataMap.get("reference") != null) {
                List<Map<Object, Object>> references = (List<Map<Object, Object>>) DataMap.get("reference");
                this.saveReferences(pubId, references);
              }
              // 保存其他信息
              Map<String, Object> map = (Map<String, Object>) resultMap.get("pubData");
              this.saveOtherInfo(map, pubId);
              originalPdwhPubRelation.setStatus(1);
              originalPdwhPubRelation.setUpdateDate(new Date());
            } else {
              originalPdwhPubRelation.setStatus(9);
              originalPdwhPubRelation.setErrorMsg("保存基准库接口出错");
              originalPdwhPubRelation.setUpdateDate(new Date());
            }
          }
        }
      } catch (Exception e) {
        originalPdwhPubRelation.setStatus(9);
        originalPdwhPubRelation.setErrorMsg(e.getMessage());
        originalPdwhPubRelation.setUpdateDate(new Date());
      }
    }
    originalPdwhPubRelationDao.saveOrUpdate(originalPdwhPubRelation);

  }

  public Map<String, Object> savePdwhPub(Map<String, Object> DataMap, Long insId, Long psnId, Integer recordFrom) {
    Map<String, Object> map = new HashMap<String, Object>();
    // 调用接口将xml转成json

    if ((List<String>) DataMap.get("title") == null) {
      return null;
    }
    map = this.CrossrefToJsonData(DataMap);
    if (StringUtils.isBlank(map.get("title").toString())) {
      return null;
    }
    // 先调用查重接口
    String dupResult = this.getPubDupucheckStatus(map);
    Map dupResultMap = JacksonUtils.jsonToMap(dupResult);
    String result = "";
    if ("SUCCESS".equals(dupResultMap.get("status").toString())) {
      if (dupResultMap.get("msg") != null) {// 调用更新基准库成果的接口
        Long pdwhPubId = Long.valueOf(dupResultMap.get("msg").toString());
        result = this.updatePdwhPub(map, pdwhPubId, insId, psnId);
      } else {// 调用接口保存基准库成果
        result = this.saveNewPdwhPub(map, insId, psnId);
      }
    }
    Map<String, Object> resultMap = new HashMap<String, Object>();
    resultMap.put("result", StringUtils.isNotBlank(result) ? result : dupResult);
    resultMap.put("pubData", map);
    return resultMap;
  }


  private void savePubCategory(Long pubId, List<String> subject) {
    List<String> subjects = new ArrayList<String>();
    for (String category : subject) {
      PubCategoryCrossref pubCategory = pubCategoryCrossrefDao.getCategory(pubId, category.trim());
      if (pubCategory == null) {
        if (subjects.contains(category)) {
          continue;
        }
        subjects.add(category);
        pubCategory = new PubCategoryCrossref(pubId, category.trim());
        pubCategoryCrossrefDao.save(pubCategory);
      }
    }
  }

  private void saveReferences(Long pubId, List<Map<Object, Object>> references) {
    List<String> keyString = new ArrayList<String>();
    for (Map<Object, Object> referenceMap : references) {
      String DOI = null;
      String key = String.valueOf(referenceMap.get("key")) == "null" ? "" : String.valueOf(referenceMap.get("key"));
      if (StringUtils.isBlank(key)) {
        continue;
      }
      PubReference pubReference = pubReferenceDao.getReference(pubId, key);
      if (pubReference == null) {
        pubReference = new PubReference(pubId, key);
        if (keyString.contains(key)) {
          continue;
        }
        keyString.add(key);
      }
      if (referenceMap.get("DOI") != null) {
        DOI = String.valueOf(referenceMap.get("DOI")) == "null" ? "" : String.valueOf(referenceMap.get("DOI"));
        pubReference.setDoi(DOI);
      }
      if (referenceMap.get("issue") != null) {
        pubReference.setIssue(
            String.valueOf(referenceMap.get("issue")) == "null" ? "" : String.valueOf(referenceMap.get("issue")));
      }
      if (referenceMap.get("first-page") != null) {
        pubReference.setFirstPage(String.valueOf(referenceMap.get("first-page")) == "null" ? ""
            : String.valueOf(referenceMap.get("first-page")));
      }
      if (referenceMap.get("volume") != null) {
        pubReference.setVolume(
            String.valueOf(referenceMap.get("volume")) == "null" ? "" : String.valueOf(referenceMap.get("volume")));
      }
      if (referenceMap.get("edition") != null) {
        pubReference.setEdition(
            String.valueOf(referenceMap.get("edition")) == "null" ? "" : String.valueOf(referenceMap.get("edition")));
      }
      if (referenceMap.get("component") != null) {
        pubReference.setComponent(String.valueOf(referenceMap.get("component")) == "null" ? ""
            : String.valueOf(referenceMap.get("component")));
      }
      if (referenceMap.get("author") != null) {
        pubReference.setAuthor(
            String.valueOf(referenceMap.get("author")) == "null" ? "" : String.valueOf(referenceMap.get("author")));
      }
      if (referenceMap.get("year") != null) {
        pubReference.setYear(
            String.valueOf(referenceMap.get("year")) == "null" ? "" : String.valueOf(referenceMap.get("year")));
      }
      if (referenceMap.get("unstructured") != null) {
        pubReference.setUnstructured(String.valueOf(referenceMap.get("unstructured")) == "null" ? ""
            : String.valueOf(referenceMap.get("unstructured")));
      }
      String journalTitle = null;
      if (referenceMap.get("journal-title") != null) {
        journalTitle = String.valueOf(referenceMap.get("journal-title")) == "null" ? ""
            : String.valueOf(referenceMap.get("journal-title"));
        pubReference.setJournalTitle(journalTitle);
      }
      String articleTitle = null;
      if (referenceMap.get("article-title") != null) {
        articleTitle = String.valueOf(referenceMap.get("article-title")) == "null" ? ""
            : String.valueOf(referenceMap.get("article-title"));
        pubReference.setArticleTitle(articleTitle);
      }
      String seriesTitle = null;
      if (referenceMap.get("series-title") != null) {
        seriesTitle = String.valueOf(referenceMap.get("series-title")) == "null" ? ""
            : String.valueOf(referenceMap.get("series-title"));
        pubReference.setSeriesTitle(seriesTitle);
      }
      String volumeTitle = null;
      if (referenceMap.get("volume-title") != null) {
        volumeTitle = String.valueOf(referenceMap.get("volume-title")) == "null" ? ""
            : String.valueOf(referenceMap.get("volume-title"));
        pubReference.setVolumeTitle(volumeTitle);
      }
      if (referenceMap.get("ISSN") != null) {
        pubReference.setIssn(
            String.valueOf(referenceMap.get("ISSN")) == "null" ? "" : String.valueOf(referenceMap.get("ISSN")));
      }
      if (referenceMap.get("ISBN") != null) {
        pubReference.setIsbn(
            String.valueOf(referenceMap.get("ISBN")) == "null" ? "" : String.valueOf(referenceMap.get("ISBN")));
      }
      if (referenceMap.get("standard-designator") != null) {
        pubReference.setStandardDesignator(String.valueOf(referenceMap.get("standard-designator")) == "null" ? ""
            : String.valueOf(referenceMap.get("standard-designator")));
      }
      if (referenceMap.get("standards-body") != null) {
        pubReference.setStandardsBody(String.valueOf(referenceMap.get("standards-body")) == "null" ? ""
            : String.valueOf(referenceMap.get("standards-body")));
      }
      Long dupPubId = null;
      if (StringUtils.isNotBlank(DOI)) {
        Long doiHash = PubHashUtils.cleanDoiHash(DOI);
        Long doiCleanHash = PubHashUtils.getDoiHashRemotePun(DOI);
        pubReference.setDoiHash(String.valueOf(doiHash));
        dupPubId = pdwhPubDuplicateDAO.getPubIdByCleanDoiHash(doiHash, doiCleanHash);
      }
      if (dupPubId == null) {
        if (StringUtils.isNotBlank(journalTitle)) {
          Long journalTitleHash = PubHashUtils.cleanTitleHash(journalTitle);
          pubReference.setJournalTitleHash(String.valueOf(journalTitleHash));
          dupPubId = pdwhPubDuplicateDAO.getPubPdwhIdByTitleHash(journalTitleHash);
        }
        if (dupPubId == null) {
          if (StringUtils.isNotBlank(articleTitle)) {
            Long articleTitleHash = PubHashUtils.cleanTitleHash(articleTitle);
            pubReference.setArticleTitleHash(String.valueOf(articleTitleHash));
            dupPubId = pdwhPubDuplicateDAO.getPubPdwhIdByTitleHash(articleTitleHash);
          }
        }
        if (dupPubId == null) {
          if (StringUtils.isNotBlank(seriesTitle)) {
            Long seriesTitleHash = PubHashUtils.cleanTitleHash(seriesTitle);
            pubReference.setSeriesTitleHash(String.valueOf(seriesTitleHash));
            dupPubId = pdwhPubDuplicateDAO.getPubPdwhIdByTitleHash(seriesTitleHash);
          }
        }
        if (dupPubId == null) {
          if (StringUtils.isNotBlank(volumeTitle)) {
            Long volumeTitleHash = PubHashUtils.cleanTitleHash(volumeTitle);
            pubReference.setVolumeTitleHash(String.valueOf(volumeTitleHash));
            dupPubId = pdwhPubDuplicateDAO.getPubPdwhIdByTitleHash(volumeTitleHash);
          }
        }
      }
      pubReference.setPdwhPubId(dupPubId);
      pubReferenceDao.saveOrUpdate(pubReference);
    }

  }

  private void saveOtherInfo(Map<String, Object> dataMap, Long pubId) {
    CrossrefOtherInfo otherInfo = crossrefOtherInfoDao.getInfo(pubId);
    if (otherInfo == null) {
      otherInfo = new CrossrefOtherInfo(pubId);
    }
    otherInfo.setCrossrefMemberId(Long.valueOf(dataMap.get("crossref_member_id").toString()));
    otherInfo.setFulltextUrls(String.valueOf(dataMap.get("fulltextUrls")));
    otherInfo.setDepositedDate(String.valueOf(dataMap.get("deposited")));
    if (dataMap.get("eissn") != null) {
      otherInfo.setEissn(String.valueOf(dataMap.get("eissn")));
    }
    crossrefOtherInfoDao.saveOrUpdate(otherInfo);
  }

  @SuppressWarnings("unchecked")
  private String getPubDupucheckStatus(Map map) {
    Map<String, Object> dupMap = new HashMap<String, Object>();
    dupMap.put("pubGener", 3);// 成果大类别，1代表个人成果 2代表群组成果 3代表基准库成果
    dupMap.put("title", map.get("title"));
    dupMap.put("pubYear", map.get("pubYear"));
    Integer pubType = (Integer) map.get("pubType");
    dupMap.put("pubType", pubType);
    dupMap.put("doi", map.get("doi"));
    dupMap.put("srcDbId", map.get("srcDbId"));
    dupMap.put("sourceId", map.get("sourceId"));
    if (pubType != null && pubType == 5) {
      Map<String, Object> typeInfo = (Map<String, Object>) map.get("pubTypeInfo");
      if (typeInfo != null) {
        dupMap.put("applicationNo", typeInfo.get("applicationNo"));
        dupMap.put("publicationOpenNo", typeInfo.get("publicationOpenNo"));
      }
    }
    if (pubType != null && pubType == 12) {
      Map<String, Object> typeInfo = (Map<String, Object>) map.get("pubTypeInfo");
      if (typeInfo != null) {
        dupMap.put("standardNo", typeInfo.get("standardNo"));
      }
    }
    if (pubType != null && pubType == 13) {
      Map<String, Object> typeInfo = (Map<String, Object>) map.get("pubTypeInfo");
      if (typeInfo != null) {
        dupMap.put("registerNo", typeInfo.get("registerNo"));
      }
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dupMap), headers);
    String dupUrl = scmDomain + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String result = restTemplate.postForObject(dupUrl, entity, String.class);
    return result;
  }

  private String saveNewPdwhPub(Map<String, Object> map, Long insId, Long PsnId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    String url = scmDomain + V8pubQueryPubConst.PUBHANDLER_URL;
    map.put("pubHandlerName", "savePdwhPubHandler");
    map.put("recordFrom", 3);
    map.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(PsnId)));
    map.put("insId", insId);
    String dataJson = JacksonUtils.mapToJsonStr(map);
    // 进行xss的过滤
    dataJson = XssUtils.transferJson(dataJson);
    // 先对json数据进行多个空格的替换
    dataJson = dataJson.replaceAll("\\s+", " ");
    HttpEntity<String> entity1 = new HttpEntity<String>(dataJson, headers);
    return restTemplate.postForObject(url, entity1, String.class);
  }

  private String updatePdwhPub(Map<String, Object> map, Long pdwhPubId, Long insId, Long PsnId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    String url = scmDomain + V8pubQueryPubConst.PUBHANDLER_URL;
    map.put("pubHandlerName", "updatePdwhPubHandler");
    map.put("recordFrom", 3);
    map.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(2L)));
    map.put("insId", insId);
    map.put("des3PubId", Des3Utils.encodeToDes3(pdwhPubId.toString()));
    String dataJson = JacksonUtils.mapToJsonStr(map);
    // 进行xss的过滤
    dataJson = XssUtils.transferJson(dataJson);
    // 先对json数据进行多个空格的替换
    dataJson = dataJson.replaceAll("\\s+", " ");
    HttpEntity<String> entity1 = new HttpEntity<String>(dataJson, headers);
    return restTemplate.postForObject(url, entity1, String.class);
  }

  private Map<String, Object> CrossrefToJsonData(Map<String, Object> dataMap) {
    Map<String, Object> result = new HashMap<String, Object>();

    // 成果基本信息
    dealPubBase(dataMap, result);
    // 成员信息
    dealPubMembers(dataMap, result);
    // 不同类型对应的额外信息
    buildPubTypeInfo(dataMap, result);
    // 城市id 构造
    buildCountryId(result);
    List<PubSituationDTO> situations = new ArrayList<>();
    result.put("situations", situations);
    return result;
  }

  private void buildCountryId(Map<String, Object> map) {
    Long countryId = null;
    List<ConstRegion> regionList = constRegionDao.findAll();
    String country = (String) map.get("country");
    String city = (String) map.get("city");
    if (StringUtils.isNotEmpty(country)) {
      countryId = matchCountryId(regionList, country);
      if (countryId == null && StringUtils.isNotEmpty(country)) {
        countryId = matchCountryId(regionList, city);
      }
    }
    map.remove("city");
    map.remove("country");
    map.put("countryId", countryId);
  }

  @SuppressWarnings("unchecked")
  private void dealPubBase(Map<String, Object> pubData, Map<String, Object> result) {
    // 标题
    List<String> titleList = (List<String>) pubData.get("title");
    result.put("title", StringUtils.trimToEmpty(titleList.get(0)));
    // 摘要
    /*
     * if (pubData.get("abstract") != null) { result.put("summary", pubData.get("abstract").toString());
     * } else { result.put("summary", ""); }
     */
    result.put("summary", pubData.get("abstract"));
    // 关键词
    result.put("keywords", "");
    result.put("country", "");
    result.put("city", "");
    // doi
    if (pubData.get("DOI") != null) {
      result.put("doi", pubData.get("DOI").toString());
    } else {
      result.put("doi", "");
    }
    // 基金标注
    List<Map<Object, Object>> funderList = (List<Map<Object, Object>>) pubData.get("funder");
    StringBuilder fundInfo = new StringBuilder();
    String fundInfoString = "";
    if (funderList != null) {
      for (Map<Object, Object> funderMap : funderList) {
        fundInfo.append(funderMap.get("name"));
        List<String> awards = (List<String>) funderMap.get("award");
        if (awards != null && awards.size() > 0) {
          if (StringUtils.isNotBlank(awards.get(0))) {
            fundInfo.append(awards.toString());
          }
        }
        fundInfo.append(";");
      }
      fundInfoString =
          StringUtils.trimToEmpty(StringUtils.defaultString(fundInfo.toString().substring(0, fundInfo.length() - 1)));
    }
    result.put("fundInfo", fundInfoString);

    StringBuilder fulltextUrls = new StringBuilder();
    String srcFulltextUrl = "";
    List<Map<Object, Object>> linkList = (List<Map<Object, Object>>) pubData.get("link");
    if (linkList != null) {
      for (Map<Object, Object> linkMap : linkList) {
        if (linkMap.get("URL") != null) {
          if ("similarity-checking".equals(linkMap.get("intended-application"))) {
            srcFulltextUrl = StringUtils.trimToEmpty(linkMap.get("URL").toString());
          }
          fulltextUrls.append(linkMap.get("URL"));
          fulltextUrls.append(";");
        }
      }
      if (StringUtils.isBlank(srcFulltextUrl)) {
        srcFulltextUrl = StringUtils.trimToEmpty(fulltextUrls.toString().substring(0, fulltextUrls.indexOf(";")));
      }

    }
    // 来源全文路径
    result.put("srcFulltextUrl", srcFulltextUrl);

    result.put("fulltextUrls", fulltextUrls);

    // citedUrl
    result.put("citedUrl", "");
    // sourceUrl
    result.put("sourceUrl", "");

    // 成果类型
    String type = pubData.get("type").toString();
    String bookChapter = "book-section,book-track,book-part,book-chapter";// 书籍章节
    String book = "book,edited-book,reference-book,monograph";// 书/著作
    String journal = "journal-article";// 期刊
    String conference = "proceedings,proceedings-article";// 会议
    String thesis = "dissertation";// 学位论文
    int pubType = 7;
    if (bookChapter.indexOf(type) > -1) {
      pubType = 10;
    } else if (book.indexOf(type) > -1) {
      pubType = 2;
    } else if (journal.indexOf(type) > -1) {
      pubType = 4;
    } else if (conference.indexOf(type) > -1) {
      pubType = 3;
    } else if (thesis.indexOf(type) > -1) {
      pubType = 8;
    }
    if ("book".equals(type)) {
      pubType = 2;
    }

    if ("proceedings".equals(type)) {
      pubType = 3;
    }
    result.put("pubType", pubType);
    // srcDbId,crossref
    result.put("srcDbId", 36);

    // citations 引用次数
    if (pubData.get("is-referenced-by-count") != null) {
      result.put("citations", String.valueOf(pubData.get("is-referenced-by-count")));
    } else {
      result.put("citations", 0);
    }
    // crossref中数据更新时间
    StringBuilder depositedDate = new StringBuilder();
    if (pubData.get("deposited") != null) {
      Map<Object, Object> deposited = (Map<Object, Object>) pubData.get("deposited");
      if (deposited.get("date-parts") != null) {
        List<List<Integer>> dateParts = (List<List<Integer>>) deposited.get("date-parts");
        List<Integer> date = dateParts.get(0);
        for (Integer s : date) {
          depositedDate.append(s);
          depositedDate.append("-");
        }
      }
    }
    result.put("deposited", depositedDate.substring(0, depositedDate.length() - 1));

    Map<Object, Object> datemap = new HashMap<Object, Object>();
    if (pubData.get("published-print") != null) {
      datemap = (Map<Object, Object>) pubData.get("published-print");

    } else if (pubData.get("published-online") != null) {
      datemap = (Map<Object, Object>) pubData.get("published-online");
    } else if (pubData.get("approved") != null) {// 学位论文发表时间
      datemap = (Map<Object, Object>) pubData.get("approved");
    }
    StringBuilder publishDate = new StringBuilder();
    if (datemap.get("date-parts") != null) {
      List<List<Integer>> dateParts = (List<List<Integer>>) datemap.get("date-parts");
      List<Integer> date = dateParts.get(0);
      for (Integer s : date) {
        publishDate.append(s);
        publishDate.append("-");
      }
    }
    String pubYear = "";
    // publishDate
    if (StringUtils.isNotBlank(publishDate)) {
      pubYear = publishDate.substring(0, publishDate.indexOf("-"));
      result.put("publishDate", publishDate.substring(0, publishDate.length() - 1));
    } else {
      result.put("publishDate", "");
    }
    result.put("pubYear", pubYear);
    // HCP 高被引字段
    result.put("HCP", 0);
    // HP 热门文章
    result.put("HP", 0);
  }

  @SuppressWarnings("unchecked")
  private void dealPubMembers(Map<String, Object> pubData, Map<String, Object> result) {
    List<PubMemberDTO> memberList = new ArrayList<PubMemberDTO>();
    result.put("crossref_member_id", pubData.get("member"));
    // 记录成果作者的单位信息
    Set<String> organizations = new HashSet<String>();
    // 成果作者名拼接字段
    StringBuilder authorNames = new StringBuilder();
    List<Map<String, Object>> authorList = (List<Map<String, Object>>) pubData.get("author");
    if (CollectionUtils.isNotEmpty(authorList)) {
      PubMemberDTO pubMemberDTO = null;
      Integer seqNo = 0;
      for (Map<String, Object> author : authorList) {
        if (author != null) {
          String authorName = "";
          Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
          if (author.get("given") != null
              && pattern.matcher(author.get("given").toString().trim().replace(" ", "")).matches()
              || author.get("given").toString().equals(author.get("family").toString())) {// 中文名字
            if (author.get("family").toString().length() == 1) {
              authorName = author.get("family") + "" + author.get("given");
            } else {
              if (pattern.matcher(author.get("given").toString().trim().replace(" ", "")).matches()) {
                authorName = author.get("given").toString();
              } else {
                authorName = author.get("family").toString();
              }
            }
          } else {
            // 作者名
            if (author.get("given") != null && author.get("family") != null) {
              authorName = author.get("given") + ", " + author.get("family");
            } else if (author.get("given") != null && author.get("family") == null) {
              authorName = (String) author.get("given");
            } else if (author.get("given") == null && author.get("family") != null) {
              authorName = (String) author.get("family");
            }
          }
          // 第一作者
          boolean firstAuthor = "first".equals(String.valueOf(author.get("sequence")));
          // 作者有多个部门，一个部门一个作者进行拆分，暂时按照此规则进行构建数据，后面结构改动会进行改正此部分逻辑
          List<Map<String, Object>> deptList = (List<Map<String, Object>>) author.get("affiliation");
          if (CollectionUtils.isNotEmpty(deptList)) {
            for (Map<String, Object> dept : deptList) {
              if (dept != null && dept.get("name") != null) {
                String deptName = String.valueOf(dept.get("name"));
                pubMemberDTO = new PubMemberDTO();
                pubMemberDTO.setName(authorName);
                pubMemberDTO.setSeqNo(++seqNo);
                pubMemberDTO.setDept(deptName);
                pubMemberDTO.setFirstAuthor(firstAuthor);
                // 默认值
                pubMemberDTO.setEmail("");
                pubMemberDTO.setOwner(false);
                pubMemberDTO.setCommunicable(false);
                pubMemberDTO.setInsNames(new ArrayList<MemberInsDTO>());
                memberList.add(pubMemberDTO);

                // 处理organizations信息
                organizations.add(deptName);
              }
            }
          } else {
            // 不存在一个部门信息的作者
            pubMemberDTO = new PubMemberDTO();
            pubMemberDTO.setName(authorName);
            pubMemberDTO.setSeqNo(++seqNo);
            pubMemberDTO.setDept("");
            pubMemberDTO.setFirstAuthor(firstAuthor);
            // 默认值
            pubMemberDTO.setEmail("");
            pubMemberDTO.setOwner(false);
            pubMemberDTO.setCommunicable(false);
            pubMemberDTO.setInsNames(new ArrayList<MemberInsDTO>());
            memberList.add(pubMemberDTO);
          }

          // 处理作者名信息
          authorNames.append(authorName + ";");
        }
      }
    }

    // "organization": "单位地址信息",
    if (organizations.size() > 0) {
      StringBuilder depts = new StringBuilder();
      for (String data : organizations) {
        depts.append(data);
        depts.append(".");
      }
      result.put("organization", depts.substring(0, depts.length() - 1));
    } else {
      result.put("organization", "");
    }

    // 作者
    if (StringUtils.isNotBlank(authorNames)) {
      result.put("authorNames", authorNames);
    } else {
      result.put("authorNames", "");
    }
    result.put("members", memberList);
  }

  private void buildPubTypeInfo(Map<String, Object> pubData, Map<String, Object> result) {
    int pubType = Integer.parseInt(result.get("pubType").toString());
    Map<String, Object> typeInfo = new HashMap<>();
    switch (pubType) {
      case 2:// 书/著作
      case 10:// 书籍章节
        typeInfo = dealPubBook(pubData);
        break;
      case 3:// 会议
        typeInfo = dealPubConfPaper(pubData, result);
        break;
      case 4:// 期刊
        Map<String, Object> journalInfo = dealPubJournal(pubData);
        typeInfo = JacksonUtils.json2HashMap(journalInfo.get("typeInfo").toString());
        if (journalInfo.get("eissn") != null) {
          result.put("eissn", journalInfo.get("eissn"));
        }
        break;
      case 8:// 学位
        typeInfo = dealPubThesis(pubData, result);
        break;
      case 7:
        typeInfo = dealpubOther(pubData);
        break;
      default:
        typeInfo = dealpubOther(pubData);
        break;
    }
    result.put("pubTypeInfo", typeInfo);
  }

  private Map<String, Object> dealPubThesis(Map<String, Object> pubData, Map<String, Object> result) {
    ThesisInfoDTO thesisInfoDTO = new ThesisInfoDTO();
    // 学位
    if (pubData.get("degree") != null) {
      List<String> degreeList = (List<String>) pubData.get("degree");
      String degree = degreeList.get(0);
      if (degree.contains("D")) {
        thesisInfoDTO.setDegree(PubThesisDegreeEnum.DOCTOR);
      } else if (degree.contains("M")) {
        thesisInfoDTO.setDegree(PubThesisDegreeEnum.MASTER);
      }
    } else {
      thesisInfoDTO.setDegree(PubThesisDegreeEnum.OTHER);
    }
    if (pubData.get("institution") != null) {
      Map<String, Object> DataMap = (Map<String, Object>) pubData.get("institution");
      if (DataMap.get("name") != null) {
        thesisInfoDTO.setIssuingAuthority(DataMap.get("name").toString());
      } else {
        thesisInfoDTO.setIssuingAuthority("");
      }
      if (DataMap.get("place") != null) {
        result.put("country", DataMap.get("place").toString());
      } else {
        result.put("country", "");
      }
      if (DataMap.get("department") != null) {
        List<String> deptList = (List<String>) DataMap.get("department");
        thesisInfoDTO.setDepartment(deptList.get(0));
      } else {
        thesisInfoDTO.setDepartment("");
      }
    }
    List<String> ISBNList = (List<String>) pubData.get("ISBN");
    // 书籍isbn
    if (ISBNList != null && ISBNList.size() > 0) {
      thesisInfoDTO.setISBN(ISBNList.get(0));
    } else {
      thesisInfoDTO.setISBN("");
    }
    thesisInfoDTO.setDefenseDate(result.get("publishDate").toString());
    String jsonStr = JacksonUtils.jsonObjectSerializer(thesisInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  private Map<String, Object> dealPubBook(Map<String, Object> pubData) {
    BookInfoDTO bookInfoDTO = new BookInfoDTO();
    // 书名
    if (pubData.get("container-title") != null) {
      List<String> titleList = (List<String>) pubData.get("container-title");
      bookInfoDTO.setName(titleList.get(0));
    } else {
      bookInfoDTO.setName("");
    }
    // 书籍类型 no
    String type = String.valueOf(pubData.get("type"));
    bookInfoDTO.setType(buildBookType(type));
    // 出版社
    bookInfoDTO.setPublisher(String.valueOf(pubData.get("publisher")));
    // 页数 no
    bookInfoDTO.setTotalPages(0);
    // 总字数 no
    bookInfoDTO.setTotalWords(0);
    // 丛书名
    bookInfoDTO.setSeriesName("");
    StringBuilder editors = new StringBuilder();
    List<Map<Object, Object>> editorList = (List<Map<Object, Object>>) pubData.get("editor");
    if (editorList != null) {
      for (Map<Object, Object> editorMap : editorList) {
        String authorName = editorMap.get("given") + " " + editorMap.get("family");
        editors.append(authorName);
        editors.append(";");
      }
    }
    // 书籍编辑
    if (StringUtils.isNotBlank(editors)) {
      bookInfoDTO.setEditors(editors.substring(0, editors.length() - 1));
    } else {
      bookInfoDTO.setEditors("");
    }
    // 章节号码
    bookInfoDTO.setChapterNo("");
    // 开始结束页码，文章号合并
    String pageNumber = null;
    if (pubData.get("page") != null && StringUtils.isNotBlank(String.valueOf(pubData.get("page")))) {
      pageNumber = String.valueOf(pubData.get("page"));
    }
    if (pageNumber == null && pubData.get("article-number") != null) {
      pageNumber = String.valueOf(pubData.get("article-number"));
    }

    bookInfoDTO.setPageNumber(StringUtils.trimToEmpty(pageNumber));
    List<String> ISBNList = (List<String>) pubData.get("ISBN");
    // 书籍isbn
    if (ISBNList != null && ISBNList.size() > 0) {
      bookInfoDTO.setISBN(ISBNList.get(0));
    } else {
      bookInfoDTO.setISBN("");
    }
    // 语种
    bookInfoDTO
        .setLanguage(String.valueOf(pubData.get("language")) == "null" ? "" : String.valueOf(pubData.get("language")));
    String jsonStr = JacksonUtils.jsonObjectSerializer(bookInfoDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  /**
   * crossref数据专用
   * 
   * @param type
   * @return
   */
  private PubBookTypeEnum buildBookType(String type) {

    if (StringUtils.isBlank(type)) {
      return PubBookTypeEnum.NULL;
    }
    if ("monograph".equalsIgnoreCase(type)) {
      return PubBookTypeEnum.MONOGRAPH;
    }
    return PubBookTypeEnum.NULL;
  }

  private Map<String, Object> dealPubConfPaper(Map<String, Object> pubData, Map<String, Object> result) {
    ConferencePaperDTO conferencePaperDTO = new ConferencePaperDTO();
    // 论文集名
    if (pubData.get("container-title") != null) {
      List<String> titleList = (List<String>) pubData.get("container-title");
      conferencePaperDTO.setPapers(titleList.get(0));
    } else {
      conferencePaperDTO.setPapers("");
    }
    String pageNumber = "";
    String name = "";
    String organizer = "";
    String location = "";
    StringBuilder startDate = new StringBuilder();
    StringBuilder endDate = new StringBuilder();
    // 论文类别
    String paperType = pubData.get("type").toString();
    conferencePaperDTO.setPaperType(buildPaperType(paperType));
    if (pubData.get("event") != null) {
      Map<Object, Object> event = (Map<Object, Object>) pubData.get("event");
      name = event.get("name").toString();
      if (event.get("location") != null) {
        location = event.get("location").toString();
      }
      List<String> organizers = (List<String>) event.get("sponsor");
      organizer = organizers != null && organizers.size() > 0 ? organizers.get(0) : "";
      Map<Object, Object> start = (Map<Object, Object>) event.get("start");
      Map<Object, Object> end = (Map<Object, Object>) event.get("end");
      if (start != null) {
        List<List<Integer>> startLists = (List<List<Integer>>) start.get("date-parts");
        List<Integer> startList = startLists.get(0);
        for (Integer date : startList) {
          startDate.append(date);
          startDate.append("-");
        }
      }
      if (end != null) {
        List<List<Integer>> endLists = (List<List<Integer>>) end.get("date-parts");
        List<Integer> endList = endLists.get(0);
        for (Integer date : endList) {
          endDate.append(date);
          endDate.append("-");
        }
      }
    }
    result.put("country", location);
    // 会议名称
    conferencePaperDTO.setName(name);
    // 会议组织者
    conferencePaperDTO.setOrganizer(organizer);
    // 开始日期
    if (StringUtils.isNotEmpty(startDate.toString())) {
      conferencePaperDTO.setStartDate(startDate.toString().substring(0, startDate.length() - 1));
    } else {
      conferencePaperDTO.setStartDate("");
    }
    // 结束日期
    if (StringUtils.isNotEmpty(endDate.toString())) {
      conferencePaperDTO.setEndDate(endDate.toString().substring(0, endDate.length() - 1));
    } else {
      conferencePaperDTO.setEndDate("");
    }
    // 页码
    conferencePaperDTO.setPageNumber(pageNumber);
    String jsonStr = JacksonUtils.jsonObjectSerializer(conferencePaperDTO);
    Map<String, Object> map = JacksonUtils.json2HashMap(jsonStr);
    return map;
  }

  private PubConferencePaperTypeEnum buildPaperType(String paperType) {
    return PubConferencePaperTypeEnum.NULL;
  }

  private Map<String, Object> dealPubJournal(Map<String, Object> pubData) {
    JournalInfoDTO journalInfoDTO = new JournalInfoDTO();
    // 期刊ID
    journalInfoDTO.setJid(null);
    if (pubData.get("container-title") != null) {
      List<String> titleList = (List<String>) pubData.get("container-title");
      journalInfoDTO.setName(titleList.get(0));
    } else {
      journalInfoDTO.setName("");
    }
    // 发表状态(P已发表/A已接收),默认已发表
    journalInfoDTO.setPublishStatus("P");
    // 期号
    if (pubData.get("volume") != null) {
      journalInfoDTO.setVolumeNo(pubData.get("volume").toString());
    } else {
      journalInfoDTO.setVolumeNo("");
    }
    // 卷号
    if (pubData.get("issue") != null) {
      journalInfoDTO.setIssue(pubData.get("issue").toString());
    } else {
      journalInfoDTO.setIssue("");
    }
    // 开始结束页码，文章号合并
    String pageNumber = null;
    if (pubData.get("page") != null && StringUtils.isNotBlank(pubData.get("page").toString())) {
      pageNumber = pubData.get("page").toString();
    }
    if (pageNumber == null && pubData.get("article-number") != null) {
      pageNumber = pubData.get("article-number").toString();
    }
    journalInfoDTO.setPageNumber(StringUtils.trimToEmpty(pageNumber));
    // issn
    if (pubData.get("ISSN") != null) {
      List<String> ISSNList = (List<String>) pubData.get("ISSN");
      if (ISSNList.size() > 1) {
        // 有两个issn的情况取pissn ，eissn存至crossref_other_info
        if (pubData.get("issn-type") != null) {
          List<Map<String, String>> DataList = (List<Map<String, String>>) pubData.get("issn-type");
          for (Map<String, String> map : DataList) {
            if ("print".equals(map.get("type"))) {
              journalInfoDTO.setISSN(map.get("value"));
            } else if ("electronic".equals(map.get("type"))) {
              pubData.put("eissn", map.get("value"));
            }
          }
        }
      } else if (ISSNList.size() == 1) {
        journalInfoDTO.setISSN(ISSNList.get(0));
      }
    } else {
      journalInfoDTO.setISSN("");
    }
    String jsonStr = JacksonUtils.jsonObjectSerializer(journalInfoDTO);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("typeInfo", jsonStr);
    if (pubData.get("eissn") != null) {
      map.put("eissn", pubData.get("eissn"));
    }
    return map;
  }

  private Map<String, Object> dealpubOther(Map<String, Object> pubData) {
    return null;
  }

  public Long matchCountryId(List<ConstRegion> regionList, String countryName) {
    if (StringUtils.isEmpty(countryName)) {
      return null;
    }
    List<Long> countryIds = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(regionList)) {
      for (ConstRegion constRegion : regionList) {
        // 先处理地区名，全部转为小写
        String zhRegionName = cleanCountryName(constRegion.getZhName());
        String enRegionName = cleanCountryName(constRegion.getEnName());
        countryName = cleanCountryName(countryName);
        // 匹配
        boolean isMatch = (countryName.contains(zhRegionName) && StringUtils.isNotEmpty(zhRegionName))
            || (countryName.contains(enRegionName) && StringUtils.isNotEmpty(enRegionName));
        if (isMatch) {
          countryIds.add(constRegion.getId());
        }
      }
    }
    if (CollectionUtils.isNotEmpty(countryIds)) {
      // 取最大的地区id
      Long countryId = countryIds.stream().mapToLong(regionId -> regionId).summaryStatistics().getMax();
      return countryId;
    }
    return null;
  }

  private String cleanCountryName(String countryName) {
    if (StringUtils.isEmpty(countryName)) {
      return "";
    }
    countryName = countryName.replace("市", "");
    countryName = countryName.replace("省", "");
    countryName = countryName.replace("自治区", "");
    countryName = countryName.replace("自治州", "");
    countryName = countryName.replace("地区", "");
    countryName = countryName.replace("盟", "");
    countryName = countryName.replace("林区", "");
    countryName = countryName.replace("的岛礁及其海域", "");
    countryName = countryName.toLowerCase();
    return countryName;
  }



}
