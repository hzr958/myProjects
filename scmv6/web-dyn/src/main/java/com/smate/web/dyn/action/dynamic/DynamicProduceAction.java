package com.smate.web.dyn.action.dynamic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.utils.wechat.WechatBaseAction;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.service.dynamic.DynamicMsgService;
import com.smate.web.dyn.service.dynamic.DynamicRealtimeService;
import com.smate.web.dyn.service.dynamic.DynamicShareService;
import com.smate.web.dyn.service.psn.InsInfoService;
import com.smate.web.dyn.service.psn.PersonQueryservice;
import com.smate.web.dyn.service.share.ShareStatisticsService;

/**
 * 动态生成Action
 * 
 * @author zk
 *
 */
@Results({@Result(name = "dyn_publish", location = "/WEB-INF/jsp/dyn/publish/dyn_publish.jsp"),
    @Result(name = "customForward", location = "{forwardUrl}"),
    @Result(name = "dyn_main_share", location = "/WEB-INF/jsp/dyn/show/dyn_main_share.jsp")})
public class DynamicProduceAction extends WechatBaseAction implements Preparable, ModelDriven<DynamicForm> {

  private static final long serialVersionUID = -4677155542913316973L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private DynamicForm form;
  @Autowired
  private DynamicRealtimeService dynamicRealtimeService;
  @Autowired
  private PersonQueryservice personQueryservice;
  @Autowired
  private InsInfoService insInfoService;
  @Autowired
  private DynamicShareService dynamicShareService;
  @Autowired
  private ShareStatisticsService shareStatisticsService;
  @Autowired
  private DynamicMsgService dynamicMsgService;

  /**
   * 动态发布
   * 
   * @return
   */
  @Action("/dynweb/dynamic/publish")
  public String dynPublish() {
    try {
      if (Struts2Utils.getSession().getAttribute("wxOpenId") != null) {
        this.setWxOpenId(Struts2Utils.getSession().getAttribute("wxOpenId").toString());
        // jsapi签名只要与浏览器地址内容一致即可
        this.handleWxJsApiTicket(this.getDomain() + "/dynweb/dynamic/publish");
      }
      Person person = personQueryservice.findPersonInsAndPos(SecurityUtils.getCurrentUserId());
      if (person != null) {
        form.setPsnId(person.getPersonId());
        Map<String, String> insInfoMap = insInfoService.findPsnInsInfo(person.getPersonId());
        if (insInfoMap != null) {
          form.setPsnInsAndPos(insInfoMap.get(InsInfoService.INS_INFO_ZH));
        }
        form.setDes3psnId(ServiceUtil.encodeToDes3(person.getPersonId().toString()));
        form.setPsnAvatars(person.getAvatars());
        form.setPsnName(personQueryservice.getPsnName(person, "zh_CN"));
      }
    } catch (Exception e) {
      logger.error("获取动态发布页面出错,psnId" + SecurityUtils.getCurrentUserId(), e);
    }
    return "dyn_publish";
  }

