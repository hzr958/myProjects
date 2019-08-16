package com.smate.web.group.action.grp.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.keywords.model.KeywordsHot;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.manage.GrpCreateService;
import com.smate.web.group.service.grp.member.GrpRoleService;

/**
 * 群组管理相关Action
 * 
 * @author zzx
 *
 */
@Results({@Result(name = "ajaxcreategrp", location = "/WEB-INF/jsp/grp/grp_create.jsp"),
    @Result(name = "addGrpScienceArea", location = "/WEB-INF/jsp/editgroup/add_grp_science_area.jsp"),
    @Result(name = "grpedit", location = "/WEB-INF/jsp/grp/grpmain/grp_edit.jsp"),})
public class GrpManageAction extends ActionSupport implements ModelDriven<GrpMainForm>, Preparable {
  private static final long serialVersionUID = -6294769439940020305L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private GrpMainForm form;
  @Autowired
  private GrpRoleService grpRoleService;
  @Autowired
  private GrpBaseService grpBaseService;
  @Autowired
  private GrpCreateService grpCreateService;

  /**
   * 复制群组
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxcopygrp")
  public String copyGrp() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      form.setTargetGrpId(Long.parseLong(Des3Utils.decodeFromDes3(form.getTargetdes3GrpId())));
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getTargetGrpId());
      if (StringUtils.isNotBlank(form.getGrpName())) {
        if (role == 1) {
          grpCreateService.doCopyGrp(form);
          map.put("result", "success");
          map.put("msg", iszhCN ? "操作成功" : "Operated successfully");
          map.put("des3GrpId", form.getDes3GrpId());
        } else {
          map.put("result", "error");
          map.put("msg", iszhCN ? "没有权限操作" : "You are not eligible to copy group");
        }
      } else {
        map.put("result", "error");
        map.put("msg", iszhCN ? "群组名不能为空" : "Group title can not be empty");
      }
    } catch (Exception e) {
      logger.error(" 复制群组出错grpId=" + form.getGrpId(), e);
      map.put("result", "error");
      map.put("msg", iszhCN ? "操作失败" : "Operated failed");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 创建群组页面
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxcreategrp")
  public String ajaxShowCreateGrp() {
    try {
      // TODO
    } catch (Exception e) {
      logger.error("创建群组页面出错", e);
      return null;
    }
    return "ajaxcreategrp";
  }

  /**
   * 编辑群组管理页面
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxmanagegrp")
  public String ajaxShowEditGrp() {
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 1 || role == 2) {
        grpBaseService.getGrpInfo(form);
      } else {
        return null;
      }
    } catch (Exception e) {
      logger.error("编辑群组管理页面出错grpId=" + form.getGrpId(), e);
      return null;
    }
    return "grpedit";
  }

  /**
   * 保存-创建群组
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxsavecreategrp")
  public String ajaxCreateGrp() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      if (StringUtils.isBlank(form.getGrpName())) {
        map.put("msg", iszhCN ? "群组标题不能为空" : "Group title can not be empty");
        map.put("result", "error");
      } else if (form.getFirstCategoryId() == null || form.getFirstCategoryId() == 0) {
        map.put("msg", iszhCN ? "一级研究领域不能为空" : "The primary category can not be empty");
        map.put("result", "error");
      } else {
        grpCreateService.createGrp(form);
        map.put("result", "success");
        map.put("grpId", form.getGrpId().toString());
        map.put("des3GrpId", form.getDes3GrpId());
      }
    } catch (Exception e) {
      map.put("result", "error");
      map.put("msg", iszhCN ? "操作失败" : "Operated failed");
      logger.error("保存-创建群组出错grpId=" + form.getGrpId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 保存-编辑群组
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxsaveeditgrp")
  public String ajaxEditGrp() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 1 || role == 2) {
        grpCreateService.updateGrp(form);
        grpBaseService.modifyGrpPermissions(form);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("保存-编辑群组grpId=" + form.getGrpId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 保存-群组头像
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxsavegrpavartars")
  public String ajaxEditGrpAvartars() {
    Map<String, String> map = new HashMap<String, String>();
    boolean iszhCN = LocaleContextHolder.getLocale().toString().equals("zh_CN");
    try {
      if (StringUtils.isNotBlank(form.getGrpAvartars())) {
        Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
        if (role == 1 || role == 2) {
          grpCreateService.saveGrpAvartars(form);
          map.put("result", "success");
        } else {
          map.put("result", "error");
        }
      } else {
        map.put("result", "error");
        map.put("msg", iszhCN ? "头像地址不能为空" : "Url can not be empty.");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("保存-群组头像失败群组grpId=" + form.getGrpId(), e);
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  /**
   * 动态加载推荐的群组关键词.
   * 
   * @return
   */
  @Action("/groupweb/mygrp/ajaxLoadKeywords")
  public String ajaxRecommendKeywords() throws Exception {
    // 根据群组名称和简介（标题和摘要）进行关键词推荐
    String result = "";
    String groupName = Struts2Utils.getParameter("groupName");
    String groupDescription = Struts2Utils.getParameter("groupDescription");
    String des3GrpId = Struts2Utils.getParameter("des3GrpId");
    if (StringUtils.isNotBlank(groupName) || StringUtils.isNotBlank(groupDescription)) {
      form.setGrpName(groupName);
      form.setGrpDescription(groupDescription);
      form.setDes3GrpId(des3GrpId);
      // 获取推荐的关键词
      try {
        Map<String, List<KeywordsHot>> mapKeyword = grpBaseService.getGroupRcmdKeywords(form);
        if (mapKeyword != null && mapKeyword.size() > 0) {
          result = this.buildRecommendJson(mapKeyword);
        }
      } catch (Exception e) {
        logger.error("获取推荐关键词出错", e);
      }
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }

  /**
   * 构建推荐关键词的JSON字符串.
   * 
   * @param mapKeyword
   * @return
   */
  private String buildRecommendJson(Map<String, List<KeywordsHot>> mapKeyword) {
    Map<String, String> map = new HashMap<String, String>();
    String keywordJsonZh = "";
    String keywordJsonEn = "";
    String keywordJson = "";
    keywordJsonZh =
        this.initKeywordJson(mapKeyword.get(GrpBaseService.RECOMMEND_KW_KEY_ZH), GrpBaseService.RECOMMEND_KW_KEY_ZH);
    keywordJsonEn =
        this.initKeywordJson(mapKeyword.get(GrpBaseService.RECOMMEND_KW_KEY_EN), GrpBaseService.RECOMMEND_KW_KEY_EN);
    String locale = LocaleContextHolder.getLocale().toString();
    if ("zh_CN".equals(locale)) {
      keywordJson = StringUtils.isNotBlank(keywordJsonZh) ? keywordJsonZh : keywordJsonEn;
    } else {
      keywordJson = StringUtils.isNotBlank(keywordJsonEn) ? keywordJsonEn : keywordJsonZh;
    }
    map.put("mapKeywordZh", keywordJsonZh);
    map.put("mapKeywordEn", keywordJsonEn);
    map.put("mapKeyword", keywordJson);
    return JacksonUtils.mapToJsonStr(map);
  }

  /**
   * 转换关键词为JSON.
   * 
   * @param keywordMap
   * @return
   */
  private String initKeywordJson(List<KeywordsHot> keywordList, String language) {
    if (CollectionUtils.isEmpty(keywordList)) {
      return "";
    }
    List<Map<String, String>> result = new ArrayList<>();
    try {
      for (int i = 0, size = keywordList.size(); i < size; i++) {
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put(GrpBaseService.GROUP_RECOMMEND_KEYWORD_ID, keywordList.get(i).getId().toString());
        if (GrpBaseService.RECOMMEND_KW_KEY_EN.equalsIgnoreCase(language)) {
          jsonData.put(GrpBaseService.GROUP_RECOMMEND_KEYWORD, keywordList.get(i).getEkeywords());
          jsonData.put(GrpBaseService.GROUP_RECOMMEND_KEYWORDTXT, keywordList.get(i).getEkwTxt());
        } else {
          jsonData.put(GrpBaseService.GROUP_RECOMMEND_KEYWORD, keywordList.get(i).getKeywords());
          jsonData.put(GrpBaseService.GROUP_RECOMMEND_KEYWORDTXT, keywordList.get(i).getKwTxt());
        }
        result.add(jsonData);
      }
    } catch (Exception e) {
      logger.error("转换关键词为json出错 出错关键词列表：" + keywordList);
    }
    return JacksonUtils.jsonListSerializer(result);
  }


  /**
   * 编辑人员科技领域
   *
   * @return
   */
  @Action("/groupweb/mygrp/ajaxaddsciencearea")
  public String ajaxaddciencearea() {
    try {
      // 获取科技领域构建成的Map
      grpBaseService.buildCategoryMapBaseInfo(form);
    } catch (Exception e) {
      logger.error("弹出科技领域弹出框出错，psnId = " + form.getPsnId(), e);
    }
    return "addGrpScienceArea";
  }


  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GrpMainForm();
    }
  }

  @Override
  public GrpMainForm getModel() {
    return form;
  }
}
