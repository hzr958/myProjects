package com.smate.core.base.psn.service.psncnf.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPubDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigList;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigPubPk;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfEasy;
import com.smate.core.base.psn.service.psncnf.PsnCnfExable;

/**
 * 个人配置：成果
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfPubService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigPubImpl extends PsnConfigAbstract implements PsnCnfExable {

  @Autowired
  private PsnConfigPubDao psnConfigPubDao;

  @Autowired
  private PsnCnfEasy psnCnfListService;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    Assert.isInstanceOf(PsnConfigPub.class, psnCnfBase);
    PsnConfigPub cnfPub = (PsnConfigPub) psnCnfBase;
    Assert.notNull(cnfPub.getId());
    // 检查主键格式
    boolean hasCnfId = psnCnfBase.getCnfId() != null && psnCnfBase.getCnfId() > 0;
    boolean hasPubId = cnfPub.getId().getPubId() != null && cnfPub.getId().getPubId() > 0;
    Assert.isTrue(hasCnfId || hasPubId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查权限格式
    PsnConfigPub psnConfigPubNew = (PsnConfigPub) psnCnfBase;
    Assert.state(psnConfigPubNew.getAnyUser() > 0 && psnConfigPubNew.getAnyUser() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigPubNew.getAnyView() > 0 && psnConfigPubNew.getAnyView() <= PsnCnfConst.ALLOWS);

    PsnConfigPub psnConfigPub = (PsnConfigPub) this.doGet(psnCnfBase);
    if (!psnConfigPubNew.equals(psnConfigPub)) {// 存在数据变化
      if (psnConfigPub != null) {// 更新
        psnConfigPub.setAnyUser(psnConfigPubNew.getAnyUser());
        psnConfigPub.setAnyView(psnConfigPubNew.getAnyView());
        psnConfigPub.setUpdateDate(psnConfigPubNew.getUpdateDate());
        psnConfigPubDao.save(psnConfigPub);
      } else {// 新增
        psnConfigPubDao.save(psnConfigPubNew);
      }
      this.compose(psnCnfBase);// 列表结果汇总重算
    }

  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    psnConfigPubDao.getSession().clear();// scm-6484 同时删除多条成果时后台报错
    PsnConfigPub psnConfigPub = (PsnConfigPub) psnCnfBase;
    PsnConfigPubPk pk = psnConfigPub.getId();
    if (pk.getCnfId() != null && pk.getPubId() != null && pk.getCnfId() > 0 && pk.getPubId() > 0) {
      psnConfigPubDao.delete(psnConfigPub);
    } else {// 批量删除
      Assert.notNull(pk.getCnfId());
      psnConfigPubDao.dels(pk.getCnfId());
    }
    this.compose(psnCnfBase);// 列表结果汇总重算
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigPub psnConfigPub = (PsnConfigPub) psnCnfBase;
    // 查询对象
    return psnConfigPubDao.get(psnConfigPub.getId());
  }

  @Override
  List<PsnConfigPub> doGets(PsnCnfBase psnCnfBase) throws ServiceException {
    return psnConfigPubDao.gets(psnCnfBase.getCnfId());
  }

  @Override
  public void compose(PsnCnfBase psnCnfBase) throws ServiceException {

    // 列表结果汇总对象
    PsnConfigList psnConfigList = (PsnConfigList) psnCnfListService.get(psnCnfBase);

    if (psnConfigList != null) {
      // 结果重算
      Integer anyUser = 0;
      Integer anyView = 0;
      List<PsnConfigPub> list = this.doGets(psnCnfBase);
      for (PsnConfigPub item : list) {
        anyUser |= item.getAnyUser();
        anyView |= item.getAnyView();
      }

      // 结果更新
      psnConfigList.setAnyUserPubs(anyUser);
      psnConfigList.setAnyViewPubs(anyView);
      psnCnfListService.save(psnConfigList);
    }
  }
}
