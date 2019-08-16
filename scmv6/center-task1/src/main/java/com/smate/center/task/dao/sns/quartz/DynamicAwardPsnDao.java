package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.smate.center.task.model.sns.quartz.DynamicAwardPsn;
import com.smate.core.base.utils.data.SnsHibernateDao;


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
   * 获取成果人员赞记录（赞与取消赞的记录都需要）
   * 
   * @param awardId
   * @return
   */
  public List<DynamicAwardPsn> listDynamicAwardPsn(Long awardId) {
    String hql = "FROM DynamicAwardPsn t where t.awardId =:awardId";
    return this.createQuery(hql).setParameter("awardId", awardId).list();
  }

  /**
   * 获取成果人员赞的数量，排除掉取消赞的人员
   * 
   * @param awardId
   * @return
   */
  public Long getAwardCount(Long awardId) {
    String hql = "SELECT count(t.recordId) FROM DynamicAwardPsn t where t.awardId =:awardId and t.status= 1";
    return (Long) this.createQuery(hql).setParameter("awardId", awardId).uniqueResult();
  }
}
