package com.smate.web.prj.build.factor;

import com.smate.web.prj.exception.PrjInfoBuildFactoryException;
import com.smate.web.prj.model.common.PrjInfo;


/**
 * 项目构建工厂
 * 
 * @author zk
 *
 */
public interface PrjInfoBuildFactorAware {

  /**
   * 工厂入口
   * 
   * @param buildType
   * @param pubInfo
   */
  public void buildPubInfo(Integer buildType, PrjInfo prjInfo) throws PrjInfoBuildFactoryException;
}
