package com.smate.center.open.service.email;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.service.local.SysUserLocaleService;
import com.smate.core.base.utils.model.security.Person;

/**
 * 科研之友发送邮件.
 * 
 * @author pwl
 * 
 */
@Service("scmEmailService")
@Transactional(rollbackFor = Exception.class)
public class ScmEmailServiceImpl implements ScmEmailService {

  /**
   * 
   */
  private static final long serialVersionUID = -9220348963099701903L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private static final String ENCODING = "utf-8";
  @Autowired
  private SysUserLocaleService sysUserLocaleService;

  /**
   * 获取用户邮件接收设置的语言环境.
   * 
   * @param person
   * @return
   * @throws ServiceException
   */
  @Override
  public String getReceiverLanguage(Person person) throws Exception {
    String language = person.getEmailLanguageVersion();
    if (StringUtils.isBlank(language)) {
      language = sysUserLocaleService.getLocale(person.getPersonId());
    }
    return language;
  }
}
