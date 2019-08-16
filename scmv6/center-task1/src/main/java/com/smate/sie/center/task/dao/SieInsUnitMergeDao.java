package com.smate.sie.center.task.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieInsUnitMerge;

/**
 * 部门合并记录
 * 
 * @author ztg
 *
 */
@Repository
public class SieInsUnitMergeDao extends SieHibernateDao<SieInsUnitMerge, Long> {

  /**
   * 根据被合并部门id， 查询合并至的部门Id
   * 
   * @param beMergeUnitId
   * @return
   */
  public Long getMergeToUnitId(Long insId, Long beMergeUnitId) {
    String hql = "select t.unitBId from SieInsUnitMerge t where t.insId = ? and t.unitAId = ?";
    List<Object> params = new ArrayList<>();
    params.add(insId);
    params.add(beMergeUnitId);

    Query query = super.createQuery(hql, params.toArray());
    @SuppressWarnings("unchecked")
    List<Long> list = query.setMaxResults(1).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
