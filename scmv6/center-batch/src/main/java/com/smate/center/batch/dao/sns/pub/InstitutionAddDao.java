package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.InstitutionAdd;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 单位数据层接口.
 * 
 * @author new
 * 
 */
@Repository
public class InstitutionAddDao extends SnsHibernateDao<InstitutionAdd, Long> {

  /**
   * 根据单位名称取得单位列表 (like ?).
   * 
   * @param name
   * @return List<InstitutionAdd>
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<InstitutionAdd> findListByName(String name) throws DaoException {
    if (StringUtils.isBlank(name)) {
      return null;
    }
    boolean isAesc = StringUtils.isAsciiPrintable(name);
    String hql = null;
    if (isAesc) {
      hql = "from InstitutionAdd where lower( enName ) like ?";
    } else {
      hql = "from InstitutionAdd where zhName like ?";
    }
    Query query = createQuery(hql, "%" + name.toLowerCase() + "%");
    query.setMaxResults(20);
    return query.list();
  }

  /**
   * 通过单位编号取得单位实体.
   * 
   * @param id
   * @return InstitutionAdd
   * @throws DaoException
   */
  public InstitutionAdd findById(Long id) throws DaoException {

    String hql = "from InstitutionAdd where id = ?";
    List<InstitutionAdd> list = super.find(hql, id);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;

  }

  /**
   * 通过单位名获取单位Id.
   * 
   * @param zhName
   * @param enName
   * @return Long
   * @throws DaoException
   */
  public Long getInsIdByName(String zhName, String enName) throws DaoException {

    return super.findUnique("select id from InstitutionAdd where zhName=? or enName=?", zhName, enName);

  }

  /**
   * 模糊匹配机构名称，获取机构ID列表.
   * 
   * @param name
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getInsIdsByName(String name) {

    String hql = "select id from InstitutionAdd where zhName like ? ";
    return super.createQuery(hql, "%" + name + "%").list();
  }

  /**
   * 模糊匹配机构名称，获取机构ID列表.
   * 
   * @param name
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InstitutionAdd> getInsListByName(String name, int size) {
    boolean isAesc = StringUtils.isAsciiPrintable(name);
    String hql = null;
    if (isAesc) {
      hql = "select new InstitutionAdd(id,zhName,enName) from InstitutionAdd where lower(enName) like ?";
    } else {
      hql = "select new InstitutionAdd(id,zhName,enName) from InstitutionAdd where zhName like ?";
    }
    Query query = createQuery(hql, name.toLowerCase() + "%");
    return query.setMaxResults(size).list();
  }

  /**
   * 匹配机构名称，获取机构ID.
   * 
   * @param name
   * @return
   */
  public Long getInsIdByName(String name) {

    String hql = "select id from InstitutionAdd where zhName = ? or  enName = ? ";
    List<Long> list = super.find(hql, name, name);
    if (list.size() == 0 || list.size() > 1) {
      return null;
    }
    return list.get(0);
  }
}
