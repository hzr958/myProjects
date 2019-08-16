package com.smate.web.group.action.share;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.group.form.GroupShareForm;
import com.smate.web.group.service.share.GroupMemeberShareService;

@Results({@Result(name = "get_myfriend_names2", location = "/WEB-INF/jsp/grp/friend/MyFriendNamesList2.jsp"),
    @Result(name = "get_myfriend_names1", location = "/WEB-INF/jsp/grp/friend/MyFriendNamesList1.jsp")})
public class GroupShareAction extends ActionSupport implements ModelDriven<GroupShareForm>, Preparable {
  /**
   * 
   */
  private static final long serialVersionUID = -1866554787502001169L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private GroupShareForm form;
  @Autowired
  private GroupMemeberShareService groupMemeberShareService;

  /**
   * 群组动态-分享到我的好友-获取我的好友名字
   * 
   * @return
   */
  @Action("/groupweb/share/ajaxgetmyfriendnames")
  public String ajaxGetMyFriendNames() {
    if (form.getPsnId() == null || form.getPsnId() == 0L) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
    if (form.getGroupId() == null || form.getGroupId() == 0L) {
      form.setGroupId(Long.valueOf(Des3Utils.decodeFromDes3(form.getDes3GroupId())));
    }
    try {
      groupMemeberShareService.getRecommendPsnIds(form);
    } catch (Exception e) {
      logger.error("群组动态-分享到我的好友-获取我的好友名字出错", e);
    }
    if (form.getType().equals("1")) {
      // 检索人员的功能
      return "get_myfriend_names1";
    } else if (form.getType().equals("2")) {
      // 推荐人员
      return "get_myfriend_names2";
    }
    return null;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GroupShareForm();
    }
    if ((form.getPsnId() == null || form.getPsnId() == 0L)) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
  }

  @Override
  public GroupShareForm getModel() {
    return form;
  }
}
