package com.smate.web.v8pub.service.searchimport;

import java.util.Locale;

import com.smate.web.v8pub.dto.PubTypeInfoDTO;
import com.smate.web.v8pub.service.fileimport.extract.PubSaveData;
import com.smate.web.v8pub.vo.PendingImportPubVO;

/**
 * 成果信息构建驱动，提供Pattern和数据
 * 
 * @author jszhou
 */
public interface PubImportInfoDriver {

  /**
   * 成果类型ID.
   * 
   * @return int
   */
  int getForType();

  /**
   * 返回格式化Pattern.
   * 
   * @return String
   */
  String getPattern();

  /**
   * 返回格式化需要的数据.
   * 
   * @param locale Locale
   * @param xmlDocument XmlDocument
   * @param context Xml处理的上下文对象
   * @return Map<String, String>
   */
  String getBriefDescData(Locale locale, PendingImportPubVO pubvo) throws Exception;

  /**
   * 通过json数据构建PubSaveData对象
   * 
   * @param json
   * @return
   */
  PubSaveData buildPubSaveDataByJson(String json);

  /**
   * 通过Json构建待导入成果信息
   * 
   * @param json
   * @return
   */
  PendingImportPubVO buildPendingImportPubVoByJson(String json);

  /**
   * 获取成果typeInfo对象
   * 
   * @param pubType
   * @return
   */
  PubTypeInfoDTO getNewPubTypeInfo(Integer pubType);

  /**
   * 重新构造pubTypeInfo对象
   * 
   * @param typeInfoJson
   * @return
   */
  PubTypeInfoDTO buildTypeInfo(String typeInfoJson);
}
