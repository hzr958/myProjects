package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.hibernate.Query;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.core.base.utils.common.ReflectionUtils;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 智能提示抽象父类.
 * 
 * @author liqinghua
 * 
 * @param <T>
 */
public abstract class AutoCompleteDao<T> extends SnsHibernateDao<T, Long> {

  protected Class<T> entityClass;

  public AutoCompleteDao() {

    this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
  }

  /**
   * 查询指定智能匹配前 N 条数据.
   * 
   * @param 检索的字符串
   * @param 提示10或者20个内容
   * @return 检索结果列表
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<T> getAcEntiy(String startStr, int size) throws DaoException {

    startStr = this.getQuery(startStr);
    Query query = super.createQuery(
        "from " + entityClass.getSimpleName() + " t where  t.query like ? order by t.query ", "%" + startStr + "%");
    query.setMaxResults(size);
    return query.list();
  }

  /**
   * 判断是否已经存在该智能提示信息.
   * 
   * @param 即将需要保存智能提示的值
   * @return
   * @throws DaoException
   */
  public boolean isExistQuery(String query) throws DaoException {

    String hql = "select count(code) from " + entityClass.getSimpleName() + " t where  t.query = ? ";
    Long count = findUnique(hql, query);
    if (count > 0)
      return true;
    return false;
  }

  /**
   * 将需要处理的智能提示字符串去掉空格并转换小写。.
   */
  public String getQuery(String name) {

    return name.replaceAll("\\s+", " ").trim().toLowerCase();
  }
}
