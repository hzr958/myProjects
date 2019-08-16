package com.smate.web.management.service.analysis;

import java.util.LinkedHashMap;
import java.util.List;

import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.analysis.PsnCmdForm;


/**
 * 基金、论文合作者推荐：查询缓存接口.
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCooperatorCache {

  /**
   * 拆分关键词最多只能50个，超出忽略
   */
  Integer MAX_KW = 50;

  // 生产机最大的数值ln(2.72+质量+合作度)
  Double MAX_CONST_VAL = 4.452;
  /**
   * 每次获取的数据条数.
   */
  Integer EACH_FETCH_SIZE = 10;
  /**
   * 最多获取的数据条数
   */
  Integer MAX_FETCH_SIZE = 30;

  /**
   * 符合关键词的最多人员数
   */
  Integer MAX_KWPSN_FETCH_SIZE = 500;

  /**
   * 缓存名称
   */
  String CACHE_NAME = "PsnCooperatorCache";

  /**
   * 缓存过时时间.
   */
  Integer EXP = 60 * 15;

  /**
   * 基金、论文推荐合作者分页列表.
   * 
   * @param form
   * @param page
   * @param psnId
   * @return
   * @throws ServiceException
   */
  Page<PsnCmdForm> getCooperatorByPage(PsnCmdForm form, Page<PsnCmdForm> page, Long psnId) throws Exception;

  List<LinkedHashMap<String, String>> getKeywordCount(PsnCmdForm form) throws Exception;

  List<LinkedHashMap<String, Object>> getKeywordCountByYear(PsnCmdForm form) throws Exception;

  List<String> getKeywordsList(PsnCmdForm form) throws Exception;
}
