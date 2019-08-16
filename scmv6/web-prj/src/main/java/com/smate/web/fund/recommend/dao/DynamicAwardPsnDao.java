package com.smate.web.fund.recommend.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.prj.model.common.DynamicAwardPsn;


/**
 * 人员赞记录Dao.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class DynamicAwardPsnDao extends SnsHibernateDao<DynamicAwardPsn, Long> {
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
    String hql = "from DynamicAwardPsn t where t.awardId=? and t.status=? order by t.awardDate desc";
    return super.createQuery(hql.toString(), new Object[] {awardId, 0}).setMaxResults(maxSize).list();
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
    String hql = "update DynamicAwardPsn t set t.status=? where t.awardId=? and t.awarderPsnId=?";
    super.createQuery(hql, new Object[] {status, awardId, awarderPsnId}).executeUpdate();
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
        "select count(t.recordId) from DynamicAwardPsn t where t.status=? and t.awardId=? and t.awarderPsnId=?";
    Long count = (Long) super.createQuery(hql, new Object[] {0, awardId, awarderPsnId}).uniqueResult();
    return count.intValue();
  }

  public DynamicAwardPsn getDynamicAwardPsn(Long awarderPsnId, Long awardId) {
    String hql = "from DynamicAwardPsn t where t.awardId=? and t.awarderPsnId=?";
    return super.findUnique(hql, new Object[] {awardId, awarderPsnId});
  }

  @SuppressWarnings("unchecked")
  public List<DynamicAwardPsn> queryDynAwardPsnByPsnId(Long psnId) {
    return super.createQuery("from DynamicAwardPsn t where t.awarderPsnId=?", new Object[] {psnId}).list();
  }

  public Long queryDynAwardPsnByAwardId(Long awardId) {
    return super.findUnique("select count(t.recordId) from DynamicAwardPsn t where t.status=? and t.awardId=?",
        new Object[] {0, awardId});
  }

  /**
   * 查询人员赞记录列表.
   * 
   * @param page
   * @param awardId
   * @return
   * @throws DaoException
   */
  public void getDynAwardPsns(Page<DynamicAwardPsn> page, Long awardId) {
    String hql = "from DynamicAwardPsn t where t.awardId=? and t.status=? order by t.awardDate desc";
    String countHql = "select count(t.recordId) ";
    Long totalCount = super.findUnique(countHql + hql, new Object[] {awardId, 0});
    page.setTotalCount(totalCount);
    Query query = super.createQuery(hql, new Object[] {awardId, 0});
    query.setFirstResult(page.getFirst() - 1);
    query.setMaxResults(page.getPageSize());
    List<DynamicAwardPsn> lst = query.list();
    page.setResult(lst);
  }

  /**
   * 删除赞记录
   * 
   * @param awardPsnId
   * @param awardId
   * @throws DaoException
   */
  public void deleteAwardPsn(Long awardPsnId, Long awardId, Date createDate) {
    String hql =
        "delete from DynamicAwardPsn t where t.awardId=? and t.awarderPsnId=? and to_char(awardDate,'yyyy/MM/dd')=to_char(?,'yyyy/MM/dd')";
    super.createQuery(hql, awardId, awardPsnId, createDate).executeUpdate();
  }
}
