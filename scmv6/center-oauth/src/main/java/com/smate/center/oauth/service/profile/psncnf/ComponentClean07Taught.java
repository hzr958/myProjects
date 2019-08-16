package com.smate.center.oauth.service.profile.psncnf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigTaught;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("clean07Taught")
@Transactional(rollbackFor = Exception.class)
public class ComponentClean07Taught implements ComponentClean {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "clean08Contact")
  private ComponentClean next;

  @Override
  public void clean(Long runs, Long cnfId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.CLEAN, PsnCnfEnum.TAUGHT)) {
        // 删除所教课程配置
        PsnConfigTaught psnConfigTaught = new PsnConfigTaught(cnfId);
        psnCnfService.del(psnConfigTaught);
      }
    } catch (Exception e) {
      throw new Exception("个人配置：clean07Taught失败", e);
    }
    // 下个清理操作
    next.clean(runs, cnfId);
  }

}
