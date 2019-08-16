package com.smate.center.open.service.psnconf;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfigWork;
import com.smate.core.base.psn.model.psncnf.PsnConfigWorkPk;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("install05Work")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall05Work implements ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "install06Edu")
  private ComponentInstall next;

  @Autowired
  private WorkHistoryDao workHistoryDao;

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    try {
      if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY, PsnCnfEnum.WORK)) {
        // 创建新数据
        List<Long> workIdList = workHistoryDao.findWorkId(psnId);
        for (Long workId : workIdList) {
          PsnConfigWork cnfWork = new PsnConfigWork(new PsnConfigWorkPk(cnfId, workId));
          PsnConfigWork cnfWorkExist = psnCnfService.get(cnfWork);
          if (cnfWorkExist == null) {
            psnCnfService.save(cnfWork);
          }
        }
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install05Work失败", e);
    }
    // 下个创建操作
    next.install(runs, cnfId, psnId);
  }
}
