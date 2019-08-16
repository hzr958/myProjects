package com.smate.center.task.v8pub.dao.sns;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.sns.po.PubCommentPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果评论dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubCommentDAO extends SnsHibernateDao<PubCommentPO, Long> {

  public Long countPubComment(Long pubId) {
    String hql = "select count(1) from PubCommentPO t where t.pubId=:pubId and t.status = 0";
    return (Long) this.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<PubCommentPO> findByPubId(Long pubId) {
    String hql = "from PubCommentPO p where p.pubId=:pubId and p.status = 0 order by p.gmtCreate asc";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;

  }

  public void deleteAll(Long pubId) {
    String hql = "delete from PubCommentPO where pubId =:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public PubCommentPO findComment(Long pubId, Long psnId, String content, Date gmtCreate) {
    String hql =
        "from PubCommentPO p where p.pubId=:pubId and p.psnId =:psnId and p.content=:content and p.gmtCreate = :gmtCreate";
    List list = this.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId", psnId)
        .setParameter("content", content).setParameter("gmtCreate", gmtCreate).list();
    if (list != null && list.size() > 0) {
      return (PubCommentPO) list.get(0);
    }
    return null;

  }
}
