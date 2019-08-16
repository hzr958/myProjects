package com.smate.center.batch.service.rol.pub;

import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.pub.PubConfirmSyncMessage;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.rol.pub.PubMemberRol;
import com.smate.center.batch.model.rol.pub.PubPsnRol;
import com.smate.core.base.utils.model.Page;


/**
 * 成果指派人服务.
 * 
 * @author yamingd
 * 
 */
public interface PubRolPersonService {

  /**
   * 保存指派结果,返回PubPsnRol(JSON)结果.
   * 
   * @param pmId 对应到pub-member.id
   * @param psnId
   * @param pubId
   * @throws ServiceException
   */
  String saveAssign(Long pmId, Long psnId, Long pubId) throws ServiceException;

  /**
   * 删除指派.
   * 
   * @param assignId pubPsn主键.
   * @throws ServiceException
   */
  void removeAssign(Long assignId) throws ServiceException;

  /**
   * 删除人员时，删除所有指派.
   * 
   * @param psnId
   * @param insId
   * 
   * @throws ServiceException
   */
  void removeAssignByPsnId(Long psnId, Long insId) throws ServiceException;

  /**
   * 删除成果时，删除所有与部门人员关联的指派.
   * 
   * @param psnId
   * @param insId
   * 
   * @throws ServiceException
   */
  void removeAssignByUnit(Long pubId, Long insId, Long unitId) throws ServiceException;

  /**
   * 删除指派，通过成果ID以及人员ID.
   * 
   * @param pubId
   * @param psnId
   * @throws ServiceException
   */
  void removeAssignByPubPsn(Long pubId, Long psnId) throws ServiceException;

  /**
   * 加载成果成员表，作指派界面数据. 需要加载:PubMemberRol, PubPsnRol, RolPsnIns表的数据.
   * 
   * @param pubId
   * @throws ServiceException
   */
  List<PubMemberRol> loadPubMembersDetail(Long pubId) throws ServiceException;

  /**
   * 获取pmID为空的成果的人员指派关系数据.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  List<PubPsnRol> loadEmptyPmPubPsnRolDetail(Long pubId) throws ServiceException;

  /**
   * 个人确认成果后，将确认成果反馈给单位.
   * 
   * @param msg
   * @throws ServiceException
   */
  void receiveConfirmMessage(PubConfirmSyncMessage msg) throws ServiceException;

  /**
   * 设置成果已完成匹配.
   * 
   * @param pubIdsStr
   * @throws ServiceException
   */
  void updatePubAuthorState(String pubIdsStr) throws ServiceException;

  /**
   * 个人收到Xml成果，并保存如个人库，返回SNSPubID.
   * 
   * @param msg
   * @throws ServiceException
   */
  void pubConfirmSuccessSyncXml(PubConfirmSyncMessage msg) throws ServiceException;

  /**
   * 查询成果作者关联个数.
   * 
   * @param pubIds
   * @param insIds
   * @return
   * @throws ServiceException
   */
  Map<String, Long> getPubPsnNum(List<Long> pubIds, Long insId) throws ServiceException;

  /**
   * 过滤关联到了人员的成果ID列表.
   * 
   * @param pubIds
   * @param insId
   * @return
   * @throws ServiceException
   */
  List<Long> filterLinkPubPsn(List<Long> pubIds, Long insId) throws ServiceException;

  /**
   * 过滤关联到了人员的成果作者ID列表.
   * 
   * @param pmIds
   * @return
   * @throws ServiceException
   */
  List<Long> filterLinkPmPsn(List<Long> pmIds) throws ServiceException;

  /**
   * 查询人员关联到的成果数.
   * 
   * @param psnIds
   * @param insId
   * @return
   * @throws ServiceException
   */
  Map<String, Long> getPsnPubNum(List<Long> psnIds, Long insId) throws ServiceException;

  /**
   * 加载成果关联到的人员数据.
   * 
   * @param pubId
   * @throws ServiceException
   */
  List<PubPsnRol> loadPubPsnRolDetail(Long pubId) throws ServiceException;

  /**
   * 加载成果作者关联到的人员数据.
   * 
   * @param pmId
   * @throws ServiceException
   */
  List<PubPsnRol> loadPmPsnRolDetail(Long pmId) throws ServiceException;

  /**
   * 成果确认标记重新发送.
   * 
   * @param pubId
   * @throws ServiceException
   */
  public void updatePubConfirmReSend(Long pubId) throws ServiceException;

  List<PubMemberRol> getPubMembers(Long pubId) throws ServiceException;

  /**
   * 获取论文作者
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  List<RolPsnIns> getPubAuthors(Long pubId) throws ServiceException;

  /**
   * 更多作者
   * 
   * @param page
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Page<RolPsnIns> findSnsPubAuthorListByPubId(Page page, Long pubId) throws ServiceException;

  /**
   * 获取论文作者数
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  int getPubAuthorsCount(Long pubId) throws ServiceException;
}
