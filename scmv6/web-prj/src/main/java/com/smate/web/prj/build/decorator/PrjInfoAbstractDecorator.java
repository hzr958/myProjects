package com.smate.web.prj.build.decorator;

import com.smate.web.prj.exception.BuildDecoratorException;
import com.smate.web.prj.model.common.PrjInfo;

/**
 * 项目构建 信息装饰 抽象 类
 * 
 * @author zk
 * @since 6.0.1
 */
public abstract class PrjInfoAbstractDecorator implements PrjInfoComponentDecorator {

  @Override
  abstract public PrjInfo fillPrjInfo() throws BuildDecoratorException;

}
