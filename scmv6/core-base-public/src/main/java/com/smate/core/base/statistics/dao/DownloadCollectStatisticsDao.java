package com.smate.core.base.statistics.dao;

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.smate.core.base.exception.DAOException;
import com.smate.core.base.statistics.model.DownloadCollectStatistics;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果下载或者收藏次数统计
 * 
 * @author Scy
 * 
 */
@Repository(value = "downloadCollectStatisticsDao")
public class DownloadCollectStatisticsDao extends SnsHibernateDao<DownloadCollectStatistics, Long> {

  /**
   * 查找下载或者收藏记录
   * 
   * @param psnId
   * @param actionKey
   * @param actionType
   * @param dcPsnId
   * @param formateDate
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public DownloadCollectStatistics findRecord(Long psnId, Long actionKey, Integer actionType, Long dcPsnId,
      Long formateDate) throws Exception {
    String hql =
        "from DownloadCollectStatistics t where t.psnId = ? and t.dcPsnId = ? and t.actionKey = ? and t.actionType = ? and t.formateDate = ?";
    List<DownloadCollectStatistics> ls =
        super.createQuery(hql, psnId, dcPsnId, actionKey, actionType, formateDate).list();
    if (ls != null && ls.size() > 0) {
      return ls.get(0);
    }
    return null;
  }

  /**
   * 获取某人成果上个月的下载数和收藏数 zk add.
   */
  public List findRecord(Integer size) throws DAOException {

    // String hql =
    // "select count(p.id) as count,p.psnId as psnId from DownloadCollectStatistics p where
    // p.dcdDate>=trunc(last_day(add_months(sysdate,-2))+1) and
    // p.dcdDate<trunc(last_day(add_months(sysdate,-1))+1) and p.dcount is not null group by p.psnId
    // having count(p.psnId)>=1";
    String hql =
        "select  sum(dcount) as count,p.psnId  as psnId from DownloadCollectStatistics p where trunc(p.dcdDate)>=trunc(sysdate-7) and p.dcount is not null  group by p.psnId having count(p.psnId)>=1";
    return super.createQuery(hql).setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP).setMaxResults(100)
        .setFirstResult(size * 100).list();
  }

  /**
   * 查看某个东西被下载了多少次
   * 
   * @param actionKey
   * @param actionType
   * @return
   * @throws Exception
   */
  public Long countDownloadByKey(Long actionKey, Integer actionType) throws Exception {
    String sql =
        "select sum(nvl(t.dcount,0)) from DownloadCollectStatistics t where t.actionKey = ? and t.actionType = ?";
    Long count = (Long) super.createQuery(sql, actionKey, actionType).uniqueResult();
    return count;
  }

  /**
   * 统计人员所有资源的下载数
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public Long countPsnResourceDownload(Long psnId) {
    String hql = "select sum(nvl(t.dcount,0)) from DownloadCollectStatistics t where t.psnId = :psnId ";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取某人成果所有下载和收藏数
   */
  public Long findPsnPubTotalNum(Long psnId) throws DAOException {
    return super.queryForLong(
        "select  sum(dcount)  from DOWNLOAD_COLLECT_STATISTICS t where t.PSN_ID = ? and t.dcount is not null",
        new Object[] {psnId});
  }
}
