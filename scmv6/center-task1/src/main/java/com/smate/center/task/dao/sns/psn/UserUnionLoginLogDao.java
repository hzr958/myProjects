package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.UserUnionLoginLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class UserUnionLoginLogDao extends SnsHibernateDao<UserUnionLoginLog, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getTaskPsnList(Long lastPsnId, int type) {
    String hql = "select t.psnId from UserUnionLoginLog t where t.psnId > :lastPsnId";
    String listHql = "";
    if (1 == type) {// 登录过系统且关联过其他业务系统
      listHql = " and t.psnFundScore =5";
    } else if (2 == type) {// 未登录过系统但关联过其他业务系统
      listHql = " and t.psnFundScore =3";
    } else if (3 == type) {// 登录过系统未关联过其他业务系统
      listHql = " and t.psnFundScore =2";
    }
    String orderHql = " order by t.psnId";
    return super.createQuery(hql + listHql + orderHql).setParameter("lastPsnId", lastPsnId).setMaxResults(200).list();
  }

}
