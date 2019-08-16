package com.smate.core.base.psn.service.psncnf.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPositionDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigPosition;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;

/**
 * 个人配置：个人简介
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfPositionService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigPositionImpl extends PsnConfigAbstract {

  @Autowired
  private PsnConfigPositionDao psnConfigPositionDao;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    Assert.isInstanceOf(PsnConfigPosition.class, psnCnfBase);
    // 检查主键格式
    boolean hasCnfId = psnCnfBase.getCnfId() != null && psnCnfBase.getCnfId() > 0;
    Assert.isTrue(hasCnfId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查权限格式
    PsnConfigPosition psnConfigPositionNew = (PsnConfigPosition) psnCnfBase;
    Assert.state(psnConfigPositionNew.getAnyUser() > 0 && psnConfigPositionNew.getAnyUser() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigPositionNew.getAnyView() > 0 && psnConfigPositionNew.getAnyView() <= PsnCnfConst.ALLOWS);

    PsnConfigPosition psnConfigPosition = (PsnConfigPosition) this.doGet(psnCnfBase);
    if (!psnConfigPositionNew.equals(psnConfigPosition)) {// 存在数据变化
      if (psnConfigPosition != null) {// 更新
        psnConfigPosition.setAnyUser(psnConfigPositionNew.getAnyUser());
        psnConfigPosition.setAnyView(psnConfigPositionNew.getAnyView());
        psnConfigPosition.setUpdateDate(psnConfigPositionNew.getUpdateDate());
        psnConfigPositionDao.save(psnConfigPosition);
      } else {// 新增
        psnConfigPositionDao.save(psnConfigPositionNew);
      }
    }

  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigPosition psnConfigPosition = (PsnConfigPosition) this.doGet(psnCnfBase);
    if (psnConfigPosition != null) {// 存在对象，则删除
      psnConfigPositionDao.delete(psnConfigPosition);
    }
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {
    // 查询对象
    return psnConfigPositionDao.get(psnCnfBase.getCnfId());
  }

}
