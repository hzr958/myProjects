package com.smate.center.batch.dao.sns.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.psn.AttSettings;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 人员关注设置dao
 * 
 * @author tsz
 *
 */
@Repository
public class AttSettingsDao extends SnsHibernateDao<AttSettings, Long> {

  @SuppressWarnings("unchecked")
  public List<AttSettings> loadAttSettingsByPsnId() throws DaoException {

    String hql = "From AttSettings where attSettingsId.psnId=? ";
    Long psnId = SecurityUtils.getCurrentUserId();
    return super.createQuery(hql, new Object[] {psnId}).list();

  }

  /**
   * 获取人员的关注点.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<AttSettings> getAttSettingsByPsnId(Long psnId) throws DaoException {
    String hql = "From AttSettings where attSettingsId.psnId=? ";
    return super.createQuery(hql, new Object[] {psnId}).list();
  }

  /**
   * 是否关注某项.
   * 
   * @param psnId
   * @param attId
   * @return
   * @throws DaoException
   */
  public int hasAttSettingsByPsnId(Long psnId, String attId) throws DaoException {
    String hql =
        "select count(attSettingsId.psnId) from AttSettings where attSettingsId.psnId=? and attSettingsId.attId=?";
    Object object = super.createQuery(hql, new Object[] {psnId, attId}).uniqueResult();
    return Integer.parseInt(object.toString());
  }

  public void delPsnAttSetting(String attId) throws DaoException {

    String hql = "Delete AttSettings where attSettingsId.psnId=? and attSettingsId.attId=?";
    createQuery(hql, new Object[] {SecurityUtils.getCurrentUserId(), attId}).executeUpdate();

  }

}
