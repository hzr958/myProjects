package com.smate.center.task.service.pdwh.quartz;

import java.io.Serializable;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.pdwh.quartz.PdwhPublicationAll;
import com.smate.center.task.model.sns.quartz.PublicationXml;

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

  String getXmlStringByPubId(Long pubId);

}
