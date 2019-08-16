package com.smate.center.batch.service.projectmerge;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.prj.ScmPrjXmlDao;
import com.smate.center.batch.dao.sns.prj.ScmPrjXmlEmptyDao;
import com.smate.center.batch.model.sns.prj.ScmPrjXml;
import com.smate.center.batch.model.sns.prj.ScmPrjXmlEmpty;

/**
 * 
 * @author liqinghua
 * 
 */
@Service("scmPrjXmlService")
@Transactional(rollbackFor = Exception.class)
public class ScmPrjXmlServiceImpl implements ScmPrjXmlService {

  /**
   * 
   */
  private static final long serialVersionUID = 5321526402450985428L;

  private static Logger logger = LoggerFactory.getLogger(ScmPrjXmlServiceImpl.class);
  @Autowired
  private ScmPrjXmlDao scmPrjXmlDao;
  @Autowired
  private ScmPrjXmlEmptyDao scmPrjXmlEmptyDao;

  @Override
  public ScmPrjXml savePrjXml(Long prjId, String xml) throws Exception {

    if (StringUtils.isBlank(xml)) {
      logger.error("xml内容不能为空prjId：" + prjId);
      throw new Exception("xml内容不能为空prjId：" + prjId);
    }
    ScmPrjXml prjXml = scmPrjXmlDao.get(prjId);
    if (prjXml == null) {
      prjXml = new ScmPrjXml(prjId);
    }
    prjXml.setPrjXml(xml);
    this.scmPrjXmlDao.save(prjXml);
    return prjXml;
  }

  @Override
  public ScmPrjXml getPrjXml(Long prjId) throws Exception {

    try {
      return scmPrjXmlDao.get(prjId);
    } catch (Exception e) {
      logger.error("获取项目XML." + prjId, e);
      throw new Exception("获取项目XML." + prjId, e);
    }
  }

  @Override
  public void saveScmPrjXmlEmpty(Long prjId) throws Exception {

    try {
      ScmPrjXmlEmpty prjXmlEmpty = scmPrjXmlEmptyDao.get(prjId);
      if (prjXmlEmpty == null) {
        prjXmlEmpty = new ScmPrjXmlEmpty(prjId);
        scmPrjXmlEmptyDao.save(prjXmlEmpty);
      }
    } catch (Exception e) {
      logger.error("添加项目XML为空的记录." + prjId, e);
      throw new Exception("添加项目XML为空的记录." + prjId, e);
    }
  }

}
