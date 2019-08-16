package com.smate.center.task.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubLikePO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果 赞dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubLikeDAO extends SnsHibernateDao<PubLikePO, Long> {

  public PubLikePO findByPubIdAndPsnId(Long pubId, Long psnId) {
    String hql = "from PubLikePO p where p.pubId =:pubId and p.psnId =:psnId";
    Object object = this.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId", psnId).uniqueResult();
    if (object != null) {
      return (PubLikePO) object;
    }
    return null;
  }

  /**
   * 查看当天psnA对某条成果的第一次点赞次数
   */
  public long getLikeCount(Long pubId, Long psnId) {
    String hql =
        "select count(*) from PubLikePO t where t.pubId=:pubId and t.psnId=:psnId and t.status=1 and to_char(t.gmtCreate,'YYYY-MM-dd')=to_char(sysdate,'YYYY-MM-dd') and to_char(t.gmtCreate,'YYYY-MM-dd hh24:mi:ss')=to_char(t.gmtModified,'YYYY-MM-dd hh24:mi:ss')";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 获取赞记录
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public PubLikePO getLikeRecord(Long pubId, Long psnId) {
    String hql = "from PubLikePO t where t.pubId=:pubId and t.psnId=:psnId";
    return (PubLikePO) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).uniqueResult();
  }

  public void deleteAll(Long pubId) {
    String hql = "delete from PubLikePO where pubId =:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  public Long countPubLiked(Long pubId) {
    String hql = "select count(1) from PubLikePO t where t.pubId=:pubId and t.status=1";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 获取赞记录
   * 
   * @param pdwhPubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubLikePO> findByPubId(Long pubId) {
    String hql = "from PubLikePO p where p.pubId =:pubId and p.status=1";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
