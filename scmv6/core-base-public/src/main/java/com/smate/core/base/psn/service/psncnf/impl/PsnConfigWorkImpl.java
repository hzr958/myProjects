package com.smate.core.base.psn.service.psncnf.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.psncnf.PsnConfigWorkDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigList;
import com.smate.core.base.psn.model.psncnf.PsnConfigWork;
import com.smate.core.base.psn.model.psncnf.PsnConfigWorkPk;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfEasy;
import com.smate.core.base.psn.service.psncnf.PsnCnfExable;

/**
 * 个人配置：工作经历
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfWorkService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigWorkImpl extends PsnConfigAbstract implements PsnCnfExable {

  @Autowired
  private PsnConfigWorkDao psnConfigWorkDao;

  @Autowired
  private PsnCnfEasy psnCnfListService;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    Assert.isInstanceOf(PsnConfigWork.class, psnCnfBase);
    PsnConfigWork cnfWork = (PsnConfigWork) psnCnfBase;
    Assert.notNull(cnfWork.getId());
    // 检查主键格式
    boolean hasCnfId = psnCnfBase.getCnfId() != null && psnCnfBase.getCnfId() > 0;
    boolean hasWorkId = cnfWork.getId().getWorkId() != null && cnfWork.getId().getWorkId() > 0;
    Assert.isTrue(hasCnfId || hasWorkId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查权限格式
    PsnConfigWork psnConfigWorkNew = (PsnConfigWork) psnCnfBase;
    Assert.state(psnConfigWorkNew.getAnyUser() > 0 && psnConfigWorkNew.getAnyUser() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigWorkNew.getAnyView() > 0 && psnConfigWorkNew.getAnyView() <= PsnCnfConst.ALLOWS);

    PsnConfigWork psnConfigWork = (PsnConfigWork) this.doGet(psnCnfBase);
    if (!psnConfigWorkNew.equals(psnConfigWork)) {// 存在数据变化
      if (psnConfigWork != null) {// 更新
        psnConfigWork.setAnyUser(psnConfigWorkNew.getAnyUser());
        psnConfigWork.setAnyView(psnConfigWorkNew.getAnyView());
        psnConfigWork.setUpdateDate(psnConfigWorkNew.getUpdateDate());
        psnConfigWorkDao.save(psnConfigWork);
      } else {// 新增
        psnConfigWorkDao.save(psnConfigWorkNew);
      }
      this.compose(psnCnfBase);// 列表结果汇总重算
    }

  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigWork psnConfigWork = (PsnConfigWork) psnCnfBase;
    PsnConfigWorkPk pk = psnConfigWork.getId();
    if (pk.getCnfId() != null && pk.getWorkId() != null && pk.getCnfId() > 0 && pk.getWorkId() > 0) {
      psnConfigWorkDao.delete(psnConfigWork);
    } else {// 批量删除
      Assert.notNull(pk.getCnfId());
      psnConfigWorkDao.dels(pk.getCnfId());
    }
    this.compose(psnCnfBase);// 列表结果汇总重算
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigWork psnConfigWork = (PsnConfigWork) psnCnfBase;
    // 查询对象
    return psnConfigWorkDao.get(psnConfigWork.getId());
  }

  @Override
  List<PsnConfigWork> doGets(PsnCnfBase psnCnfBase) throws ServiceException {
    return psnConfigWorkDao.gets(psnCnfBase.getCnfId());
  }

  @Override
  public void compose(PsnCnfBase psnCnfBase) throws ServiceException {

    // 列表结果汇总对象
    PsnConfigList psnConfigList = (PsnConfigList) psnCnfListService.get(psnCnfBase);

    if (psnConfigList != null) {
      // 结果重算
      Integer anyUser = 0;
      Integer anyView = 0;
      List<PsnConfigWork> list = this.doGets(psnCnfBase);
      for (PsnConfigWork item : list) {
        anyUser |= item.getAnyUser();
        anyView |= item.getAnyView();
      }

      // 结果更新
      psnConfigList.setAnyUserWorks(anyUser);
      psnConfigList.setAnyViewWorks(anyView);
      psnCnfListService.save(psnConfigList);
    }
  }

}
