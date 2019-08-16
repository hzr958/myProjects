package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.pdwh.pub.PsnPubAllRelated;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 推荐出来的文献记录Dao
 * 
 * @author warrior
 * 
 */
@Repository
public class PsnPubAllRelatedDao extends PdwhHibernateDao<PsnPubAllRelated, Long> {

  /**
   * 根据人员删除推荐记录
   * 
   * @param psnId
   * @throws DaoException
   */
  public void delPsnPubAllRelated(Long psnId) {
    String hql = "delete from PsnPubAllRelated t where t.psnId = ?";
    super.createQuery(hql, psnId).executeUpdate();
  }

  /**
   * 计算推荐出来的文献，没有研究领域的个数
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long countPubNotDisid(Long psnId) throws DaoException {
    String hql =
        "select count(*) from PsnPubAllRelated t where t.psnId = ? and not exists (select b.id from PublicationAllDis b where b.pubAllId = t.pubAllId)";
    return (Long) super.createQuery(hql, psnId).uniqueResult();
  }

  /**
   * 
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnPubAllRelated> getPsnPubAll(Long psnId) throws DaoException {
    String hql = "from PsnPubAllRelated t where t.psnId = ? ";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 根据人员删除推荐记录
   * 
   * @param psnId
   * @throws DaoException
   */
  public void delPsnDis(Long psnId) throws DaoException {
    String sql = "delete from PSN_DIS t where t.psn_id = ?";
    super.update(sql, new Object[] {psnId});
  }

  public void delPsnPuballByPuballId(Long puballId) {
    String hql = "delete from PsnPubAllRelated where pubAllId=?";
    super.createQuery(hql, puballId).executeUpdate();
  }
}
