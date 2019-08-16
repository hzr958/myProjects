package com.smate.center.oauth.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.oauth.exception.ServiceException;
import com.smate.center.oauth.model.pub.Publication;

/**
 * 成果SERVICE接口.
 * 
 * @author liqinghua
 * 
 */
public interface PublicationService extends Serializable {

  /**
   * @author liangguokeng
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Publication> findPubIdsByPsnId(Long psnId) throws ServiceException;


}
