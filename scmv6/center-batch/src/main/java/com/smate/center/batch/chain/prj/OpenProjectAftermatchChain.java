package com.smate.center.batch.chain.prj;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.center.batch.context.OpenProjectContext;
import com.smate.center.batch.model.sns.prj.OpenProject;
import com.smate.center.batch.oldXml.prj.PrjXmlConstants;
import com.smate.center.batch.oldXml.prj.ProjectOperationEnum;
import com.smate.center.batch.service.projectmerge.OpenProjectService;
import com.smate.center.batch.service.projectmerge.ProjectLogService;
import com.smate.center.batch.service.psn.PsnStatisticsUpdateService;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.utils.PsnCnfUtils;
import com.smate.core.base.utils.cache.CacheService;

/**
 * 数据保存后的处理
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @throws Exception
 */
public class OpenProjectAftermatchChain implements OpenProjectBaseChain {

  @Autowired
  private ProjectLogService projectLogService;
  @Autowired
  private PsnStatisticsUpdateService psnStatisticsUpdateService;
  @Autowired
  private OpenProjectService openProjectService;

  @Autowired
  private CacheService cacheService;

  @Override
  public boolean can(OpenProjectContext context, OpenProject project) {
    return true;
  }

  @Override
  public OpenProjectContext run(OpenProjectContext context, OpenProject project) throws Exception {
    projectLogService.logOp(context.getCurrentPrjId(), context.getCurrentUserId(), null,
        ProjectOperationEnum.DuplicationMerge, null);
    psnStatisticsUpdateService.updatePsnStatisticsByPrj(context.getCurrentUserId());
    // 个人项目权限设置
    String authority = context.getXmlDocument().getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "authority");
    if (StringUtils.isBlank(authority)) {
      authority = PsnCnfConst.ALLOWS.toString();
    }
    Integer anyUser = Integer.parseInt(authority);
    // 构造权限对象
    PsnConfigPrj cnfPrj = new PsnConfigPrj();
    cnfPrj.getId().setPrjId(context.getCurrentPrjId());
    cnfPrj.setAnyUser(anyUser);
    cnfPrj.setAnyView(cnfPrj.getAnyUser());
    save(context.getCurrentUserId(), cnfPrj);
    return context;
  }

  /**
   * 保存配置前置
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param psnId
   * @param cnfBase
   * @throws Exception
   */
  public void save(Long psnId, PsnConfigPrj cnfBase) throws Exception {
    PsnConfig cnf = new PsnConfig(psnId);
    PsnConfig cnfExist = (PsnConfig) openProjectService.prjEasyGet(cnf);
    Assert.notNull(cnfExist);
    PsnCnfUtils.convertCnfId(cnfBase, cnfExist);
    this.save(cnfBase);
  }

  /**
   * 保存配置
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param psnId
   * @param cnfBase
   * @throws Exception
   */
  public void save(PsnCnfBase cnfBase) throws Exception {
    openProjectService.PrjEasySave(cnfBase);
    this.delCached(cnfBase);// 删除缓存
  }

  /**
   * 删除缓存(PsnCnfBuild)
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param cnfBase
   * @throws Exception
   */
  private void delCached(PsnCnfBase cnfBase) throws Exception {
    PsnConfig cnf = new PsnConfig();
    if (cnfBase != null && cnfBase.getCnfId() != null && cnfBase.getCnfId() > 0) {
      cnf.setCnfId(cnfBase.getCnfId());
      PsnConfig cnfExist = (PsnConfig) openProjectService.prjEasyGet(cnf);
      if (cnfExist != null) {
        cacheService.remove(PsnCnfConst.CNF_CACHE_KEY, cnfExist.getPsnId().toString());
      }
    }

  }
}
