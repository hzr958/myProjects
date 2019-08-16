package com.smate.web.prj.build.decorator;

import com.smate.web.prj.exception.BuildDecoratorException;
import com.smate.web.prj.model.common.PrjInfo;

/**
 * 项目信息构建修饰接口
 * 
 * @author zk
 * @since 6.0.1
 */
public interface PrjInfoComponentDecorator {

  /**
   * 数据处理方法 prjInfo对象 的传递
   */
  PrjInfo fillPrjInfo() throws BuildDecoratorException;
}
