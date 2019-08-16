package com.smate.center.task.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubCommentPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果评论dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubCommentDAO extends PdwhHibernateDao<PdwhPubCommentPO, Long> {

  @SuppressWarnings("unchecked")
  public List<PdwhPubCommentPO> findByPubId(Long pdwhPubId) throws ServiceException {
    String hql = "from PdwhPubCommentPO p where p.pdwhPubId=:pdwhPubId and p.status = 0 order by p.gmtCreate asc";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  public Long getCommentsCount(Long pubId) {
    String hql = "select count (t.commentId) from PdwhPubCommentPO t where t.pdwhPubId=:pubId and t.status=0";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

}
