package com.smate.center.open.service.keyword;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author aijiangbin
 * @date 2018年4月25日
 */
public interface PdwhPubkeywordsService {

  /**
   * 获取需要翻译的成果关键词
   * 
   * @return
   */
  public List<Map<String, Object>> getNeedTranslateKeyword();

  public List<Map<String, Object>> getKeywordByPubId(Long pubId);

  public List<Map<String, Object>> getKeywordByPageNo(Integer pageNo);

  public void updatePdwhPubkeywords(Map<String, Object> map) throws Exception;

  public void updatePdwhPubkeywordsList(List<Map<String, Object>> list) throws Exception;

}
