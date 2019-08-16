package com.smate.web.prj.build.factor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.smate.web.prj.build.decorator.PrjInfoBaseDecorator;
import com.smate.web.prj.build.decorator.PrjInfoComponentDecorator;
import com.smate.web.prj.build.decorator.PrjInfoWrapAwardCountDecorator;
import com.smate.web.prj.build.decorator.PrjInfoWrapCommnetCountDecorator;
import com.smate.web.prj.build.decorator.PrjInfoWrapShareCountDecorator;
import com.smate.web.prj.exception.BuildDecoratorException;
import com.smate.web.prj.exception.BuildFactoryNotExistsTypeException;
import com.smate.web.prj.exception.PrjInfoBuildFactoryException;
import com.smate.web.prj.model.common.PrjInfo;

/**
 * 项目信息构建工厂
 * 
 * @author zk
 *
 */
@Service("prjInfoBuildFactory")
public class PrjInfoBuildFactory implements PrjInfoBuildFactorAware {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void buildPubInfo(Integer buildType, PrjInfo prjInfo) throws PrjInfoBuildFactoryException {
    switch (buildType) {
      case 2:
        this.buildPrjForWeChat(prjInfo);
        break;
      default:
        logger.error("没有对应的项目构建类型 buildType＝" + buildType);
        throw new BuildFactoryNotExistsTypeException("没有对应的项目构建类型 buildType＝" + buildType);
    }
  }

  /**
   * 构建基础信息--wechat
   * 
   * @param prjInfo
   * @throws BuildDecoratorException
   */
  private void buildPrjForWeChat(PrjInfo prjInfo) throws BuildDecoratorException {
    PrjInfoComponentDecorator prjInfoCom = new PrjInfoBaseDecorator(prjInfo);
    PrjInfoComponentDecorator prjInfoAward = new PrjInfoWrapAwardCountDecorator(prjInfoCom);
    PrjInfoComponentDecorator prjInfoShare = new PrjInfoWrapShareCountDecorator(prjInfoAward);
    PrjInfoComponentDecorator prjInfoCommnet = new PrjInfoWrapCommnetCountDecorator(prjInfoShare);
    prjInfo = prjInfoCommnet.fillPrjInfo();
  }

}
