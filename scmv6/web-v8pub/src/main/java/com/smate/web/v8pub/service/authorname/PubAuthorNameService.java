package com.smate.web.v8pub.service.authorname;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.exception.ServiceException;


public interface PubAuthorNameService {

  /**
   * 处理作者名数据
   * 
   * @throws ServiceException
   */
  String disposeAuthorName(JSONArray members, String authorNames) throws ServiceException;

}
