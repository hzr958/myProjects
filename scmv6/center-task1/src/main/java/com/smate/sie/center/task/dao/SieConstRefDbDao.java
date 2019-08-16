package com.smate.sie.center.task.dao;

import java.util.List;
import java.util.Locale;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.exception.DataException;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieConstRefDb;

/**
 * 文献数据库定义表.
 * 
 * @author jszhou
 *
 */
@Repository
public class SieConstRefDbDao extends SieHibernateDao<SieConstRefDb, Long> {

  /**
   * 获取所有数据列表.
   * 
   * @return
   * @throws DataException
   */
  @SuppressWarnings("unchecked")
  public List<SieConstRefDb> getAllConstRefDb(Locale locale) throws DataException {
    String hql = "from SieConstRefDb order by " + locale.getLanguage() + "SortKey asc";
    Query query = createQuery(hql);
    // 暂不使用缓存方便中对文献进行配置
    // query.setCacheable(true);
    return query.list();

  }

  public List<SieConstRefDb> getPubRefDb(Locale locale) {
    String hql = "from SieConstRefDb where isPublic=1  order by " + locale.getLanguage() + "SortKey asc";
    return find(hql);
  }

  /**
   * 获取成果数据库列表.
   * 
   * @return
   */
  public List<SieConstRefDb> getPublicationRefDb() {
    String hql = "from SieConstRefDb where isPublic=1 and dbType like '%1%' order by zhSortKey asc";
    return find(hql);
  }

  /**
   * 获取指定ID的数据库列表.
   * 
   * @param dbIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieConstRefDb> getRefDb(List<Long> dbIds) {
    String hql = "from SieConstRefDb t where t.id in(:dbIds)";

    return super.createQuery(hql).setParameterList("dbIds", dbIds).list();
  }

  /**
   * 获取单个指定ID的数据.
   * 
   * @param id
   * @return
   * @throws DataException
   */
  public SieConstRefDb getConstRefDbById(Long id) throws DataException {

    return super.get(id);
  }

  /**
   * 获取单个指定DbCode的数据.
   * 
   * @param id
   * @return
   * @throws DataException
   */
  public SieConstRefDb getConstRefDbByCode(String dbCode) throws DataException {

    return (SieConstRefDb) super.createQuery("from SieConstRefDb where upper(code) = ? ", dbCode.toUpperCase())
        .uniqueResult();
  }

  /**
   * 根据类型获取对应可用文献库.
   * 
   * @author jszhou
   * @param dbType 需要查找的文献库类型
   * @param language 当前登录的语言版本，以适应不同的语言显示文献库顺序不同
   * @return 文献库列表
   */
  @SuppressWarnings("unchecked")
  public List<SieConstRefDb> getConstRefDbByType(String dbType, Locale locale) throws DataException {
    String sql = " from SieConstRefDb where db_type =? order by " + locale.getLanguage() + "SortKey";
    Query query = super.createQuery(sql, new Object[] {dbType});
    List<SieConstRefDb> list = query.list();

    return list;
  }

  /**
   * 删除所有数据，供数据同步时使用.
   * 
   * @throws DataException
   */
  public void removeAll() throws DataException {

    super.createQuery("delete from SieConstRefDb ").executeUpdate();
  }

  /**
   * 获取所有数据，直接查询数据库，未经缓存，同步数据时使用.
   * 
   * @return
   * @throws DataException
   */
  @SuppressWarnings("unchecked")
  public List<SieConstRefDb> getAllNoCacheConstRefDb() throws DataException {

    Query query = super.createQuery("from SieConstRefDb t ");
    query.setCacheMode(CacheMode.IGNORE);
    return query.list();
  }

}
