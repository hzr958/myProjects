package com.smate.web.psn.action.profile;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dto.profile.Personal;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.service.profile.PersonalManager;


/**
 * 补充研究领域
 * 
 * @author AiJiangBin
 *
 */
@Results({@Result(name = "test", location = "/WEB-INF/jsp/discipline/mian_improve_discipline.jsp")})
public class PersonPersonalAction extends ActionSupport implements ModelDriven<Personal>, Preparable {

  private static final long serialVersionUID = -1810037093757024670L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private Personal form;
  @Autowired
  private PersonalManager personalManager;

  /**
   * 保存个人研究领域.
   * 
   * @return
   * @throws Exception
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Action("/psnweb/discipline/saveRegResearchDiscipline")
  public String savePsnResearchAreaInfo() throws Exception {
    Map jsonRes = new HashMap();
    form.setPsnId(SecurityUtils.getCurrentUserId());

    // 保存权限，可能为空的情况
    String anyUserStr = Struts2Utils.getParameter("anyUser");
    Integer anyUser = PsnCnfConst.ALLOWS;
    if (NumberUtils.isNumber(anyUserStr)) {
      anyUser = Integer.valueOf(anyUserStr);
    }
    int result = personalManager.savePersonal(form, anyUser);
    if (result == 2) {// 修改研究领域关键词
      jsonRes.put("result", "success");
    } else {
      jsonRes.put("result", "error");
    }
    Struts2Utils.renderJson(jsonRes, "encoding:UTF-8");
    return null;
  }


  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new Personal();
    }
  }

  @Override
  public Personal getModel() {

    return form;
  }


}
