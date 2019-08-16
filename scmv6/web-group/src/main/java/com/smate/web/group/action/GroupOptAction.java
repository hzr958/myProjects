package com.smate.web.group.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.form.GroupOptForm;
import com.smate.web.group.model.group.GroupBaseInfo;
import com.smate.web.group.service.group.GroupOptService;

/**
 * 群组操作Action
 * 
 * @author zk
 *
 */
public class GroupOptAction extends ActionSupport implements Preparable, ModelDriven<GroupOptForm> {

  private static final long serialVersionUID = 8622587712978888104L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private GroupOptForm form;
  @Autowired
  private GroupOptService groupOptService;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${groupDyn.restful.url}")
  private String groupDynRestfulUrl;

  /**
   * 文件描述修改
   * 
   * @return
   */
  @Action("/groupweb/groupopt/ajaxgroupfiledescredit")
  public String ajaxGroupFileDescrEdit() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      // // 群组角色状态 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ， 2 是群组普通成员 3 群组管理成员 4.群主
      Integer relationWithGroup =
          groupOptService.getRelationWithGroup(SecurityUtils.getCurrentUserId(), form.getGroupId());
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (relationWithGroup != 0 && relationWithGroup != 1 && groupOptService.editGroupFile(form)) {
        resultMap.put("result", SUCCESS);
      } else {
        resultMap.put("result", ERROR);
      }
    } catch (Exception e) {
      logger.error("群组文件描述修改出错", e);
      resultMap.put("result", ERROR);
    }
    Struts2Utils.renderJson(JacksonUtils.mapToJsonStr(resultMap), "encoding:UTF-8");
    return null;
  }

  /**
   * 群组添加文件
   * 
   * @return
   */
  @Action("/groupweb/groupopt/ajaxgroupaddfile")
  public String ajaxGroupAddFile() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      // // 群组角色状态 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ， 2 是群组普通成员 3 群组管理成员 4.群主
      Integer relationWithGroup =
          groupOptService.getRelationWithGroup(SecurityUtils.getCurrentUserId(), form.getGroupId());
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (relationWithGroup != 0 && relationWithGroup != 1 && groupOptService.addGroupFile(form)) {
        resultMap.put("result", SUCCESS);
        // 发一个群组动态
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("groupId", form.getGroupId());
        map.put("psnId", form.getPsnId());
        map.put("resType", "file");
        map.put("resId", form.getStataionFileId());
        map.put("tempType", "ADDFILE");
        restTemplate.postForObject(this.groupDynRestfulUrl, map, Object.class);
      } else {
        resultMap.put("result", ERROR);
      }
    } catch (Exception e) {
      logger.error("群组添加文件出错", e);
      resultMap.put("result", ERROR);
    }
    Struts2Utils.renderJson(JacksonUtils.mapToJsonStr(resultMap), "encoding:UTF-8");
    return null;
  }

  /**
   * 群组删除文件
   * 
   * @return
   */
  @Action("/groupweb/groupopt/ajaxgroupdeletefile")
  public String ajaxGroupDeleteFile() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      // // 群组角色状态 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ， 2 是群组普通成员 3 群组管理成员 4.群主
      Integer relationWithGroup =
          groupOptService.getRelationWithGroup(SecurityUtils.getCurrentUserId(), form.getGroupId());
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (relationWithGroup != 0 && relationWithGroup != 1 && groupOptService.deleteGroupFile(form)) {
        resultMap.put("result", SUCCESS);
      } else {
        if (form.isFlag()) {
          resultMap.put("result", "NoPermissions");
        } else {
          resultMap.put("result", ERROR);
        }
      }
    } catch (Exception e) {
      logger.error("群组删除文件出错", e);
      resultMap.put("result", ERROR);
    }
    Struts2Utils.renderJson(JacksonUtils.mapToJsonStr(resultMap), "encoding:UTF-8");
    return null;
  }

  /**
   * 判断是否为我的群组文件
   * 
   * @return
   */
  @Action("/groupweb/groupopt/ajaxgroupmyfilelist")
  public String ajaxGroupMyFileList() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      if (groupOptService.isMyGroupFile(form)) {
        resultMap.put("result", SUCCESS);
      } else {
        resultMap.put("result", ERROR);
      }
    } catch (Exception e) {
      logger.error("判断是否为我的群组文件出错", e);
      resultMap.put("result", ERROR);
    }
    Struts2Utils.renderJson(JacksonUtils.mapToJsonStr(resultMap), "encoding:UTF-8");
    return null;
  }

  /**
   * 群组 成员移除
   * 
   * @return
   */
  @Action("/groupweb/groupopt/ajaxdeletemember")
  public String ajaxDeleteMember() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      form.setPsnId(SecurityUtils.getCurrentUserId());
      // // 群组角色状态 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ， 2 是群组普通成员 3 群组管理成员 4.群主
      Integer relationWithGroup = groupOptService.getRelationWithGroup(form.getPsnId(), form.getGroupId());
      if ((relationWithGroup == 3 || relationWithGroup == 4) && groupOptService.deleteGroupMember(form)) {
        resultMap.put("result", SUCCESS);
      } else if (form.getMemberId().equals(form.getPsnId())) {
        resultMap.put("result", "notdelself");
      } else {
        resultMap.put("result", ERROR);
      }
    } catch (Exception e) {
      logger.error("群组成员移除出错", e);
      resultMap.put("result", ERROR);
    }
    Struts2Utils.renderJson(JacksonUtils.mapToJsonStr(resultMap), "encoding:UTF-8");
    return null;
  }

  /**
   * 自动填充群组名字
   * 
   * @return
   */
  @Action("/groupweb/groupopt/ajaxautogroupnames")
  public String ajaxAutoGroupNames() {
    if (form.getPsnId() == null || form.getPsnId() == 0L) {
      form.setPsnId(SecurityUtils.getCurrentUserId());
    }
    try {
      List<GroupBaseInfo> groupNames = groupOptService.getGroupNames(form);
      if (groupNames != null && groupNames.size() > 0) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (GroupBaseInfo g : groupNames) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("code", Des3Utils.encodeToDes3(g.getGroupId().toString()));
          map.put("name", g.getGroupName());
          list.add(map);
        }
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(list), "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("自动填充群组名字出错", e);
    }
    return null;
  }


  @Override
  public GroupOptForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GroupOptForm();
    }
  }

  public GroupOptForm getForm() {
    return form;
  }

  public void setForm(GroupOptForm form) {
    this.form = form;
  }


}
