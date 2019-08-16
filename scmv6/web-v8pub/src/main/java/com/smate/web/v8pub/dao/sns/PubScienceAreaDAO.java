package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.PubScienceAreaPO;

@Repository
public class PubScienceAreaDAO extends SnsHibernateDao<PubScienceAreaPO, Long> {

  /**
   * 通过pubId删除所有的科技领域
   * 
   * @param pubId
   */
  public void deleteById(Long pubId) {
    String hql = "delete from PubScienceAreaPO t where t.pubId =:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  /**
   * 通过pubId获取成果的科技领域
   * 
   * @param pubId
   */
  public List<PubScienceAreaPO> getPubAreaList(Long pubId) {
    String hql = "from PubScienceAreaPO t where pubId=:pubId";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
