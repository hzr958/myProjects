package com.smate.center.batch.dao.dynamic;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.dynamic.DynamicSharePsn;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员分享Dao.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class DynamicSharePsnDao extends SnsHibernateDao<DynamicSharePsn, Long> {
  /**
   * 查询分享详情.
   * 
   * @param shareId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<DynamicSharePsn> getDynamicSharePsnsById(Long shareId) throws DaoException {
    String hql = "from DynamicSharePsn t where t.shareId=? order by t.shareDate desc";
    return super.createQuery(hql, shareId).list();
  }

  @SuppressWarnings("unchecked")
  public List<DynamicSharePsn> queryDynSharePsnByPsnId(Long psnId) throws DaoException {
    return super.createQuery("from DynamicSharePsn t where t.sharerPsnId=?", new Object[] {psnId}).list();
  }

}
