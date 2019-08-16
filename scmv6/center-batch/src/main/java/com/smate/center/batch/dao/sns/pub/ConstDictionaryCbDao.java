package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.ConstDictionary;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 读取常量表DAO.
 * 
 * @author zb
 * 
 */
@Repository
public class ConstDictionaryCbDao extends SnsHibernateDao<ConstDictionary, Long> {

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

  @SuppressWarnings("unchecked")
  public List<ConstDictionary> findConstByCategory(String category, Integer size) {

    String hql = "from ConstDictionary where key.category =  ? order by seqNo asc ";
    List<ConstDictionary> items = null;

    Query query = createQuery(hql, category);
    if (size != null) {
      query.setMaxResults(size);
    }
    query.setCacheable(true);
    items = query.list();
    return items;
  }

  @SuppressWarnings("unchecked")
  public List<ConstDictionary> findConstCategoryWithCode(String category, String code, String excludes) {

    String hql = "from ConstDictionary where key.category =  ? and key.code>?  ";

    if (StringUtils.isNotEmpty(excludes)) {
      hql += " and key.code not in(" + excludes + ") order by seqNo asc";

    }
    List<ConstDictionary> items = null;

    Query query = createQuery(hql, category, code);
    query.setCacheable(true);
    items = query.list();
    return items;
  }

  /**
   * @param category
   * @param code
   * @return @
   */
  public ConstDictionary findConstByCategoryAndCode(String category, String code) {

    List<ConstDictionary> list =
        super.find("from ConstDictionary where key.category =  ? and key.code = ?", category, code);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 根据用户保存的日期常量编号和常量类型查询具体日期.
   * 
   * @param category
   * @param code
   * @return @
   */
  public Long findConstByCategoryId(String category, Long code) {

    if (null == code) {
      return null;
    }
    String codes = code.toString();
    List<ConstDictionary> consts = findConstByCategory(category);
    for (int i = 0; i < consts.size(); i++) {
      if (consts.get(i).getKey().getCode().equals(codes)) {
        code = Long.parseLong(consts.get(i).getZhCnName());
        break;
      }
    }
    return code;
  }

  public String findConstCodeByCaption(String caption, String queryStr) {

    boolean isEnglish = StringUtils.isAsciiPrintable(queryStr);
    String hql = "select key.code  from ConstDictionary cd where cd.key.category=? ";
    // 判断是否是非英文,查询本人数据
    if (isEnglish) {
      hql = " and cd.enUsName=? ";
      return (String) super.createQuery(hql, caption, queryStr).uniqueResult();
    } else {
      hql = "and (cd.zhCnName=? or cd.zhTwName=?) ";
      return (String) super.createQuery(hql, caption, queryStr, queryStr).uniqueResult();
    }
  }

  /**
   * 移除常量.
   * 
   * @param category
   * @param code
   * @return @
   */
  public Integer removeConstDictionary(String category, String code) {

    Integer i =
        super.createQuery("delete from ConstDictionary t where t.key.category = ? and t.key.code = ? ", category, code)
            .executeUpdate();
    return i;

  }

  /**
   * 移除所有常量.
   * 
   * @
   */
  public void removeAll() {

    super.createQuery("delete from ConstDictionary").executeUpdate();
  }

  /**
   * 获取所有常量，直接查询数据库，未经缓存，同步数据时使用.
   * 
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<ConstDictionary> getAllNoCacheConstDictionary() {

    Query query = super.createQuery("from ConstDictionary t ");
    query.setCacheMode(CacheMode.IGNORE);
    return query.list();
  }

  /**
   * 查找code通过名称.
   * 
   * @param category
   * @param name
   * @return
   */
  public String findCodeByName(String category, String name) {

    String hql =
        "from ConstDictionary t where t.key.category = ? and (t.enUsName = ? or t.zhCnName = ? or t.zhTwName = ?) ";

    List<ConstDictionary> list = super.find(hql, category, name, name, name);
    if (list == null || list.size() == 0)
      return null;
    return list.get(0).getKey().getCode();
  }

  public List<ConstDictionary> findConstByCategoryAndCodes(String category, String codes) {
    StringBuilder sb = new StringBuilder();
    sb.append("from ConstDictionary t where t.key.category = ? ");
    if (StringUtils.isNotBlank(codes)) {
      sb.append("and t.key.code in (" + codes + ")");
    }
    sb.append("order by t.seqNo asc");
    return super.find(sb.toString(), category);
  }
}
