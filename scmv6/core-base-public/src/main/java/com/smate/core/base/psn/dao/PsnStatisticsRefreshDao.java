package com.smate.core.base.psn.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.PsnStatisticsRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员统计更新dao.
 * 
 * @author tsz
 *
 * @date 2018年9月19日
 */
@Repository
public class PsnStatisticsRefreshDao extends SnsHibernateDao<PsnStatisticsRefresh, Long> {
  /**
   * 更新刷新表记录.
   */
  public void getToRefresh() {
    String sql = "insert into v_psn_statistics_refresh select t.psn_id,0,sysdate,'' from person t where not exists "
        + "(select 1 from v_psn_statistics_refresh t1 where t1.psn_id=t.psn_id)";
    super.update(sql);
  }

  /**
   * 获取待更新的数据.
   * 
   * @param size not null
   * @return
   */
  public List<PsnStatisticsRefresh> getToBeRefresh(Long startId, int size) {
    String hql = "from PsnStatisticsRefresh t where t.psnId>:startId and t.status=0 order by t.psnId asc";
    List<PsnStatisticsRefresh> list = super.createQuery(hql).setParameter("startId", startId).setMaxResults(200).list();
    return list;
  }
}
