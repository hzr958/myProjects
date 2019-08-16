package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubCommentPO;
import com.smate.web.v8pub.vo.PubCommentVO;

/**
 * 成果评论dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubCommentDAO extends PdwhHibernateDao<PdwhPubCommentPO, Long> {

  public List<PdwhPubCommentPO> findByPubId(Long pdwhPubId) throws ServiceException {
    String hql = "from PdwhPubCommentPO p where p.pdwhPubId=:pdwhPubId and p.status = 0 "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) "
        + " order by p.gmtCreate asc";
    List list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubCommentPO> queryComments(PubCommentVO pubCommentVO, Page page) {
    String hql = "from PdwhPubCommentPO t where t.pdwhPubId=:pdwhPubId and t.status=0 "
        + " and exists (SELECT 1 FROM PubPdwhPO p where p.status = 0 and p.pubId = t.pdwhPubId) "
        + " order by t.gmtCreate desc";
    String countHql = "select count(*) from PdwhPubCommentPO t where t.pdwhPubId=:pdwhPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO p where p.status = 0 and p.pubId = t.pdwhPubId) ";
    Long totalCount =
        (Long) super.createQuery(countHql).setParameter("pdwhPubId", pubCommentVO.getPubId()).uniqueResult();
    page.setTotalCount(totalCount);
    return super.createQuery(hql).setParameter("pdwhPubId", pubCommentVO.getPubId())
        .setFirstResult((page.getPageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();

  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubCommentPO> queryCommentsById(Long pubId, Page page) {
    String hql = "from PdwhPubCommentPO t where t.pdwhPubId=:pdwhPubId and t.status=0 "
        + " and exists (SELECT 1 FROM PubPdwhPO p where p.status = 0 and p.pubId = t.pdwhPubId) "
        + " order by t.gmtCreate desc";
    String countHql = "select count(*) from PdwhPubCommentPO t where t.pdwhPubId=:pdwhPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO p where p.status = 0 and p.pubId = t.pdwhPubId) ";
    Long totalCount = (Long) super.createQuery(countHql).setParameter("pdwhPubId", pubId).uniqueResult();
    page.setTotalCount(totalCount);
    return super.createQuery(hql).setParameter("pdwhPubId", pubId)
        .setFirstResult((page.getPageNo() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();

  }

  public Long getCommentsCount(Long pubId) {
    String hql = "select count (*) from PdwhPubCommentPO t where t.pdwhPubId=:pubId and t.status=0 "
        + " and exists (SELECT 1 FROM PubPdwhPO p where p.status = 0 and p.pubId = t.pdwhPubId) ";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 查看当天psnA对某条成果的评论次数
   */
  public Long getCommentCount(Long pubId, Long psnId) {
    String hql = "select count(*) from PdwhPubCommentPO p where  p.psnId=:psnId and p.pdwhPubId=:pubId "
        + "and to_char(p.gmtCreate,'YYYY-MM-dd')=to_char(sysdate,'YYYY-MM-dd') "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) ";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).uniqueResult();
  }

}
