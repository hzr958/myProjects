package com.smate.center.task.dao.sns.pub;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.pub.PubSameRecord;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubSameRecordDao extends SnsHibernateDao<PubSameRecord, Long> {
  /**
   * 通过psnId获取重复成果数量
   * 
   * @param userId
   * @return
   */
  public Long findPubSameRecordCount(Long userId) {
    String hql = " select count(*) from PubSameRecord t where t.psnId=:psnId ";
    return (Long) super.createQuery(hql).setParameter("psnId", userId).uniqueResult();
  }

  /**
   * 根据人员id获取其所有的组id
   * 
   * @param psnId 人员id
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubSameRecord> getPubSameRecords(Long psnId) {
    String hql = "from PubSameRecord p where p.psnId := psnId order by p.createDate desc,p.recordId desc";
    return this.createQuery(hql.toString()).setParameter("psnId", psnId).list();
  }

  public Date getSameRecNewestUpdateDate(Long psnId) {
    String hql =
        " select p.updateDate from PubSameRecord p where p.psnId =:psnId order by p.updateDate desc nulls last";
    List list = this.createQuery(hql.toString()).setParameter("psnId", psnId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return (Date) list.get(0);
    }
    return null;
  }

  /**
   * 获取分组的最后更新时间
   * 
   * @param psnId
   * @param dupPubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public Date getUpdateDate(Long recordId) {
    String hql = "select t.updateDate from PubSameRecord t where t.recordId=:recordId ";
    return (Date) super.createQuery(hql).setParameter("recordId", recordId).uniqueResult();

  }

  public void deleteByRecordId(Long recordId) {
    String hql = "delete from PubSameRecord t where t.recordId=:recordId ";
    super.createQuery(hql).setParameter("recordId", recordId).executeUpdate();
  }

}
