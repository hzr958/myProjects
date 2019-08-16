package com.smate.web.management.dao.institution.bpo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.data.BpoHibernateDao;

/**
 * 常量字典dao
 * 
 * @author zjh
 *
 */
@Repository
public class ConstDictionaryBpoDao extends BpoHibernateDao<ConstDictionary, Serializable> {
  /**
   * 取得某类别的代码列表.
   * 
   * @param category
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<ConstDictionary> findConstByCategory(String category) {
    String hql = "from ConstDictionary where key.category =  ? order by seqNo,code ";
    List<ConstDictionary> items = null;
    Query query = createQuery(hql, category);
    query.setCacheable(true);
    items = query.list();
    return items;
  }

}
