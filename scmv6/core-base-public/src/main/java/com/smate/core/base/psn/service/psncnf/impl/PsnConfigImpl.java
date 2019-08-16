package com.smate.core.base.psn.service.psncnf.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfExable;

/**
 * 个人配置：主表
 * 
 * @author zhuangyanming
 * 
 */
@Service("cnfService")
@Transactional(rollbackFor = Exception.class)
class PsnConfigImpl extends PsnConfigAbstract implements PsnCnfExable {

  @Autowired
  private PsnConfigDao psnConfigDao;

  @Override
  public void doVerify(PsnCnfBase psnCnfBase) throws ServiceException {
    // 检查对象格式
    Assert.notNull(psnCnfBase);
    // 检查权限格式
    Assert.isInstanceOf(PsnConfig.class, psnCnfBase);
    PsnConfig psnConfig = (PsnConfig) psnCnfBase;
    // 检查主键格式
    boolean hasCnfId = psnConfig.getCnfId() != null && psnConfig.getCnfId() > 0;
    boolean hasPsnId = psnConfig.getPsnId() != null && psnConfig.getPsnId() > 0;
    Assert.isTrue(hasCnfId || hasPsnId);
  }

  @Override
  public void doSave(PsnCnfBase psnCnfBase) throws ServiceException {

    PsnConfig psnConfigNew = (PsnConfig) psnCnfBase;

    PsnConfig psnConfig = (PsnConfig) this.doGet(psnCnfBase);
    if (!psnConfigNew.equals(psnConfig)) {// 存在数据变化
      if (psnConfig != null) {// 更新
        psnConfig.setStatus(psnConfigNew.getStatus());
        psnConfig.setRuns(psnConfigNew.getRuns());
        psnConfig.setUpdateDate(psnConfigNew.getUpdateDate());
        psnConfigDao.save(psnConfig);
      } else {// 新增
        // 检查人员主键格式
        Assert.isTrue(psnConfigNew.getPsnId() > 0);
        psnConfigDao.save(psnConfigNew);
      }
    }
  }

  @Override
  public void doDel(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfig psnConfig = (PsnConfig) this.doGet(psnCnfBase);
    if (psnConfig != null) {// 存在对象，则删除
      psnConfigDao.delete(psnConfig);
    }
  }

  @Override
  public PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException {
    PsnConfig psnConfig = (PsnConfig) psnCnfBase;
    // 查询对象
    if (psnConfig.getCnfId() != null && psnConfig.getCnfId() > 0) {
      return psnConfigDao.get(psnConfig.getCnfId());
    } else {
      return psnConfigDao.getByPsn(psnConfig.getPsnId());
    }
  }

  @Override
  List<PsnConfig> doGets(PsnCnfBase psnCnfBase) throws ServiceException {
    return psnConfigDao.getList();
  }

  @Override
  public void compose(PsnCnfBase psnCnfBase) throws ServiceException {

  }

}
