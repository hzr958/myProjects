package com.smate.web.management.service.analysis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.web.management.model.analysis.sns.KeywordsPsnQuery;

/**
 * 通过关键词查找相关人员.
 * 
 * @author lqh
 * 
 */
public interface KeywordsQueryPsnService extends Serializable {

  public Map<Long, Long> queryKeyPsnAndKeyCount(List<KeywordsPsnQuery> kwList, int size) throws Exception;

}
