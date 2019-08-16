package com.smate.center.batch.service.pub.rcmd;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.PubAssignSyncMessageEnum;
import com.smate.center.batch.constant.PubConfirmSyncMessageEnum;
import com.smate.center.batch.constant.RcmdDynamicConstants;
import com.smate.center.batch.dao.rcmd.pub.PubConfirmRolPubDao;
import com.smate.center.batch.dao.rcmd.pub.PubConfirmRolPubDupFieldsDao;
import com.smate.center.batch.dao.rcmd.pub.PubConfirmRolPubMembersDao;
import com.smate.center.batch.dao.rcmd.pub.PublicationConfirmDao;
import com.smate.center.batch.dao.rcmd.pub.PublicationConfirmHiDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.pub.PubConfirmRolPubDupFields;
import com.smate.center.batch.model.rcmd.pub.PubConfirmSyncMessage;
import com.smate.center.batch.model.rcmd.pub.PublicationConfirm;
import com.smate.center.batch.model.sns.pub.PubAssignSyncMessage;
import com.smate.center.batch.service.pub.PubDupService;
import com.smate.center.batch.service.rol.pub.InsPortalManager;
import com.smate.center.batch.service.rol.pub.PubRolPersonService;
import com.smate.center.batch.util.pub.LogUtils;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.model.InsPortal;



/**
 * 成果确认业务逻辑.
 * 
 * @author LY
 * 
 */
@Service("publicationConfirmService")
@Transactional(rollbackFor = Exception.class)
public class PublicationConfirmServiceImpl implements PublicationConfirmService {
  private static Logger logger = LoggerFactory.getLogger(PublicationConfirmServiceImpl.class);

  @Autowired
  private PublicationConfirmDao publicationConfirmDao;

  @Autowired
  private PubConfirmRolPubDupFieldsDao pubConfirmRolPubDupFieldsDao;

  @Autowired
  private PubConfirmRolPubMembersDao pubConfirmRolPubMembersDao;

  @Autowired
  private PublicationConfirmHiDao publicationConfirmHiDao;

  @Autowired
  private PubConfirmRolPubDao publicationConfirmRolPubDao;

  @Autowired
  private InsPortalManager insPortalManager;

  @Autowired
  private PubDupService pubDupService;

  @Autowired
  private PubRolPersonService pubRolPersonService;

  @Autowired
  private DynRecomPsnTaskService dynRecomPsnTaskService;


  @Override
  public void autoComfirmStrictDupPub(Long psnId, Long cfmId) throws BatchTaskException {
    try {
      PublicationConfirm confirm = this.publicationConfirmDao.getPublicationConfirmById(psnId, cfmId);
      PubConfirmRolPubDupFields dupFields =
          this.pubConfirmRolPubDupFieldsDao.getByPubPsnId(confirm.getRolPubId(), psnId);
      this.autoComfirmStrictDupPub(confirm, dupFields);
    } catch (Exception e) {
      logger.error("如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担失败", e);
      throw new BatchTaskException("如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担失败.", e);
    }

  }

  @Override
  public void autoComfirmDupPub(Long psnId, Long insPubId) throws BatchTaskException {
    try {
      PublicationConfirm confirm = this.publicationConfirmDao.getPublicationConfirmRol(psnId, insPubId);
      PubConfirmRolPubDupFields dupFields =
          this.pubConfirmRolPubDupFieldsDao.getByPubPsnId(confirm.getRolPubId(), psnId);
      this.autoComfirmStrictDupPub(confirm, dupFields);
    } catch (Exception e) {
      logger.error("如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担失败", e);
      throw new BatchTaskException("如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担失败.", e);
    }

  }


