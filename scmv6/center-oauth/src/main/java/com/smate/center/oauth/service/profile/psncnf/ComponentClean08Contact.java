package com.smate.center.oauth.service.profile.psncnf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigContact;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("clean08Contact")
@Transactional(rollbackFor = Exception.class)
public class ComponentClean08Contact implements ComponentClean {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "clean09Expertise")
  private ComponentClean next;

  @Override
  public void clean(Long runs, Long cnfId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.CLEAN, PsnCnfEnum.CONTACT)) {
        // 删除联系方式配置
        PsnConfigContact psnConfigContact = new PsnConfigContact(cnfId);
        psnCnfService.del(psnConfigContact);
      }
    } catch (Exception e) {
      throw new Exception("个人配置：clean08Contact失败", e);
    }
    // 下个清理操作
    next.clean(runs, cnfId);
  }

}
