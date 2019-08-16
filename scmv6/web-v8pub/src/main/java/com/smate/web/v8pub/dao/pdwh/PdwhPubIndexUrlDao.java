package com.smate.web.v8pub.dao.pdwh;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubIndexUrl;

@Repository
public class PdwhPubIndexUrlDao extends PdwhHibernateDao<PdwhPubIndexUrl, Long> {


  @SuppressWarnings("unchecked")
  public List<PdwhPubIndexUrl> getByIds(List<Long> ids) {
    List<PdwhPubIndexUrl> list = new ArrayList<PdwhPubIndexUrl>();
    String hql = "from PdwhPubIndexUrl t where t.pubId "
        + "in(SELECT pp.pubId FROM PubPdwhPO pp where pp.status = 0 and pp.pubId in(:ids))";
    if (ids.size() > 1000) {
      for (int i = 0; i < ids.size() / 1000; i++) {
        list.addAll(this.createQuery(hql).setParameterList("ids", ids.subList(i * 1000, 1000 * (i + 1))).list());
      }
    } else {
      list = this.createQuery(hql).setParameterList("ids", ids).list();
    }
    return list;

  }

  /**
   * 获取成果短地址
   * 
   * @param pubId
   * @return
   */
  public String getIndexUrlByPubId(Long pubId) {
    String hql = "select t.pubIndexUrl from PdwhPubIndexUrl t where t.pubId =:pubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId) ";
    return (String) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }
}