  /**
   * 实时动态入口
   */
  @Action("/dynweb/dynamic/ajaxrealtime")
  public void dynamicRealtime() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long resId = form.getResId();
    Integer resType = form.getResType();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (form.getPsnId() == 0 && !"".equals(form.getDes3psnId())) {
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3psnId())));
      }
      dynamicRealtimeService.dynamicRealtime(form);
      // 纯资源动态要更新资源分享统计数，有些会单独另发启url去更新统计数
      if ("B2TEMP".equals(form.getDynType()) && 3 == form.getOperatorType()) {
        dynamicShareService.shareDynamic(form);
      }
      // A、B1动态要更新动态分享统计数（-1目前是什么逻辑不清楚了）
      if (-1 == form.getOperatorType() || ("B1TEMP".equals(form.getDynType()) && 3 == form.getOperatorType())) {// 分享更新统计表（有分享内容是-1）
        // shareStatisticsService.addBatchShareRecord(form.getPsnId(),
        // resType, resId);
        form.setPlatform("1");
        shareStatisticsService.addNewShareRecord(form);
      }
      map.put("result", "success");
    } catch (Exception e) {
      logger.error("生成动态数据出错,dynId=" + form.getDynId(), e);
      map.put("result", "fail");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

  /**
   * 更新基金分享至动态统计数
   */
  @Action("/dynweb/dynamic/fundsharenum")
  public void updateFundShareNum() {
    Map<String, String> result = new HashMap<String, String>();
    String des3ResId = form.getDes3ResId();
    if (StringUtils.isNotEmpty(des3ResId)) {
      try {
        Long fundId = com.smate.core.base.utils.number.NumberUtils.toLong(Des3Utils.decodeFromDes3(des3ResId));
        shareStatisticsService.updateFundShareNum(fundId);
        result.put("status", "success");
        result.put("msg", "update success");
      } catch (Exception e) {
        logger.error("更新基金分享统计数失败,des3ResId={}", des3ResId, e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 更新项目分享至动态统计数
   */
  @Action("/dynweb/dynamic/prjsharenum")
  public void updatePrjShareNum() {
    Map<String, String> result = new HashMap<String, String>();
    String des3ResId = form.getDes3ResId();
    if (StringUtils.isNotEmpty(des3ResId)) {
      try {
        Long prjId = com.smate.core.base.utils.number.NumberUtils.toLong(Des3Utils.decodeFromDes3(des3ResId));
        shareStatisticsService.updatePrjShareNum(prjId);
        result.put("status", "success");
        result.put("msg", "update success");
      } catch (Exception e) {
        logger.error("更新项目分享统计数失败,des3ResId={}", des3ResId, e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 更新个人库成果分享数
   */
  @Action("/dynweb/dynamic/snssharenum")
  public void updateSnsShareNum() {
    Map<String, String> result = new HashMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    String des3ResId = form.getDes3ResId();
    if (StringUtils.isNotEmpty(des3ResId) && com.smate.core.base.utils.number.NumberUtils.isNotNullOrZero(psnId)) {
      try {
        form.setPsnId(psnId);
        Long snsId = com.smate.core.base.utils.number.NumberUtils.toLong(Des3Utils.decodeFromDes3(des3ResId));
        shareStatisticsService.updateSnsShareNum(form, snsId);
        result.put("status", "success");
        result.put("msg", "update success");
      } catch (Exception e) {
        logger.error("更新项目分享统计数失败,des3ResId={}", des3ResId, e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 更新基准库成果分享数
   */
  @Action("/dynweb/dynamic/pdwhsharenum")
  public void updatePdwhShareNum() {
    Map<String, String> result = new HashMap<String, String>();
    Long psnId = SecurityUtils.getCurrentUserId();
    String des3ResId = form.getDes3ResId();
    if (StringUtils.isNotEmpty(des3ResId) && com.smate.core.base.utils.number.NumberUtils.isNotNullOrZero(psnId)) {
      try {
        form.setPsnId(psnId);
        Long pdwhId = com.smate.core.base.utils.number.NumberUtils.toLong(Des3Utils.decodeFromDes3(des3ResId));
        shareStatisticsService.updatePdwhShareNum(form, pdwhId);
        result.put("status", "success");
        result.put("msg", "update success");
      } catch (Exception e) {
        logger.error("更新项目分享统计数失败,des3ResId={}", des3ResId, e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 移动端动态分享数据跳转页
   * 
   * @return
   */
  @Action("/dyndata/dynamic/getshareinfo")
  public void getShareInfo() {
    Map<String, Object> result = new HashMap<String, Object>();
    Long dynId = form.getDynId();
    if (dynId != null && dynId > 0L) {
      try {
        DynamicMsg dynamicMsg = dynamicMsgService.getById(dynId);
        result.put("des3DynId", Des3Utils.encodeToDes3(String.valueOf(dynId)));
        if (Objects.nonNull(dynamicMsg)) {
          result.put("des3ResId", Des3Utils.encodeToDes3(String.valueOf(dynamicMsg.getResId())));
          result.put("dynType", dynamicMsg.getDynType());
          result.put("resType", dynamicMsg.getResType());
          result.put("resId", dynamicMsg.getResId());
        }
        result.put("status", "success");
        result.put("msg", "get data success");
      } catch (Exception e) {
        logger.error("获取动态相关信息出错,dynId={}", dynId, e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "dynId is null");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new DynamicForm();
    }
  }

  public DynamicForm getForm() {
    return form;
  }

  public void setForm(DynamicForm form) {
    this.form = form;
  }

  @Override
  public DynamicForm getModel() {
    return form;
  }

}
