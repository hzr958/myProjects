package com.smate.web.dyn.action.dynamic.grp;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.group.GroupDynShowForm;
import com.smate.web.dyn.service.dynamic.group.GroupDynamicShowService;
import com.smate.web.dyn.service.group.GroupOptService;

/**
 * 群组动态显示及相关操作Action
 * 
 * @author tsz
 *
 */
@Results({@Result(name = "groupdynlist", location = "/WEB-INF/jsp/dyn/grp/grp_discuss_list.jsp"),
    @Result(name = "discuss_sample_comment", location = "/WEB-INF/jsp/dyn/grp/grp_discuss_comment_sample.jsp"),
    @Result(name = "discuss_comment", location = "/WEB-INF/jsp/dyn/grp/grp_discuss_comment_list.jsp")

})
public class GrpDynamicShowAction extends ActionSupport implements Preparable, ModelDriven<GroupDynShowForm> {
  private static final long serialVersionUID = -5050867127561568019L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private GroupDynShowForm form;
  @Autowired
  private GroupDynamicShowService groupDynamicShowService;
  @Resource
  private GroupOptService groupOptService;

  /**
   * 删除群组动态
   * 
   * @return
   */
  @Action("/dynweb/dynamic/grpdyn/ajaxdelgrpdyn")
  public String delGrpDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      groupDynamicShowService.delGrpDyn(form);
      map.put("result", "success");
    } catch (Exception e) {
      logger.error("删除群组动态出错,DynId=" + form.getDynId(), e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 获取群组动态列表链接
   * 
   * @return
   */
  @Action("/dynweb/dynamic/grpdyn/ajaxshow")
  public String ajaxShowGroupDynList() {
    try {
      form.setShowNew(1);
      form.setFirstResult((form.getPage().getPageNo() - 1) * 10);
      form.setMaxResults(10);
      groupDynamicShowService.buildGroupDynList(form);
    } catch (Exception e) {
      logger.error("获取群组动态列表出错,groupId=" + form.getGroupId(), e);
    }

    return "groupdynlist";

  }

  /**
   * 群组动态列表附带的评论
   * 
   * @return
   */
  @Action("/dynweb/dynamic/groupdyn/ajaxgetsamplecomment")
  public String ajaxGetCommentsForGroupDynList() {
    try {
      form.setMaxResults(1);
      groupDynamicShowService.getGroupDynCommentList(form);
    } catch (Exception e) {
      logger.error("获取群组动态列表附带的评论出错,groupId=" + form.getGroupId(), e);
    }
    return "discuss_sample_comment";
  }


  /**
   * 获取动态评论列表链接
   * 
   * @return
   */
  @Action("/dynweb/dynamic/groupdyn/ajaxdiscusscommentlist")
  public String ajaxGetCommentList() {
    try {
      groupDynamicShowService.getGroupDynCommentList(form);
    } catch (Exception e) {
      logger.error("获取群组动态评论列表列表出错,dynId=" + form.getDynId(), e);
    }
    return "discuss_comment";
  }

  @Override
  public GroupDynShowForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GroupDynShowForm();
    }
  }
}
