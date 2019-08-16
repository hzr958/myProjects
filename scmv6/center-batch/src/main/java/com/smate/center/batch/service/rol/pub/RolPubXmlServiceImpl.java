package com.smate.center.batch.service.rol.pub;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.RolPubXmlDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.RolPubXml;


/**
 * 
 * @author liqinghua
 * 
 */
@Service("rolPubXmlService")
@Transactional(rollbackFor = Exception.class)
public class RolPubXmlServiceImpl implements RolPubXmlService {


  /**
   * 
   */
  private static final long serialVersionUID = -70022128825543975L;
  private static Logger logger = LoggerFactory.getLogger(RolPubXmlServiceImpl.class);
  @Autowired
  private RolPubXmlDao rolPubXmlDao;

  @Override
  public RolPubXml savePubXml(Long pubId, String xml) throws ServiceException {

    if (StringUtils.isBlank(xml)) {
      logger.error("xml内容不能为空pubId：" + pubId);
      throw new ServiceException("xml内容不能为空pubId：" + pubId);
    }
    RolPubXml pubXml = rolPubXmlDao.get(pubId);
    if (pubXml == null) {
      pubXml = new RolPubXml(pubId);
    }
    pubXml.setPubXml(xml);
    this.rolPubXmlDao.save(pubXml);
    return pubXml;
  }

  @Override
  public RolPubXml getPubXml(Long pubId) throws ServiceException {

    try {
      return rolPubXmlDao.get(pubId);
    } catch (Exception e) {
      logger.error("获取成果XML." + pubId, e);
      throw new ServiceException("获取成果XML." + pubId, e);
    }
  }

  @Override
  public void saveRolPubXmlEmpty(Long pubId) throws ServiceException {
    // TODO
  }

}
