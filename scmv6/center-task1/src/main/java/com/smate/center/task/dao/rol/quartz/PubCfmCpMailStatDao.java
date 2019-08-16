package com.smate.center.task.dao.rol.quartz;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.rol.quartz.PubCfmCpMailStat;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果确认邮件通知合作者状态，用于记录用户最后确认成果时间.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubCfmCpMailStatDao extends RolHibernateDao<PubCfmCpMailStat, Long> {
  /**
   * 保存合作者邮件通知状态.
   * 
   * @param psnId
   */
  public void savePubCfmCpMailStat(Long psnId) {
    String hql = "from PubCfmCpMailStat t where t.psnId =:psnId";
    PubCfmCpMailStat stat = (PubCfmCpMailStat) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    if (stat == null) {
      stat = new PubCfmCpMailStat(psnId);
    } else {
      // 正在运行，忽略
      if (stat.getStatus() == 1) {
        return;
      } else if (stat.getStatus() == 9) {
        stat.setStatus(0);
        stat.setNum(0);
      }
      stat.setCfmAt(new Date());

    }
    super.save(stat);

  }

}
