package com.smate.center.task.single.util.pub;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 导入文件xml格式过滤.
 * 
 * @author liqinghua
 */
public class ImplXmlFileNameFilter implements FilenameFilter {
  public Integer xmlType;

  public ImplXmlFileNameFilter(Integer xmlType) {
    super();
    this.xmlType = xmlType;
  }

  @Override
  public boolean accept(File dir, String name) {
    // 359_20151_16_18_1.xml
    name = name.toLowerCase();
    // 文件
    if (!name.endsWith(".xml")) {
      return true;
    }
    if (xmlType.equals(1)) {
      // rainpat专利
      if (name.matches("^[0-9]{4}-[0-9]{1,}\\.xml")) {
        return true;
      }

    } else {
      // 格式正确 // 13304407_2017_32768_23_1_000345689900020
      if (name.matches("^[0-9]{1,}_[0-9]{1,}_.*\\.xml")) {
        return true;
      }
    }

    return false;
  }

}
