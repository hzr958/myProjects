package com.smate.web.dyn.dao.psn;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.psn.RecentSelected;

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

  /**
   * 更新联系时间
   * 
   * @param psnId
   * @param selectedPsnId
   */
  public void updateSelectedDate(Long psnId, Long selectedPsnId) {
    String hql =
        "update RecentSelected t set t.selectedDate=:selectedDate where t.psnId=:psnId and t.selectedPsnId =:selectedPsnId ";
    this.createQuery(hql).setParameter("selectedDate", new Date()).setParameter("psnId", psnId)
        .setParameter("selectedPsnId", selectedPsnId).executeUpdate();
  }
}
