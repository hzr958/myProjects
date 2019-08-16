package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.SettingPubAssignCnkiPatScore;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * CnkiPat成果智能指派分数配置.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class SettingPubAssignCnkiPatScoreDao extends RolHibernateDao<SettingPubAssignCnkiPatScore, Long> {

  /**
   * 获取具体文献库的指派分数配置.
   * 
   * @param dbId
   * @return
   */
  public SettingPubAssignCnkiPatScore getSettingPubAssignScore(Long dbId) {
    String hql = "from SettingPubAssignCnkiPatScore where dbId  = ? ";
    List<SettingPubAssignCnkiPatScore> list = super.find(hql, dbId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
