package com.smate.center.task.service.rcmd.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.rcmd.quartz.PubConfirmRolPubDao;
import com.smate.center.task.dao.rcmd.quartz.PubConfirmRolPubDupFieldsDao;
import com.smate.center.task.dao.rcmd.quartz.PubConfirmRolPubMembersDao;
import com.smate.center.task.dao.rcmd.quartz.PubConfirmRolPubPdwhDao;
import com.smate.center.task.dao.rcmd.quartz.PublicationConfirmDao;
import com.smate.center.task.dao.sns.quartz.PubConfirmKnowFieldsDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.InsPortalRcmd;
import com.smate.center.task.model.rcmd.quartz.PubAssignSyncMessageEnum;
import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPub;
import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPubDupFields;
import com.smate.center.task.model.rcmd.quartz.PubConfirmRolPubMember;
import com.smate.center.task.model.rcmd.quartz.PubConfirmSyncMessage;
import com.smate.center.task.model.rcmd.quartz.PubConfirmSyncMessageEnum;
import com.smate.center.task.model.rcmd.quartz.PublicationConfirm;
import com.smate.center.task.model.rol.quartz.PubAssignSyncMessage;
import com.smate.center.task.model.sns.quartz.PubConfirmKnowFields;
import com.smate.center.task.rcmd.jms.PendingCfmPubNumMessageProducer;
import com.smate.center.task.rcmd.jms.PubConfirmXmlSyncProducer;
import com.smate.center.task.single.constants.RcmdDynamicConstants;

/**
 * 成果确认业务逻辑.
 * 
 * @author zjh
 *
 */
