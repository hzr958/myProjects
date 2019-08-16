package com.smate.web.prj.action.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.prj.form.ProjectDetailsForm;
import com.smate.web.prj.form.ProjectOptForm;
import com.smate.web.prj.form.wechat.PrjWeChatForm;
import com.smate.web.prj.model.common.PrjInfo;
import com.smate.web.prj.service.project.SnsPrjOptService;
import com.smate.web.prj.service.project.SnsProjectDetailsService;
import com.smate.web.prj.service.project.SnsProjectQueryService;
import com.smate.web.prj.service.wechat.PrjWeChatQueryService;

/**
 * 
 * @author LJ
 *
 *         2017年7月19日
 */
@Results({@Result(name = "app_prjxml", location = "/WEB-INF/jsp/prj/app/app_prjxml.jsp"),
    @Result(name = "prjNotExit", location = "/WEB-INF/jsp/prj/app/prjNotExit.jsp"),
    @Result(name = "prjNoPrivacy", location = "/WEB-INF/jsp/prj/app/prjNoPrivacy.jsp")})
public class APPProjectAction extends ActionSupport implements ModelDriven<PrjWeChatForm>, Preparable {

  private static final long serialVersionUID = 5293305269582789666L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PrjWeChatForm form;
  @Autowired
  private PrjWeChatQueryService prjWeChatQueryService;
  @Autowired
  private SnsProjectDetailsService snsProjectDetailsService;
  @Autowired
  private SnsPrjOptService snsPrjOptService;
  private int total = 0;
  private String status = IOSHttpStatus.OK;// 默认状态
  private String msg = "操作成功";
  @Autowired
  private SnsProjectQueryService snsProjectQueryService;

