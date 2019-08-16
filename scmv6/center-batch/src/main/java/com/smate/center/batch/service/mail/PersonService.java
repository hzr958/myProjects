package com.smate.center.batch.service.mail;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.model.security.Person;


public interface PersonService extends Serializable {

  Person getPeronsForEmail(Long psnId) throws ServiceException;

  String getEmailLanguByPsnId(Long psnId) throws ServiceException;

  String getLangByPsnId(Long psnId) throws ServiceException;

}
