package com.smate.center.task.dao.thirdparty;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.thirdparty.ThirdSources;
import com.smate.core.base.utils.data.SnsHibernateDao;


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

}
