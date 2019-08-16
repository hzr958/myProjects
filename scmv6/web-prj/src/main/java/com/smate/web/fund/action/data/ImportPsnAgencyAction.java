package com.smate.web.fund.action.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.agency.model.FundAgencyForm;
import com.smate.web.fund.service.agency.ImportPsnAgencyService;

@RestController
public class ImportPsnAgencyAction extends ActionSupport implements ModelDriven<FundAgencyForm>, Preparable {
  @Autowired
  private ImportPsnAgencyService importPsnAgencyService;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private FundAgencyForm form;

  /**
   * 同步单位下的人员关注的资助机构
   * 
   * @param params
   * @return
   */
  @Action("/prjdata/updatepsnagency")
  public Object importSIEPsnAgency() {
    Map<String, Object> result = new HashMap<String, Object>();
    Long insId = form.getInsId();
    String agencyIdsStr = form.getAgencyIdsStr();
    try {
      if (insId != null && StringUtils.isNoneBlank(agencyIdsStr)) {
        importPsnAgencyService.importPsnAgency(insId, agencyIdsStr);
        result.put("status", "success");
      }
    } catch (Exception e) {
      result.put("status", "error");
      logger.error("同步单位下人员关注的资助机构出错 insId={},angecyIds={}", insId, agencyIdsStr, e);
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FundAgencyForm();
    }
  }

  @Override
  public FundAgencyForm getModel() {
    return form;
  }
}
