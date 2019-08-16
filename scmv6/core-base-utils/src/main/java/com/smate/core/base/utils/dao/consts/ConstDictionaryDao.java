package com.smate.core.base.utils.dao.consts;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository("constDictionaryDao")
public class ConstDictionaryDao extends SnsHibernateDao<ConstDictionary, Long> {

  public List<ConstDictionary> findConstByCategoryAndCodes(String category, String codes) throws Exception {
    StringBuilder sb = new StringBuilder();
    sb.append("from ConstDictionary t where t.key.category = ? ");
    if (StringUtils.isNotBlank(codes)) {
      sb.append("and t.key.code in (" + codes + ")");
    }
    sb.append("order by t.seqNo asc");
    return super.find(sb.toString(), category);
  }

  /**
   * @param category
   * @param code
   * @return
   * @throws DaoException
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

  /**
   * 获取自动填充的学历
   * 
   * @param category
   * @param searchKey
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ConstDictionary> getAcDegree(String category, String searchKey) {
    String hql = "from ConstDictionary t where t.key.category =:category";
    String orderHql = " order by t.seqNo";
    Locale locale = LocaleContextHolder.getLocale();
    if ("en_US".equals(locale + "")) {
      hql += " and instr(upper(t.enUsName),:searchKey) >0";
    } else {
      hql += " and instr(upper(t.zhCnName),:searchKey) >0";
    }
    hql += orderHql;
    return super.createQuery(hql).setParameter("category", category)
        .setParameter("searchKey", searchKey == null ? "" : searchKey.replaceAll("\'", "&#39;").toUpperCase().trim())
        .list();
  }

  @SuppressWarnings("unchecked")
  public List<ConstDictionary> findConstByCategory(String category, Integer size) throws Exception {
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

}