@Service("publicationConfirmService")
@Transactional(rollbackFor = Exception.class)
public class PublicationConfirmServiceImpl implements PublicationConfirmService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationConfirmDao publicationConfirmDao;
  @Autowired
  private PubConfirmRolPubDao pubConfirmRolPubDao;
  @Autowired
  private PubConfirmRolPubMembersDao pubConfirmRolPubMembersDao;
  @Autowired
  private DynRecomPsnTaskService dynRecomPsnTaskService;
  @Autowired
  private InsPortalManager insPortalManager;
  @Autowired
  private PubConfirmXmlSyncProducer pubConfirmXmlSyncProducer;
  @Autowired
  private PubConfirmKnowFieldsDao pubConfirmKnowFieldsDao;
  @Autowired
  private PubConfirmRolPubPdwhDao pubConfirmRolPubPdwhDao;
  @Autowired
  private PubConfirmRolPubDupFieldsDao pubConfirmRolPubDupFieldsDao;
  @Autowired
  private PendingCfmPubNumMessageProducer pendingCfmPubNumMessageProducer;
  @Autowired
  private PubDupService pubDupService;

  @Override
  public List<PublicationConfirm> loadReconfirmList(Long startId) throws ServiceException {
    List<PublicationConfirm> pubConfirmList = new ArrayList<PublicationConfirm>();
    try {
      pubConfirmList = publicationConfirmDao.loadReconfirmList(startId);
    } catch (Exception e) {
      logger.error("个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认，加载符合条件的数据错误", e);
      throw new ServiceException("个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认，加载符合条件的数据错误", e);
    }
    return pubConfirmList;
  }

  @Override
  public void updateConfirmSyncNum(PublicationConfirm pubConfirm) throws ServiceException {
    pubConfirm.setSyncNum(pubConfirm.getSyncNum() + 1);
    publicationConfirmDao.save(pubConfirm);
  }

  @Override
  public void reconfirmPublication(PublicationConfirm pubConfirm) throws ServiceException {
    PubConfirmRolPub pubDetail = pubConfirmRolPubDao.get(pubConfirm.getRolPubId());
    try {
      Integer confirmResult = pubConfirm.getConfirmResult(); // 1是我的成果，2不是我的成果
      Long confirmPmId = pubConfirm.getPmId();// 确认的pub_member id
      Integer dupAction = pubConfirm.getDupStatus(); // 1不重复，覆盖2,新增3,跳过即不导入4
      if (pubDetail == null) {
        return;
      }
      Integer publishYear = pubDetail.getPublishYear();// 发表年份
      Integer publishMonth = pubDetail.getPublishMonth();// 发表月份

      if (PublicationConfirmService.RESULT_CONFIRM_INT.equals(confirmResult)) {
        PubConfirmSyncMessageEnum confirmResultEnum = PubConfirmSyncMessageEnum.CONFIRM;
        this.updateConfirmSyncNum(pubConfirm);
        String publishDate = builderPublishDate(publishYear, publishMonth, null);// 发表年份
        Map<String, String> pubList = builderPubList(pubDetail.getPubList()); // 收录情况
        if (dupAction == 4) {// 用户选择跳过，直接备份到历史记录表中
          this.bakToHistory(pubConfirm);
        } else {
          sendPubConfirmSyncMessage(pubConfirm.getInsId(), pubConfirm.getPsnId(), pubConfirm.getInsAssignId(),
              pubConfirm.getRolPubId(), 1, null, confirmPmId, pubList, publishDate, confirmResultEnum);
        }
        dynRecomPsnTaskService.saveDynRePsnTask(pubConfirm.getPsnId(), RcmdDynamicConstants.DYN_RECOMMEND_TYPE_PUB);
      }

    } catch (Exception e) {
      logger.error("个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认失败", e);
      throw new ServiceException("个人确认成果后，可能ROL那边系统错误，导致成果没有确认，需要程序自动确认失败.", e);

    }

  }

  private void sendPubConfirmSyncMessage(Long insId, Long psnId, Long insAssignId, Long rolPubId, int confirmResult,
      String xml, Long confirmPmId, Map<String, String> pubList, String publishDate,
      PubConfirmSyncMessageEnum actionType) throws ServiceException {
    try {
      InsPortalRcmd insportal = insPortalManager.getInsPortalByInsId(insId);
      if (insportal == null) {
        return;
      }
      PubConfirmSyncMessage message = new PubConfirmSyncMessage();
      message.setPsnId(psnId);
      message.setInsId(insId);
      message.setAssignId(insAssignId);
      message.setPmId(confirmPmId);
      message.setPubList(pubList);
      message.setInsPubId(rolPubId);
      message.setPubXml(xml);
      message.setNodeId(insportal.getRolNodeId());
      message.setConfirmResult(confirmResult);
      message.setNodeId(insportal.getRolNodeId());
      message.setActionType(actionType);
      pubConfirmXmlSyncProducer.pubConfirmSyncMessage(message);
    } catch (Exception e) {
      logger.error("查找单位节点失败" + e);
      throw new ServiceException("查找单位节点失败" + ":发送成果确认消失失败", e);

    }

  }

  /**
   * 收录情况.
   * 
   * @param jpubList
   * @return
   */
  private Map<String, String> builderPubList(String pubList) {
    if (StringUtils.isBlank(pubList)) {
      return null;
    }
    Map<String, String> pubListMap = new HashMap<String, String>();
    String[] pubArr = pubList.split(",");
    for (String pubSource : pubArr) {
      pubListMap.put("list_" + pubSource.toLowerCase(), "1");
    }
    return pubListMap;


  }

  private String builderPublishDate(Integer publishYear, Integer publishMonth, Integer publishDay) {
    String publishDate = "";

    if (publishYear == null) {
      return null;
    }
    if (publishMonth != null) {
      publishDate = publishYear + "-" + publishMonth;

      if (publishDay != null) {
        publishDate = publishDate + "-" + publishDay;
      }
    } else {
      publishDate = publishYear.toString();
    }
    return publishDate;
  }

  @Override
  public void bakToHistory(PublicationConfirm confirm) throws ServiceException {
    if (confirm == null) {
      return;
    }
    try {
      publicationConfirmDao.remove(confirm.getId());
      publicationConfirmDao.removeHi(confirm.getId());
      publicationConfirmDao.saveHistory(confirm);

    } catch (Exception e) {
      logger.error("备份确认结果到历史记录中错误", e);
      throw new ServiceException("备份确认结果到历史记录中错误", e);

    }
  }

  @Override
  public void receivePubAssignFromInsSyncMessage(PubAssignSyncMessage msg, PubAssignSyncMessageEnum assign)
      throws ServiceException {
    if (PubAssignSyncMessageEnum.ASSIGN.equals(assign)) {
      savePubAssign(msg);
    } else if (PubAssignSyncMessageEnum.DIS_ASSIGN.equals(assign)) {// 单位删除指派
      deletePubAssign(msg);
    } else if (PubAssignSyncMessageEnum.SYC_ASSIGN_XML_ERROR.equals(assign)) {
      savePubAssignError(msg);
    }

    dynRecomPsnTaskService.saveDynRePsnTask(msg.getPsnId(), RcmdDynamicConstants.DYN_RECOMMEND_TYPE_PUB);

  }

  public void savePubAssign(PubAssignSyncMessage msg) {
    deletePubAssign(msg);
    PublicationConfirm confirm = new PublicationConfirm();
    confirm.setAssignSeqNo(msg.getSeqNo());
    confirm.setPmId(msg.getPmId());
    confirm.setConfirmResult(0);
    confirm.setInsId(msg.getInsId());
    confirm.setPsnId(msg.getPsnId());
    confirm.setRolPubId(msg.getInsPubId());
    confirm.setInsAssignId(msg.getAssignId());
    confirm.setDupStatus(0);
    confirm.setSyncStatus(0);
    confirm.setAssignScore(msg.getAssignScore());
    confirm.setAutoComfirm(0);
    try {
      Long id = publicationConfirmDao.savePublicationConfirm(confirm);
      PubConfirmRolPub pubDetail = msg.getPubConfirmRolPub();

      /**
       * 判断成果详情是否更新.
       */
      if (pubConfirmRolPubDao.isPubUpdate(pubDetail.getRolPubId(), pubDetail.getUpdateDate())) {
        if (msg.getIsSaveRcmdPubConfirm() == 0) {// scm-6576成果指派给多个人，如果已经保存过，就不再保存
          pubConfirmRolPubMembersDao.deleteMembers(pubDetail.getRolPubId());
          pubConfirmRolPubDao.deleteById(pubDetail.getRolPubId());
          // 成果详情
          pubConfirmRolPubDao.save(pubDetail);

          List<PubConfirmRolPubMember> members = msg.getPubConfirmRolPub().getMemberList();
          if (CollectionUtils.isNotEmpty(members)) {
            for (PubConfirmRolPubMember pm : members) {
              pubConfirmRolPubMembersDao.save(pm);
            }
          }

          if (pubDetail.getPdwh() != null) {
            pubConfirmRolPubPdwhDao.deletePdwh(pubDetail.getRolPubId());
            pubConfirmRolPubPdwhDao.save(pubDetail.getPdwh());
            // psnRefreshInfoMessageProducer.syncRefreshPsnPub(message.getPsnId(),
            // null, 0, 1);
          }
        }

      }

      pubConfirmRolPubDupFieldsDao.deleteByPubPsnId(pubDetail.getRolPubId(), msg.getPsnId());// 先清理相同的查重信息
      // 保存查重信息
      if (msg.getPubDupFields() != null) {
        msg.getPubDupFields().setOwnerId(msg.getPsnId());
        PubConfirmRolPubDupFields dupFields =
            new PubConfirmRolPubDupFields(pubDetail.getRolPubId(), msg.getPubDupFields());
        this.pubConfirmRolPubDupFieldsDao.save(dupFields);
      }

      // 保存好友推荐信息
      PubConfirmKnowFields knowFields = new PubConfirmKnowFields(id, msg.getPsnId(), confirm.getRolPubId(),
          pubDetail.getTypeId(), pubDetail.getZhTitleHash(), pubDetail.getEnTitleHash());
      pubConfirmKnowFieldsDao.save(knowFields);

    } catch (Exception e) {
      logger.error("保存单位指派成果失败");
      throw new ServiceException("保存单位指派成果失败.", e);
    }
  }

  public void deletePubAssign(PubAssignSyncMessage msg) throws ServiceException {
    try {
      PublicationConfirm existConfirm =
          publicationConfirmDao.getPublicationConfirmRol(msg.getPsnId(), msg.getInsPubId());
      if (existConfirm != null) {
        publicationConfirmDao.remove(existConfirm.getId());
        pubConfirmKnowFieldsDao.deleteById(existConfirm.getId());
        publicationConfirmDao.removeHi(existConfirm.getId());
      }

    } catch (Exception e) {
      logger.error("删除指派信息错误", e);
      throw new ServiceException("删除指派信息错误", e);

    }
  }


  public void savePubAssignError(PubAssignSyncMessage msg) {
    try {

      PublicationConfirm existConfirm =
          publicationConfirmDao.getPublicationConfirmRol(msg.getPsnId(), msg.getInsPubId());
      if (existConfirm != null) {
        // 同步成果XML失败类型，1单位删除了指派关系、2成果人员被删除
        int syncStatus = 0;
        switch (msg.getErrorType()) {
          case 1:
            syncStatus = 3;
            break;
          case 2:
            syncStatus = 4;
            break;
          default:
            syncStatus = 0;
        }
        existConfirm.setSyncStatus(syncStatus);
        publicationConfirmDao.save(existConfirm);
      }
    } catch (Exception e) {
      logger.error("判断指派的成果是否存在失败", e);
      throw new ServiceException("判断指派的成果是否存在失败.", e);

    }

  }

  @Override
  public void autoComfirmDupPub(PubAssignSyncMessage msg) throws ServiceException {
    try {
      PublicationConfirm confirm = publicationConfirmDao.getPublicationConfirmRol(msg.getPsnId(), msg.getInsPubId());
      PubConfirmRolPubDupFields dupFields =
          pubConfirmRolPubDupFieldsDao.getByPubPsnId(msg.getInsPubId(), msg.getPsnId());
      autoComfirmStrictDupPub(confirm, dupFields);
    } catch (Exception e) {
      logger.error("如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担失败", e);
      throw new ServiceException("如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担失败.", e);
    }

  }

  public void autoComfirmStrictDupPub(PublicationConfirm pubConfirm, PubConfirmRolPubDupFields dupFields)
      throws ServiceException {
    if (pubConfirm == null || !PublicationConfirmService.RESULT_UNCONFIRM_INT.equals(pubConfirm.getConfirmResult())) {
      return;
    }
    Map<Integer, List<Long>> dupMap = null;
    if (dupFields != null) {
      // scm-6695
      dupMap = pubDupService.getDupPub(dupFields.wrapPubDupFields());
    }

    // 未查到严格重复，退出
    if (MapUtils.isEmpty(dupMap)) {
      return;
    }
    // 单位未指定指派哪个作者
    if (pubConfirm.getAssignSeqNo() == null) {
      return;
    }
    Long pmId = pubConfirmRolPubMembersDao.getRolPmIdBySeqNo(pubConfirm.getRolPubId(), pubConfirm.getAssignSeqNo());
    // 修改确认结果
    Long dupId = null;
    if (CollectionUtils.isNotEmpty(dupMap.get(1))) {
      dupId = dupMap.get(1).get(0);
      pubConfirm.setDupPubIds(dupId.toString());
    } else if (CollectionUtils.isNotEmpty(dupMap.get(2))) {
      dupId = dupMap.get(2).get(0);
      pubConfirm.setDupPubIds(dupId.toString());
    }
    pubConfirm.setSnsPubId(dupId);
    pubConfirm.setDupStatus(1);
    pubConfirm.setConfirmSeqNo(pubConfirm.getAssignSeqNo());
    pubConfirm.setPmId(pmId);
    pubConfirm.setConfirmResult(PublicationConfirmService.RESULT_CONFIRM_INT);
    pubConfirm.setConfirmDate(new Date());
    pubConfirm.setAutoComfirm(1);
    publicationConfirmDao.save(pubConfirm);
    // 移动到历史记录表
    this.bakToHistory(pubConfirm);

    if (CollectionUtils.isNotEmpty(dupMap.get(1))) {
      try {
        Map map = pubConfirmRolPubMembersDao.queryCfmRolPubBypubId(pubConfirm.getRolPubId());
      } catch (Exception e) {
        logger.error("获取成果成员出错", e);
        throw new ServiceException("获取成果成员出错", e);

      }
      // if (MapUtils.isNotEmpty(map)) {
      // this.publicationSaveService.updateCiteTimeAndListInfo(dupId,
      // map);
      // }
    }
    sendPubConfirmSyncMessage(pubConfirm.getInsId(), pubConfirm.getPsnId(), pubConfirm.getInsAssignId(),
        pubConfirm.getRolPubId(), 1, null, pubConfirm.getPmId(), null, null, PubConfirmSyncMessageEnum.AUTO_CONFIRM);

    dynRecomPsnTaskService.saveDynRePsnTask(pubConfirm.getPsnId(), RcmdDynamicConstants.DYN_RECOMMEND_TYPE_PUB);
  }

  private void sendPubConfirmSyncMessage(Long insId, Long psnId, Long insAssignId, Long rolPubId, Integer confirmResult,
      String xml, Long confirmPmId, Map<String, String> pubList, String publishDate,
      PubConfirmSyncMessageEnum actionType) throws ServiceException {
    InsPortalRcmd insPortal = null;
    try {
      insPortal = insPortalManager.getInsPortalByInsId(insId);
      if (insPortal == null) {
        return;
      }
      PubConfirmSyncMessage message = new PubConfirmSyncMessage();
      message.setPsnId(psnId);
      message.setInsId(insId);
      message.setAssignId(insAssignId);
      message.setPmId(confirmPmId);
      message.setPubList(pubList);
      message.setInsPubId(rolPubId);
      message.setPubXml(xml);
      message.setNodeId(insPortal.getRolNodeId());
      message.setConfirmResult(confirmResult);
      message.setNodeId(insPortal.getRolNodeId());
      message.setActionType(actionType);
      pubConfirmXmlSyncProducer.pubConfirmSyncMessage(message);
    } catch (Exception e) {
      logger.error("查找单位节点失败" + e);
      throw new ServiceException("查找单位节点失败" + ":发送成果确认消失失败");
    }

  }

  @Override
  public Long getPubConfirmCountByPsnId(Long psnId) throws ServiceException {
    try {
      return publicationConfirmDao.queryPubConfirmCount(psnId, null);
    } catch (Exception e) {
      logger.error("条件查询人员" + psnId + "需要确认的成果总数出现异常：", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void setPubConfirmSuccess(Long psnId, Long snsPubId, Long insPubId, Long insId) throws ServiceException {

    PublicationConfirm existConfirm;
    try {
      existConfirm = this.publicationConfirmDao.getPublicationConfirmRol(psnId, insPubId);
      if (existConfirm == null) {
        return;
      }
      existConfirm.setSyncStatus(1);
      existConfirm.setSnsPubId(snsPubId);
      if (existConfirm.getConfirmDate() == null || "".equals(existConfirm.getConfirmDate())) {
        existConfirm.setConfirmDate(new Date());
      }
      this.publicationConfirmDao.save(existConfirm);
      // 如果是新增成果，则记录日志，用于发邮件给好友 zk add.
      // if (existConfirm.getDupStatus() == 1) {
      // ConfirmPubLog confirmPubLog = new ConfirmPubLog();
      // confirmPubLog.setPsnId(message.getPsnId());
      // confirmPubLog.setPubId(confirmPubId);
      // confirmPubLog.setCreateDate(new Date());
      // confirmPubLog.setStatus(0);
      // friendsPubRecommendService.saveFrdsPubRecommend(confirmPubLog);
      // }
      // 历史信息
      existConfirm.setConfirmDate(new Date());
      this.bakToHistory(existConfirm);

      // MJG_SCM-5987.
      dynRecomPsnTaskService.saveDynRePsnTask(psnId, RcmdDynamicConstants.DYN_RECOMMEND_TYPE_PUB);

      // 同步待确认成果数.
      Long pendingCfmPubNum = getPubConfirmCountByPsnId(psnId);
      pendingCfmPubNumMessageProducer.syncPendingCfmPubNum(psnId, pendingCfmPubNum.intValue());
    } catch (Exception e) {
      logger.error("确认成果XML同步失败.psnId=" + psnId + " insPubId=" + insPubId, e);
      throw new ServiceException("确认成果XML同步失败.psnId=" + psnId + " insPubId=" + insPubId, e);
    }

  }

}
