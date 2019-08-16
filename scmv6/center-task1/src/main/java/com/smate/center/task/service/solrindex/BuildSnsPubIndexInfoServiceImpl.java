package com.smate.center.task.service.solrindex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.task.dao.open.OpenUserUnionDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.v8pub.dao.sns.PubSnsDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDetailDAO;
import com.smate.center.task.v8pub.sns.po.PubSnsPO;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

@Transactional(rollbackFor = Exception.class)
public class BuildSnsPubIndexInfoServiceImpl extends AbstractIndexHandleServiceImpl {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;

  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;

  @Override
  public Map<String, Object> checkData(IndexInfoVO indexInfoVO) {
    Map<String, Object> map = new HashMap<>();
    if (indexInfoVO.getLastPatId() == null) {
      map.put("result", "error");
      map.put("msg", "查询的pubId不能为空");
      return map;
    }

    return null;
  }

  @Override
  public void queryBaseData(IndexInfoVO indexInfoVO) {
    indexInfoVO.setSnsPubList(pubSnsDAO.batchGetPublist(indexInfoVO.getLastSnsPubId()));
  }

  @Override
  public void buildIndexData(IndexInfoVO indexInfoVO) {
    List<PubSnsPO> snsPubList = indexInfoVO.getSnsPubList();
    if (CollectionUtils.isNotEmpty(snsPubList)) {
      ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
      for (PubSnsPO snsPub : snsPubList) {
        PubSnsDetailDOM detail = pubSnsDetailDAO.findById(snsPub.getPubId());
        SolrInputDocument doc = new SolrInputDocument();
        if (detail != null) {
          // 必须字段设定schema.xml配置
          doc.setField("businessType", IndexInfoVO.INDEX_TYPE_SNS_PUB);
          Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
          doc.setField("env", runEnv);
          doc.setField("id", generateIdForIndex(detail.getPubId(), IndexInfoVO.INDEX_TYPE_SNS_PUB));
          doc.setField("pubId", detail.getPubId());
          doc.setField("pubOwnerOpenId", getOpenId(psnPubDAO.getPsnOwner(detail.getPubId())));
          doc.setField("title", detail.getTitle());
        }
        docList.add(doc);
      }
    }

  }

  private Object getOpenId(Long psnId) {
    Long openId = null;
    try {
      openId = openUserUnionDao.getOpenIdByPsnId(psnId);
    } catch (DaoException e) {
      logger.error("snsPub索引根据PsnId获取openId出错,psnId" + psnId);
    }
    return openId;
  }

}
