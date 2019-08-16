package com.smate.web.psn.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.profile.PsnUpdateDiscLog;

/**
 * 新增研究领域日志，用于更新研究领域后，发邮件通知好友
 */
@Repository
public class PsnUpdateDiscLogDao extends SnsHibernateDao<PsnUpdateDiscLog, Long> {

  public void savePsnUpdateDiscLog(PsnUpdateDiscLog log) {

    Long count =
        super.findUnique("select count(psnId) from PsnUpdateDiscLog where psnId = ? and keyWords = ? and status = 0",
            log.getPsnId(), log.getKeyWords());
    if (count == 0)
      super.save(log);
  }

}
