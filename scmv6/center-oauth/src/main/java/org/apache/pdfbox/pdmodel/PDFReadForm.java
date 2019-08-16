package org.apache.pdfbox.pdmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PDFReadForm {
  /**
   * 放匹配上的内容.
   */
  private Map<String, Set<String>> pdfMatchInfo = new HashMap<String, Set<String>>();

  /**
   * 上一行数据
   */
  private List<?> pdfReadPreContext = new ArrayList<Object>();

  /**
   * 上一行内容
   */
  private String pdfReadPreContent = "";

  public Map<String, Set<String>> getPdfMatchInfo() {
    return pdfMatchInfo;
  }

  public void setPdfMatchInfo(Map<String, Set<String>> pdfMatchInfo) {
    this.pdfMatchInfo = pdfMatchInfo;
  }

  public List<?> getPdfReadPreContext() {
    return pdfReadPreContext;
  }

  public void setPdfReadPreContext(List<?> pdfReadPreContext) {
    this.pdfReadPreContext = pdfReadPreContext;
  }

  public String getPdfReadPreContent() {
    return pdfReadPreContent;
  }

  public void setPdfReadPreContent(String pdfReadPreContent) {
    this.pdfReadPreContent = pdfReadPreContent;
  }
}
