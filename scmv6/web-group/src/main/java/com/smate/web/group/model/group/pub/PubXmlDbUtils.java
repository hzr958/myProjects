package com.smate.web.group.model.group.pub;

import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * 成果来源工具类.
 * 
 * @author liqinghua
 * 
 */
public class PubXmlDbUtils {

  /**
   * 是否是ISI来源ID.
   * 
   * @return
   */
  public static boolean isIsiDb(Long sourceDbId) {

    return PubXmlConstants.SOURCE_DBID_ISTP.equals(sourceDbId) || PubXmlConstants.SOURCE_DBID_SCI.equals(sourceDbId)
        || PubXmlConstants.SOURCE_DBID_SSCI.equals(sourceDbId);
  }

  /**
   * 是否是ISI来源code.
   * 
   * @return
   */
  public static boolean isIsiDb(String dbCode) {

    return PubXmlConstants.SOURCE_DBCODE_ISTP.equalsIgnoreCase(dbCode)
        || PubXmlConstants.SOURCE_DBCODE_SCI.equalsIgnoreCase(dbCode)
        || PubXmlConstants.SOURCE_DBCODE_SSCI.equalsIgnoreCase(dbCode);
  }

  /**
   * 是否是ISI来源ID.
   * 
   * @return
   */
  public static boolean isIsiDb(Integer sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_DBID_ISTP == sourceDbId.longValue()
        || PubXmlConstants.SOURCE_DBID_SCI == sourceDbId.longValue()
        || PubXmlConstants.SOURCE_DBID_SSCI == sourceDbId.longValue();
  }

  /**
   * 是否是CNKI来源ID.
   * 
   * @return
   */
  public static boolean isCnkiDb(Long sourceDbId) {

    return PubXmlConstants.SOURCE_DBID_CNKI.equals(sourceDbId);
  }

  /**
   * 是否是CNKI来源code.
   * 
   * @return
   */
  public static boolean isCnkiDb(String dbCode) {

    return PubXmlConstants.SOURCE_DBCODE_CNKI.equalsIgnoreCase(dbCode);
  }

  /**
   * 是否是CNKI来源ID.
   * 
   * @return
   */
  public static boolean isCnkiDb(Integer sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_DBID_CNKI == sourceDbId.longValue();
  }

  /**
   * 是否是SCOPUS来源code.
   * 
   * @return
   */
  public static boolean isScopusDb(String dbCode) {

    return PubXmlConstants.SOURCE_DBCODE_SCOPUS.equalsIgnoreCase(dbCode);
  }

  /**
   * 是否是SCOPUS来源ID.
   * 
   * @return
   */
  public static boolean isScopusDb(Long sourceDbId) {

    return PubXmlConstants.SOURCE_DBID_SCOPUS.equals(sourceDbId);
  }

  /**
   * 是否是SCOPUS来源ID.
   * 
   * @return
   */
  public static boolean isScopusDb(Integer sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_DBID_SCOPUS == sourceDbId.longValue();
  }

  /**
   * 是否是EI来源code.
   * 
   * @return
   */
  public static boolean isEiDb(String dbCode) {

    return PubXmlConstants.SOURCE_DBCODE_EI.equalsIgnoreCase(dbCode);
  }

  /**
   * 是否是EI来源ID.
   * 
   * @return
   */
  public static boolean isEiDb(Long sourceDbId) {

    return PubXmlConstants.SOURCE_DBID_EI.equals(sourceDbId);
  }

  /**
   * 是否是EI来源ID.
   * 
   * @return
   */
  public static boolean isEiDb(Integer sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_DBID_EI == sourceDbId.longValue();
  }

  /**
   * 是否是Wanfang来源code.
   * 
   * @return
   */
  public static boolean isWanFangDb(String dbCode) {
    dbCode = "wf".equalsIgnoreCase(dbCode) ? "WanFang" : dbCode;
    return PubXmlConstants.SOURCE_DBCODE_WANG_FANG.equalsIgnoreCase(dbCode);
  }

  /**
   * 是否是Wanfang来源ID.
   * 
   * @return
   */
  public static boolean isWanFangDb(Long sourceDbId) {

    return PubXmlConstants.SOURCE_WANG_FANG.equals(sourceDbId);
  }

