package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.IsisMatchedPrjMembers;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * IsisPrjMemeberMatch任务 Dao
 * 
 * @author lj
 *
 */

@Repository
public class IsisPrjMemeberMatchDao extends SnsHibernateDao<IsisMatchedPrjMembers, Long> {

  /**
   * 根据姓名邮件查重
   * 
   * @param name
   * @param email
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<IsisMatchedPrjMembers> getMatchedByNameAndEmail(String name, String email) {
    String hql = " from IsisMatchedPrjMembers t where t.name =:name and t.email =:email";
    return super.createQuery(hql).setParameter("name", name).setParameter("email", email).list();
  }


}


