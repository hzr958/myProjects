package com.smate.center.batch.chain.prj;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.context.OpenProjectContext;
import com.smate.center.batch.model.sns.prj.OpenPrjMember;
import com.smate.center.batch.model.sns.prj.OpenProject;
import com.smate.center.batch.oldXml.prj.IPrjBriefDriver;
import com.smate.center.batch.oldXml.prj.PrjBriefDriver;
import com.smate.center.batch.oldXml.prj.PrjBriefFormatter;
import com.smate.center.batch.oldXml.prj.PrjXmlConstants;
import com.smate.center.batch.oldXml.prj.PrjXmlDocument;
import com.smate.center.batch.oldXml.prj.PrjXmlFragmentCleanerHelper;
import com.smate.center.batch.oldXml.prj.ProjectHash;
import com.smate.center.batch.service.projectmerge.OpenProjectService;
import com.smate.core.base.utils.common.MoneyFormatterUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 项目合并-数据初始化
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @throws Exception
 */
public class OpenProjectDataHandleChain implements OpenProjectBaseChain {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenProjectService openProjectService;

  @Override
  public boolean can(OpenProjectContext context, OpenProject project) {
    return true;
  }

  @Override
  public OpenProjectContext run(OpenProjectContext context, OpenProject project) throws Exception {
    try {
      Long id = project.getId();
      Long openId = project.getOpenId();
      context.setOpenId(openId);
      context.setToken(project.getToken());
      // 获取成员
      List<OpenPrjMember> members = openProjectService.openPrjMemberDaoGetMembersByPrjId(id);
      context.setOpenPrjMembers(members);
      // 构造XML
      PrjXmlDocument xmlDocument = new PrjXmlDocument();
      context.setXmlDocument(xmlDocument);
      fullProjectMetaAttribute(xmlDocument);
      Element eleProject = ((Element) xmlDocument.getRootNode()).addElement("project");
      fullProjectAttribute(eleProject, project);
      Element eleFullText = ((Element) xmlDocument.getRootNode()).addElement("prj_fulltext");
      fullFullTextAttribute(eleFullText);
      Element eleMembers = ((Element) xmlDocument.getRootNode()).addElement("prj_members");
      fullMembersAttribute(eleMembers, members);
      // 处理预处理
      methodProjectMetaTask(xmlDocument, context);
      methodDateAttributeCleanTask(xmlDocument, context);
      methodPrjMemberCleanTask(xmlDocument, context);
      methodProjectTAKCleanTask(xmlDocument, context);
      methodPrjSchemeTask(xmlDocument, context);
      methodXmlFieldCodePageCleanTask(xmlDocument, context);
      methodMoneyFormatterTask(xmlDocument, context);
      methodSnsPrjDataMapping(xmlDocument, context);
      methodXmlFieldValidateTask(xmlDocument, context);
      methodProjectBriefGenerateTask(xmlDocument, context);
      methodGenerateProjectHashTask(xmlDocument, context);

    } catch (Exception e) {
      logger.error("Open系统-项目合并-数据XML构造失败:openprojectid " + project.getId(), e);
      throw e;
    }
    return context;
  }

