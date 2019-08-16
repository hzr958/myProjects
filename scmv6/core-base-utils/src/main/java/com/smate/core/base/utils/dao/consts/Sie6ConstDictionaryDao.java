package com.smate.core.base.utils.dao.consts;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.data.SieHibernateDao;

/**
 * 数据字典表子表
 * 
 * @author hd
 *
 */
@Repository
public class Sie6ConstDictionaryDao extends SieHibernateDao<ConstDictionary, Long> {

  /**
   * 取得某类别的代码列表.
   * 
   * @param category
   * @return
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

  /**
   * 取得某类别的代码列表.
   * 
   * @param category
   * @return
   */
  @SuppressWarnings("unchecked")
  public ConstDictionary findConstByCategoryAndCode(String category, String name) {
    List<Object> params = new ArrayList<Object>();
    String hql = "from ConstDictionary where key.category =  ? and (zhCnName = ? or enUsName = ?) order by seqNo ";
    params.add(category);
    params.add(name);
    params.add(name);
    List<ConstDictionary> items = null;
    Query query = createQuery(hql, params.toArray());
    query.setCacheable(true);
    items = query.list();
    if (items != null && items.size() > 0) {
      return items.get(0);
    } else {
      return null;
    }

  }

  /**
   * 取得某类别某代码的数据字典中文名称
   * 
   * @param category
   * @param code
   * @return
   */
  public String findZhNameByCategoryAndCode(String category, String code) {
    String hql = "select zhCnName from ConstDictionary where key.category =  ? and key.code = ?";
    Query query = createQuery(hql, category, code);
    return ObjectUtils.toString(query.uniqueResult());
  }

  /**
   * 取得某类别某代码的数据字典中文名称
   * 
   * @param category
   * @param code
   * @return
   */
  public String findCodeByCategoryAndName(String category, String zhCnName) {
    String hql = "select key.code from ConstDictionary where key.category =  ? and zhCnName = ?";
    Query query = createQuery(hql, category, zhCnName);
    return ObjectUtils.toString(query.uniqueResult());
  }
}
