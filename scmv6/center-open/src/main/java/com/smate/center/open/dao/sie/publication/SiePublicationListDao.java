package com.smate.center.open.dao.sie.publication;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.sie.publication.SiePublicationList;
import com.smate.core.base.utils.data.RolHibernateDao;



/**
 * 成果收录情况DAO.
 * 
 * @author zll
 * 
 */
@Repository
public class SiePublicationListDao extends RolHibernateDao<SiePublicationList, Long> {

  /**
   * 更新pubid查重成果的收录情况.
   * 
   * @param pubId
   * @return
   */
  public SiePublicationList getPublicationList(Long pubId) {
    return super.findUnique(" from PublicationList t where t.id=?", pubId);
  }

}
