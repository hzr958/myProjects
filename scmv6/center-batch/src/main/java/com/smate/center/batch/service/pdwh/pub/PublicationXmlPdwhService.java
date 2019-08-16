package com.smate.center.batch.service.pdwh.pub;

import java.io.Serializable;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PdwhPublicationAll;
import com.smate.center.batch.model.sns.pub.PublicationXml;

/**
 * 成果XML基本服务，提供与当前人、单位无关的操作.
 * 
 * @author changwenli
 */
public interface PublicationXmlPdwhService extends Serializable {

  /**
   * 获取基准库成果xml.
   * 
   * @param pubId
   * @param dbid
   * @return
   * @throws ServiceException
   */
  PublicationXml getPdwhPubXmlById(Long pubId, Integer dbid) throws ServiceException;

  /**
   * 获取基准库成果详情.
   * 
   * @param pubId
   * @param dbid
   * @return
   * @throws ServiceException
   */
  PdwhPublicationAll getPdwhPublicationAll(Long pubId, Integer dbid) throws ServiceException;

}
