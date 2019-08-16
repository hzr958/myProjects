package com.smate.web.psn.service.institution;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.Institution;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.PsnInfoDaoException;

/**
 * 单位服务接口实现类
 * 
 * @author Administrator
 *
 */
@Service("institutionManager")
@Transactional(rollbackFor = Exception.class)
public class InstitutionManagerImpl implements InstitutionManager {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InstitutionDao institutionDao;

  /**
   * 通过单位名称获取单位ID
   */
  @Override
  public Long getInsIdByName(String insNameZh, String insNameEn) throws PsnException {
    try {
      return institutionDao.getInsIdByName(insNameZh, insNameEn);
    } catch (PsnInfoDaoException e) {
      logger.error("根据单位名查找单位ID出错", e);
    }
    return null;
  }

  /**
   * 根据单位ID获取对应单位的名称详细信息.
   */
  @Override
  public Institution getInstitution(String insName, Long insId) {
    Institution targetIns = null;
    try {
      Institution ins = null;
      if (insId != null) {
        ins = this.getInstitution(insId);
      } else if (StringUtils.isNotBlank(insName)) {
        ins = this.findByName(insName);
      }
      if (ins != null) {
        targetIns = new Institution();
        targetIns.setId(ins.getId());
        targetIns.setZhName(ins.getZhName());
        targetIns.setEnName(ins.getEnName());
        targetIns.setRegionId(ins.getRegionId());
        targetIns.setAbbreviation(ins.getAbbreviation());
      }
    } catch (Exception e) {
      logger.error("获取单位的详细信息出错", e);
    }
    return targetIns;
  }

  /**
   * 通过单位编号取得单位实体
   */
  @Override
  public Institution getInstitution(Long insId) throws PsnInfoDaoException {
    try {

      return institutionDao.findById(insId);

    } catch (PsnInfoDaoException e) {
      logger.error("取单位列表出错:", e);
      throw new PsnException();
    }
  }

  @Override
  public Institution findByName(String name) throws PsnInfoDaoException {

    try {
      return institutionDao.findByName(name);
    } catch (PsnInfoDaoException e) {
      logger.error("findListByName查询单位出错:", e);
      throw new PsnInfoDaoException(e);
    }
  }

}
