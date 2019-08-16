package com.smate.core.base.privacy.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.privacy.model.PrivacySettings;
import com.smate.core.base.privacy.model.PrivacySettingsPK;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.security.SecurityUtils;

@Repository
public class PrivacySettingsDao extends SnsHibernateDao<PrivacySettings, PrivacySettingsPK> {

  public Boolean isMyFriend(Long psnId, Long friendPsnId) {
    String sql = "select *  from psn_friend  where psn_id =:psnId  and friend_psn_id =:friendPsnId";
    List list = this.getSession().createSQLQuery(sql).setParameter("psnId", psnId)
        .setParameter("friendPsnId", friendPsnId).list();
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

  /**
   * 查询隐私类型设置
   */
  public PrivacySettings loadPsByPsnId(Long psnId, String privacyAction) {

    String hql = "From PrivacySettings ps where ps.pk.psnId=:psnId and ps.pk.privacyAction=:privacyAction";
    return (PrivacySettings) super.createQuery(hql.toString()).setParameter("psnId", psnId)
        .setParameter("privacyAction", privacyAction).uniqueResult();

  }

  /**
   * 根据隐私的条件过滤掉人员ID
   * 
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPrivacySettingByPsnIds(List<Long> psnIds, String privacyAction, Integer permission) {
    String hql =
        "select ps.pk.psnId from PrivacySettings ps where ps.pk.psnId in (:psnIds) and ps.pk.privacyAction=:privacyAction and ps.permission != :permission";
    return (List<Long>) super.createQuery(hql).setParameterList("psnIds", psnIds)
        .setParameter("privacyAction", privacyAction).setParameter("permission", permission).list();
  }

  /**
   * 根据隐私的条件获取人员ID
   * 
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdsByPrivacySetting(List<Long> psnIds, String privacyAction, Integer permission) {
    String hql =
        "select ps.pk.psnId from PrivacySettings ps where ps.pk.psnId in (:psnIds) and ps.pk.privacyAction=:privacyAction and ps.permission = :permission";
    return (List<Long>) super.createQuery(hql).setParameterList("psnIds", psnIds)
        .setParameter("privacyAction", privacyAction).setParameter("permission", permission).list();
  }

  @SuppressWarnings("unchecked")
  public boolean hasConfigPs() {

    String hql = "From PrivacySettings ps where ps.pk.psnId=?";
    List<PrivacySettings> list = super.createQuery(hql, new Object[] {SecurityUtils.getCurrentUserId()}).list();

    return (list != null && list.size() > 0);

  }

  @SuppressWarnings("unchecked")
  public List<PrivacySettings> loadPs() {

    String hql = "From PrivacySettings ps where ps.pk.psnId=?";

    return (List<PrivacySettings>) super.createQuery(hql, new Object[] {SecurityUtils.getCurrentUserId()}).list();

  }

  // oauth start

  @SuppressWarnings("unchecked")
  public boolean hasConfigPs(Long psnId) {

    String hql = "From PrivacySettings ps where ps.pk.psnId=?";
    List<PrivacySettings> list = super.createQuery(hql, new Object[] {psnId}).list();

    return (list != null && list.size() > 0);

  }

  // oauth end

  // pub start
  @SuppressWarnings("unchecked")
  public List<PrivacySettings> getPrivacySettings(Long psnId) {
    return super.createQuery("from PrivacySettings ps where ps.pk.psnId=?", psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PrivacySettings> getPsnsPs(String psnIds, String privacyAction) {

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
  // pub end

  // center batch start

  // center batch end
}
