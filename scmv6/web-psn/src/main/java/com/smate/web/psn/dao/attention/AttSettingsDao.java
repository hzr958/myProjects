package com.smate.web.psn.dao.attention;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.attention.AttSettings;


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

}
