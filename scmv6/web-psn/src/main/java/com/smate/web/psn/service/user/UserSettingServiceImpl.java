package com.smate.web.psn.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.dao.attention.AttPersonDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.attention.AttPerson;

/**
 * 用户设置服务实现
 *
 * @author wsn
 * @createTime 2017年7月6日 下午5:04:31
 *
 */
@Service("userSettingService")
@Transactional(rollbackFor = Exception.class)
public class UserSettingServiceImpl implements UserSettingService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private AttPersonDao attPersonDao;
  @Autowired
  private PersonProfileDao personProfileDao;

  @Override
  public void buildAndSaveAttPerson(Long psnId, Long refPsnId) throws ServiceException {
    try {

      AttPerson attPerson = this.attPersonDao.find(psnId, refPsnId);
      if (attPerson == null) {
        Person person = personProfileDao.findPsnInfoForAttPerson(refPsnId);
        if (person != null) {
          attPerson = new AttPerson(psnId, refPsnId);
          attPerson.setRefPsnName(person.getName());
          attPerson.setRefFirstName(person.getFirstName());
          attPerson.setRefLastName(person.getLastName());
          attPerson.setRefInsName(person.getInsName());
          attPerson.setRefHeadUrl(person.getAvatars());
          attPerson.setRefTitle(person.getTitolo());
          attPersonDao.save(attPerson);
        }
      }
    } catch (Exception e) {
      logger.error("人员关注关系保存失败！psnId=" + psnId + ", refPsnId=" + refPsnId, e);
      throw new ServiceException(e);
    }
  }

}
