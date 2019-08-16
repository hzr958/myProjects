package com.smate.core.base.utils.dao.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;
import com.smate.core.base.utils.exception.SysDataException;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.cas.security.User;

@Repository
public class UserDao extends HibernateDao<User, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.CAS;
  }

  /**
   * 通过登录名,密码查找User类.
   * 
   * @param userName ，password
   * @return User
   */
  public User getUser(String userName, String password) {
    String hql = "from User t where  (lower(t.loginName)=? or t.mobileNumber=? )and lower(t.password) = ?";
    return super.findUnique(hql, userName.toLowerCase(), userName.toLowerCase(), password.toLowerCase());
  }

  /**
   * 通过登录名查找User类.
   * 
   * @param userName ，password
   * @return User
   */
  public User getUserByUsername(String userName) {

    String hql = "from User t where lower(t.loginName)=?";
    return super.findUnique(hql, userName.toLowerCase());

  }

  /**
   * 通过psnId查找User类.
   *
   * @param
   * @return User
   */
  public User getUserByPsnId(Long psnId, String password) {
    String hql = "from User t where  t.id=? and lower(t.password) = ?";
    return super.findUnique(hql, psnId, password.toLowerCase());

  }

  /**
   * 通过登录名查找User类.
   *
   * @param mobileNumber
   * @return User
   */
  public User getUserByMobile(String mobileNumber) {
    String hql = "from User t where lower(t.mobileNumber)=?";
    return super.findUnique(hql, mobileNumber.toLowerCase());

  }

  /**
   * 通过登录名查找ID.
   * 
   * @param loginName
   * @return Long
   */
  public Long findIdByLoginName(String loginName) {
    String queryString = "select id from User where lower(loginName) = ?";
    return findUnique(queryString, loginName.toLowerCase());
  }

  /**
   * 判断登录名是否被其他人占用.
   * 
   * @param email
   * @param extPsnId 排除自己 .
   * @return
   */
  public boolean isLoginNameUsed(String email, Long extPsnId) {
    email = email.toLowerCase();
    Long count =
        super.findUnique("select count(id) from User t where lower(t.loginName) = ? and t.id <> ? ", email, extPsnId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public List<User> findUserByLoginNameOrEmail(String email) {
    email = email.toLowerCase();
    String queryStr = "From User t where lower(t.loginName)=? or lower(t.email)=? order by t.id";
    return super.createQuery(queryStr, new Object[] {email, email}).list();

  }

  @SuppressWarnings("unchecked")
  public List<User> getUserNodeIds(String psnIds) throws SysDataException {
    String hql = "from  User t where t.id in ( " + psnIds + ")";
    return super.createQuery(hql, new Object[] {}).list();

  }

  /**
   * 通过userID字符串取得用户信息
   * 
   * @param userIdList
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<User> findUserByUserIdList(List<Long> userIdList) throws Exception {
    String hql = "from User u where u.id in(:userIds)";
    return super.createQuery(hql).setParameterList("userIds", userIdList).list();
  }

  public User findByLoginName(String loginName) {
    // 按小写进行检索登录名,登录名不区分大小写.统一按小写处理.
    String hql = "from User where lower(loginName)=?";
    return super.findUnique(hql, loginName.toLowerCase());
  }

  public String getLoginNameById(Long psnId) {
    String hql = "select t.loginName from User t where t.id = :psnId";
    return (String) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 匹配账号
   * 
   * @param page
   * @param searchString
   */
  public Page<Long> matchPsnByEmail(Page<Long> page, String searchString, List<Long> friendIds) {
    String countHql = "select count(id) ";
    String listHql = "select t.id ";
    String hql = " from User t where lower(t.loginName) like :searchString ";
    String excludeHql = " and t.id not in (:friendIds)";
    List<Long> psnIds = new ArrayList<Long>();
    Long totalCount = 0L;
    String searchParam = StringUtils.isNotBlank(searchString) ? ("%" + searchString.trim().toLowerCase() + "%") : "";
    if (CollectionUtils.isNotEmpty(friendIds)) {
      psnIds = super.createQuery(listHql + hql + excludeHql).setParameter("searchString", searchParam)
          .setParameterList("friendIds", friendIds).setFirstResult(page.getFirst() - 1)
          .setMaxResults(page.getPageSize()).list();
      totalCount = (Long) super.getSession().createQuery(countHql + hql + excludeHql)
          .setParameter("searchString", searchParam).setParameterList("friendIds", friendIds).uniqueResult();
    } else {
      psnIds = super.createQuery(listHql + hql).setParameter("searchString", searchParam)
          .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
      totalCount = (Long) super.getSession().createQuery(countHql + hql).setParameter("searchString", searchParam)
          .uniqueResult();
    }
    page.setTotalCount(totalCount);
    page.setResult(psnIds);
    return page;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdsByStart(Long lastPsnId) {
    String hql = "select t.id from User t where t.id > :lastPsnId order by t.id ";
    return super.createQuery(hql).setParameter("lastPsnId", lastPsnId).setMaxResults(500).list();
  }

  public User findUserByMobile(String mobileNumber) {
    if (StringUtils.isBlank(mobileNumber))
      return null;
    String hql = "from User t where t.mobileNumber = :mobileNumber";
    return (User) super.createQuery(hql).setParameter("mobileNumber", mobileNumber).uniqueResult();
  }
}
