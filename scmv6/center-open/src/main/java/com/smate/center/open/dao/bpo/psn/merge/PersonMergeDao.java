package com.smate.center.open.dao.bpo.psn.merge;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.bpo.psn.merge.PersonMerge;
import com.smate.core.base.utils.data.BpoHibernateDao;

/**
 * 人员合并
 * 
 * @author tsz
 *
 */
@Repository
public class PersonMergeDao extends BpoHibernateDao<PersonMerge, Long> {

  /**
   * 根据删除人员 获取合并记录
   * 
   * @param delPsnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public PersonMerge getPersonMerge(Long delPsnId) throws OpenException {
    String hql = "from PersonMerge t where t.delPsnId=:delPsnId and t.status=1";
    List<PersonMerge> list = super.createQuery(hql).setParameter("delPsnId", delPsnId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
