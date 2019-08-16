package com.smate.web.psn.build.decorator;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 人员首要单位信息封装
 * 
 * @author zk
 *
 */
public class PsnInfoPrimaryInsDecorator implements PsnInfoComponentDecorator {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PsnInfo psnInfo;

  public PsnInfoPrimaryInsDecorator(PsnInfo psnInfo) {
    this.psnInfo = psnInfo;
  }

  @Override
  public PsnInfo fillPsnInfo() throws PsnBuildException {
    Map<String, String> primaryMap = psnInfo.getPrimaryIns();
    if (primaryMap != null && !primaryMap.isEmpty()) {
      StringBuffer sb = new StringBuffer();
      if (StringUtils.isNotBlank(primaryMap.get("INSNAME"))) {
        sb.append(primaryMap.get("INSNAME"));
      }
      if (StringUtils.isNotBlank(primaryMap.get("INSDPT"))) {
        if (sb.length() != 0) {
          sb.append(", ");
        }
        sb.append(primaryMap.get("INSDPT"));
      }
      if (StringUtils.isNotBlank(primaryMap.get("INSPOS"))) {
        if (sb.length() != 0) {
          sb.append(", ");
        }
        sb.append(primaryMap.get("INSPOS"));
      }
      psnInfo.setInsInfo(sb.toString());
    }
    return psnInfo;
  }

}
