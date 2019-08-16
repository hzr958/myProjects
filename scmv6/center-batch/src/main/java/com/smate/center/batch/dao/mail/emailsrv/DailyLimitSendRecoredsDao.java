package com.smate.center.batch.dao.mail.emailsrv;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.emailsrv.DailyLimitSendRecoreds;
import com.smate.center.batch.model.mail.emailsrv.DailyLimitSendRecoredsPk;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;


/**
 * 每日邮件限制发送日志Dao
 * 
 * @author zk
 *
 */
@Repository
public class DailyLimitSendRecoredsDao extends EmailSrvHibernateDao<DailyLimitSendRecoreds, DailyLimitSendRecoredsPk> {

  /**
   * 保存发送日志
   * 
   * @param recored
   */
  public void saveSendRecored(DailyLimitSendRecoreds recored) {
    super.save(recored);
  }

  /**
   * 判断今天是否已经发送过邮件
   * 
   * @param email
   * @param tempName
   * @return
   */
  public boolean isSendToday(String email, String tempName) {
    String hql =
        "from DailyLimitSendRecoreds r where r.pk.email = :email and r.pk.tempName = :tempName and to_char(r.sendDate,'yyyy-mm-dd') = to_char(sysdate,'yyyy-mm-dd')";
    Integer count =
        (Integer) super.createQuery(hql).setParameter("email", email).setParameter("tempName", tempName).list().size();
    if (count != null && count > 0) {
      return true;
    } else {
      return false;
    }
  }


}
