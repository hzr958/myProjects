package com.smate.center.task.dao.thirdparty;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.thirdparty.ThirdSourcesType;
import com.smate.core.base.utils.data.SnsHibernateDao;


@Repository
public class ThirdSourcesTypeDao extends SnsHibernateDao<ThirdSourcesType, Long> {

  /**
   * 获取可类型
   * 
   * @return
   */
  public List<ThirdSourcesType> getSourcesTypeListBySid(Long sourceId) {

    String hql = "from ThirdSourcesType t where t.status=0 and t.sourceId=:sourceId ";
    return super.createQuery(hql).setParameter("sourceId", sourceId).list();
  }

}
