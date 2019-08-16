package com.smate.center.batch.service.pub;

import java.util.List;

import com.smate.center.batch.form.pub.PubReaderForm;
import com.smate.center.batch.model.sns.pub.PubReader;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.model.Page;


/**
 * 成果读者推荐.
 * 
 * @author lichangwen
 * 
 */
public interface PubReaderService {

  /**
   * @param list
   * @throws BatchTaskException
   */
  void save(List<PubReader> list) throws BatchTaskException;

  /**
   * @param pubId
   * @throws BatchTaskException
   */
  void delete(Long pubId) throws BatchTaskException;

  /**
   * @param lastId
   * @param batchSize
   * @return
   * @throws BatchTaskException
   */
  List<Long> findTaskPubReadIds(int batchSize) throws BatchTaskException;

  /**
   * @param pubId
   * @param status
   * @throws BatchTaskException
   */
  void updateTaskPubReadIds(Long pubId, int status) throws BatchTaskException;

  /**
   * @param pubId
   * @throws BatchTaskException
   */
  void saveTaskPubReadIds(Long pubId) throws BatchTaskException;

  /**
   * @param pubId
   * @return
   * @throws BatchTaskException
   */
  List<Long> getPubReaders(Long pubId, Long psnId) throws BatchTaskException;

  /**
   * @param page
   * @param pubId
   * @return
   * @throws BatchTaskException
   */
  @SuppressWarnings("rawtypes")
  Page getPubReaders(Page<PubReaderForm> page, Long pubId) throws BatchTaskException;

  /**
   * @param pubId
   * @param psnId
   * @throws BatchTaskException
   */
  void ajaxDelPubReader(Long pubId, Long psnId) throws BatchTaskException;
}
