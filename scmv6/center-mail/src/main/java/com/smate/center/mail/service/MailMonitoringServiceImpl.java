package com.smate.center.mail.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.cache.MailCacheService;
import com.smate.center.mail.connector.dao.MailOriginalDataDao;
import com.smate.center.mail.connector.dao.MailTemplateDao;
import com.smate.center.mail.service.monitor.MailMonitorService;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;

/**
 * 邮件监控服务
 * 
 * @author zzx
 *
 */
@Service("mailMonitoringService")
@Transactional(rollbackFor = Exception.class)
public class MailMonitoringServiceImpl implements MailMonitoringService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailOriginalDataDao mailOriginalDataDao;
  @Autowired
  private MailMonitorService mailMonitorService;
  @Autowired
  private MailCacheService mailCacheService;
  @Autowired
  private MailTemplateDao mailTemplateDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private MailWhitelistService mailWhitelistService;

  @Override
  public void checkData1() throws Exception {
    List<Object[]> list = mailOriginalDataDao.findCheck1();
    if (list != null && list.size() > 0) {
      Long count = 0L;
      Long psnId = 0L;
      Integer templateCode = 0;
      String subject = "";
      String psnName = "";
      String email = "";
      for (Object[] one : list) {
        count = Long.parseLong(one[0].toString());
        psnId = Long.parseLong(one[1].toString());
        templateCode = Integer.parseInt(one[2].toString());
        subject = mailTemplateDao.get(templateCode).getSubject_zh();
        Person person = personDao.getPersonName(psnId);
        psnName = getPersonName(person);
        email = person == null ? "" : person.getEmail();
        if (!mailWhitelistService.isExistsWhitelist(email)) {
          logger
              .error(psnId + "(" + psnName + ")在一个小时内发送“" + subject + "（templateCode:" + templateCode + "）”的邮件已经超过20封");
          // 锁定账号，让该psnId,1小时内不能发送任何邮件，也不发送通知邮件
          if (StringUtils.isBlank(mailCacheService.getMonitorLock(psnId.toString() + templateCode.toString()))) {
            mailCacheService.monitorLock(psnId.toString() + templateCode.toString());
            mailMonitorService.sendMonitorMail("邮件发送系统异常【" + subject + "】",
                psnId + "(" + psnName + ")在一个小时内发送“" + subject + "（templateCode:" + templateCode + "）”的邮件已经超过20封,请处理!");
          }
        }
      }
    }
  }

  @Override
  public void checkData2() throws Exception {
    List<Object[]> list = mailOriginalDataDao.findCheck2();
    if (list != null && list.size() > 0) {
      Long count = 0L;
      String receiver = "";
      Integer templateCode = 0;
      String subject = "";
      for (Object[] one : list) {
        count = Long.parseLong(one[0].toString());
        receiver = one[1].toString();
        templateCode = Integer.parseInt(one[2].toString());
        subject = mailTemplateDao.get(templateCode).getSubject_zh();
        if (!mailWhitelistService.isExistsWhitelist(receiver)) {
          logger.error(receiver + "在一个小时内接收“" + subject + "（templateCode:" + templateCode + "）”的邮件已经超过20封");
          // 锁定该收件箱，1小时内不在接收任何邮件 也不发送通知邮件
          if (StringUtils.isBlank(mailCacheService.getMonitorLock(receiver + templateCode.toString()))) {
            mailCacheService.monitorLock(receiver + templateCode.toString());
            mailMonitorService.sendMonitorMail("邮件发送系统异常【" + subject + "】",
                receiver + "在一个小时内接收“" + subject + "（templateCode:" + templateCode + "）”的邮件已经超过20封,请处理!");
          }
        }

      }
    }

  }

  public String getPersonName(Person person) {
    String name = "";
    if (person != null) {
      if (StringUtils.isNotBlank(person.getName())) {
        name = person.getName();
      } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
        name = person.getFirstName() + " " + person.getLastName();
      } else {
        name = person.getEname();
      }
    }
    return name;
  }

}
