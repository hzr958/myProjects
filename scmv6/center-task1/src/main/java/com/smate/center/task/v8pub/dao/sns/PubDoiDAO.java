package com.smate.center.task.v8pub.dao.sns;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubDoiPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人库成果doi数据
 * 
 * @author YJ
 *
 *         2019年3月29日
 */
@Repository
public class PubDoiDAO extends SnsHibernateDao<PubDoiPO, Long> {

  public void deleteByPubId(Long pubId) {
    String hql = "delete from PubDoiPO t where t.pubId=:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

}
