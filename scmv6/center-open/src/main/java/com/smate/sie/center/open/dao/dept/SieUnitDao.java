package com.smate.sie.center.open.dao.dept;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.sie.publication.InsUnit;
import com.smate.core.base.utils.data.RolHibernateDao;

@Repository
public class SieUnitDao extends RolHibernateDao<InsUnit, Long> {

  /**
   * 获取单位部门数
   * 
   * @param insId
   * @return
   */
  public Long getNums(Long insId) {
    Long unitNum = super.findUnique("select count(*) from InsUnit p where p.insId = ? ", insId);
    return unitNum;
  }

  /**
   * 根据unitId获取部门
   * 
   * @param unitId
   * @return
   */
  public InsUnit getUnitById(Long unitId) {
    String hql = "From InsUnit t where t.id = ? ";
    return (InsUnit) super.createQuery(hql, new Object[] {unitId}).uniqueResult();
  }

  public void insertUnit(Long id, Long ins_id, String unitName, Long superInsUnitId) {
    String sql = "insert into ins_unit(unit_id,ins_id,super_unit_id,zh_name) values(?,?,?,?)";
    super.update(sql, new Object[] {id, ins_id, superInsUnitId, unitName});

  }

}
