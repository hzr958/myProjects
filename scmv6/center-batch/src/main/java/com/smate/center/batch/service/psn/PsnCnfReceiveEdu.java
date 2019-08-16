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
import com.smate.core.base.psn.model.psncnf.PsnConfigEdu;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;

/**
 * 教育经历权限保存
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfReceiveEdu")
@Transactional(rollbackFor = Exception.class)
class PsnCnfReceiveEdu implements PsnCnfReceive {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnCnfService psnCnfService;

  @Override
  public void saveCnfByIds(Long psnId, String ids) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && ids != null) {
        String[] eduIds = ids.split(",");
        List<PsnCnfBase> cnfList = new ArrayList<PsnCnfBase>();
        for (int i = 0; i < eduIds.length; i++) {
          if (NumberUtils.isNumber(eduIds[i])) {
            Long eduId = Long.parseLong(eduIds[i]);
            // 构造权限对象
            PsnConfigEdu cnfEdu = new PsnConfigEdu();
            cnfEdu.getId().setEduId(eduId);
            cnfList.add(cnfEdu);
          }
        }

        if (cnfList.size() > 0) {// 保存权限
          psnCnfService.save(psnId, cnfList);
        }
      }
    } catch (Exception e) {
      logger.error("教育经历权限保存失败psnId=" + psnId, e);
    }
  }

  @Override
  public void saveCnfByObjs(Long psnId, List<String> objs) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && CollectionUtils.isNotEmpty(objs)) {
        for (int i = 0; i < objs.size(); i++) {
          String[] pids = objs.get(i).split(",");
          if (pids.length == 2 && NumberUtils.isNumber(pids[0]) && NumberUtils.isNumber(pids[1])) {
            Long eduId = Long.parseLong(pids[0]);
            Integer anyUser = Integer.parseInt(pids[1]);
            // 构造权限对象
            PsnConfigEdu cnfEdu = new PsnConfigEdu();
            cnfEdu.getId().setEduId(eduId);
            cnfEdu.setAnyUser(anyUser);
            cnfEdu.setAnyView(cnfEdu.getAnyUser());
            psnCnfService.save(psnId, cnfEdu);// 保存权限
          }
        }
      }

    } catch (Exception e) {
      logger.error("教育经历权限保存失败psnId=" + psnId, e);
    }
  }

  @Override
  public void delCnfByIds(Long psnId, List<Long> idList) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && CollectionUtils.isNotEmpty(idList)) {
        for (Long eduId : idList) {
          // 构造权限对象
          PsnConfigEdu cnfEdu = new PsnConfigEdu();
          cnfEdu.getId().setEduId(eduId);
          psnCnfService.del(psnId, cnfEdu);// 删除权限
        }
      }

    } catch (Exception e) {
      logger.error("教育经历权限删除失败psnId=" + psnId, e);
    }
  }
}
