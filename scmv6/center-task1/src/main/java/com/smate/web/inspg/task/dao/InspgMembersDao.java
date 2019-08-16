package com.smate.web.inspg.task.dao;

import java.util.ArrayList;
import java.util.List;

import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.exception.RcmdTaskException;
import com.smate.web.inspg.task.model.InspgMembers;

/**
 * 机构成员实体类Dao for task
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class InspgMembersDao extends SnsHibernateDao<InspgMembers, Long> {

  /**
   * 得到比psnId大的List<Long> psnIdList，List大小为size
   * 
   * @param psnId
   */
  @SuppressWarnings("unchecked")
  public List<Long> getCandidateList(Long psnId, Integer size) throws RcmdTaskException {

    List<Long> psnIdList = new ArrayList<Long>();
    String hql = "select t.id from InspgMembers t where t.id > ?";
    psnIdList = super.createQuery(hql, psnId).setMaxResults(size).list();
    return psnIdList;
  }

}
