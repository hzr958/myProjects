package com.smate.web.v8pub.dao.repeatpub;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.repeatpub.PubSameRecordPO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PubSameRecordDAO extends SnsHibernateDao<PubSameRecordPO, Long> {
  /**
   * 通过psnId获取重复成果的组数
   * 
   * @param userId
   * @return
   */
  public Long findPubSameRecordCount(Long userId) {
    String hql = " select count(*) from PubSameRecordPO t where  t.dealStatus = 0 and t.psnId=:psnId  ";
    return (Long) super.createQuery(hql).setParameter("psnId", userId).uniqueResult();
  }

  /**
   * 根据人员id获取其所有的组id
   * 
   * @param psnId 人员id
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPubSameRecords(Long psnId) {
    String hql =
        "select p.recordId from PubSameRecordPO p where p.dealStatus = 0 and p.psnId =:psnId order by p.updateDate desc,p.recordId desc";
    return this.createQuery(hql.toString()).setParameter("psnId", psnId).list();
  }

}
