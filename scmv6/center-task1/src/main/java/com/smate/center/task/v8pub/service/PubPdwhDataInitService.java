package com.smate.center.task.v8pub.service;

import java.util.Map;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.PdwhPublication;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public interface PubPdwhDataInitService {

  void constructData(Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化查重记录表数据
   * 
   * @param pdwhPubId 原查重记录数据，存在pdwh_pub_dup中
   * @param isChina
   * @throws ServiceException
   */
  void initDuplicate(PubPdwhDetailDOM pubDetail) throws ServiceException;

  /**
   * 初始化成果全文表数据 原成果全文，存放在PDWH_FULLTEXT_FILE（存fileId）和PDWH_FULLTEXT_IMG（全文图片地址）中
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  Long initFulltext(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化关键词数据
   * 
   * @param pdwhPubId 存放在pdwh_pub_keywords 中
   * @throws ServiceException
   */
  void initKeywords(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化成果引用次数记录表 原数据存放在pdwh_pub_cited_times中
   * 
   * @param pdwhPubId
   * @param xmlDocument
   * @throws ServiceException
   */
  void initCitations(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化成果作者数据
   * 
   * @param pdwhPubId 原数据存放在pdwh_pub_xml中
   * @throws ServiceException
   */
  void initMembers(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化收录情况表数据
   * 
   * @param pdwhPubId 原数据存放在PDWH_PUB_SOURCE_DB中
   * @throws ServiceException
   */
  void initSitation(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化成果主表数据 原数据存放在pdwh_publication中，缺少数据可以从pdwh_pub_xml中取
   * 
   * @param xmlDocument
   * @param pdwhPublication TODO
   * 
   * @throws ServiceException
   */
  void initPubPdwh(Map<String, Object> dataMap, PdwhPublication pdwhPublication) throws ServiceException;

  /**
   * 初始化成果详情表数据 原始数据存放在pdwh_pub_xml中
   * 
   * @param pdwhPubId
   * @param xmlDocument
   * @throws ServiceException
   */
  PubPdwhDetailDOM initPubDetail(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化数据至备份表
   * 
   * @param pdwhDetail
   * @throws ServiceException
   */
  void initPdwhPubDetail(Long pubId, PubPdwhDetailDOM pdwhDetail) throws ServiceException;

  /**
   * 初始化成果统计表数据 原数据存放在PDWH_PUB_STATISTICS
   * 
   * @param pdwhPubId
   * @throws ServiceException
   */
  void initPubStatistics(Long pdwhPubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化成果赞记录表数据 原始数据存放在PDWH_PUB_AWARD中
   * 
   * @return 赞数
   * @throws ServiceException
   */
  Integer initPubLike(Long pdwhPubId) throws ServiceException;

  /**
   * 初始化成果分享记录表数据 原始数据存放在PDWH_PUB_SHARE中
   * 
   * @return 分享数
   * @throws ServiceException
   */
  Integer initPubShare(Long pdwhPubId, boolean isChina) throws ServiceException;

  /**
   * 初始化成果评论记录数据 原始数据存放在PDWH_PUB_COMMENTS
   * 
   * @param pdwhPubId
   * @return 评论数
   * @throws ServiceException
   */
  Integer initPubComment(Long pdwhPubId) throws ServiceException;

  /**
   * 初始化成果查看记录表数据 原数据存放在PDWH_READ_STATISTICS
   * 
   * @param pdwhPubId
   * @return 浏览数
   * @throws ServiceException
   */
  Integer initPubView(Long pdwhPubId) throws ServiceException;

  /**
   * 处理基准库作者信息
   * 
   * @param pubPdwhDetailDOM
   */
  void dealWithMember(Long pdwhPubId);

}
