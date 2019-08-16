package com.smate.web.psn.build.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 人员统计数据信息封装
 * 
 * @author zk
 *
 */
public class PsnInfoStatisticsDecorator implements PsnInfoComponentDecorator {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PsnInfo psnInfo;

  public PsnInfoStatisticsDecorator(PsnInfo psnInfo) {
    super();
    this.psnInfo = psnInfo;
  }

  @Override
  public PsnInfo fillPsnInfo() throws PsnBuildException {
    PsnStatistics psnStatis = psnInfo.getPsnStatistics();
    if (psnStatis != null) {
      psnInfo.setPubSum(psnStatis.getPubSum() == null ? 0 : psnStatis.getPubSum());
      psnInfo.setHindex(psnStatis.getHindex() == null ? 0 : psnStatis.getHindex());
      psnInfo.setCitedSum(psnStatis.getCitedSum() == null ? 0 : psnStatis.getCitedSum());
      psnInfo.setPrjSum(psnStatis.getPrjSum() == null ? 0 : psnStatis.getPrjSum());
    }
    return psnInfo;
  }

}
