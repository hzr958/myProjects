package com.smate.sie.core.base.utils.dao.pub;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.pub.Sie6PublicationList;
import org.springframework.stereotype.Repository;

/**
 * 成果收录情况DAO.
 * 
 * @author zll
 * 
 */
@Repository
public class Sie6PublicationListDao extends SieHibernateDao<Sie6PublicationList, Long> {

  /**
   * 更新pubid查重成果的收录情况.
   * 
   * @param pubId
   * @return
   */
  public Sie6PublicationList getPublicationList(Long pubId) {
    return super.findUnique(" from PublicationList t where t.id=?", pubId);
  }

}
