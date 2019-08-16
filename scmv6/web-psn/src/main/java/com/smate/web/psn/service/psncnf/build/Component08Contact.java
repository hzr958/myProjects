package com.smate.web.psn.service.psncnf.build;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfigContact;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

@Service("component08Contact")
@Transactional(rollbackFor = Exception.class)
class Component08Contact implements ComponentBase {

  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "component09Expertise")
  private ComponentBase next;

  @Override
  public void assemble(PsnCnfBuild cnfBuild) throws ServiceException, PsnCnfException {

    Long cnfId = cnfBuild.getCnfId();
    PsnConfigContact cnfContact = psnCnfService.get(new PsnConfigContact(cnfId));

    if (cnfContact == null) {
      throw new ServiceException("psn_config_contact缺少数据，cnfId=" + cnfId);
    }

    // 添加到PsnCnfBuild，进行封装
    cnfBuild.setCnfContact(cnfContact);

    // 下个封装操作
    next.assemble(cnfBuild);
  }
}