  @Override
  public void autoComfirmStrictDupPub(PublicationConfirm confirm, PubConfirmRolPubDupFields dupFields)
      throws BatchTaskException {
    try {
      if (confirm == null || !PublicationConfirmService.RESULT_UNCONFIRM_INT.equals(confirm.getConfirmResult())) {
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
      if (confirm.getAssignSeqNo() == null) {
        return;
      }
      Long pmId = this.pubConfirmRolPubMembersDao.getRolPmIdBySeqNo(confirm.getRolPubId(), confirm.getAssignSeqNo());
      // 修改确认结果
      Long dupId = null;
      if (CollectionUtils.isNotEmpty(dupMap.get(1))) {
        dupId = dupMap.get(1).get(0);
        confirm.setDupPubIds(dupId.toString());
      } else if (CollectionUtils.isNotEmpty(dupMap.get(2))) {
        dupId = dupMap.get(2).get(0);
        confirm.setDupPubIds(dupId.toString());
      }
      confirm.setSnsPubId(dupId);
      confirm.setDupStatus(1);
      confirm.setConfirmSeqNo(confirm.getAssignSeqNo());
      confirm.setPmId(pmId);
      confirm.setConfirmResult(PublicationConfirmService.RESULT_CONFIRM_INT);
      confirm.setConfirmDate(new Date());
      confirm.setAutoComfirm(1);
      this.publicationConfirmDao.save(confirm);
      // 移动到历史记录表
      this.bakToHistory(confirm);

      if (CollectionUtils.isNotEmpty(dupMap.get(1))) {
        Map map = this.publicationConfirmRolPubDao.queryCfmRolPubBypubId(confirm.getRolPubId());
      }

      this.sendPubConfirmSyncMessage(confirm.getInsId(), confirm.getPsnId(), confirm.getInsAssignId(),
          confirm.getRolPubId(), 1, null, confirm.getPmId(), null, null, PubConfirmSyncMessageEnum.AUTO_CONFIRM);

      // MJG_SCM-5987.
      dynRecomPsnTaskService.saveDynRePsnTask(confirm.getPsnId(), RcmdDynamicConstants.DYN_RECOMMEND_TYPE_PUB);
    } catch (Exception e) {
      logger.error("如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担失败", e);
      throw new BatchTaskException("如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担失败.", e);
    }

  }

  /**
   * pub_simple_hash中简单查重，然后自动确认
   * 
   * @param dupPubIds，psnId，insPubId
   * @throws Exception
   * 
   **/
  @Override
  public void autoConfirmPubSimple(List<Long> dupPubIds, Long psnId, Long snspubId) throws Exception {
    try {
      if (CollectionUtils.isEmpty(dupPubIds)) {
        return;
      }

      for (Long insPubId : dupPubIds) {
        PublicationConfirm confirm = this.publicationConfirmDao.getPublicationConfirmRol(psnId, insPubId);

        if (confirm == null || !PublicationConfirmService.RESULT_UNCONFIRM_INT.equals(confirm.getConfirmResult())) {
          return;
        }

        if (confirm.getAssignSeqNo() == null) {
          return;
        }
        Long pmId = this.pubConfirmRolPubMembersDao.getRolPmIdBySeqNo(confirm.getRolPubId(), confirm.getAssignSeqNo());
        // 修改确认结果
        // Long dupId = dupPubIds.get(0);

        confirm.setSnsPubId(snspubId);
        confirm.setDupStatus(1);
        confirm.setConfirmSeqNo(confirm.getAssignSeqNo());
        confirm.setPmId(pmId);
        confirm.setConfirmResult(PublicationConfirmService.RESULT_CONFIRM_INT);
        confirm.setConfirmDate(new Date());
        confirm.setAutoComfirm(1);
        this.publicationConfirmDao.save(confirm);
        // 移动到历史记录表
        this.bakToHistory(confirm);

        /*
         * if (CollectionUtils.isNotEmpty(dupMap.get(1))) { Map map =
         * this.publicationConfirmRolPubDao.queryCfmRolPubBypubId(confirm.getRolPubId()); }
         */

        this.sendPubConfirmSyncMessage(confirm.getInsId(), confirm.getPsnId(), confirm.getInsAssignId(),
            confirm.getRolPubId(), 1, null, confirm.getPmId(), null, null, PubConfirmSyncMessageEnum.AUTO_CONFIRM);

        // MJG_SCM-5987.
        dynRecomPsnTaskService.saveDynRePsnTask(confirm.getPsnId(), RcmdDynamicConstants.DYN_RECOMMEND_TYPE_PUB);
      }

    } catch (Exception e) {
      logger.error("autoConfirmPubSimple成果自动确认出错", e);
      throw new Exception(e);
    }


  }



  @Override
  public void bakToHistory(PublicationConfirm confirm) throws BatchTaskException {
    if (confirm == null) {
      return;
    }
    try {
      // 删除附属信息
      this.publicationConfirmDao.remove(confirm.getId());
      this.publicationConfirmDao.removeHi(confirm.getId());
      // 移动到历史记录表中.
      this.publicationConfirmHiDao.saveHistory(confirm);
      // this.publicationConfirmDao.saveHistory(confirm);
    } catch (Exception e) {
      logger.error("备份确认结果到历史记录中错误", e);
      throw new BatchTaskException("备份确认结果到历史记录中错误", e);
    }

  }


  /**
   * 同步成果确认信息到单位，不再发送信息，直接使用
   * 
   * @param insId
   * @param psnId
   * @param insAssignId
   * @param rolPubId
   * @param confirmResult
   * @param xml
   * @param confirmPmId
   * @param pubList
   * @param publishDate
   * @param actionType
   * @throws BatchTaskException
   */
  private void sendPubConfirmSyncMessage(Long insId, Long psnId, Long insAssignId, Long rolPubId, Integer confirmResult,
      String xml, Long confirmPmId, Map<String, String> pubList, String publishDate,
      PubConfirmSyncMessageEnum actionType) throws BatchTaskException {

    InsPortal insPortal = insPortalManager.getInsPortalByInsId(insId);
    if (insPortal == null) {
      return;
    }

    PubConfirmSyncMessage msg = new PubConfirmSyncMessage();

    // msg.setFromNodeId(SecurityUtils.getCurrentAllNodeId().get(0));
    msg.setPsnId(psnId);
    msg.setInsId(insId);
    msg.setAssignId(insAssignId);
    msg.setPmId(confirmPmId);
    msg.setPubList(pubList);
    msg.setInsPubId(rolPubId);
    msg.setPubXml(xml);
    // msg.setNodeId(insPortal.getRolNodeId());
    msg.setConfirmResult(confirmResult);
    // msg.setNodeId(insPortal.getRolNodeId());
    msg.setActionType(actionType);

    if (PubConfirmSyncMessageEnum.CONFIRM.equals(actionType)
        || PubConfirmSyncMessageEnum.AUTO_CONFIRM.equals(actionType)
        || PubConfirmSyncMessageEnum.JUST_CONFIRM.equals(actionType)) {
      pubRolPersonService.receiveConfirmMessage(msg);
    }
  }

  /**
   * 只移动了成果自动确认的功能
   * 
   */
  @Override
  public void receivePubAssignFromInsSyncMessage(PubAssignSyncMessage msg, PubAssignSyncMessageEnum actionType)
      throws ServiceException {
    // 单位发送指派信息过来
    // if (PubAssignSyncMessageEnum.ASSIGN.equals(actionType)) {
    // savePubAssign(msg);
    // 单位删除指派
    // } else if (PubAssignSyncMessageEnum.DIS_ASSIGN.equals(actionType)) {
    // deletePubAssign(msg);
    // } else if (PubAssignSyncMessageEnum.SYC_ASSIGN_XML_ERROR.equals(actionType)) {
    if (!PubAssignSyncMessageEnum.SYC_ASSIGN_XML_ERROR.equals(actionType)) {
      return;
    }
    savePubAssignError(msg);
    // }

    // MJG_SCM-5987.
    dynRecomPsnTaskService.saveDynRePsnTask(msg.getPsnId(), RcmdDynamicConstants.DYN_RECOMMEND_TYPE_PUB);
  }

  /**
   * @param msg
   * @throws ServiceException
   */
  private void savePubAssignError(PubAssignSyncMessage message) throws ServiceException {
    PublicationConfirm existConfirm;
    try {
      existConfirm = this.publicationConfirmDao.getPublicationConfirmRol(message.getPsnId(), message.getInsPubId());
      if (existConfirm != null) {
        // 同步成果XML失败类型，1单位删除了指派关系、2成果人员被删除
        int syncStatus = 0;
        switch (message.getErrorType()) {
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
        this.publicationConfirmDao.save(existConfirm);
      }

      // MJG_SCM-5987.
      dynRecomPsnTaskService.saveDynRePsnTask(message.getPsnId(), RcmdDynamicConstants.DYN_RECOMMEND_TYPE_PUB);
    } catch (Exception e) {
      logger.error("判断指派的成果是否存在失败", e);
      throw new ServiceException("判断指派的成果是否存在失败.", e);
    }
  }


  @Override
  public Long getPubConfirmCountByPsnId(Long psnId) throws ServiceException {
    try {
      return this.publicationConfirmDao.queryPubConfirmCount(psnId);
    } catch (Exception e) {
      LogUtils.error(logger, e, "条件查询人员psnId={}需要确认的成果总数出现异常：", psnId);
      throw new ServiceException(e);
    }
  }


  @Override
  public void autoComfirmDupPub(PubAssignSyncMessage message) throws ServiceException {

    try {
      PublicationConfirm confirm =
          this.publicationConfirmDao.getPublicationConfirmRol(message.getPsnId(), message.getInsPubId());
      PubConfirmRolPubDupFields dupFields =
          this.pubConfirmRolPubDupFieldsDao.getByPubPsnId(confirm.getRolPubId(), message.getPsnId());
      this.autoComfirmStrictDupPub(confirm, dupFields);
    } catch (Exception e) {
      logger.error("如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担失败", e);
      throw new ServiceException("如果用户成果库中已经存在严格重复成果，自动给用户确认，减少用户负担失败.", e);
    }
  }

}
