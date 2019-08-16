package com.smate.center.batch.dao.mail.emailsrv;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.mail.emailsrv.MailLogBigHis;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;


@Repository
public class MailLogBigHisDao extends EmailSrvHibernateDao<MailLogBigHis, Long> {

  /**
   * 清理指定mailid数据
   * 
   * @param midHisIds
   * @throws ServiceException
   */
  public void clearAssignId(List<Long> mailIdList) throws ServiceException {
    String hql = "delete from MailLogBigHis m where m.mailId in (:ids)";
    super.createQuery(hql).setParameterList("ids", mailIdList).executeUpdate();
  }
}
