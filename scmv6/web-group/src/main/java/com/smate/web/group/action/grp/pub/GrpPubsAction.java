package com.smate.web.group.action.grp.pub;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpPubForm;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.member.GrpRoleService;
import com.smate.web.group.service.grp.pub.GrpPubsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 群组成果action
 * 
 * @author tsz
 *
 */
@SuppressWarnings("serial")
@Results({

    @Result(name = "grppublist", location = "/WEB-INF/jsp/grp/pub/grp_pub_list.jsp"),
    @Result(name = "ajaxshowgrppubsmain", location = "/WEB-INF/jsp/grp/pub/show_grp_pubs_main.jsp"),
    @Result(name = "ajaxselectpublist", location = "/WEB-INF/jsp/grp/discuss/grp_select_pub.jsp"),
    @Result(name = "ajaxmsgpublist", location = "/WEB-INF/jsp/grp/discuss/psn_msg_pub.jsp"),
    @Result(name = "ajaxmemberpublist", location = "/WEB-INF/jsp/grp/pub/grp_member_pub_list.jsp"),
    @Result(name = "ajaxmemberpublist2", location = "/WEB-INF/jsp/grp/pub/grp_member_pub_list2.jsp")})
public class GrpPubsAction extends ActionSupport implements ModelDriven<GrpPubForm>, Preparable {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private GrpPubForm form;

  @Autowired
  private GrpPubsService grpPubsService;
  @Autowired
  private GrpRoleService grpRoleService;
  @Autowired
  private GrpBaseService grpBaseService;

  @Action(value = "/groupweb/grppub/ajaxshowgrppubsmain")
  public String ajaxShowGrpPubsMain() {

    try {
      form.setGrpCategory(grpBaseService.getGrpCategory(form.getGrpId()));
      int role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      form.setRole(role);
    } catch (Exception e) {
      logger.error("进入群组成果异常" + "grpId=" + form.getGrpId(), e);
    }
    return "ajaxshowgrppubsmain";
  }

  /**
   * 获取群组成果列表
   * 
   * @return
   */
  @Action(value = "/groupweb/grppub/ajaxgrppublist")
  public String ajaxGetGrpPubs() {

    try {
      // 课程群组查询所有的 SCM-12664
      Integer grpCategory = grpBaseService.getGrpCategory(form.getGrpId());
      form.setGrpCategory(grpCategory);
      if (grpCategory == 10  || grpCategory == 12 ) {
        form.setShowPrjPub(1);
        form.setShowRefPub(1);
      }
      if (NumberUtils.isNullOrZero(form.getPsnId())) {
        form.setPsnId(SecurityUtils.getCurrentUserId());
      }
      if (!(form.getShowPrjPub() == 0 && form.getShowRefPub() == 0)) {
        grpPubsService.getGrpPubs(form);
      } else {
        form.getPage().setTotalCount(0);
      }
    } catch (Exception e) {
      logger.error("获取群组成果列表数据异常 psnId=" + form.getPsnId() + "; grpId=" + form.getGrpId(), e);
    }
    return "grppublist";
  }

  /**
   * 群组成果列表筛选回显
   * 
   * @return
   */
  @Action(value = "/groupweb/grppub/ajaxgrppublistcallback")
  public String ajaxGetGrpPubsCallBack() {
    try {
      if (form.getPsnId().equals(SecurityUtils.getCurrentUserId())) {
        form.setSelf("yes");
      } else {
        form.setSelf("no");
      }
      if (form.getIsPsnPubs().equals("1")) {
        grpPubsService.getPsnPubsCallBack(form);
      } else {
        grpPubsService.getPubsCallBack(form);
      }
    } catch (Exception e) {
      logger.error("群组成果列表筛选回显异常 psnId=" + form.getPsnId() + "; grpId=" + form.getGrpId(), e);
    }
    Struts2Utils.renderJson(form.getResult2Map(), "encoding:UTF-8");
    return null;
  }

  /**
   * 获取成员成果列表 (页面传入psnid参数)
   * 
   * @return
   */
  @Action(value = "/groupweb/grppub/ajaxmemberpublist")
  public String ajaxmemberpublist() {

    try {
      grpPubsService.getMemberPubs(form);

    } catch (Exception e) {
      logger.error("成员成果列表 psnId=" + form.getPsnId() + "; grpId=" + form.getGrpId(), e);
    }
    return "ajaxmemberpublist";
  }

  /**
   * 获取成员成果列表 (页面传入psnid参数)
   * 
   * @return
   */
  @Action(value = "/groupweb/grppub/ajaxmemberpublist2")
  public String ajaxmemberpublist2() {
    try {
      grpPubsService.getMemberPubs(form);
    } catch (Exception e) {
      logger.error("成员成果列表 psnId=" + form.getPsnId() + "; grpId=" + form.getGrpId(), e);
    }
    return "ajaxmemberpublist2";
  }

