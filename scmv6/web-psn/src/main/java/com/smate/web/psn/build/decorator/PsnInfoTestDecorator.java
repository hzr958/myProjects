package com.smate.web.psn.build.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.web.psn.exception.PsnBuildException;
import com.smate.web.psn.model.psninfo.PsnInfo;

/***
 * 人员信息构建（测试用）
 * 
 * @author zk
 *
 */
public class PsnInfoTestDecorator extends PsnInfoAbstractDecorator {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PsnInfoComponentDecorator psnInfoDecorator;

  public PsnInfoTestDecorator(PsnInfoComponentDecorator psnInfoDecorator) {
    super();
    this.psnInfoDecorator = psnInfoDecorator;
  }

  @Override
  public PsnInfo fillPsnInfo() throws PsnBuildException {
    PsnInfo psnInfo = psnInfoDecorator.fillPsnInfo();
    // TODO 填充信息
    return psnInfo;
  }

}
