package com.smate.core.base.utils.constant;

/**
 * 常量定义,用于XML节点的常量定义.
 * 
 * @author hzr
 */
public class PubXmlConstants {

  /**
   * <?xml version=\"1.0\" encoding=\"UTF-8\"?>.
   */
  public static final String XML_DECL = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  /**
   * dd/MM/yyyy.
   */
  public static final String ENG_DATE_PATTERN = "dd/MM/yyyy";

  /**
   * yyyy-MM-dd.
   */
  public static final String CHS_DATE_PATTERN = "yyyy-MM-dd";

  /**
   * dd/MM/yyyy.
   */
  public static final String CITE_ENG_DATE_PATTERN = "dd/MM/yyyy";

  /**
   * yyyy/MM/dd.
   */
  public static final String CITE_CHS_DATE_PATTERN = "yyyy/MM/dd";
  /**
   * 专利的有效数据 yyyy.MM.dd.
   */
  public static final String PATENT_CHS_DATE_PATTERN = "yyyy.MM.dd";

  /**
   * xml根节点.
   */
  public static final String ROOT_XPATH = "/data";

  /**
   * 学位论文路径.
   */
  public static final String PUB_THESIS_XPATH = "/pub_thesis";

  /**
   * 书籍路径.
   */
  public static final String PUB_BOOK_XPATH = "/pub_book";

  /**
   * 全文路径.
   */
  public static final String PUB_FULLTEXT_XPATH = "/pub_fulltext";

  /**
   * Meta数据路径.
   */
  public static final String PUB_META_XPATH = "/pub_meta";

  /**
   * pubsimple增加内容
   */
  public static final String PUB_EXPAND_XPATH = "/pub_expand";

  /**
   * 成果类型路径.
   */
  public static final String PUB_TYPE_XPATH = "/pub_type";

  /**
   * 相关项目路径.
   */
  public static final String PUB_RELATED_PROJECTS_XPATH = "/pub_related_projects";

  /**
   * 附件路径.
   */
  public static final String PUB_ATTACHMENTS_XPATH = "/pub_attachments";

  /**
   * 附件路径.
   */
  public static final String PUB_ATTACHMENTS_ATTACHMENT_XPATH = "/pub_attachments/pub_attachment";

  /**
   * 奖励路径.
   */
  public static final String PUB_AWARD_XPATH = "/pub_award";

  /**
   * 其他类型路径.
   */
  public static final String PUB_OTHER_XPATH = "/pub_other";

  /**
   * 会议论文路径.
   */
  public static final String PUB_CONF_PAPER_XPATH = "/pub_conf_paper";

  /**
   * 专利路径.
   */
  public static final String PUB_PATENT_XPATH = "/pub_patent";

  /**
   * 专利权人
   */
  public static final String PUB_APPLIERS_APPLIER_XPATH = "/pub_appliers/pub_applier";

  /**
   * 期刊路径.
   */
  public static final String PUB_JOURNAL_XPATH = "/pub_journal";

  /**
   * 期刊编辑路径.
   */
  public static final String PUB_JOURNAL_EDITOR_XPATH = "/pub_journal_editor";

  /**
   * 项目路径.
   */
  public static final String PUB_PROJECT_XPATH = "/pub_project";

  /**
   * 作者路径.
   */
  public static final String PUB_MEMBERS_XPATH = "/pub_members";

  /**
   * 作者路径.
   */
  public static final String PUB_MEMBERS_MEMBER_XPATH = "/pub_members/pub_member";

  /**
   * 作者详细路径.
   */
  public static final String PUB_AUTHORS_XPATH = "/pub_authors";
  /**
   * 作者详细路径.
   */
  public static final String PUB_AUTHOR_XPATH = "/pub_authors/author";

  /**
   * 数据检查结果路径.
   */
  public static final String PUB_ERRORS_XPATH = "/pub_errors";

  /**
   * 数据检查结果路径.
   */
  public static final String PUB_ERRORS_ERROR_XPATH = "/pub_errors/error";

  /**
   * 成果基本信息路径.
   */
  public static final String PUBLICATION_XPATH = "/publication";
  /**
   * 允许最多20个作者.
   */
  public static final int MAX_AUTHOR_COUNT = 20;

