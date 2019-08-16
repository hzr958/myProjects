package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PsnKwRmcRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PsnKwRmcRefreshDao extends SnsHibernateDao<PsnKwRmcRefresh, Long> {

  public List<Long> getRefreshData() {
    String hql = "select psnId from PsnKwRmcRefresh t where ((sysdate-t.mkAt)*24>5) and t.status=0";
    return super.createQuery(hql).list();
  }

  public void deleteByPsnid(Long psnId) {
    String hql = "delete from PsnKwRmcRefresh t where t.psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  public void updateByPsnId(Long psnId) {
    String hql = "update from PsnKwRmcRefresh t set t.status=9 where t.psnId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

}
