package com.smate.center.batch.service.pubfulltexttoimage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.ScmPubXmlDao;
import com.smate.center.batch.dao.sns.pub.ScmPubXmlEmptyDao;
import com.smate.center.batch.model.sns.pub.ScmPubXml;
import com.smate.center.batch.model.sns.pub.ScmPubXmlEmpty;
import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * 
 * @author liqinghua
 * 
 */
@Service("scmPubXmlService")
@Transactional(rollbackFor = Exception.class)
public class ScmPubXmlServiceImpl implements ScmPubXmlService {


  /**
   * 
   */
  private static final long serialVersionUID = -4748428222878525362L;
  private static Logger logger = LoggerFactory.getLogger(ScmPubXmlServiceImpl.class);
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private ScmPubXmlEmptyDao scmPubXmlEmptyDao;

  @Override
  public ScmPubXml savePubXml(Long pubId, String xml) throws BatchTaskException {

    if (StringUtils.isBlank(xml)) {
      logger.error("xml内容不能为空pubId：" + pubId);
      throw new BatchTaskException("xml内容不能为空pubId：" + pubId);
    }
    ScmPubXml pubXml = scmPubXmlDao.get(pubId);
    if (pubXml == null) {
      pubXml = new ScmPubXml(pubId);
    }
    pubXml.setPubXml(xml);
    this.scmPubXmlDao.save(pubXml);
    return pubXml;
  }

  @Override
  public ScmPubXml getPubXml(Long pubId) throws BatchTaskException {

    try {
      return scmPubXmlDao.get(pubId);
    } catch (Exception e) {
      logger.error("获取成果XML." + pubId, e);
      throw new BatchTaskException("获取成果XML." + pubId, e);
    }
  }

  @Override
  public void saveScmPubXmlEmpty(Long pubId) throws BatchTaskException {

    try {
      ScmPubXmlEmpty pubXmlEmpty = scmPubXmlEmptyDao.get(pubId);
      if (pubXmlEmpty == null) {
        pubXmlEmpty = new ScmPubXmlEmpty(pubId);
        scmPubXmlEmptyDao.save(pubXmlEmpty);
      }
    } catch (Exception e) {
      logger.error("添加成果XML为空的记录." + pubId, e);
      throw new BatchTaskException("添加成果XML为空的记录." + pubId, e);
    }
  }

}
