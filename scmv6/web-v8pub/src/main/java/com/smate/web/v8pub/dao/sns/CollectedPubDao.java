package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.CollectedPub;

@Repository
public class CollectedPubDao extends SnsHibernateDao<CollectedPub, Long> {
  /**
   * 获取全部的收藏论文
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<CollectedPub> getCollectedPubs(Long psnId) {
    String hql = "from CollectedPub t where t.psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 根据pubDb获取收藏论文
   * 
   * @return
   */
  public List<CollectedPub> getColcPubsByPubDb(Long psnId, PubDbEnum pubDb) {
    String hql = "from CollectedPub t where t.psnId=:psnId and t.pubDb=:pubDb";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubDb", pubDb).list();
  }

  /**
   * 根据pubDb获取收藏论文Id
   * 
   * @return
   */
  public List<Long> getColcPubIdsByPubDb(Long psnId, PubDbEnum pubDb) {
    String hql = "select distinct t.pubId from CollectedPub t where t.psnId=:psnId and t.pubDb=:pubDb";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubDb", pubDb).list();
  }

  /**
   * 删除收藏的论文
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public void delCollectedPub(Long psnId, Long pubId, PubDbEnum pubDb) {
    String hql = "delete from CollectedPub t where t.psnId=:psnId and t.pubId=:pubId and t.pubDb=:pubDb";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).setParameter("pubDb", pubDb)
        .executeUpdate();
  }

  public boolean isCollectedPub(Long psnId, Long pubId, PubDbEnum pubDb) {
    String hql = "select count(t.id) from CollectedPub t where t.psnId=:psnId and t.pubId=:pubId and t.pubDb=:pubDb";
    long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId)
        .setParameter("pubDb", pubDb).uniqueResult();
    return count > 0 ? true : false;
  }

  /**
   * 获取个人收藏的基准库列表
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public List<CollectedPub> getCollectionPdwhs(Long psnId) {
    String hql = "from CollectedPub t where t.psnId=:psnId and pubDb=0";
    return super.createQuery(hql).setParameter("psnId", psnId).list();

  }

}
