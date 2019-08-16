package com.smate.center.task.single.service.pub;

/**
 * 基准库pub_all表对应各库的标识，即对应puball表中的dbid字段.
 * 
 * @author liqinghua
 * 
 */
public interface ConstPdwhPubRefDb {
  // ISI
  public static final Integer ISI = 2;
  // ISI-LIST
  public static final Integer[] ISI_LIST = new Integer[] {2, 15, 16, 17};
  // Google Scholar
  public static final Integer GoogleScholar = 3;
  // 中国知网(期刊文章、会议论文、学位论文）
  public static final Integer CNKI = 4;
  // ScienceDirect
  public static final Integer ScienceDirect = 5;
  // Scopus
  public static final Integer SCOPUS = 8;
  // 万方
  public static final Integer WanFang = 10;
  // 中国知识产权网
  public static final Integer CNIPR = 11;
  // Engineering Village (EI Compendex)
  public static final Integer EI = 14;
  // PubMed
  public static final Integer PubMed = 19;
  // 百度专利
  public static final Integer Baidu = 20;
  // 中国知网(专利)
  public static final Integer Cnkipat = 21;
  // IEEEXplore
  public static final Integer IEEE = 1;
  // 英文库
  public static final Integer[] EN_LIST = new Integer[] {2, 15, 16, 17, 3, 5, 8, 14, 19};
  // rainpat
  public static final Integer RainPat = 31;
}
