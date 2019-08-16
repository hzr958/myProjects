package com.smate.web.v8pub.service.restful;

import java.util.Map;

public interface PublicPubDupCheckService {
  /**
   * 基准库后台导入成果查重
   * 
   */
  public String getPubDupcheckStatus(Map map);

  /**
   * 基准库的成果到个人库查重
   */
  public String getPdwhPubDupcheckStatus(Long psnId, Long pdwhPubId);

  /**
   * 切换成果类型进行查重
   * 
   * @param psnId
   * @param pubId
   * @param pubType
   * @return
   */
  public String dupByPubType(Long psnId, Long pubId, Integer pubType);

  public Map<String, Object> dupByPubType(Long psnId, String cacheKey, Integer pubType, Integer index);

  /**
   * 个人库成果查重
   */
  public String getSnsPubDupcheckStatus(Long psnId, Long snsPubId);

}
