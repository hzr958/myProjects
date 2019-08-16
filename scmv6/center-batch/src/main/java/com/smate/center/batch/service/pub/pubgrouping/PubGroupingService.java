package com.smate.center.batch.service.pub.pubgrouping;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PublicationRolPdwh;
import com.smate.center.batch.model.sns.pub.PubGrouping;
import com.smate.center.batch.model.sns.pub.PublicationPdwh;

/**
 * 成果分组 接口服务.
 * 
 * @author tsz
 * 
 */
public interface PubGroupingService {

  public void pubGroupingFor(List<PublicationPdwh> list);

  /**
   * 根据成果分组 id获取相关sns成果id
   * 
   * @param groupId
   * @return
   * @throws ServiceException
   */
  List<Long> findSnsPubIdsByGroupId(Long groupId) throws ServiceException;

  /**
   * 获取分组id
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  Long findPubGroupId(Long pubId) throws ServiceException;

  /**
   * 根据基准库成果id获取成果分组内容
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  PubGrouping findPubGroup(Long pubId) throws ServiceException;

  /**
   * 新建分组
   * 
   * @param pubGroup
   * @return
   * @throws ServiceException
   */
  PubGrouping addPubPdwhGroup(PubGrouping pubGroup) throws ServiceException;

  /**
   * 保存成果分组信息
   * 
   * @param pubId
   * @param pdwhPubId
   * @throws ServiceException
   */
  void pubGroupingBuilder(PublicationPdwh pubPdwh) throws Exception;

  /**
   * 获取初始化数据
   * 
   * @param maxsize
   * @return
   * @throws ServiceException
   */
  List<PublicationPdwh> getInitList(int maxsize) throws ServiceException;

  /**
   * 判断是否有其他版本相关成果.
   * 
   * @author xys
   * @param pubId
   * @param psnId
   * @return
   * @throws ServiceException
   */
  boolean checkIsHasSnsOtherRelPub(Long pubId, Long psnId) throws ServiceException;

  /**
   * 删除分组记录
   */
  public void delGroupingByPubId(Long pubId) throws Exception;

  /**
   * 保存成果分组信息
   * 
   * @param pubId
   * @param pdwhPubId
   * @throws ServiceException
   */
  public void pubGroupingBuilder(PublicationRolPdwh pdwh);
}
