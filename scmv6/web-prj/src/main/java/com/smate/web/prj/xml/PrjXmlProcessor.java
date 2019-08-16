package com.smate.web.prj.xml;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.consts.service.InstitutionService;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.project.model.PrjMember;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.service.PrjMemberService;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.common.MoneyFormatterUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.InvalidXpathException;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.fund.recommend.dao.CategoryMapBaseDao;
import com.smate.web.fund.recommend.model.CategoryMapBase;
import com.smate.web.prj.consts.PrjXmlOperationEnum;
import com.smate.web.prj.dao.project.CategoryMapScmNsfcDao;
import com.smate.web.prj.dao.sns.PersonOpenDao;
import com.smate.web.prj.exception.ProjectNotExistException;
import com.smate.web.prj.form.PrjXmlProcessContext;
import com.smate.web.prj.model.common.CategoryMapScmNsfc;
import com.smate.web.prj.model.common.PersonOpen;
import com.smate.web.prj.util.PrjXmlFragmentCleanerHelper;
import com.smate.web.prj.util.PrjXmlHashUtil;

/**
 * 项目XML处理器
 * 
 * @author houchuanjie
 * @date 2018年3月22日 下午3:09:17
 */
public class PrjXmlProcessor {
  private static final Logger logger = LoggerFactory.getLogger(PrjXmlProcessor.class);
  public static final int PRJ_TITLE_MAX_LENGTH = 250;
  private static final String[] SBC_NODE = {"prj_external_no", "prj_internal_no"};

  private final PrjXmlDocument prjXmlDoc;

  public PrjXmlProcessor(PrjXmlDocument prjXmlDoc) {
    this.prjXmlDoc = prjXmlDoc;
  }

  /**
   * @return prjXmlDocument
   */
  public PrjXmlDocument getPrjXmlDocument() {
    return prjXmlDoc;
  }

