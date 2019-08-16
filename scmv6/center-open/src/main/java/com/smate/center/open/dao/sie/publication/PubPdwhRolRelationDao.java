package com.smate.center.open.dao.sie.publication;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.sie.publication.PubPdwhRolRelation;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 基准库成果ID和ROL成果ID关系对应表
 * 
 * @author ajb
 *
 */
@Repository
public class PubPdwhRolRelationDao extends RolHibernateDao<PubPdwhRolRelation, Long> {

  public List<Object[]> getPdwhPubIdsByIds(List<Long> rolPubIds) {
    String hql = "select   p.pdwhPubId   ,  p.rolPubId  from  PubPdwhRolRelation  p where p.rolPubId in ( :rolPubIds )";
    List list = this.createQuery(hql).setParameterList("rolPubIds", rolPubIds).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  public Long getrolPubIdBypdwhPubId(Long pdwhPubId) {
    String hql = "select p.rolPubId   from  PubPdwhRolRelation p where p.pdwhPubId=:pdwhPubId ";
    List list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
    if (list != null && list.size() > 0) {
      return (Long) list.get(0);
    }
    return 0L;
  }
}
