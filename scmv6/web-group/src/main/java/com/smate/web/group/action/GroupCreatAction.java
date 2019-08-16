package com.smate.web.group.action;

import java.util.HashMap;
import java.util.Map;

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
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.exception.GroupException;
import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.service.group.GroupDisciplineService;
import com.smate.web.group.service.group.GroupService;

/**
 * 群组创建action
 * 
 * @author lhd
 *
 */
@Results({@Result(name = "groupMain", location = "/WEB-INF/jsp/createGroup/create_group_main.jsp"),
    @Result(name = "groupsubMain", location = "/WEB-INF/jsp/createGroup/groupsubMain.jsp")})
public class GroupCreatAction extends ActionSupport implements ModelDriven<GroupPsnForm>, Preparable {

  private static final long serialVersionUID = 6670502031787644806L;

  private GroupPsnForm form;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupService groupService;
  @Value("${domainscm}")
  private String domain;
  @Autowired
  private GroupDisciplineService groupDisciplineService;

  /**
   * 对群组的创建进行保存
   */
  @Action("/groupweb/creategroup/ajaxsave")
  public String createGroupSave() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      form.setOpenId("99999999");
      boolean flag = groupService.createGroupInfo(form);

      if (flag) {
        map.put("result", "success");
        String forwardUrl = this.getDomain() + "/groupweb/groupmain/show?des3GroupId=" + form.getDes3GroupId();
        map.put("forwardUrl", forwardUrl);
        Struts2Utils.renderJson(map, "encoding:UTF-8");

      } else {
        map.put("result", "error");
        Struts2Utils.renderJson(map, "encoding:UTF-8");
      }

    } catch (Exception e) {
      map.put("result", "error");
      Struts2Utils.renderJson(map, "encoding:UTF-8");
      logger.error("创建群组出错 web-group", e);
    }
    return null;

  }

  /**
   * 创建群组
   * 
   * @return
   * @throws Exception
   */
  @Action(value = "/groupweb/creategroup/create")
  public String createGroup() throws Exception {
    // 从我的项目点击创建群组是会带过来群组的id
    Object des3prjId = Struts2Utils.getRequest().getAttribute("des3Id");
    String threesys = Struts2Utils.getRequest().getParameter("threesys");
    Object groupName = Struts2Utils.getRequest().getAttribute("groupName");
    Struts2Utils.getRequest().setAttribute("topDisciplineList",
        groupDisciplineService.getTopDisciplineList() == null ? "" : groupDisciplineService.getTopDisciplineList());
    if (des3prjId != null && groupName != null) {
      Struts2Utils.getRequest().setAttribute("step", "2");
      Struts2Utils.getRequest().setAttribute("groupCategory", "11");
    } else if (threesys == null ? false : threesys.equals("1")) {
      Struts2Utils.getRequest().setAttribute("step", "2");
      Struts2Utils.getRequest().setAttribute("groupCategory", "11");
    } else {
      Struts2Utils.getRequest().setAttribute("step", "1");
      // SCM-11286uat，创建群组，选择兴趣群组，群组名会默认填充一个已有的项目群组名，请修改
      Struts2Utils.getRequest().setAttribute("groupName", "");
    }

    return "groupMain";
  }

  /**
   * 获取一级研究领域
   * 
   * @return
   * @throws Exception
   */
  @Action(value = "/groupweb/creategroup/ajaxGetSecondDiscipline")
  public String getTopDisciplineList() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long disciplineId = 0L;
      if (Struts2Utils.getRequest().getParameter("topCategoryId") != null) {
        disciplineId = NumberUtils.toLong(Struts2Utils.getRequest().getParameter("topCategoryId").toString());
      }
      if (disciplineId != 0L) {
        Struts2Utils.getRequest().setAttribute("secondDisciplineList",
            groupDisciplineService.getSecondDisciplineListById(disciplineId) == null ? ""
                : groupDisciplineService.getSecondDisciplineListById(disciplineId));
      }
    } catch (GroupException e) {
      logger.error("获取群组一级学科领域失败", e);

    }
    return "groupsubMain";

  }

  @Override
  public GroupPsnForm getModel() {
    return form;
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

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

}
