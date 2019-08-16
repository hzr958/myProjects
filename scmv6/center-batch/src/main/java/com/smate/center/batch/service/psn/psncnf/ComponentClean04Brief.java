package com.smate.center.batch.service.psn.psncnf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigBrief;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("clean04Brief")
@Transactional(rollbackFor = Exception.class)
public class ComponentClean04Brief implements ComponentClean {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "clean05Work")
  private ComponentClean next;

  @Override
  public void clean(Long runs, Long cnfId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.CLEAN, PsnCnfEnum.BRIEF)) {
        // 删除个人简介配置
        PsnConfigBrief psnConfigBrief = new PsnConfigBrief(cnfId);
        psnCnfService.del(psnConfigBrief);
      }
    } catch (Exception e) {
      throw new Exception("个人配置：clean04Brief失败", e);
    }
    // 下个清理操作
    next.clean(runs, cnfId);
  }

}
