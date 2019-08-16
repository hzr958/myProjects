package com.smate.web.dyn.action.dynamic.group;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.group.GroupDynProduceForm;
import com.smate.web.dyn.service.dynamic.group.GroupDynamicOptService;
import com.smate.web.dyn.service.group.GroupOptService;
import com.smate.web.dyn.service.share.ShareStatisticsService;



/**
 * 
 */

@Results({@Result(name = "produce_dyn", location = "/WEB-INF/jsp/dyn/group/produce_dyn.jsp")})
public class GroupDynamicProduceAction extends ActionSupport implements Preparable, ModelDriven<GroupDynProduceForm> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 
   */
  private static final long serialVersionUID = -5050867127561568019L;

  private GroupDynProduceForm form;

  @Resource
  private GroupOptService groupOptService;
  @Resource
  private GroupDynamicOptService groupDynamicOptService;
  @Resource
  private ShareStatisticsService shareStatisticsService;



  /**
   * 发布新动态
   * 
   * @return
   */
  @Action("/dynweb/dynamic/ajaxgroupdyn/dopublish")
  public void ajaxPublishNewDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    if (checkPublistNewDynNeedParam(form)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      try {
        // 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ，
        Integer permission = groupOptService.getRelationWithGroup(form.getPsnId(), form.getReceiverGrpId());
        Integer grpRela = groupOptService.getRelationWithGrp(form.getPsnId(), form.getReceiverGrpId());
        if ((permission == 0 || permission == 1) && grpRela == null) {
          map.put("result", "noPermission");
        } else {
          checkPublistNewDynForm(form);
          String result = groupDynamicOptService.produceGroupDyn(form);
          // 群组动态统计数，另起url更新统计数，参考群组动态分享到群组、我的成果分享到群组
          map.put("result", result);
        }

      } catch (Exception e) {
        map.put("result", "error");
        logger.error("进入动态发布页面异常：psnId=" + form.getPsnId() + ":groupId=" + form.getGroupId(), e);
      }
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

  /**
   * 检查必须要的参数参数
   * 
   * @param form
   */
  boolean checkPublistNewDynNeedParam(GroupDynProduceForm form) {
    if ((form.getGroupId() == null || form.getGroupId() == 0L)
        && (form.getReceiverGrpId() == null || form.getReceiverGrpId() == 0)) {
      return false;
    } else if (SecurityUtils.getCurrentUserId() == 0L) {
      return false;
    } else if (StringUtils.isBlank(form.getDynContent())
        && (StringUtils.isBlank(form.getDes3ResId()) && form.getResId() == null)) {
      return false;
    } else {
      return true;
    }

  }

  /**
   * 检查参数
   * 
   * @param form
   */
  void checkPublistNewDynForm(GroupDynProduceForm form) {
    if (StringUtils.isNotBlank(form.getTempType())) {
      return;
    }
    if (StringUtils.isNotBlank(form.getResType())) {// 说明是添加文件动态
      form.setTempType("ADDRES");
    } else {
      form.setTempType("PUBLISHDYN");
    }
  }

  /**
   * 进入动态发布页面
   * 
   * @return
   */
  @Action("/dynweb/dynamic/groupdyn/ajaxpublishpage")
  public String ajaxPublishPage() {
    Map<String, Object> map = new HashMap<String, Object>();
    if (form.getGroupId() != null && form.getGroupId() != 0L && SecurityUtils.getCurrentUserId() != 0L) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      try {
        // 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ，
        Integer permission = groupOptService.getRelationWithGroup(form.getPsnId(), form.getGroupId());
        if (permission == 0 || permission == 1) {
          map.put("result", "noPermission");
        } else {
          map.put("result", "success");
        }

      } catch (Exception e) {
        map.put("result", "error");
        logger.error("进入动态发布页面异常：psnId=" + form.getPsnId() + ":groupId=" + form.getGroupId() + e.toString());
      }
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Override
  public GroupDynProduceForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GroupDynProduceForm();
    }
  }

}
