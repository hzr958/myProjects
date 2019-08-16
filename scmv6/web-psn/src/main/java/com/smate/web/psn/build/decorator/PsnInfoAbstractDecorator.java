package com.smate.web.psn.build.decorator;

import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.model.psninfo.PsnInfo;


/**
 * 人员信息构建 信息装饰 抽象 类
 * 
 * @author zk
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public abstract class PsnInfoAbstractDecorator implements PsnInfoComponentDecorator {

  /**
   * 数据处理方法 pubInfo对象 的传递
   */
  @Override
  abstract public PsnInfo fillPsnInfo() throws PsnBuildException;

}
