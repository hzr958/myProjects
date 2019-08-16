package com.smate.core.base.pub.enums;

/**
 * @author ajb 成果类型枚举
 */
public interface PublicationTypeEnum {

  /**
   * 奖励.
   */
  static final int AWARD = 1;

  /**
   * 书籍.
   */
  static final int BOOK = 2;

  /**
   * 会议论文.
   */
  static final int CONFERENCE_PAPER = 3;

  /**
   * 期刊文章.
   */
  static final int JOURNAL_ARTICLE = 4;

  /**
   * 专利.
   */
  static final int PATENT = 5;

  /**
   * 学位论文.
   */
  static final int THESIS = 8;

  /**
   * 书籍章节.
   */
  static final int BOOK_CHAPTER = 10;

  /**
   * 期刊编辑.
   */
  static final int JOURNAL_EDITOR = 11;

  /**
   * 其他.
   */
  static final int OTHERS = 7;

  /**
   * 标准
   */
  static final int STANDARD = 12;

  /**
   * 软件著作权
   */
  static final int SOFTWARE_COPYRIGHT = 13;

  /**
   * 判断成果是否为专利 pubType=5
   * 
   * @param pubType
   * @return
   */
  public static boolean isPatent(int pubType) {
    return PATENT == pubType;
  }
}
