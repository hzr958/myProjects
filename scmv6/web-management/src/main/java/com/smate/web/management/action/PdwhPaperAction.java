package com.smate.web.management.action;



import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.management.model.pub.PubInfoForm;
import com.smate.web.management.model.pub.PubPdwhPO;
import com.smate.web.management.service.pub.PdwhPubService;


/**
 * 基准库成果管理系统
 * 
 * @author YHX
 *
 */
@Results({@Result(name = "pubInfoMain", location = "/WEB-INF/paper/paper_main.jsp"),
    @Result(name = "pubInfoList", location = "/WEB-INF/paper/ajax_paper_list.jsp")})
public class PdwhPaperAction extends ActionSupport implements Preparable, ModelDriven<PubInfoForm> {
  private static final long serialVersionUID = 2542151835894564124L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PubInfoForm form;
  @Autowired
  private PdwhPubService pdwhPubService;

  @Action("/scmmanagement/pubInfo/main")
  public String pubInfoMain() {
    return "pubInfoMain";
  }

  @Action("/scmmanagement/pubInfo/ajaxPubList")
  public String pubInfoList() throws Exception {
    pdwhPubService.getPubList(form);
    return "pubInfoList";
  }

  @Action("/scmmanagement/setting/ajaxdelete")
  public String deleteOpt() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (StringUtils.isNotBlank(form.getDes3PubIds())) {
        String[] tmpPubIds = StringUtils.split(form.getDes3PubIds(), ",");
        int deleteCount = 0;
        for (String des3PubId : tmpPubIds) {
          Long pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3PubId));
          PubPdwhPO pubPdwh = pdwhPubService.get(pubId);
          if (pubPdwh == null || pubPdwh.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
            map.put("result", "isDel");
            map.put("msg", "成果不存在");
          } else {
            String result = pdwhPubService.deletePub(des3PubId);
            String status = JacksonUtils.jsonToMap(result).get("status").toString();
            if ("SUCCESS".equals(status)) {
              deleteCount += 1;
            }
            // 记录操作日志
            pdwhPubService.savePubOperateLog(pubId, psnId, 0L, "删除基准库成果");
          }
        }
        if (deleteCount > 0) {
          map.put("result", "success");
          map.put("count", deleteCount);
          map.put("msg", "删除操作成功");
        } else {
          map.put("result", "error");
          map.put("msg", "操作失败");
        }
      } else {
        map.put("result", "error");
        map.put("msg", "操作失败");
      }
    } catch (

    Exception e) {
      map.put("result", "error");
      map.put("msg", "操作失败");
      logger.error("管理系统删除基准库成果出错！", e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Override
  public PubInfoForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    form = new PubInfoForm();
    if (form.getPage() == null) {
      form.setPage(new Page());
    }

  }


}
