package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieDynMsg;

/**
 * 
 * @author yxy
 *
 */
@Repository
public class SieDynMsgDao extends SieHibernateDao<SieDynMsg, Long> {

  /**
   * 获取动态中的成果的数量
   */
  public Long getPubSize() {
    String hql = "select count(distinct t.resId) from SieDynMsg t where t.resType=1";
    return (Long) super.createQuery(hql).uniqueResult();
  };

  @SuppressWarnings("unchecked")
  public List<Long> getPubList() {
    String hql =
        "select distinct t.resId from SieDynMsg t where not exists( select 1 from SiePubSyncFulltextRefresh d where t.resId=d.snsPubId ) and t.resType=1";
    return super.createQuery(hql).list();
  }

  /*
   * 通过snsDynId获取机构个人动态
   */
  public SieDynMsg getBySnsDynId(Long dynId) {
    String hql = "from SieDynMsg t where t.snsDynId =:dynId";
    Object obj = super.createQuery(hql).setParameter("dynId", dynId).uniqueResult();
    if (null != obj) {
      SieDynMsg sieDynMsg = (SieDynMsg) obj;
      return sieDynMsg;
    }
    return null;
  }
}
