package com.smate.web.prj.action.simplesns;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.prj.form.ProjectForm;
import com.smate.web.prj.service.simplesns.PrjSimpleSnsSaveService;

/**
 * 
 * 上传文件保存数据
 * 
 * @author zx
 *
 */
public class PrjSimpleSnsSaveAction extends ActionSupport implements ModelDriven<ProjectForm>, Preparable {

  private static final long serialVersionUID = 1071878163775619644L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private ProjectForm form;

  @Autowired
  private PrjSimpleSnsSaveService prjSimpleSnsSaveService;

  @Action("/prjweb/project/ajaxdatefulltext")
  public String ajaxUpdatePrjFulltext() throws Exception {

    Map<String, String> map = new HashMap<String, String>();
    try {
      String field = form.getFulltextField();
      if (form.getDes3Id() != null) {
        String prjId = ServiceUtil.decodeFromDes3(form.getDes3Id());
        if (prjId != null && field != null) {
          form.setId(Long.valueOf(prjId));
          map = prjSimpleSnsSaveService.uploadPubXmlFulltext(form);
        }
      }
      map.put("result", "sucess");
    } catch (Exception e) {
      logger.error("项目保存出错", e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new ProjectForm();
    }
  }

  @Override
  public ProjectForm getModel() {
    return form;
  }
}