  /**
   * 导入成员成果到群组
   * 
   * @return
   */
  @Action(value = "/groupweb/grppub/ajaximportmemberpub")
  public String ajaxImportMemberPub() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      int role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 1 || role == 2 || role == 3) {
        grpPubsService.importMemberPub(form);
        map.put("result", "success");
        map.put("msg", iszhCN ? "操作成功" : "Operated successfully.");
      } else {
        map.put("result", "error");
        map.put("msg", iszhCN ? "没有权限" : "You are not eligible to import member.");
      }
    } catch (Exception e) {
      map.put("result", "error");
      map.put("msg", iszhCN ? "网络异常" : "System error occured, please try again later.");
      logger.error("导入群组成员成果出错 form=" + form.toString(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 标记为群组项目成果或标记为文献
   * 
   * @return
   */
  @Action(value = "/groupweb/grppub/ajaxmarkgrppub")
  public String ajaxmarkgrppub() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      if (StringUtils.isNotBlank(form.getPubIds())) {
        String[] pubIdStrs = form.getPubIds().split(",");
        for (String pubIdStr : pubIdStrs) {
          Long pubId = null;
          if (!NumberUtils.isNumber(pubIdStr)) {
            pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(pubIdStr));
          } else {
            pubId = Long.parseLong(pubIdStr);
          }
          if (pubId == null || pubId == 0L) {
            continue;
          }
          int role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
          boolean isOwner = grpPubsService.checkPubIsOwner(form.getPsnId(), pubId);
          form.setPubId(pubId);
          if (role == 1 || role == 2 || isOwner) {
            grpPubsService.markGrpPub(form);
            map.put("result", "success");
            map.put("msg", iszhCN ? "操作成功" : "Operated successfully.");
          } else {
            map.put("result", "error");
            map.put("msg", iszhCN ? "没有权限" : "You are not eligible to import menber.");
          }
        }
      }
    } catch (Exception e) {
      map.put("result", "error");
      map.put("msg", iszhCN ? "网络异常" : "System error occured, please try again later.");
      logger.error("标记成果出错 form=" + form.toString(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 删除群组文献
   * 
   * @return
   */
  @Action(value = "/groupweb/grppub/ajaxDeleteGrpPub")
  public String ajaxDeleteGrpPub() {
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    Map<String, Object> map = new HashMap<String, Object>();
    List<String> delDes3Pubs = new ArrayList<String>(); // 记录删除的pubId
    try {
      if (StringUtils.isNotBlank(form.getPubIds())) {
        String[] pubIdStrs = form.getPubIds().split(",");
        Long pubId = null;
        for (String pubIdStr : pubIdStrs) {
          if (!NumberUtils.isNumber(pubIdStr)) {
            pubId = Long.parseLong(Des3Utils.decodeFromDes3(pubIdStr));
          } else {
            pubId = Long.parseLong(pubIdStr);
          }
          int role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
          boolean isOwner = grpPubsService.checkPubIsOwner(form.getPsnId(), pubId);
          form.setPubId(pubId);
          if (role == 1 || role == 2 || isOwner) {
            grpPubsService.deleteGrpPub(form);
            map.put("result", "success");
            delDes3Pubs.add(Des3Utils.encodeToDes3(pubId.toString()));
            map.put("msg", iszhCN ? "操作成功" : "Operated successfully.");
            if (map.get("count") != null && StringUtils.isNotBlank(map.get("count").toString())) {// 统计成功操作了多少条记录，没有权限操作的忽略
              map.put("count", Integer.parseInt(map.get("count").toString()) + 1 + "");
            } else {
              map.put("count", "1");
            }
          } else {
            map.put("result", "noPermit");
            map.put("msg", iszhCN ? "没有权限" : "You are not eligible to import menber.");
          }
        }
      }
    } catch (Exception e) {
      map.put("result", "error");
      map.put("msg", iszhCN ? "网络异常" : "System error occured, please try again later.");
      logger.error("删除群组成果出错 form=" + form.toString(), e);
    }
    if (map.get("count") != null && Integer.parseInt(map.get("count").toString()) > 0) {
      map.put("result", "success");
    }
    map.put("delDes3Pubs", delDes3Pubs);
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 选择成果 成果列表获取 //有群组id 查群组成果 没有群组id查个人成果
   * 
   * @return
   */
  @Action(value = "/groupweb/grppub/ajaxselectpublist")
  public String ajaxselectpublist() {

    try {
      grpPubsService.getSelectPubsList(form);

    } catch (Exception e) {
      logger.error("获取选择成果 成果列表出错 " + form.toString(), e);
    }
    return "ajaxselectpublist";
  }

  /**
   * 消息中心查个人成果
   * 
   * @return
   */
  @Action(value = "/groupweb/grppub/ajaxmsgpublist")
  public String ajaxmsgpublist() {
    try {
      grpPubsService.getSelectPubsList(form);
    } catch (Exception e) {
      logger.error("消息中心查个人成果出错 " + form.toString(), e);
    }
    return "ajaxmsgpublist";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpPubForm();
    }
  }

  @Override
  public GrpPubForm getModel() {

    return form;
  }

}
