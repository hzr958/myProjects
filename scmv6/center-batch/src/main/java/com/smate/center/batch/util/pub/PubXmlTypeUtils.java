package com.smate.center.batch.util.pub;

import com.smate.core.base.pub.enums.PublicationTypeEnum;

/**
 * 成果类型工具类.
 * 
 * @author liqinghua
 * 
 */
public class PubXmlTypeUtils {

  /**
   * 是否是期刊文章.
   * 
   * @param pubType
   * @return
   */
  public static boolean isJournalType(Integer pubType) {

    if (pubType == null) {
      return false;
    }
    return pubType == PublicationTypeEnum.JOURNAL_ARTICLE;
  }

  /**
   * 是否是会议论文.
   * 
   * @param pubType
   * @return
   */
  public static boolean isConference(Integer pubType) {
    if (pubType == null) {
      return false;
    }
    return pubType == PublicationTypeEnum.CONFERENCE_PAPER;
  }

  /**
   * 是否是专利.
   * 
   * @param pubType
   * @return
   */
  public static boolean isPatent(Integer pubType) {
    if (pubType == null) {
      return false;
    }
    return pubType == PublicationTypeEnum.PATENT;
  }
}
