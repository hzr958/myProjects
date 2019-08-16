package com.smate.web.psn.service.psncnf.build;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfigBrief;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

@Service("component04Brief")
@Transactional(rollbackFor = Exception.class)
class Component04Brief implements ComponentBase {

  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "component05Work")
  private ComponentBase next;

  @Override
  public void assemble(PsnCnfBuild cnfBuild) throws ServiceException, PsnCnfException {

    Long cnfId = cnfBuild.getCnfId();
    PsnConfigBrief psnConfigBrief = psnCnfService.get(new PsnConfigBrief(cnfId));

    if (psnConfigBrief == null) {
      throw new PsnCnfException("psn_config_brief缺少数据，cnfId=" + cnfId);
    }

    // 添加到PsnCnfBuild，进行封装
    cnfBuild.setCnfBrief(psnConfigBrief);

    // 下个封装操作
    next.assemble(cnfBuild);
  }
}
