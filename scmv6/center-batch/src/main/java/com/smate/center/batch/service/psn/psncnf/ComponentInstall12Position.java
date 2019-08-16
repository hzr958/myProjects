package com.smate.center.batch.service.psn.psncnf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigPosition;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("install12Position")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall12Position implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY, PsnCnfEnum.POSITION)) {
        // 创建新数据
        PsnConfigPosition cnfPosition = new PsnConfigPosition(cnfId);
        PsnConfigPosition cnfPositionExist = psnCnfService.get(cnfPosition);
        if (cnfPositionExist == null) {
          psnCnfService.save(cnfPosition);
        }
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install12Position失败", e);
    }
  }
}
