package com.smate.center.oauth.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.exception.DaoException;
import com.smate.center.oauth.model.pub.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果、文献DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PublicationDao extends SnsHibernateDao<Publication, Long> {

  /**
   * 
   * @author liangguokeng
   */
  @SuppressWarnings("unchecked")
  public List<Publication> findPubIdsByPsnId(Long psnId) throws DaoException {
    String hql = "from Publication t where t.status=0 and t.articleType=1 and t.psnId=? ";
    return super.createQuery(hql, psnId).list();
  }

}
