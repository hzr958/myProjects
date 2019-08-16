package com.smate.core.base.utils.service.security;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.menu.ResourceDao;
import com.smate.core.base.utils.model.menu.Resource;


/**
 * 从数据库查询URL--授权定义的RequestMapService实现类.
 * 
 * 
 */
@Service("resourceDetailService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class ResourceDetailServiceImpl implements ResourceDetailService {
  @Autowired
  private ResourceDao resourceDao;

  /**
   * @see ResourceDetailService#getRequestMap()
   */
  public LinkedHashMap<String, String> getRequestMap() throws Exception {
    List<Resource> resourceList = resourceDao.find(Resource.QUERY_BY_URL_TYPE, Resource.URL_TYPE);
    LinkedHashMap<String, String> requestMap = new LinkedHashMap<String, String>();
    for (Resource resource : resourceList) {
      resource.setAuthNames();
      requestMap.put(resource.getValue(), resource.getAuthNames());
    }
    return requestMap;
  }

}
