package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.SettingPubForm;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果录入模版配置表DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class SettingPubFormDao extends SnsHibernateDao<SettingPubForm, Integer> {

  /**
   * 传入单位ID获取单个SettingPubForm实体，每个单位只能配置一个设置.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public SettingPubForm getSettingPubFormByInsId(Long insId) {

    return (SettingPubForm) super.createQuery("from SettingPubForm t where t.insId = ? ", new Object[] {insId})
        .uniqueResult();
  }

}