  /**
   * 获取成果统计数
   * 
   * @return
   */
  @Action("/app/prjweb/project/statistics")
  public void getpubstatics() {
    form.setResultMap(new HashMap<String, Object>());
    status = IOSHttpStatus.OK;// 默认状态
    msg = "操作成功";
    if (StringUtils.isBlank(form.getDes3PrjId())) {
      status = IOSHttpStatus.PARAM_ERROR;// 默认状态
      msg = "缺失des3PrjId";
    }
    try {
      if (IOSHttpStatus.OK.equals(status)) {
        form.setPrjId(Long.parseLong(Des3Utils.decodeFromDes3(form.getDes3PrjId())));
        prjWeChatQueryService.appHandlePrjStatistics(form);
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      msg = "服务器出错";
      logger.error("app获取个人成果统计数出错,pubId=" + form.getDes3PrjId(), e);
    }
    AppActionUtils.doResult(form.getResultMap(), total, status, msg);
  }

  /**
   * 评论列表（站内站外均要使用）
   */
  @Action("/app/prjweb/project/ajaxcommentshow")
  public void prjCommentShow() {
    ProjectDetailsForm f = new ProjectDetailsForm();
    f.setPage(form.getPage());
    status = IOSHttpStatus.OK;// 默认状态
    msg = "操作成功";
    if (StringUtils.isBlank(form.getDes3PrjId())) {
      msg = "项目id缺失";
      status = IOSHttpStatus.PARAM_ERROR;
    }
    try {
      if (IOSHttpStatus.OK.equals(status)) {
        f.setPrjId(Long.valueOf(Des3Utils.decodeFromDes3(form.getDes3PrjId())));
        snsProjectDetailsService.showPrjComment(f);
      }
    } catch (Exception e) {
      msg = "服务器出错";
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("显示项目评论页面出错，prjId=", f.getPrjId(), e);
    }
    Long count = f.getPage().getTotalCount();
    AppActionUtils.doResult(f.getCommentList(), count == null ? 0 : count.intValue(), status, msg);
  }

  /**
   * 项目赞
   */
  @Action("/app/prjweb/project/ajaxprjaddaward")
  public void ajaxAddAward() {
    ProjectOptForm f = new ProjectOptForm();
    status = IOSHttpStatus.OK;// 默认状态
    msg = "操作成功";
    if (StringUtils.isBlank(form.getDes3PrjId())) {
      msg = "项目id缺失";
      status = IOSHttpStatus.PARAM_ERROR;
    }
    String awardPsnContent = "";
    Map<Object, Object> map = new HashMap<Object, Object>();
    try {
      if (IOSHttpStatus.OK.equals(status)) {
        f.setId(Long.valueOf(Des3Utils.decodeFromDes3(form.getDes3PrjId())));
        f.setResNode(1);
        f.setResType(4);
        awardPsnContent = snsPrjOptService.prjAddAward(f);
        if (StringUtils.isNotBlank(awardPsnContent)) {
          map = JacksonUtils.json2Map(awardPsnContent);
        }
      }
    } catch (Exception e) {
      msg = "服务器出错";
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("添加赞出错，prjId=", f.getPrjId(), e);
    }
    AppActionUtils.doResult(map, total, status, msg);
  }

  /**
   * 项目取消赞
   */
  @Action("/app/prjweb/project/ajaxprjcancelaward")
  public void ajaxCancelAward() {
    ProjectOptForm f = new ProjectOptForm();
    status = IOSHttpStatus.OK;// 默认状态
    msg = "操作成功";
    if (StringUtils.isBlank(form.getDes3PrjId())) {
      msg = "项目id缺失";
      status = IOSHttpStatus.PARAM_ERROR;
    }
    String awardPsnContent = "";
    Map<Object, Object> map = new HashMap<Object, Object>();
    try {
      if (IOSHttpStatus.OK.equals(status)) {
        f.setId(Long.valueOf(Des3Utils.decodeFromDes3(form.getDes3PrjId())));
        f.setResNode(1);
        f.setResType(4);
        awardPsnContent = snsPrjOptService.prjCancelAward(f);
        if (StringUtils.isNotBlank(awardPsnContent)) {
          map = JacksonUtils.json2Map(awardPsnContent);
        }
      }
    } catch (Exception e) {
      msg = "服务器出错";
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("添加赞出错，prjId=", f.getPrjId(), e);
    }
    AppActionUtils.doResult(map, total, status, msg);
  }

  /**
   * 项目添加评论
   */
  @Action("/app/prjweb/project/ajaxaddcomment")
  public void prjAddComment() {
    ProjectDetailsForm f = new ProjectDetailsForm();
    String msg = "操作成功";
    status = IOSHttpStatus.OK;
    if (StringUtils.isBlank(form.getComment())) {
      msg = "评论内容不能为空";
      status = IOSHttpStatus.PARAM_ERROR;
    }
    if (StringUtils.isBlank(form.getDes3PrjId())) {
      msg = "项目id缺失";
      status = IOSHttpStatus.PARAM_ERROR;
    }
    try {
      if (IOSHttpStatus.OK.equals(status)) {
        f.setDes3PrjId(form.getDes3PrjId());
        f.setComment(form.getComment());
        f.setResultMap(new HashMap<String, Object>());
        snsProjectDetailsService.prjAddComment(f);
      }
    } catch (Exception e) {
      msg = "服务器出错";
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("app项目添加评论出错，des3PrjId=", f.getDes3PrjId(), e);
    }
    AppActionUtils.doResult(f.getResultMap(), total, status, msg);
  }

  // 项目列表
  @Action("/app/prjweb/findprjs")
  public String queryPrj() {

    List<PrjInfo> resultList = null;
    try {
      Long currentPsnId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {
        Long searchPsnId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId()));
        form.setSearchPsnId(searchPsnId);
      }
      form.setPsnId(currentPsnId);
      form.setCurrentPsnId(currentPsnId);
      form.getPage().setPageNo(NumberUtils.toInt(form.getNextId(), 1));
      snsProjectQueryService.queryPrjList(form);
      if (form.getPage().getResult() != null) {
        resultList = form.getPage().getResult();
        total = form.getPage().getTotalCount() == null ? 0 : form.getPage().getTotalCount().intValue();
      }
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("获取项目列表出错", e);
    }
    AppActionUtils.renderAPPReturnJson(resultList, total, status);
    return null;

  }

  // 项目详情
  @Action("/app/prjweb/findprjdetail")
  public String queryPrjXml() {
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
    if (checkParam(form.getDes3PrjId())) {
      status = IOSHttpStatus.BAD_REQUEST;
      AppActionUtils.renderAPPReturnJson("bad request", total, status);
      return null;
    }
    try {
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {// 获取他人的
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      }
      prjWeChatQueryService.queryPrjXml(form);
      if ("notExists".equals(form.getResultMap().get("result"))) {
        return "prjNotExit";
      }
      if ("noPrivacy".equals(form.getResultMap().get("result"))) {
        return "prjNoPrivacy";
      }
    } catch (Exception e) {
      logger.error("获取项目xml出错,prjid=" + form.getDes3PrjId(), e);
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      AppActionUtils.renderAPPReturnJson("error", total, status);
      return null;
    }
    return "app_prjxml";

  }

  // 项目详情 返回json数据

  @Action("/app/prjweb/query/detail")

  public void queryPrjDetail() {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    Map<String, String> map = new HashMap<String, String>();
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
    try {
      if (checkParam(form.getDes3PrjId())) {
        status = IOSHttpStatus.BAD_REQUEST;
        AppActionUtils.renderAPPReturnJson("bad request", total, status);
        return;
      }
      prjWeChatQueryService.queryPrjXml(form);
      if ("notExists".equals(form.getResultMap().get("result"))) {// 不存在的情况
        resultMap.put("status", "not exists");
      } else if ("noPrivacy".equals(form.getResultMap().get("result"))) {// 没权限的情况
        resultMap.put("status", "no permission");
      } else if (form.getResultMap() == null) {
        resultMap.put("status", "hasDeleted");
      } else {
        resultMap.put("status", "success");

        resultMap.put("result", form.getPrjList().get(0));
      }

    } catch (Exception e) {
      logger.error("获取项目xml出错,prjid=" + form.getDes3PrjId(), e);
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      AppActionUtils.renderAPPReturnJson("error", total, status);
      return;
    }
    Struts2Utils.renderJsonNoNull(resultMap, "encoding:utf-8");
  }


  /**
   * 获取与群组关联的项目信息
   */
  @Action("/prjdata/relationgrp/info")
  public void reqGrpRelationPrjInfo() {
    Map<String, Object> result = new HashMap<String, Object>();
    String status = "error";
    try {
      if (StringUtils.isNotBlank(form.getDes3GrpId())) {
        Long grpId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3GrpId()), 0L);
        if (NumberUtils.isNotNullOrZero(grpId)) {
          PrjInfo prjInfo = snsProjectDetailsService.findGrpRelationPrjInfo(grpId);
          result.put("prjInfo", NumberUtils.isNullOrZero(prjInfo.getPrjId()) ? null : prjInfo);
          status = "success";
        }
      }
    } catch (Exception e) {
      logger.error("获取群组关联的项目信息异常， grpId={}, prjId={}, psnId={}", form.getDes3GrpId(), form.getPrjId(), form.getPsnId(),
          e);
    }
    result.put("status", status);
    Struts2Utils.renderJsonNoNull(result, "encoding:utf-8");
  }

  /**
   * 参数校验
   * 
   * @return
   */
  public boolean checkParam(String des3PrjId) {
    if (StringUtils.isEmpty(des3PrjId)) {
      return true;
    }
    return false;
  }

  public PrjWeChatForm getForm() {
    return form;
  }

  public void setForm(PrjWeChatForm form) {
    this.form = form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PrjWeChatForm();
    }
    if (form.getPage() == null) {
      form.setPage(new Page<PrjInfo>());
    }

  }

  @Override
  public PrjWeChatForm getModel() {
    return form;
  }

}
