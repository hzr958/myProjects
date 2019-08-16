package com.smate.center.task.single.service.pub;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.PublicationIns;

/**
 * 
 * @author liqinghua
 * 
 */
public interface PublicationInsService {

  /**
   * @param name
   * @return
   * @throws ServiceException
   */
  PublicationIns lookUpByName(String name) throws ServiceException;

  /**
   * 
   * @param insId
   * @return
   * @throws ServiceException
   */
  public PublicationIns lookUpById(Long insId) throws ServiceException;

}
