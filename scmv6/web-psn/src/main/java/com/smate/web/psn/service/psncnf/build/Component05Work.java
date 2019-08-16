package com.smate.web.psn.service.psncnf.build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfigWork;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

@Service("component05Work")
@Transactional(rollbackFor = Exception.class)
class Component05Work implements ComponentBase {

  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "component06Edu")
  private ComponentBase next;

  @Override
  public void assemble(PsnCnfBuild cnfBuild) throws ServiceException, PsnCnfException {

    Long cnfId = cnfBuild.getCnfId();
    List<PsnConfigWork> cnfWorkList = psnCnfService.gets(new PsnConfigWork(cnfId));

    Map<Long, PsnConfigWork> cnfWorks = new HashMap<Long, PsnConfigWork>();
    for (PsnConfigWork item : cnfWorkList) {
      cnfWorks.put(item.getId().getWorkId(), item);
    }

    // 添加到PsnCnfBuild，进行封装
    cnfBuild.setCnfWork(cnfWorks);

    // 下个封装操作
    next.assemble(cnfBuild);
  }
}
