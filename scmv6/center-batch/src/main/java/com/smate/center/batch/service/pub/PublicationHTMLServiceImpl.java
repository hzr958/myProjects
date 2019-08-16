package com.smate.center.batch.service.pub;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.center.batch.constant.TemplateConstants;
import com.smate.center.batch.service.pub.mq.DynRecommendThesisForm;
import com.smate.core.base.utils.data.XmlUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 生成成果HTML内容服务<用模版构建的方式生成HTML以便解除后台逻辑代码和前台JS事件的绑定>_SCM-5988.
 * 
 * @author mjg
 * 
 */
@Service("publicationHTMLService")
@Transactional(rollbackFor = Exception.class)
public class PublicationHTMLServiceImpl implements PublicationHTMLService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private Configuration pubFreemarkerConfiguration;

  /**
   * 构建无全文的成果显示内容.
   * 
   * @param form
   * @return
   * @throws IOException
   */
  @Override
  public String buildPubShowCellNoFull(DynRecommendThesisForm pubForm) throws Exception {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put("pub_id", pubForm.getPubId());
    paramMap.put("des3_pub_id", pubForm.getDes3Id());
    paramMap.put("db_id", pubForm.getDbId());
    paramMap.put("authorNames", pubForm.getAuthorNames());
    // 标题.
    String pubTitle = null;
    Locale locale = LocaleContextHolder.getLocale();
    // 默认为zh
    String localeString = StringUtils.isNotEmpty(locale.toString()) ? locale.toString() : "zh_CN";

    if ("zh".equalsIgnoreCase(locale.getLanguage())) {
      pubTitle = pubForm.getZhTitle();
      if (StringUtils.isBlank(pubTitle)) {
        pubTitle = pubForm.getEnTitle();
      }
    } else if ("en".equalsIgnoreCase(locale.getLanguage())) {
      pubTitle = pubForm.getEnTitle();
      if (StringUtils.isBlank(pubTitle)) {
        pubTitle = pubForm.getZhTitle();
      }
    } else {
      pubTitle = pubForm.getZhTitle();
    }
    paramMap.put("pub_title", pubTitle);
    // 摘要.
    String pubBrief = null;
    if (locale.equals(Locale.US)) {
      pubBrief = StringUtils.isBlank(pubForm.getEnBriefDesc()) ? pubForm.getZhBriefDesc() : pubForm.getEnBriefDesc();
    } else {
      pubBrief = pubForm.getZhBriefDesc();
    }
    pubBrief = XmlUtil.replacePdwhAllPubType(pubBrief);
    paramMap.put("pub_brief_desc", pubBrief);

    Template template = pubFreemarkerConfiguration.getTemplate(
        TemplateConstants.DYN_TEMP_RECOMM_PUB_SHOW + "_" + localeString + ".ftl", TemplateConstants.ENCODING);
    return FreeMarkerTemplateUtils.processTemplateIntoString(template, paramMap);
  }

}
