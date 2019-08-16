package com.smate.center.batch.service.pub;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.dao.sns.pub.PublicationListDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PdwhPublicationAll;
import com.smate.center.batch.model.sns.pub.ConstPubType;
import com.smate.center.batch.model.sns.pub.ErrorField;
import com.smate.center.batch.model.sns.pub.HtmlBuilder;
import com.smate.center.batch.model.sns.pub.HtmlCellConfig;
import com.smate.center.batch.model.sns.pub.PubErrorFields;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * Publication html wrap.
 * 
 * @author Ly
 * 
 */
@Service("publicationWrapService")
@Transactional(rollbackFor = Exception.class)
public class PublicationWrapServiceImpl implements PublicationWrapService {
  private static Logger logger = LoggerFactory.getLogger(PublicationWrapServiceImpl.class);
  private HtmlCellConfig htmlCellConfig = null;
  @Autowired
  private PublicationListDao publicationListDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private ConstPubTypeService publicationTypeService;



  @Override
  public void wrapPopulateDataItems(Publication item, boolean isFillErrorField, boolean isViewUploadFulltext,
      String des3ResSendId, String des3ResRecId) {

    Locale locale = LocaleContextHolder.getLocale();
    this.wrapPopulateDataItems(item, isFillErrorField, locale, isViewUploadFulltext, des3ResSendId, des3ResRecId);
  }

  @Override
  public void wrapPopulateDataItems(Publication item, boolean isFillErrorField, Locale locale,
      boolean isViewUploadFulltext, String des3ResSendId, String des3ResRecId) {
    try {
      // 引用情况
      if (item.getTypeId() == 4 || item.getTypeId() == 3 || item.getTypeId() == 7) {
        PublicationList pubList = this.publicationListDao.get(item.getId());
        if (pubList != null) {
          String list = "";
          if (pubList.getListEi() != null && pubList.getListEi().intValue() == 1) {
            list += ",EI";
          }
          if (pubList.getListSci() != null && pubList.getListSci().intValue() == 1) {
            list += ",SCI";
          }
          if (pubList.getListIstp() != null && pubList.getListIstp().intValue() == 1) {
            list += ",ISTP";
          }
          if (pubList.getListSsci() != null && pubList.getListSsci().intValue() == 1) {
            list += ",SSCI";
          }
          if (list.length() > 1) {
            list = list.substring(1);
            String[] lists = list.split(",");
            if (lists.length > 2) {
              list = "";
              for (int i = 0; i < lists.length; i++) {
                if ("".equals(list)) {
                  list = lists[i];
                } else {
                  list += "," + lists[i];
                }
              }
            }
            item.setListInfo(list);

          }

        }
      }
      String html = this.buildHtmlAbstract(item, locale, isViewUploadFulltext, des3ResSendId, des3ResRecId);
      item.setHtmlAbstract(html);
      // 是否需要获取错误信息.
      if (isFillErrorField) {
        List<PubErrorFields> erFields = this.publicationDao.getPubErrorFields(item.getId());
        if (CollectionUtils.isNotEmpty(erFields)) {
          Set<ErrorField> erFieldsSet = new HashSet<ErrorField>();
          for (PubErrorFields errorField : erFields) {
            erFieldsSet.add(new ErrorField(errorField.getName(), errorField.getErrorNo()));
          }
          item.setErrorFields(erFieldsSet);
        }
      }
    } catch (Exception e) {
      logger.error(String.format("populateDataFromXml通过成果xml构造Html内容错误。pubId=%s", item.getId()), e);
    }
  }


