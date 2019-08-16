package com.smate.web.management.service.mail;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.mail.connector.dao.MailContentDao;
import com.smate.center.mail.connector.mongodb.model.MailContent;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 邮件详情服务实现
 * 
 * @author zzx
 *
 */
@Service("mailDetailsService")
@Transactional(rollbackFor = Exception.class)
public class MailDetailsServiceImp implements MailDetailsService {
  @Autowired
  private MailContentDao mailContentDao;

  @Override
  public String findMailDetails(String des3MailId) {
    if (StringUtils.isNotBlank(des3MailId)) {
      Long mailId = Long.parseLong(Des3Utils.decodeFromDes3(des3MailId));
      MailContent content = mailContentDao.findById(mailId);
      if (content != null && StringUtils.isNotBlank(content.getContent())) {
        return content.getContent();
      }
    }
    return null;
  }

}
