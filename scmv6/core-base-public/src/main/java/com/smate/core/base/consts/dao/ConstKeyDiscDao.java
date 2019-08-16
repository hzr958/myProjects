package com.smate.core.base.consts.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.consts.model.ConstKeyDisc;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 关键字-学科 库dao
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class ConstKeyDiscDao extends SnsHibernateDao<ConstKeyDisc, Long> {
  /**
   * 
   * @return
   */
  public List<ConstKeyDisc> findKeys(String seachKey, Integer size) {
    if (size == null || size <= 0) {
      size = 5;
    }
    String hql =
        "from ConstKeyDisc t where instr(upper(t.keyWord),:seachKey) >0 order by instr(upper(t.keyWord),:seachKey) asc";
    String newSeachKey = seachKey.replaceAll("\'", "&#39;").toUpperCase().trim();
    return this.createQuery(hql)
        .setParameter("seachKey", seachKey == null ? "" : seachKey.replaceAll("\'", "&#39;").toUpperCase().trim())
        .setMaxResults(size).list();
  }
}
