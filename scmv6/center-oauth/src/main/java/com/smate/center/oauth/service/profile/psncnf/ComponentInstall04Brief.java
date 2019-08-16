package com.smate.center.oauth.service.profile.psncnf;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.service.profile.PersonService;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigBrief;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("install04Brief")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall04Brief implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "install05Work")
  private ComponentInstall next;

  @Autowired
  private PersonService personManager;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY, PsnCnfEnum.BRIEF)) {
        // 创建新数据
        PsnConfigBrief cnfBrief = new PsnConfigBrief(cnfId);
        PsnConfigBrief cnfBriefExist = psnCnfService.get(cnfBrief);
        if (cnfBriefExist == null) {
          boolean hasText = StringUtils.isNotBlank(personManager.getPersonBrief(psnId));
          // 查看权限 & 数据内容有无
          cnfBrief.setAnyView(cnfBrief.getAnyUser() & PsnCnfUtils.convertAnyView(hasText));
          psnCnfService.save(cnfBrief);
        }
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install04Brief失败", e);
    }
    // 下个创建操作
    next.install(runs, cnfId, psnId);
  }
}
