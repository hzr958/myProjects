package com.smate.center.task.single.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.single.model.rol.pub.PublicationRolPdwh;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果基准库id.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PublicationRolPdwhDao extends RolHibernateDao<PublicationRolPdwh, Long> {

  /**
   * 批量获取成果基准库ID信息.
   * 
   * @param lastId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PublicationRolPdwh> loadPubPdwhBatch(Long lastId) {
    String hql = "from PublicationRolPdwh t where t.pubId > ? order by t.pubId asc";
    return super.createQuery(hql, lastId).setMaxResults(100).list();
  }


  /**
   * 根据基准库成果ID查找出rol的成果ID
   * 
   * @param id
   * @param colomn
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> findRolPubIds(Long id, String column) throws DaoException {
    String hql = "select t.pubId from PublicationRolPdwh t where t." + column + "=?";
    return super.createQuery(hql, id).list();
  }

}
