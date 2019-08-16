package com.smate.center.batch.service.pub;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PublicationIns;

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
