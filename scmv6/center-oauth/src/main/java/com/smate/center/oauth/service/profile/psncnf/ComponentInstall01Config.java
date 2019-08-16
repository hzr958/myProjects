package com.smate.center.oauth.service.profile.psncnf;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;

@Service("install01Config")
@Transactional(rollbackFor = Exception.class)
public class ComponentInstall01Config implements ComponentStart, ComponentInstall {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "install02Moudle")
  private ComponentInstall next;

  @Override
  public void start(Long psnId) throws Exception {
    // 创建新数据
    PsnConfig cnfExist = null;
    try {
      PsnConfig cnf = new PsnConfig(psnId);
      cnfExist = psnCnfService.get(cnf);
      Assert.notNull(cnfExist);
      if (!PsnCnfConst.CNF_TORUN_STATUS.equals(cnfExist.getStatus())) {// 非运行状态，忽略
        return;
      }
    } catch (Exception e) {
      throw new Exception("个人配置：install01Config失败", e);
    }

    Long runs = cnfExist.getRuns();
    Long cnfId = cnfExist.getCnfId();
    // 添加数据
    if (PsnCnfUtils.canRun(runs, PsnCnfEnum.DIRTY)) {
      this.install(runs, cnfId, psnId);
    }
  }

  @Override
  public void install(Long runs, Long cnfId, Long psnId) throws Exception {
    // 下个创建操作
    next.install(runs, cnfId, psnId);
  }
}
