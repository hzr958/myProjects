package com.smate.center.task.dyn.dao.base;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.dyn.model.base.DynamicRelation;
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
   */
  public void updateRelationStatus(Long dynId, Long producer, Long receiver, Integer status) {

  }

  /**
   * 屏蔽此类型的动态关联
   */
  public void updateRelationStatus(Long receiver, List<Integer> tmpList, List<Integer> dynTypeList, int visible) {

  }

}
