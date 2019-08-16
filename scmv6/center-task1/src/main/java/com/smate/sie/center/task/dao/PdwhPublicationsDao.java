package com.smate.sie.center.task.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhPublication;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author ztt
 *
 */
@Repository
public class PdwhPublicationsDao extends PdwhHibernateDao<PdwhPublication, Long> {

  /**
   * 获取基准库成果信息
   * 
   * @param size
   * @param updateTime
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PdwhPublication> getPdwhPublicationslist(Integer size, Date updateTime, Long insId) {
    String hql;
    if (null != updateTime) {
      hql =
          "from PdwhPublication t where exists (select 1 from PdwhPubAddrInsRecord d where d.pubId=t.pubId and d.insId = ?) and t.pubType in (2,3,4,5) and t.updateDate >= :updateTime order by t.updateDate asc";
      return super.createQuery(hql, insId).setParameter("updateTime", updateTime).setMaxResults(size).list();
    } else {
      hql =
          "from PdwhPublication t where exists (select 1 from PdwhPubAddrInsRecord d where d.pubId=t.pubId and d.insId = ?) and t.pubType in (2,3,4,5)  order by t.updateDate asc";
      return super.createQuery(hql, insId).setMaxResults(size).list();
    }
  }
}
