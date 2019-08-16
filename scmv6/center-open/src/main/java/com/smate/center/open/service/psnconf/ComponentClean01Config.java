package com.smate.center.open.service.psnconf;

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

@Service("clean01Config")
@Transactional(rollbackFor = Exception.class)
public class ComponentClean01Config implements ComponentStart, ComponentClean {
  @Autowired
  private PsnCnfService psnCnfService;

  @Resource(name = "clean02Moudle")
  private ComponentClean next;

  @Override
  public void start(Long psnId, Long cnfId) throws Exception {
    PsnConfig cnfExist = null;
    try {
      PsnConfig cnf = new PsnConfig(psnId);
      // 由于psn_config表的主键是cnfId, 单纯设置psnId, 事务没提交用psnId查询不到
      cnf.setCnfId(cnfId);
      cnfExist = psnCnfService.get(cnf);
      Assert.notNull(cnfExist);
      if (!PsnCnfConst.CNF_TORUN_STATUS.equals(cnfExist.getStatus())) {// 非运行状态，忽略
        return;
      }
    } catch (Exception e) {
      throw new Exception("个人配置：clean01Config失败", e);
    }
    Long runs = cnfExist.getRuns();
    // Long cnfId = cnfExist.getCnfId();
    if (PsnCnfUtils.canRun(runs, PsnCnfEnum.CLEAN)) {
      this.clean(runs, cnfId);
    }
  }

  @Override
  public void clean(Long runs, Long cnfId) throws Exception {

    // 下个清理操作
    next.clean(runs, cnfId);
  }

}
