package com.smate.center.batch.service.pub;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.smate.center.batch.model.sns.pub.HtmlBuilder;
import com.smate.center.batch.model.sns.pub.HtmlCellConfig;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.InvalidXpathException;
import com.smate.core.base.utils.exception.PubHtmlCellContentBuildException;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 通过Xml构建成果页面表格Cell的内容.
 * 
 * @author yamingd
 */
public class ScholarPubHtmlCellContentBuilder implements IPubHtmlCellContentBuilder {

  /**
   * 数据字段.
   */
  private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ScholarPubHtmlCellContentBuilder.class);
  private static List<String> fields = new ArrayList<String>();
  static {
    fields.add("/publication/@author_names");
    fields.add("/publication/@brief_desc");
    fields.add("/publication/@brief_desc_en");
    fields.add("/publication/@zh_title");
    fields.add("/publication/@en_title");
    fields.add("/pub_meta/@source_url");
    fields.add("/pub_fulltext/@fulltext_url");
    fields.add("/pub_fulltext/@file_id");
    fields.add("/publication/@cited_url");
  }

  private HtmlCellConfig htmlCellConfig = null;

  private String webappContext;
  private String resappContext;
  private String domain;
  @Value("${domainscm}")
  private String domainscm;


  public ScholarPubHtmlCellContentBuilder(HtmlCellConfig htmlCellConfig) {

    Assert.notNull(htmlCellConfig);
    this.htmlCellConfig = htmlCellConfig;
  }

  public String getImageAbslouteUrl(String imageName) {
    return "https://" + domain + this.getResappContext() + imageName;
  }

  public String build(Locale locale, PubXmlDocument xmlDocument, Integer citedTimes, Date citedDate,
      String impactFactors) throws PubHtmlCellContentBuildException {
    if (xmlDocument == null) {
      throw new NullPointerException("xmlDocument参数不能为空。");
    }
    if (locale == null) {
      throw new NullPointerException("locale参数不能为空。");
    }
    if (this.getHtmlCellConfig() == null) {
      throw new NullPointerException("cellConfig参数不能为空。");
    }

    Assert.notNull(this.getWebappContext());
    Assert.notNull(this.getResappContext());

    Map<String, String> data;
    String pubId = String.valueOf(xmlDocument.getPubId());

    try {
      data = xmlDocument.getFieldsData(getXmlFields());

      String des3PubId = ServiceUtil.encodeToDes3(pubId);
      String authorNames = data.get("/publication/@author_names");
      String zhTitle = data.get("/publication/@zh_title");
      String enTitle = data.get("/publication/@en_title");
      String briefDesc = data.get("/publication/@brief_desc");
      String briefDescEn = data.get("/publication/@brief_desc_en");
      String sourceUrl = data.get("/pub_meta/@source_url");
      String fulltextUrl = data.get("/pub_fulltext/@fulltext_url");
      String fileId = data.get("/pub_fulltext/@file_id");
      String citedUrl = data.get("/publication/@cited_url");
      String title = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhTitle, enTitle);

      HtmlBuilder html = new HtmlBuilder();
      SimpleDateFormat dateFormatter = new SimpleDateFormat(htmlCellConfig.getCiteDatePattern());

      int inden = 0;
      // 第一行：作者名
      if (!StringUtils.isBlank(authorNames)) {
        html.span().style("word-break:break-word;").close();
        html.append(authorNames);
        html.spanEnd();
        inden = 1;
      }
      if (inden > 0 && !title.startsWith("<P>")) {
        html.br();
      }
      // 第二行：成果标题
      html.span().id("title_" + pubId).styleClass("pubGridTitle").style("word-break:break-word;cursor:pointer;")
          .close();
      if (htmlCellConfig.isTitleWithLink()) {
        html.span().styleClass("notPrintLinkSpan").style("cursor:hand").onclick("javascript:ScholarView.viewPubDetail('"
            + des3PubId + "," + SecurityUtils.getCurrentAllNodeId().get(0).intValue() + "',this)").close();

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
      if (locale.equals(Locale.US))
        briefDesc = StringUtils.isBlank(briefDescEn) ? briefDesc : briefDescEn;
      if (!StringUtils.isBlank(briefDesc)) {
        briefDesc = briefDesc.replace(">", "&gt;").replace("<", "&lt;");

        if (inden > 0 && !title.endsWith("</P>")) {
          html.br();
        }
        html.span().close();
        html.append(briefDesc);
        html.spanEnd();
        inden++;
      }
      // 第四行：引用次数、影响因子、来源按钮、全文按钮
      boolean showCiteTimes = false;
      // 引用次数
      if (htmlCellConfig.isShowCitedTimes() && citedTimes != null && citedTimes > 0) {
        showCiteTimes = true;
        String date = citedDate != null ? dateFormatter.format(citedDate) : "";
        String view = "<a href=\"#\" onclick='ScholarView.openCitesUrl(\"%s\")'>%s (%s)</a>";

        if (citedTimes == 0 || StringUtils.isBlank(citedUrl)) {
          view = String.format("%s (%s)", citedTimes, date);
        } else {
          view = String.format(view, des3PubId, citedTimes, date);
        }
        if (inden > 0) {
          html.br();
        }

        html.span().close();
        html.append(view);
        html.spanEnd();
      }
      // 影响因子
      if (htmlCellConfig.isShowImpactFactors() && !StringUtils.isBlank(impactFactors)) {
        if (!showCiteTimes && inden > 0) {
          html.br();
        }
        String label = "Impact Factor: ";
        if ("zh".equalsIgnoreCase(locale.getLanguage())) {
          label = "影响因子：";
        }
        /*
         * (Impact Factor: [@factor@]) or (影响因子：[@factor@])
         */
        html.nbsp().append(String.format("(%s%s)", label, impactFactors));
      }
      // 全文按钮
      if (htmlCellConfig.isShowFulltextButton()
          && (StringUtils.isNotBlank(fulltextUrl) || StringUtils.isNotBlank(fileId))) {
        html.span().styleClass("notPrintLinkSpan").style("cursor:pointer;");
        if (StringUtils.isNotBlank(fileId)) {
          try {
            Integer nodeId = IrisNumberUtils
                .createInteger(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "node_id"));
            Long insId = IrisNumberUtils
                .createLong(xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_FULLTEXT_XPATH, "ins_id"));
            String url = domainscm + "/scmwebsns";
            String link = ArchiveFileUtil.generateDownloadLink(url, Long.parseLong(fileId), nodeId, insId);
            html.append("onclick=\"javascript:window.open('" + link + "')\"");

          } catch (Exception e) {
            throw new PubHtmlCellContentBuildException(Long.parseLong(pubId), e);
          }
        } else if (StringUtils.isNotBlank(fulltextUrl)) {
          html.append("onclick=\"javascript:ScholarView.openFullTextUrl('" + des3PubId + "')\"");
        }
        html.close();

        if (StringUtils.isNotBlank(fileId)) {
          String fulltextIcon = getImageAbslouteUrl("/images/pub_ftxt_file_icon.gif");
          String fulltextHint = "";
          html.img(fulltextIcon, fulltextHint);
        } else if (StringUtils.isNotBlank(fulltextUrl)) {

          String fulltextIcon = getImageAbslouteUrl("/images/pub_ftxt_icon.jpg");
          String fulltextHint = "";
          html.img(fulltextIcon, fulltextHint);
        }

        html.nbsp();

        html.spanEnd();
        html.nbsp();
      }

      // 来源按钮
      // if (htmlCellConfig.isShowSourceUrlButton() &&
      // StringUtils.isNotBlank(sourceUrl)) {
      // html.nbsp().nbsp();
      // html.span().styleClass("notPrintLinkSpan").style("cursor:pointer;").append(
      // "onclick=\"javascript:ScholarView.openSourceUrl('" + des3PubId +
      // "')\"").close();
      //
      // String sourceIcon = getImageAbslouteUrl(currentDb,
      // "/images/pub_src_icon_" + locale.getLanguage()
      // + ".jpg");
      //
      // html.img(sourceIcon);
      //
      // html.spanEnd().nbsp();
      // }

      return html.toString();

    } catch (InvalidXpathException e1) {
      throw new PubHtmlCellContentBuildException(Long.parseLong(pubId), e1);
    }
  }

  @Override
  public List<String> getXmlFields() {
    return fields;
  }

  @Override
  public HtmlCellConfig getHtmlCellConfig() {
    return this.htmlCellConfig;
  }

  /**
   * @param cellConfig the cellConfig to set
   */
  public void setHtmlCellConfig(HtmlCellConfig cellConfig) {
    this.htmlCellConfig = cellConfig;
  }

  @Override
  public int getCurrentNodeId() {
    return SecurityUtils.getCurrentUserNodeId();
  }

  @Override
  public String getResappContext() {
    return this.resappContext;
  }

  public void setResappContext(String resappContext) {
    this.resappContext = resappContext;
  }

  @Override
  public String getWebappContext() {
    return this.webappContext;
  }

  public void setWebappContext(String webappContext) {
    this.webappContext = webappContext;
  }


}
