package com.smate.core.base.psn.service.psncnf.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.model.psncnf.PsnConfigBrief;
import com.smate.core.base.psn.model.psncnf.PsnConfigContact;
import com.smate.core.base.psn.model.psncnf.PsnConfigEdu;
import com.smate.core.base.psn.model.psncnf.PsnConfigExpertise;
import com.smate.core.base.psn.model.psncnf.PsnConfigList;
import com.smate.core.base.psn.model.psncnf.PsnConfigMoudle;
import com.smate.core.base.psn.model.psncnf.PsnConfigPosition;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigTaught;
import com.smate.core.base.psn.model.psncnf.PsnConfigWork;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfEasy;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;
import com.smate.core.base.utils.cache.SnsCacheService;

/**
 * 个人配置工厂类：包含保存、删除和获取记录。 方法要求传入：PsnCnfBase的子类
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfService")
@Transactional(rollbackFor = Exception.class)
public class PsnCnfFactory implements PsnCnfService {
  // 保存、删除和单记录获取 start

  @Resource(name = "cnfService")
  private PsnCnfEasy cnfEasy;
  @Resource(name = "psnCnfListService")
  private PsnCnfEasy listEasy;
  @Resource(name = "psnCnfBriefService")
  private PsnCnfEasy briefEasy;
  @Resource(name = "psnCnfContactService")
  private PsnCnfEasy contactEasy;
  @Resource(name = "psnCnfEduService")
  private PsnCnfEasy eduEasy;
  @Resource(name = "psnCnfWorkService")
  private PsnCnfEasy workEasy;
  @Resource(name = "psnCnfExpertiseService")
  private PsnCnfEasy expertiseEasy;
  @Resource(name = "psnCnfMoudleService")
  private PsnCnfEasy moudleEasy;
  @Resource(name = "psnCnfPrjService")
  private PsnCnfEasy prjEasy;
  @Resource(name = "psnCnfPubService")
  private PsnCnfEasy pubEasy;
  @Resource(name = "psnCnfTaughtService")
  private PsnCnfEasy taughtEasy;
  @Resource(name = "psnCnfPositionService")
  private PsnCnfEasy positionEasy;
  @Resource(name = "snsCacheService")
  private SnsCacheService snsCacheService;

  // 保存、删除和单记录获取 end
  // 初始化配置Map
  // 个人配置服务Map：保存、删除和单记录获取
  private final Map<Class<?>, PsnCnfEasy> easyAdapters = new HashMap<Class<?>, PsnCnfEasy>();

  @PostConstruct
  public void init() {
    Class<?>[] easyKeyArr = {PsnConfig.class, PsnConfigList.class, PsnConfigBrief.class, PsnConfigContact.class,
        PsnConfigEdu.class, PsnConfigWork.class, PsnConfigExpertise.class, PsnConfigMoudle.class, PsnConfigPrj.class,
        PsnConfigPub.class, PsnConfigTaught.class, PsnConfigPosition.class};
    PsnCnfEasy[] easySrvArr = {cnfEasy, listEasy, briefEasy, contactEasy, eduEasy, workEasy, expertiseEasy, moudleEasy,
        prjEasy, pubEasy, taughtEasy, positionEasy};
    Assert.isTrue(easyKeyArr.length == easySrvArr.length);

    for (int i = 0; i < easyKeyArr.length; i++) {
      easyAdapters.put(easyKeyArr[i], easySrvArr[i]);
    }
  }

  @Override
  public void save(PsnCnfBase cnfBase) throws ServiceException {
    PsnCnfEasy psnCnfEasy = easyAdapters.get(cnfBase.getClass());
    psnCnfEasy.save(cnfBase);
    this.delCached(cnfBase);// 删除缓存
  }

  @Override
  public void save(Long psnId, PsnCnfBase cnfBase) throws ServiceException {
    PsnConfig cnf = new PsnConfig(psnId);
    PsnConfig cnfExist = this.get(cnf);
    Assert.notNull(cnfExist);
    PsnCnfUtils.convertCnfId(cnfBase, cnfExist);
    this.save(cnfBase);
  }

  @Override
  public void save(Long psnId, List<PsnCnfBase> cnfList) throws ServiceException {
    PsnConfig cnf = new PsnConfig(psnId);
    PsnConfig cnfExist = this.get(cnf);
    Assert.notNull(cnfExist);
    for (PsnCnfBase cnfBase : cnfList) {
      PsnCnfUtils.convertCnfId(cnfBase, cnfExist);
      this.save(cnfBase);
    }
  }

  @Override
  public void del(PsnCnfBase cnfBase) throws ServiceException {
    this.delCached(cnfBase);// 删除缓存
    PsnCnfEasy psnCnfEasy = easyAdapters.get(cnfBase.getClass());
    psnCnfEasy.del(cnfBase);

  }

  @Override
  public void del(Long psnId, PsnCnfBase cnfBase) throws ServiceException {

    PsnConfig cnf = new PsnConfig(psnId);
    PsnConfig cnfExist = this.get(cnf);
    Assert.notNull(cnfExist);
    PsnCnfUtils.convertCnfId(cnfBase, cnfExist);
    this.del(cnfBase);

  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T get(PsnCnfBase cnfBase) throws ServiceException {

    PsnCnfEasy psnCnfEasy = easyAdapters.get(cnfBase.getClass());
    return (T) psnCnfEasy.get(cnfBase);
  }

  @Override
  public <T> T get(Long psnId, PsnCnfBase cnfBase) throws ServiceException {
    PsnConfig cnf = new PsnConfig(psnId);
    PsnConfig cnfExist = this.get(cnf);
    Assert.notNull(cnfExist);
    PsnCnfUtils.convertCnfId(cnfBase, cnfExist);

    return this.get(cnfBase);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T gets(PsnCnfBase cnfBase) throws ServiceException {

    PsnCnfEasy psnCnfEasy = easyAdapters.get(cnfBase.getClass());
    return (T) psnCnfEasy.gets(cnfBase);
  }

  @Override
  public void log(Long psnId, Integer status) throws ServiceException {
    this.log(psnId, status, null);
  }

  @Override
  public void log(Long psnId, Integer status, String errMsg) throws ServiceException {
    PsnConfig cnfExist = this.get(new PsnConfig(psnId));
    if (cnfExist != null) {// 记录执行失败日志
      if (!PsnCnfConst.CNF_TORUN_STATUS.equals(cnfExist.getStatus())) {// 非运行状态，忽略
        return;
      }
      cnfExist.setStatus(status);
      errMsg = ObjectUtils.toString(errMsg);
      if (errMsg.length() > 50) {
        errMsg = errMsg.substring(0, 50);
      }
      cnfExist.setErrMsg(errMsg);
      cnfExist.setUpdateDate(new Date());
      this.save(cnfExist);
    }
  }

  // 删除缓存(PsnCnfBuild)
  private void delCached(PsnCnfBase cnfBase) throws ServiceException {
    PsnConfig cnf = new PsnConfig();
    if (cnfBase != null && cnfBase.getCnfId() != null && cnfBase.getCnfId() > 0) {
      cnf.setCnfId(cnfBase.getCnfId());
      PsnConfig cnfExist = this.get(cnf);
      if (cnfExist != null) {
        snsCacheService.remove(PsnCnfConst.CNF_CACHE_KEY, cnfExist.getPsnId().toString());
      }
    }

  }
}
