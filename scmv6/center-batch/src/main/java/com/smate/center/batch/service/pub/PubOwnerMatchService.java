package com.smate.center.batch.service.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PublicationPdwh;

/**
 * 成果作者匹配表，用于确定用户与作者的关系.
 * 
 * @author liqinghua
 * 
 */
public interface PubOwnerMatchService extends Serializable {

  /**
   * 成果所有人作者与成果匹配.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void pubOwnerMatch(Long pubId, Long psnId) throws ServiceException;

  /**
   * 删除数据.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void delPubOwnerMatch(Long pubId) throws ServiceException;

  /**
   * 通过成果作者匹配表查找isi_pubId
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Long> findPdwhIsiId(Long psnId) throws ServiceException;

  /**
   * 通过成果作者匹配表查找cnki_pubId
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  List<Long> findPdwhCnkiId(Long psnId) throws ServiceException;

  /**
   * 通过成果作者匹配表
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public List<PublicationPdwh> findByPsnId(Long psnId) throws ServiceException;

  /**
   * 成果所有人是否与成果作者匹配.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public boolean isPubOwnerMatch(Long pubId, Long psnId) throws ServiceException;
}
