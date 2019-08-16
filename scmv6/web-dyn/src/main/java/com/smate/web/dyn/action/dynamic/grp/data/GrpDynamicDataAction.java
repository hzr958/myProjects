package com.smate.web.dyn.action.dynamic.grp.data;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.TheadLocalPsnId;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.group.GroupDynShowForm;
import com.smate.web.dyn.service.dynamic.group.GroupDynamicShowService;
import com.smate.web.dyn.service.group.GroupOptService;
import com.smate.web.dyn.service.psn.PersonQueryservice;

/**
 * 群组动态数据服务
 * 
 * @author wsn
 * @date May 10, 2019
 */
public class GrpDynamicDataAction extends ActionSupport implements Preparable, ModelDriven<GroupDynShowForm> {
  private static final long serialVersionUID = -5050867127561568019L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private GroupDynShowForm form;
  @Autowired
  private GroupDynamicShowService groupDynamicShowService;
  @Resource
  private GroupOptService groupOptService;
  @Autowired
  private PersonQueryservice personQueryservice;



  /**
   * 获取群组动态列表
   * 
   * @return
   */
  @Action("/dyndata/grpdyn/list")
  public void ajaxShowGroupDynList() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (NumberUtils.isNotNullOrZero(form.getGroupId())) {
        Long currentPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3CurrentPsnId()), 0L);
        form.setShowNew(1);
        form.setFirstResult((form.getPage().getPageNo() - 1) * 10);
        form.setMaxResults(10);
        form.setCurrentPsnId(currentPsnId);
        TheadLocalPsnId.setPsnId(currentPsnId);
        groupDynamicShowService.buildGrpDynListJsonInfo(form);
        result.put("status", "success");
        result.put("dynInfo", form.getGroupDynShowInfoList());
        result.put("totalCount", form.getPage().getTotalCount());
      }
    } catch (Exception e) {
      logger.error("获取群组动态列表出错,groupId=" + form.getGroupId(), e);
      result.put("status", "error");
    }
    Struts2Utils.renderJson(result, "encoding: UTF-8");
  }



  /**
   * 获取单个群组动态信息
   * 
   * @return
   */
  @Action("/dyndata/grpdyn/info")
  public void showGroupDynDetails() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      if (NumberUtils.isNotNullOrZero(form.getGroupId())) {
        Long currentPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3CurrentPsnId()), 0L);
        form.setShowNew(1);
        form.setCurrentPsnId(currentPsnId);
        form.setDynId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3DynId()), 0L));
        form.setGroupId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3GroupId()), 0L));
        TheadLocalPsnId.setPsnId(currentPsnId);
        groupDynamicShowService.buildGrpDynDetailInfo(form);
        Person currentPsn = personQueryservice.findPersonBase(currentPsnId);
        if (currentPsn != null) {
          result.put("psnName", StringUtils.isNotBlank(currentPsn.getName()) ? currentPsn.getName()
              : (currentPsn.getFirstName() + " " + currentPsn.getLastName()));
          result.put("avatars", currentPsn.getAvatars());
        }
        result.put("status", "success");
        result.put("dynInfo", form.getGrpDynShowInfo());

      }
    } catch (Exception e) {
      logger.error("获取群组动态列表出错,groupId=" + form.getGroupId(), e);
      result.put("status", "error");
    }
    Struts2Utils.renderJson(result, "encoding: UTF-8");
  }

  /**
   * 获取动态评论列表
   * 
   * @return
   */
  @Action("/dyndata/grpdyn/comments")
  public void ajaxGetCommentList() {
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      Long currentPsnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3CurrentPsnId()), 0L);
      form.setCurrentPsnId(currentPsnId);
      form.setDynId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3DynId()), 0L));
      form.setGroupId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3GroupId()), 0L));
      TheadLocalPsnId.setPsnId(currentPsnId);
      groupDynamicShowService.getGroupDynCommentList(form);
      result.put("status", "success");
      result.put("dynCommentList", form.getGroupDynCommentsShowInfoList());
    } catch (Exception e) {
      logger.error("获取群组动态评论列表出错,dynId=" + form.getDynId(), e);
      result.put("status", "error");
    }
    Struts2Utils.renderJson(result, "encoding: UTF-8");
  }

  @Override
  public GroupDynShowForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GroupDynShowForm();
    }
    LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
  }

}
