package com.smate.core.base.utils.dao.insunit;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.rol.SieInsUnit;

/**
 * 
 * @author yxs
 * @descript 部门dao
 */
@Repository
public class SieInsUnitDao extends SieHibernateDao<SieInsUnit, Long> {


  // 获取部门某个类型的统计数
  public Long getUnitTypeCount(Long insId, String unitType) {
    return (Long) super.createQuery("select count(1)  from SieInsUnit t where t.insId =? and t.unitType=?",
        new Object[] {insId, unitType}).uniqueResult();
  }

  public Long getDisciCount(List<String> disCodes, Long insId) {
    if (CollectionUtils.isEmpty(disCodes)) {
      return null;
    }
    return (Long) super.createQuery(
        "select count(1) from SieInsUnit t,UnitDiscipline t2 where t.id=t2.unitId and t2.disCode in(:disCode) and t.insId= :insId ")
            .setParameterList("disCode", disCodes).setParameter("insId", insId).uniqueResult();
  }

  /**
   * 获取单位部门,获取最顶层，不加载子部门.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieInsUnit> getSuperUnitByInsId(Long insId) {

    List<SieInsUnit> superUnits =
        super.createQuery("from SieInsUnit t where t.insId = ? and t.superInsUnitId is null ", insId).list();
    return superUnits;
  }

  /**
   * 通过部门名称，获取部门ID.
   * 
   * @param unitName
   * @param insId
   * @return
   */
  public Long getId(String unitName, Long insId) throws Exception {
    return findUnique("select id from SieInsUnit where (zhName=? or enName = ?)  and insId=?", unitName, unitName,
        insId);
  }

  /**
   * 获取所有的部门.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieInsUnit> getAllInsUnit(Long insId) {
    String hql = "from SieInsUnit where insId=? order by  id ";
    Query query = createQuery(hql, insId);
    query.setCacheable(true);
    List<SieInsUnit> list = query.list();
    return list;
  }

  /**
   * 统计单位部门总数
   * 
   * @param insId
   * @return
   */
  public Long getUnitSumByInsId(Long insId) {
    String hql = "select count(id) from SieInsUnit where insId = ? ";
    return super.findUnique(hql, insId);
  }



}
