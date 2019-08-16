package com.smate.web.psn.service.psncnf.build;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.web.psn.cache.PsnCacheService;
import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.psncnf.PsnCnfBuild;

/**
 * 个人配置：封装
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfBuildService")
@Transactional(rollbackFor = Exception.class)
public class PsnCnfBuildFactory implements PsnCnfBuildService {

  @Resource(name = "component01Config")
  private ComponentBase component;


  @Autowired
  private PsnCacheService psnCacheService;

  @Override
  public PsnCnfBuild get(Long psnId) throws ServiceException, PsnCnfException {
    Assert.notNull(psnId);
    Assert.state(psnId > 0);
    /*
     * PsnCnfBuild cnfBuild = (PsnCnfBuild) psnCacheService.get(PsnCnfConst.CNF_CACHE_KEY,
     * psnId.toString()); if (cnfBuild == null) {
     */
    // TODO 暂时 不存放缓存 避免与老系统冲突 tsz
    PsnCnfBuild cnfBuild = new PsnCnfBuild(psnId);
    component.assemble(cnfBuild);
    /*
     * // 写入缓存 psnCacheService.put(PsnCnfConst.CNF_CACHE_KEY, PsnCnfConst.CNF_CACHE_TIME_OUT,
     * psnId.toString(), cnfBuild); }
     */

    return cnfBuild;
  }

}
