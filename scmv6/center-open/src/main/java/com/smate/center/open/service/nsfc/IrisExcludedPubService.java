package com.smate.center.open.service.nsfc;

import java.io.Serializable;
import java.util.List;

/**
 * IRIS业务系统需要排除的成果Service.
 * 
 * @author pwl
 * 
 */
public interface IrisExcludedPubService extends Serializable {

  int deleteIrisExcludedPub(String uuid) throws Exception;

  void saveIrisExcludedPub(List<Long> pubIdList, String uuid) throws Exception;
}
