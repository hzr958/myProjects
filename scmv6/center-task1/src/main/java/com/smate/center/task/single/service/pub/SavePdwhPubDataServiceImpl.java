package com.smate.center.task.single.service.pub;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.CrossrefOtherInfoDao;
import com.smate.center.task.dao.pdwh.quartz.OriginalPdwhPubRelationDao;
import com.smate.center.task.dao.pdwh.quartz.OriginalPdwhPubRelationHisDao;
import com.smate.center.task.dao.pdwh.quartz.PubCategoryCrossrefDao;
import com.smate.center.task.dao.pdwh.quartz.PubReferenceDao;
import com.smate.center.task.model.pdwh.pub.CrossrefOtherInfo;
import com.smate.center.task.model.pdwh.pub.OriginalPdwhPubRelation;
import com.smate.center.task.model.pdwh.pub.OriginalPdwhPubRelationHis;
import com.smate.center.task.model.pdwh.pub.PubCategoryCrossref;
import com.smate.center.task.model.pdwh.pub.PubReference;
import com.smate.center.task.service.pdwh.quartz.HandlePdwhPubXmlService;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubDuplicateDAO;
import com.smate.center.task.v8pub.dao.sns.PubOriginalDataDAO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dom.pdwh.PubOriginalDataDOM;

