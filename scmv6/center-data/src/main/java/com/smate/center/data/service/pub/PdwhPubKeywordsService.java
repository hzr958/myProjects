package com.smate.center.data.service.pub;

import java.util.List;

import com.smate.center.data.model.pub.PdwhPubKeywordsCategory;

/**
 * 计算pdwh成果关键词组合频次相关服务
 * 
 * @author lhd
 *
 */
public interface PdwhPubKeywordsService {

  /**
   * 获取指定数量的成果关键词
   * 
   * @param size
   * @param startPubId
   * @param endPubId
   * @return
   */
  List<PdwhPubKeywordsCategory> getPdwhPubKeywords(Integer size, Long startPubId, Long endPubId);

  /**
   * 更新状态
   * 
   * @param pubId
   * @param status
   */
  public void saveOpResult(Long pubId, int status);

  public Boolean checkCombine(String pubKey) throws Exception;

  public void saveCombineTable(String pubKey) throws Exception;

}
