package com.smate.center.open.service.publication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.publication.ScmPubXmlDao;
import com.smate.center.open.model.publication.ScmPubXml;


/**
 * 
 * @author liqinghua
 * 
 */
@Service("scmPubXmlService")
@Transactional(rollbackFor = Exception.class)
public class ScmPubXmlServiceImpl implements ScmPubXmlService {

  @Autowired
  private ScmPubXmlDao scmPubXmlDao;

  /**
   * 
   */
  private static final long serialVersionUID = 5321526402450985428L;

  private static Logger logger = LoggerFactory.getLogger(ScmPubXmlServiceImpl.class);

  @Override
  public ScmPubXml getPubXml(Long pubId) throws Exception {

    try {
      return scmPubXmlDao.get(pubId);
    } catch (Exception e) {
      logger.error("获取成果XML." + pubId, e);
      throw new Exception("获取成果XML." + pubId, e);
    }
  }
}
