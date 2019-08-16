package com.smate.center.batch.dao.rol.pub;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.KpiRefreshPub;
import com.smate.core.base.utils.data.RolHibernateDao;



/**
 * 成果更新.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class KpiRefreshPubDao extends RolHibernateDao<KpiRefreshPub, Long> {

  /**
   * 加载需要更新的成果统计冗余.
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<KpiRefreshPub> loadNeedRefreshPubId(int maxSize) {

    String hql = "from KpiRefreshPub where lastDate < ? ";
    // 60秒之前
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.SECOND, -60);
    Date date = cal.getTime();
    Query queryResult = super.createQuery(hql, date);
    queryResult.setMaxResults(maxSize);

    return queryResult.list();
  }

  /**
   * 更新成果为删除状态.
   * 
   * @param pubId
   */
  public void updatePubDel(Long pubId) {
    String hql = "update KpiRefreshPub t set t.isDel = 1 where t.pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 删除记录.
   * 
   * @param pubId
   */
  public void remove(Long pubId) {
    String hql = "delete from KpiRefreshPub where pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }
}
