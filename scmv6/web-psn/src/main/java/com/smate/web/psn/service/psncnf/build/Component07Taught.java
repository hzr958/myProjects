package com.smate.web.psn.service.psncnf.build;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfigTaught;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

@Service("component07Taught")
@Transactional(rollbackFor = Exception.class)
class Component07Taught implements ComponentBase {

  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "component08Contact")
  private ComponentBase next;

  @Override
  public void assemble(PsnCnfBuild cnfBuild) throws ServiceException, PsnCnfException {

    Long cnfId = cnfBuild.getCnfId();
    PsnConfigTaught cnfTaught = psnCnfService.get(new PsnConfigTaught(cnfId));

    if (cnfTaught == null) {
      throw new ServiceException("psn_config_taught缺少数据，cnfId=" + cnfId);
    }

    // 添加到PsnCnfBuild，进行封装
    cnfBuild.setCnfTaught(cnfTaught);

    // 下个封装操作
    next.assemble(cnfBuild);
  }
}
