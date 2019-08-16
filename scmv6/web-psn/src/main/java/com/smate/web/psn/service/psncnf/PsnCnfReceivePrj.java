package com.smate.web.psn.service.psncnf;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.web.psn.exception.ServiceException;

/**
 * 项目权限保存
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfReceivePrj")
@Transactional(rollbackFor = Exception.class)
class PsnCnfReceivePrj implements PsnCnfReceive {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnCnfService psnCnfService;

  @Override
  public void saveCnfByIds(Long psnId, String ids) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && ids != null) {
        String[] prjIds = ids.split(",");
        List<PsnCnfBase> cnfList = new ArrayList<PsnCnfBase>();
        for (int i = 0; i < prjIds.length; i++) {
          if (NumberUtils.isNumber(prjIds[i])) {
            Long prjId = Long.parseLong(prjIds[i]);
            // 构造权限对象
            PsnConfigPrj cnfPrj = new PsnConfigPrj();
            cnfPrj.getId().setPrjId(prjId);
            cnfList.add(cnfPrj);
          }
        }

        if (cnfList.size() > 0) {// 保存权限
          psnCnfService.save(psnId, cnfList);
        }
      }
    } catch (Exception e) {
      logger.error("项目权限保存失败psnId=" + psnId, e);
    }
  }

  @Override
  public void saveCnfByObjs(Long psnId, List<String> objs) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && CollectionUtils.isNotEmpty(objs)) {
        for (int i = 0; i < objs.size(); i++) {
          String[] pids = objs.get(i).split(",");
          if (pids.length == 2 && NumberUtils.isNumber(pids[0]) && NumberUtils.isNumber(pids[1])) {
            Long prjId = Long.parseLong(pids[0]);
            Integer anyUser = Integer.parseInt(pids[1]);
            // 构造权限对象
            PsnConfigPrj cnfPrj = new PsnConfigPrj();
            cnfPrj.getId().setPrjId(prjId);
            cnfPrj.setAnyUser(anyUser);
            cnfPrj.setAnyView(cnfPrj.getAnyUser());
            psnCnfService.save(psnId, cnfPrj);// 保存权限
          }
        }
      }

    } catch (Exception e) {
      logger.error("项目权限保存失败psnId=" + psnId, e);
    }
  }

  @Override
  public void delCnfByIds(Long psnId, List<Long> idList) throws ServiceException {
    try {
      if (psnId != null && psnId > 0 && CollectionUtils.isNotEmpty(idList)) {
        for (Long prjId : idList) {
          // 构造权限对象
          PsnConfigPrj cnfPrj = new PsnConfigPrj();
          cnfPrj.getId().setPrjId(prjId);
          psnCnfService.del(psnId, cnfPrj);// 删除权限
        }
      }

    } catch (Exception e) {
      logger.error("项目权限删除失败psnId=" + psnId, e);
    }
  }
}
