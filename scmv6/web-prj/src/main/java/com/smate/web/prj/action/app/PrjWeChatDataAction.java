package com.smate.web.prj.action.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.prj.form.ProjectQueryForm;
import com.smate.web.prj.form.wechat.PrjWeChatForm;
import com.smate.web.prj.service.project.SnsProjectQueryService;
import com.smate.web.prj.service.wechat.PrjWeChatQueryService;

/*
 * @author zkj 项目信息获取
 */
@RestController
public class PrjWeChatDataAction extends WechatBaseAction implements ModelDriven<PrjWeChatForm>, Preparable {
  private static final long serialVersionUID = -6688611413646534069L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PrjWeChatForm form;
  @Autowired
  private PrjWeChatQueryService prjWeChatQueryService;
  @Autowired
  private SnsProjectQueryService snsProjectQueryService;
  private String forwardUrl;
  private String referer;


  // 项目列表
  @Action("/prjdata/wechat/findprjs")
  public String queryPrj() {
    Map<String, Object> data = new HashMap<String, Object>();
    try {
      Long currentPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()));
      if (StringUtils.isNotBlank(form.getSearchDes3PsnId())) {
        form.setPsnId(currentPsnId);
        snsProjectQueryService.queryPrjList(form);
      }
    } catch (Exception e) {
      logger.error("获取项目列表出错", e);
    }
    data.put("status", "success");
    data.put("resultList", form.getPage().getResult());
    data.put("totalCount", form.getPage().getTotalCount());
    Struts2Utils.renderJson(data, "encoding:utf-8");
    return null;
  }

  @Action("/prjdata/wechat/prjcondition")
  public String prjCondition() {
    Map<String, Object> data = new HashMap<String, Object>();
    try {
      ProjectQueryForm formQuery = new ProjectQueryForm();
      if (form.getSearchDes3PsnId() != null) {
        if (!form.getSearchPsnId().equals(form.getPsnId())) {
          formQuery.setOthersSee(true);
        }
        formQuery.setSearchPsnId(form.getSearchPsnId());
        snsProjectQueryService.queryAgencyName(formQuery);
      }
      data.put("status", "success");
      data.put("resultList", formQuery.getAgencyNameList());
    } catch (Exception e) {
      logger.error("移动端获取项目查询条件出错 psnId={},searchPsnId={}", form.getPsnId(), form.getSearchPsnId(), e);
    }
    Struts2Utils.renderJson(data, "encoding:utf-8");

    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PrjWeChatForm();
    }

  }

  public PrjWeChatForm getForm() {
    return form;
  }

  public void setForm(PrjWeChatForm form) {
    this.form = form;
  }

  @Override
  public PrjWeChatForm getModel() {
    return form;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public String getReferer() {
    return referer;
  }

  public void setReferer(String referer) {
    this.referer = referer;
  }

}
