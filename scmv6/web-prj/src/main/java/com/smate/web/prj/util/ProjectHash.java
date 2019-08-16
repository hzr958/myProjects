package com.smate.web.prj.util;

import com.smate.core.base.utils.data.XmlUtil;

/**
 * 生成项目特定字段Hashcode.
 * 
 * @author yamingd
 */
public class ProjectHash {

  /**
   * @param dirtyTitle 脏标题(包含HTML)
   * @return int
   */
  public static Integer dirtyTitleHash(String dirtyTitle) {

    if (dirtyTitle == null) {
      return null;
    } else {
      dirtyTitle = XmlUtil.trimUnSupportHTML(dirtyTitle);
      dirtyTitle = XmlUtil.trimP(dirtyTitle);
    }

    return cleanTitleHash(dirtyTitle);
  }

  /**
   * 成果标题HashCode.
   * 
   * @param title 标题
   * @return int
   */
  public static Integer cleanTitleHash(String title) {

    if (title == null) {
      return null;
    } else {
      title = XmlUtil.trimAllHtml(title);
      title = XmlUtil.filterForCompare(title);
    }
    title = title.replaceAll("\\s+", "").trim().toLowerCase();
    if ("".equals(title)) {
      return null;
    }
    return title.hashCode();
  }
}