  @Override
  public String buildHtmlAbstract(Publication item, Locale locale, boolean isViewUploadFulltext, String des3ResSendId,
      String des3ResRecId) {
    HtmlBuilder html = new HtmlBuilder();
    htmlCellConfig = new HtmlCellConfig();
    String title = "";
    if ("zh".equalsIgnoreCase(locale.getLanguage())) {
      title = item.getZhTitle();
      if (StringUtils.isBlank(title)) {
        title = item.getEnTitle();
      }
    } else if ("en".equalsIgnoreCase(locale.getLanguage())) {
      title = item.getEnTitle();
      if (StringUtils.isBlank(title)) {
        title = item.getZhTitle();
      }
    } else {
      title = item.getZhTitle();
    }
    int inden = 0;
    // 第一行：作者名
    if (!StringUtils.isBlank(item.getAuthorNames())) {
      html.p().style("word-wrap:break-word; word-break:break-all;").close();
      html.append(XmlUtil.formateSymbolAuthors(title, item.getAuthorNames()));
      html.pEnd();
      inden = 1;
    }
    /*
     * span 换成p 下面的br去掉 if (inden > 0 && title != null && !title.startsWith("<P>")) { html.br(); }
     */
    // 第二行：成果标题
    html.span().id("title_" + item.getId()).styleClass("pubGridTitle").style("word-break:break-word;cursor:pointer;")
        .close();
    if (htmlCellConfig.isTitleWithLink()) {
      html.span().styleClass("notPrintLinkSpan_title").style("cursor:pointer")
          .onclick("javascript:ScholarView.viewPubDetail('" + item.getDes3Id() + ","
              + SecurityUtils.getCurrentAllNodeId().get(0).intValue() + "',this)")
          .close();
      html.append("<font color=\"#005eac\">");
      html.append(title);
      html.append("</font>");
      html.spanEnd();
    } else {
      html.append(title);
    }
    html.spanEnd();

    html.nbsp();
    inden++;
    // 第三行：来源
    String briefDesc = "";
    if (locale.equals(Locale.US)) {
      briefDesc = StringUtils.isBlank(item.getBriefDescEn()) ? item.getBriefDesc() : item.getBriefDescEn();
    } else {
      briefDesc = item.getBriefDesc();
    }
    if (StringUtils.isNotBlank(briefDesc)) {
      briefDesc = briefDesc.replace(">", "&gt;").replace("<", "&lt;");
      if (StringUtils.isNotBlank(title) && inden > 0 && !title.endsWith("</P>")) {
        html.br();
      }
      html.span().styleClass("maintBriefDesc").close();
      // Cnkipat该库的专利号格式如： CN201030049402.3. 所以不能对其进行标点符号格式化
      if (null != item.getSourceDbId() && item.getSourceDbId().equals(21)) {// scm-5569 标点问题修改
        html.append(briefDesc);
      } else {
        html.append(XmlUtil.formateSymbol(title, briefDesc));
      }
      html.spanEnd();
      inden++;
    }

    if (StringUtils.isBlank(item.getBriefDesc()) && StringUtils.isNotBlank(title) && inden > 0
        && !title.endsWith("</P>")) {
      html.br();
    }
    html.nbsp();
    html.span().style("white-space:nowrap;");
    html.close();
    // html.append(StringUtils.trimToEmpty(item.getTypeName()));
    // html.append(StringUtils.isBlank(item.getListInfo()) ? "" :
    // XmlUtil.formateSymbol(title, "," + item.getListInfo()).replace(",",
    // ", "));
    // html.append(StringUtils.isBlank(item.getImpactFactors()) ? "" :
    // XmlUtil.formateSymbol(title, "," +
    // item.getImpactFactors()).replace(",", ", ").replace("。", ". "));
    html.spanEnd();
    html.nbsp();
    // 全文按钮
    if (htmlCellConfig.isShowFulltextButton()) {
      html.span().id("span_fulltext_" + item.getId()).styleClass("notPrintLinkSpan")
          .style("cursor:pointer;white-space:nowrap;");
      if (NumberUtils.isDigits(item.getFulltextFileid())) {
        try {
          Integer nodeId = null;
          if (item.getFulltextNodeId() != null) {
            nodeId = item.getFulltextNodeId();
          } else {
            nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
          }
          html.append("restype=" + item.getArticleType() + " resnode=" + nodeId + " resid=" + item.getId() + " groupId="
              + item.getGroupId() + " des3ResSendId=\"" + (des3ResSendId == null ? "" : des3ResSendId)
              + "\" des3ResRecId=\"" + (des3ResRecId == null ? "" : des3ResRecId) + "\"");
        } catch (Exception e) {
          logger.error(e.getMessage() + e);
        }
      }
      html.close();
      if (StringUtils.isNotBlank(item.getFulltextFileid())) {
        String fileType = item.getFulltextExt();
        String fulltextIcon = ArchiveFileUtil.getFileTypeIco("/resmod", fileType, locale);
        html.img().src(fulltextIcon).style("padding-left:1px;");
        html.xclose();
      } else if (isViewUploadFulltext) {
        html.a("javascript:void(0)").id("fulltext_" + item.getId()).alt(ObjectUtils.toString(item.getNodeId()))
            .styleClass("green upload_fulltext").close();
        if ("zh".equalsIgnoreCase(locale.getLanguage())) {
          html.append("【上传全文】");
        } else {
          html.append("[ Upload Full-text ]");
        }
        html.aEnd();
      }
      // SCM-5053_zk_2014_04_23
      // html.nbsp();
      html.spanEnd();
      html.nbsp();
    }
    return html.toString();
  }

