package com.smate.center.batch.service.pdwh.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.rainpat.RainPatPubDupDao;
import com.smate.center.batch.dao.pdwh.pub.rainpat.RainPatPubExtendDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.rainpat.RainPatPubDup;
import com.smate.center.batch.model.pdwh.pub.rainpat.RainPatPubExtend;

@Service("rainPatPublicationService")
@Transactional(rollbackFor = Exception.class)
public class RainPatPublicationServiceImpl implements RainPatPublicationService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RainPatPubDupDao rainPatPubDupDao;
  @Autowired
  private RainPatPubExtendDao rainPatPubExtendDao;

  @Override
  public Long getDupPub(Long titleHashValue, Long patentNoHash, Long patentOpenNoHash) {
    try {
      RainPatPubDup rainPatDupPub = rainPatPubDupDao.getRainPatDupPub(titleHashValue, patentNoHash, patentOpenNoHash);
      if (rainPatDupPub != null) {
        return rainPatDupPub.getPubId();
      }

    } catch (Exception e) {
      logger.error("Rainpat查重出错", e);
      throw new ServiceException("Rainpat查重出错", e);
    }
    return null;
  }

  @Override
  public void saveRainPatPubDup(Long pubId, Long titleHashValue, Long patentNoHash, Long patentOpenNoHash) {
    rainPatPubDupDao.saveRainPatPubDup(pubId, titleHashValue, patentNoHash, patentOpenNoHash);
  }

  @Override
  public void saveRainPatPubExtend(Long pubId, String xmlData) {
    RainPatPubExtend extend = rainPatPubExtendDao.get(pubId);
    if (extend == null) {
      extend = new RainPatPubExtend(pubId, xmlData);
    } else {
      extend.setXmlData(xmlData);
    }
    rainPatPubExtendDao.save(extend);
  }

}
