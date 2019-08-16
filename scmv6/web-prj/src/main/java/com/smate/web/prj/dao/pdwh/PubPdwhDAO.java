package com.smate.web.prj.dao.pdwh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.prj.model.pdwh.PubPdwhPO;

/**
 * @author houchuanjie
 * @date 2018/06/01 17:44
 */
@Repository
public class PubPdwhDAO extends PdwhHibernateDao<PubPdwhPO, Long> {

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> queryPubCountList(List<Long> pubIds) {
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    String includeType = " select new Map(nvl(t.pubType,7) as pubType,count(t.pubId) as count) ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from PubPdwhPO t where t.pubId in(:pubIds) and  t.status = 0");
    hql.append("  group by  nvl(t.pubType,7)  ");
    return this.createQuery(includeType + hql.toString(), params.toArray()).setParameterList("pubIds", pubIds).list();
  }

  public Long getPubCount(List<Long> pubPdwhIds) {
    String hql = "select count(pubId) from PubPdwhPO p where p.pubId in (:ids) and p.status = 0 ";
    return (Long) this.createQuery(hql).setParameterList("ids", pubPdwhIds).uniqueResult();
  }
}
