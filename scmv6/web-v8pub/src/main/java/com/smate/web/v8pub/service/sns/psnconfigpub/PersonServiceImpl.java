package com.smate.web.v8pub.service.sns.psnconfigpub;

import com.smate.core.base.utils.string.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.v8pub.exception.ServiceException;

import java.util.Locale;

/**
 * 人员信息服务类
 * 
 * @author YJ
 *
 *         2018年9月17日
 */
@Service(value = "perSonService")
@Transactional(rollbackFor = Exception.class)
public class PersonServiceImpl implements PersonService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonDao personDao;

  @Override
  public Person getPersonName(Long psnId) throws ServiceException {
    try {
      return personDao.getPersonName(psnId);
    } catch (Exception e) {
      logger.error("出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String getName(Long psnId) {
    Person person = getPersonName(psnId);
    if(person == null) return "";
    Locale locale = LocaleContextHolder.getLocale();
    if(locale.equals(Locale.CHINA)){
      if(StringUtils.isNotBlank(person.getName())) return person.getName();
      else if(StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())){
        return person.getFirstName()+" "+person.getLastName();
      }else{
        return  person.getEname();
      }
    }else{
      if(StringUtils.isNotBlank(person.getEname())) return person.getEname();
      else if(StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())){
        return person.getFirstName()+" "+person.getLastName();
      }else{
        return  person.getName();
      }
    }
  }
}
