package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.core.base.utils.model.Page;

/**
 * 在单位ROL操作个人成果使用本服务.
 * 
 * @author yamingd
 * 
 */
public interface HttpMyPublicationService {

  /**
   * 读取成果XML，同步到单位ROL.
   * 
   * @param pubIds 成果ID列表，最多5个一次
   * @return List<PublicationXml>
   */
  List<PublicationXml> fetchSubmittedXml(Long[] pubIds) throws ServiceException;

  /**
   * 同步提交的成果的最新状态到Scholar.方便查询过滤.
   * 
   * @param pubIds 成果ids
   * @param insId 单位Id
   * @param state 提交状态
   * @return true or false
   */
  boolean syncSubmissionState(Long[] pubIds, Long insId, Integer state) throws ServiceException;

}
