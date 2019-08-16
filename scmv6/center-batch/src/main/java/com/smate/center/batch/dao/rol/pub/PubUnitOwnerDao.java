package com.smate.center.batch.dao.rol.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PubUnitOwner;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 成果部门创建关系.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubUnitOwnerDao extends RolHibernateDao<PubUnitOwner, Long> {

  /**
   * 创建关系.
   * 
   * @param pubId
   * @param unitId
   * @param parentUnitId
   */
  public void savePubUnitOwner(Long pubId, Long insId, Long unitId, Long parentUnitId) {

    PubUnitOwner puo = super.get(pubId);
    if (puo == null) {
      puo = new PubUnitOwner(pubId, insId, unitId, parentUnitId);
    } else {
      puo.setInsId(insId);
      puo.setUnitId(unitId);
      puo.setParentUnitId(parentUnitId);
    }
    super.save(puo);
  }

  /**
   * 删除部门的拥有关系.
   * 
   * @param pubId
   * @param insId
   * @param unitId
   */
  public void removePubUnitOwner(Long pubId, Long insId, Long unitId) {

    String hql = "delete from PubUnitOwner t where (unitId = ? or parentUnitId = ? ) and insId = ? and pubId = ?";
    super.createQuery(hql, unitId, unitId, insId, pubId).executeUpdate();
  }

  /**
   * 获取部门与成果的拥有关系.
   * 
   * @param pubId
   * @param insId
   * @param parentUnitId
   * @return
   */
  public PubUnitOwner getPubUnitOwner(Long pubId, Long insId, Long unitId) {

    String hql = "from PubUnitOwner where (unitId = ? or parentUnitId = ? ) and insId = ? and pubId = ? ";
    return super.findUnique(hql, unitId, unitId, insId, pubId);
  }
}
