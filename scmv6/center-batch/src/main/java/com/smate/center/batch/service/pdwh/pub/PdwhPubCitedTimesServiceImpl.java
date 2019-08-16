package com.smate.center.batch.service.pdwh.pub;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.PdwhCitedRelationDao;
import com.smate.center.batch.model.pdwh.pub.PdwhCitedRelation;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;

/**
 * 
 * @author zjh 成果引用次数实现类
 *
 */
@Service("pdwhPubCitedTimesService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubCitedTimesServiceImpl implements PdwhPubCitedTimesService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhCitedRelationDao pdwhCitedRelationDao;

  @Override
  public void updatePdwhPubCitedRelation(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    Long pdwhPubId = NumberUtils.toLong(context.getPdwhPubInfoMap().get("pdwhPubId").toString());
    if (pdwhPub.getPubId() != null && pdwhPubId != null) {
      this.savePdwhPubCitedRelation(pdwhPubId, pdwhPub.getPubId());
    }

  }

  @Override
  public void savePdwhPubCitedRelation(Long pdwhPubId, Long citedPubId) throws Exception {
    // pdwhPubId 被引用的
    // citedPubId 引用该成果的
    PdwhCitedRelation pubRelation = pdwhCitedRelationDao.getPdwhCitedRelation(pdwhPubId, citedPubId);
    if (pubRelation == null) {// 重复数据不插入
      try {
        this.saveCitedRelation(pdwhPubId, citedPubId);
      } catch (Throwable e) {
        logger.error("保存成果引用关系出错，pubId={},citedPubId={}", pdwhPubId, citedPubId, e);
      }
    }

  }

  /**
   * 线程安全保存引用关系
   * 
   * @param pdwhPubId
   * @param citedPubId
   * @author LIJUN
   * @date 2018年6月21日
   */
  public void saveCitedRelation(Long pdwhPubId, Long citedPubId) {
    synchronized (this) {
      PdwhCitedRelation pdwhCitedRelation = new PdwhCitedRelation();
      pdwhCitedRelation.setPdwdPubId(pdwhPubId);
      pdwhCitedRelation.setPdwhCitedPubId(citedPubId);
      pdwhCitedRelationDao.save(pdwhCitedRelation);
    }
  }

}
