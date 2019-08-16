package com.smate.center.batch.service.pdwh.pub;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubXmlSyncEvent;


/**
 * SNS-ROl成果XML同步服务.
 * 
 * @author yamingd
 * 
 */
public interface RolPubXmlSyncService {

  /**
   * 从SNS拉成果XML,提交成果时触发同步事件.
   * 
   * @param req ,同步请求事件实体
   * @throws ServiceException
   */
  void pullFromSNS(PubXmlSyncEvent req) throws ServiceException;

  /**
   * 往SNS推成果XML,R在ROL确认成果时，触发同步事件.
   * 
   * @param req ,同步请求事件实体
   * @throws ServiceException
   */
  void pushToSNS(PubXmlSyncEvent req) throws ServiceException;

  /**
   * 接受到个人提交成果消息数据.
   * 
   * @param snsPubId TODO
   * @param psnId TODO
   * @param insId TODO
   * @param xmlData TODO
   * @return TODO
   * 
   * @throws ServiceException
   */
  Long reserviceFromSns(Long snsPubId, Long psnId, Long insId, String xmlData) throws ServiceException;
}
