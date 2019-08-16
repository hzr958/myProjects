package com.smate.web.v8pub.service.findpub;

import java.util.List;
import java.util.Map;

import com.smate.core.base.exception.ServiceException;

public interface FindPubService {
  public List<Map<String, Object>> getAllScienceArea() throws ServiceException;
}
