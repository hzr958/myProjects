package com.smate.web.dyn.action.dynamic;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.DynamicInfluenceForm;
import com.smate.web.dyn.service.dynamic.DynamicInfluenceService;
import com.smate.web.dyn.service.msg.ShowMsgService;

/**
 * 动态影响力优化用户指引 Action
 * 
 * @author YHX
 *
 */
public class DynamicInfluenceAction extends ActionSupport implements ModelDriven<DynamicInfluenceForm>, Preparable {

  private static final long serialVersionUID = -9090557623490845340L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private DynamicInfluenceForm form;

  @Resource
  private ShowMsgService showMsgService;
  @Resource
  private DynamicInfluenceService dynamicInfluenceService;

  @Action("/dynweb/dynamic/ajaxdataconfirmpaper")
  public void getConfirmPaper() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      // 认领论文
      dynamicInfluenceService.dynConfirmPaper(form);
      String psnUrl = dynamicInfluenceService.dynSharePsnUrl(form.getPsnId());
      result.put("confirmList", form.getConfirmList());
      result.put("psnUrl", psnUrl);
      result.put("des3PsnId", Des3Utils.encodeToDes3(form.getPsnId() + ""));
      result.put("status", "success");
    } catch (Exception e) {
      result.put("status", "error");
      result.put("confirmList", null);
      result.put("psnUrl", null);
      result.put("des3PsnId", Des3Utils.encodeToDes3(form.getPsnId() + ""));
      logger.error("获取影响力用户指引页面 认领论文数据 出错,psnId=" + form.getPsnId(), e);
    }
    Struts2Utils.renderJsonNoNull(result, "encoding:utf-8");
  }

  @Action("/dynweb/dynamic/ajaxdatauploadpaper")
  public void getUploadPaper() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      // 上传全文
      dynamicInfluenceService.dynUploadPaper(form);
      result.put("uploadList", form.getUploadList());
      result.put("status", "success");
    } catch (Exception e) {
      result.put("status", "error");
      result.put("uploadList", null);
      logger.error("获取影响力用户指引页面 上传全文数据 出错,psnId=" + form.getPsnId(), e);
    }
    Struts2Utils.renderJsonNoNull(result, "encoding:utf-8");
  }

  @Action("/dynweb/dynamic/ajaxjudgeImpact")
  public void judgeImpact() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      boolean flag = dynamicInfluenceService.judgeImpact(form.getPsnId());
      result.put("flag", flag);
      result.put("status", "success");
    } catch (Exception e) {
      result.put("status", "error");
      result.put("flag", true);
      logger.error("判断是否弹出影响力用户指引页面  出错,psnId=" + form.getPsnId(), e);
    }
    Struts2Utils.renderJsonNoNull(result, "encoding:utf-8");
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new DynamicInfluenceForm();
    }
    form.setPsnId(SecurityUtils.getCurrentUserId());
  }

  @Override
  public DynamicInfluenceForm getModel() {
    return form;
  }

}
