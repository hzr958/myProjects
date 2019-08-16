package com.smate.center.batch.dao.sns.prj;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.prj.PrjRelatedPubRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 项目相关成果刷新DAO.
 * 
 * @author xys
 * 
 */
@Repository
public class PrjRelatedPubRefreshDao extends SnsHibernateDao<PrjRelatedPubRefresh, Long> {

  /**
   * 批量获取待刷新记录.
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PrjRelatedPubRefresh> getPrjRelatedPubNeedRefreshBatch(int maxSize) {
    String hql = "from PrjRelatedPubRefresh t where t.status=0";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }

  /**
   * 根据项目id获取项目相关成果刷新记录.
   * 
   * @param prjId
   * @return
   */
  @SuppressWarnings("unchecked")
  public PrjRelatedPubRefresh getPrjRelatedPubRefreshByPrjId(Long prjId) {
    String hql = "from PrjRelatedPubRefresh t where t.prjId=?";
    List<PrjRelatedPubRefresh> list = super.createQuery(hql, prjId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 根据成果id获取项目相关成果刷新记录.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public PrjRelatedPubRefresh getPrjRelatedPubRefreshByPubId(Long pubId) {
    String hql = "from PrjRelatedPubRefresh t where t.pubId=?";
    List<PrjRelatedPubRefresh> list = super.createQuery(hql, pubId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }
}
