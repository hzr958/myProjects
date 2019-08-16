package com.smate.web.group.action.group.pub;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.group.exception.GroupNoAccessException;
import com.smate.web.group.exception.GroupNotExistException;
import com.smate.web.group.exception.GroupParameterException;
import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.service.group.GroupService;
import com.smate.web.group.service.group.pub.GroupPublicationService;

/**
 * 群组成果
 * 
 * @author tsz
 *
 */
@Results({

    @Result(name = "groupPub", location = "/WEB-INF/jsp/grouppub/group_pub_main.jsp"),
    @Result(name = "dyn_group_pub", location = "/WEB-INF/jsp/dynGroupPub/group_pub_main.jsp"),
    @Result(name = "groupPubMain", location = "/WEB-INF/jsp/groupmodule/groupPubMain.jsp"),
    @Result(name = "groupPubList", location = "/WEB-INF/jsp/groupmodule/groupPubList.jsp"),
    @Result(name = "redirectAction", location = "${forwardUrl}", type = "redirect"),
    @Result(name = "leftGroupMemberPub", location = "/WEB-INF/jsp/grouppub/group_pub_members.jsp"),
    @Result(name = "groupMemberPub", location = "/WEB-INF/jsp/groupmodule/groupPubPubmembers.jsp")})
public class GroupPublicationAction extends ActionSupport implements ModelDriven<GroupPsnForm>, Preparable {


  /**
   * 
   */
  private static final long serialVersionUID = -4810887246226995219L;
  private static Logger logger = LoggerFactory.getLogger(GroupPublicationAction.class);

  private GroupPsnForm form;
  @Autowired
  private GroupPublicationService groupPublicationService;
  @Autowired
  private GroupService groupService;
  @Value("${domainscm}")
  private String snsDomain;

  /**
   * 按条件显示群组成果主页面(改造)
   * 
   * @return
   * @throws Exception
   */
  @Action(value = "/groupweb/grouppub/ajaxshowmain")
  public String groupPublicationMain() {
    try {
      String searchKey = StringEscapeUtils.unescapeHtml4(form.getSearchKey());
      if (StringUtils.isNotEmpty(searchKey)) {
        form.setSearchKey(searchKey);
      }
      groupPublicationService.showGroup(form);

      // 与页面样式相似度对应。不能放在shorwgroup方法中，会导致grouppubs修改后更新至数据库（unknown
      // reason）
      // groupPublicationService.setRelevance(form);
    } catch (GroupNoAccessException e) {
      logger.error("没有访问该连接的权限", e);
      String info = "group.tip.noRecord1";
      form.setForwardUrl(this.getSnsDomain() + "/scmwebsns/group/view?groupPsn.des3GroupNodeId="
          + form.getDes3GroupNodeId() + "&groupPsn.des3GroupId=" + form.getDes3GroupId() + "&errInfo=" + info);
      return "redirectAction";
    } catch (Exception e) {
      logger.error("进入群组成果主页面出错", e);
    }
    if ("1".equals(form.getBackType())) {
      return "groupPubMain";
    } else {
      return "groupPubList";
    }
  }

