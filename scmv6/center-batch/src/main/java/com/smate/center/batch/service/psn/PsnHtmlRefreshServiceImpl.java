package com.smate.center.batch.service.psn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnHtmlRefreshDao;
import com.smate.center.batch.exception.pub.ServiceException;


/**
 * 人员Html刷新服务类
 * 
 * @author zk
 * 
 */
@Service("psnHtmlRefreshService")
@Transactional(rollbackFor = Exception.class)
public class PsnHtmlRefreshServiceImpl implements PsnHtmlRefreshService {

  @Autowired
  private PsnHtmlRefreshDao psnHtmlRefreshDao;


  @Override
  public void updatePsnHtmlRefresh(Long psnId) throws ServiceException {
    psnHtmlRefreshDao.updateRefresh(psnId, 1);
  }
}
