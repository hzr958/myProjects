package com.smate.center.open.service.publication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.publication.PublicationQueryDao;
import com.smate.core.base.pub.model.Publication;

/**
 * @author yamingd 我的成果管理查询专用服务
 */
@Service("myPublicationQueryService")
@Transactional(rollbackFor = Exception.class)
public class MyPublicationQueryServiceImpl implements MyPublicationQueryService {

  @Autowired
  private PublicationQueryDao publicationQueryDao;

  @Override
  public List<Publication> getPubsByPubIds(List<Long> pubIds) throws Exception {
    return publicationQueryDao.getPubsByPubIds(pubIds);
  }

}
