package com.smate.center.task.single.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.single.model.rol.pub.PubPdwhRolRelation;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 基准库成果ID和ROL成果ID关系对应表
 * 
 * @author ajb
 *
 */
@Repository
public class PubPdwhRolRelationDao extends RolHibernateDao<PubPdwhRolRelation, Long> {

  public List<Long> getPdwhPubIdsByIds(List<Long> rolPubIds) {
    String hql = "select  p.pdwhPubId  from  PubPdwhRolRelation  p where p.rolPubId in ( :rolPubIds )";
    List list = this.createQuery(hql).setParameterList("rolPubIds", rolPubIds).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  public Long getPdwhPubId(Long rolPubId) {
    String hql = "select  p.pdwhPubId  from  PubPdwhRolRelation  p where p.rolPubId = :rolPubId";
    return (Long) super.createQuery(hql).setParameter("rolPubId", rolPubId).uniqueResult();
  }

}
