package com.smate.core.base.utils.service.insunit;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.insunit.SieInsUnitDao;
import com.smate.core.base.utils.model.rol.SieInsUnit;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 
 * @author yxs
 * @descript 部门service
 */
@Service("sieInsUnitService")
@Transactional(rollbackFor = Exception.class)
public class SieInsUnitServiceImpl implements SieInsUnitService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SieInsUnitDao insUnitDao;

  @Override
  public List<SieInsUnit> getAcInsUnit(Long insId) throws Exception {
    List<SieInsUnit> insUnitList = null;
    insUnitList = insUnitDao.getSuperUnitByInsId(insId);
    return insUnitList;

  }

  @Override
  public Long getInsUnitId(String unitName, Long insId) throws Exception {
    try {
      return insUnitDao.getId(unitName, insId);
    } catch (Exception e) {
      logger.error("获取部门Id出错", e);
      return null;
    }
  }

  @Override
  public SieInsUnit getInsUnitById(Long id) throws Exception {
    return insUnitDao.get(id);
  }

  @Override
  public List<SieInsUnit> getAllInsUnit() throws Exception {

    try {
      Long insId = SecurityUtils.getCurrentInsId();
      return insUnitDao.getAllInsUnit(insId);
    } catch (Exception e) {
      logger.error("获取机构所有部门出错", e);
      return null;
    }
  }

}
