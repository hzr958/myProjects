package com.smate.web.v8pub.dao.dynamic;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.dynamic.DynamicAwardPsn;

/**
 * 人员赞记录Dao.
 * 
 * @author ajb
 * 
 */
@Repository
public class DynamicAwardPsnDao extends SnsHibernateDao<DynamicAwardPsn, Long> {
  /**
   * 查看当天psnA对某条资源的评论次数
   */
  public int getAwardCount(Long resId, Long psnId, Integer resType) {
    String hql =
        "select p.recordId from DynamicAwardPsn p,DynamicAwardRes r where p.awardId=r.awardId and p.awarderPsnId=:psnId and r.resId=:resId and r.resType=:resType  and to_char(p.awardDate,'YYYY-MM-dd')=to_char(sysdate,'YYYY-MM-dd')";
    int count = super.createQuery(hql).setParameter("psnId", psnId).setParameter("resId", resId)
        .setParameter("resType", resType).list().size();
    return count;
  }

  public DynamicAwardPsn getDynamicAwardPsn(Long awarderPsnId, Long awardId) {
    String hql = "from DynamicAwardPsn t where t.awardId=:awardId and t.awarderPsnId=:awarderPsnId";
    return (DynamicAwardPsn) super.createQuery(hql).setParameter("awardId", awardId)
        .setParameter("awarderPsnId", awarderPsnId).uniqueResult();
  }

  /**
   * 查询人员赞记录.
   * 
   * @param resId
   * @param resType
   * @param resNode
   * @param maxSize
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<DynamicAwardPsn> getDynamicAwardPsns(Long awardId, int maxSize) {
    String hql = "from DynamicAwardPsn t where t.awardId=:awardId and t.status= 0 order by t.awardDate desc";
    return super.createQuery(hql.toString()).setParameter("awardId", awardId).setMaxResults(maxSize).list();
  }

  /**
   * 查找某人赞的记录.
   * 
   * @param awarderPsnId
   * @param awardId
   * @return
   * @throws DaoException
   */
  public int awardByMe(Long awarderPsnId, Long awardId) {
    String hql =
        "select count(t.recordId) from DynamicAwardPsn t where t.status=:status and t.awardId=:awardId and t.awarderPsnId=:awarderPsnId";
    Long count = (Long) super.createQuery(hql).setParameter("status", LikeStatusEnum.LIKE)
        .setParameter("awardId", awardId).setParameter("awarderPsnId", awarderPsnId).uniqueResult();
    return count.intValue();
  }
}
