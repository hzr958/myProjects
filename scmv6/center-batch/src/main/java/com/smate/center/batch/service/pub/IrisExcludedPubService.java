package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * IRIS业务系统需要排除的成果Service.
 * 
 * @author pwl
 * 
 */
public interface IrisExcludedPubService extends Serializable {

  void deleteIrisExcludedPub(String uuid) throws ServiceException;

  void saveIrisExcludedPub(List<Long> pubIdList, String uuid) throws ServiceException;
}
