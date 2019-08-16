package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.SettingPubAssignKwWt;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果智能指派关键词权重.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class SettingPubAssignKwWtDao extends RolHibernateDao<SettingPubAssignKwWt, Long> {

  /**
   * 获取所有指派关键词权重，按边界值降序.
   * 
   * @return
   */
  public List<SettingPubAssignKwWt> getSettingPubAssignKwWt() {
    String hql = "from SettingPubAssignKwWt order by bound desc";
    return super.find(hql);
  }
}
