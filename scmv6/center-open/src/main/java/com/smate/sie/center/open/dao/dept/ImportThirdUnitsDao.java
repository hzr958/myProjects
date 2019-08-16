package com.smate.sie.center.open.dao.dept;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.open.model.dept.ImportThirdUnits;
import com.smate.sie.center.open.model.dept.ImportThirdUnitsPK;

/**
 * 第三方部门信息DAO.
 * 
 * @author xys
 *
 */
@Repository
public class ImportThirdUnitsDao extends SieHibernateDao<ImportThirdUnits, ImportThirdUnitsPK> {

  /**
   * 删除某个单位的第三方部门信息.
   * 
   * @param insId
   * @throws Exception
   */
  public void deleteImportThirdUnitsByInsId(Long insId) throws Exception {
    super.createQuery("delete from ImportThirdUnits t where t.pk.insId=?", insId).executeUpdate();
  }
}
