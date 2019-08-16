package com.smate.web.v8pub.service.sns;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.v8pub.dao.seo.PubSeoSecondLevelSerachDao;
import com.smate.web.v8pub.dao.seo.PubSeoThirdLevelSerachDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.seo.PubIndexThirdLevel;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Service("systemSeoSearch")
@Transactional(rollbackFor = Exception.class)
public class SystemSeoSearchImpl implements SystemSeoSearch {
  static final String ENCODING = "utf-8";
  static final String PUBLICATION_INDEX_ZH_FTL = "publication_index_zh_CN.ftl";
  static final String PUBLICATION_INDEX_EN_FTL = "publication_index_en_US.ftl";
  @Value("${indexfile.root}")
  private String fileRoot;// 获取文件根路经
  @Value("${domainscm}")
  private String domainscm;
  private static final long serialVersionUID = 3523616844712760937L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSeoSecondLevelSerachDao pubSeoSecondLevelSerachDao;
  @Autowired
  private Configuration pubFreemarkerConfiguration;

  @Autowired
  private PubSeoThirdLevelSerachDao pubSeoThirdLevelSerachDao;

  /**
   * 获取26个英文字母列表.
   * 
   * @return
   */
  private List<String> getCodeList() {
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
  public void putFilterAndbuildPage(String code, Integer secondGroup) {
    List<String> pubTitleList = pubSeoSecondLevelSerachDao.getSecondLevelList(code, secondGroup);
    // 构建站外检索成果内容.
    try {
      String url = domainscm + "/indexhtml/[code][group]-[language]-[page]-[pageSize].html";
      Map<String, Object> map = new HashMap<String, Object>();
      List<String> letters = this.getCodeList();
      StringBuilder divStr = new StringBuilder("<ul>");
      for (int i = 1; i <= pubTitleList.size(); i++) {
        try {
          // 链接地址(访问基准库中成果).
          String href = url.replace("[code]", code).replace("[group]", secondGroup.toString())
              .replace("[page]", String.valueOf(i)).replace("[pageSize]", pubTitleList.size() + "");
          // 链接标签.
          String aStr = "<a href=\"" + href + "\" title=\"" + StringUtils.replace(pubTitleList.get(i - 1), "\"", "")
          // + "\" class=\"Blue\" target=\"_blank\">" + title
          // +
          // "</a>";
              + "\" class=\"Blue\"> " + pubTitleList.get(i - 1) + "</a>";
          divStr.append("<li>" + aStr + "</li>");
        } catch (Exception e) {
          continue;
        }
      }
      divStr.append("</ul>");
      map.put("currentLetter", code);
      map.put("letterList", letters);
      // 将结果数据写入静态文件.
      try {
        map.put("content", StringUtils.replace(divStr.toString(), "[language]", "zh_cn"));
        this.writeHtmlCon(fileRoot, "pub2_" + code + "_zh_CN.html", this.getHtmlCon(PUBLICATION_INDEX_ZH_FTL, map));

        map.put("content", StringUtils.replace(divStr.toString(), "[language]", "en_us"));
        this.writeHtmlCon(fileRoot, "pub2_" + code + "_en_US.html", this.getHtmlCon(PUBLICATION_INDEX_EN_FTL, map));
      } catch (ServiceException e) {
        logger.error("写入站外检索成果文件出错,出错类别：" + code, e);
      }
    } catch (Exception e) {
      logger.error("将成果分层数据构建为html文件", e);
    }
  }

  private void writeHtmlCon(String fileRoot2, String fileName, String content) {
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

  @Override
  public Page<PubIndexThirdLevel> getPubByLabel2(String code, Integer secondGroup, Integer thirdGroup,
      Page<PubIndexThirdLevel> pages) {
    if ("other".equalsIgnoreCase(code)) {
      code = "0";
    }
    pubSeoThirdLevelSerachDao.getThirdLevel2(code, secondGroup, thirdGroup, pages);
    return pages;
  }
}
