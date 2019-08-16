package com.smate.web.psn.dao.autocomplete;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.autocomplete.AcPsnDisciplineKey;

/**
 * 个人关键词自动提示.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class AcPsnDisciplineKeyDao extends AutoCompleteDao<AcPsnDisciplineKey> {
  @SuppressWarnings("unchecked")
  public List<AcPsnDisciplineKey> getAcPsnDisciplineKey(String startWith, Long psnDisId, int size) throws DaoException {
    Query query = null;
    List<Object> params = new ArrayList<Object>();
    startWith = this.getQuery(startWith);
    StringBuffer hql = new StringBuffer();
    hql.append("from AcPsnDisciplineKey t where t.psnDisId=? and t.status=?");
    params.add(psnDisId);
    params.add(4);
    hql.append(" and lower(t.zhKeyWord) like ? order by t.zhKeyWord");
    params.add("%" + startWith + "%");
    query = super.createQuery(hql.toString(), params.toArray());
    query.setMaxResults(size);
    return query.list();
  }
}
