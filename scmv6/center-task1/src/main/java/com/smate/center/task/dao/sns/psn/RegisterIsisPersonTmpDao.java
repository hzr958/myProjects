package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.psn.RegisterIsisPersonTmp;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * isis注册人员Dao
 * 
 * @author zzx
 *
 */
@Repository
public class RegisterIsisPersonTmpDao extends SnsHibernateDao<RegisterIsisPersonTmp, Long> {
  public List<RegisterIsisPersonTmp> getList(Integer batchSize) {
    String hql = "from RegisterIsisPersonTmp t where t.dealStatus = 0";
    return this.createQuery(hql).setMaxResults(batchSize).list();
  }
}
