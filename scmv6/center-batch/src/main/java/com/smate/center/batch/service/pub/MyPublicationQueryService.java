package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.model.Page;

/**
 * 我的成果管理查询专用服务.
 * 
 * @author yamingd
 */
public interface MyPublicationQueryService {

  @SuppressWarnings("rawtypes")
  List<Map> findRecordMap(int articleType);

  @SuppressWarnings("rawtypes")
  Map queryYearsMap(int articleType);

  PsnStatistics getPsnStatistics(Long psnId);

  List<Publication> findPubsForNsfc(List<Long> pubIds) throws ServiceException;

  void wrapPopulateData(Page<Publication> page, boolean isFillErrorField, boolean isViewUploadFulltext)
      throws ServiceException;

  /**
   * 加载查询结果集的类别名称.
   * 
   * @param pe
   * @throws ServiceException
   */
  void wrapQueryResultTypeName(Publication pe) throws ServiceException;

  void wrapQueryResultTypeName(List<Publication> result) throws ServiceException;

  void wrapPopulateDataItems(Publication item, boolean isFillErrorField, boolean isViewUploadFulltext)
      throws ServiceException;

  /**
   * 1.人员ID，收藏夹ID，成果IDS. 2.根据结果集，读取xml中的字段 包括:来源(brief_desc),标题(zh_title,en_title
   * ),来源url(source_url),全文附件，全文链接(fulltext_url) 作者名(author_names).
   * 
   * @throws ServiceException ServiceException
   */
  List<Publication> queryOutput(Long psnId, List<Long> pubIds) throws ServiceException;

  /**
   * 统计人员成果引用总数.
   * 
   * @return
   * @throws ServiceException
   */
  Integer getTotalCiteTimes() throws ServiceException;

  /**
   * 统计某个人员成果引用总数.
   * 
   * @return
   * @throws ServiceException
   */
  Integer getTotalCiteTimes(Long psnId) throws ServiceException;

  /**
   * 得到某个人的h指数.
   * 
   * @return
   * @throws ServiceException
   */
  Integer getHindex(Long psnId) throws ServiceException;

}
