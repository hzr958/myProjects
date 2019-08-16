package com.smate.web.psn.build.factory;

import com.smate.web.psn.build.decorator.PsnInfoBaseDecorator;
import com.smate.web.psn.build.decorator.PsnInfoComponentDecorator;
import com.smate.web.psn.build.decorator.PsnInfoPrimaryInsDecorator;
import com.smate.web.psn.build.decorator.PsnInfoStatisticsDecorator;
import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 人员信息构造工厂抽象类，
 * 
 * @author zk
 *
 */
public abstract class PsnInfoBuildFactoryAbstract {

  /**
   * 构建人员信息入口
   * 
   * @param buildType
   * @param psnInfo
   * @throws PsnException
   */
  public abstract void buildPsnInfo(Integer buildType, PsnInfo psnInfo) throws PsnBuildException;

  /**
   * 构造人员信息用于移动端
   * 
   * @param psnInfo
   */
  protected void buildPsnForMobile(PsnInfo psnInfo) throws PsnBuildException {
    // 填充人员基本信息（person）
    PsnInfoComponentDecorator psnInfoBase = new PsnInfoBaseDecorator(psnInfo);
    // TODO 装饰人员信息
    psnInfo = psnInfoBase.fillPsnInfo();
    // 封装统计数
    PsnInfoStatisticsDecorator psnInfoStatis = new PsnInfoStatisticsDecorator(psnInfo);
    psnInfo = psnInfoStatis.fillPsnInfo();
    // PsnInfoComponentDecorator psnInfoTest = new
    // PsnInfoTestDecorator(psnInfoBase);
    // psnInfo = psnInfoTest.fillPsnInfo();
  }

  /**
   * 构造人员信息用于检索
   * 
   * @param psnInfo
   * @throws PsnBuildException
   */
  protected void buildPsnForSearch(PsnInfo psnInfo) throws PsnBuildException {
    // 填充人员基本信息（person）
    PsnInfoComponentDecorator psnInfoBase = new PsnInfoBaseDecorator(psnInfo);
    // 封装统计数
    PsnInfoStatisticsDecorator psnInfoStatis = new PsnInfoStatisticsDecorator(psnInfoBase.fillPsnInfo());
    // 封装单位信息
    PsnInfoPrimaryInsDecorator psnInfoPrimaryIns = new PsnInfoPrimaryInsDecorator(psnInfoStatis.fillPsnInfo());
    psnInfo = psnInfoPrimaryIns.fillPsnInfo();
  }

  /**
   * 构造人员信息用于群组人员好友
   * 
   * @param psnInfo
   */
  protected void buildPsnForgroupFriend(PsnInfo psnInfo) throws PsnBuildException {
    // 填充人员基本信息（person）
    PsnInfoComponentDecorator psnInfoBase = new PsnInfoBaseDecorator(psnInfo);
    // 装饰人员信息
    psnInfo = psnInfoBase.fillPsnInfo();
  }
}
