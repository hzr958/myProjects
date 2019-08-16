package com.smate.center.task.service.solrindex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.task.dao.pdwh.quartz.BaseJnlCategoryRankDao;
import com.smate.center.task.dao.pdwh.quartz.BaseJournalCategoryDao;
import com.smate.center.task.model.pdwh.quartz.BaseJnlCategoryRankEnum;
import com.smate.center.task.utils.MessageDigestUtils;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubSituationDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubStatisticsDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.dao.sns.PubStatisticsDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubStatisticsPO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

@Transactional(rollbackFor = Exception.class)
public class BuildPdwhPaperIndexInfoServiceImpl extends AbstractIndexHandleServiceImpl {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;

  @Autowired
  private PdwhPubSituationDAO pdwhPubSituationDAO;
  @Autowired
  private BaseJournalCategoryDao baseJournalCategoryDao;
  @Autowired
  private BaseJnlCategoryRankDao baseJnlCategoryRankDao;

  @Autowired
  private PdwhPubStatisticsDAO pdwhPubStatisticsDAO;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;


  @Override
  public Map<String, Object> checkData(IndexInfoVO indexInfoVO) {
    Map<String, Object> map = new HashMap<>();
    if (indexInfoVO.getLastPaperId() == null) {
      map.put("result", "error");
      map.put("msg", "查询的pubId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryBaseData(IndexInfoVO indexInfoVO) {
    indexInfoVO.setPdwhPubList(pubPdwhDAO.findPubByBatchSize(indexInfoVO.getLastPaperId(), indexInfoVO.getMaxPubId()));

  }

  @Override
  public void buildIndexData(IndexInfoVO indexInfoVO) {
    List<PubPdwhPO> pdwhPubList = indexInfoVO.getPdwhPubList();
    if (CollectionUtils.isNotEmpty(pdwhPubList)) {
      ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
      for (PubPdwhPO pdwhPub : pdwhPubList) {
        try {
          PubPdwhDetailDOM detail = pubPdwhDetailDAO.findById(pdwhPub.getPubId());
          if (detail != null) {
            SolrInputDocument doc = new SolrInputDocument();
            // 必须字段设定schema.xml配置
            doc.setField("businessType", IndexInfoVO.INDEX_TYPE_PDWH_PAPER);
            Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
            doc.setField("env", runEnv);
            doc.setField("id", generateIdForIndex(detail.getPubId(), IndexInfoVO.INDEX_TYPE_PDWH_PAPER));
            doc.setField("pubId", detail.getPubId());
            doc.setField("pubDbId", detail.getSrcDbId());
            buildPubDbIds(doc, detail.getPubId());
            doc.setField("pubTitle", detail.getTitle());
            doc.setField("authors", detail.getAuthorNames());
            try {
              String cleanAuthors = XmlUtil.cleanXMLAuthorChars(detail.getAuthorNames());
              if (cleanAuthors != null) {
                String[] authors = cleanAuthors.split(";");
                StringBuffer md5Authors = new StringBuffer();
                for (int i = 0; i < authors.length - 1; i++) {
                  md5Authors.append(MessageDigestUtils.messageDigest(authors[i].trim()));
                  md5Authors.append("; ");
                }
                md5Authors.append(MessageDigestUtils.messageDigest(authors[authors.length - 1].trim()));
                doc.setField("cleanAuthors", cleanAuthors);
                doc.setField("md5Authors", md5Authors);
              }
            } catch (Exception e) {
              logger.error("生成cleanAuthors出错", e);
            }
            doc.setField("doi", detail.getDoi());
            doc.setField("summary", detail.getSummary());
            doc.setField("keywords", detail.getKeywords());
            if (pdwhPub.getPublishYear() != null) {
              doc.setField("pubYear", pdwhPub.getPublishYear());
            }
            if (pdwhPub.getPublishMonth() != null) {
              doc.setField("pubMonth", pdwhPub.getPublishMonth());
            }
            if (pdwhPub.getPublishDay() != null) {
              doc.setField("pubDay", pdwhPub.getPublishDay());
            }
            doc.setField("pubTypeId", detail.getPubType());
            doc.setField("pubBrief", detail.getBriefDesc());
            doc.setField("pubOrganization", detail.getOrganization());
            doc.setField("pubCitations", detail.getCitations());
            buildPdwhPubRegionIds(pdwhPub.getPubId(), doc);// 构造成果地区
            buildPdwhPubShortUrl(detail.getPubId(), doc);// 构造成果短地址
            buildPdwhPubCategory(pdwhPub.getPubId(), doc);// 构造成果研究领域
            doc.setField("fundInfo", detail.getFundInfo());
            buildPdwhPubFulltext(pdwhPub.getPubId(), doc);// 构造成果全文信息
            PdwhPubStatisticsPO pdwhPubStatistics = pdwhPubStatisticsDAO.get(detail.getPubId());
            // SCM-19763 增加基准库与个人库关联的成果的统计数
            List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsPubIdListByPdwhId(detail.getPubId());
            Long readCount = 0L;
            if (CollectionUtils.isNotEmpty(snsPubIds)) {
              readCount = pubStatisticsDAO.findPubReadCounts(snsPubIds);
              if (readCount == null) {
                readCount = 0L;
              }
            }
            // SCM-19763 增加基准库与个人库关联的成果的统计数
            readCount = (pdwhPubStatistics.getReadCount() == null) ? 0 + readCount
                : pdwhPubStatistics.getReadCount() + readCount;
            doc.setField("readCount", readCount.intValue());

            if (4 == detail.getPubType()) {
              JournalInfoBean infoBean = (JournalInfoBean) detail.getTypeInfo();
              doc.setField("journalGrade", this.getPubQualityByJnlRank(infoBean.getJid()));
              doc.setField("journalName", infoBean.getName());
            }
            docList.add(doc);
          }
        } catch (Exception e) {
          logger.error("基准库论文数据错误，pubid:", pdwhPub.getPubId(), e);
        }

      }
      if (saveIndex(docList)) {
        indexInfoVO.setStatus("success");
        indexInfoVO.setMsg("创建论文索引成功");
      } else {
        indexInfoVO.setStatus("error");
        indexInfoVO.setMsg("创建论文索引失败");
      }
      indexInfoVO.setLastPaperId(pdwhPubList.get(pdwhPubList.size() - 1).getPubId());
    }

  }



  private void buildPubDbIds(SolrInputDocument doc, Long pdwhPubId) {
    List<String> dbIdList = pdwhPubSituationDAO.listByPdwhPubId(pdwhPubId);
    List<Long> dbIds = new ArrayList<Long>();
    for (String dbId : dbIdList) {
      if (StringUtils.isNotBlank(dbId)) {
        dbIds.add(Long.valueOf(dbId));
      }
    }
    if (dbIdList.size() > 0) {
      doc.setField("pubDbIdList", dbIds.toArray());
    }
  }

  private Object getPubQualityByJnlRank(Long jid) {
    Integer highestRank = 9;
    List<Long> ids = baseJournalCategoryDao.getCategoryIdsByJnlId(jid);
    if (CollectionUtils.isNotEmpty(ids)) {
      List<String> rankStrings = baseJnlCategoryRankDao.getRanksByCategoryIdList(ids);
      if (CollectionUtils.isNotEmpty(rankStrings)) {
        String highestRankString = rankStrings.get(0);
        highestRank = BaseJnlCategoryRankEnum.getRankValue(highestRankString);
      }
    }
    return highestRank;
  }

}
