package com.smate.web.management.dao.other.fund;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.other.fund.ThirdSources;


@Repository
public class ThirdSourcesDao extends SnsHibernateDao<ThirdSources, Long> {

  /**
   * 获取可用来源系统连接.
   * 
   * @return
   */
  public List<ThirdSources> getSourcesList() {
    String hql = "from ThirdSources t where t.status=0";

    return super.createQuery(hql).list();
  }

  @SuppressWarnings("unchecked")
  public Long getAgentcyIdByToken(String token) {
    String hql = "select agencyId from ThirdSources t where t.token=:token";
    List<Long> list = this.createQuery(hql).setParameter("token", token).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
