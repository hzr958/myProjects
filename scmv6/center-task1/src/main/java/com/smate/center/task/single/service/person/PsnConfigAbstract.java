package com.smate.center.task.single.service.person;

import java.util.ArrayList;
import java.util.List;

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
  public PsnCnfBase get(PsnCnfBase psnCnfBase) {

    this.doVerify(psnCnfBase);
    return this.doGet(psnCnfBase);
  }

  @Override
  public void save(PsnCnfBase psnCnfBase) {

    this.doVerify(psnCnfBase);
    this.doSave(psnCnfBase);

  }

  @Override
  public void del(PsnCnfBase psnCnfBase) {

    this.doVerify(psnCnfBase);
    this.doDel(psnCnfBase);
  }

  @Override
  public List<?> gets(PsnCnfBase psnCnfBase) {

    this.doVerify(psnCnfBase);
    return this.doGets(psnCnfBase);
  }

  /**
   * 获取数据
   * 
   * @param psnCnfBase
   * @return @
   */
  abstract PsnCnfBase doGet(PsnCnfBase psnCnfBase);

  /**
   * 执行保存：新增或修改
   * 
   * @param psnCnfBase @
   */
  abstract void doSave(PsnCnfBase psnCnfBase);

  /**
   * 执行删除
   * 
   * @param psnCnfBase @
   */
  abstract void doDel(PsnCnfBase psnCnfBase);

  /**
   * 必须执行校验的内容
   * 
   * @param psnCnfBase @
   */
  abstract void doVerify(PsnCnfBase psnCnfBase);

  /**
   * 获取多条数据.
   * 
   * @param psnCnfBase
   * @return @
   */
  List<?> doGets(PsnCnfBase psnCnfBase) {
    List<PsnCnfBase> slist = new ArrayList<PsnCnfBase>();
    PsnCnfBase get = this.get(psnCnfBase);
    if (get != null) {
      slist.add(get);
    }
    return slist;
  }
}
