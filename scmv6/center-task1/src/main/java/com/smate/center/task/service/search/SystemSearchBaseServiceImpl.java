package com.smate.center.task.service.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pub.seo.PubIndexFirstLevel;
import com.smate.center.task.model.search.UserSearchResultForm;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.base.utils.string.ServiceUtil;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * 站外检索基础服务类<构建HTML页面正文内容并写入文件>.
 * 
 * @author mjg
 * 
 */
@Service("systemSearchBaseService")
@Transactional(rollbackFor = Exception.class)
class SystemSearchBaseServiceImpl implements SystemSearchBaseService {

  private static Logger logger = LoggerFactory.getLogger(SystemSearchBaseServiceImpl.class);
  @Autowired
  private Configuration pubFreemarkerConfiguration;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;

  /**
   * 获取26个英文字母列表.
   * 
   * @return
   */
  @Override
  public List<String> getCodeList() {
    List<String> codeList = new ArrayList<String>();
    char A = '\101';
    for (int r = 0; r < 26; r++) {
      codeList.add(String.valueOf(A));
      A += 1;
    }
    codeList.add("other");
    return codeList;
  }

  @Override
  public void buildPubPageCon(String fileRoot, String code, List<PubIndexFirstLevel> pubList) {
    try {
      String url = domainscm + "/indexhtml/[code][group]-[language].html";
      List<String> letters = this.getCodeList();
      Map<String, Object> map = new HashMap<String, Object>();
      StringBuilder divStr = new StringBuilder("<ul>");
      if (CollectionUtils.isNotEmpty(pubList)) {
        for (int i = 0; i < pubList.size(); i++) {
          PubIndexFirstLevel pub = pubList.get(i);
          String title = pub.getFirstLabel();
          // 单独处理 < >
          title = title.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
          try {
            // 链接地址(访问基准库中成果).
            String href = url.replace("[code]", code).replace("[group]", pub.getSecondGroup().toString());
            // 链接标签.
            String aStr = "<a href=\"" + href + "\" title=\"" + StringUtils.replace(title, "\"", "")
            // + "\" class=\"Blue\"
            // target=\"_blank\">" +
            // title + "</a>";
                + "\" class=\"Blue\"> " + title + "</a>";
            divStr.append("<li>" + aStr + "</li>");
          } catch (Exception e) {
            continue;
          }
        }
      }
      divStr.append("</ul>");
      map.put("currentLetter", code);
      map.put("letterList", letters);
      // 将结果数据写入静态文件.
      try {
        map.put("content", StringUtils.replace(divStr.toString(), "[language]", "zh_cn"));
        this.writeHtmlCon(fileRoot, "pub_" + code + "_zh_CN.html", this.getHtmlCon(PUBLICATION_INDEX_ZH_FTL, map));
        map.put("content", StringUtils.replace(divStr.toString(), "[language]", "en_us"));
        this.writeHtmlCon(fileRoot, "pub_" + code + "_en_US.html", this.getHtmlCon(PUBLICATION_INDEX_EN_FTL, map));
      } catch (ServiceException e) {
        logger.error("写入站外检索成果文件出错,出错类别：" + code, e);
      }
    } catch (Exception e) {
      logger.error("将成果分层数据构建为html文件", e);
    }
  }

