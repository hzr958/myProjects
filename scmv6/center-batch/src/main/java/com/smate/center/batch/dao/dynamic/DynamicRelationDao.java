package com.smate.center.batch.dao.dynamic;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.dynamic.DynamicRelation;
import com.smate.center.batch.model.dynamic.DynamicRelationPk;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 动态关系dao
 * 
 * @author zk
 *
 */
@Repository
public class DynamicRelationDao extends SnsHibernateDao<DynamicRelation, Long> {

  /**
   * 删除动态关联
   * 
   * @param dynId
   * @param psnId
   */
  public void deleteDynRelation(Long dynId, Long psnId) {
    String hql =
        " update  DynamicRelation    dr set  dr.dealStatus =99  where dr.id.dynId=:dynId and dr.id.receiver=:psnId  and dr.dealStatus=0 ";
    this.createQuery(hql).setParameter("dynId", dynId).setParameter("psnId", psnId).executeUpdate();
  }


  public DynamicRelation getDynRelation(DynamicRelationPk pk) {
    String hql = "from DynamicRelation dr where dr.id =:pk";
    return (DynamicRelation) super.createQuery(hql).setParameter("pk", pk).uniqueResult();
  }

}
