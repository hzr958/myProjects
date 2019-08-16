package com.smate.web.v8pub.service.sns;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubScienceAreaPO;
import com.smate.web.v8pub.service.BaseService;

/**
 * sns库成果科技领域服务接口
 * 
 * @author YJ
 *
 *         2018年8月7日
 */
public interface PubScienceAreaService extends BaseService<Long, PubScienceAreaPO> {

  public List<PubScienceAreaPO> getScienceAreaList(Long id) throws ServiceException;
}
