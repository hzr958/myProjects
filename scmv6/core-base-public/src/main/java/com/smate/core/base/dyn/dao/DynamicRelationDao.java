package com.smate.core.base.dyn.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.dyn.model.DynamicRelation;
import com.smate.core.base.dyn.model.DynamicRelationPk;
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

  }

  /**
   * 屏蔽动态关联
   * 
   * @param dynId
   * @param psnId
   */
  public void shieldDynRelation(Long dynId, Long psnId) {
    String hql =
        " update  DynamicRelation    dr set  dr.dealStatus = 1 where dr.id.dynId=:dynId and dr.id.receiver=:psnId  and dr.dealStatus=0 ";
    this.createQuery(hql).setParameter("dynId", dynId).setParameter("psnId", psnId).executeUpdate();

  }

  /**
   * 屏蔽动态关联
   */
  public void updateRelationStatus(Long dynId, Long producer, Long receiver, Integer status) {

  }

  /**
   * 屏蔽此类型的动态关联
   */
  public void updateRelationStatus(Long receiver, List<Integer> tmpList, List<Integer> dynTypeList, int visible) {

  }

  public DynamicRelation getDynRelation(DynamicRelationPk pk) {
    String hql = "from DynamicRelation dr where dr.id =:pk";
    return (DynamicRelation) super.createQuery(hql).setParameter("pk", pk).uniqueResult();
  }

}
