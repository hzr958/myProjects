package com.smate.web.management.service.analysis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.analysis.ExpertDiscForm;
import com.smate.web.management.model.analysis.KeywordSplit;
import com.smate.web.management.model.analysis.PsnCmdForm;

/**
 * 合作者推荐service接口.
 * 
 * @author pwl
 * 
 */

public interface CooperatorCmdService extends Serializable {
  /**
   * 通过标题和摘要拆分关键词(只返回前面10个关键词).
   * 
   * @param title
   * @param abs
   * @return
   * @throws ServiceException
   */
  List<KeywordSplit> getKeywordsList(String title, String abs) throws Exception;

  /**
   * 获取合作者显示信息.
   * 
   * @param currentPsnId 当前用户id
   * @param insId 当前用户首要单位id
   * @param posId 当前用户职称id
   * @param psnIdList 推荐用户id列表
   * @param commonKeyCountMap 关键词相同个数
   * @param publishedJidList 当前用户发表期刊id列表
   * @param refJidList 当前用户阅读期刊id列表
   * @param discIdList 当前用户研究领域id列表
   * @param psnTaughtList 当前用户所教课程
   * @return
   * @throws ServiceException
   */
  List<PsnCmdForm> getCooperatorInfo(Long currentPsnId, Long insId, List<Long> psnIdList,
      Map<Long, Long> commonKeyCountMap, List<Long> publishedJidList, List<Long> refJidList, List<Long> discIdList,
      List<String> psnTaughtList) throws Exception;

  /**
   * 分页获取合作者显示信息.
   * 
   * @param form
   * @param sessionId
   * @param page
   * @return
   * @throws ServiceException
   */
  Page<PsnCmdForm> getCooperatorByPage(PsnCmdForm form, String sessionId, Page<PsnCmdForm> page) throws Exception;

  /**
   * 获取合作者研究领域.
   * 
   * @param keywordJson
   * @param loadFlag
   * @param sessionId
   * @return
   * @throws ServiceException
   */
  List<ExpertDiscForm> getCooperatorDisc(PsnCmdForm form, String sessionId) throws Exception;

  List<KeywordSplit> getKeywordsListForChart(String title, String abs) throws Exception;
}
