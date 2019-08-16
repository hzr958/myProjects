package com.smate.core.base.psn.service.psncnf.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.psncnf.PsnConfigBriefDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigBrief;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;

/**
 * 个人配置：个人简介
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfBriefService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigBriefImpl extends PsnConfigAbstract {

  @Autowired
  private PsnConfigBriefDao psnConfigBriefDao;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    Assert.isInstanceOf(PsnConfigBrief.class, psnCnfBase);
    // 检查主键格式
    boolean hasCnfId = psnCnfBase.getCnfId() != null && psnCnfBase.getCnfId() > 0;
    Assert.isTrue(hasCnfId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查权限格式
    PsnConfigBrief psnConfigBriefNew = (PsnConfigBrief) psnCnfBase;
    Assert.state(psnConfigBriefNew.getAnyUser() > 0 && psnConfigBriefNew.getAnyUser() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigBriefNew.getAnyView() > 0 && psnConfigBriefNew.getAnyView() <= PsnCnfConst.ALLOWS);

    PsnConfigBrief psnConfigBrief = (PsnConfigBrief) this.doGet(psnCnfBase);
    if (!psnConfigBriefNew.equals(psnConfigBrief)) {// 存在数据变化
      if (psnConfigBrief != null) {// 更新
        psnConfigBrief.setAnyUser(psnConfigBriefNew.getAnyUser());
        psnConfigBrief.setAnyView(psnConfigBriefNew.getAnyView());
        psnConfigBrief.setUpdateDate(psnConfigBriefNew.getUpdateDate());
        psnConfigBriefDao.save(psnConfigBrief);
      } else {// 新增
        psnConfigBriefDao.save(psnConfigBriefNew);
      }
    }

  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigBrief psnConfigBrief = (PsnConfigBrief) this.doGet(psnCnfBase);
    if (psnConfigBrief != null) {// 存在对象，则删除
      psnConfigBriefDao.delete(psnConfigBrief);
    }
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {
    // 查询对象
    return psnConfigBriefDao.get(psnCnfBase.getCnfId());
  }

}
