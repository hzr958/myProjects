package com.smate.web.v8pub.service.sns.group;

import java.util.List;
import com.smate.web.v8pub.exception.ServiceException;

/**
 * 群组关键词服务类
 * 
 * @author YJ
 *
 *         2018年8月3日
 */
public interface GrpKwDiscService {

  /**
   * 获取群组关键词
   * 
   * @param grpId
   * @return
   * @throws ServiceException
   */
  List<String> listGrpKeyword(Long grpId) throws ServiceException;
}
