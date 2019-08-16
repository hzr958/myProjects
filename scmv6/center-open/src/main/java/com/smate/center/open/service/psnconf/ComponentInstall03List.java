package com.smate.center.open.service.psnconf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigList;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("install03List")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall03List implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "install04Brief")
  private ComponentInstall next;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY)) {
        // 创建新数据
        PsnConfigList cnfList = new PsnConfigList(cnfId);
        PsnConfigList cnfListExist = psnCnfService.get(cnfList);
        if (cnfListExist == null) {
          psnCnfService.save(cnfList);
        }
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install03List失败", e);
    }
    // 下个创建操作
    next.install(runs, cnfId, psnId);
  }
}
