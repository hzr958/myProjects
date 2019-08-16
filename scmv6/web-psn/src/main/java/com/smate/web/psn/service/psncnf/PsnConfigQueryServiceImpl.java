package com.smate.web.psn.service.psncnf;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.psncnf.PsnConfigContactDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigWorkDao;
import com.smate.web.psn.exception.ServiceException;

/**
 * 人员权限查看服务类
 * 
 * @author zk
 * 
 */
@Service("psnConfigQueryService")
@Transactional(rollbackFor = Exception.class)
public class PsnConfigQueryServiceImpl implements PsnConfigQueryService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnConfigContactDao psnConfigContactDao;

  @Autowired
  private PsnConfigWorkDao psnConfigWorkDao;

  /**
   * 获取一批人员的Contact权限
   * 
   * @author zk
   */
  @SuppressWarnings("rawtypes")
  @Override
  public List<Map> getPsnConfigContactByPsnIds(List<Long> psnIdList) throws ServiceException {
    try {
      return psnConfigContactDao.getPsnConfigContactByPsnIds(psnIdList);
    } catch (Exception e) {
      logger.error("获取人员联系方式出错,psnIds=" + psnIdList, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 检查工作经历是否匹配anyview值
   * 
   */
  @Override
  public Boolean checkPsnWork(Long psnId, Long insId, String insNameZh, String insNameEn, Integer anyView)
      throws ServiceException {
    Long count = psnConfigWorkDao.queryPsnConfigWorkByIns(psnId, insId, insNameZh, insNameEn, anyView);
    if (count == 0) {
      return false;
    } else {
      return true;
    }
  }
}
