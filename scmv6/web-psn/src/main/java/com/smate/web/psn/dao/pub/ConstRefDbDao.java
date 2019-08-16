package com.smate.web.psn.dao.pub;

import java.util.List;
import java.util.Locale;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.exception.DataException;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.pub.ConstRefDb;


/**
 * 文献数据库定义表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class ConstRefDbDao extends SnsHibernateDao<ConstRefDb, Long> {

  /**
   * 获取所有数据列表.
   * 
   * @return
   * @throws DataException
   */
  @SuppressWarnings("unchecked")
  public List<ConstRefDb> getAllConstRefDb(Locale locale) throws DataException {
    String hql = "from ConstRefDb order by " + locale.getLanguage() + "SortKey asc";
    Query query = createQuery(hql);
    // 暂不使用缓存方便中对文献进行配置
    // query.setCacheable(true);
    return query.list();

  }

  public List<ConstRefDb> getPubRefDb(Locale locale) {
    String hql = "from ConstRefDb where isPublic=1  order by " + locale.getLanguage() + "SortKey asc";
    return find(hql);
  }
}
