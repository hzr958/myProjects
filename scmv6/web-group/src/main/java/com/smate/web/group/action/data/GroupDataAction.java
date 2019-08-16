package com.smate.web.group.action.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpBaseForm;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.action.grp.form.GrpPubForm;
import com.smate.web.group.action.grp.form.GrpPubShowInfo;
import com.smate.web.group.action.grp.form.GrpShowInfo;
import com.smate.web.group.form.GroupDataForm;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.model.grp.grpbase.GrpControl;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.member.GrpMemberOptService;
import com.smate.web.group.service.grp.member.GrpRoleService;
import com.smate.web.group.service.grp.pub.GrpPubsService;
import com.smate.web.group.service.grp.rcmd.GrpRcmdService;

public class GroupDataAction extends ActionSupport implements ModelDriven<GroupDataForm>, Preparable {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private GroupDataForm form;
  @Autowired
  private GrpRcmdService grpRcmdService;
  @Autowired
  private GrpBaseService grpBaseService;
  @Autowired
  private GrpRoleService grpRoleService;
  @Autowired
  private GrpMemberOptService grpMemberOptService;
  @Autowired
  private GrpPubsService grpPubsService;

  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态

  /**
   * 获取群组推荐的列表参数{grpCategory:10,disciplineCategory:1,searchKey:"dna"}
   * 
   * @return
   */
  @Action("/grpdata/findgrouplist")
  public String updateShareStatic() {
    GrpBaseForm grpBaseForm = new GrpBaseForm();
    grpBaseForm.setGrpCategory(form.getGrpCategory());
    grpBaseForm.setDisciplineCategory(form.getDisciplineCategory());
    grpBaseForm.setSearchKey(form.getSearchKey());
    grpBaseForm.setPsnId(form.getPsnId());
    grpBaseForm.getPage().setPageNo(form.getPageNo());
    try {
      if (form.getPsnId() != null && form.getPsnId() != 0L) {
        grpRcmdService.getRcmdGrpList(grpBaseForm);
        List<GrpShowInfo> grpShowInfoList = grpBaseForm.getGrpShowInfoList();
        if (CollectionUtils.isNotEmpty(grpShowInfoList)) {
          total = grpBaseForm.getPage().getTotalCount().intValue();
          status = IOSHttpStatus.OK;
        }
        AppActionUtils.renderAPPReturnJson(grpShowInfoList, total, status);
      }
    } catch (Exception e) {
      logger.error("群组推荐列表 ，操作 异常：psnId=" + form.getPsnId() + e.toString());
    }

    return null;
  }

  /**
   * 群组推荐后操作（申请 ， 忽略）
   * 
   * @return
   */
  @Action("/grpdata/optionrcmdgrp")
  public String ajaxOptionRcmdGrp() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (checkGrpIdPsnId() && form.getRcmdStatus() != null) {
        grpRcmdService.optionRcmdGrp(form.getPsnId(),form.getGrpId(),form.getRcmdStatus());
        map.put("status", "success");
        status = IOSHttpStatus.OK;
      } else {
        status = IOSHttpStatus.BAD_REQUEST;
        map.put("status", "error");
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("群组推荐 ，操作 异常：psnId=" + form.getPsnId() + e.toString());
      map.put("status", "error");
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;
  }

