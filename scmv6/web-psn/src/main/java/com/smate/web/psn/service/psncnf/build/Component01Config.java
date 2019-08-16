package com.smate.web.psn.service.psncnf.build;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

@Service("component01Config")
@Transactional(rollbackFor = Exception.class)
class Component01Config implements ComponentBase {

  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "component02Moudle")
  private ComponentBase next;


  @Override
  public void assemble(PsnCnfBuild cnfBuild) throws ServiceException, PsnCnfException {

    Long psnId = cnfBuild.getPsnId();
    PsnConfig cnf = psnCnfService.get(new PsnConfig(psnId));
    if (cnf == null) {
      throw new PsnCnfException("psnConfig为空，psnId=" + psnId);
    }
    // 添加到PsnCnfBuild，进行封装
    cnfBuild.setCnf(cnf);

    // 下个封装操作
    next.assemble(cnfBuild);
  }
}
