package com.smate.web.dyn.service.pub;

import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.exception.DynException;


/**
 * 成果SERVICE接口.
 * 
 * @author zk
 * 
 */
public interface PublicationService {

  /**
   * 获取成果拥有者Id.
   * 
   * @param pubId
   * @return
   * @throws DynException
   */
  Publication getPubOwnerPsnIdOrStatus(Long pubId) throws DynException;

  /**
   * 获取成果评论相关
   * 
   * @param pubId
   * @return
   * @throws DynException
   */
  Publication getPubForComments(Long pubId) throws DynException;

  /**
   * 保存成果
   * 
   * @param pub
   * @throws DynException
   */
  void savePub(Publication pub) throws DynException;


  /**
   * 获取成果所有者.
   * 
   * @param pubId
   * @return
   * @throws DynException
   */
  Long getPubOwner(Long pubId) throws DynException;

  /**
   * 更新成果.
   * 
   * @param publication
   * @return
   * @throws DynException
   */
  public void updatePub(Publication pub) throws DynException;

}
