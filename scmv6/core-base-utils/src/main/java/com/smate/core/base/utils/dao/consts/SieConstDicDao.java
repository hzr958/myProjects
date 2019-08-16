package com.smate.core.base.utils.dao.consts;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.data.SieHibernateDao;

@Repository
public class SieConstDicDao extends SieHibernateDao<ConstDictionary, Long> {
  /**
   * 取得某类别的代码列表.
   * 
   * @param category
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstDictionary> findConstByCategory(String category) {
    String hql = "from ConstDictionary where key.category = ? order by seqNo,code ";
    List<ConstDictionary> items = null;
    Query query = createQuery(hql, category);
    query.setCacheable(true);
    items = query.list();
    return items;
  }

  /**
   * @param category
   * @param code
   * @return
   * @throws DaoException
   */
  public ConstDictionary findConstByCategoryAndName(String category, String name) {

    List<ConstDictionary> list =
        super.find("from ConstDictionary where key.category =  ? and zhCnName = ?", category, name);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
