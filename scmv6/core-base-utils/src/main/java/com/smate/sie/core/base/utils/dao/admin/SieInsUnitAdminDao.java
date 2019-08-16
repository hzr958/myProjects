package com.smate.sie.core.base.utils.dao.admin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.admin.SieInsUnitAdmin;
import com.smate.sie.core.base.utils.model.admin.SieInsUnitAdminPk;

/**
 * 部门管理员Dao
 * 
 * @author hd
 *
 */
@Repository
public class SieInsUnitAdminDao extends SieHibernateDao<SieInsUnitAdmin, SieInsUnitAdminPk> {

  /**
   * 统计单位部门管理员总数
   * 
   * @param insId
   * @return
   */
  public Long getInsUnitAdminSumByInsId(Long insId) {
    String hql = "select count(*) from SieInsUnitAdmin where pk.insId = ? ";
    return super.findUnique(hql, insId);
  }

  /**
   * 统计部门管理员总数
   * 
   * @param unitId
   * @return
   */
  public Long getInsUnitAdminSumByUnitId(Long unitId) {
    String hql = "select count(*) from SieInsUnitAdmin where pk.unitId = ? ";
    return super.findUnique(hql, unitId);
  }

  @SuppressWarnings("unchecked")
  public List<SieInsUnitAdmin> getListNumByInsIds(List<Long> insIds) {
    String hql = "from SieInsUnitAdmin t where t.pk.insId in (:insIds) ";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<SieInsUnitAdmin> getListNumByInsId(Long insId) {
    String hql = "from SieInsUnitAdmin t where t.pk.insId = ?";
    return super.createQuery(hql, insId).list();
  }

  public void deleteByInsId(Long mergeid) {
    String hql = "delete from SieInsUnitAdmin t where t.pk.insId= ? ";
    super.createQuery(hql, mergeid).executeUpdate();
  }

}
