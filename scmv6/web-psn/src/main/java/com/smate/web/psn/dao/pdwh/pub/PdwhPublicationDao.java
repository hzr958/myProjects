package com.smate.web.psn.dao.pdwh.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.pdwh.pub.PdwhPublication;

/**
 * 基准库成果dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPublicationDao extends PdwhHibernateDao<PdwhPublication, Long> {


  /**
   * 通过ID获取成果authorNameSpec.
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  public String getAuthorNameSpec(Long pubId) {
    String hql = "select t.authorNameSpec from PdwhPublication t where t.pubId=? ";
    return this.findUnique(hql, pubId);

  }

}
