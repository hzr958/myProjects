package com.smate.web.management.dao.analysis.sns;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.DaoException;

/**
 * 成果、文献DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PublicationDao extends SnsHibernateDao<Publication, Long> {


  /**
   * 个人成果的总数.
   * 
   * @param psnId
   * @throws DaoException
   */
  public Long getTotalPubsByPsnId(Long psnId) throws Exception {
    Long count = super.findUnique(
        "select count(t.pubId) from Publication t where t.ownerPsnId=? and t.articleType = 1  and t.status=0",
        new Object[] {psnId});
    return count;
  }

}
