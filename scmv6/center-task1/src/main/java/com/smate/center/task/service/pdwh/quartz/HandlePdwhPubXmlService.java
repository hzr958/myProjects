package com.smate.center.task.service.pdwh.quartz;

import java.util.Map;

public interface HandlePdwhPubXmlService {
  /**
   * xml转成json
   * 
   * @param pubData
   * @return
   */
  Map<String, Object> XmlToJsonData(String pubData);

  /**
   * 基准库成果查重
   * 
   * @param map
   * @param headers
   * @return
   */
  String getPubDupucheckStatus(Map map);

  /**
   * 调用接口保存新成果
   * 
   * @param map
   * @param insId
   * @param psnId
   * @return
   */

  String saveNewPdwhPub(Map<String, Object> map, Long insId, Long psnId);

  /**
   * 调用接口更新成果
   * 
   * @param map
   * @param pdwhPubId
   * @param insId
   * @param psnId
   * @return
   */
  String updatePdwhPub(Map<String, Object> map, Long pdwhPubId, Long insId, Long psnId);

  /**
   * 调用接口更新引用次数
   * 
   * @param currentPubId
   * @param fileciteTimes
   * @return
   */
  String updateCitedTimes(Long currentPubId, int fileciteTimes);

  void updateSnsPubCitedTimes(Long pubId, int fileciteTimes);

  Long saveOriginalPdwhPubRelation();

  void updateOriginalPdwhPubRelation(Long id, Long pdwhPubId, int status);

  Map<String, Object> CrossrefToJsonData(Map<String, Object> DataMap);

  void updatePubInfoInSolr(Long currentPubId);

}
