package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;
import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubFulltextImageRefreshRol;
import com.smate.center.batch.model.rol.pub.PubFulltextRol;

/**
 * 成果全文Service.
 * 
 * @author pwl
 * 
 */
public interface PubFulltextRolService extends Serializable {

  /**
   * 保存成果全文转换成图片的刷新记录.
   * 
   * @param pubId
   * @param fulltextFileId
   * @param fulltextNode
   * @param fulltextFileExt
   * @throws ServiceException
   */
  public void savePubFulltextImageRefresh(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt)
      throws ServiceException;

  /**
   * 保存刷新记录.
   * 
   * @param pubFulltextImageRefresh
   * @throws ServiceException
   */
  public void savePubFulltextImageRefresh(PubFulltextImageRefreshRol pubFulltextImageRefresh) throws ServiceException;

  /**
   * 通过id删除刷新记录.
   * 
   * @param id
   * @throws ServiceException
   */
  public void delPubFulltextImageRefresh(Long id) throws ServiceException;

  /**
   * 批量获取需要刷新的数据.
   * 
   * @param startId
   * @param maxSize
   * @return
   * @throws ServiceException
   */
  public List<PubFulltextImageRefreshRol> getNeedRefreshData(int maxSize) throws ServiceException;

  /**
   * 转换图片并保存图片信息.
   * 
   * @param pubFulltextImageRefresh
   * @throws ServiceException
   */
  public void refreshData(PubFulltextImageRefreshRol pubFulltextImageRefresh) throws ServiceException;

  /**
   * 保存成果全文信息.
   * 
   * @param pubId
   * @param fulltextFileId
   * @param fulltextNode
   * @param fulltextFileExt
   * @throws ServiceException
   */
  public void savePubFulltext(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt)
      throws ServiceException;

  /**
   * 获取成果全文信息.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public PubFulltextRol getPubFulltextByPubId(Long pubId) throws ServiceException;

  /**
   * 获取成果全文图片地址.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String getFulltextImageUrl(Long pubId) throws ServiceException;

  /**
   * 删除全文信息，同时也会删除全文附件所转换的图片.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void deletePubFulltextByPubId(Long pubId) throws ServiceException;

  /**
   * 处理成果全文.
   * 
   * @param pubId
   * @param fulltextFileId
   * @param fulltextNode
   * @param fulltextFileExt
   * @throws ServiceException
   */
  public void dealPubFulltext(Long pubId, Long fulltextFileId, Integer fulltextNode, String fulltextFileExt)
      throws ServiceException;
}
