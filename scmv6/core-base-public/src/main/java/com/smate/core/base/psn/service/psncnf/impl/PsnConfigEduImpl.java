package com.smate.core.base.psn.service.psncnf.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.psncnf.PsnConfigEduDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigEdu;
import com.smate.core.base.psn.model.psncnf.PsnConfigEduPk;
import com.smate.core.base.psn.model.psncnf.PsnConfigList;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfEasy;
import com.smate.core.base.psn.service.psncnf.PsnCnfExable;

/**
 * 个人配置：教育经历
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfEduService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigEduImpl extends PsnConfigAbstract implements PsnCnfExable {

  @Autowired
  private PsnConfigEduDao psnConfigEduDao;

  @Autowired
  private PsnCnfEasy psnCnfListService;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    Assert.isInstanceOf(PsnConfigEdu.class, psnCnfBase);
    PsnConfigEdu cnfEdu = (PsnConfigEdu) psnCnfBase;
    Assert.notNull(cnfEdu.getId());
    // 检查主键格式
    boolean hasCnfId = psnCnfBase.getCnfId() != null && psnCnfBase.getCnfId() > 0;
    boolean hasEduId = cnfEdu.getId().getEduId() != null && cnfEdu.getId().getEduId() > 0;
    Assert.isTrue(hasCnfId || hasEduId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查权限格式
    PsnConfigEdu psnConfigEduNew = (PsnConfigEdu) psnCnfBase;
    Assert.state(psnConfigEduNew.getAnyUser() > 0 && psnConfigEduNew.getAnyUser() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigEduNew.getAnyView() > 0 && psnConfigEduNew.getAnyView() <= PsnCnfConst.ALLOWS);

    PsnConfigEdu psnConfigEdu = (PsnConfigEdu) this.doGet(psnCnfBase);
    if (!psnConfigEduNew.equals(psnConfigEdu)) {// 存在数据变化
      if (psnConfigEdu != null) {// 更新
        psnConfigEdu.setAnyUser(psnConfigEduNew.getAnyUser());
        psnConfigEdu.setAnyView(psnConfigEduNew.getAnyView());
        psnConfigEdu.setUpdateDate(psnConfigEduNew.getUpdateDate());
        psnConfigEduDao.save(psnConfigEdu);
      } else {// 新增
        psnConfigEduDao.save(psnConfigEduNew);
      }
      this.compose(psnCnfBase);// 列表结果汇总重算
    }

  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigEdu psnConfigEdu = (PsnConfigEdu) psnCnfBase;
    PsnConfigEduPk pk = psnConfigEdu.getId();
    if (pk.getCnfId() != null && pk.getEduId() != null && pk.getCnfId() > 0 && pk.getEduId() > 0) {
      psnConfigEduDao.delete(psnConfigEdu);
    } else {// 批量删除
      Assert.notNull(pk.getCnfId());
      psnConfigEduDao.dels(pk.getCnfId());
    }
    this.compose(psnCnfBase);// 列表结果汇总重算
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigEdu psnConfigEdu = (PsnConfigEdu) psnCnfBase;
    // 查询对象
    return psnConfigEduDao.get(psnConfigEdu.getId());
  }

  @Override
  List<PsnConfigEdu> doGets(PsnCnfBase psnCnfBase) throws ServiceException {
    return psnConfigEduDao.gets(psnCnfBase.getCnfId());
  }

  @Override
  public void compose(PsnCnfBase psnCnfBase) throws ServiceException {

    // 列表结果汇总对象
    PsnConfigList psnConfigList = (PsnConfigList) psnCnfListService.get(psnCnfBase);

    if (psnConfigList != null) {
      // 结果重算
      Integer anyUser = 0;
      Integer anyView = 0;
      List<PsnConfigEdu> list = this.doGets(psnCnfBase);
      for (PsnConfigEdu item : list) {
        anyUser |= item.getAnyUser();
        anyView |= item.getAnyView();
      }

      // 结果更新
      psnConfigList.setAnyUserEdus(anyUser);
      psnConfigList.setAnyViewEdus(anyView);
      psnCnfListService.save(psnConfigList);
    }
  }

}
