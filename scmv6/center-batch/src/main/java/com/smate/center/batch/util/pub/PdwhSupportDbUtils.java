package com.smate.center.batch.util.pub;

import org.apache.commons.lang3.StringUtils;

/**
 * 基准库支持的数据库工具类.
 * 
 * @author liqinghua
 * 
 */
public class PdwhSupportDbUtils {

  /**
   * 判断此数据库基准库是否支持.
   * 
   * @param dbcode
   * @return
   */
  public static boolean isPdwhSupportDb(String dbCode) {

    if (StringUtils.isBlank(dbCode)) {
      return false;
    }
    // cnki
    if (PubXmlDbUtils.isCnkiDb(dbCode)) {
      return true;
      // scopus
    } else if (PubXmlDbUtils.isScopusDb(dbCode)) {
      return true;
      // isi
    } else if (PubXmlDbUtils.isIsiDb(dbCode)) {
      return true;
      // ei
    } else if (PubXmlDbUtils.isEiDb(dbCode)) {

      return true;
      // wanfang
    } else if (PubXmlDbUtils.isWanFangDb(dbCode)) {

      return true;
      // CNIPRDb
    } else if (PubXmlDbUtils.isCNIPRDb(dbCode)) {

      return true;
      // PubMed
    } else if (PubXmlDbUtils.isPubMedDb(dbCode)) {

      return true;
    } // iEEEXp
    else if (PubXmlDbUtils.isIEEEXploreDb(dbCode)) {
      return true;
    } // ScienceDirect
    else if (PubXmlDbUtils.isScienceDirectDb(dbCode)) {

      return true;
    } // baidu百度专利暂时不上线，没有专利号
      // else if (PubXmlDbUtils.isBaiduDb(dbCode)) {
      //
      // baiduPublicationService.addFetchPub(pubCache.getPsnId(),
      // pubCache.getInsId(),
      // pubCache.getFetchTime(), pubData);
      // }
      // CNIPRDb
    else if (PubXmlDbUtils.isCnkipatDb(dbCode)) {

      return true;
    }
    return false;
  }

  /**
   * 判断此数据库基准库是否支持.
   * 
   * @param dbcode
   * @return
   */
  public static boolean isPdwhSupportDb(Integer dbId) {

    if (dbId == null) {
      return false;
    }
    // isi文献
    if (PubXmlDbUtils.isIsiDb(dbId)) {
      return true;
      // scopus文献
    } else if (PubXmlDbUtils.isScopusDb(dbId)) {
      return true;
      // cnki文献
    } else if (PubXmlDbUtils.isCnkiDb(dbId)) {
      return true;
      // ei
    } else if (PubXmlDbUtils.isEiDb(dbId)) {
      return true;
      // wanfang
    } else if (PubXmlDbUtils.isWanFangDb(dbId)) {
      return true;
      // CNIPRDb
    } else if (PubXmlDbUtils.isCNIPRDb(dbId)) {
      return true;
      // pubmed
    } else if (PubXmlDbUtils.isPubMedDb(dbId)) {
      return true;
    } // ieeexp
    else if (PubXmlDbUtils.isIEEEXploreDb(dbId)) {
      return true;
    } // ScienceDirect
    else if (PubXmlDbUtils.isScienceDirectDb(dbId)) {
      return true;
    } // baidu百度专利暂时不上线，没有专利号
      // else if (PubXmlDbUtils.isBaiduDb(dbId)) {
      // return true;
      // }
      // CNIPRDb
    else if (PubXmlDbUtils.isCnkipatDb(dbId)) {
      return true;
    }
    return false;
  }

  /**
   * 判断此数据库基准库是否支持.
   * 
   * @param dbcode
   * @return
   */
  public static boolean isPdwhSupportDb(Long dbId) {
    if (dbId == null) {
      return false;
    }
    return isPdwhSupportDb(dbId.intValue());
  }
}
