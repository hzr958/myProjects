package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.smate.center.task.model.pdwh.pub.PdwhReadStatistics;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库阅读记录DAO
 *
 * @author wsn
 * @createTime 2017年7月24日 上午1:15:38
 *
 */
@Repository
public class PdwhReadStatisticsDao extends PdwhHibernateDao<PdwhReadStatistics, Long> {
  /**
   * 查找访问记录
   * 
   * @param psnId
   * @param readPsnId
   * @param actionKey
   * @param actionType
   * @param formateDate
   * @return @
   */
  public PdwhReadStatistics findReadRecord(Long psnId, Long actionKey, Integer actionType, Long formateDate,
      String ip) {
    String hql =
        "from PdwhReadStatistics t where t.psnId = ? and t.actionKey = ? and t.actionType = ? and t.formateDate = ? and ";
    List<PdwhReadStatistics> list = null;
    if (ip != null) {
      hql += " t.ip = ? ";
      list = super.createQuery(hql, psnId, actionKey, actionType, formateDate, ip).list();
    } else {
      hql += " t.ip is null ";
      list = super.createQuery(hql, psnId, actionKey, actionType, formateDate).list();
    }

    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public List<PdwhReadStatistics> listByPdwhPubId(Long pdwhPubId) {
    String hql = "from PdwhReadStatistics t where t.actionKey=:pdwhPubId and t.actionType = 1";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  public Long getReadCount(Long pdwhPubId) {
    String hql = "select count(t.id) from PdwhReadStatistics t where t.actionKey=:pdwhPubId and t.actionType = 1";
    return (Long) this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }
}
