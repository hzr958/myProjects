package com.smate.web.prj.action.data;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.prj.dto.ProjectDTO;

/**
 * 项目保存action
 * 
 * @author YJ
 *
 *         2019年8月8日
 */
public class ProjectSaveAction extends ActionSupport implements Preparable, ModelDriven<ProjectDTO> {

  private static final long serialVersionUID = 8209398728559603670L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  private ProjectDTO prj; // 接受数据的对象，传过来的数据是一个json数据

  /**
   * 项目保存入口
   */
  @Action("/prjdata/project/saveorupdate")
  public void saveOrUpdate() {
    try {


    } catch (Exception e) {
      logger.error("保存项目数据出错！prjJson={}", prj, e);
    }
    Struts2Utils.renderJson(prj.resultMap, "encoding:UTF-8");
  }

  @Override
  public ProjectDTO getModel() {
    return prj;
  }

  @Override
  public void prepare() throws Exception {
    if (prj == null) {
      prj = new ProjectDTO();
    }
  }

}
