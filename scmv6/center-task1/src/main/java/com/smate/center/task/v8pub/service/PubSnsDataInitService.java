package com.smate.center.task.v8pub.service;

import java.util.Map;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.PubSimple;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

public interface PubSnsDataInitService {

  void constructData(Map<String, Object> dataMap) throws ServiceException;

  void initSNSPubDetail(Long pubId, PubSnsDetailDOM pubSnsDetail) throws ServiceException;

  /**
   * 初始化附件表数据
   * 
   * @param xmlDocument 原附件存在scm_pub_xml中
   * @throws ServiceException
   */
  void initAccessory(Long pubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化查重记录表数据
   * 
   * @param pubId 原查重记录数据，存在V_pub_simple_hash中
   * @throws ServiceException
   */
  void initDuplicate(Long pubId, PubSnsDetailDOM pubDetail) throws ServiceException;

  /**
   * 初始化成果全文表数据
   * 
   * @param pubId 原成果全文，存在pub_fulltext 中
   * @return
   * @throws ServiceException
   */
  Long initFulltext(Long pubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化关键词数据
   * 
   * @param pubId 存放在scm_pub_xml 中
   * @throws ServiceException
   */
  void initKeywords(Long pubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化成果引用次数记录表
   * 
   * @param pubId
   * @param xmlDocument
   * @throws ServiceException
   */
  void initCitations(Long pubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化个人与成果关系表
   * 
   * @param pubId
   * @param xmlDocument
   * @throws ServiceException
   */
  void initPsnPub(PubSimple pubSimple) throws ServiceException;

  /**
   * 初始化成果作者数据
   * 
   * @param pubId 原数据存放在pub_member中
   * @throws ServiceException
   */
  void initMembers(Long pubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化收录情况表数据
   * 
   * @param pubId 原数据存放在pub_list中
   * @throws ServiceException
   */
  void initSitation(Long pubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化成果主表数据 原数据存放在publication，v_pub_simple中 缺少数据可以从scm_pub_xml中取
   * 
   * @param pubId
   * @param xmlDocument
   * @throws ServiceException
   */
  void initPubSns(PubSimple pubSimple, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化群组成果拥有者字段
   * 
   * @throws ServiceException
   */
  void initGrpPubs(PubSimple pubSimple) throws ServiceException;

  /**
   * 初始化成果详情表数据 原始数据存放在publication，v_pub_simple中 缺少数据可以从scm_pub_xml中取
   * 
   * @param pubId
   * @param xmlDocument
   * @return 返回detailJson
   * @throws ServiceException
   */
  PubSnsDetailDOM initPubDetail(Long pubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化成果统计表数据 原数据存放在publication_statistics
   * 
   * @param pubId
   * @throws ServiceException
   */
  void initPubStatistics(Long pubId, Map<String, Object> dataMap) throws ServiceException;

  /**
   * 初始化成果赞记录表数据 原始数据存放在 dynamic_award_res和dynamic_award_psn
   * 
   * @return
   * @throws ServiceException
   */
  Integer initPubLike(Long pubId) throws ServiceException;

  /**
   * 初始化成果分享记录表数据 原始数据存放在 dynamic_share_res和dynamic_share_psn中
   * 
   * @return
   * @throws ServiceException
   */
  Integer initPubShare(Long pubId, boolean isChina) throws ServiceException;

  /**
   * 初始化成果评论记录数据 原始数据存放在pub_comments
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Integer initPubComment(Long pubId) throws ServiceException;

  /**
   * 初始化成果查看记录表数据 原数据存放在read_statistics
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Integer initPubView(Long pubId) throws ServiceException;

}
