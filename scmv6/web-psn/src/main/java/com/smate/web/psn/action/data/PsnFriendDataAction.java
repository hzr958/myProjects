package com.smate.web.psn.action.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.model.friend.FriendForm;
import com.smate.web.psn.service.friend.FriendService;

public class PsnFriendDataAction extends ActionSupport implements ModelDriven<FriendForm>, Preparable, Serializable {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private FriendForm form;
  @Autowired
  private FriendService friendService;


  /**
   * 添加联系人
   * 
   * @return
   */
  @Action("/psndata/friend/add")
  public void addFriendReq() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      form.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3PsnId()), 0L));
      resultMap = friendService.dealWithAddFriendReq(form);
    } catch (Exception e) {
      logger.error("添加联系人异常， currentPsnId={}, reqFriendId={}", form.getDes3PsnId(), form.getDes3Id(), e);
      resultMap.put("result", "false");
      resultMap.put("errorMsg", "has a exception");
      resultMap.put("errorCode", "5");
    }
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
  }



  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new FriendForm();
    }
  }

  @Override
  public FriendForm getModel() {
    return form;
  }

}
