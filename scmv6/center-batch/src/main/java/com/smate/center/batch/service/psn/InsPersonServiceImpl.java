package com.smate.center.batch.service.psn;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.dao.rol.pub.InsUnitDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.rol.pub.InsUnit;
import com.smate.center.batch.service.rol.pub.InsUnitRolService;
import com.smate.core.base.utils.dao.security.UserRoleDao;
import com.smate.core.base.utils.model.security.UserRole;
import com.smate.core.base.utils.security.TheadLocalInsId;

/**
 * 
 * 单位人员管理服务类.
 * 
 */
@Service("insPersonService")
@Transactional(rollbackFor = Exception.class)
public class InsPersonServiceImpl implements InsPersonService, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 182079982347870004L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserRoleDao userRoleDao;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private InsUnitRolService insUnitRolService;
  @Autowired
  private InsUnitDao insUnitDao;

  @Override
  public List<UserRole> getSysAdministrator() throws ServiceException {
    try {
      return this.userRoleDao.getSysAdministrator();
    } catch (Exception e) {
      logger.error("getSysAdministrator", e);
      throw new ServiceException();
    }
  }

  @Override
  public String getPsnUnitNameCompose(Long insId, Long psnId) throws ServiceException {
    try {
      RolPsnIns psnIns = rolPsnInsDao.findPsnIns(psnId, insId);
      if (psnIns != null) {
        String psnName = StringUtils.isBlank(psnIns.getZhName()) ? psnIns.getEnName() : psnIns.getZhName();
        Long unitId = psnIns.getUnitId();
        if (unitId != null) {
          InsUnit unitRol = insUnitRolService.getInsUnitRolById(unitId);
          if (unitRol != null) {
            String unitName = unitRol.getZhName() == null ? unitRol.getEnName() : unitRol.getZhName();
            psnName = psnName + "(" + unitName + ")";
          }
        }
        return psnName;
      }
      return null;
    } catch (Exception e) {
      logger.error("获取单位人员姓名以及所在机构组合，psnId：" + psnId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取本单位指定人员.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  @Override
  public RolPsnIns findRolPsnIns(Long psnId) throws ServiceException {
    Long insId = TheadLocalInsId.getInsId();
    try {
      RolPsnIns rolPsn = this.rolPsnInsDao.findPsnIns(psnId, insId);
      return rolPsn;
    } catch (Exception e) {
      logger.error("获取本单位指定人员.出错，psnId：" + psnId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public RolPsnIns findRolPsnIns(Long insId, Long psnId) throws ServiceException {
    try {

      RolPsnIns rolPsn = this.rolPsnInsDao.findPsnIns(psnId, insId);
      return rolPsn;
    } catch (Exception e) {
      logger.error("获取指定单位指定人员.出错，psnId：" + psnId + " insid:" + insId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, String[]> getPsnUnitName(Long insId, Collection<Long> psnIds) throws ServiceException {

    List<RolPsnIns> psnList = this.rolPsnInsDao.getPsnUnitId(insId, psnIds);
    if (CollectionUtils.isEmpty(psnList)) {
      return null;
    }
    Set<Long> unitIds = new HashSet<Long>();
    for (RolPsnIns psn : psnList) {
      if (psn.getUnitId() != null) {
        unitIds.add(psn.getUnitId());
      }
      if (psn.getSuperUnitId() != null) {
        unitIds.add(psn.getSuperUnitId());
      }
    }
    Map<Long, String[]> map = new HashMap<Long, String[]>();
    List<InsUnit> unitList = this.insUnitDao.getInsUnitByIds(unitIds);
    for (RolPsnIns psn : psnList) {
      String[] unitNames = new String[] {null, null};
      map.put(psn.getPk().getPsnId(), unitNames);
      for (InsUnit unit : unitList) {
        // sub unit
        if (unit.getId().equals(psn.getUnitId())) {
          unitNames[0] = unit.getZhName();
        }
        // super unit
        if (unit.getId().equals(psn.getSuperUnitId())) {
          unitNames[1] = unit.getZhName();
        }
      }
    }
    return map;
  }
}
