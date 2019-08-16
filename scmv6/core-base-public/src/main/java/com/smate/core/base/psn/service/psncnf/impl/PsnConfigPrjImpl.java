package com.smate.core.base.psn.service.psncnf.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPrjDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigList;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrjPk;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfEasy;
import com.smate.core.base.psn.service.psncnf.PsnCnfExable;

/**
 * 个人配置：项目
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfPrjService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigPrjImpl extends PsnConfigAbstract implements PsnCnfExable {

  @Autowired
  private PsnConfigPrjDao psnConfigPrjDao;

  @Autowired
  private PsnCnfEasy psnCnfListService;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    Assert.isInstanceOf(PsnConfigPrj.class, psnCnfBase);
    PsnConfigPrj cnfPrj = (PsnConfigPrj) psnCnfBase;
    Assert.notNull(cnfPrj.getId());
    // 检查主键格式
    boolean hasCnfId = psnCnfBase.getCnfId() != null && psnCnfBase.getCnfId() > 0;
    boolean hasPrjId = cnfPrj.getId().getPrjId() != null && cnfPrj.getId().getPrjId() > 0;
    Assert.isTrue(hasCnfId || hasPrjId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查权限格式
    PsnConfigPrj psnConfigPrjNew = (PsnConfigPrj) psnCnfBase;
    Assert.state(psnConfigPrjNew.getAnyUser() > 0 && psnConfigPrjNew.getAnyUser() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigPrjNew.getAnyView() > 0 && psnConfigPrjNew.getAnyView() <= PsnCnfConst.ALLOWS);

    PsnConfigPrj psnConfigPrj = (PsnConfigPrj) this.doGet(psnCnfBase);
    if (!psnConfigPrjNew.equals(psnConfigPrj)) {// 存在数据变化
      if (psnConfigPrj != null) {// 更新
        psnConfigPrj.setAnyUser(psnConfigPrjNew.getAnyUser());
        psnConfigPrj.setAnyView(psnConfigPrjNew.getAnyView());
        psnConfigPrj.setUpdateDate(psnConfigPrjNew.getUpdateDate());
        psnConfigPrjDao.save(psnConfigPrj);
      } else {// 新增
        psnConfigPrjDao.save(psnConfigPrjNew);
      }
      this.compose(psnCnfBase);// 列表结果汇总重算
    }

  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    psnConfigPrjDao.getSession().clear();// scm-6484 同时删除多条项目时后台报错
    PsnConfigPrj psnConfigPrj = (PsnConfigPrj) psnCnfBase;
    PsnConfigPrjPk pk = psnConfigPrj.getId();
    if (pk.getCnfId() != null && pk.getPrjId() != null && pk.getCnfId() > 0 && pk.getPrjId() > 0) {
      psnConfigPrjDao.delete(psnConfigPrj);
    } else {// 批量删除
      Assert.notNull(pk.getCnfId());
      psnConfigPrjDao.dels(pk.getCnfId());
    }
    this.compose(psnCnfBase);// 列表结果汇总重算
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigPrj psnConfigPrj = (PsnConfigPrj) psnCnfBase;
    // 查询对象
    return psnConfigPrjDao.get(psnConfigPrj.getId());
  }

  @Override
  List<PsnConfigPrj> doGets(PsnCnfBase psnCnfBase) throws ServiceException {
    return psnConfigPrjDao.gets(psnCnfBase.getCnfId());
  }

  @Override
  public void compose(PsnCnfBase psnCnfBase) throws ServiceException {

    // 列表结果汇总对象
    PsnConfigList psnConfigList = (PsnConfigList) psnCnfListService.get(psnCnfBase);

    if (psnConfigList != null) {
      // 结果重算
      Integer anyUser = 0;
      Integer anyView = 0;
      List<PsnConfigPrj> list = this.doGets(psnCnfBase);
      for (PsnConfigPrj item : list) {
        anyUser |= item.getAnyUser();
        anyView |= item.getAnyView();
      }

      // 结果更新
      psnConfigList.setAnyUserPrjs(anyUser);
      psnConfigList.setAnyViewPrjs(anyView);
      psnCnfListService.save(psnConfigList);
    }
  }

}
