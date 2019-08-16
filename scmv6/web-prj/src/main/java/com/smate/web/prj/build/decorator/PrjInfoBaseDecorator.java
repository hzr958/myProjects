package com.smate.web.prj.build.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.project.model.Project;
import com.smate.web.prj.exception.BuildDecoratorException;
import com.smate.web.prj.model.common.PrjInfo;

/**
 * 项目信息基础装饰
 * 
 * @author zk
 *
 */
public class PrjInfoBaseDecorator implements PrjInfoComponentDecorator {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PrjInfo prjInfo;

  public PrjInfoBaseDecorator() {}

  public PrjInfoBaseDecorator(PrjInfo prjInfo) {
    super();
    this.prjInfo = prjInfo;
  }

  @Override
  public PrjInfo fillPrjInfo() throws BuildDecoratorException {
    Project prj = prjInfo.getPrj();
    prjInfo.setPrjId(prj.getId());
    prjInfo.setAuthors(prj.getAuthorNames());
    if ("zh_CN".equals(LocaleContextHolder.getLocale().toString())) {
      prjInfo.setTitle(prj.getZhTitle() == null ? prj.getEnTitle() : prj.getZhTitle());
      prjInfo.setBriefDesc(prj.getBriefDesc() == null ? prj.getBriefDescEn() : prj.getBriefDesc());
    } else {
      prjInfo.setTitle(prj.getEnTitle() == null ? prj.getZhTitle() : prj.getEnTitle());
      prjInfo.setBriefDesc(prj.getBriefDescEn() == null ? prj.getBriefDesc() : prj.getBriefDescEn());
    }
    return prjInfo;
  }

}
