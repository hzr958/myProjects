package com.smate.web.psn.service.psncnf.build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

@Service("component10Pub")
@Transactional(rollbackFor = Exception.class)
class Component10Pub implements ComponentBase {

  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "component11Prj")
  private ComponentBase next;

  @Override
  public void assemble(PsnCnfBuild cnfBuild) throws ServiceException, PsnCnfException {

    Long cnfId = cnfBuild.getCnfId();
    List<PsnConfigPub> cnfPubList = psnCnfService.gets(new PsnConfigPub(cnfId));

    Map<Long, PsnConfigPub> cnfPubs = new HashMap<Long, PsnConfigPub>();
    for (PsnConfigPub item : cnfPubList) {
      cnfPubs.put(item.getId().getPubId(), item);
    }

    // 添加到PsnCnfBuild，进行封装
    cnfBuild.setCnfPub(cnfPubs);

    // 下个封装操作
    next.assemble(cnfBuild);
  }
}
