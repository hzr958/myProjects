package com.smate.center.open.service.psnconf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("clean13End")
@Transactional(rollbackFor = Exception.class)
public class ComponentClean13End implements ComponentClean {
  @Autowired
  private PsnCnfService psnCnfService;

  @Override
  public void clean(Long runs, Long cnfId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.CLEAN, PsnCnfEnum.DIRTY, PsnCnfEnum.ALL)) {
        // 删除成果配置
        PsnConfig cnf = new PsnConfig();
        cnf.setCnfId(cnfId);
        PsnConfig cnfExist = psnCnfService.get(cnf);
        if (cnfExist != null) {// 产生新的个人配置数据
          PsnConfig cnfNew = new PsnConfig(cnfExist.getPsnId());
          cnfNew.setRuns(cnfExist.getRuns());
          cnfNew.setCreateDate(cnfExist.getCreateDate());
          psnCnfService.del(cnfExist);
          psnCnfService.save(cnfNew);
        }
      }
    } catch (Exception e) {
      throw new Exception("个人配置：clean12End失败", e);
    }

  }
}
