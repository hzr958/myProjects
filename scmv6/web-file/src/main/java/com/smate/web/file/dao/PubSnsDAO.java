package com.smate.web.file.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.file.model.PubSnsPO;

/**
 * 个人成果基础信息查询DAO
 * 
 * @author houchuanjie
 * @date 2018/06/01 16:51
 */
@Repository
public class PubSnsDAO extends SnsHibernateDao<PubSnsPO, Long> {
  @SuppressWarnings("unchecked")
  public List<Long> getSnsPubList(List<Long> snsPubIds) {
    String hql = "select t.createPsnId from PubSnsPO  t where t.status = 0 and t.pubId in (:snsPubIds) ";
    return super.createQuery(hql).setParameterList("snsPubIds", snsPubIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<Long, Long>> getSnsPub(List<Long> snsPubIds) {
    String hql =
        "select new Map(t.createPsnId as createPsnId,t.pubId as pubId ) from PubSnsPO  t where t.status = 0 and t.pubId in (:snsPubIds) ";
    return super.createQuery(hql).setParameterList("snsPubIds", snsPubIds).list();
  }
}
