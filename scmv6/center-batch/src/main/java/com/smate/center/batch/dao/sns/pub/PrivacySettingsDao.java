package com.smate.center.batch.dao.sns.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.PrivacySettings;
import com.smate.center.batch.model.sns.pub.PrivacySettingsPK;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 动态消息添加Dao.
 * 
 * @author oyh
 * 
 */

@Repository
public class PrivacySettingsDao extends SnsHibernateDao<PrivacySettings, PrivacySettingsPK> {

  @SuppressWarnings("unchecked")
  public List<PrivacySettings> getPrivacySettings(Long psnId) throws DaoException {
    return super.createQuery("from PrivacySettings ps where ps.pk.psnId=?", psnId).list();
  }


  @SuppressWarnings("unchecked")
  public boolean hasConfigPs() throws DaoException {

    String hql = "From PrivacySettings ps where ps.pk.psnId=?";
    List<PrivacySettings> list = super.createQuery(hql, new Object[] {SecurityUtils.getCurrentUserId()}).list();

    return (list != null && list.size() > 0);

  }

  @SuppressWarnings("unchecked")
  public boolean hasConfigPs(Long psnId) throws DaoException {

    String hql = "From PrivacySettings ps where ps.pk.psnId=?";
    List<PrivacySettings> list = super.createQuery(hql, new Object[] {psnId}).list();

    return (list != null && list.size() > 0);

  }

  @SuppressWarnings("unchecked")
  public List<PrivacySettings> loadPs() throws DaoException {

    String hql = "From PrivacySettings ps where ps.pk.psnId=?";

    return (List<PrivacySettings>) super.createQuery(hql, new Object[] {SecurityUtils.getCurrentUserId()}).list();

  }

  @SuppressWarnings("unchecked")
  public PrivacySettings loadPsByPsnId(Long psnId, String privacyAction) throws DaoException {

    String hql = "From PrivacySettings ps where ps.pk.psnId=? and ps.pk.privacyAction=?";

    return (PrivacySettings) super.createQuery(hql, new Object[] {psnId, privacyAction}).uniqueResult();

  }

  @SuppressWarnings("unchecked")
  public List<PrivacySettings> getPsnsPs(String psnIds, String privacyAction) throws DaoException {

    StringBuffer sb = new StringBuffer();
    sb.append("From PrivacySettings ps where ps.pk.psnId in(").append(psnIds).append(")");
    sb.append(" and ps.k.privacyAction=?");

    return (List<PrivacySettings>) super.createQuery(sb.toString(), new Object[] {privacyAction}).list();

  }

  /**
   * 得到psnid的好友列表权限R
   * 
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<Long, Integer> getPrivacySettingForFrdList(List<Long> psnIds) {

    Map<Long, Integer> returnMap = new HashMap<Long, Integer>();
    String hql = " from PrivacySettings ps where ps.pk.psnId in (:psnIds) and ps.pk.privacyAction='vFrdList'";
    List<PrivacySettings> psList = super.createQuery(hql).setParameterList("psnIds", psnIds).list();
    if (CollectionUtils.isNotEmpty(psList)) {
      for (PrivacySettings ps : psList) {
        returnMap.put(ps.getPk().getPsnId(), ps.getPermission());
      }
    }
    return returnMap;
  }
}
