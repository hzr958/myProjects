package com.smate.center.oauth.service.profile.psncnf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("clean10Pub")
@Transactional(rollbackFor = Exception.class)
public class ComponentClean10Pub implements ComponentClean {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "clean11Prj")
  private ComponentClean next;

  @Override
  public void clean(Long runs, Long cnfId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.CLEAN, PsnCnfEnum.PUB)) {
        // 删除成果配置
        PsnConfigPub psnConfigPub = new PsnConfigPub(cnfId);
        psnCnfService.del(psnConfigPub);
      }
    } catch (Exception e) {
      throw new Exception("个人配置：clean10Pub失败", e);
    }
    // 下个清理操作
    next.clean(runs, cnfId);
  }
}
