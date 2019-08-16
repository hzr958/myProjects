package com.smate.center.open.service.reschproject;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.center.open.dao.institution.InstitutionDao;
import com.smate.center.open.exception.OpenPsnException;
import com.smate.center.open.exception.OpenPsnInfoDaoException;
import com.smate.core.base.consts.model.Institution;


/**
 * 单位机构服务接实现类.
 * 
 * @author ajb
 * 
 */
@Service("institutionManager")
@Transactional(rollbackFor = Exception.class)
public class InstitutionManagerImpl implements InstitutionManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private InstitutionDao institutionDao;

  @Override
  public Long getInsIdByName(String insNameZh, String insNameEn) throws Exception {
    try {
      return institutionDao.getInsIdByName(insNameZh, insNameEn);
    } catch (Exception e) {
      logger.error("根据单位名查找单位ID出错", e);
    }
    return null;
  }

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
  public Institution getInstitution(Long insId) throws OpenPsnInfoDaoException {
    try {
      return institutionDao.findById(insId);
    } catch (OpenPsnInfoDaoException e) {
      logger.error("取单位列表出错:", e);
      throw new OpenPsnException();
    }
  }

  @Override
  public Institution findByName(String name) throws OpenPsnInfoDaoException {
    try {
      return institutionDao.findByName(name);
    } catch (OpenPsnInfoDaoException e) {
      logger.error("findListByName查询单位出错:", e);
      throw new OpenPsnInfoDaoException(e);
    }
  }

  @Override
  public List<Institution> getInsListByName(String insNameZh, String insNameEn, Long natureType) {
    try {
      return institutionDao.getInsListByName(insNameZh, insNameEn, natureType);
    } catch (Exception e) {
      logger.error("根据单位名查找单位ID出错", e);
    }
    return null;
  }

}
