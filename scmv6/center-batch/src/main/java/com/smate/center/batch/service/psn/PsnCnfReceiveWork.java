package com.smate.center.batch.service.psn;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.psn.model.psncnf.PsnConfigWork;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;

/**
 * 工作经历权限保存
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfReceiveWork")
@Transactional(rollbackFor = Exception.class)
class PsnCnfReceiveWork implements PsnCnfReceive {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnCnfService psnCnfService;

  @Override
  public void saveCnfByIds(Long psnId, String ids) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && ids != null) {
        String[] workIds = ids.split(",");
        List<PsnCnfBase> cnfList = new ArrayList<PsnCnfBase>();
        for (int i = 0; i < workIds.length; i++) {
          if (NumberUtils.isNumber(workIds[i])) {
            Long workId = Long.parseLong(workIds[i]);
            // 构造权限对象
            PsnConfigWork cnfWork = new PsnConfigWork();
            cnfWork.getId().setWorkId(workId);
            cnfList.add(cnfWork);
          }
        }

        if (cnfList.size() > 0) {// 保存权限
          psnCnfService.save(psnId, cnfList);
        }
      }
    } catch (Exception e) {
      logger.error("工作经历权限保存失败psnId=" + psnId, e);
    }
  }

  @Override
  public void saveCnfByObjs(Long psnId, List<String> objs) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && CollectionUtils.isNotEmpty(objs)) {
        for (int i = 0; i < objs.size(); i++) {
          String[] pids = objs.get(i).split(",");
          if (pids.length == 2 && NumberUtils.isNumber(pids[0]) && NumberUtils.isNumber(pids[1])) {
            Long workId = Long.parseLong(pids[0]);
            Integer anyUser = Integer.parseInt(pids[1]);
            // 构造权限对象
            PsnConfigWork cnfWork = new PsnConfigWork();
            cnfWork.getId().setWorkId(workId);
            cnfWork.setAnyUser(anyUser);
            cnfWork.setAnyView(cnfWork.getAnyUser());
            psnCnfService.save(psnId, cnfWork);// 保存权限
          }
        }
      }

    } catch (Exception e) {
      logger.error("工作经历权限保存失败psnId=" + psnId, e);
    }
  }

  @Override
  public void delCnfByIds(Long psnId, List<Long> idList) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && CollectionUtils.isNotEmpty(idList)) {
        for (Long workId : idList) {
          // 构造权限对象
          PsnConfigWork cnfWork = new PsnConfigWork();
          cnfWork.getId().setWorkId(workId);
          psnCnfService.del(psnId, cnfWork);// 删除权限
        }
      }

    } catch (Exception e) {
      logger.error("工作经历权限删除失败psnId=" + psnId, e);
    }
  }
}
