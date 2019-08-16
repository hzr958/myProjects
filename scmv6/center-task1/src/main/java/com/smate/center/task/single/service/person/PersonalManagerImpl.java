package com.smate.center.task.single.service.person;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.InstitutionDao;
import com.smate.center.task.dao.sns.quartz.PsnDisciplineDao;
import com.smate.center.task.exception.DaoException;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.Institution;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.utils.model.security.Person;

@Service("personalManager")
@Transactional(rollbackFor = Exception.class)
public class PersonalManagerImpl implements PersonalManager {

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnDisciplineDao psnDisciplineDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private InstitutionDao institutionDao;

  /**
   * 获取刷新用户信息完整度的数据.
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  @Override
  public boolean isPsnDiscExit(Long psnId) throws ServiceException {

    try {
      return psnDisciplineDao.isPsnDiscExit(psnId);
    } catch (DaoException e) {
      logger.error("获取刷新用户信息完整度的数据", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取人员首要工作单位所在地区.
   * 
   * @param psnId
   * @return
   */
  @Override
  public Long getPsnInsRegionId(Long psnId) {
    Long regionId = null;
    Person person = personProfileDao.getPsnInsData(psnId);
    if (person != null) {
      regionId = this.getInsRegion(person);
    }
    return regionId;
  }

  /**
   * 获取人员首要工作单位的region_id .
   * 
   * @param person
   * @return
   */
  private Long getInsRegion(Person person) {
    Long insRegionId = null;
    try {
      Institution institute = null;
      if (person.getInsId() != null) {
        institute = institutionDao.findById(person.getInsId());
      } else if (StringUtils.isNotBlank(person.getInsName())) {
        institute = institutionDao.findByName(person.getInsName());
      }
      if (institute != null) {
        insRegionId = institute.getRegionId();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return insRegionId;
  }
}
