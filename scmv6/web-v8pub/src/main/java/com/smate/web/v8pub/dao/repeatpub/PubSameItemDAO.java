package com.smate.web.v8pub.dao.repeatpub;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.repeatpub.PubSameItemPO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author YJ
 *
 */
@Repository
public class PubSameItemDAO extends SnsHibernateDao<PubSameItemPO, Long> {


  /**
   * 根据组id查找成果id的列表
   * 
   * @param recordId 组id
   * @param psnId 人员id
   * @return 成果id的列表
   */
  @SuppressWarnings("unchecked")
  public List<PubSameItemPO> getPubSameItems(Long recordId) {
    String hql = "from PubSameItemPO p where p.recordId =:recordId  order by p.updateDate desc,p.id desc";
    return this.createQuery(hql.toString()).setParameter("recordId", recordId).list();
  }

  /**
   * 获取未处理的重复的成果列表
   * 
   * @param recordId 组id
   * @param psnId 人员id
   * @param pubId 成果id
   * @return 重复的成果列表
   */
  @SuppressWarnings("unchecked")
  public List<PubSameItemPO> getNoDealPubSameItems(Long recordId, Long psnId, Long pubSameItemId) {
    String hql =
        "from PubSameItemPO p where p.id<>:pubSameItemId and p.dealStatus=0 and p.psnId =:psnId and p.recordId =:recordId ";
    return this.createQuery(hql.toString()).setParameter("pubSameItemId", pubSameItemId)
        .setParameter("recordId", recordId).setParameter("psnId", psnId).list();
  }

  /**
   * 未处理的设置为保留
   * 
   * @param recordId
   * @param psnId
   */
  public void setKeepAll(Long recordId, Long psnId) {
    String hql =
        "update PubSameItemPO p set p.dealStatus=1 where p.dealStatus=0 and p.psnId =:psnId and p.recordId =:recordId ";
    this.createQuery(hql).setParameter("recordId", recordId).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 未处理的设置为删除
   * 
   * @param recordId
   * @param psnId
   */
  public void setItemDel(Long recordId, Long psnId) {
    String hql =
        "update PubSameItemPO p set p.dealStatus=2 where p.dealStatus=0 and p.psnId =: psnId and p.recordId =: recordId ";
    this.createQuery(hql).setParameter("recordId", recordId).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 通过psnId和pubId获取重复成果列表
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubSameItemPO> getByPsnIdAndPubId(Long pubId, Long psnId) {
    String hql = "from PubSameItemPO p where p.dealStatus=0 and p.psnId =:psnId and p.pubId =:pubId";
    return this.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).list();
  }
}
