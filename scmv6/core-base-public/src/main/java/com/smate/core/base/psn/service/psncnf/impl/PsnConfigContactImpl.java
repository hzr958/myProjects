package com.smate.core.base.psn.service.psncnf.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.psncnf.PsnConfigContactDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigContact;
import com.smate.core.base.psn.model.psncnf.PsnConfigList;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfEasy;
import com.smate.core.base.psn.service.psncnf.PsnCnfExable;

/**
 * 个人配置：联系方式
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfContactService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigContactImpl extends PsnConfigAbstract implements PsnCnfExable {

  @Autowired
  private PsnConfigContactDao psnConfigContactDao;

  @Autowired
  private PsnCnfEasy psnCnfListService;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    Assert.isInstanceOf(PsnConfigContact.class, psnCnfBase);
    // 检查主键格式
    boolean hasCnfId = psnCnfBase.getCnfId() != null && psnCnfBase.getCnfId() > 0;
    Assert.isTrue(hasCnfId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查权限格式
    PsnConfigContact psnConfigContactNew = (PsnConfigContact) psnCnfBase;
    Assert.state(
        psnConfigContactNew.getAnyUserEmail() > 0 && psnConfigContactNew.getAnyUserEmail() <= PsnCnfConst.ALLOWS);
    Assert.state(
        psnConfigContactNew.getAnyViewEmail() > 0 && psnConfigContactNew.getAnyViewEmail() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigContactNew.getAnyUserTel() > 0 && psnConfigContactNew.getAnyUserTel() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigContactNew.getAnyViewTel() > 0 && psnConfigContactNew.getAnyViewTel() <= PsnCnfConst.ALLOWS);
    Assert.state(
        psnConfigContactNew.getAnyUserMobile() > 0 && psnConfigContactNew.getAnyUserMobile() <= PsnCnfConst.ALLOWS);
    Assert.state(
        psnConfigContactNew.getAnyViewMobile() > 0 && psnConfigContactNew.getAnyViewMobile() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigContactNew.getAnyUserQq() > 0 && psnConfigContactNew.getAnyUserQq() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigContactNew.getAnyViewQq() > 0 && psnConfigContactNew.getAnyViewQq() <= PsnCnfConst.ALLOWS);
    Assert.state(
        psnConfigContactNew.getAnyUserSkype() > 0 && psnConfigContactNew.getAnyUserSkype() <= PsnCnfConst.ALLOWS);
    Assert.state(
        psnConfigContactNew.getAnyViewSkype() > 0 && psnConfigContactNew.getAnyViewSkype() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigContactNew.getAnyUserMsn() > 0 && psnConfigContactNew.getAnyUserMsn() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigContactNew.getAnyViewMsn() > 0 && psnConfigContactNew.getAnyViewMsn() <= PsnCnfConst.ALLOWS);

    PsnConfigContact psnConfigContact = (PsnConfigContact) this.doGet(psnCnfBase);
    if (!psnConfigContactNew.equals(psnConfigContact)) {// 存在数据变化
      if (psnConfigContact != null) {// 更新
        psnConfigContact.setAnyUserEmail(psnConfigContactNew.getAnyUserEmail());
        psnConfigContact.setAnyViewEmail(psnConfigContactNew.getAnyViewEmail());
        psnConfigContact.setAnyUserTel(psnConfigContactNew.getAnyUserTel());
        psnConfigContact.setAnyViewTel(psnConfigContactNew.getAnyViewTel());
        psnConfigContact.setAnyUserMobile(psnConfigContactNew.getAnyUserMobile());
        psnConfigContact.setAnyViewMobile(psnConfigContactNew.getAnyViewMobile());
        psnConfigContact.setAnyUserQq(psnConfigContactNew.getAnyUserQq());
        psnConfigContact.setAnyViewQq(psnConfigContactNew.getAnyViewQq());
        psnConfigContact.setAnyUserSkype(psnConfigContactNew.getAnyUserSkype());
        psnConfigContact.setAnyViewSkype(psnConfigContactNew.getAnyViewSkype());
        psnConfigContact.setAnyUserMsn(psnConfigContactNew.getAnyUserMsn());
        psnConfigContact.setAnyViewMsn(psnConfigContactNew.getAnyViewMsn());

        psnConfigContact.setUpdateDate(psnConfigContactNew.getUpdateDate());
        psnConfigContactDao.save(psnConfigContact);
      } else {// 新增
        psnConfigContactDao.save(psnConfigContactNew);
      }
      this.compose(psnCnfBase);// 列表结果汇总重算
    }

  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigContact psnConfigContact = (PsnConfigContact) this.doGet(psnCnfBase);
    if (psnConfigContact != null) {// 存在对象，则删除
      psnConfigContactDao.delete(psnConfigContact);
      this.compose(psnCnfBase);// 列表结果汇总重算
    }
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {
    // 查询对象
    return psnConfigContactDao.get(psnCnfBase.getCnfId());
  }

  @Override
  public void compose(PsnCnfBase psnCnfBase) throws ServiceException {
    // 列表结果汇总对象
    PsnConfigList psnConfigList = (PsnConfigList) psnCnfListService.get(psnCnfBase);

    if (psnConfigList != null) {
      // 结果重算
      Integer anyUser = 0;
      Integer anyView = 0;
      PsnConfigContact cnfContact = (PsnConfigContact) this.doGet(psnCnfBase);

      anyUser |= cnfContact.getAnyUserEmail();
      anyView |= cnfContact.getAnyViewEmail();
      anyUser |= cnfContact.getAnyUserMobile();
      anyView |= cnfContact.getAnyViewMobile();
      anyUser |= cnfContact.getAnyUserMsn();
      anyView |= cnfContact.getAnyViewMsn();
      anyUser |= cnfContact.getAnyUserTel();
      anyView |= cnfContact.getAnyViewTel();
      anyUser |= cnfContact.getAnyUserQq();
      anyView |= cnfContact.getAnyViewQq();
      anyUser |= cnfContact.getAnyUserSkype();
      anyView |= cnfContact.getAnyViewSkype();

      // 结果更新
      psnConfigList.setAnyUserContacts(anyUser);
      psnConfigList.setAnyViewContacts(anyView);
      psnCnfListService.save(psnConfigList);
    }
  }

}
