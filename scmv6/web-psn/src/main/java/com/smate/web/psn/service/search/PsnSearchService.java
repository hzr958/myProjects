package com.smate.web.psn.service.search;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;

import com.smate.core.base.solr.model.QueryFields;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.model.search.PersonSearch;

/**
 * 人员检索服务接口.
 * 
 * @author xys
 *
 */
public interface PsnSearchService {

  /**
   * 获取人员列表.
   * 
   * @param page
   * @param queryFields
   * @return
   * @throws SolrServerException
   */
  Page<PersonSearch> getPsns(Page<PersonSearch> page, QueryFields queryFields) throws SolrServerException;

  /**
   * 获取左侧菜单.
   * 
   * @param queryFields
   * @return
   */
  Map<String, Object> getLeftMenu(QueryFields queryFields);

  /**
   * 获取人员其他信息.
   * 
   * @param page
   * @param queryFields
   * @return
   * @throws SolrServerException
   */
  String getPsnOtherInfo(String psnIdsStrs);

  /**
   * 批量获取头像url.
   * 
   * @param des3PsnIdsStr
   * @return
   */
  String getAvatarUrls(String des3PsnIdsStr);

  /**
   * 发现好友
   * 
   * @param page
   * @param queryFields
   * @return
   * @throws SolrServerException
   */
  public Page<PersonSearch> findPsn(Page<PersonSearch> page, QueryFields queryFields) throws SolrServerException;

  void getPsnsForMsg(Page<PersonSearch> page, QueryFields queryFields) throws SolrServerException;

  /**
   * 查找不需要显示添加好友按钮的人员ID
   * 
   * @param psnId
   * @return
   * @throws SolrServerException
   */
  public List<Long> findNotNeedFriendReqPsnIds(Long psnId) throws SolrServerException;

  Map<String, Object> getAvatarUrlsMap(String des3PsnIdsStr);

}
