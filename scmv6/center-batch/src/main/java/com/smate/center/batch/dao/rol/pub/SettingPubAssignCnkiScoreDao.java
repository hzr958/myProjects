package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.SettingPubAssignCnkiScore;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * Cnki成果智能指派分数配置.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class SettingPubAssignCnkiScoreDao extends RolHibernateDao<SettingPubAssignCnkiScore, Long> {

  /**
   * 获取具体文献库的指派分数配置.
   * 
   * @param dbId
   * @return
   */
  public SettingPubAssignCnkiScore getSettingPubAssignScore(Long dbId) {
    String hql = "from SettingPubAssignCnkiScore where dbId  = ? ";
    List<SettingPubAssignCnkiScore> list = super.find(hql, dbId);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }
}
