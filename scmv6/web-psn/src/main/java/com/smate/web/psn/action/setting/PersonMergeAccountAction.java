package com.smate.web.psn.action.setting;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.form.MergeCountForm;
import com.smate.web.psn.service.setting.MergeCountService;
import com.smate.web.psn.service.setting.SysMergeUserHistoryService;

/**
 * 账号合并
 * 
 * @author tsz
 *
 */

@Results({@Result(name = "merge_set", location = "/WEB-INF/jsp/psnsetting/merge/merge_set.jsp")})
public class PersonMergeAccountAction extends ActionSupport implements ModelDriven<MergeCountForm>, Preparable {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private MergeCountForm mergeCountForm;

  private List<MergeCountForm> mergeList;
  private String currentStatus; // 当前帐号的合并状态
  private String psnIds; // 但前正在被合并的帐号 获取合并结果用

  @Autowired
  private MergeCountService mergeCountService;
  @Autowired
  private SysMergeUserHistoryService sysMergeUserHistoryService;

  /**
   * 
   */
  private static final long serialVersionUID = -7496914505522794654L;

  /**
   * 监听合并状态 并给出提示
   * 
   * @return
   */
  @Action("/psnweb/psnsetting/ajaxgetmergestatus")
  public String getMergeCount() {
    MapBuilder mb = MapBuilder.getInstance();
    if (StringUtils.isNotBlank(psnIds)) {
      try {
        Map<String, String> map = sysMergeUserHistoryService.getMergeResult(psnIds);
        //
        String scusess = map.get("scusess");
        String error = map.get("error");
        if (scusess != null && scusess.length() > 0) {
          mb.put("mergeScusess", scusess);
          // 是否显示
          mb.put("showScusess", "true");
        }
        if (error != null && error.length() > 0) {
          mb.put("mergeError", map.get("error"));
          mb.put("showError", "true");
        }
        // 是否成功
        mb.put("result", "scusess");

      } catch (Exception e) {
        logger.error("获取合并结果出错", e);
        mb.put("result", "error");
      }
    } else {
      mb.put("result", "scusess");
      // 是否显示
      mb.put("showScusess", "false");
      mb.put("showError", "false");
    }
    Struts2Utils.renderJson(mb.getJson(), "encoding:UTF-8");
    return null;
  }

  /**
   * 进入合并帐号设置页面.
   * 
   * @return
   */
  @Action("/psnweb/psnsetting/ajaxmergeCount")
  public String getMailTypeList() {

    try {
      Long currentPsnId = SecurityUtils.getCurrentUserId();
      mergeCountService.initMainCountInfo(mergeCountForm);
      mergeList = mergeCountService.getMergingList();
      boolean b = sysMergeUserHistoryService.getCurrentPersonMergeStatus(currentPsnId);
      if (b) {
        currentStatus = "true";
      } else {
        currentStatus = "false";
      }
    } catch (Exception e) {
      logger.error("进入合并帐号设置页面出错", e);
    }
    return "merge_set";
  }

  /**
   * 进入添加合并帐号页面.
   * 
   * @return
   */
  @Action("/psnweb/psnsetting/ajaxEnterAddMerge")
  public String enterAddMerge() {
    return "addMerge";
  }

  /**
   * 合并帐号.
   * 
   * @return
   */
  @Action("/psnweb/psnsetting/ajaxMergeCount")
  public String ajaxMergeCount() {
    String resultJson = "";
    String userName = Struts2Utils.getParameter("mergeCount");
    String passWord = Struts2Utils.getParameter("mergePwd");
    if (StringUtils.isBlank(userName)) {
      resultJson = "{\"result\":\"error\",\"msg\":\"" + getText("page.merge.add.tip.nullcount") + "\"}";
    } else if (StringUtils.isBlank(passWord)) {
      resultJson = "{\"result\":\"errpwd\",\"msg\":\"" + getText("page.merge.add.tip.errpwd") + "\"}";
    } else {
      // 验证帐号密码是否正确，并返回其对应用户ID.
      Long targetPsnId = mergeCountService.checkLoginCount(userName, passWord);
      if (targetPsnId != null) {
        Long currPsnId = SecurityUtils.getCurrentUserId();
        if (targetPsnId.longValue() == currPsnId.longValue()) {
          resultJson = "{\"result\":\"error\",\"msg\":\"" + getText("page.merge.add.tip.noSameCount") + "\"}";
        } else if (targetPsnId.longValue() == MergeCountService.CHECK_RESULT_LOGIN_ERROR) {
          // 帐号不存在.
          resultJson =
              "{\"result\":\"errMergeCount\",\"msg\":\"" + getText("page.merge.add.tip.errPwdOrAccount") + "\"}";
        } else if (targetPsnId.longValue() == MergeCountService.CHECK_RESULT_PWD_ERROR) {
          // 密码错误.
          resultJson = "{\"result\":\"errpwd\",\"msg\":\"" + getText("page.merge.add.tip.errPwdOrAccount") + "\"}";
        } else {
          try {
            Long currentPsnId = SecurityUtils.getCurrentUserId();
            // 判断是否重复添加 tsz
            Integer status = sysMergeUserHistoryService.getMergeStatus(currentPsnId, targetPsnId);
            // 如果是 判断是否失败。
            if (status != null) {
              String msgInfo = "";
              if (status == 2) {
                // 重复添加 合并失败账号
                msgInfo = "该账户已经合并失败，请联系客服";
              } else {
                // 重复添加正在合并账号
                msgInfo = getText("page.merge.main.acountHadMerge");
              }
              resultJson = "{\"result\":\"success\",\"msg\":\"" + msgInfo + "\"}";
            } else {
              // 对目标用户的帐号进行合并，并返回其首要邮件.
              String targetEmail = mergeCountService.mergePsnCount(targetPsnId);
              // 提示信息.
              String msgInfo = null;
              String locale = LocaleContextHolder.getLocale().toString();
              if ("en_US".equals(locale)) {
                msgInfo = getText("page.merge.add.tip.succeed1");
              } else {
                msgInfo = getText("page.merge.add.tip.succeed1") + " " + targetEmail + " "
                    + getText("page.merge.add.tip.succeed2");
              }
              resultJson = "{\"result\":\"success\",\"msg\":\"" + msgInfo + "\"}";
            }
          } catch (Exception e) {
            resultJson = "{\"result\":\"error\",\"msg\":\"" + getText("maint.action.fail") + "\"}";
          }
        }
      }
    }
    Struts2Utils.renderJson(resultJson, "encoding:UTF-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (mergeCountForm == null) {
      mergeCountForm = new MergeCountForm();
    }

  }

  @Override
  public MergeCountForm getModel() {
    return mergeCountForm;
  }

  public List<MergeCountForm> getMergeList() {
    return mergeList;
  }

  public void setMergeList(List<MergeCountForm> mergeList) {
    this.mergeList = mergeList;
  }

  public String getCurrentStatus() {
    return currentStatus;
  }

  public void setCurrentStatus(String currentStatus) {
    this.currentStatus = currentStatus;
  }

  public String getPsnIds() {
    return psnIds;
  }

  public void setPsnIds(String psnIds) {
    this.psnIds = psnIds;
  }

}
