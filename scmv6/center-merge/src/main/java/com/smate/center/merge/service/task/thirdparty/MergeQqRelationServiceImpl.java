package com.smate.center.merge.service.task.thirdparty;

import com.smate.center.merge.dao.thirdparth.SysThirdUserDao;
import com.smate.center.merge.model.cas.thirdparty.SysThirdUser;
import com.smate.center.merge.service.task.MergeBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理QQ关联记录.
 * 
 * @author tsz
 *
 * @date 2018年9月12日
 */
@Transactional(rollbackFor = Exception.class)
public class MergeQqRelationServiceImpl extends MergeBaseService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SysThirdUserDao sysThirdUserDao;

  @Override
  public boolean checkRun(Long savePsnId, Long delPsnId) throws Exception {
    SysThirdUser del = sysThirdUserDao.find(SysThirdUser.TYPE_QQ, delPsnId);
    SysThirdUser save = sysThirdUserDao.find(SysThirdUser.TYPE_QQ, savePsnId);
    if (del != null && save == null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean dealMerge(Long savePsnId, Long delPsnId) throws Exception {
    SysThirdUser del = sysThirdUserDao.find(SysThirdUser.TYPE_QQ, delPsnId);
    del.setPsnId(savePsnId);
    sysThirdUserDao.save(del);
    return false;
  }
}
