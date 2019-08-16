package com.smate.center.task.service.solrindex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubAddrInsRecordDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubIndexUrlDao;
import com.smate.center.task.dao.pdwh.quartz.PubCategoryDao;
import com.smate.center.task.dao.sns.pub.FileDownloadRecordDao;
import com.smate.center.task.dao.sns.quartz.CategoryScmDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubFullTextDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubFullTextPO;
import com.smate.core.base.utils.constant.ShortUrlConst;

public abstract class AbstractIndexHandleServiceImpl implements AllIndexService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  public String runEnv = System.getenv("RUN_ENV");
  @Value("${solr.server.url.update}")
  private String serverUrl;
  @Value("${domainscm}")
  private String domainScm;

  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private PubCategoryDao pubCategoryDao;
  @Autowired
  private CategoryScmDao categoryScmDao;
  @Autowired
  private PdwhPubAddrInsRecordDao pdwhPubAddrInsRecordDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private FileDownloadRecordDao fileDownloadRecordDao;

  public abstract Map<String, Object> checkData(IndexInfoVO indexInfoVO);

  public abstract void queryBaseData(IndexInfoVO indexInfoVO);

  public abstract void buildIndexData(IndexInfoVO indexInfoVO);

  @Override
  public IndexInfoVO indexHandleByType(IndexInfoVO indexInfoVO) {
    // 1: 检查参数
    Map<String, Object> checkResult = checkData(indexInfoVO);
    if (checkResult == null) {
      // 2:查询数据
      queryBaseData(indexInfoVO);
      // 3:将需要加到solr里面的字段加入进去
      buildIndexData(indexInfoVO);
    } else {
      indexInfoVO.setStatus("error");
      indexInfoVO.setMsg(checkResult.get("msg").toString());
    }
    return indexInfoVO;
  }

  /*
   * 在index时区分唯一id
   */
  public Long generateIdForIndex(Long id, String type) {

    if (IndexInfoVO.INDEX_TYPE_PDWH_PAPER.equalsIgnoreCase(type)) {// pdwhPaper前缀为100000
      String idString = String.valueOf(id);
      return Long.parseLong("100000" + idString);
    } else if (IndexInfoVO.INDEX_TYPE_PDWH_PAT.equalsIgnoreCase(type)) { // pdwhPat前缀为700000
      String idString = String.valueOf(id);
      return Long.parseLong("700000" + idString);
    } else if (IndexInfoVO.INDEX_TYPE_SNS_PUB.equalsIgnoreCase(type)) {// snsPub前缀为700000

      String idString = String.valueOf(id);
      return Long.parseLong("410000" + idString);
    } else if (IndexInfoVO.INDEX_TYPE_PSN.equalsIgnoreCase(type)) {// psn前缀为50
      String idString = String.valueOf(id);
      return Long.parseLong("50" + idString);
    } else {
      return id;
    }
  }

  public void buildPdwhPubShortUrl(Long pdwhPubId, SolrInputDocument doc) {
    String pdwhShortUrlDomain = domainScm + "/" + ShortUrlConst.S_TYPE + "/";
    String pubShortUrl = this.pdwhPubIndexUrlDao.getStringUrlByPubId(pdwhPubId);
    if (StringUtils.isNotBlank(pubShortUrl)) {
      doc.setField("pubShortUrl", pdwhShortUrlDomain + pubShortUrl);
    }
  }

  public void buildPdwhPubCategory(Long pubId, SolrInputDocument doc) {
    Set<Long> superCatIds = new HashSet<Long>();
    List<Long> categoryList = this.pubCategoryDao.getScmCategoryByPubId(pubId);
    if (categoryList != null && categoryList.size() > 0) {
      doc.setField("pubCategory", categoryList.toArray());
      for (Long catId : categoryList) {
        Long superCatId = categoryScmDao.getSuperCatId(catId);
        superCatIds.add(0L == superCatId ? catId : superCatId);
      }
      doc.setField("pubSuperCategory", superCatIds.toArray());
    }
  }

  public void buildPdwhPubRegionIds(Long pubId, SolrInputDocument doc) {
    Set<Long> regionIdList = new HashSet<Long>();
    Optional.ofNullable(pdwhPubAddrInsRecordDao.getPubRegionIdByPubId(pubId)).ifPresent(regionIds -> {
      regionIdList.addAll(regionIds);
      regionIds.forEach(regionId -> {
        Optional.ofNullable(constRegionDao.getSuperRegionList(regionId)).ifPresent(superRegionIds -> {
          regionIdList.addAll(superRegionIds);
        });
      });
    });
    if (regionIdList != null && regionIdList.size() > 0) {
      doc.setField("regionCode", regionIdList.toArray());
    }
  }


  public void buildPdwhPubFulltext(Long pubId, SolrInputDocument doc) {
    PdwhPubFullTextPO fulltext = this.pdwhPubFullTextDAO.getByPubId(pubId);
    Long downLoadSum = 0L;
    if (fulltext != null) {
      if (fulltext.getPermission() == 0) {
        downLoadSum = fileDownloadRecordDao.getFileDonwloadSum(fulltext.getFileId());
      }
      if (StringUtils.isNotBlank(fulltext.getThumbnailPath())) {
        doc.setField("fullTextImgUrl", domainScm + fulltext.getThumbnailPath());
      }
      doc.setField("downLoadCount", downLoadSum.intValue());
    }
    // 用于有全文的排序，1有；0无
    doc.setField("fullText", fulltext == null ? 0 : 1);
  }

  public boolean saveIndex(ArrayList<SolrInputDocument> docList) {
    if (CollectionUtils.isNotEmpty(docList)) {
      SolrServer server = new HttpSolrServer(serverUrl);
      try {
        Date start = new Date();
        server.add(docList);
        server.commit();
        Date end = new Date();
        logger.info("Patent索引创建完成，end = " + new Date() + " , 总共耗费时间(s)：" + (end.getTime() - start.getTime()) / 1000);
        return true;
      } catch (SolrServerException | IOException e) {
        e.printStackTrace();
        logger.info("Patent索引创建出错，end = " + new Date());
        return false;
      }
    }
    return false;
  }

}
