package com.smate.web.dyn.dao.pub;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.pub.PubLikePO;
import org.springframework.stereotype.Repository;

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
        "select count(*) from PubLikePO t where t.pubId=:pubId and t.psnId=:psnId and to_char(t.gmtCreate,'YYYY-MM-dd')=to_char(sysdate,'YYYY-MM-dd') and to_char(t.gmtCreate,'YYYY-MM-dd hh24:mi:ss')!=to_char(t.gmtModified,'YYYY-MM-dd hh24:mi:ss')";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 获取赞记录
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public long getLikeRecord(Long pubId, Long psnId) {
    String hql = "select count(*) from PubLikePO t where t.pubId=:pubId and t.psnId=:psnId and t.status=1";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).uniqueResult();
  }

}
