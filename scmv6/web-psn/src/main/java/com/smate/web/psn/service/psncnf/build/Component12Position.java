package com.smate.web.psn.service.psncnf.build;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfigPosition;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

@Service("component12Position")
@Transactional(rollbackFor = Exception.class)
class Component12Position implements ComponentBase {

  @Autowired
  private PsnCnfService psnCnfService;

  @Override
  public void assemble(PsnCnfBuild cnfBuild) throws ServiceException {

    Long cnfId = cnfBuild.getCnfId();
    PsnConfigPosition psnConfigPosition = psnCnfService.get(new PsnConfigPosition(cnfId));

    if (psnConfigPosition == null) {
      throw new ServiceException("psn_config_position缺少数据，cnfId=" + cnfId);
    }

    // 添加到PsnCnfBuild，进行封装
    cnfBuild.setCnfPosition(psnConfigPosition);

  }
}
