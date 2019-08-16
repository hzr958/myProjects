package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.common.InsDataFrom;
import com.smate.center.task.model.common.InsDataFromId;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 单位数据来源Dao sns
 * 
 * @author hd
 *
 */
@Repository
public class InsDataFromSnsDao extends SnsHibernateDao<InsDataFrom, InsDataFromId> {

  @SuppressWarnings("unchecked")
  public InsDataFrom findByInsIdAndToken(Long insId, String token) {
    List<InsDataFrom> list = super.createQuery("from InsDataFrom t where t.id.token = :token and t.insId =:insId")
        .setParameter("insId", insId).setParameter("token", token).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
