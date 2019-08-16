package com.smate.core.base.psn.service.psncnf.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.psncnf.PsnConfigExpertiseDao;
import com.smate.core.base.psn.model.psncnf.PsnConfigExpertise;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;

/**
 * 个人配置：研究领域
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCnfExpertiseService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigExpertiseImpl extends PsnConfigAbstract {

  @Autowired
  private PsnConfigExpertiseDao psnConfigExpertiseDao;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    Assert.isInstanceOf(PsnConfigExpertise.class, psnCnfBase);
    // 检查主键格式
    boolean hasCnfId = psnCnfBase.getCnfId() != null && psnCnfBase.getCnfId() > 0;
    Assert.isTrue(hasCnfId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查权限格式
    PsnConfigExpertise psnConfigExpertiseNew = (PsnConfigExpertise) psnCnfBase;
    Assert.state(psnConfigExpertiseNew.getAnyUser() > 0 && psnConfigExpertiseNew.getAnyUser() <= PsnCnfConst.ALLOWS);
    Assert.state(psnConfigExpertiseNew.getAnyView() > 0 && psnConfigExpertiseNew.getAnyView() <= PsnCnfConst.ALLOWS);

    PsnConfigExpertise psnConfigExpertise = (PsnConfigExpertise) this.doGet(psnCnfBase);
    if (!psnConfigExpertiseNew.equals(psnConfigExpertise)) {// 存在数据变化
      if (psnConfigExpertise != null) {// 更新
        psnConfigExpertise.setAnyUser(psnConfigExpertiseNew.getAnyUser());
        psnConfigExpertise.setAnyView(psnConfigExpertiseNew.getAnyView());
        psnConfigExpertise.setUpdateDate(psnConfigExpertiseNew.getUpdateDate());
        psnConfigExpertiseDao.save(psnConfigExpertise);
      } else {// 新增
        psnConfigExpertiseDao.save(psnConfigExpertiseNew);
      }
    }

  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfigExpertise psnConfigExpertise = (PsnConfigExpertise) this.doGet(psnCnfBase);
    if (psnConfigExpertise != null) {// 存在对象，则删除
      psnConfigExpertiseDao.delete(psnConfigExpertise);
    }
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {
    // 查询对象
    return psnConfigExpertiseDao.get(psnCnfBase.getCnfId());
  }

}
