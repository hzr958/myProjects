package com.smate.center.batch.service.pub.rcmd;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;

/**
 * 成果全文推荐服务.
 * 
 * @author pwl
 * 
 */
public interface PubFulltextRcmdService extends Serializable {


  /**
   * 保存基准库ISI成果冗余信息.
   * 
   * @param list
   * @throws ServiceException
   */
  public void saveIsiPublication(List<Map<String, Object>> list) throws ServiceException;

}
