/**
 * 
 */
package com.smate.web.prj.xml;

/**
 * 常量定义,用于XML节点的常量定义.
 * 
 * @author yamingd
 */
public class PrjXmlConstants {

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
   * 导入作者列表详细路径.
   */
  public static final String PUB_AUTHOR_XPATH = "/pub_authors/author";
  /**
   * 导入作者详细路径.
   */
  public static final String PUB_AUTHORS_XPATH = "/pub_authors";

  /**
   * xml根节点.
   */
  public static final String ROOT_XPATH = "/data";

  /**
   * 全文路径.
   */
  public static final String PRJ_FULLTEXT_XPATH = "/prj_fulltext";

  /**
   * Meta数据路径.
   */
  public static final String PRJ_META_XPATH = "/prj_meta";

  /**
   * 附件路径.
   */
  public static final String PRJ_ATTACHMENTS_XPATH = "/prj_attachments";

  /**
   * 附件路径.
   */
  public static final String PRJ_ATTACHMENTS_ATTACHMENT_XPATH = "/prj_attachments/prj_attachment";

  /**
   * 作者路径.
   */
  public static final String PRJ_MEMBERS_XPATH = "/prj_members";

  /**
   * 作者路径.
   */
  public static final String PRJ_MEMBERS_MEMBER_XPATH = "/prj_members/prj_member";

  /**
   * 数据检查结果路径.
   */
  public static final String PRJ_ERRORS_XPATH = "/prj_errors";

  /**
   * 数据检查结果路径.
   */
  public static final String PRJ_ERRORS_ERROR_XPATH = "/prj_errors/error";

  /**
   * 项目基本信息路径.
   */
  public static final String PROJECT_XPATH = "/project";

  /**
   * 导入项目时基本信息路径.
   */
  public static final String PUBLICATION_XPATH = "/publication";
  /**
   * 允许最多20个作者.
   */
  public static final int MAX_AUTHOR_COUNT = 20;
}
