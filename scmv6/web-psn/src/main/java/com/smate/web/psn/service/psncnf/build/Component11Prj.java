package com.smate.web.psn.service.psncnf.build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

@Service("component11Prj")
@Transactional(rollbackFor = Exception.class)
class Component11Prj implements ComponentBase {

  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "component12Position")
  private ComponentBase next;

  @Override
  public void assemble(PsnCnfBuild cnfBuild) throws ServiceException, PsnCnfException {

    Long cnfId = cnfBuild.getCnfId();
    List<PsnConfigPrj> cnfPrjList = psnCnfService.gets(new PsnConfigPrj(cnfId));

    Map<Long, PsnConfigPrj> cnfPrjs = new HashMap<Long, PsnConfigPrj>();
    for (PsnConfigPrj item : cnfPrjList) {
      cnfPrjs.put(item.getId().getPrjId(), item);
    }

    // 添加到PsnCnfBuild，进行封装
    cnfBuild.setCnfPrj(cnfPrjs);

    // 下个封装操作
    next.assemble(cnfBuild);

  }
}
