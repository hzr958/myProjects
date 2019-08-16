package com.smate.center.open.dao.psn.sysch;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.third.psn.ThirdPsnInfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 保存第三方人员的dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class ThirdPersonDao extends SnsHibernateDao<ThirdPsnInfo, Long> {

  public Long getId(Long psnId, String fromSys) {
    String hql = "select t.id  from ThirdPsnInfo  t where t.psnId=:psnId  and t.fromSys=:fromSys ";
    Long id = (Long) this.createQuery(hql).setParameter("psnId", psnId).setParameter("fromSys", fromSys).uniqueResult();
    if (id != null) {
      return id;
    }
    return null;

  }

}
