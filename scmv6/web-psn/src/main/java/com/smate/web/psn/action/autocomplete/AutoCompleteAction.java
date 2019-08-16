package com.smate.web.psn.action.autocomplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.consts.model.ConstKeyDisc;
import com.smate.core.base.consts.model.ConstPosition;
import com.smate.core.base.consts.model.ConstRegion;
import com.smate.core.base.consts.service.ConstPositionService;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringHtml;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.form.autocomplete.AutoCompleteForm;
import com.smate.web.psn.model.autocomplete.AcInsUnit;
import com.smate.web.psn.model.autocomplete.AcInstitution;
import com.smate.web.psn.service.autocomplete.AutoCompleteSnsService;

/**
 * 页面上输入单词，进行自动提示的类
 */
public class AutoCompleteAction extends ActionSupport implements ModelDriven<AutoCompleteForm>, Preparable {

  private static final long serialVersionUID = 6866955514755380566L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private static final int MAXSIZE = 10;
  private AutoCompleteForm form;
  @Autowired
  private AutoCompleteSnsService autoCompleteSnsService;
  @Autowired
  private ConstPositionService constPositionService;

  /**
   * 获取自动填充的群组研究领域
   * 
   * @return
   * @throws Exception
   */
  @Action(value = "/psnweb/ac/keywords")
  public String getDisciplinekeyword() throws Exception {
    HttpServletRequest request = Struts2Utils.getRequest();
    String startWith = request.getParameter("q");

    if (StringUtils.isBlank(startWith))
      startWith = "";
    else
      startWith = startWith.trim();

    String data = "[]";
    data = autoCompleteSnsService.autoCompleteDiscipline(MAXSIZE, startWith);
    Struts2Utils.renderJson(data, "encoding:UTF-8");
    return null;
  }

  /**
   * 个人主页-获取自动填充的部门(学院)
   * 
   * @return
   */
  @Actions({@Action("/psnweb/ac/ajaxautoinsunit"), @Action("/psndata/mobile/ajaxautoinsunit")})
  public String getAutoInsUnit() {
    try {
      if ("zh_CN".equals(form.getLocale())) {
        LocaleContextHolder.setLocale(Locale.CHINA, true);
      }
      List<AcInsUnit> insUnitList = autoCompleteSnsService.getAcInsUnit(form);
      if (CollectionUtils.isNotEmpty(insUnitList)) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (AcInsUnit acInsUnit : insUnitList) {
          Map<String, String> map = new HashMap<String, String>();
          if (StringUtils.isEmpty(acInsUnit.getDepartment())) {// 没有系名称时，name值设置为院名称
            map.put("name", acInsUnit.getCollegeName());
          } else {
            map.put("name", acInsUnit.getDepartment());
          }
          map.put("code", acInsUnit.getUnitIds());
          list.add(map);
        }
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(list), "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("个人主页-获取自动填充的部门,出错", e);
    }
    return null;
  }

