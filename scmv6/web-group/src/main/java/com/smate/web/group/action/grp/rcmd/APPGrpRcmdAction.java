package com.smate.web.group.action.grp.rcmd;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.consts.model.ConstKeyDisc;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.group.action.grp.form.GrpBaseForm;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.action.grp.form.GrpShowInfo;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.rcmd.GrpRcmdService;

/**
 * 
 * @author LJ
 *
 *         2017年6月28日
 */
public class APPGrpRcmdAction extends ActionSupport implements ModelDriven<GrpBaseForm>, Preparable {
  private static final long serialVersionUID = 3727606095733078813L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 
   */
  private GrpBaseForm form;

  @Autowired
  private GrpRcmdService grpRcmdService;
  @Autowired
  private GrpBaseService grpBaseService;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态

  /**
   * 自动填充学科关键词
   * 
   * @return
   */
  @Action("/app/groupweb/mygrp/ajaxautoconstkeydiscscodeid")
  public void ajaxAutoConstKeyDiscsCodeId() {
    List<ConstKeyDisc> list = null;
    try {
      if (checkParam(form)) {
        GrpMainForm f = initData(form);
        list = grpBaseService.getConstKeyDiscs(f);
        if (list == null || list.size() == 0) {
          form.setAppStatus(IOSHttpStatus.NOT_MODIFIED);
          form.setMsg("没有查询到任何记录");
        }
      }
    } catch (Exception e) {
      form.setAppStatus(IOSHttpStatus.INTERNAL_SERVER_ERROR);
      logger.error("app自动填充学科关键词出错，SearchKey=" + form.getSearchKey(), e);
    }
    AppActionUtils.doResult(list, form.getAppTotal(), form.getAppStatus(), form.getMsg());
  }

  private boolean checkParam(GrpBaseForm form) {
    if (StringUtils.isNotBlank(form.getSearchKey())) {
      return true;
    } else {
      form.setAppStatus(IOSHttpStatus.BAD_REQUEST);
      form.setMsg("searchKey为空");
      return false;
    }
  }

  private GrpMainForm initData(GrpBaseForm form) {
    GrpMainForm f = new GrpMainForm();
    f.setSearchKey(form.getSearchKey());
    f.setKeywordsSize(form.getKeywordsSize());
    form.setAppStatus(IOSHttpStatus.OK);
    return f;
  }

  /**
   * 群组推荐 列表
   * 
   * @return
   */
  @Action("/app/groupweb/rcmdgrp/ajaxrcmdgrplist")
  public String ajaxGetRcmdGrpList() {
    try {
      if (StringUtils.isNotBlank(form.getDes3PsnId())) {// 获取他人的
        form.setPsnId(NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3PsnId())));
      }
      grpRcmdService.getRcmdGrpList(form);
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("群组推荐列表 ，操作 异常：psnId=" + form.getPsnId() + e.toString());
    }
    List<GrpShowInfo> grpShowInfoList = form.getGrpShowInfoList();
    if (CollectionUtils.isNotEmpty(grpShowInfoList)) {
      total = grpShowInfoList.size();
    }
    AppActionUtils.renderAPPReturnJson(grpShowInfoList, total, status);
    return null;
  }


  /**
   * 检查群组id不为空
   * 
   * @return
   */
  private boolean checkGrpId() {
    try {
      form.setGrpId(Long.valueOf(ServiceUtil.decodeFromDes3(form.getDes3GrpId())));
    } catch (Exception e) {
      logger.error("获取群组Id出错", e);
      status = IOSHttpStatus.BAD_REQUEST;
    }

    if (form.getGrpId() == null || form.getGrpId() == 0L) {
      return false;
    }
    return true;
  }

  /**
   * 检查群组id，空人员id不为
   * 
   * @return
   */
  private boolean checkGrpIdPsnId() {
    if (checkGrpId() && (form.getPsnId() != null && form.getPsnId() != 0L)) {
      return true;
    }
    return false;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpBaseForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public GrpBaseForm getModel() {
    return form;
  }

}
