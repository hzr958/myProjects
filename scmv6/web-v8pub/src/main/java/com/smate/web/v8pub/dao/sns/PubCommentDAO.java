package com.smate.web.v8pub.dao.sns;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubCommentPO;
import com.smate.web.v8pub.vo.PubCommentVO;

/**
 * 成果评论dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PubCommentDAO extends SnsHibernateDao<PubCommentPO, Long> {

  public List<PubCommentPO> findByPubId(Long pubId) throws ServiceException {
    String hql = "from PubCommentPO p where p.pubId=:pubId and p.status = 0 order by p.gmtCreate asc";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;

  }

  /**
   * 查看当天psnA对某条成果的评论次数
   */
  public Long getCommentCount(Long pubId, Long psnId) {
    String hql =
        "select count(*) from PubCommentPO p where  p.psnId=:psnId and p.pubId=:pubId and to_char(p.gmtCreate,'YYYY-MM-dd')=to_char(sysdate,'YYYY-MM-dd')";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PubCommentPO> queryComments(PubCommentVO pubCommentVO, Page page) {
    String hql = "from PubCommentPO t where t.pubId=:pubId and t.status=0 order by t.gmtCreate desc";
    String countHql = "select count(*) from PubCommentPO t where t.pubId=:pubId";
    Long totalCount = (Long) super.createQuery(countHql).setParameter("pubId", pubCommentVO.getPubId()).uniqueResult();
    page.setTotalCount(totalCount);
    return super.createQuery(hql).setParameter("pubId", pubCommentVO.getPubId())
        .setFirstResult((page.getPageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();

  }

  public Long getCommentsCount(Long pubId) {
    String hql = "select count (*) from PubCommentPO t where t.pubId=:pubId and t.status=0";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
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
