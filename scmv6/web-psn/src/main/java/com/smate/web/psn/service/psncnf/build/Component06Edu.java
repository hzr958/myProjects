package com.smate.web.psn.service.psncnf.build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfigEdu;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

@Service("component06Edu")
@Transactional(rollbackFor = Exception.class)
class Component06Edu implements ComponentBase {

  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "component08Contact")
  private ComponentBase next;

  @Override
  public void assemble(PsnCnfBuild cnfBuild) throws ServiceException, PsnCnfException {

    Long cnfId = cnfBuild.getCnfId();
    List<PsnConfigEdu> cnfEduList = psnCnfService.gets(new PsnConfigEdu(cnfId));

    Map<Long, PsnConfigEdu> cnfEdus = new HashMap<Long, PsnConfigEdu>();
    for (PsnConfigEdu item : cnfEduList) {
      cnfEdus.put(item.getId().getEduId(), item);
    }
    // 添加到PsnCnfBuild，进行封装
    cnfBuild.setCnfEdu(cnfEdus);

    // 下个封装操作
    next.assemble(cnfBuild);
  }
}
