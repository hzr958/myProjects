package com.smate.web.psn.build.decorator;

import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 人员信息构建装饰接口
 * 
 * @author zk
 *
 */
public interface PsnInfoComponentDecorator {

  /**
   * 数据处理方法 psnInfo对象 的传递
   */
  PsnInfo fillPsnInfo() throws PsnBuildException;
}