  /**
   * 是否是Wanfang来源ID.
   * 
   * @return
   */
  public static boolean isWanFangDb(Integer sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_WANG_FANG == sourceDbId.longValue();
  }

  /**
   * 是否是中国知识产权网来源code.
   * 
   * @return
   */
  public static boolean isCNIPRDb(String dbCode) {

    return PubXmlConstants.SOURCE_DBCODE_CNIPR.equalsIgnoreCase(dbCode);
  }

  /**
   * 是否是中国知识产权网来源ID.
   * 
   * @return
   */
  public static boolean isCNIPRDb(Long sourceDbId) {

    return PubXmlConstants.SOURCE_CNIPR.equals(sourceDbId);
  }

  /**
   * 是否是中国知识产权网来源ID.
   * 
   * @return
   */
  public static boolean isCNIPRDb(Integer sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_CNIPR == sourceDbId.longValue();
  }

  /**
   * 是否是ScienceDirect来源ID.
   * 
   * @return
   */
  public static boolean isScienceDirectDb(Integer sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_SCD == sourceDbId.longValue();
  }

  /**
   * 是否是ScienceDirect来源code.
   * 
   * @return
   */
  public static boolean isScienceDirectDb(String dbCode) {

    return PubXmlConstants.SOURCE_DBCODE_SCD.equalsIgnoreCase(dbCode);
  }

  /**
   * 是否是ScienceDirect来源ID.
   * 
   * @return
   */
  public static boolean isScienceDirectDb(Long sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_SCD.equals(sourceDbId);
  }

  /**
   * 是否是IEEEXplore来源ID.
   * 
   * @return
   */
  public static boolean isIEEEXploreDb(Integer sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_IEEEXP == sourceDbId.longValue();
  }

  /**
   * 是否是IEEEXplore来源code.
   * 
   * @return
   */
  public static boolean isIEEEXploreDb(String dbCode) {

    return PubXmlConstants.SOURCE_DBCODE_IEEEXP.equalsIgnoreCase(dbCode);
  }

  /**
   * 是否是IEEEXplore来源ID.
   * 
   * @return
   */
  public static boolean isIEEEXploreDb(Long sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_IEEEXP.equals(sourceDbId);
  }

  /**
   * 是否是PubMed来源ID.
   * 
   * @return
   */
  public static boolean isPubMedDb(Integer sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_PUBMED == sourceDbId.longValue();
  }

  /**
   * 是否是PubMed来源code.
   * 
   * @return
   */
  public static boolean isPubMedDb(String dbCode) {

    return PubXmlConstants.SOURCE_DBCODE_PUBMED.equalsIgnoreCase(dbCode);
  }

  /**
   * 是否是PubMed来源ID.
   * 
   * @return
   */
  public static boolean isPubMedDb(Long sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_PUBMED.equals(sourceDbId);
  }

  /**
   * 是否是Baidu来源ID.
   * 
   * @return
   */
  public static boolean isBaiduDb(Integer sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_BAIDU == sourceDbId.longValue();
  }

  /**
   * 是否是Baidu来源code.
   * 
   * @return
   */
  public static boolean isBaiduDb(String dbCode) {

    return PubXmlConstants.SOURCE_DBCODE_BAIDU.equalsIgnoreCase(dbCode);
  }

  /**
   * 是否是Baidu来源ID.
   * 
   * @return
   */
  public static boolean isBaiduDb(Long sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_BAIDU.equals(sourceDbId);
  }

  /**
   * 是否是Cnkipat来源ID.
   * 
   * @return
   */
  public static boolean isCnkipatDb(Integer sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_CNKIPAT == sourceDbId.longValue();
  }

  /**
   * 是否是Cnkipat来源code.
   * 
   * @return
   */
  public static boolean isCnkipatDb(String dbCode) {

    return PubXmlConstants.SOURCE_DBCODE_CNKIPAT.equalsIgnoreCase(dbCode);
  }

  /**
   * 是否是Cnkipat来源ID.
   * 
   * @return
   */
  public static boolean isCnkipatDb(Long sourceDbId) {

    if (sourceDbId == null) {
      return false;
    }
    return PubXmlConstants.SOURCE_CNKIPAT.equals(sourceDbId);
  }
}
