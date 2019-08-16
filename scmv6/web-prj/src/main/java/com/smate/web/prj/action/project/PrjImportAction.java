package com.smate.web.prj.action.project;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONArray;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.prj.consts.PrjConsts;
import com.smate.web.prj.form.PrjImportForm;
import com.smate.web.prj.service.project.search.ProjectSearchImportService;

/**
 * 联邦检索导入项目Action.
 * 
 * @author wsn
 * @date Dec 14, 2018
 */
@Results({@Result(name = "import_prj_page", location = "/WEB-INF/jsp/prj/import/search_import_first_step.jsp"),
    @Result(name = "import_prj_list", location = "/WEB-INF/jsp/prj/import/import_prj_list.jsp"),
    @Result(name = "import_prj_result", location = "/WEB-INF/jsp/prj/import/import_prj_result.jsp")})
public class PrjImportAction extends ActionSupport implements Preparable, ModelDriven<PrjImportForm> {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private PrjImportForm form;
  @Autowired
  private ProjectSearchImportService projectSearchImportService;
  @Autowired
  private CacheService cacheService;
  @Value("${domain.scm.http}")
  private String snsDomain;


  /**
   * 进入联邦检索导入项目页面.
   * 
   * @return
   */
  @Action("/prjweb/import")
  public String showPrjImportPage() {
    Long psnId = SecurityUtils.getCurrentUserId();
    try {
      // 科研之友域名，页面设置插件地址等地方有用到，要用http协议的
      form.setSnsDomain(snsDomain);
      // 设置当前登录人员ID
      form.setPsnId(SecurityUtils.getCurrentUserId());
      // 设置是本人检索
      form.setIsInitRadio(PrjConsts.SEARCH_ME);
      // 设置要查询的文献库类型为成果
      form.setDbType(PrjConsts.IMPORT_TYPE_PRJ);
      // 初始化检索页面所需信息
      projectSearchImportService.initSearchInfo(form);
      // 获取人员所有的单位信息
      List<WorkHistory> workList = projectSearchImportService.findPsnAllInsInfo(form.getPsnId());
      form.setWorkList(workList);
      // 设置首要单位信息（workList中第一个单位信息）
      for (WorkHistory work : workList) {
        if (CommonUtils.compareLongValue(work.getIsPrimary(), 1L)) {
          form.setPrimaryWork(work);
          break;
        }
      }
    } catch (Exception e) {
      logger.error("进入联邦检索导入项目页面出错， psnId = {}", psnId, e);
    }
    return "import_prj_page";
  }


  /**
   * 显示待导入的项目列表.
   * 
   * @return
   */
  @Action("/prjweb/import/ajaxinit")
  public String initImportPrjList() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      String inputXml = URLDecoder.decode(form.getInputXml(), "UTF-8");
      logger.debug("传入的项目xml内容：" + inputXml);
      if (StringUtils.isNotBlank(inputXml)) {
        form.setInputXml(inputXml);
        // 重新处理下待导入项目的xml
        projectSearchImportService.buildPendingImportPrjByXml(form);
      }
    } catch (Exception e) {
      logger.error("初始化待导入项目数据出错， psnId={}, inputXML={}", form.getPsnId(), form.getInputXml(), e);
      form.setErrorMsg("构建待导入项目信息出错");
    }
    return "import_prj_list";
  }


  /**
   * 保存检索导入的项目.
   * 
   * @return
   */
  @Action("/prjweb/import/ajaxsave")
  public String saveImportPrjs() {
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (StringUtils.isNotBlank(form.getPrjJsonParams())) {
        JSONArray jsonArray = JSONArray.parseArray(form.getPrjJsonParams());
        form.setPrjJsonList(jsonArray);
      }
      projectSearchImportService.savePendingImportPrjs(form);
    } catch (Exception e) {
      logger.error("检索导入项目保存出错, psnId={}", form.getPsnId(), e);
    }
    return "import_prj_result";
  }


  /**
   * 删除缓存的待导入项目信息.
   * 
   * @return
   */
  @Action("/prjweb/cache/ajaxremove")
  public String removeCahcePendingImportPrj() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      cacheService.remove("searchImportPrjCache", form.getCacheKey());
      map.put("result", "success");
    } catch (Exception e) {
      logger.error("删除掉缓存的待导入成果出错, psnId ={} , cacheKey = {}", SecurityUtils.getCurrentUserId(), form.getCacheKey(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }

  @Override
  public PrjImportForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PrjImportForm();
    }
  }

}
