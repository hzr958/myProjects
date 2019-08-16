package com.smate.center.task.service.thirdparty.source;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.thirdparty.ThirdSourcesDao;
import com.smate.center.task.dao.thirdparty.ThirdSourcesTypeDao;
import com.smate.center.task.model.thirdparty.ThirdSources;
import com.smate.center.task.model.thirdparty.ThirdSourcesType;

@Service("thirdSourcesService")
@Transactional(rollbackFor = Exception.class)
public class ThirdSourcesServiceImpl implements ThirdSourcesService {

  @Autowired
  private ThirdSourcesDao thirdSourcesDao;

  @Autowired
  private ThirdSourcesTypeDao thirdSourcesTypeDao;

  @Override
  public List<ThirdSources> getSourcesList() throws Exception {
    List<ThirdSources> list = thirdSourcesDao.getSourcesList();
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }
    for (ThirdSources ts : list) {
      List<ThirdSourcesType> listType = thirdSourcesTypeDao.getSourcesTypeListBySid(ts.getSourceId());
      ts.setTsType(listType);
    }
    return list;
  }

}
