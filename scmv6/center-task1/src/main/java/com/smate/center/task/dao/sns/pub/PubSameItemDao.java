package com.smate.center.task.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.PubSameItem;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * @author YJ
 *
 */
@Repository
public class PubSameItemDao extends SnsHibernateDao<PubSameItem, Long> {

  /**
   * 获取重复的成果列表
   * 
   * @param recordId 组id
   * @param psnId 人员id
   * @param pubId 成果id
   * @return 重复的成果列表
   */
  @SuppressWarnings("unchecked")
  public List<PubSameItem> getPubSameItems(Long recordId, Long psnId) {
    String hql = "from PubSameItem p where p.psnId =: psnId and p.recordId =: recordId  order by p.createDate desc";
    return this.createQuery(hql).setParameter("recordId", recordId).list();
  }

  /**
   * 根据recordId删除记录
   * 
   * @param recordId
   * 
   */
  public void deleteItemByRecordId(Long recordId) {
    String hql = "delete from PubSameItem p where p.recordId=:recordId";
    super.createQuery(hql).setParameter("recordId", recordId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PubSameItem> getPsnItemInfoByPubId(Long pubId, Long psnId) {
    String hql = "from PubSameItem p where p.pubId=:pubId and p.psnId=:psnId";
    return (List<PubSameItem>) this.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId", psnId).list();
  }

}
