package com.smate.web.prj.dao.project;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.prj.model.common.SettingPrjForm;

/**
 * 成果录入模版配置表DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class SettingPrjFormDao extends SnsHibernateDao<SettingPrjForm, Integer> {

  /**
   * 传入单位ID获取单个SettingPrjForm实体，每个单位只能配置一个设置.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public SettingPrjForm getByInsId(Long insId) {

    return (SettingPrjForm) super.createQuery("from SettingPrjForm t where t.insId = ? ", new Object[] {insId})
        .uniqueResult();
  }

}
