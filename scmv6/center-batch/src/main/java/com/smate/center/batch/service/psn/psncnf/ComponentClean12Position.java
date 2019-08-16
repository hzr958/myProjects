package com.smate.center.batch.service.psn.psncnf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigPosition;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("clean12Position")
@Transactional(rollbackFor = Exception.class)
public class ComponentClean12Position implements ComponentClean {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "clean13End")
  private ComponentClean next;

  @Override
  public void clean(Long runs, Long cnfId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.CLEAN, PsnCnfEnum.POSITION)) {
        // 删除职称配置
        PsnConfigPosition psnConfigPosition = new PsnConfigPosition(cnfId);
        psnCnfService.del(psnConfigPosition);
      }
    } catch (Exception e) {
      throw new Exception("个人配置：clean12Position失败", e);
    }
    // 下个清理操作
    next.clean(runs, cnfId);
  }

}