  /**
   * 显示群组成员页面(改造)
   * 
   * @return
   * @throws Exception
   */
  @Action(value = "/groupweb/grouppub/ajaxgrouppubmembers")
  public String ajaxGroupPubMembers() throws Exception {
    try {
      String des3GroupId = form.getDes3GroupId();
      if (des3GroupId != null) {
        if (!NumberUtils.isNumber(des3GroupId)) {
          form.setGroupId(Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupId)));
        } else if (des3GroupId != null) {
          form.setGroupId(Long.valueOf(des3GroupId));
        }
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      // 获取群组的详细信息
      groupService.getGroupPsn(form);
      // 获取群组成员成果列表
      groupPublicationService.ajaxGroupPubMembers(form);
    } catch (GroupNotExistException e) {
      logger.error("群组不存在---groupId=" + form.getGroupId(), e);
    } catch (Exception e) {
      logger.error("获取群组成员成果信息出错", e);
    }
    return "groupMemberPub";
  }

  /**
   * 按条件显示群组成果页面
   * 
   * @return
   * @throws Exception
   */
  @Action(value = "/groupweb/grouppub/show")
  public String publication() {
    try {
      String searchKey = StringEscapeUtils.unescapeHtml4(form.getSearchKey());
      if (getText("application.file.tip.inputTip.groupPubs").equals(searchKey)) {
        form.setSearchKey(null);
      } else {
        form.setSearchKey(searchKey);
      }
      groupPublicationService.show(form);
    } catch (GroupParameterException e) {
      logger.error("访问群组成果页面  参数不正确", e);
    } catch (GroupNotExistException e) {
      logger.error("访问的群组不存在", e);
    } catch (GroupNoAccessException e) {
      logger.error("没有访问改连接的权限", e);
      String info = "group.tip.noRecord1";
      form.setForwardUrl(this.getSnsDomain() + "/scmwebsns/group/view?groupPsn.des3GroupNodeId="
          + form.getDes3GroupNodeId() + "&groupPsn.des3GroupId=" + form.getDes3GroupId() + "&errInfo=" + info);
      return "redirectAction";
    } catch (Exception e) {
      logger.error("进入群组成果页面出错", e);
    }
    return "groupPub";
  }

  @Action(value = "/groupweb/grouppub/ajaxgroupmembers")
  public String ajaxGroupMembers() throws Exception {
    try {
      String des3GroupId = form.getDes3GroupId();
      if (des3GroupId != null) {
        if (!NumberUtils.isNumber(des3GroupId)) {
          form.setGroupId(Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupId)));
        } else if (des3GroupId != null) {
          form.setGroupId(Long.valueOf(des3GroupId));
        }
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      // 获取群组的详细信息
      groupService.getGroupPsn(form);
      // 获取群组成员成果列表
      groupPublicationService.ajaxGroupMembers(form);
    } catch (GroupNotExistException e) {
      logger.error("群组不存在---groupId=" + form.getGroupId(), e);
    } catch (Exception e) {
      logger.error("获取群组成员成果信息出错", e);
    }
    return "leftGroupMemberPub";
  }

  /**
   * 加载群组成果 为了发表动态
   * 
   * @return
   * @throws Exception
   */
  @Action(value = "/groupweb/grouppub/ajaxgrouppubfordyn")
  public String ajaxGroupPubForDyn() throws Exception {
    try {
      String des3GroupId = form.getDes3GroupId();
      if (des3GroupId != null) {
        if (!NumberUtils.isNumber(des3GroupId)) {
          form.setGroupId(Long.valueOf(ServiceUtil.decodeFromDes3(des3GroupId)));
        } else if (des3GroupId != null) {
          form.setGroupId(Long.valueOf(des3GroupId));
        }
      }
      // 获取群组成员成果列表
      if (StringUtils.isNotBlank(form.getSearchKey())) {// scm-8438
        form.setSearchKey(StringEscapeUtils.unescapeHtml4(form.getSearchKey()));
      }
      groupPublicationService.showGroupPubForDyn(form);
    } catch (GroupNotExistException e) {
      logger.error("群组不存在---groupId=" + form.getGroupId(), e);
    } catch (Exception e) {
      logger.error("获取群组成果信息出错", e);
    }
    return "dyn_group_pub";
  }


  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GroupPsnForm();
      form.setGroupPsn(new GroupPsn());
    } else if (form.getGroupPsn() == null) {
      form.setGroupPsn(new GroupPsn());
    }
  }

  @Override
  public GroupPsnForm getModel() {
    return form;
  }

  public String getSnsDomain() {
    return snsDomain;
  }

  public void setSnsDomain(String snsDomain) {
    this.snsDomain = snsDomain;
  }


}