  /**
   * 构建站外检索人员内容.
   * 
   * @return
   */
  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void buildPsnPageCon(String fileRoot, String locale, Map<String, List<UserSearchResultForm>> userMap) {
    Map<String, Object> zhMap = MapBuilder.getInstance().getMap();
    Map<String, Object> enMap = MapBuilder.getInstance().getMap();
    if (userMap != null && userMap.size() > 0) {
      Set set = userMap.keySet();
      Iterator iterator = set.iterator();
      while (iterator.hasNext()) {
        StringBuilder divZhStr = new StringBuilder();
        StringBuilder divEnStr = new StringBuilder();
        String key = (String) iterator.next();
        divZhStr.append("<ul>");
        divEnStr.append("<ul>");
        List<UserSearchResultForm> userInfo = userMap.get(key);
        if (CollectionUtils.isNotEmpty(userInfo)) {
          for (int i = 0; i < userInfo.size(); i++) {
            UserSearchResultForm iuser = userInfo.get(i);
            Long psnId = iuser.getPsnId();
            String entitle = (StringUtils.isNotBlank(iuser.getEnInfo())) ? iuser.getEnInfo() : iuser.getZhInfo();
            String zhtitle = (StringUtils.isNotBlank(iuser.getZhInfo())) ? iuser.getZhInfo() : iuser.getEnInfo();
            String des3PsnId = ServiceUtil.encodeToDes3(psnId.toString());
            try {
              // 链接地址(访问基准库中成果).
              String params = "des3PsnId=" + java.net.URLEncoder.encode(des3PsnId, "UTF-8") + "&des3CpsnId=";
              String hrefStr = "";
              PsnProfileUrl profileUrl = psnProfileUrlDao.get(psnId);
              if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
                hrefStr = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl();
              } else {
                hrefStr = domainscm + "/psnweb/outside/homepage?" + params;
              }
              // 链接标签.
              String aZhStr = "<a href=\"" + hrefStr + "\" title=\"" + zhtitle + "\" class=\"Blue\" target=\"_blank\">"
                  + zhtitle + "</a>";
              String aEnStr = "<a href=\"" + hrefStr + "\" title=\"" + entitle + "\" class=\"Blue\" target=\"_blank\">"
                  + entitle + "</a>";
              divZhStr.append("<li>" + aZhStr + "</li>");
              divEnStr.append("<li>" + aEnStr + "</li>");
            } catch (Exception e) {
              continue;
            }
          }
        }
        divZhStr.append("</ul>");
        divEnStr.append("</ul>");
        zhMap.put(key, divZhStr.toString());
        enMap.put(key, divEnStr.toString());
      }
      // 将结果数据写入静态文件.
      try {
        String pubZhTempName = PSN_CON_PAGE_NAME + "_" + LOCALE_ZH + TEMP_SUFFIX_NAME;
        writeHtmlCon(fileRoot, PSN_CON_PAGE_NAME + "_" + LOCALE_ZH + HTML_SUFFIX_NAME,
            getHtmlCon(pubZhTempName, zhMap));
        String pubEnTempName = PSN_CON_PAGE_NAME + "_" + LOCALE_EN + TEMP_SUFFIX_NAME;
        writeHtmlCon(fileRoot, PSN_CON_PAGE_NAME + "_" + LOCALE_EN + HTML_SUFFIX_NAME,
            getHtmlCon(pubEnTempName, enMap));
      } catch (ServiceException e) {
        logger.error("写入站外检索成果文件出错", e);
      }
    }
  }

  /**
   * 将结果内容写入HTML文件.
   * 
   * @param fileName
   * @param content
   */
  private void writeHtmlCon(String fileRoot, String fileName, String content) {
    try {
      String filePath = fileRoot + "/" + fileName;
      File file = new File(filePath);
      // 目录是否存在
      File parentFile = file.getParentFile();
      if (!parentFile.exists()) {
        parentFile.mkdirs();
      }
      FileUtils.writeStringToFile(file, content, "UTF-8");
    } catch (Exception e) {
      logger.error("写入结果文件出错", e);
    }
  }

  /**
   * 构建静态页面内容.
   * 
   * @param map
   * @return
   * @throws ServiceException
   */
  private String getHtmlCon(String templateName, Map<String, Object> map) throws ServiceException {
    String msg = "";
    try {
      msg = FreeMarkerTemplateUtils
          .processTemplateIntoString(pubFreemarkerConfiguration.getTemplate(templateName, ENCODING), map);
    } catch (IOException e) {
      logger.error("构建静态页面内容失败，没有找到对应的静态页面内容模板！", e);
    } catch (TemplateException e) {
      logger.error("构造静态页面内容失败,FreeMarker处理失败", e);
    }
    return msg;
  }

  public static void main(String[] args) {
    System.out.println(HtmlUtils.htmlEscape("<i>nihao"));
  }

}
