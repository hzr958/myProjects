package com.smate.center.task.dao.fund.rcmd;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.fund.rcmd.ConstFundCategory;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * @author cwli
 * 
 */
@Repository
public class ConstFundCategoryDao extends RcmdHibernateDao<ConstFundCategory, Long> {

  @SuppressWarnings("unchecked")
  public List<ConstFundCategory> getFundList(Long lastId, Integer size) {

    String hql = "from ConstFundCategory t where t.id>:lastId and  t.insId = 0 order by t.id asc";
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(size).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getFundIds() {
    String hql =
        "select t.id from ConstFundCategory t where trunc(t.updateDate) >=trunc(sysdate-3)  and t.insType not in (:insType) order by t.id asc";
    return super.createQuery(hql).setParameter("insType", "1").list();
  }

  @SuppressWarnings("unchecked")
  public List<ConstFundCategory> getFundListInfo() {
    String hql =
        "from ConstFundCategory t where trunc(t.updateDate) >=trunc(sysdate-3)  and t.insType not in (:insType) order by t.id asc";
    return super.createQuery(hql).setParameter("insType", "1").list();
  }

}