@Service("savePdwhPubDataService")
@Transactional(rollbackFor = Exception.class)
public class SavePdwhPubDataServiceImpl implements SavePdwhPubDataService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private HandlePdwhPubXmlService handlePdwhPubXmlService;
  @Autowired
  private OriginalPdwhPubRelationDao originalPdwhPubRelationDao;
  @Autowired
  private OriginalPdwhPubRelationHisDao originalPdwhPubRelationHisDao;
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


  @Override
  public List<OriginalPdwhPubRelation> getHandleData() {
    return originalPdwhPubRelationDao.getHandleData();
  }

  @Override
  public void handleOriginalPubData(OriginalPdwhPubRelation originalPdwhPubRelation) {
    PubOriginalDataDOM pubDOM = pubOriginalDataDAO.findById(originalPdwhPubRelation.getId());
    if (pubDOM == null) {
      logger.error("保存基准库成果出错----没找到对应的原始数据");
      originalPdwhPubRelation.setStatus(9);
      originalPdwhPubRelation.setErrorMsg("在V_PUB_ORIGINAL_DATA中没找到对应的原始数据");
    } else {
      Map<String, Object> resultMap = savePdwhPub(pubDOM.getPubData(), originalPdwhPubRelation.getInsId(),
          originalPdwhPubRelation.getPsnId(), originalPdwhPubRelation.getRecordFrom());
      if (resultMap == null) {
        originalPdwhPubRelation.setStatus(9);
        originalPdwhPubRelation.setUpdateDate(new Date());
        originalPdwhPubRelation.setErrorMsg("crossref原始数据title为空 ,不处理");
      } else {
        String result = (String) resultMap.get("result");
        if ("1".equals(result)) {
          originalPdwhPubRelation.setStatus(9);
          originalPdwhPubRelation.setUpdateDate(new Date());
          originalPdwhPubRelation.setErrorMsg("crossref原始数据type与系统type不匹配,不处理");
        } else {
          String status = JacksonUtils.jsonToMap(result).get("status").toString();
          if ("SUCCESS".equals(status)) {
            String des3pubId = JacksonUtils.jsonToMap(result).get("de3PubId").toString();
            originalPdwhPubRelation.setPdwhPubId(Long.valueOf(Des3Utils.decodeFromDes3(des3pubId)));
            if (originalPdwhPubRelation.getRecordFrom() == 3) {
              // 处理需要另外存储的数据
              // 1.备份单条数据到mogodb
              Long pubId = Long.valueOf(Des3Utils.decodeFromDes3(des3pubId));
              Map<String, Object> DataMap = JacksonUtils.jsonToMap(pubDOM.getPubData());
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
            }
            originalPdwhPubRelation.setStatus(1);
            originalPdwhPubRelation.setErrorMsg("");
            originalPdwhPubRelation.setUpdateDate(new Date());
          } else {
            originalPdwhPubRelation.setStatus(9);
            originalPdwhPubRelation.setErrorMsg("保存基准库接口出错");
            originalPdwhPubRelation.setUpdateDate(new Date());
          }
        }
      }
    }
    originalPdwhPubRelationDao.saveOrUpdate(originalPdwhPubRelation);
  }

  public Map<String, Object> savePdwhPub(String pubData, Long insId, Long psnId, Integer recordFrom) {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> resultMap = new HashMap<String, Object>();
    // 调用接口将xml转成json
    if (recordFrom == 3) {
      Map<String, Object> DataMap = JacksonUtils.jsonToMap(pubData);
      if ((List<String>) DataMap.get("title") == null) {
        return null;
      }
      String type =
          "report,peer-review,reference-entry,component,report-series,standard,posted-content,dataset,journal-issue,journal-volume,journal,proceedings-series,book-set,book-series";// 这些数据暂时不处理
      if (type.contains(String.valueOf(DataMap.get("type")))) {
        resultMap.put("result", "1");
        return resultMap;
      }
      map = handlePdwhPubXmlService.CrossrefToJsonData(DataMap);
      if (StringUtils.isBlank(map.get("title").toString())) {
        return null;
      }
    } else {
      map = handlePdwhPubXmlService.XmlToJsonData(pubData);
    }
    // 先调用查重接口
    String dupResult = handlePdwhPubXmlService.getPubDupucheckStatus(map);
    Map dupResultMap = JacksonUtils.jsonToMap(dupResult);
    String result = "";
    if ("SUCCESS".equals(dupResultMap.get("status").toString())) {
      if (dupResultMap.get("msg") != null) {// 调用更新基准库成果的接口
        Long pdwhPubId = Long.valueOf(dupResultMap.get("msg").toString());
        result = handlePdwhPubXmlService.updatePdwhPub(map, pdwhPubId, insId, psnId);
      } else {// 调用接口保存基准库成果
        result = handlePdwhPubXmlService.saveNewPdwhPub(map, insId, psnId);
      }
    }
    resultMap.put("result", StringUtils.isNotBlank(result) ? result : dupResult);
    resultMap.put("pubData", map);
    return resultMap;
  }

  @Override
  public void saveHandleResult(OriginalPdwhPubRelation originalPdwhPubRelation) {
    originalPdwhPubRelationDao.saveOrUpdate(originalPdwhPubRelation);
  }

  private void savePubCategory(Long pubId, List<String> subject) {
    for (String category : subject) {
      PubCategoryCrossref pubCategory = pubCategoryCrossrefDao.getCategory(pubId, category.trim());
      if (pubCategory == null) {
        pubCategory = new PubCategoryCrossref(pubId, category.trim());
        pubCategoryCrossrefDao.save(pubCategory);
      }

    }
  }

  private void saveReferences(Long pubId, List<Map<Object, Object>> references) {
    for (Map<Object, Object> referenceMap : references) {
      String DOI = null;
      String key = String.valueOf(referenceMap.get("key")) == "null" ? "" : String.valueOf(referenceMap.get("key"));
      if (StringUtils.isBlank(key)) {
        continue;
      }
      PubReference pubReference = pubReferenceDao.getReference(pubId, key);
      if (pubReference == null) {
        pubReference = new PubReference(pubId, key);
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

  @Override
  public List<OriginalPdwhPubRelation> getRemoveData() {
    return originalPdwhPubRelationDao.getRemoveData();
  }

  @Override
  public void saveOriginalPdwhPubRelationHis(OriginalPdwhPubRelationHis relationHis) {
    originalPdwhPubRelationHisDao.save(relationHis);
  }

  @Override
  public void deleteOriginalPdwhPubRelation(OriginalPdwhPubRelation originalPdwhPubRelation) {
    originalPdwhPubRelationDao.delete(originalPdwhPubRelation.getId());
  }

}
