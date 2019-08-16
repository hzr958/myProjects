package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubLikePO;
import com.smate.web.v8pub.vo.pdwh.PdwhPubOperateVO;

/**
 * 成果 赞dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubLikeDAO extends PdwhHibernateDao<PdwhPubLikePO, Long> {

  public PdwhPubLikePO findByPubIdAndPsnId(Long pdwhPubId, Long psnId) {
    String hql = "from PdwhPubLikePO p where p.pdwhPubId =:pdwhPubId and p.psnId =:psnId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) ";
    Object object =
        this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId).uniqueResult();
    if (object != null) {
      return (PdwhPubLikePO) object;
    }
    return null;
  }

  /**
   * 用户赞/取消赞记录
   * 
   */
  public int isPdwhLike(PdwhPubOperateVO pdwhPubOperateVO) {
    String hql = "select count(t.likeId) from PdwhPubLikePO t where "
        + "t.pdwhPubId=:pdwhPubId and t.psnId=:psnId and t.status=:status "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) ";
    Long count = (Long) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubOperateVO.getPdwhPubId())
        .setParameter("psnId", pdwhPubOperateVO.getPsnId()).setParameter("status", pdwhPubOperateVO.getOperate())
        .uniqueResult();
    return count.intValue();
  }

  /**
   * 获取赞记录
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public long getLikeRecord(Long pdwhPubId, Long psnId) {
    String hql = "select count(*) from PdwhPubLikePO t where t.pdwhPubId=:pdwhPubId and t.psnId=:psnId and t.status=1 "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) ";
    return (Long) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId)
        .uniqueResult();
  }

  /**
   * 查看当天psnA对某条成果的第一次点赞次数
   */
  public long getLikeCount(Long pubId, Long psnId) {
    String hql = "select count(*) from PdwhPubLikePO t where t.pdwhPubId=:pubId and t.psnId=:psnId "
        + "and to_char(t.gmtModified,'YYYY-MM-dd')=to_char(sysdate,'YYYY-MM-dd') "
        + "and to_char(t.gmtCreate,'YYYY-MM-dd hh24:mi:ss')!=to_char(t.gmtModified,'YYYY-MM-dd hh24:mi:ss') "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) ";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubLikePO> findByPdwhPubId(Long pdwhPubId) {
    String hql = "from PdwhPubLikePO p where p.pdwhPubId=:pdwhPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId)"
        + " order by p.gmtCreate asc";
    List<PdwhPubLikePO> list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  /**
   * 获取点赞数
   * 
   * @param pdwhPubId
   * @return
   */
  public Long getLikeCountByPdwhPubId(Long pdwhPubId) {
    String hql = "select count(1) from PdwhPubLikePO t where t.pdwhPubId =:pdwhPubId and t.status=1"
        + " and exists (SELECT 1 FROM PubPdwhPO p where p.status = 0 and p.pubId = t.pdwhPubId) ";
    return (Long) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

}
