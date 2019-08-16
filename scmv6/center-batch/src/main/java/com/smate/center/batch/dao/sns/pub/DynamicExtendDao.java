package com.smate.center.batch.dao.sns.pub;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.DynamicExtend;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 动态扩展信息Dao.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class DynamicExtendDao extends SnsHibernateDao<DynamicExtend, Long> {

  @SuppressWarnings("unchecked")
  public List<DynamicExtend> getDynamicExtendList(Long dynId) throws DaoException {
    String hql = "from DynamicExtend t where t.dynId=? and t.firstFlag=?";
    return super.createQuery(hql, new Object[] {dynId, 0}).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map> getDynamicExtendList(int maxSize) throws DaoException {
    String hql = "select new map(t.dynId as dynId) from DynamicExtend t group by t.dynId";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }

  /**
   * 清理扩展信息.
   * 
   * @param dynId
   * @throws DaoException
   */
  public void deleteExtByDynId(Long dynId) throws DaoException {
    String hql = "delete from DynamicExtend t where t.dynId=?";
    super.createQuery(hql, dynId).executeUpdate();
  }

  /**
   * 查询某个动态的全部扩展信息.
   * 
   * @param dynId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<DynamicExtend> queryDynamicExtendByDynId(Long dynId) throws DaoException {
    return super.createQuery("from DynamicExtend t where t.dynId=?", new Object[] {dynId}).list();
  }

}
