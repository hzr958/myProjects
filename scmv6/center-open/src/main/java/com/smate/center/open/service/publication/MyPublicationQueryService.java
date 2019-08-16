package com.smate.center.open.service.publication;

import java.util.List;

import com.smate.core.base.pub.model.Publication;

/**
 * 我的成果管理查询专用服务.
 * 
 * @author yamingd
 */
public interface MyPublicationQueryService {

  /**
   * 根据指定的pubIds获取pubs.
   * 
   * @param pubIds
   * @return
   * @throws Exception
   */
  List<Publication> getPubsByPubIds(List<Long> pubIds) throws Exception;
}
