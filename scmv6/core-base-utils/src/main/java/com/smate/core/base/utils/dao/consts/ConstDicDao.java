package com.smate.core.base.utils.dao.consts;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.model.consts.ConstDictionary2;

@Repository
public class ConstDicDao extends HibernateDao<ConstDictionary2, Long> {

  /**
   * 取得某类别的代码列表.
   * 
   * @param category
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstDictionary2> findConstByCategory(String category) {

    String hql = "from ConstDictionary2 where key.category =  ? order by seqNo,code ";
    List<ConstDictionary2> items = null;
    Query query = createQuery(hql, category);
    query.setCacheable(true);
    items = query.list();
    return items;
  }

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

}