  /**
   * 加载查询结果集的类别名称.
   * 
   * @param page
   * @throws ServiceException
   */
  @Override
  public void wrapQueryResultTypeName(Publication pe) throws ServiceException {

    ConstPubType type = publicationTypeService.get(pe.getTypeId());
    pe.setTypeName(type.getName());
  }


  @Override
  public String buildHtmlAbstract(PdwhPublicationAll item) throws ServiceException {
    Locale locale = LocaleContextHolder.getLocale();
    HtmlBuilder html = new HtmlBuilder();
    htmlCellConfig = new HtmlCellConfig();
    String title = "";
    if ("zh".equalsIgnoreCase(locale.getLanguage())) {
      title = item.getZhTitle();
      if (StringUtils.isBlank(title)) {
        title = item.getEnTitle();
      }
    } else if ("en".equalsIgnoreCase(locale.getLanguage())) {
      title = item.getEnTitle();
      if (StringUtils.isBlank(title)) {
        title = item.getZhTitle();
      }
    } else {
      title = item.getZhTitle();
    }
    int inden = 0;
    // 第一行：作者名
    if (!StringUtils.isBlank(item.getAuthorNames())) {
      html.p().style("word-wrap:break-word; word-break:break-all;").close();
      html.append(XmlUtil.formateSymbolAuthors(title, item.getAuthorNames()));
      html.pEnd();
      inden = 1;
    }
    /*
     * span 换成p 下面的br去掉 if (inden > 0 && title != null && !title.startsWith("<P>")) { html.br(); }
     */
    // 第二行：成果标题
    html.span().id("title_" + item.getId()).styleClass("pubGridTitle").style("word-break:break-word;cursor:pointer;")
        .close();
    if (htmlCellConfig.isTitleWithLink()) {
      html.span().styleClass("notPrintLinkSpan_title").style("cursor:pointer")
          .onclick("javascript:ScholarView.viewPdwhPubDetail('" + item.getDes3Id() + "'," + item.getDbid() + ")")
          .close();
      html.append("<font color=\"#005eac\">");
      html.append(title);
      html.append("</font>");
      html.spanEnd();
    } else {
      html.append(title);
    }
    html.spanEnd();
    html.nbsp();
    inden++;
    // 第三行：来源
    String briefDesc = "";
    if (locale.equals(Locale.US)) {
      briefDesc = StringUtils.isBlank(item.getBriefDescEn()) ? item.getBriefDescZh() : item.getBriefDescEn();
    } else {
      briefDesc = item.getBriefDescZh();
    }
    briefDesc = XmlUtil.replacePdwhAllPubType(briefDesc);
    if (StringUtils.isNotBlank(briefDesc)) {
      briefDesc = briefDesc.replace(">", "&gt;").replace("<", "&lt;");
      if (StringUtils.isNotBlank(title) && inden > 0 && !title.endsWith("</P>")) {
        html.br();
      }
      html.span().styleClass("maintBriefDesc").close();
      html.append(XmlUtil.formateSymbol(title, briefDesc));
      // html.append(StringUtils.isBlank(item.getListInfo()) ? "" :
      // XmlUtil.formateSymbol(title, "," +
      // item.getListInfo()).replace(",", ", "));
      // html.append(StringUtils.isBlank(item.getImpactFactors()) ? "" :
      // XmlUtil.formateSymbol(title, "," +
      // item.getImpactFactors()).replace(",", ", ").replace("。", ". "));
      html.spanEnd();
      inden++;
    }
    if (StringUtils.isBlank(item.getBriefDescZh()) && StringUtils.isNotBlank(title) && inden > 0
        && !title.endsWith("</P>")) {
      html.br();
    }
    html.nbsp();
    return html.toString();
  }



}
