package com.smate.center.batch.service.mail;

import com.smate.center.batch.exception.pub.ServiceException;


public interface ConstMailTypeTemplateRelService {

  Long getTypeidFromTemplateid(Integer templateId) throws ServiceException;

  Long getIsreceive(Long receivePsnId, Long typeId) throws ServiceException;

}
