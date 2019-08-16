package com.smate.core.base.psn.service.psncnf.impl;

import java.util.ArrayList;
import java.util.List;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;
import com.smate.core.base.psn.service.psncnf.PsnCnfEasy;

/**
 * 个人配置内部抽象类.
 * 
 * @author zhuangyanming
 * 
 */
abstract class PsnConfigAbstract implements PsnCnfEasy {

  @Override
  public PsnCnfBase get(PsnCnfBase psnCnfBase) throws ServiceException {

    this.doVerify(psnCnfBase);
    return this.doGet(psnCnfBase);
  }

  @Override
  public void save(PsnCnfBase psnCnfBase) throws ServiceException {

    this.doVerify(psnCnfBase);
    this.doSave(psnCnfBase);

  }

  @Override
  public void del(PsnCnfBase psnCnfBase) throws ServiceException {

    this.doVerify(psnCnfBase);
    this.doDel(psnCnfBase);
  }

  @Override
  public List<?> gets(PsnCnfBase psnCnfBase) throws ServiceException {

    this.doVerify(psnCnfBase);
    return this.doGets(psnCnfBase);
  }

  /**
   * 获取数据
   * 
   * @param psnCnfBase
   * @return
   * @throws ServiceException
   */
  abstract PsnCnfBase doGet(PsnCnfBase psnCnfBase) throws ServiceException;

  /**
   * 执行保存：新增或修改
   * 
   * @param psnCnfBase
   * @throws ServiceException
   */
  abstract void doSave(PsnCnfBase psnCnfBase) throws ServiceException;

  /**
   * 执行删除
   * 
   * @param psnCnfBase
   * @throws ServiceException
   */
  abstract void doDel(PsnCnfBase psnCnfBase) throws ServiceException;

  /**
   * 必须执行校验的内容
   * 
   * @param psnCnfBase
   * @throws ServiceException
   */
  abstract void doVerify(PsnCnfBase psnCnfBase) throws ServiceException;

  /**
   * 获取多条数据.
   * 
   * @param psnCnfBase
   * @return
   * @throws ServiceException
   */
  List<?> doGets(PsnCnfBase psnCnfBase) throws ServiceException {
    List<PsnCnfBase> slist = new ArrayList<PsnCnfBase>();
    PsnCnfBase get = this.get(psnCnfBase);
    if (get != null) {
      slist.add(get);
    }
    return slist;
  }
}
