package com.smate.center.oauth.service.profile.psncnf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigEdu;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("clean06Edu")
@Transactional(rollbackFor = Exception.class)
public class ComponentClean06Edu implements ComponentClean {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "clean07Taught")
  private ComponentClean next;

  @Override
  public void clean(Long runs, Long cnfId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.CLEAN, PsnCnfEnum.EDU)) {
        // 删除教育经历配置
        PsnConfigEdu psnConfigEdu = new PsnConfigEdu(cnfId);
        psnCnfService.del(psnConfigEdu);
      }
    } catch (Exception e) {
      throw new Exception("个人配置：clean06Edu失败", e);
    }
    // 下个清理操作
    next.clean(runs, cnfId);
  }

}
