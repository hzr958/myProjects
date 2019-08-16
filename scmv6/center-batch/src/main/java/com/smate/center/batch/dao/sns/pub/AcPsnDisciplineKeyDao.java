package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.AcPsnDisciplineKey;

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
