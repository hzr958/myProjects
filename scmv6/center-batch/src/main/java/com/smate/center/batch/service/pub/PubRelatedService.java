package com.smate.center.batch.service.pub;

import java.util.List;

import com.smate.center.batch.model.sns.pub.PubRelated;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.model.Page;


/**
 * 成果相关文献.
 * 
 * @author lichangwen
 * 
 */
public interface PubRelatedService {

  /**
   * @param pubPubAllRelated
   * @throws BatchTaskException
   */
  void save(List<PubRelated> list) throws BatchTaskException;

  /**
   * @param pubId
   * @throws BatchTaskException
   */
  void delete(Long pubId) throws BatchTaskException;

  /**
   * 根据pubId获取相关文献pubAllIds.
   * 
   * @param page
   * @param pubId
   * @return
   * @throws BatchTaskException
   */
  @SuppressWarnings("rawtypes")
  List<Long> findPubAllIdsByPubId(Page page, Long pubId) throws BatchTaskException;

  /**
   * @param lastId
   * @param batchSize
   * @return
   * @throws BatchTaskException
   */
  List<Long> findTaskPubRelatedIds(int batchSize) throws BatchTaskException;

  /**
   * @param pubId
   * @param status
   * @throws BatchTaskException
   */
  void updateTaskPubRelatedIds(Long pubId, int status) throws BatchTaskException;

  /**
   * @param pubId
   * @throws BatchTaskException
   */
  void saveTaskPubRelatedIds(Long pubId) throws BatchTaskException;
}
