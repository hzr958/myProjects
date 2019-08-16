package com.smate.web.prj.build.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.web.prj.exception.BuildDecoratorException;
import com.smate.web.prj.model.common.PrjInfo;


/**
 * 项目构建 项目 分享次数统计
 * 
 * @author ajb
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class PrjInfoWrapShareCountDecorator extends PrjInfoAbstractDecorator {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PrjInfoComponentDecorator prjInfoComponentDecorator;


  public PrjInfoWrapShareCountDecorator(PrjInfoComponentDecorator prjInfoComponentDecorator) {
    super();
    this.prjInfoComponentDecorator = prjInfoComponentDecorator;
  }


  /**
   * 数据处理方法 pubInfo对象 的传递
   */
  @Override
  public PrjInfo fillPrjInfo() throws BuildDecoratorException {
    PrjInfo prjInfo = prjInfoComponentDecorator.fillPrjInfo();

    ProjectStatistics projectStatistics = prjInfo.getProjectStatistics();
    if (projectStatistics != null) {
      prjInfo.setShareCount(projectStatistics.getShareCount() == null ? 0 : projectStatistics.getShareCount());
    } else {
      prjInfo.setShareCount(0);
    }
    return prjInfo;
  }



}
