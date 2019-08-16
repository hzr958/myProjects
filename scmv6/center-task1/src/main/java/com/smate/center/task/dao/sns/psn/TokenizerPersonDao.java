package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.psn.model.TokenizerPerson;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 
 * 邮件服务 persondao,只做查询
 * 
 * @author zk
 * 
 */
@Repository
public class TokenizerPersonDao extends SnsHibernateDao<TokenizerPerson, Long> {
  // 批量获取人员姓名
  @SuppressWarnings("unchecked")
  public List<String> findUserNameByBatchSize(Integer pageNo, int batchSize) {
    String hql = " select t.name from TokenizerPerson t order by t.personId asc";
    return super.createQuery(hql).setFirstResult((pageNo - 1) * batchSize).setMaxResults(batchSize).list();
  }
}
