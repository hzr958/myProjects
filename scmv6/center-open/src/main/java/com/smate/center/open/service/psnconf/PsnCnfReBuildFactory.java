package com.smate.center.open.service.psnconf;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;

/**
 * 个人配置：重建数据实现
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfReBuildService")
@Transactional(rollbackFor = Exception.class)
public class PsnCnfReBuildFactory implements PsnCnfReBuildService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "clean01Config")
  private ComponentStart componentClean;

  @Resource(name = "install01Config")
  private ComponentStart componentInstall;

  @Autowired
  private PsnCnfService psnCnfService;

  @Override
  public void start(Long psnId, Long cnfId) throws Exception {
    try {
      componentClean.start(psnId, cnfId);
      componentInstall.start(psnId, cnfId);
      psnCnfService.log(psnId, PsnCnfConst.CNF_SUCC_STATUS);// 重建成功
    } catch (Exception e) {
      logger.error("个人配置重建数据失败", e);
      psnCnfService.log(psnId, PsnCnfConst.CNF_FAIL_STATUS, e.getMessage());
    }
  }

  @Override
  public void init(Long psnId) throws Exception {
    PsnConfig cnf = new PsnConfig(psnId);
    PsnConfig cnfExist = psnCnfService.get(cnf);
    if (cnfExist == null) {// 添加用户配置
      Long allMoudle = Long.valueOf(PsnCnfEnum.ALL.toString());
      Long dirtyAct = Long.valueOf(PsnCnfEnum.DIRTY.toString());
      cnf.setRuns(allMoudle | dirtyAct);
      psnCnfService.save(cnf);
    }

    this.start(psnId, cnf.getCnfId());
  }
}
