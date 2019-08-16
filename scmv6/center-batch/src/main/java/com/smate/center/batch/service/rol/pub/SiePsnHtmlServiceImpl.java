package com.smate.center.batch.service.rol.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.SiePsnHtmlRefreshDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.SiePsnHtmlRefresh;

/**
 * 人员HTML服务类
 * 
 * @author zk
 * 
 */
@Service("psnHtmlService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
public class SiePsnHtmlServiceImpl implements SiePsnHtmlService {

  @Autowired
  private SiePsnHtmlRefreshDao psnHtmlRefreshDao;

  /**
   * 设置psnId需要刷新
   */
  public void saveToRefreshTask(Long psnId) throws ServiceException {
    SiePsnHtmlRefresh refresh = psnHtmlRefreshDao.findByPsnId(psnId);
    if (refresh == null) {
      refresh = new SiePsnHtmlRefresh();
      refresh.setPsnId(psnId);
    }
    refresh.setTempCode(0);
    refresh.setStatus(1);
    psnHtmlRefreshDao.save(refresh);
  }

}
