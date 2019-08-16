package com.smate.center.batch.service.pdwh.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.oalib.OalibPubDupDao;
import com.smate.center.batch.dao.pdwh.pub.oalib.OalibPubExtendDao;
import com.smate.center.batch.model.pdwh.pub.oalib.OalibPubExtend;

/**
 * oalib成果业务实现
 * 
 * @author LIJUN
 *
 */
@Service("oalibPublicationService")
@Transactional(rollbackFor = Exception.class)
public class OalibPublicationServiceImpl implements OalibPublicationService {
  @Autowired
  private OalibPubExtendDao oalibPubExtendDao;
  @Autowired
  private OalibPubDupDao oalibPubDupDao;

  @Override
  public Long getDupPub(Long titleHashValue, Long unionHashValue, Long sourceIdHash) {
    return oalibPubDupDao.getDupOalibPub(titleHashValue, unionHashValue, sourceIdHash);
  }

  @Override
  public void saveOalibPubDup(Long pubId, Long titleHashValue, Long unitHashValue, Long sourceIdHashValue) {
    oalibPubDupDao.saveOalibPub(pubId, titleHashValue, sourceIdHashValue, unitHashValue);
  }

  @Override
  public void saveOalibPubExtend(Long pubId, String xmlData) {
    OalibPubExtend extend = oalibPubExtendDao.get(pubId);
    if (extend == null) {
      extend = new OalibPubExtend(pubId, xmlData);
    } else {
      extend.setXmlData(xmlData);
    }
    oalibPubExtendDao.save(extend);
  }

}
