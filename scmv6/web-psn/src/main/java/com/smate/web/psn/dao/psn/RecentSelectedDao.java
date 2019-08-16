package com.smate.web.psn.dao.psn;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.psninfo.RecentSelected;

/**
 * 好友选择记录Dao
 * 
 * @author zzx
 *
 */
@Repository
public class RecentSelectedDao extends SnsHibernateDao<RecentSelected, Long> {
  /**
   * 根据psnId \selectedPsnId 获取RecentSelected 对象
   * 
   * @return
   */
  public RecentSelected getRecentSelected(Long psnId, Long selectedPsnId) {
    String hql = "from RecentSelected t where t.psnId=:psnId and t.selectedPsnId=:selectedPsnId ";
    return (RecentSelected) this.createQuery(hql).setParameter("psnId", psnId)
        .setParameter("selectedPsnId", selectedPsnId).uniqueResult();
  }
}