  /**
   * 填充prj_meta节点属性
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param ele
   */
  private void fullProjectMetaAttribute(PrjXmlDocument xmlDocument) {
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "tmpl_form", "scholar");
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "prj_id", "");
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "authority", "7");
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "from_prj_id", "");
  }

  /**
   * 填充project节点属性
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param ele
   */
  private void fullProjectAttribute(Element ele, OpenProject project) {
    ele.addAttribute("scheme_id", objectToString(project.getSchemeId()));
    ele.addAttribute("old_scheme_agency_name_en", "");
    ele.addAttribute("zh_keywords", "");
    ele.addAttribute("old_scheme_agency_name", "");
    ele.addAttribute("scheme_agency_name", project.getAgencyName());
    ele.addAttribute("scheme_name_en", project.getEnSchemeName());
    ele.addAttribute("scheme_agency_id", objectToString(project.getAgencyId()));
    ele.addAttribute("scheme_name", project.getSchemeName());
    ele.addAttribute("old_scheme_name_en", "");
    ele.addAttribute("funding_year", objectToString(project.getFundingYear()));
    ele.addAttribute("start_year", objectToString(project.getStartYear()));
    ele.addAttribute("start_month", objectToString(project.getStartMonth()));
    ele.addAttribute("start_day", objectToString(project.getStartDay()));
    ele.addAttribute("amount_unit", project.getAmountUnit());
    ele.addAttribute("en_abstract", "");
    ele.addAttribute("zh_abstract", "");
    ele.addAttribute("scheme_en_id", "");
    ele.addAttribute("prj_type", objectToString(project.getType()));
    ele.addAttribute("prj_state", objectToString(project.getState()));
    ele.addAttribute("prj_external_no", project.getExternalNo());
    ele.addAttribute("start_month", objectToString(project.getStartMonth()));
    ele.addAttribute("en_keywords", "");
    ele.addAttribute("zh_title", project.getZhTitle());
    ele.addAttribute("end_year", objectToString(project.getEndYear()));
    ele.addAttribute("end_month", objectToString(project.getEndMonth()));
    ele.addAttribute("end_day", objectToString(project.getEndDay()));
    ele.addAttribute("amount", objectToString(project.getAmount()));
    ele.addAttribute("ins_id", objectToString(project.getInsId()));
    ele.addAttribute("ins_name", project.getInsName());
    ele.addAttribute("remark", "");
    ele.addAttribute("en_title", project.getEnTitle());
    ele.addAttribute("old_scheme_name", "");
    ele.addAttribute("scheme_agency_name_en", project.getEnAgencyName());
    ele.addAttribute("start_day", objectToString(project.getStartDay()));
    ele.addAttribute("old_ins_name", "");
    ele.addAttribute("discipline", "");
    ele.addAttribute("scheme_agency_en_id", "");
    ele.addAttribute("end_day", objectToString(project.getEndDay()));
    ele.addAttribute("prj_internal_no", project.getInternalNo());
    ele.addAttribute("author_names", project.getAuthorNames());
    ele.addAttribute("author_names_en", project.getAuthorNamesEn());
    ele.addAttribute("source_db_id", objectToString(project.getDbId()));
    ele.addAttribute("is_principal_ins", objectToString(project.getIsPrincipalIns()));
  }

  /**
   * 填充prj_fulltext节点属性
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param ele
   */
  private void fullFullTextAttribute(Element ele) {
    ele.addAttribute("node_id", "1");
    ele.addAttribute("file_desc", "");
    ele.addAttribute("permission", "1");
    ele.addAttribute("file_name", "");
    ele.addAttribute("fulltext_url", "");
    ele.addAttribute("file_id", "");
    ele.addAttribute("upload_date", "");
    ele.addAttribute("ins_id", "");
    ele.addAttribute("file_ext", "");

  }

  /**
   * 填充prj_members节点属性
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param ele
   */
  private void fullMembersAttribute(Element ele, List<OpenPrjMember> members) {
    ele.addAttribute("remove_pms", "");
    if (CollectionUtils.isNotEmpty(members)) {
      for (int i = 0; i < members.size(); i++) {
        Element e1 = ele.addElement("prj_member");
        e1.addAttribute("seq_no", i + 1 + "");
        e1.addAttribute("member_psn_name", members.get(i).getName() == null ? "" : members.get(i).getName());
        e1.addAttribute("owner", "0");
        e1.addAttribute("old_ins_name1", "");
        e1.addAttribute("pm_id", "");
        e1.addAttribute("email", members.get(i).getEmail());
        e1.addAttribute("member_psn_id", "");
        e1.addAttribute("member_psn_name_en", "");
        e1.addAttribute("ins_count", "");
        e1.addAttribute("ins_name1", members.get(i).getInsName());
        e1.addAttribute("old_ins_id1", "");
        e1.addAttribute("ins_id1", "");
        e1.addAttribute("notify_author", objectToString(members.get(i).getNotifyAuthor()));

      }
    }

  }

  /**
   * 预处理
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param xmlDocument
   * @param context
   * @throws Exception
   */
  private void methodProjectMetaTask(PrjXmlDocument xmlDocument, OpenProjectContext context) throws Exception {
    Element meta = (Element) xmlDocument.getPrjMeta();
    if (meta == null) {
      meta = xmlDocument.createElement(PrjXmlConstants.PRJ_META_XPATH);
    }
    Date now = new Date();
    String nowStr = ServiceUtil.formateZhDateFull(now);
    OpenUserUnion user = openProjectService.openUserUnionDaoGetOpenUserUnion(context.getOpenId(), context.getToken());
    String userId = String.valueOf(user.getPsnId());
    context.setCurrentUserId(user.getPsnId());
    // String userId = "1000000728413";
    // context.setCurrentUserId(1000000728413l);
    // 最后更新信息
    meta.addAttribute("last_update_psn_id", userId);
    meta.addAttribute("last_update_date", nowStr);
    meta.addAttribute("record_psn_id", userId);
    meta.addAttribute("create_psn_id", userId);
    meta.addAttribute("create_date", nowStr);
    // meta.addAttribute("source_db_id", "30");
    meta.addAttribute("record_from", "9");// 来源第三方系统合并
    meta.addAttribute("record_node_id", "1");
  }

  /**
   * 处理日期
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param xmlDocument
   * @param context
   * @throws Exception
   */
  private void methodDateAttributeCleanTask(PrjXmlDocument xmlDocument, OpenProjectContext context) throws Exception {
    if (xmlDocument.existsNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_year")) {
      PrjXmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/project/@start_",
          PrjXmlConstants.CHS_DATE_PATTERN);
      PrjXmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/project/@end_",
          PrjXmlConstants.CHS_DATE_PATTERN);
    }
  }

  /**
   * 匹配成员单位信息
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param xmlDocument
   * @param context
   * @throws Exception
   */
  private void methodPrjMemberCleanTask(PrjXmlDocument xmlDocument, OpenProjectContext context) throws Exception {
    // TODO 匹配成员单位 暂时省略
  }

  /**
   * 处理特殊字符
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param xmlDocument
   * @param context
   * @throws Exception
   */
  private void methodProjectTAKCleanTask(PrjXmlDocument xmlDocument, OpenProjectContext context) throws Exception {
    String ctitle = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title");
    ctitle = XmlUtil.trimThreateningHtml(ctitle);
    String etitle = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title");
    etitle = XmlUtil.trimThreateningHtml(etitle);

    etitle = XmlUtil.trimP(etitle);
    ctitle = XmlUtil.trimP(ctitle);

    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title", ctitle);
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title", etitle);

    // 再处理一次存放到数据库
    ctitle = XmlUtil.trimAllHtml(ctitle);
    etitle = XmlUtil.trimAllHtml(etitle);
    if ("".equals(etitle) && "".equals(ctitle)) {
      throw new Exception("项目标题不能为空！！！！！");
    }
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title_text",
        StringUtils.substring(ctitle, 0, 250));
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title_text",
        StringUtils.substring(etitle, 0, 250));
  }

  /**
   * 资助机构,资助类别
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param xmlDocument
   * @param context
   * @throws Exception
   */
  private void methodPrjSchemeTask(PrjXmlDocument xmlDocument, OpenProjectContext context) throws Exception {
    // 资助机构
    String agencyName = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name");
    if (StringUtils.isNotBlank(agencyName)) {
      xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name_en", "");
      xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name", agencyName);
    }
    // 资助类别
    String schemeName = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name");
    if (StringUtils.isNotBlank(schemeName)) {
      xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name_en", "");
      xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name", schemeName);
    }
  }

  /**
   * 全角转半角
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param xmlDocument
   * @param context
   * @throws Exception
   */
  private void methodXmlFieldCodePageCleanTask(PrjXmlDocument xmlDocument, OpenProjectContext context)
      throws Exception {
    List<String> eleList = new ArrayList<String>();
    eleList.add("/project@prj_external_no");
    eleList.add("/project@prj_internal_no");
    for (int index = 0; index < eleList.size(); index++) {
      String[] field = eleList.get(index).split("@");
      String xpath = field[0];
      if (xmlDocument.existsNodeAttribute(xpath, field[1])) {
        String val = xmlDocument.getXmlNodeAttribute(xpath, field[1]);
        val = XmlUtil.changeSBCChar(val);
        xmlDocument.setXmlNodeAttribute(xpath, field[1], val);
      }
    }
  }

  /**
   * 金额处理
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param xmlDocument
   * @param context
   * @throws Exception
   */
  private void methodMoneyFormatterTask(PrjXmlDocument xmlDocument, OpenProjectContext context) throws Exception {
    if (xmlDocument.existsNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount")) {
      String amount = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount");
      amount = MoneyFormatterUtils.format(amount);
      xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount_view", amount);
    }
  }

  /**
   * 项目依托机构
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param xmlDocument
   * @param context
   * @throws Exception
   */
  private void methodSnsPrjDataMapping(PrjXmlDocument xmlDocument, OpenProjectContext context) throws Exception {
    Long insId = createLong(xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_id"));
    String insName = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_name");
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_id", insId == null ? "" : insId.toString());
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_name",
        StringUtils.isBlank(insName) ? "" : insName);
  }

  /**
   * 错误处理,不处理
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param xmlDocument
   * @param context
   * @throws Exception
   */
  private void methodXmlFieldValidateTask(PrjXmlDocument xmlDocument, OpenProjectContext context) throws Exception {
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "is_valid", "1");
  }

  /**
   * 构造列表显示数据
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param xmlDocument
   * @param context
   * @throws Exception
   */
  private void methodProjectBriefGenerateTask(PrjXmlDocument xmlDocument, OpenProjectContext context) throws Exception {
    String formTmpl = xmlDocument.getFormTemplate();
    IPrjBriefDriver briefDriver = new PrjBriefDriver();

    String agencyNameZhTmp = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name");
    String agencyNameEnTmp = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name_en");
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name",
        StringUtils.isNotBlank(agencyNameZhTmp) ? agencyNameZhTmp : agencyNameEnTmp);
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name_en",
        StringUtils.isNotBlank(agencyNameEnTmp) ? agencyNameEnTmp : agencyNameZhTmp);

    String schemeNameZhTmp = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name");
    String schemeNameEnTmp = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name_en");
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name",
        StringUtils.isBlank(schemeNameZhTmp) ? "" : "-" + schemeNameZhTmp);
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name_en",
        StringUtils.isBlank(schemeNameEnTmp) ? "" : "-" + schemeNameEnTmp);

    Map result = briefDriver.getData(context.getLocale(), xmlDocument, context);
    String pattern = briefDriver.getPattern();
    PrjBriefFormatter formatter = new PrjBriefFormatter(context.getLocale(), result);
    String brief = formatter.format(pattern);

    String patternEn = briefDriver.getPatternEn();
    PrjBriefFormatter formatterEn = new PrjBriefFormatter(context.getLocale(), result);
    String briefEn = formatterEn.format(patternEn);

    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "brief_desc",
        StringUtils.isNotBlank(brief) ? brief.replace("-", " - ") : ObjectUtils.toString(briefEn));
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "brief_desc_en",
        StringUtils.isNotBlank(briefEn) ? briefEn.replace("-", " - ") : ObjectUtils.toString(brief));

    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name",
        ObjectUtils.toString(agencyNameZhTmp));
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name_en",
        ObjectUtils.toString(agencyNameEnTmp));
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name",
        ObjectUtils.toString(schemeNameZhTmp));
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name_en",
        ObjectUtils.toString(schemeNameEnTmp));
    formatter = null;

  }

  /**
   * 项目HashCode生成
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param xmlDocument
   * @param context
   * @throws Exception
   */
  private void methodGenerateProjectHashTask(PrjXmlDocument xmlDocument, OpenProjectContext context) throws Exception {

    String etitle = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title");
    String ctitle = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title");
    Integer hash = ProjectHash.cleanTitleHash(ctitle);
    Integer hash2 = ProjectHash.cleanTitleHash(etitle);
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title_hash",
        hash != null ? String.valueOf(hash) : "");
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title_hash",
        hash2 != null ? String.valueOf(hash2) : "");
  }

  /**
   * 工具方法
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param str
   * @return
   */
  private Long createLong(String str) {

    if (StringUtils.isBlank(str) || !str.matches("^(-?[1-9]+[0-9]*)*0?$")) {
      return null;
    }
    return Long.valueOf(str);
  }

  /**
   * 转字符串工具
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param obj
   * @return
   */
  private String objectToString(Object obj) {
    return obj == null ? "" : (obj + "");
  }
}
