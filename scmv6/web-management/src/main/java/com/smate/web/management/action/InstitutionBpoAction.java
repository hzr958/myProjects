package com.smate.web.management.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
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
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.institution.bpo.InstitutionBpo;
import com.smate.web.management.model.institution.bpo.InstitutionRolForm;
import com.smate.web.management.model.institution.rol.ConstCnCity;
import com.smate.web.management.service.institution.ConstCnRegionService;
import com.smate.web.management.service.institution.InstitutionBpoService;

@Results({@Result(name = "main", location = "/WEB-INF/institution/institution_main.jsp"),
    @Result(name = "editMain", location = "/WEB-INF/institution/institution_edit.jsp"),
    @Result(name = "sendResetPwdMain", location = "/WEB-INF/institution/institution_sendResetPwd_thickbox.jsp"),
    @Result(name = "insEditRemark", location = "/WEB-INF/institution/institution_edit_remark_thickbox.jsp")})
public class InstitutionBpoAction extends ActionSupport implements ModelDriven<InstitutionRolForm>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = -6374377938453762623L;
  private InstitutionRolForm form;
  private static final int PAGE_SIZE = 10;
  @Autowired
  private InstitutionBpoService institutionBpoService;
  @Autowired
  private ConstCnRegionService constCnRegionService;

  Logger logger = LoggerFactory.getLogger(getClass());
  private List<User> userList;
  @SuppressWarnings("rawtypes")
  private Page page = new Page(PAGE_SIZE);

  @Action("/scmmanagement/institution/manage/maint")
  public String openInstitutionMain() throws Exception {
    try {
      page = institutionBpoService.getInstitutionByPage(form, page);
    } catch (Exception e) {
      logger.error("获取单位数据时出现异常：", e);
    }
    return "main";
  }

  @Action("/scmmanagement/institution/manage/editMain")
  public String openEditInstitutionMain() throws Exception {
    try {
      form = institutionBpoService.getEditInstitutionDetail(form);
    } catch (Exception e) {
      logger.error("分装单位信息编辑页面所需信息时系统出现异常：", e);
    }
    return "editMain";
  }

  @Action("/scmmanagement/institution/manage/sendResetPwdMain")
  public String openSendResetPwdMain() throws Exception {
    try {
      setUserList(institutionBpoService.getInsAdminByInsId(form.getInsId()));
    } catch (Exception e) {
      logger.error("获取单位管理员账号及邮箱信息时出现异常：", e);
    }

    return "sendResetPwdMain";
  }

  @Action("/scmmanagement/institution/manage/sendResetPwd")
  public String ajaxSendResetPwd() throws Exception {
    MapBuilder mb = MapBuilder.getInstance();
    try {
      String scmUrl = ObjectUtils.toString(Struts2Utils.getServletContext().getAttribute("domainscm"));
      int failedTotal = institutionBpoService.sendResetPwdEmail(form.getJsonParam(), scmUrl);
      if (failedTotal == 0) {
        mb.put("result", "success").put("msg", "重置密码邮件发送成功");
      } else {
        mb.put("result", "warn").put("msg", failedTotal + "个账号重置密码邮件发送失败");
      }
    } catch (Exception e) {
      logger.error("发送重置密码邮件出现异常：", e);
      mb.put("result", "error").put("msg", "重置密码邮件发送失败");
    }
    Struts2Utils.renderJson(mb.getJson(), "encoding:UTF-8");
    return null;
  }

  /**
   * 检查单位名称是否存在
   * 
   * @return
   * @throws Exception
   */
  @Action("/scmmanagement/institution/manage/ajaxCheckInsName")
  public String ajaxCheckInsNameIsUsing() throws Exception {
    MapBuilder mb = MapBuilder.getInstance();
    try {
      if (StringUtils.isBlank(form.getInsName())) {
        mb.put("result", "warn").put("msg", "单位名称不能为空");
      } else {
        boolean isUsing =
            institutionBpoService.checkInsNameIsUsing(form.getInsId(), StringUtils.trim(form.getInsName()));
        if (isUsing) {
          mb.put("result", "success").put("isUsing", true).put("msg",
              "单位名称“" + StringUtils.trim(form.getInsName()) + "”已被使用，是否覆盖此单位信息？");
        } else {
          mb.put("result", "success").put("isUsing", false);
        }
      }
    } catch (Exception e) {
      logger.error("检查单位名称是否已被其他单位使用时出现异常：", e);
      mb.put("result", "error").put("msg", "系统出现异常");
    }

    Struts2Utils.renderJson(mb.getJson(), "encoding:UTF-8");

    return null;
  }

  @Action("/scmmanagement/institution/manage/ajaxCheckDomain")
  public String ajaxCheckDomainIsUsing() throws Exception {
    MapBuilder mb = MapBuilder.getInstance();
    try {
      if (StringUtils.isBlank(form.getInsDomain())) {
        mb.put("result", "warn").put("msg", "单位域名不能为空");
      } else {
        boolean isUsing = institutionBpoService.checkDomainIsUsing(form.getInsId(), form.getInsDomain());
        mb.put("result", "success").put("isUsing", isUsing);
      }
    } catch (Exception e) {
      logger.error("检查单位域名是否被使用出错", e);
      mb.put("result", "error").put("msg", "系统出现异常");
    }
    Struts2Utils.renderJson(mb.getJson(), "encoding:UTF-8");

    return null;
  }

  /**
   * 根据省号获取市
   * 
   * @return
   * @throws Exception
   */
  @Action("/scmmanagement/approve/ajaxSelect")
  public String approveSelect() throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      List<ConstCnCity> cityList = constCnRegionService.getAllCity(form.getProvinceId());
      String cityJson = "[]";
      if (cityList != null && cityList.size() > 0) {
        cityJson = JacksonUtils.listToJsonStr(cityList);
      }
      map.put("result", "success");
      map.put("cityJson", cityJson);
      Struts2Utils.renderJson(map, "encoding:UTF-8");
    } catch (Exception e) {
      map.put("result", "failed");
      Struts2Utils.renderJson(map, "encoding:UTF-8");

    }
    return null;

  }

  /**
   * 修改单位信息
   * 
   * @return
   * @throws Exception
   */
  @Action("/scmmanagement/institution/manage/ajaxEditInstitution")
  public String ajaxEditInstitution() throws Exception {
    MapBuilder mb = MapBuilder.getInstance();
    try {
      if (StringUtils.isBlank(form.getInsName())) {
        mb.put("result", "warn").put("msg", "单位名称不能为空");
      } else {
        boolean isUsing =
            institutionBpoService.checkInsNameIsUsing(form.getInsId(), StringUtils.trim(form.getInsName()));
        if (isUsing) {
          mb.put("result", "warn").put("msg", "单位名称已被其他单位使用");
        } else {
          if (StringUtils.isBlank(form.getInsDomain())) {
            mb.put("result", "warn").put("msg", "单位域名不能为空");
          } else {
            isUsing = institutionBpoService.checkDomainIsUsing(form.getInsId(), form.getInsDomain());
            if (isUsing) {
              mb.put("result", "warn").put("msg", "单位域名已被其他单位使用");
            } else {
              institutionBpoService.editInstitution(form);
              mb.put("result", "success").put("msg", "修改成功");
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("修改单位信息时系统出现异常", e);
      mb.put("result", "error").put("msg", "修改失败");
    }
    Struts2Utils.renderJson(mb.getJson(), "encoding:UTF-8");

    return null;
  }

  @Action("/scmmanagement/institution/manage/openInsEditRemarkPage")
  public String openInsEditRemarkPage() throws Exception {
    try {
      page = institutionBpoService.getInsEditRemarkByPage(form.getInsId(), page);
    } catch (Exception e) {
      logger.error("获取单位修改历史备注出现异常：", e);
    }
    return "insEditRemark";
  }

  @SuppressWarnings("unchecked")
  public Page<InstitutionBpo> getPage() {
    return page;
  }

  public void setPage(Page<InstitutionBpo> page) {
    this.page = page;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null)
      form = new InstitutionRolForm();

  }

  @Override
  public InstitutionRolForm getModel() {
    return form;
  }

  public List<User> getUserList() {
    return userList;
  }

  public void setUserList(List<User> userList) {
    this.userList = userList;
  }
}
