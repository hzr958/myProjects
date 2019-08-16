package com.smate.center.task.dao.sns.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PubConfirmKnowFields;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果确认数据冗余表--只有部分字段，用于好友推荐的成果合作者.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubConfirmKnowFieldsDao extends SnsHibernateDao<PubConfirmKnowFields, Long> {
  public void deleteById(Long dtId) {
    String hql = "delete from PubConfirmKnowFields t where t.dtId=:dtId";
    super.createQuery(hql).setParameter("dtId", dtId).executeUpdate();
  }

}
