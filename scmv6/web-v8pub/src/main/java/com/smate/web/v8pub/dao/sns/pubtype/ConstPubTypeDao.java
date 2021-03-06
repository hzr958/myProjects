package com.smate.web.v8pub.dao.sns.pubtype;

import java.util.List;
import java.util.Locale;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.pubType.ConstPubType;

/**
 * @author tsz 成果类型常数Dao.
 */
@Repository
public class ConstPubTypeDao extends SnsHibernateDao<ConstPubType, Integer> {

  /**
   * 读取所有Enabled=1的类别.
   * 
   * @return List<PublicationType>
   * @throws DaoException DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstPubType> getAllTypes(Locale locale) {

    Query query = super.createQuery("from ConstPubType t where enabled=1 order by t.seqNo");

    query.setCacheable(true);
    List<ConstPubType> list = query.list();

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (ConstPubType cr : list) {

        if ("zh".equals(locale.getLanguage())) {
          cr.setName(cr.getZhName());
        } else {
          cr.setName(cr.getEnName());
        }
      }
    }
    return query.list();

  }

  /**
   * 读取所有Enabled=1的类别,不包括 专利.
   * 
   * @return List<PublicationType>
   * @throws DaoException DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstPubType> getTypesExcludePat(Locale locale) {

    Query query = super.createQuery("from ConstPubType t where enabled=1  and t.id in (4,3,5,7) order by t.seqNo");
    query.setCacheable(true);
    List<ConstPubType> list = query.list();

    // 赋予正确的值给name属性
    if (list != null && list.size() > 0) {
      for (ConstPubType cr : list) {
        if ("zh".equals(locale.getLanguage()) || "zh_CN".equals(locale.getLanguage())) {
          cr.setName(cr.getZhName());
        } else {
          cr.setName(cr.getEnName());
        }
      }
    }
    return query.list();

  }

  public int getPubTypeNum(int pubType, int articleType, long psnId) {
    String hql =
        "select count(t.pubId) from PubSimple t where t.status = 0 and t.pubType = ? and t.articleType=? and t.ownerPsnId = ? ";
    Long count = super.findUnique(hql, pubType, articleType, psnId);
    return count.intValue();
  }

  public ConstPubType get(int id, Locale locale) {

    ConstPubType constPubType = super.get(id);

    if ("zh".equals(locale.getLanguage())) {
      constPubType.setName(constPubType.getZhName());
    } else {
      constPubType.setName(constPubType.getEnName());
    }
    return constPubType;
  }

  /**
   * 只读取英文名.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public ConstPubType get(int id) {

    ConstPubType constPubType = super.get(id);
    constPubType.setName(constPubType.getEnName());

    return constPubType;
  }

  /**
   * 删除所有数据，供数据同步时使用.
   * 
   * @throws DaoException
   */
  public void removeAll() {

    super.createQuery("delete from ConstPubType ").executeUpdate();
  }

  /**
   * 获取所有数据，直接查询数据库，未经缓存，同步数据时使用.
   * 
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<ConstPubType> getAllNoCacheConstPubType() {

    Query query = super.createQuery("from ConstPubType t ");
    query.setCacheMode(CacheMode.IGNORE);
    return query.list();
  }

}
