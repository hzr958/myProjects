package com.smate.core.base.project.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.project.model.PrjPub;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 项目成果dao
 * 
 * @author yhx
 * @date 2019年8月5日
 *
 */
@Repository
public class PrjPubDao extends SnsHibernateDao<PrjPub, Serializable> {

  /**
   * 根据prjId、pubFrom获取pubIds
   * 
   * @param prjId
   * @param pubFrom
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPrjPubIdsByPrjId(Long prjId, Integer pubFrom) {
    String hql = "select distinct t.pubId from PrjPub t where t.prjId=:prjId and t.pubFrom=:pubFrom";
    return super.createQuery(hql).setParameter("prjId", prjId).setParameter("pubFrom", pubFrom).list();
  }

  public Long getPrjPubSum(Long prjId) {
    String hql = "select count(t.pubId) from PrjPub t where t.prjId=:prjId";
    return (Long) super.createQuery(hql).setParameter("prjId", prjId).uniqueResult();
  }

  public PrjPub getPrjPubByPrjIdAndPubId(Long prjId, Long pubId) {
    String hql = "from PrjPub t where t.prjId=:prjId and t.pubId=:pubId";
    return (PrjPub) super.createQuery(hql).setParameter("prjId", prjId).setParameter("pubId", pubId).uniqueResult();
  }
}
