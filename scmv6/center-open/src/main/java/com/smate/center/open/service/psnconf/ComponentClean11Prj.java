package com.smate.center.open.service.psnconf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("clean11Prj")
@Transactional(rollbackFor = Exception.class)
public class ComponentClean11Prj implements ComponentClean {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "clean12Position")
  private ComponentClean next;

  @Override
  public void clean(Long runs, Long cnfId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.CLEAN, PsnCnfEnum.PRJ)) {
        // 删除项目配置
        PsnConfigPrj psnConfigPrj = new PsnConfigPrj(cnfId);
        psnCnfService.del(psnConfigPrj);
      }
    } catch (Exception e) {
      throw new Exception("个人配置：clean11Prj失败", e);
    }
    // 下个清理操作
    next.clean(runs, cnfId);
  }

}
