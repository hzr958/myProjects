package com.smate.core.base.psn.service.psncnf.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.psncnf.PsnConfigTaughtDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigTaught;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;

/**
 * 个人配置：所教课程
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfTaughtService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigTaughtImpl extends PsnConfigAbstract {

  @Autowired
  private PsnConfigTaughtDao psnConfigTaughtDao;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    Assert.isInstanceOf(PsnConfigTaught.class, psnCnfBase);
    // 检查主键格式
    boolean hasCnfId = psnCnfBase.getCnfId() != null && psnCnfBase.getCnfId() > 0;
    Assert.isTrue(hasCnfId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查权限格式
    PsnConfigTaught psnConfigTaughtNew = (PsnConfigTaught) psnCnfBase;
    Assert.state(psnConfigTaughtNew.getAnyUser() > 0 && psnConfigTaughtNew.getAnyUser() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigTaughtNew.getAnyView() > 0 && psnConfigTaughtNew.getAnyView() <= PsnCnfConst.ALLOWS);

    PsnConfigTaught psnConfigTaught = (PsnConfigTaught) this.doGet(psnCnfBase);
    if (!psnConfigTaughtNew.equals(psnConfigTaught)) {// 存在数据变化
      if (psnConfigTaught != null) {// 更新
        psnConfigTaught.setAnyUser(psnConfigTaughtNew.getAnyUser());
        psnConfigTaught.setAnyView(psnConfigTaughtNew.getAnyView());
        psnConfigTaught.setUpdateDate(psnConfigTaughtNew.getUpdateDate());
        psnConfigTaughtDao.save(psnConfigTaught);
      } else {// 新增
        psnConfigTaughtDao.save(psnConfigTaughtNew);
      }
    }

  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigTaught psnConfigTaught = (PsnConfigTaught) this.doGet(psnCnfBase);
    if (psnConfigTaught != null) {// 存在对象，则删除
      psnConfigTaughtDao.delete(psnConfigTaught);
    }
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {
    // 查询对象
    return psnConfigTaughtDao.get(psnCnfBase.getCnfId());
  }
}