  /**
   * 工作文档路径.
   */
  public static final String PUB_WORK_PAPER_XPATH = "/pub_work_paper";
  /**
   * 收录情况路径.
   */
  public static final String PUB_LIST_XPATH = "/pub_list";
  /**
   * 成果基准库ID关联路径.
   */
  public static final String PUB_PDWH_XPATH = "/pub_pdwh";

  /**
   * 批量保存data节点位置
   */
  public static final String PUB_SCHOLAR_DATA = "/scholarWorks/data";

  /**
   * 成果节点.
   */
  public static final String[] PUB_NODES =
      new String[] {"pub_other", "pub_award", "pub_book", "pub_book_chapter", "pub_conf_paper", "pub_journal",
          "pub_journal_editor", "pub_patent", "pub_project", "pub_thesis", "pub_work_paper", "pub_list"};

  // ISI DBID
  public static final Long SOURCE_DBID_ISTP = 15L;
  public static final Long SOURCE_DBID_SCI = 16L;
  public static final Long SOURCE_DBID_SSCI = 17L;
  public static final Integer SOURCE_DBID_ISTP_INT = 15;
  public static final Integer SOURCE_DBID_SCI_INT = 16;
  public static final Integer SOURCE_DBID_SSCI_INT = 17;
  public static final Long[] SOURCE_ISIDBIDS = {SOURCE_DBID_ISTP, SOURCE_DBID_SCI, SOURCE_DBID_SSCI};
  public static final Integer[] SOURCE_ISIDBIDS_INT =
      {SOURCE_DBID_ISTP.intValue(), SOURCE_DBID_SCI.intValue(), SOURCE_DBID_SSCI.intValue()};
  // ISI DBCODE
  public static final String SOURCE_DBCODE_ISTP = "ISTP";
  public static final String SOURCE_DBCODE_SCI = "SCI";
  public static final String SOURCE_DBCODE_SSCI = "SSCI";

  // CNKI DBID
  public static final Long SOURCE_DBID_CNKI = 4L;
  public static final Integer SOURCE_DBID_CNKI_INT = SOURCE_DBID_CNKI.intValue();
  // CNKI DBCODE
  public static final String SOURCE_DBCODE_CNKI = "ChinaJournal";

  // SCOUPS DBID
  public static final Long SOURCE_DBID_SCOPUS = 8L;
  public static final Integer SOURCE_DBID_SCOPUS_INT = SOURCE_DBID_SCOPUS.intValue();
  // SCOUPS DBCODE
  public static final String SOURCE_DBCODE_SCOPUS = "Scopus";

  // EI DBID
  public static final Long SOURCE_DBID_EI = 14L;
  public static final Integer SOURCE_DBID_EI_INT = SOURCE_DBID_EI.intValue();
  // EI DBCODE
  public static final String SOURCE_DBCODE_EI = "EI";

  // 万方
  public static final Long SOURCE_WANG_FANG = 10L;
  public static final Integer SOURCE_WANG_FANG_INT = SOURCE_WANG_FANG.intValue();
  // 万方 DBCODE
  public static final String SOURCE_DBCODE_WANG_FANG = "WanFang";

  // 中国知识产权网
  public static final Long SOURCE_CNIPR = 11L;
  public static final Integer SOURCE_CNIPR_INT = SOURCE_CNIPR.intValue();
  // 中国知识产权网 DBCODE
  public static final String SOURCE_DBCODE_CNIPR = "CNIPR";

  // ScienceDirect
  public static final Long SOURCE_SCD = 5L;
  public static final Integer SOURCE_SCD_INT = SOURCE_SCD.intValue();
  // ScienceDirect
  public static final String SOURCE_DBCODE_SCD = "ScienceDirect";

  // IEEEXplore
  public static final Long SOURCE_IEEEXP = 1L;
  public static final Integer SOURCE_IEEEXP_INT = SOURCE_IEEEXP.intValue();
  // IEEEXplore
  public static final String SOURCE_DBCODE_IEEEXP = "IEEEXplore";

  // PubMed
  public static final Long SOURCE_PUBMED = 19L;
  public static final Integer SOURCE_PUBMED_INT = SOURCE_PUBMED.intValue();
  // PubMed
  public static final String SOURCE_DBCODE_PUBMED = "PubMed";