  /**
   * 申请/取消申请加入群组
   * 
   * @return
   */
  @Action("/grpdata/applyjoingrp")
  public String applyJoinGrp() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (grpRoleService.IsisExistGrp(form.getGrpId())) {// 群组是否存在判断
        GrpMemberForm baseForm = new GrpMemberForm();
        baseForm.setDes3GrpId(form.getDes3GrpId());
        baseForm.setGrpId(form.getGrpId());
        baseForm.setPsnId(form.getPsnId());
        baseForm.setIsApplyJoinGrp(form.getIsApplyJoinGrp());
        // isApplyJoinGrp 是否是申请加入群组1=申请加入群组，0=取消加群组
        if (form.getIsApplyJoinGrp() == 0) {
          grpMemberOptService.cancelJoinGrp(baseForm);
        } else {
          grpMemberOptService.applyJoinGrp(baseForm);
          grpMemberOptService.updateGrpMemberCount(form.getGrpId());
          map.put("role", grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId()).toString());
        }
        map.put("status", "success");
        status = IOSHttpStatus.OK;
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      map.put("status", "error");
      logger.error("申请/取消申请加入群组出错", e);
    }
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;
  }

  /**
   * 检查群组id，空人员id不为
   * 
   * @return
   */
  private boolean checkGrpIdPsnId() {
    if (NumberUtils.isNotNullOrZero(form.getPsnId()) && NumberUtils.isNotNullOrZero(form.getGrpId())) {
      return true;
    }
    return false;
  }

  /**
   * 获取我的群组列表
   * 
   * @return
   */
  @Action("/grpdata/mygrouplist")
  public String myGroupList() {
    try {
      GrpMainForm mainform = new GrpMainForm();
      mainform.setGrpCategory(form.getGrpCategory());
      mainform.setSearchByRole(form.getSearchByRole());
      mainform.setSearchKey(form.getSearchKey());
      mainform.setPsnId(form.getPsnId());
      mainform.getPage().setPageNo(form.getPageNo());

      grpBaseService.getMyGrpInfoList(mainform);// 查询
      List<GrpShowInfo> grpShowInfoList = mainform.getGrpShowInfoList();
      if (CollectionUtils.isNotEmpty(grpShowInfoList)) {
        total = mainform.getPage().getTotalCount().intValue();
        status = IOSHttpStatus.OK;
      }
      AppActionUtils.renderAPPReturnJson(grpShowInfoList, total, status);
    } catch (Exception e) {
      logger.error("获取我的群组列表出错psnId=" + form.getPsnId(), e);
    }
    return null;
  }

  /**
   * 获取分享群组列表
   * 
   * @return
   */
  @Action("/grpdata/sharegrouplist")
  public String shareGroupList() {
    try {
      GrpMainForm mainform = new GrpMainForm();
      mainform.setSearchKey(form.getSearchKey());
      mainform.setPsnId(form.getPsnId());
      mainform.getPage().setPageNo(form.getPageNo());
      mainform.setSelectAll(false);
      grpBaseService.getAllMyGrp(mainform);

      List<GrpBaseinfo> grpBaseinfoList = mainform.getGrpBaseInfoList();
      if (CollectionUtils.isNotEmpty(grpBaseinfoList)) {
        total = mainform.getPage().getTotalCount().intValue();
        status = IOSHttpStatus.OK;
      }
      AppActionUtils.renderAPPReturnJson(grpBaseinfoList, total, status);
    } catch (Exception e) {
      logger.error("获取我的群组列表出错psnId=" + form.getPsnId(), e);
    }
    return null;
  }

  /**
   * 获取群组设置
   * 
   * @return
   */
  @Action("/grpdata/getgrpcontrol")
  public String getGroupCategoryAndGrpControl() {
    try {
      Map<String, Object> returnData = new HashMap<String, Object>();
      if (grpRoleService.checkRoleVisitGrp(form.getPsnId(), form.getGrpId())) {// 群组是否存在
        // 更新访问时间
        grpMemberOptService.updateVisitDate(form.getPsnId(), form.getGrpId());

        Integer grpCategory = grpBaseService.getGrpCategory(form.getGrpId());
        Integer psnCount = grpBaseService.getProposerCount(form.getGrpId());// 群组申请人数
        GrpControl grpControl = grpBaseService.getCurrGrpControl(form.getGrpId());
        GrpBaseinfo currGrpBaseInfo = grpBaseService.getCurrGrp(form.getGrpId());
        // 人员访问群组的权限
        int psnRole = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
        if (NumberUtils.isNotNullOrZero(grpCategory) && grpControl != null) {
          status = IOSHttpStatus.OK;
        }
        // 是否隐藏 start
        String openType = currGrpBaseInfo.getOpenType();
        boolean flag = true;
        if (psnRole == 1 || psnRole == 2 || psnRole == 3) {
          flag = false;
        }
        if (flag) {
          if ("P".equals(openType)) {
            returnData.put("isPrivate", "1");// 是否隐藏
          }
        }
        // 是否隐藏 end
        returnData.put("status", status);
        returnData.put("grpCategory", grpCategory);
        returnData.put("grpControl", grpControl);
        returnData.put("psnRole", psnRole);
        returnData.put("proposerCount", psnCount);
        returnData.put("openType", openType);
      } else {
        returnData.put("isExist", "0");
      }
      Struts2Utils.renderJsonNoNull(returnData, "encoding:utf-8");
    } catch (Exception e) {
      logger.error("获取我的群组列表出错psnId=" + form.getPsnId(), e);
    }
    return null;
  }

  /**
   * 获取群组成果列表
   * 
   * @return
   */
  @Action("/grpdata/grouppublist")
  public String groupPubList() {
    try {
      // 课程群组查询所有的 SCM-12664
      Integer grpCategory = grpBaseService.getGrpCategory(form.getGrpId());
      GrpPubForm pubForm = new GrpPubForm();
      pubForm.setShowPrjPub(form.getShowPrjPub());
      pubForm.setShowRefPub(form.getShowRefPub());
      pubForm.setSearchKey(form.getSearchKey());
      pubForm.setPublishYear(form.getPublishYear());
      pubForm.setPubType(form.getPubType());
      pubForm.setIncludeType(form.getIncludeType());
      pubForm.setOrderBy(form.getOrderBy());
      pubForm.setDes3PsnId(form.getDes3PsnId());
      pubForm.setPsnId(form.getPsnId());
      pubForm.setGrpId(form.getGrpId());
      pubForm.getPage().setPageNo(form.getPageNo());


      pubForm.setGrpCategory(grpCategory);
      if (grpCategory == 10 || grpCategory == 12) {
        pubForm.setShowPrjPub(1);
        pubForm.setShowRefPub(1);
      }
      if (!(pubForm.getShowPrjPub() == 0 && pubForm.getShowRefPub() == 0)) {
        grpPubsService.getGrpPubs(pubForm);
      } else {
        form.getPage().setTotalCount(0);
      }

      List<GrpPubShowInfo> showPubList = pubForm.getPage().getResult();
      if (CollectionUtils.isNotEmpty(showPubList)) {
        total = pubForm.getPage().getTotalCount().intValue();
        status = IOSHttpStatus.OK;
      }
      AppActionUtils.renderAPPReturnJson(showPubList, total, status);
    } catch (Exception e) {
      logger.error("获取群组成果列表出错grpId={}", form.getGrpId(), e);
    }
    return null;
  }

  /**
   * 获取群组成果列表
   * 
   * @return
   */
  @Action("/grpdata/sharegrppublist")
  public String shareGrpPubList() {
    try {
      // 课程群组查询所有的 SCM-12664
      Integer grpCategory = grpBaseService.getGrpCategory(form.getGrpId());
      GrpPubForm pubForm = new GrpPubForm();
      pubForm.setSearchKey(form.getSearchKey());
      pubForm.setDes3PsnId(form.getDes3PsnId());
      pubForm.setPsnId(form.getPsnId());
      pubForm.setGrpId(form.getGrpId());
      pubForm.getPage().setPageNo(form.getPageNo());


      pubForm.setGrpCategory(grpCategory);

      grpPubsService.getSelectPubsList(pubForm);

      List<GrpPubShowInfo> showPubList = pubForm.getPage().getResult();
      if (CollectionUtils.isNotEmpty(showPubList)) {
        total = pubForm.getPage().getTotalCount().intValue();
        status = IOSHttpStatus.OK;
      }
      AppActionUtils.renderAPPReturnJson(showPubList, total, status);
    } catch (Exception e) {
      logger.error("分享时获取群组成果列表出错grpId={}", form.getGrpId(), e);
    }
    return null;
  }

  /**
   * 分享 选择群组列表
   * 
   * @return
   */

  @Action("/grpdata/share/grplist")
  public String findShareGrpList() {
    Map<String, Object> map = new HashMap<String, Object>();
    List<GrpShowInfo> grpShowInfoList = new ArrayList<GrpShowInfo>();
    String status = "error";
    GrpMainForm mainform = new GrpMainForm();
    try {
      Long psnId = NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L);
      if (NumberUtils.isNotNullOrZero(psnId)) {
        mainform.setSearchKey(form.getSearchKey());
        mainform.setPsnId(psnId);
        mainform.getPage().setPageNo(form.getPageNo());

        grpBaseService.searchMyJoinedGrp(mainform);
        if (CollectionUtils.isNotEmpty(mainform.getGrpBaseInfoList())) {
          for (GrpBaseinfo g : mainform.getGrpBaseInfoList()) {
            GrpShowInfo info = new GrpShowInfo();
            info.setDes3GrpId(Des3Utils.encodeToDes3(g.getGrpId().toString()));
            info.setGrpName(g.getGrpName());
            grpShowInfoList.add(info);
          }
        }
        status = "success";
      }
    } catch (Exception e) {
      logger.error("加载选择群组列表出错", e);
    }
    map.put("totalCount", mainform.getPage() != null ? mainform.getPage().getTotalCount() : 0);
    map.put("status", status);
    map.put("grpInfos", grpShowInfoList);
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }


  @Override
  public GroupDataForm getModel() {
    if (form == null) {
      form = new GroupDataForm();
    }
    return form;
  }

  @Override
  public void prepare() throws Exception {
    LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
  }

}
