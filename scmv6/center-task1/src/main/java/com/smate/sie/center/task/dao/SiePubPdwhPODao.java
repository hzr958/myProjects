package com.smate.sie.center.task.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author sjzhou
 *
 */
@Repository
public class SiePubPdwhPODao extends PdwhHibernateDao<PubPdwhPO, Long> {

  /**
   * 获取基准库成果信息
   * 
   * @param size
   * @param updateTime
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubPdwhPO> getPdwhPublicationslist(Integer size, Date updateTime, Long insId) {
    String hql;
    if (null != updateTime) {
      hql =
          "from PubPdwhPO t where exists (select 1 from PdwhPubAddrInsRecord d where d.pubId=t.pubId and d.insId = ?) and t.gmtModified >= :updateTime and t.status = 0 order by t.gmtModified asc";
      return super.createQuery(hql, insId).setParameter("updateTime", updateTime).setMaxResults(size).list();
    } else {
      hql =
          "from PubPdwhPO t where exists (select 1 from PdwhPubAddrInsRecord d where d.pubId=t.pubId and d.insId = ?) and t.status = 0 order by t.gmtModified asc";
      return super.createQuery(hql, insId).setMaxResults(size).list();
    }
  }
}
