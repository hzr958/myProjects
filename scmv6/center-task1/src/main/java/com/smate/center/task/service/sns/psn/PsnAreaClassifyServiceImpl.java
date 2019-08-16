package com.smate.center.task.service.sns.psn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.psn.PsnAreaClassifyDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.psn.PsnAreaClassify;


/**
 * 人员领域大类，默认使用前两个项目大类，无项目情况下使用成果期刊大类.
 * 
 * 
 * @author liqinghua
 * 
 */
@Service("psnAreaClassifyService")
@Transactional(rollbackFor = Exception.class)
public class PsnAreaClassifyServiceImpl implements PsnAreaClassifyService {

  /**
   * 
   */
  private static final long serialVersionUID = 7518356602396170222L;
  @Autowired
  private PsnAreaClassifyDao psnAreaClassifyDao;


  @Override
  public List<String> getPsnClassify(Long psnId) throws ServiceException {
    return psnAreaClassifyDao.getPsnAreaClassifyStr(psnId);
  }

}
