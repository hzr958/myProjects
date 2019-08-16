package com.smate.center.task.single.service.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;

/**
 * 封装对成果保存的封装.
 * <p/>
 * 不对WEB层开放的接口
 * 
 * @author ly
 * 
 */
public interface PublicationSaveService extends Serializable {
  /**
   * 保存新添加成果.
   * 
   * @param pub
   * @throws ServiceException
   */
  Publication savePubCreate(PubXmlDocument doc, PubXmlProcessContext context) throws ServiceException;

  /**
   * 保存成果编辑内容.
   * 
   * @param pub
   * @throws ServcieException
   */
  Publication savePubEdit(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws ServiceException;



  /**
   * @param lastPubId
   * @param size
   * @return
   * @throws ServiceException
   */
  List<Long> findRebuildPubId(int size) throws ServiceException;

  /**
   * 重新构造成果XML中的author_names字段并且同步更新publication表和group_pubs表中的author_names冗余字段 .
   * 
   * @param pubId
   * @throws ServiceException
   */
  void rebuildSnsPubAuthorNames(Long pubId) throws ServiceException;

  /**
   * 更新重构成果XML中的author_names字段任务列表.
   * 
   * @param pubId
   * @throws ServiceException
   */
  void updateTaskPubAuthor(Long pubId) throws ServiceException;

  void updateCiteTimeAndListInfo(Long pubId, Map map) throws ServiceException;
}
