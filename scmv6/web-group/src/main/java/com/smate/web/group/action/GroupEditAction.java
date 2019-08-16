package com.smate.web.group.action;

import java.net.URLEncoder;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.model.group.homepage.GroupHomePageConfig;
import com.smate.web.group.model.group.pub.Discipline;
import com.smate.web.group.service.group.GroupDisciplineService;
import com.smate.web.group.service.group.GroupEditService;
import com.smate.web.group.service.group.GroupOptService;
import com.smate.web.group.service.group.GroupService;
import com.smate.web.group.service.group.homepage.GroupHomePageService;
import com.smate.web.group.service.group.union.GroupUnionService;

/**
 * 群组编辑保存
 * 
 * @author Administrator
 *
 */
@Results({@Result(name = "editgroup", location = "/WEB-INF/jsp/editgroup/edit_group.jsp"),})
public class GroupEditAction extends ActionSupport implements ModelDriven<GroupPsnForm>, Preparable {
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  private static final long serialVersionUID = 624618313754228499L;
  private GroupPsnForm form;
  @Autowired
  private GroupService groupService;
  @Autowired
  private GroupOptService groupOptService;
  @Autowired
  private GroupHomePageService groupHomePageService;
  @Autowired
  private GroupEditService groupEditService;
  private List<ConstDictionary> constGroupList;
  @Value("${domainscm}")
  private String domain;
  private String forwardUrl;
  @Autowired
  private GroupDisciplineService groupDisciplineService;
  @Autowired
  private GroupUnionService groupUnionService;

  /**
   * 获取数据，进入编辑页面
   * 
   * @return
   */
  @Action("/groupweb/groupedit/ajaxedit")
  public String editGroup() {
    // 当前群组节点.
    try {
      if (form.getGroupId() != null) {
        GroupPsn dataGroupPsn = groupService.findMyGroup(form.getGroupId(), 1);
        if (dataGroupPsn == null) {
          return "groupNotExist";
        }
        form = groupService.modelToForm(dataGroupPsn);
        String isSave = "yes".equals(form.getIsSave()) ? "yes" : "";
        form.setIsSave(isSave);
        // constGroupList =groupService.findConstGroupList();
        /*
         * List<Long> disciplineList = new ArrayList<Long>(); disciplineList.add(form.getDiscipline1()); //
         * 查询获取学科名称. String disciplineNameList = groupService.findDisciplineNameList(disciplineList); if
         * (dataGroupPsn.getDiscipline1() != null) { Discipline discipine =
         * groupDisciplineService.getTopDisciplinetById(dataGroupPsn .getDiscipline1()); }
         * Struts2Utils.getRequest().setAttribute("disciplineNameList", disciplineNameList);
         */
        // 获取一级学科领域
        Struts2Utils.getRequest().setAttribute("topDisciplineList",
            groupDisciplineService.getTopDisciplineList() == null ? "" : groupDisciplineService.getTopDisciplineList());
        // 根据二级学科领域获取一级学科领域
        if (form.getDiscipline1() != null) {
          Discipline topDiscipline = groupDisciplineService.getTopDisciplinetById(form.getDiscipline1());
          Discipline secondDisciplineName = groupDisciplineService.getSecondDisciplinetById(form.getDiscipline1());
          boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
          if (topDiscipline != null) {
            Struts2Utils.getRequest().setAttribute("topCategoryName",
                iszhCN ? topDiscipline.getTopCategoryZhName() : topDiscipline.getTopCategoryEnName());
            Struts2Utils.getRequest().setAttribute("topCategoryId", topDiscipline.getTopCategoryId());
          }
          if (secondDisciplineName != null) {
            Struts2Utils.getRequest().setAttribute("secondCategoryName",
                iszhCN ? secondDisciplineName.getSecondCategoryZhName()
                    : secondDisciplineName.getSecondCategoryEnName());
          }

        }
        // 菜单权限判断用
        // 没有使用的对象 GroupInvitePsn groupInvitePsn =
        // groupService.findGroupInvitePsn(form.getGroupId());
        String tempPageUrl = URLEncoder.encode(ServiceUtil.encodeToDes3(String.valueOf(form.getGroupId())), "utf-8");
        Struts2Utils.getRequest().setAttribute("tempPageUrl", tempPageUrl);
        GroupHomePageConfig groupHomeConfig = groupHomePageService.getHomePageConfig(1L, form.getGroupId());
        if (groupHomeConfig != null) {
          form.setGroupPageUrl(groupHomeConfig.getUrl());
        }
        // 设置权限
        form.setPsnId(SecurityUtils.getCurrentUserId());
        form.setCurrentPsnGroupRoleStatus(groupOptService.getRelationWithGroup(form.getPsnId(), form.getGroupId()));
        form.setHasUnion(groupUnionService.whetherGroupUnion(form.getGroupId()) == true ? 1 : 0);
      }

    } catch (Exception e) {
      LOGGER.error("加载我的群组详细出错", e);
    }
    return "editgroup";

  }

  /**
   * 编辑群组后进行保存操作
   * 
   * @return
   */
  @Action("/groupweb/groupedit/save")
  public String saveGroupEdit() {
    try {
      // // 群组角色状态 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ， 2 是群组普通成员 3 群组管理成员 4.群主
      Integer relationWithGroup =
          groupOptService.getRelationWithGroup(SecurityUtils.getCurrentUserId(), form.getGroupId());
      if (relationWithGroup != 3 && relationWithGroup != 4) {
        return null;
      }
      // 群组简介，有转译字符SCM-10800
      if (StringUtils.isNotBlank(form.getGroupDescription())) {
        form.setGroupDescription(form.getGroupDescription().replaceAll("&nbsp;", " "));
      }
      GroupPsn groupPsn = groupService.formToModel(form);
      groupEditService.updateGroupPsn(groupPsn);
      forwardUrl = this.getDomain() + "/groupweb/groupmain/show?des3GroupId="
          + URLEncoder.encode(form.getDes3GroupId(), "utf-8");
      Struts2Utils.getResponse().sendRedirect(forwardUrl);
    } catch (Exception e) {
      LOGGER.error("保存编辑后的群组失败", e);
    }
    return null;

  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
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

  public GroupPsnForm getForm() {
    return form;
  }

  public void setForm(GroupPsnForm form) {
    this.form = form;
  }

  public List<ConstDictionary> getConstGroupList() {
    return constGroupList;
  }

  public void setConstGroupList(List<ConstDictionary> constGroupList) {
    this.constGroupList = constGroupList;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }
}
