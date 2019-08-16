package com.smate.web.psn.build.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 人员信息构造工厂
 * 
 * @author zk
 *
 */
@Service("psnInfoBuildFactory")
public class PsnInfoBuildFactory extends PsnInfoBuildFactoryAbstract {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 工厂入口
   * 
   * @param buildType
   * @param pubInfo zk
   */
  public void buildPsnInfo(Integer buildType, PsnInfo psnInfo) throws PsnBuildException {

    switch (buildType) {
      case 1:
        this.buildPsnForMobile(psnInfo);
        break;
      case 2:
        this.buildPsnForSearch(psnInfo);
        break;
      case 3:
        this.buildPsnForgroupFriend(psnInfo);
        break;
      default:
        logger.error("没有对应的人员构建类型 buildType＝" + buildType);
        throw new PsnBuildException("没有对应的人员构建类型 buildType＝" + buildType);
    }
  }

}
