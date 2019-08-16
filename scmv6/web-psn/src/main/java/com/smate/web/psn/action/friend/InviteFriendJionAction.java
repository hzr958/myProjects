package com.smate.web.psn.action.friend;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
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
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.model.friend.InviteJionForm;
import com.smate.web.psn.service.friend.InvitefriendJionService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 群组-成员-邀请成员加入action
 * 
 * @author lhd
 *
 */
@Results({@Result(name = "invite_friend_jion", location = "/WEB-INF/jsp/friend/InviteFriendJion.jsp"),
    @Result(name = "get_myfriend_names", location = "/WEB-INF/jsp/friend/MyFriendNamesList.jsp"),
    @Result(name = "get_myfriend_names1", location = "/WEB-INF/jsp/friend/MyFriendNamesList1.jsp"),
    @Result(name = "get_myfriend_names2", location = "/WEB-INF/jsp/friend/MyFriendNamesList2.jsp")})
public class InviteFriendJionAction extends ActionSupport implements Preparable, ModelDriven<InviteJionForm> {

  private InviteJionForm form;
  private static final long serialVersionUID = 7986458736317744057L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InvitefriendJionService invitefriendJionService;

  /**
   * 群组-成员-邀请成员加入
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxinvitefriendjion")
  public String ajaxInvateFriendJion() {
    try {
      if (StringUtils.isNotBlank(form.getDes3GroupId())) {
        form.setGroupId(Long.valueOf(ServiceUtil.decodeFromDes3(URLEncoder.encode(form.getDes3GroupId(), "utf-8"))));
      }
      form.setPsnId(SecurityUtils.getCurrentUserId());
      invitefriendJionService.getFriendJionList(form);
    } catch (Exception e) {
      logger.error("群组-成员-邀请好友加入出错", e);
    }
    return "invite_friend_jion";
  }

  /**
   * 群组-成员-邀请成员-自动填充名字
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxautofriendnames")
  public String ajaxAutoFriendNames() {
    if (form.getPsnId() == null || form.getPsnId() == 0L) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
    String data = "[]";
    try {
      List<Map<String, Object>> friendNames = invitefriendJionService.getFriendNames(form);
      if (friendNames != null && friendNames.size() > 0) {
        JSONArray jsonArray = new JSONArray();
        for (Map<String, Object> name : friendNames) {
          JSONObject jsonObj = new JSONObject();
          jsonObj.put("code", Des3Utils.encodeToDes3(name.get("code").toString()));
          jsonObj.put("name", name.get("name"));
          jsonArray.add(jsonObj);
        }
        data = jsonArray.toString();
      }
    } catch (Exception e) {
      logger.error("群组-成员-邀请好友-自动获取好友名字出错", e);
    }
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 群组动态-分享到我的好友-获取我的好友名字
   * 
   * @return
   */
  @Action("/psnweb/friend/ajaxgetmyfriendnames")
  public String ajaxGetMyFriendNames() {
    if (form.getPsnId() == null || form.getPsnId() == 0L) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
    try {
      invitefriendJionService.getMyFriendNames(form);
    } catch (Exception e) {
      logger.error("群组动态-分享到我的好友-获取我的好友名字出错", e);
    }
    if ("1".equals(form.getType())) {
      return "get_myfriend_names1";
    } else if ("2".equals(form.getType())) {
      return "get_myfriend_names2";
    }
    return "get_myfriend_names";
  }

  /**
   * 分享到我的好友-获取我的好友名字-返回json
   */
  @Action("/psndata/friend/ajaxgetmyfriendnames")
  public void buildFriendInfo() {
    Long psnId = form.getPsnId();
    Map<String, Object> result = new HashMap<String, Object>();
    if (NumberUtils.isNotNullOrZero(psnId)) {
      try {
        invitefriendJionService.getMyFriendNames(form);
        result.put("result", form.getPsnInfoList());
        result.put("status", IOSHttpStatus.OK);
        result.put("msg", "query data success");
      } catch (Exception e) {
        logger.error("获取我的好友名字,psnId={}", psnId, e);
        result.put("status", IOSHttpStatus.INTERNAL_SERVER_ERROR);
        result.put("msg", "system error");
      }
    } else {
      result.put("status", IOSHttpStatus.BAD_REQUEST);
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new InviteJionForm();
    }
  }

  @Override
  public InviteJionForm getModel() {
    return form;
  }

}