  // Baidu
  public static final Long SOURCE_BAIDU = 20L;
  public static final Integer SOURCE_BAIDU_INT = SOURCE_BAIDU.intValue();
  // Baidu
  public static final String SOURCE_DBCODE_BAIDU = "Baidu";

  // Cnkipat
  public static final Long SOURCE_CNKIPAT = 21L;
  public static final Integer SOURCE_CNKIPAT_INT = SOURCE_CNKIPAT.intValue();
  // Cnkipat
  public static final String SOURCE_DBCODE_CNKIPAT = "Cnkipat";

  // RAINPAT
  public static final Long SOURCE_RAINPAT = 31L;
  public static final Integer SOURCE_RAINPAT_INT = SOURCE_RAINPAT.intValue();
  // RAINPAT
  public static final String SOURCE_DBCODE_RAINPAT = "RAINPAT";

  // OPEN_ACCESS_LIBRARY
  public static final Long SOURCE_OPEN_ACCESS_LIBRARY = 32L;
  public static final Integer SOURCE_OPEN_ACCESS_LIBRARY_INT = SOURCE_OPEN_ACCESS_LIBRARY.intValue();
  // OPEN_ACCESS_LIBRARY
  public static final String SOURCE_DBCODE_OPEN_ACCESS_LIBRARY = "OPENACCESSLIBRARY";

  /**
   * 引用情况.
   */
  public static final String[] PUB_LIST_ATTRS = new String[] {"list_sci", "list_ei", "list_istp", "list_ssci"};

  /**
   * 引用情况，包括原始引用情况.
   */
  public static final String[] PUB_LIST_SOURCE_ATTRS = new String[] {"list_sci", "list_ei", "list_istp", "list_ssci",
      "list_sci_source", "list_ei_source", "list_istp_source", "list_ssci_source"};

  /**
   * 成果编辑时，保留的publication属性.
   */
  public static final String[] PUB_EDIT_REMAIN_PUBLICATION_ATTR =
      new String[] {"tmpsource_url", "tmpcite_record_url", "organization", "authors_names_spec", "original",
          "description", "citation_index", "source_id", "isi_id", "ei_id", "sps_id", "source", "source_url",
          "source_db_id", "source_db_code", "series_name", "is_insert", "ins_info", "source_catalog", "cited_url",
          "original_author_names", "cite_times", "author_names", "author_names_abbr", "issn", "cited_list"};

  /**
   * 成果编辑时，保留的meta属性.
   */
  public static final String[] PUB_EDIT_REMAIN_MATA_ATTR =
      new String[] {"source_url", "source_db_id", "source", "source_db_code", "isi_id", "ei_id", "sps_id"};

  /**
   * 成果编辑时，保留的pub_list属性.
   */
  public static final String[] PUB_EDIT_REMAIN_PUBLIST_ATTR = new String[] {"list_sci_source", "list_ei_source",
      "list_istp_source", "list_ssci_source", "title_hash", "unit_hash", "source_id_hash", "patent_hash"};

  /**
   * 成果合并时，合并的meta属性.
   */
  public static final String[] PUB_MERGE_REMAIN_META_ATTR =
      new String[] {"source_id", "isi_id", "ei_id", "sps_id", "cited_url", "cite_times", "authority"};

  /**
   * 成果合并时，合并的publication属性.
   */
  public static final String[] PUB_MERGE_REMAIN_PUBLICATION_ATTR =
      new String[] {"organization", "source_id", "cited_url", "original", "isbn", "issue", "volume"};

  /**
   * psnId
   */
  public static final String PSNID = "psnId";
  /**
   * local
   */
  public static final String LOCAL = "local";
  /**
   * currentPubStatus
   */
  public static final String CURRENT_PUB_STATUS = "currentPubStatus";
  /**
   * currentAction
   */
  public static final String CURRENT_ACTION = "currentAction";
  /**
   * articleType
   */
  public static final String ARTICLE_TYPE = "articleType";
  /**
   * groupFolderId
   */
  public static final String GROUP_FOLDER_ID = "groupFolderId";

  /**
   * privacyLevel
   */
  public static final String PRIVACY_LEVEL = "privacyLevel";
  /**
   * groupId
   */
  public static final String GROUP_ID = "groupId";

  /**
   * 专利权人.
   */
  public static final String PUB_APPLIERS_XPATH = "/pub_appliers";

}
