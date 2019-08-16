package com.smate.center.task.v8pub.dao.sns;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubPatentPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 基准库成果专利数据
 * 
 * @author YJ
 *
 *         2019年3月29日
 */
@Repository
public class PubPatentDAO extends SnsHibernateDao<PubPatentPO, Long> {

  public void deleteByPubId(Long pubId) {
    String hql = "delete from PubPatentPO t where t.pubId=:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }
}
