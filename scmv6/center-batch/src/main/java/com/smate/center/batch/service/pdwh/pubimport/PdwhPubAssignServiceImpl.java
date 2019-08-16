package com.smate.center.batch.service.pdwh.pubimport;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubAssignDao;
import com.smate.center.batch.dao.pdwh.pubimport.PdwhPubXmlDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAssign;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXml;
import com.smate.center.batch.service.pub.mq.PdwhPubCacheAssignProducer;

/**
 * 基准库成果服务实现
 * 
 * @author zll
 *
 */
@Service("pdwhPubAssignService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubAssignServiceImpl implements PdwhPubAssignService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubAssignDao pdwhPubAssignDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Resource(name = "pdwhPubCacheAssignProducer")
  private PdwhPubCacheAssignProducer pdwhPubCacheAssignProducer;

  @Override
  public PdwhPubAssign getPdwhPubAssign(Long pubId, Long insId) {
    PdwhPubAssign assignInfo = this.pdwhPubAssignDao.getPdwhPubAssign(pubId, insId);
    if (assignInfo != null) {
      if (pubId != null) {
        PdwhPubXml xml = this.pdwhPubXmlDao.get(pubId);
        assignInfo.setXmlString(xml.getXml());
      }
    }
    return assignInfo;
  }

  @Override
  public void sendInsPub(PdwhPubAssign pdwhAssign, Integer dbid) {
    try {
      if (pdwhAssign.getInsId() != null) {
        pdwhPubCacheAssignProducer.sendAssignMsg(pdwhAssign.getPubId(), pdwhAssign.getXmlString(),
            pdwhAssign.getInsId(), (pdwhAssign.getResult() == 1 ? 1 : 2), dbid);
      }
      pdwhAssign.setIsSend(1);
      this.pdwhPubAssignDao.save(pdwhAssign);
    } catch (Exception e) {
      logger.error("发送单位成果", e);
      throw new ServiceException("发送单位成果", e);
    }
  }



}
