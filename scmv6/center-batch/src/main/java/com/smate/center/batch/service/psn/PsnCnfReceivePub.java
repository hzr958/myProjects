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
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;

/**
 * 成果权限保存
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfReceivePub")
@Transactional(rollbackFor = Exception.class)
class PsnCnfReceivePub implements PsnCnfReceive {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnCnfService psnCnfService;

  @Override
  public void saveCnfByIds(Long psnId, String ids) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && ids != null) {
        String[] pubIds = ids.split(",");
        List<PsnCnfBase> cnfList = new ArrayList<PsnCnfBase>();
        for (int i = 0; i < pubIds.length; i++) {
          if (NumberUtils.isNumber(pubIds[i])) {
            Long pubId = Long.parseLong(pubIds[i]);
            // 构造权限对象
            PsnConfigPub cnfPub = new PsnConfigPub();
            cnfPub.getId().setPubId(pubId);
            cnfList.add(cnfPub);
          }
        }

        if (cnfList.size() > 0) {// 保存权限
          psnCnfService.save(psnId, cnfList);
        }
      }
    } catch (Exception e) {
      logger.error("成果权限保存失败psnId=" + psnId, e);
    }
  }

  @Override
  public void saveCnfByObjs(Long psnId, List<String> objs) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && CollectionUtils.isNotEmpty(objs)) {
        for (int i = 0; i < objs.size(); i++) {
          String[] pids = objs.get(i).split(",");
          if (pids.length == 2 && NumberUtils.isNumber(pids[0]) && NumberUtils.isNumber(pids[1])) {
            Long pubId = Long.parseLong(pids[0]);
            Integer anyUser = Integer.parseInt(pids[1]);
            // 构造权限对象
            PsnConfigPub cnfPub = new PsnConfigPub();
            cnfPub.getId().setPubId(pubId);
            cnfPub.setAnyUser(anyUser);
            cnfPub.setAnyView(cnfPub.getAnyUser());
            psnCnfService.save(psnId, cnfPub);// 保存权限
          }
        }
      }
    } catch (Exception e) {
      logger.error("成果权限保存失败psnId=" + psnId, e);
    }
  }

  @Override
  public void delCnfByIds(Long psnId, List<Long> idList) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && CollectionUtils.isNotEmpty(idList)) {
        for (Long pubId : idList) {
          // 构造权限对象
          PsnConfigPub cnfPub = new PsnConfigPub();
          cnfPub.getId().setPubId(pubId);
          psnCnfService.del(psnId, cnfPub);// 删除权限
        }
      }

    } catch (Exception e) {
      logger.error("成果权限删除失败psnId=" + psnId, e);
    }
  }
}
