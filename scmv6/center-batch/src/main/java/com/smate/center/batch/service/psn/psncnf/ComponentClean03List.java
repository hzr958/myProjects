package com.smate.center.batch.service.psn.psncnf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigList;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("clean03List")
@Transactional(rollbackFor = Exception.class)
public class ComponentClean03List implements ComponentClean {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "clean04Brief")
  private ComponentClean next;

  @Override
  public void clean(Long runs, Long cnfId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.CLEAN, PsnCnfEnum.PRJ, PsnCnfEnum.PUB, PsnCnfEnum.WORK, PsnCnfEnum.EDU,
          PsnCnfEnum.CONTACT)) {
        // 删除列表结果汇总
        PsnConfigList psnConfigList = new PsnConfigList(cnfId);
        psnCnfService.del(psnConfigList);
      }
    } catch (Exception e) {
      throw new Exception("个人配置：clean03List失败", e);
    }
    // 下个清理操作
    next.clean(runs, cnfId);
  }

}