  /**
   * 个人主页-获取自动填充的职称
   * 
   * @return
   */
  @Actions({@Action("/psnweb/ac/ajaxautoposition"), @Action("/psndata/mobile/ajaxautoposition")})
  public String getAutoPostion() {
    try {
      if ("zh_CN".equals(form.getLocale())) {
        LocaleContextHolder.setLocale(Locale.CHINA, true);
      }
      List<ConstPosition> positionList = constPositionService.getPosLike(form.getSearchKey(), 5);
      if (CollectionUtils.isNotEmpty(positionList)) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ConstPosition constPosition : positionList) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("name", constPosition.getName());
          map.put("code", constPosition.getId());
          list.add(map);
        }
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(list), "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("个人主页-获取自动填充的职称,出错", e);
    }
    return null;
  }

  /**
   * 个人主页-获取自动填充的机构名称
   * 
   * @return
   */
  @Actions({@Action("/psnweb/ac/ajaxautoinstitution"), @Action("/psndata/mobile/ajaxautoinstitution")})
  public String getAutoInstitution() {
    try {
      // 移动端的请求会传locale="zh_CN"，移动端的请求不设置为中文的话生产机会检索英文语言的逻辑
      if ("zh_CN".equals(form.getLocale())) {
        LocaleContextHolder.setLocale(Locale.CHINA, true);
      }
      List<AcInstitution> acInstitutionList = autoCompleteSnsService.getAcInstitution(form.getSearchKey(), null, 5);
      if (CollectionUtils.isNotEmpty(acInstitutionList)) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (AcInstitution acInstitution : acInstitutionList) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("name", acInstitution.getName());
          map.put("code", acInstitution.getCode());
          list.add(map);
        }
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(list), "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("个人主页-获取自动填充的机构名称,出错", e);
    }
    return null;
  }

  /**
   * 自动填充学科关键词
   * 
   * @return
   */
  @Action("/psnweb/recommend/ajaxautoconstkeydiscscodeid")
  public String ajaxAutoConstKeyDiscsCodeId() {
    try {
      List<ConstKeyDisc> constKeyDiscList = autoCompleteSnsService.getConstKeyDiscs(form.getSearchKey(), 5);
      if (constKeyDiscList != null && constKeyDiscList.size() > 0) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (ConstKeyDisc c : constKeyDiscList) {
          Map<String, String> map = new HashMap<String, String>();
          map.put("code", c.getDiscCodes().toString());
          map.put("name", c.getKeyWord());
          list.add(map);
        }
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(list), "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("自动填充学科关键词", e);
    }
    return null;
  }

  /**
   * 
   * 获取自动填充地区
   */
  @Actions({@Action("/psnweb/ac/ajaxautoregion"), @Action("/psnweb/outside/ajaxautoregion")})
  public String getAutoregion() {
    try {
      List<ConstRegion> acRegionList = autoCompleteSnsService.getAcregion(form.getSearchKey(), null, 5);
      if (CollectionUtils.isNotEmpty(acRegionList)) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ConstRegion constRegion : acRegionList) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("name", constRegion.getName());
          map.put("code", constRegion.getId());
          list.add(map);
        }
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(list), "encoding:UTF-8");
      }

    } catch (Exception e) {
      logger.error("个人主页-获取自动填充的机构名称,出错", e);
    }
    return null;
  }

  /**
   * 
   * 获取自动填充地区
   */
  @Actions({@Action("/psnweb/outside/ajaxautoprovinces")})
  public String getAutoProvinces() {
    try {
      List<ConstRegion> acRegionList = autoCompleteSnsService.getAcprovinces(form.getSearchKey(), 5);
      if (CollectionUtils.isNotEmpty(acRegionList)) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ConstRegion constRegion : acRegionList) {
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("name", constRegion.getName());
          map.put("code", constRegion.getId());
          list.add(map);
        }
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(list), "encoding:UTF-8");
      }

    } catch (Exception e) {
      logger.error("个人主页-获取自动填充的机构名称,出错", e);
    }
    return null;
  }

  /**
   * 个人主页-获取自动填充的学历
   * 
   * @return
   */
  @Actions({@Action("/psnweb/ac/ajaxautodegree"), @Action("/psndata/mobile/ajaxautodegree")})
  public String getAutoDegree() {
    try {
      Locale locale = LocaleContextHolder.getLocale();// zh_CN
      List<ConstDictionary> constDictionaryList = autoCompleteSnsService.getAcDegree("psn_degree", form.getSearchKey());
      if (CollectionUtils.isNotEmpty(constDictionaryList)) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ConstDictionary constDictionary : constDictionaryList) {
          Map<String, Object> map = new HashMap<String, Object>();
          if ("en_US".equals(locale + "")) {
            map.put("name", constDictionary.getEnUsName());
          } else {
            map.put("name", constDictionary.getZhCnName());
          }
          map.put("code", constDictionary.getKey().getCode());
          list.add(map);
        }
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(list), "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("个人主页-获取自动填充的学历,出错", e);
    }
    return null;
  }

  /**
   * 自动提示所在地区 SCM-11985_WSN_2017-5-17
   * 
   * @return
   */
  @Action("/psnweb/ac/ajaxregionstr")
  public String buildPsnRegionStr() {
    try {
      // 查询的字符串为空直接返回
      if (StringUtils.isNotBlank(form.getSearchKey())) {
        List<Map<String, Object>> mapList = autoCompleteSnsService.searchConstRegionInfo(form.getSearchKey(), "", 10);
        Struts2Utils.renderJson(JacksonUtils.listToJsonStr(mapList), "encoding:UTF-8");
      }
    } catch (Exception e) {
      logger.error("自动提示地区信息出错，searchKey=" + form.getSearchKey(), e);
    }
    return null;
  }

  /**
   * 成果编辑添加成果作者，从合作者或好友中提示
   * 
   * @return
   */
  @Action("/psnweb/ac/ajaxpsncooperator")
  public String builPsnCooperator() {
    try {
      List<Map<String, Object>> mapList = autoCompleteSnsService.searchPsnCooperator(form, 5);
      Struts2Utils.renderJson(JacksonUtils.listToJsonStr(mapList), "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("成果作者自动提示人名出错，searchKey=" + form.getSearchKey(), e);
    }
    return null;
  }

  /**
   * 获取自动提示JSON数据.
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/ac/ajaxgetComplete")
  public void getAcJsonData() throws Exception {

    String type = form.getType();

    try {
      List<Map<String, Object>> data = null;
      String searchKey = form.getSearchKey();
      if (StringUtils.isNotBlank(type)) {
        if (type.equalsIgnoreCase("awardCategory")) {// 奖励类别自动提示列表

          data = autoCompleteSnsService.getAcAwardCategory(searchKey, MAXSIZE);

        } else if (type.equalsIgnoreCase("awardGrade")) {// 奖励等级列表

          data = autoCompleteSnsService.getAcAwardGrade(searchKey, MAXSIZE);

        } else if (type.equalsIgnoreCase("confName")) {// 会议名称自动提示

          data = autoCompleteSnsService.getAcConfName(searchKey, MAXSIZE);

        } else if (type.equalsIgnoreCase("confOrganizer")) {// 会议组织者自动提示列表

          data = autoCompleteSnsService.getAcConfOrganizer(searchKey, MAXSIZE);

        } else if (type.equalsIgnoreCase("journal")) {// 期刊列表

          data = autoCompleteSnsService.getAcJournal(searchKey, MAXSIZE);

        } else if (type.contains("Title")) { // 期刊标题
          // TODO
          data = autoCompleteSnsService.getAcTitle(searchKey, type);
        } else if (type.equalsIgnoreCase("patentOrg")) {// 发证单位自动提示列表

          data = autoCompleteSnsService.getAcPatentOrg(searchKey, MAXSIZE);

        } else if (type.equalsIgnoreCase("thesisOrg")) {// 颁发单位自动提示列表

          data = autoCompleteSnsService.getAcThesisOrg(searchKey, MAXSIZE);

        } else if (type.equalsIgnoreCase("publisher")) {// 出版社自动提示列表

          data = autoCompleteSnsService.getAcPublisher(searchKey, MAXSIZE);
        }

      }
      Struts2Utils.renderJson(JacksonUtils.listToJsonStr(data), "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("成果编辑自动提示出错，type=" + type, e);
    }
  }

  /**
   * 为成果与人员检索获取自动提示JSON数据.
   * 
   * @return
   * @throws Exception
   */
  @Action("/psnweb/ac/ajaxpdwhsearchpub")
  public void getAcJsonDataForPdwhSearch() throws Exception {
    try {
      List<Map<String, Object>> data = null;
      String searchKey = form.getSearchKey();
      // suggestType=1 使用pub检索的排序提示
      data = this.autoCompleteSnsService.getPdwhSearchSuggest(searchKey, 1);
      String jsonData = StringHtml.toHtmlInput(JacksonUtils.listToJsonStr(data));// 将数据中包含的那些html标签进行转义
      Struts2Utils.renderJson(jsonData, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("成果检索获取自动提示JSON数据，type=pubSearch", e);
    }
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
  }

}
