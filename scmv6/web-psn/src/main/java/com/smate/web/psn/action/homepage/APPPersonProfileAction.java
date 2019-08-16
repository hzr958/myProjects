package com.smate.web.psn.action.homepage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.AppForm;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.service.keyword.CategoryMapBaseService;
import com.smate.web.psn.service.keyword.CategoryScmService;
import com.smate.web.psn.service.psnwork.PsnConstRegionService;
import com.smate.web.psn.service.sciencearea.ScienceAreaService;

/**
 * 
 * @author LJ
 *
 *         2017年6月27日
 */
public class APPPersonProfileAction extends ActionSupport implements ModelDriven<PersonProfileForm>, Preparable {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private PersonProfileForm form;
  private static final long serialVersionUID = 5924939813513351900L;
  @Autowired
  private ScienceAreaService scienceAreaService;
  @Autowired
  private CategoryMapBaseService categoryMapBaseService;
  private int total = 0;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  @Autowired
  private PsnConstRegionService psnConstRegionService;
  @Autowired
  private CategoryScmService categoryScmService;

  /**
   * 自动提示科技领域数据
   * 
   * @return
   */
  @Action("/app/psnweb/scienceArea/ajaxgetScienceArea")
  public String ajaxGetScienceArea() {
    form.setAppForm(new AppForm());
    try {
      form.setData("[]");
      categoryScmService.findCategoryByName(form, false);
    } catch (Exception e) {
      form.getAppForm().setAppStatus(IOSHttpStatus.INTERNAL_SERVER_ERROR);
      form.getAppForm().setAppMsg("论文推荐提示科技领域数据出错");
      logger.error("自动提示地域数据，form = " + form, e);
    }
    AppActionUtils.doResult(form.getData(), form.getAppForm().getAppTotal(), form.getAppForm().getAppStatus(),
        form.getAppForm().getAppMsg());
    return null;
  }

  /**
   * 自动提示地域
   * 
   * @return
   */
  @Action("/app/psnweb/homepage/ajaxautoregion")
  public String ajaxAutoRegion() {
    try {
      form.setData("[]");
      psnConstRegionService.autoRegionPrompt(form);
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("自动提示地域数据，form = " + form, e);
    }
    AppActionUtils.renderAPPReturnJson(form.getData(), total, status);
    return null;
  }

  /**
   * 保存人员科技领域信息
   * 
   * @return
   */
  @Action("/app/psnweb/sciencearea/ajaxsave")
  public String savePsnScienceArea() {
    Map<String, String> data = new HashMap<String, String>();
    try {
      if (this.isMySelf()) {
        String result = scienceAreaService.savePsnScienceArea(form.getPsnId(), form.getScienceAreaIds());
        data.put("result", result);
      } else {
        data.put("result", "error");
      }
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      logger.error("保存人员科技领域信息出错， psnId = " + form.getPsnId(), e);
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      data.put("result", "error");
    }
    AppActionUtils.renderAPPReturnJson(data, total, status);
    return null;
  }

  /**
   * 编辑人员科技领域
   * 
   * @return
   */
  @Action("/app/psnweb/sciencearea/ajaxedit")
  public String editPsnScienceArea() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String scienceAreaIds = null;
    List<Object> categoryMapBaseInfo = null;
    try {
      this.isMySelf();
      // 查找当前人员的科技领域
      List<PsnScienceArea> scienceAreaList = scienceAreaService.findPsnScienceAreaList(form.getPsnId(), 1);
      // 获取科技领域构建成的Map
      categoryMapBaseInfo = categoryMapBaseService.getCategoryMapBaseInfo(scienceAreaList);
      // 最多5个

      if (scienceAreaList != null && scienceAreaList.size() > 5) {
        scienceAreaList = scienceAreaList.subList(0, 4);
      }
      form.setScienceAreaList(scienceAreaList);
      StringBuilder scienceAreaId = new StringBuilder();
      for (PsnScienceArea area : scienceAreaList) {
        scienceAreaId.append("," + area.getScienceAreaId());
      }
      scienceAreaIds = scienceAreaId.toString();
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      logger.error("人员科技领域出错，psnId = " + form.getPsnId(), e);
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
    }

    map.put("CategoryMap_first", categoryMapBaseInfo);
    map.put("scienceAreaIds", scienceAreaIds);
    AppActionUtils.renderAPPReturnJson(map, total, status);
    return null;

  }

  /**
   * 显示人员科技领域
   * 
   * @return
   */
  @Action("/app/psnweb/sciencearea/ajaxshow")
  public String showPsnScienceArea() {
    try {
      this.isMySelf();
      form = scienceAreaService.buildPsnScienceAreaInfo(form);
      status = IOSHttpStatus.OK;
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("显示人员科技领域信息出错， psnId = " + form.getPsnId(), e);
    }

    List<PsnScienceArea> scienceAreaList = form.getScienceAreaList();
    if (CollectionUtils.isNotEmpty(scienceAreaList)) {
      total = scienceAreaList.size();
    }
    AppActionUtils.renderAPPReturnJson(scienceAreaList, total, status);
    return null;
  }

  /**
   * 判断是否是本人
   * 
   * @return
   */
  private boolean isMySelf() {
    boolean isSelf = true;
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    if (form.getPsnId() == null || form.getPsnId() == 0) {
      form.setPsnId(currentPsnId);
    }
    if (currentPsnId != null && currentPsnId != 0) {
      if (currentPsnId.longValue() == form.getPsnId().longValue()) {
        isSelf = true;
      } else {
        isSelf = false;
      }
    }
    form.setIsMySelf(isSelf);
    return isSelf;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PersonProfileForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public PersonProfileForm getModel() {
    return form;
  }
}
