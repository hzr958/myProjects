package com.smate.web.group.dao.grp.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.grp.pub.DynamicAwardPsn;

/**
 * 人员赞记录Dao.
 * 
 * @author ajb
 * 
 */
@Repository
public class DynamicAwardPsnDao extends SnsHibernateDao<DynamicAwardPsn, Long> {

  public DynamicAwardPsn getDynamicAwardPsn(Long awarderPsnId, Long awardId) {
    String hql =
        "from DynamicAwardPsn t where t.awardId=:awardId and t.awarderPsnId=:awarderPsnId order by t.awardDate desc ";
    return (DynamicAwardPsn) super.createQuery(hql).setParameter("awardId", awardId)
        .setParameter("awarderPsnId", awarderPsnId).setMaxResults(1).uniqueResult();
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
   * 查找人员赞过的记录条数
   * 
   * @param awarderPsnId
   * @param awardId
   * @return
   */
  public Long querySumOfPsnAward(Long awarderPsnId, Long awardId) {
    String hql =
        "select count(*) from DynamicAwardRes res, DynamicAwardPsn psn where psn.awardId = res.awardId and res.resId = :awardId and psn.awarderPsnId = :awarderPsnId";
    return (Long) super.createQuery(hql).setParameter("awardId", awardId).setParameter("awarderPsnId", awarderPsnId)
        .uniqueResult();
  }

  /**
   * 更新赞状态.
   * 
   * @param awarderPsnId
   * @param awardId
   * @param status
   * @throws DaoException
   */
  public void updateAwardStatus(Long awarderPsnId, Long awardId, int status) {
    String hql =
        "update DynamicAwardPsn t set t.status=:status where t.awardId=:awardId and t.awarderPsnId=:awarderPsnId";
    super.createQuery(hql).setParameter("status", status).setParameter("awardId", awardId)
        .setParameter("awarderPsnId", awarderPsnId).executeUpdate();
  }

  /**
   * 某人赞记录.
   * 
   * @param awarderPsnId
   * @param awardId
   * @return
   * @throws DaoException
   */
  public int awardByMe(Long awarderPsnId, Long awardId) {
    String hql =
        "select count(t.recordId) from DynamicAwardPsn t where t.status=0 and t.awardId=:awardId and t.awarderPsnId=:awarderPsnId";
    Long count = (Long) super.createQuery(hql).setParameter("awardId", awardId)
        .setParameter("awarderPsnId", awarderPsnId).uniqueResult();
    return count.intValue();
  }

}
