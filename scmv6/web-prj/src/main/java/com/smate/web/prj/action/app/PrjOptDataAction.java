package com.smate.web.prj.action.app;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.prj.form.ProjectOptForm;
import com.smate.web.prj.service.project.SnsPrjOptService;

/**
 * 项目操作接口
 * 
 * @author wsn
 * @date May 17, 2019
 */
public class PrjOptDataAction extends ActionSupport implements ModelDriven<ProjectOptForm>, Preparable {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private ProjectOptForm form;
  @Autowired
  private SnsPrjOptService snsPrjOptService;


  /**
   * 对项目进行赞操作
   */
  @Action("/prjdata/award")
  public void ajaxAddAward() {
    String awardResult = "";
    try {
      Long prjId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3Id()), 0L);
      Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
      String optType = form.getOptType();
      if (NumberUtils.isNotNullOrZero(prjId) && StringUtils.isNotBlank(optType) && NumberUtils.isNotNullOrZero(psnId)) {
        form.setId(prjId);
        TheadLocalPsnId.setPsnId(psnId);
        if ("1".equals(optType.trim())) {
          // 赞
          awardResult = snsPrjOptService.prjAddAward(form);
        } else if ("0".equals(optType.trim())) {
          // 取消赞
          awardResult = snsPrjOptService.prjCancelAward(form);
        }
      }
    } catch (Exception e) {
      logger.error("对项目进行赞、取消赞操作异常，prjId={}, optType={}", form.getDes3Id(), form.getOptType(), e);
    }
    Struts2Utils.renderJson(awardResult, "encoding:utf-8");
  }

  @Override
  public void prepare() throws Exception {
    this.form = Optional.ofNullable(form).orElse(new ProjectOptForm());
  }

  @Override
  public ProjectOptForm getModel() {
    return form;
  }

}
