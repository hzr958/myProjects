package com.smate.web.psn.build.decorator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.model.psninfo.PsnInfo;

/**
 * 人员信息构建 基础信息装饰者(入口)
 * 
 * @author zk
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class PsnInfoBaseDecorator implements PsnInfoComponentDecorator {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PsnInfo psnInfo;

  public PsnInfoBaseDecorator(PsnInfo psnInfo) {
    super();
    this.psnInfo = psnInfo;
  }

  /**
   * 数据处理方法 pubInfo对象 的传递
   */
  @Override
  public PsnInfo fillPsnInfo() throws PsnBuildException {
    Person psn = psnInfo.getPerson();
    if (psn != null) {
      psnInfo.setAvatarUrl(psn.getAvatars());
      String eName = StringUtils.isNotBlank(psn.getFirstName()) && StringUtils.isNotBlank(psn.getLastName())
          ? psn.getFirstName() + " " + psn.getLastName()
          : "";
      String showName = StringUtils.isNotBlank(psn.getName()) ? psn.getName() : eName;
      psnInfo.setName(showName);
      psnInfo.setPsnId(psn.getPersonId());
      psnInfo.setDes3PsnId(ServiceUtil.encodeToDes3(psn.getPersonId().toString()));
      psnInfo.setInsInfo(psn.getInsInfo());
      psnInfo.setDes3PsnId(psn.getPersonDes3Id());
    }
    return psnInfo;
  }
}