  /**
   * 复制项目的旧的xml数据，然后用新的参数更新其中的节点数据
   *
   * @author houchuanjie
   * @date 2018年3月21日 下午6:22:09
   * @param oldXmlDoc
   * @param paramsMap
   * @return 返回包含新的PrjXmlDocument实例的PrjXmlProcessor实例，以便于进行其他操作，如果要获取PrjXmlDocument实例，请调用{@link #getPrjXmlDocument()}
   * @throws ProjectNotExistException
   * @throws ServiceException
   */
  public static PrjXmlProcessor copyFromOldPrjXml(PrjXmlDocument oldXmlDoc, Map<String, Object> paramsMap)
      throws ServiceException {
    try {
      PrjXmlDocument newXmlDoc = PrjXmlDocumentBuilder.build(paramsMap);
      String newAuthority = newXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "authority");
      // 保留之前的prj_meta
      Element oldMeta = (Element) oldXmlDoc.getNode(PrjXmlConstants.PRJ_META_XPATH);
      newXmlDoc.copyPrjElement(oldMeta);
      https: // test.scholarmate.com/resmod/js_v8/pub/edit/pub_edit_or_enter.js
      newXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "authority", newAuthority);
      // 保留之前的publication节点(导入时的原始信息)
      Element oldPublication = (Element) oldXmlDoc.getNode(PrjXmlConstants.PUBLICATION_XPATH);
      newXmlDoc.copyPrjElement(oldPublication);
      // 保留之前的pub_authors节点(导入时的原始信息)
      Element oldPubAuthor = (Element) oldXmlDoc.getNode(PrjXmlConstants.PUB_AUTHORS_XPATH);
      newXmlDoc.copyPrjElement(oldPubAuthor);
      // 如存在keyword_plus,sc,author_names_abbr则保存
      String keywordPlus = oldXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "keyword_plus");
      if (StringUtils.isNotBlank(keywordPlus)) {
        newXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "keyword_plus", keywordPlus);
      }
      String categories = oldXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "sc");
      if (StringUtils.isNotBlank(categories)) {
        newXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "sc", categories);
      }
      String prjAuthorsAbbr = oldXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names_abbr");
      if (StringUtils.isNotBlank(prjAuthorsAbbr)) {
        newXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names_abbr", prjAuthorsAbbr);
      }

      return new PrjXmlProcessor(newXmlDoc);
    } catch (Exception e) {
      logger.error("复制并更新项目XML错误！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 更新项目xml元信息，包括编辑人员和编辑时间
   *
   * @author houchuanjie
   * @date 2018年3月21日 下午6:22:55
   * @return
   */
  public PrjXmlProcessor updatePrjMeta(PrjXmlProcessContext context) {
    Element prjMeta = Optional.ofNullable((Element) prjXmlDoc.getPrjMeta())
        .orElse(prjXmlDoc.createElement(PrjXmlConstants.PRJ_META_XPATH));
    String userId = Objects.toString(SecurityUtils.getCurrentUserId());
    String date = ServiceUtil.formateZhDateFull(new Date());
    // 设置编辑的人员id和编辑时间
    prjMeta.addAttribute("last_update_psn_id", userId);
    prjMeta.addAttribute("last_update_date", date);
    if (context != null && NumberUtils.isNotNullOrZero(context.getCurrentPrjId())) {
      prjMeta.addAttribute("prj_id", context.getCurrentPrjId().toString());
    }
    return this;
  }

  public Element addPrjMeta() {
    Element prjMeta = Optional.ofNullable((Element) prjXmlDoc.getPrjMeta())
        .orElse(prjXmlDoc.createElement(PrjXmlConstants.PRJ_META_XPATH));
    String userId = Objects.toString(SecurityUtils.getCurrentUserId());
    String date = ServiceUtil.formateZhDateFull(new Date());
    // 设置编辑的人员id和编辑时间
    prjMeta.addAttribute("last_update_psn_id", userId);
    prjMeta.addAttribute("last_update_date", date);

    prjMeta.addAttribute("record_psn_id", userId);
    prjMeta.addAttribute("create_psn_id", userId);
    prjMeta.addAttribute("create_date", date);
    return prjMeta;
  }

  /**
   * 更新日期节点信息， 包括开始和结束日期
   *
   * @author houchuanjie
   * @date 2018年3月22日 上午11:38:39
   * @return
   */
  public PrjXmlProcessor updateDateNode() {
    String pattern = PrjXmlConstants.CHS_DATE_PATTERN;
    String xpath = PrjXmlConstants.PROJECT_XPATH;
    try {
      if (!prjXmlDoc.existsNodeAttribute(xpath, "start_date") && prjXmlDoc.existsNodeAttribute(xpath, "start_year")) {
        // 编辑页面 没有 start_date 字段 但是 start_year 这个字段
        prjXmlDoc.setXmlNodeAttribute(xpath, "start_date", prjXmlDoc.getXmlNodeAttribute(xpath, "start_year"));
        prjXmlDoc.setXmlNodeAttribute(xpath, "end_date", prjXmlDoc.getXmlNodeAttribute(xpath, "end_year"));
      }
      if (prjXmlDoc.existsNodeAttribute(xpath, "start_date")) {
        String[] startYMD = PrjXmlFragmentCleanerHelper.splitDateYearMonth(prjXmlDoc, xpath, "start_date", pattern);
        prjXmlDoc.setXmlNodeAttribute(xpath, "start_year", startYMD[0]);
        prjXmlDoc.setXmlNodeAttribute(xpath, "start_month", startYMD[1]);
        prjXmlDoc.setXmlNodeAttribute(xpath, "start_day", startYMD[2]);

        String[] endYMD = PrjXmlFragmentCleanerHelper.splitDateYearMonth(prjXmlDoc, xpath, "end_date", pattern);
        prjXmlDoc.setXmlNodeAttribute(xpath, "end_year", endYMD[0]);
        prjXmlDoc.setXmlNodeAttribute(xpath, "end_month", endYMD[1]);
        prjXmlDoc.setXmlNodeAttribute(xpath, "end_day", endYMD[2]);
      }
      if (prjXmlDoc.existsNodeAttribute(xpath, "start_year")) {
        PrjXmlFragmentCleanerHelper.setDateValueFromYMD(prjXmlDoc, "/data/project/@start_", pattern);
        PrjXmlFragmentCleanerHelper.setDateValueFromYMD(prjXmlDoc, "/data/project/@end_", pattern);
      }
      return this;
    } catch (Exception e) {
      logger.error("更新项目XML日期出错！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 更新项目成员信息
   * 
   * @author houchuanjie
   * @date 2018年3月22日 上午11:40:30
   * @return
   */
  @SuppressWarnings("unchecked")
  public PrjXmlProcessor updatePrjMember(InstitutionService institutionService) {
    String xpath = PrjXmlConstants.PROJECT_XPATH;
    // 指派作者节点
    String authorNames = prjXmlDoc.getXmlNodeAttribute(xpath, "author_names");
    if (authorNames.endsWith(";")) {
      authorNames = authorNames.substring(0, authorNames.length() - 1);
      prjXmlDoc.setXmlNodeAttribute(xpath, "author_names", authorNames);
    }
    // 设定prj mebmer owner's member id, 匹配作者单位的ID
    List<Element> authors = prjXmlDoc.getNodes(PrjXmlConstants.PRJ_MEMBERS_MEMBER_XPATH);
    Map<String, String> authorInsIds = new HashMap<String, String>();// 缓存匹配到的ID
    String ownerPsnId = Objects.toString(SecurityUtils.getCurrentUserId());
    Optional.ofNullable(authors).ifPresent(authorEles -> {
      authorEles.forEach(ele -> {
        String owner = ele.attributeValue("owner");// 是否是本人
        String email = StringUtils.trimToEmpty(ele.attributeValue("email"));
        email = XmlUtil.changeSBCChar(email); // 全角转半角
        ele.addAttribute("email", email);
        // 只有个人项目才有此节点
        if (prjXmlDoc.existsNodeAttribute(ele.getPath(), "owner")) {
          if ("1".equals(owner)) {
            ele.addAttribute("member_psn_id", ownerPsnId); // owner的ID
            ele.addAttribute("des3_member_psn_id", Des3Utils.encodeToDes3(ownerPsnId)); // owner的ID
          } else {
            String des3_member_psn_id = ele.attributeValue("des3_member_psn_id");
            if (des3_member_psn_id != "" && NumberUtils.toLong(Des3Utils.decodeFromDes3(des3_member_psn_id)) != 0L) {
              Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3_member_psn_id));
              ele.addAttribute("member_psn_id", psnId.toString());
            } else {
              ele.addAttribute("member_psn_id", "");
            }
            ele.addAttribute("owner", "0");
          }
        }
        String notifyAuthor = StringUtils.trimToEmpty(ele.attributeValue("notify_author"));// 项目负责人
        if (StringUtils.isBlank(notifyAuthor)) {
          ele.addAttribute("notify_author", "0");
        }
        // 获取单位ID
        String insId = StringUtils.trimToEmpty(ele.attributeValue("ins_id1"));
        String insName = StringUtils.trimToEmpty(ele.attributeValue("ins_name1"));
        // ID为空，名字不为空
        if (StringUtils.isBlank(insId) && StringUtils.isNotBlank(insName)) {
          // 匹配单位ID
          if (authorInsIds.containsKey(insName)) {
            insId = authorInsIds.get(insName);
          } else {
            // 匹配单位ID
            Optional<Institution> optIns = institutionService.findByName(insName);
            insId = optIns.map(ins -> Objects.toString(ins.getId())).orElse(null);
            authorInsIds.put(insName, insId);
          }
        }
        ele.addAttribute("ins_name1", insName);
        ele.addAttribute("ins_id1", StringUtils.isBlank(insId) ? null : insId);
      });
    });
    return this;
  }

  /**
   * 处理标题、摘要、关键字的HTML标记等.
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午2:39:18
   * @return
   */
  public PrjXmlProcessor updateTitileAbstractAndKeywords() {
    // 处理etitle,ctitle,eabstract,cabstract,ekeywords,ckeywords

    String ctitle = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title");
    String etitle = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title");
    ctitle = XmlUtil.trimThreateningHtml(ctitle);
    etitle = XmlUtil.trimThreateningHtml(etitle);
    ctitle = XmlUtil.trimP(ctitle);
    etitle = XmlUtil.trimP(etitle);

    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title", ctitle);
    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title", etitle);

    // 再处理一次存放到数据库
    ctitle = XmlUtil.trimAllHtml(ctitle);
    etitle = XmlUtil.trimAllHtml(etitle);

    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title_text",
        StringUtils.substring(ctitle, 0, PRJ_TITLE_MAX_LENGTH));
    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title_text",
        StringUtils.substring(etitle, 0, PRJ_TITLE_MAX_LENGTH));

    String zhAbstract = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_abstract");
    zhAbstract = XmlUtil.trimStartEndP(zhAbstract);
    zhAbstract = XmlUtil.trimThreateningHtml(zhAbstract);
    zhAbstract = zhAbstract.replaceAll("(&nbsp;)+", " ");
    zhAbstract = StringUtils.isBlank(XmlUtil.trimAllHtml(zhAbstract)) ? StringUtils.EMPTY : zhAbstract;
    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_abstract", zhAbstract);

    String enAbstract = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_abstract");
    enAbstract = XmlUtil.trimStartEndP(enAbstract);
    enAbstract = XmlUtil.trimThreateningHtml(enAbstract);
    enAbstract = enAbstract.replaceAll("(&nbsp;)+", " ");
    enAbstract = StringUtils.isBlank(XmlUtil.trimAllHtml(enAbstract)) ? StringUtils.EMPTY : enAbstract;
    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_abstract", enAbstract);

    String enKeywords = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_keywords");
    // enKeywords = PublicationHelper.changeSBCChar(enKeywords).trim();
    enKeywords = StringUtils.strip(enKeywords, ";").trim();
    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_keywords", enKeywords);

    String zhKeywords = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_keywords");
    // zhKeywords = PublicationHelper.changeSBCChar(zhKeywords).trim();
    zhKeywords = org.apache.commons.lang.StringUtils.strip(zhKeywords, ";").trim();
    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_keywords", zhKeywords);

    return this;
  }

  /**
   * 更新项目资助机构
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午3:05:13
   * @return
   */
  public PrjXmlProcessor updateScheme() {
    String agencyName = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name");
    if (StringUtils.isNotBlank(agencyName)) {
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name_en", agencyName);
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name", "");
    }
    // 资助类别
    String schemeName = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name");
    if (StringUtils.isNotBlank(schemeName)) {
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name_en", schemeName);
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name", "");
    }
    return this;
  }


  /**
   * 更新项目资助机构
   *
   * @author wsn
   * @return
   */
  public PrjXmlProcessor updateImportXmlScheme(PrjXmlProcessContext context) {
    if (PrjXmlOperationEnum.Import.equals(context.getCurrentAction())) {
      String dbid =
          StringUtils.trimToEmpty(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "source_db_id"));
      if ("7".equals(dbid)) {
        String agencyName = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name");
        if (StringUtils.isNotBlank(agencyName)) {
          prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name_en", agencyName);
          prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name", "");
        }
        // 资助类别
        String schemeName = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name");
        if (StringUtils.isNotBlank(schemeName)) {
          prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name_en", schemeName);
          prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name", "");
        }
      }
    }
    return this;
  }

  /**
   * SBC case （全角）转 DBC case（半角）
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午3:42:07
   * @return
   */
  public PrjXmlProcessor SBCase2DBCase() {
    for (String node : SBC_NODE) {
      String xpath = PrjXmlConstants.PROJECT_XPATH;
      if (prjXmlDoc.existsNodeAttribute(xpath, node)) {
        String val = prjXmlDoc.getXmlNodeAttribute(xpath, node);
        val = XmlUtil.changeSBCChar(val);
        prjXmlDoc.setXmlNodeAttribute(xpath, node, val);
      }
    }
    return this;
  }

  /**
   * 格式化金额
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午4:00:00
   * @return
   */
  public PrjXmlProcessor formatMoney() {
    if (prjXmlDoc.existsNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount")) {
      String amount = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount");
      amount = MoneyFormatterUtils.formatNum(amount);
      if (StringUtils.isBlank(amount) || "0".equals(amount)) {
        prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount", "");
      }
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount_view", amount);
    }
    return this;
  }

  /**
   * 更新项目依托单位
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午4:11:36
   * @param institutionService
   * @return
   */
  public PrjXmlProcessor updateIns(InstitutionService institutionService) {
    // 依托单位，存在名称找ID，存在ID找名称.
    Long insId = IrisNumberUtils.createLong(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_id"));
    String insName = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_name");
    insName = StringUtils.trimToEmpty(insName);
    if (Objects.isNull(insId) && StringUtils.isNotBlank(insName)) {
      // 很多导入依托单位以分号结尾，删除结尾分号
      if (insName.length() > 1 && (insName.endsWith(";") || insName.endsWith("；"))) {
        insName = insName.substring(0, insName.length() - 1);
      }
      insId = institutionService.findByName(insName).map(ins -> ins.getId()).orElse(null);
    } else if (Objects.nonNull(insId) && StringUtils.isBlank(insName)) {
      // SCM-854 用户可以清除原来的依托单位
      insId = null;
    }
    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_id",
        Objects.isNull(insId) ? StringUtils.EMPTY : insId.toString());
    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_name",
        StringUtils.isBlank(insName) ? StringUtils.EMPTY : insName);
    return this;
  }

  /**
   * 更新文件导入的数据
   * 
   * @return
   */
  public PrjXmlProcessor updateFileImportData(CategoryMapBaseDao categoryMapBaseDao,
      CategoryMapScmNsfcDao categoryMapScmNsfcDao) {
    String file_import = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "file_import");
    if (StringUtils.isBlank(file_import))
      return this;
    String science_area = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "science_area");
    String subject_code1 = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "subject_code1");
    String subject_code2 = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "subject_code2");
    if (StringUtils.isNotBlank(science_area)) {
      CategoryMapBase categoryMapBase = categoryMapBaseDao.getCategoryMapBase(science_area);
      if (categoryMapBase != null) {
        prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "areaId",
            categoryMapBase.getCategryId().toString());
      }
    } else if (StringUtils.isNotBlank(subject_code1) || StringUtils.isNotBlank(subject_code2)) {
      CategoryMapScmNsfc scmNsfc = null;
      if (StringUtils.isNotBlank(subject_code1)) {
        scmNsfc = categoryMapScmNsfcDao.findScmNsfc(subject_code1);
      }
      if (StringUtils.isNotBlank(subject_code1) && scmNsfc == null) {
        scmNsfc = categoryMapScmNsfcDao.findScmNsfc(subject_code2);
      }
      if (scmNsfc != null) {
        CategoryMapBase categoryMapBase =
            categoryMapBaseDao.getCategoryMapBase(Integer.parseInt(scmNsfc.getScmCategoryId().toString()));
        if (categoryMapBase != null) {
          prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "areaId",
              categoryMapBase.getCategryId().toString());
        }
      }

    }
    String file_fulltext_url = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "file_fulltext_url");
    if (StringUtils.isNotBlank(file_fulltext_url)) {
      /// prj_fulltext/@fulltext_url 全文
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "fulltext_url", file_fulltext_url);
    }

    // 处理 mdb 格式的 成员
    List temp_members = prjXmlDoc.getNodes(PrjXmlConstants.PRJ_MEMBERS_XPATH_TEMP);
    if (CollectionUtils.isNotEmpty(temp_members)) {
      prjXmlDoc.removeNode(PrjXmlConstants.PRJ_MEMBERS_XPATH);
      Element newMembers = prjXmlDoc.createElement(PrjXmlConstants.PRJ_MEMBERS_XPATH);
      for (int i = 0; i < temp_members.size(); i++) {
        Node member = (Node) temp_members.get(i);
        Element newAttach = newMembers.addElement("prj_member");
        prjXmlDoc.copyPrjElement(newAttach, (Element) member);
      }
      prjXmlDoc.removeNode(PrjXmlConstants.PRJ_MEMBERS_XPATH_TEMP);
    }

    prjXmlDoc.removeAttribute(PrjXmlConstants.PROJECT_XPATH, "file_fulltext_url", "science_area", "file_import",
        "subject_code1", "subject_code2");
    return this;
  }

  /**
   * 项目xml字段校验
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午4:41:52
   * @return
   */
  public PrjXmlProcessor validate() {

    try {
      Element errors = prjXmlDoc.createElement(PrjXmlConstants.PRJ_ERRORS_XPATH);

      String formTmpl = prjXmlDoc.getFormTemplate();
      List<PrjXmlErrorField> fields = PrjXmlValidator.validate(prjXmlDoc);
      if (CollectionUtils.isNotEmpty(fields)) {
        prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "is_valid", "0");
        fields.forEach(field -> {
          Element error = errors.addElement("error");
          error.addAttribute("field", field.getName());
          error.addAttribute("error_no", String.valueOf(field.getErrorNo()));
        });
      } else {
        prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "is_valid", "1");
      }
      return this;
    } catch (InvalidXpathException e) {
      logger.error("校验项目xml出现异常！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 生成简要描述（页面表格的来源列）.
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午4:54:13
   * @return
   */
  public PrjXmlProcessor generateBrief() {
    try {
      String agencyNameZhTmp = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name");
      String agencyNameEnTmp = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name_en");
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name",
          StringUtils.isNotBlank(agencyNameZhTmp) ? agencyNameZhTmp : agencyNameEnTmp);
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name_en",
          StringUtils.isNotBlank(agencyNameEnTmp) ? agencyNameEnTmp : agencyNameZhTmp);

      String schemeNameZhTmp = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name");
      String schemeNameEnTmp = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name_en");
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name",
          StringUtils.isBlank(schemeNameZhTmp) ? "" : " - " + schemeNameZhTmp);
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name_en",
          StringUtils.isBlank(schemeNameEnTmp) ? "" : " - " + schemeNameEnTmp);
      PrjXmlBriefHandler briefHandler = new PrjXmlBriefHandler();
      Locale locale = LocaleContextHolder.getLocale();
      Map result = briefHandler.getData(locale, prjXmlDoc);
      // 类别从新构建 - 2019-06-27 上面的逻辑有点问题
      if (StringUtils.isNotBlank(agencyNameZhTmp)) {
        result.put("SCHEME_NAME", StringUtils.isBlank(schemeNameZhTmp) ? "" : " - " + schemeNameZhTmp);
      } else {
        result.put("SCHEME_NAME", StringUtils.isBlank(schemeNameZhTmp) ? "" : schemeNameZhTmp);
      }
      if (StringUtils.isNotBlank(agencyNameEnTmp)) {
        result.put("SCHEME_NAME_EN", StringUtils.isBlank(agencyNameEnTmp) ? "" : " - " + agencyNameEnTmp);
      } else {
        result.put("SCHEME_NAME_EN", StringUtils.isBlank(agencyNameEnTmp) ? "" : agencyNameEnTmp);
      }
      String pattern = briefHandler.getPattern();
      PrjBriefFormatter formatter = new PrjBriefFormatter(locale, result);
      String brief = formatter.format(pattern);

      String patternEn = briefHandler.getPatternEn();
      PrjBriefFormatter formatterEn = new PrjBriefFormatter(locale, result);
      String briefEn = formatterEn.format(patternEn);

      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "brief_desc",
          StringUtils.isNotBlank(brief) ? brief : Objects.toString(briefEn));
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "brief_desc_en",
          StringUtils.isNotBlank(briefEn) ? briefEn : Objects.toString(brief));

      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name",
          Objects.toString(agencyNameZhTmp));
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name_en",
          Objects.toString(agencyNameEnTmp));
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name", Objects.toString(schemeNameZhTmp));
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name_en", Objects.toString(schemeNameEnTmp));
      return this;
    } catch (Exception e) {
      logger.error("生成简要描述出错！", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 生成项目哈希散列值
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午5:03:22
   * @return
   */
  public PrjXmlProcessor generateHashCode() {
    String enTitle = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title");
    String zhTitle = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title");

    String zhHash = Objects.toString(PrjXmlHashUtil.buildTitleHashCode(zhTitle), StringUtils.EMPTY);
    String enHash = Objects.toString(PrjXmlHashUtil.buildTitleHashCode(enTitle), StringUtils.EMPTY);
    // 先注释掉：允许单独输入特殊字符
    // if (hash == null && hash2 == null) {
    // throw new XmlProcessStopExecuteException("项目标题Hash不能为空！！！！！");
    // }

    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title_hash", zhHash);
    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title_hash", enHash);
    return this;
  }

  /**
   * 同步更新项目对应字段
   * 
   * @param prj
   */
  public PrjXmlProcessor syncProjectData(Project prj) {
    prj.setVersionNo(prj.getVersionNo() + 1);
    prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "version_no", Objects.toString(prj.getVersionNo()));
    // 项目批准号(本机构)
    prj.setInternalNo(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_internal_no"));
    // 项目批准号（资助机构)
    prj.setExternalNo(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_external_no"));
    // 资金总数
    prj.setAmount(
        IrisNumberUtils.createBigDecimal(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount")));
    // 资金单位
    prj.setAmountUnit(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount_unit"));
    // 参与方式：主导单位1/参与单位0
    prj.setIsPrincipalIns(IrisNumberUtils
        .createInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "is_principal_ins")));
    // 项目状态01进行中/02完成/03其他,04:申请项目
    prj.setState(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_state"));
    // 项目年度
    prj.setFundingYear(
        IrisNumberUtils.createInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "funding_year")));
    // 资助机构名称
    prj.setAgencyName(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name"));
    prj.setEnAgencyName(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name_en"));
    // 资助机构ID
    prj.setAgencyId(
        IrisNumberUtils.createLong(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_id")));
    // 资助类别ID
    prj.setSchemeId(
        IrisNumberUtils.createLong(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_id")));
    // 资助类别名称
    prj.setSchemeName(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name"));
    prj.setEnSchemeName(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name_en"));
    // 项目类型,1：内部项目，0:外部项目
    prj.setType(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_type"));
    // 开始日期
    prj.setStartYear(
        IrisNumberUtils.createInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_year")));
    prj.setStartMonth(
        IrisNumberUtils.monthDayToInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_month")));
    prj.setStartDay(
        IrisNumberUtils.monthDayToInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_day")));
    // 结束日期
    prj.setEndYear(
        IrisNumberUtils.createInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "end_year")));
    prj.setEndMonth(
        IrisNumberUtils.monthDayToInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "end_month")));
    prj.setEndDay(
        IrisNumberUtils.monthDayToInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "end_day")));
    // 依托单位Id
    prj.setInsId(IrisNumberUtils.createLong(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_id")));
    // 依托单位名
    prj.setInsName(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_name"));
    // 数据来源DBID
    prj.setDbId(
        IrisNumberUtils.createInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "source_db_id")));
    // 最后更新时间
    prj.setUpdateDate(new Date());
    // 最后更新人
    prj.setUpdatePsnId(SecurityUtils.getCurrentUserId());
    // 是否校验通过
    prj.setIsValid(
        IrisNumberUtils.createInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "is_valid")));
    // 数据来源
    prj.setRecordFrom(
        IrisNumberUtils.createInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "record_from")));
    // 外文标题
    prj.setEnTitle(
        StringUtils.substring(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title_text"), 0, 250));
    prj.setEnTitleHash(
        IrisNumberUtils.createInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title_hash")));
    // 中文标题
    prj.setZhTitle(
        StringUtils.substring(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title_text"), 0, 250));
    prj.setZhTitleHash(
        IrisNumberUtils.createInteger(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title_hash")));
    // brief
    prj.setBriefDesc(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "brief_desc"));
    prj.setBriefDescEn(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "brief_desc_en"));
    // 全文
    prj.setFulltextFileId(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "file_id"));
    prj.setFulltextExt(null);
    if (prj.getFulltextFileId() != null) {
      String fileExt = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "file_ext");
      if (StringUtils.isNotBlank(fileExt)) {
        prj.setFulltextExt(StringUtils.substring(fileExt, 0, 30));
      }
    }
    prj.setOldPubId(
        IrisNumberUtils.createLong(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "from_prj_id")));

    String fullTextUrl = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "fulltext_url");
    if (StringUtils.length(fullTextUrl) < 3000) {
      prj.setFulltextUrl(fullTextUrl);
    }
    return this;
  }

  /**
   * 同步项目组成员
   * 
   * @param prjMemberService
   * @param prj
   * @throws ServiceException ServiceException
   */
  @SuppressWarnings("unchecked")
  public PrjXmlProcessor syncPrjMember(PrjMemberService prjMemberService, PersonOpenDao personOpenDao, Project prj)
      throws ServiceException {
    try {
      // 有删除的成员先删除
      String[] pmIdStrs = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_MEMBERS_XPATH, "remove_pms").split(",");
      /* Map<String, String> opDetail = new HashMap<String, String>(); */
      Optional.ofNullable(pmIdStrs).ifPresent(idArray -> {
        for (String pmIdStr : idArray) {
          Optional.ofNullable(NumberUtils.parseLong(pmIdStr)).ifPresent(pmId -> {
            Optional<PrjMember> pm = prjMemberService.removeById(pmId);
            /*
             * opDetail.put("author", pm.getName()); opDetail.put("seqNo", pm.getSeqNo().toString());
             * opDetail.put("owner", pm.getOwner().toString()); projectLogService.logOp(prjId, opPsnId, null,
             * ProjectOperationEnum.RemovePrjMember, opDetail);
             */
          });
        }
      });
      List<Node> ndList = prjXmlDoc.getPrjMembers();
      if (CollectionUtils.isEmpty(ndList)) {
        return this;
      }
      String authorNamesZh = "";
      String authorNamesEn = "";
      Set<Long> insSet = new HashSet<Long>();
      for (Node node : ndList) {

        Element em = (Element) node;
        // 获取成员ID，如果存在则查找修改
        PrjMember pm = Optional.ofNullable(IrisNumberUtils.createLong(em.attributeValue("pm_id")))
            // 映射为prjMember
            .flatMap(pmId -> prjMemberService.getPrjMember(pmId))
            // prjMember为空则创建
            .orElseGet(PrjMember::new);

        Long memberPsnId = NumberUtils.toLong(em.attributeValue("member_psn_id"));
        Integer owner = NumberUtils.toInt(em.attributeValue("owner"));
        // 根据openid查找系统中人员的psnId
        Long openId = NumberUtils.parseLong(em.attributeValue("open_id"));
        if (NumberUtils.isNotNullOrZero(openId) && personOpenDao != null) {
          PersonOpen personOpen = personOpenDao.getPersonOpenByOpenId(openId);
          if (personOpen != null) {
            // 设置psnId
            memberPsnId = personOpen.getPsnId();
            if (memberPsnId.equals(prj.getCreatePsnId())) {
              // 如果psnId是当前人的话，设置为拥有者
              owner = 1;
            }
          }
        }
        pm.setPsnId(memberPsnId);
        pm.setOwner(owner);

        // 项目负责人
        pm.setNotifyAuthor(IrisNumberUtils.createInteger(em.attributeValue("notify_author")));
        pm.setEmail(IrisStringUtils.getMaxCharacterLength(em.attributeValue("email"), 50));
        pm.setInsCount(IrisNumberUtils.createInteger(em.attributeValue("ins_count")));
        String zhName = StringUtils.substring(em.attributeValue("member_psn_name"), 0, 50);
        String enName = StringUtils.substring(em.attributeValue("member_psn_name_en"), 0, 50);
        pm.setName(StringUtils.isBlank(zhName) ? enName : zhName);
        pm.setPrjId(prj.getId());


        pm.setSeqNo(IrisNumberUtils.createInteger(em.attributeValue("seq_no")));
        // 单位、成果数据
        for (int i = 1; i < 6; i++) {
          Long insId = IrisNumberUtils.createLong(em.attributeValue("ins_id" + i));
          if (insId != null && insId > 0) {
            insSet.add(insId);
          }
        }
        pm.setInsCount(insSet.size());
        // 重构作者
        authorNamesZh = this.rebuildPrjXmlAuthorNames(authorNamesZh, em, zhName, enName, "zh");
        authorNamesEn = this.rebuildPrjXmlAuthorNames(authorNamesEn, em, zhName, enName, "en");

        // 保存项目成员
        prjMemberService.savePrjMember(pm);
        // 成员ID写入XML
        em.addAttribute("pm_id", pm.getId().toString());
      }

      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names",
          StringUtils.isBlank(authorNamesZh) ? "" : authorNamesZh.substring(1));

      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names_en",
          StringUtils.isBlank(authorNamesEn) ? "" : authorNamesEn.substring(1));

      return this;
    } catch (Exception e) {
      logger.error("同步项目成员出错！ ", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 重构作者信息
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午6:56:06
   * @param author_names
   * @param em
   * @param nameZh
   * @param nameEn
   * @param lang
   * @return
   */
  private String rebuildPrjXmlAuthorNames(String author_names, Element em, String nameZh, String nameEn, String lang) {
    if (StringUtils.isBlank(nameZh) && StringUtils.isBlank(nameEn)) {
      return author_names;
    }
    String authorName = "";
    if ("zh".equalsIgnoreCase(lang)) {
      authorName = StringUtils.isBlank(nameZh) ? nameEn : nameZh;
    } else {
      authorName = StringUtils.isBlank(nameEn) ? nameZh : nameEn;
    }
    if (CommonUtils.compareIntegerValue(IrisNumberUtils.createInteger(em.attributeValue("owner")), 1)) {
      author_names += "; " + String.format("<strong>%s</strong>", authorName); // 是项目负责人，则名称前加*号
    } else {
      author_names += "; " + authorName;
    }
    return author_names;
  }

  /**
   * 作者信息缩略
   *
   * @author houchuanjie
   * @date 2018年3月23日 上午9:42:37
   * @return
   */
  public PrjXmlProcessor abbreviateAuthorNames(Project prj) {
    String zhAuthorNames =
        StringUtils.trimToEmpty(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names"));
    String enAuthorNames =
        StringUtils.trimToEmpty(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names_en"));
    String endStr = "……";
    if ((zhAuthorNames.length() + endStr.length()) > 200) {
      zhAuthorNames = StringUtils.substring(zhAuthorNames, 0, 200);
      zhAuthorNames = StringUtils.substring(zhAuthorNames, 0, zhAuthorNames.lastIndexOf(";"));
      zhAuthorNames = zhAuthorNames.toString() + endStr;
    }
    if ((enAuthorNames.length() + endStr.length()) > 200) {
      enAuthorNames = StringUtils.substring(enAuthorNames, 0, 200);
      enAuthorNames = StringUtils.substring(enAuthorNames, 0, enAuthorNames.lastIndexOf(";"));
      enAuthorNames = enAuthorNames.toString() + endStr;
    }
    prj.setAuthorNames(zhAuthorNames);
    prj.setAuthorNamesEn(enAuthorNames);
    return this;
  }

  /**
   * 拆分xml中的作者信息重建prj_members节点（仅在导入项目过程中使用）.
   * 
   * @return
   */
  public PrjXmlProcessor splitImportAuthorName(PrjXmlProcessContext context) {
    if (PrjXmlOperationEnum.Import.equals(context.getCurrentAction()) && !"refresh".equals(context.getDupOperation())) {
      // 删除prj_members节点
      prjXmlDoc.removeNode(PrjXmlConstants.PRJ_MEMBERS_XPATH);
      Element prjMembers = prjXmlDoc.createElement(PrjXmlConstants.PRJ_MEMBERS_XPATH);
      // 项目负责人
      String notifyName = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "notify_author_name");
      // 资助机构 香港创新及科技基金
      String dbid =
          StringUtils.trimToEmpty(prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "source_db_id"));
      // 创建新的prj_member节点
      this.buildPrjMember(prjMembers, dbid, notifyName);
      String authorNames = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names");
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "original_author_names", authorNames);
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "original_author_names_en", authorNames);
    }
    return this;
  }


  /**
   * 解析抓取数据project下面用户名称数据,创建新的prj_member节点.
   * 
   * @param prjMembers
   * @return
   */
  public void buildPrjMember(Element prjMembers, String dbid, String notifyName) {
    String authorNamesStr = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names");
    String[] authorNames =
        StringUtils.isNotBlank(authorNamesStr) ? StringUtils.stripAll(authorNamesStr.split("，|；|;| and ")) : null;
    if (authorNames != null) {
      int seqNo = 0;
      for (String authorName : authorNames) {
        Element prjMember = prjMembers.addElement("prj_member");
        prjMember.addAttribute("seq_no", String.valueOf(seqNo++));
        prjMember.addAttribute("import_member_name", authorName);
        if (!"7".equals(dbid)) {
          prjMember.addAttribute("member_psn_name", authorName);
        }
        prjMember.addAttribute("member_psn_name_en", authorName);
        if (StringUtils.isNotBlank(authorName) && notifyName.equals(authorName)) {
          prjMember.addAttribute("notify_author", "1");
        }
      }
    }
  }



}
