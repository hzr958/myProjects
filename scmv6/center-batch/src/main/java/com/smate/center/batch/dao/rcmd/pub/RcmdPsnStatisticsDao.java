package com.smate.center.batch.dao.rcmd.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rcmd.pub.RcmdPsnStatistics;
import com.smate.core.base.utils.data.RcmdHibernateDao;

/**
 * 人员信息统计Dao.
 * 
 * @author zk
 * 
 */
@Repository
public class RcmdPsnStatisticsDao extends RcmdHibernateDao<RcmdPsnStatistics, Long> {

  public RcmdPsnStatistics findByPsnId(Long psnId) {
    String hql = "from RcmdPsnStatistics t where t.psnId = ?";
    return (RcmdPsnStatistics) super.createQuery(hql, new Object[] {psnId}).uniqueResult();
  }

  /**
   * 将查询的列表封装成Map<便于用psnId查询>
   * 
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<Long, RcmdPsnStatistics> getPsnStatisticsListMap(List<Long> psnIds) {

    Map<Long, RcmdPsnStatistics> psMap = new HashMap<Long, RcmdPsnStatistics>();
    String hql = "from RcmdPsnStatistics p where p.psnId in (:psnIds)";
    List<RcmdPsnStatistics> psList = super.createQuery(hql).setParameterList("psnIds", psnIds).list();
    if (CollectionUtils.isNotEmpty(psList)) {
      for (RcmdPsnStatistics ps : psList) {
        psMap.put(ps.getPsnId(), ps);
      }
    }
    return psMap;
  }

  /**
   * 设置人员待认领成果数.
   * 
   * @param psnId
   * @param pubNum
   */
  public void setPsnPendingConfirmPubNum(Long psnId, Integer pubNum) {

  }
}
