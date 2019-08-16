package com.smate.web.dyn.action.dynamic;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.service.dynamic.DynamicAwardService;
import com.smate.web.dyn.service.dynamic.DynamicReplyService;
import com.smate.web.dyn.service.dynamic.DynamicService;
import com.smate.web.dyn.service.statistics.DynStatisticsService;

public class APPDynamicOptAction extends ActionSupport implements ModelDriven<DynamicForm>, Preparable {
  private static final long serialVersionUID = 4934709889761616368L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private DynamicForm form;
  @Autowired
  private DynamicService dynamicService;
  @Autowired
  private DynamicAwardService dynamicAwardService;
  @Autowired
  private DynamicReplyService dynamicReplyService;
  @Autowired
  private DynStatisticsService dynStatisticsService;
  private String des3PsnId;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态

  /**
   * 删除动态
   * 
   * @return
   */
  @Action("/app/dynweb/ajaxdeletedyn")
  public String deleteDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    if (form.getDynId() != null && form.getDynId() > 0L) {
      try {
        dynamicService.deleteDyn(form);
        map.put("result", "success");
        status = IOSHttpStatus.OK;
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        map.put("result", "failed");
        logger.error("删除动态异常：psnId=" + form.getPsnId() + " dynId=" + form.getDynId() + e.toString());
      }
    } else {
      status = IOSHttpStatus.BAD_REQUEST;
      map.put("result", "error");
    }
    AppActionUtils.renderAPPReturnJson(map.get("result"), total, status);
    return null;
  }

  /**
   * 添加动态评论
   * 
   * @return
   */
  @Action("/app/dynweb/ajaxreplydyn")
  public String replyDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    if (form.getDynType() != null && StringUtils.isNotBlank(form.getReplyContent())) {
      try {
        form.setOperatorType(1);// 评论操作默认值为1
        form.setNextDynType(getNextDynType(form.getDynType()));
        dynamicReplyService.replyDyn(form);
        map.put("dynReplayInfo", form.getDynReplayInfoList().get(0));
        map.put("result", "success");
        status = IOSHttpStatus.OK;
      } catch (Exception e) {
        status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
        map.put("result", "error");
        logger.error("评论动态出错", e);
      }
    } else {
      map.put("result", "error");
      status = IOSHttpStatus.BAD_REQUEST;
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);

    return null;
  }

  /**
   * 获取下一个模板类型
   * 
   * @param dynType
   * @return
   */
  public String getNextDynType(String dynType) {
    String nextDynType = null;
    switch (dynType) {
      case "ATEMP":
        nextDynType = "B1TEMP";
        break;
      case "B1TEMP":
        nextDynType = "B1TEMP";
        break;
      case "B2TEMP":
        nextDynType = "B2TEMP";
        break;
      case "B3TEMP":
        nextDynType = "B2TEMP";
        break;
      default:
        break;
    }
    return nextDynType;

  }

  public String getDes3PsnId() {
    return des3PsnId;
  }

  public void setDes3PsnId(String des3PsnId) {
    this.des3PsnId = des3PsnId;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new DynamicForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public DynamicForm getModel() {
    return form;
  }
}
