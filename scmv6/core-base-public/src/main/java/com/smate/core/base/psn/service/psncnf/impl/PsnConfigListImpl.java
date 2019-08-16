package com.smate.core.base.psn.service.psncnf.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.dao.psncnf.PsnConfigListDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigList;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;

/**
 * 个人配置：个人简介
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfListService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigListImpl extends PsnConfigAbstract {

  @Autowired
  private PsnConfigListDao psnConfigListDao;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    // 检查主键格式
    boolean hasCnfId = psnCnfBase.getCnfId() != null && psnCnfBase.getCnfId() > 0;
    Assert.isTrue(hasCnfId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查权限格式
    Assert.isInstanceOf(PsnConfigList.class, psnCnfBase);
    PsnConfigList psnConfigList = (PsnConfigList) psnCnfBase;

    if (psnConfigList != null) {// 更新
      psnConfigList.setUpdateDate(psnConfigList.getUpdateDate());
      psnConfigListDao.save(psnConfigList);
    }
  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigList psnConfigList = (PsnConfigList) this.doGet(psnCnfBase);
    if (psnConfigList != null) {// 存在对象，则删除
      psnConfigListDao.delete(psnConfigList);
    }
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {

    // 查询对象
    return psnConfigListDao.get(psnCnfBase.getCnfId());
  }

}
