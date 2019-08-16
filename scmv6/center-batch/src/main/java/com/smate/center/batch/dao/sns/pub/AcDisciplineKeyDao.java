package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.AcDisciplineKey;

/**
 * 关键词搜索自动提示.
 * 
 * @author chenxiangrong
 * 
 */
@Repository
public class AcDisciplineKeyDao extends AutoCompleteDao<AcDisciplineKey> {
  @SuppressWarnings("unchecked")
  public List<AcDisciplineKey> getAcDisciplineKey(String startWith, Long disId, Long psnId, int size)
      throws DaoException {
    Query query = null;
    startWith = this.getQuery(startWith);
    List<Object> params = new ArrayList<Object>();

    StringBuffer hql = new StringBuffer();
    hql.append("from AcDisciplineKey t where t.disId=?");
    params.add(disId);

    hql.append(" and (t.status not in(0,2) or (t.psnId=? and t.status<>2))");
    params.add(psnId);
    hql.append(" and lower(t.zhKeyWord) like ? order by t.zhKeyWord");
    params.add("%" + startWith + "%");

    query = super.createQuery(hql.toString(), params.toArray());
    query.setMaxResults(size);
    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<AcDisciplineKey> getAcDisciplineKey(String startWith, Long disId, int viewType, Long psnId, int size)
      throws DaoException {
    Query query = null;
    startWith = this.getQuery(startWith);
    List<Object> params = new ArrayList<Object>();

    StringBuffer hql = new StringBuffer();
    hql.append("from AcDisciplineKey t where t.disId=?");
    params.add(disId);
    if (viewType == 0) {// 全部
      hql.append(" and t.status<>?");
      params.add(5);
    } else if (viewType == 1) {// 已批准
      hql.append(" and t.status=?");
      params.add(1);
    } else if (viewType == 2) {// 已拒绝
      hql.append(" and t.status=?");
      params.add(2);
    }
    hql.append(" and lower(t.zhKeyWord) like ? order by t.zhKeyWord");
    params.add("%" + startWith + "%");

    query = super.createQuery(hql.toString(), params.toArray());
    query.setMaxResults(size);
    return query.list();
  }
}
