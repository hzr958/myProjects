package com.smate.web.psn.action.autocomplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.consts.model.ConstPosition;
import com.smate.core.base.consts.service.ConstPositionService;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.form.autocomplete.AutoCompleteForm;
import com.smate.web.psn.model.autocomplete.AcInsUnit;
import com.smate.web.psn.model.autocomplete.AcInstitution;
import com.smate.web.psn.service.autocomplete.AutoCompleteSnsService;

/**
 * app个人主页自动填词
 * 
 * @author LJ
 *
 *         2017年7月11日
 */
public class APPAutoCompleteAction extends ActionSupport implements ModelDriven<AutoCompleteForm>, Preparable {
  private static final long serialVersionUID = 1861311967316820449L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private AutoCompleteForm form;
  @Autowired
  private AutoCompleteSnsService autoCompleteSnsService;
  @Autowired
  private ConstPositionService constPositionService;
  private String status = IOSHttpStatus.NOT_MODIFIED;// 默认状态
  @Value("${domainscm}")
  private String domain;

  private int total;

  /**
   * 个人主页-获取自动填充的部门(学院)
   * 
   * @return
   */
  @Action("/app/psnweb/ajaxautoinsunit")
  public String getAutoInsUnit() {
    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    try {
      List<AcInsUnit> insUnitList = autoCompleteSnsService.getAcInsUnit(form);
      if (CollectionUtils.isNotEmpty(insUnitList)) {
        for (AcInsUnit acInsUnit : insUnitList) {
          Map<String, String> map = new HashMap<String, String>();
          if (StringUtils.isEmpty(acInsUnit.getDepartment())) {// 没有系名称时，name值设置为院名称
            map.put("name", acInsUnit.getCollegeName());
          } else {
            map.put("name", acInsUnit.getDepartment());
          }
          map.put("code", acInsUnit.getUnitIds());
          list.add(map);
          total = list.size();
        }
        status = IOSHttpStatus.OK;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("个人主页-获取自动填充的部门,出错", e);
    }
    AppActionUtils.renderAPPReturnJson(list, total, status);
    return null;
  }

  /**
   * 个人主页-获取自动填充的职称
   * 
   * @return
   */
  @Action("/app/psnweb/ajaxautoposition")
  public String getAutoPostion() {
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    try {
      List<ConstPosition> positionList = constPositionService.getPosLike(form.getSearchKey(), 5);
      if (CollectionUtils.isNotEmpty(positionList)) {
        for (ConstPosition constPosition : positionList) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("name", constPosition.getName());
          map.put("code", constPosition.getId());
          list.add(map);
          total = list.size();
        }
        status = IOSHttpStatus.OK;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("个人主页-获取自动填充的职称,出错", e);
    }
    AppActionUtils.renderAPPReturnJson(list, total, status);

    return null;
  }

  /**
   * 个人主页-获取自动填充的机构名称
   * 
   * @return
   */
  @Action("/app/psnweb/ajaxautoinstitution")
  public String getAutoInstitution() {
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    try {
      List<AcInstitution> acInstitutionList = autoCompleteSnsService.getAcInstitution(form.getSearchKey(), null, 5);
      if (CollectionUtils.isNotEmpty(acInstitutionList)) {
        for (AcInstitution acInstitution : acInstitutionList) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("name", acInstitution.getName());
          map.put("code", acInstitution.getCode());
          map.put("photo", domain + "/insLogo/" + acInstitution.getCode() + ".jpg");
          list.add(map);
          total = list.size();
        }
        status = IOSHttpStatus.OK;
      }
    } catch (Exception e) {
      status = IOSHttpStatus.INTERNAL_SERVER_ERROR;
      logger.error("个人主页-获取自动填充的机构名称,出错", e);
    }
    AppActionUtils.renderAPPReturnJson(list, total, status);

    return null;
  }

  @Override
  public AutoCompleteForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new AutoCompleteForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }
}
