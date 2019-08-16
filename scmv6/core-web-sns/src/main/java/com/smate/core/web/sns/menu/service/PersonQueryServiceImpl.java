package com.smate.core.web.sns.menu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.security.Person;


/**
 * 获取 人员信息类接口
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Transactional(rollbackFor = Exception.class)
public class PersonQueryServiceImpl implements PersonQueryService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonDao personDao;

  @Override
  public Person getPersonBaseInfo(Long psnId) throws SysServiceException {
    return personDao.getPersonForSnsMenu(psnId);
  }

}

