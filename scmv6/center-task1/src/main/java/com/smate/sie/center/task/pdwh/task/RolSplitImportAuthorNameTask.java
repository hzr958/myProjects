package com.smate.sie.center.task.pdwh.task;

import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.sie.center.task.pdwh.utils.XmlUtil;

/**
 * 导入人员，拆分人员.
 * 
 * @author liqinghua
 * 
 */
public class RolSplitImportAuthorNameTask extends SplitImportAuthorNameTask {

  /**
   * 
   */
  private String name = "rol_split_import_author_name";

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {

    return true;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    super.run(xmlDocument, context);
    String authorNamesSpec = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "authors_names_spec");
    authorNamesSpec = XmlUtil.formateAuthorNames(authorNamesSpec);
    String authorNames = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names");
    authorNames = XmlUtil.formateAuthorNames(authorNames);
    if (!"".equals(authorNames)) {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original_author_names", authorNames);
    } else {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original_author_names", authorNamesSpec);
    }
    return true;
  }


  public static void main(String[] args) {
    String name =
        "1Hongming_Nie1; ［英］Jianjie-Chen~[1，2];  *马如宇.（1）;  刘妮4校; 亚伦&middot; 赖2 著， 顾瑶3译， 刘妮4校 ; Yong Zhang~3; Xiaoyu Hu~4; Changjian Yin~5; Guoguang Sheng~6; Xiaodong Li~6; Boyu Xue~7; Lifu Wang~8; Wenxia Zhao~9; Xiaorong Chen~(10); Wei Zhang~(11); Yuyong Jiang~(12); Xiaoling Chi~(13); Xiaojun Wang~(14); Qikai Wu~(15); Dewen Mao~(16); Guoxian Lin~(17); Shanzhong Tan~(18); Yuanwang Qiu~(19); Erli Gu~(20); Zhongye Xu~(21) *Corresponding author:Department of hepatology disease";
    System.out.println(name);
    name = HtmlUtils.htmlUnescape(name);
    name = name.replaceAll("(<.*?>)|(\\(.*?\\))|(（.*）)", "").replaceAll("[*,~]", "").replaceAll("[\\d]", "");
    name = name.replaceAll("(\\[.*?\\])|(［.*?］)", "");
    System.out.println(name);
  }

}
