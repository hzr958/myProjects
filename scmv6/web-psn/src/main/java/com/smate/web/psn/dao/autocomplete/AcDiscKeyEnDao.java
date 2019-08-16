package com.smate.web.psn.dao.autocomplete;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.autocomplete.AcDiscKeyEn;


/**
 * 智能匹配关键词英文.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class AcDiscKeyEnDao extends AutoCompleteDao<AcDiscKeyEn> {
  /**
   * 匹配列表.
   * 
   * @param startWith
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<AcDiscKeyEn> getAcDiscKeyEn(String startWith, Long keyId, int size) throws DaoException {
    Query query = null;
    startWith = startWith.toLowerCase();
    query = super.createQuery("from AcDiscKeyEn t where  t.keyId=? and lower(t.disKeyEn) like ? order by t.disKeyEn ",
        new Object[] {keyId, "%" + startWith + "%"});

    query.setMaxResults(size);
    return query.list();
  }

}
