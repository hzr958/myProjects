package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.ImportThirdUnits;
import com.smate.sie.center.task.model.ImportThirdUnitsPK;

/**
 * 第三方部门信息DAO.
 * 
 * @author xys
 *
 */
@Repository
public class ImportThirdUnitsDao extends SieHibernateDao<ImportThirdUnits, ImportThirdUnitsPK> {

  /**
   * 获取待导入数据.
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ImportThirdUnits> getThirdUnitsNeedImport(int maxSize) {
    String hql = "from ImportThirdUnits t where t.status=0";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }
}
