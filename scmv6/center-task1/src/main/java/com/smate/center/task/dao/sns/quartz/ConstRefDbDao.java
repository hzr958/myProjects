package com.smate.center.task.dao.sns.quartz;

import java.util.List;
import java.util.Locale;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.ConstRefDb;
import com.smate.core.base.utils.data.SnsHibernateDao;

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
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<ConstRefDb> getAllConstRefDb(Locale locale) {
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

  /**
   * 获取成果数据库列表.
   * 
   * @return
   */
  public List<ConstRefDb> getPublicationRefDb() {
    String hql = "from ConstRefDb where isPublic=1 and dbType like '%1%' order by zhSortKey asc";
    return find(hql);
  }

  /**
   * 获取指定ID的数据库列表.
   * 
   * @param dbIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstRefDb> getRefDb(List<Long> dbIds) {
    String hql = "from ConstRefDb t where t.id in(:dbIds)";

    return super.createQuery(hql).setParameterList("dbIds", dbIds).list();
  }

  /**
   * 获取单个指定ID的数据.
   * 
   * @param id
   * @return @
   */
  public ConstRefDb getConstRefDbById(Long id) {

    return super.get(id);
  }

  /**
   * 获取单个指定DbCode的数据.
   * 
   * @param id
   * @return @
   */
  public ConstRefDb getConstRefDbByCode(String dbCode) {

    return (ConstRefDb) super.createQuery("from ConstRefDb where upper(code) = ? ", dbCode.toUpperCase())
        .uniqueResult();
  }

  /**
   * 根据类型获取对应可用文献库.
   * 
   * @author fanzhiqiang
   * @param dbType 需要查找的文献库类型
   * @param language 当前登录的语言版本，以适应不同的语言显示文献库顺序不同
   * @return 文献库列表
   */
  @SuppressWarnings("unchecked")
  public List<ConstRefDb> getConstRefDbByType(String dbType, Locale locale) {
    String sql = " from ConstRefDb where db_type =? order by " + locale.getLanguage() + "SortKey";
    Query query = super.createQuery(sql, new Object[] {dbType});
    List<ConstRefDb> list = query.list();

    return list;
  }

  /**
   * 删除所有数据，供数据同步时使用.
   * 
   * @
   */
  public void removeAll() {

    super.createQuery("delete from ConstRefDb ").executeUpdate();
  }

  /**
   * 获取所有数据，直接查询数据库，未经缓存，同步数据时使用.
   * 
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<ConstRefDb> getAllNoCacheConstRefDb() {

    Query query = super.createQuery("from ConstRefDb t ");
    query.setCacheMode(CacheMode.IGNORE);
    return query.list();
  }

}
