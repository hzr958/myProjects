package com.smate.center.oauth.service.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.dao.profile.PersonEmailDao;
import com.smate.center.oauth.exception.ServiceException;
import com.smate.center.oauth.model.profile.PersonEmail;

/**
 * PersonEmailManagerImpl.
 * 
 * @author new .
 * 
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PersonEmailServiceImpl implements PersonEmailService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonEmailDao personEmailDao;
  @Autowired
  private PersonService personService;

  @Override
  public boolean setPsnEmailVerified(Long psnId, String email) {
    try {
      PersonEmail psnEmail = personEmailDao.getByPsnIdAndLoginEmail(psnId, email);
      if (psnEmail != null) {
        psnEmail.setVerify(true);
        personEmailDao.save(psnEmail);
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      logger.error("设置邮件状态为已确认状态出错，用户ID：{}，用户登录邮箱：{}", psnId, email, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PersonEmail getPersonEmailBy(Long psnId, String email) throws ServiceException {
    PersonEmail psnEmail = personEmailDao.getByPsnIdAndLoginEmail(psnId, email);
    return psnEmail;
  }

}
