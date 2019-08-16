package com.smate.center.task.dao.sns.pub;
/**
 * .
 * 
 * @author zzx
 * 
 */

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.CategoryMapScmIpc;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class CategoryMapScmIpcDao extends SnsHibernateDao<CategoryMapScmIpc, Long> {

  public List<Long> findCategoryIdByIpc(String ipc) {
    String hql = "select t.scmCategoryId from CategoryMapScmIpc t where t.IpcCode=:ipc";
    return this.createQuery(hql).setParameter("ipc", ipc).list();
  }

}
