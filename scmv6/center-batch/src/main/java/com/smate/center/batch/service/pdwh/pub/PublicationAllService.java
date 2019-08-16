package com.smate.center.batch.service.pdwh.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PsnPubAllRecommend;
import com.smate.center.batch.model.pdwh.pub.RefKwForm;

/**
 * 基准库成果服务
 * 
 * @author warrior
 * 
 */
public interface PublicationAllService {

  final static String AUTHOR_NAME_SPLIT_CODE_ZH = "；";// 作者名字的分隔符(中文).
  final static String AUTHOR_NAME_SPLIT_CODE_EN = "; ";// 作者名字的分隔符(英文).
  final static int SAME_KEY_COUNT_SIMPLE = 50;// 内容和检索条件相同时的加分分数(站内论文检索).
  final static int CON_KEY_COUNT_SIMPLE = 10;// 内容包含检索条件时的加分分数(站内论文检索).
  final static int SAME_KEY_COUNT_ADVANCE = 80;// 内容和检索条件相同时的加分分数(高级检索).
  final static int CON_KEY_COUNT_ADVANCE = 20;// 内容包含检索条件时的加分分数(高级检索).
  final static int SAME_KEY_COUNT_PRIORITY = 200;// 重点比对内容相同时的加分分数(如标题和作者).
  final static int TOP_TEN_KEY_COUNT = 1000;// 前十名加分分数.
  final static int MAX_RESULT_COUNT = 100;// 论文检索的最多结果记录数.

  /**
   * 获取成果来源_pdwh
   * 
   * @param pubId
   * @param dbid
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  Map getBriefDesc(Long pubId, Integer dbid) throws ServiceException;

  /**
   * @param psnId
   * @throws ServiceException
   */
  void deletePsnPubAllRecommend(Long psnId) throws ServiceException;

  /**
   * 获取人员之前的推荐数据
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<PsnPubAllRecommend> getPsnPuballOldList(Long psnId, int language) throws ServiceException;

  /**
   * @param psnId
   * @throws ServiceException
   */
  void deletePsnPubAllRecommend(Long psnId, List<PsnPubAllRecommend> list) throws ServiceException;

  /**
   * @param psnId
   * @param puballIds
   * @throws ServiceException
   */
  void savePsnPubAllRecommend(List<PsnPubAllRecommend> list) throws ServiceException;

  /**
   * 给人员推荐基准文献，关键词匹配
   * 
   * @param kwHashList
   * @return
   * @throws ServiceException
   */
  List<RefKwForm> findPubAllByKwHashByRecommend(List<Long> kwHashList) throws ServiceException;

}
