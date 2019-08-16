package com.smate.center.task.service.sns.psn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;


/**
 * @author lcw
 * 
 */
@Service("systemRecommendService")
public class SystemRecommendServiceImpl implements SystemRecommendService {
  private static final long serialVersionUID = -6165310015131587881L;
  @Autowired
  private RecommendService recommendService;

  @Override
  public void removeAllRecommendScore() throws ServiceException {
    recommendService.removeAllRecommendScore();// 清空推荐得分表
  }

}
