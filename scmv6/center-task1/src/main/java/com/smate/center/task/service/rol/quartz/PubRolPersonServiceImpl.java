package com.smate.center.task.service.rol.quartz;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.rol.quartz.InsRegionDao;
import com.smate.center.task.dao.rol.quartz.PubPsnRolDao;
import com.smate.center.task.dao.rol.quartz.PubRolMemberDao;
import com.smate.center.task.dao.rol.quartz.PublicationRolDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.rcmd.quartz.PubConfirmSyncMessage;
import com.smate.center.task.model.rcmd.quartz.PubConfirmSyncMessageEnum;
import com.smate.center.task.model.rol.quartz.ClearTaskNoticeEvent;
import com.smate.center.task.model.rol.quartz.InsRegion;
import com.smate.center.task.model.rol.quartz.PubMemberRol;
import com.smate.center.task.model.rol.quartz.PubPsnRol;
import com.smate.center.task.model.rol.quartz.PublicationRol;
import com.smate.center.task.model.sns.quartz.PublicationXml;
import com.smate.center.task.rcmd.jms.PubAssignSyncMessageProducer;
import com.smate.center.task.rcmd.jms.PubConfirmSyncMessageProducer;
import com.smate.center.task.rcmd.jms.PubRolPsnStatRefreshProducer;
import com.smate.center.task.single.enums.pub.PublicationOperationEnum;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.PublicationLogService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 
 * @author zjh
 *
 */
@Service("pubRolPersonService")
public class PubRolPersonServiceImpl implements PubRolPersonService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubPsnRolDao pubPsnRolDao;
  @Autowired
  private PubRolMemberDao pubRolMemberDao;
  @Autowired
  private PubAssignSyncMessageProducer pubAssignSyncMessageProducer;
  @Autowired
  private RolPublicationXmlManager rolPublicationXmlManager;
  @Autowired
  private PublicationRolDao publicationRolDao;
  @Autowired
  private InsRegionDao insRegionDao;
  @Autowired
  private PubRolPsnStatRefreshProducer pubRolPsnStatRefreshProducer;
  @Autowired
  private PsnConfirmPmService psnConfirmPmService;
  @Autowired
  private RolPublicationXmlService rolPublicationXmlService;
  @Autowired
  private PubCfmCpMailService pubCfmCpMailService;
  @Autowired
  private PublicationLogService publicationLogService;
  @Autowired
  private TaskNoticeService taskNoticeService;
  @Autowired
  private KpiRefreshPubService kpiRefreshPubService;
  @Autowired
  private PubConfirmSyncMessageProducer pubConfirmSyncMessageProducer;
  @Autowired
  private PubRolSubmissionService pubRolSubmissionService;

  @Override
  public void receiveConfirmMessage(PubConfirmSyncMessage msg) throws ServiceException {
    Long insId = msg.getInsId();
    Long insPubId = msg.getInsPubId();
    Long assignId = msg.getAssignId();
    Long psnId = msg.getPsnId();
    Long cofirmPmId = msg.getPmId();
    PubConfirmSyncMessageEnum actionType = msg.getActionType();
    Integer confirmResult = msg.getConfirmResult();
    try {
      if (confirmResult == 1) {// 是我的成果
        PubPsnRol pubPsnRol = pubPsnRolDao.get(assignId);
        if (pubPsnRol == null) {// 指派关系已经被删除，则忽略不处理,把数据同步回rcmd
          pubAssignSyncMessageProducer.publishAssignError(psnId, insId, insPubId, 1);
          return;

        }

        Long assignPmId = pubPsnRol.getAuthorPMId();
        // 之前RO指派的作者
        PubMemberRol assignPubMember = null;
        if (assignPmId != null) {
          assignPubMember = pubRolMemberDao.getPubMemberById(assignPmId);
        }

        // 确认的作者已经被删除，则清空之前RO指派的作者与成果的关系
        PubMemberRol pubMember = this.pubRolMemberDao.getPubMemberById(cofirmPmId);
        if (pubMember == null) {
          if (assignPubMember != null) {
            assignPubMember.setPsnId(null);
            pubRolMemberDao.savePubMember(assignPubMember);
            rolPublicationXmlManager.updatePubMemeberXml(assignPubMember);

          }
          pubAssignSyncMessageProducer.publishAssignError(psnId, insId, insPubId, 2);
          return;
        }
        PublicationRol pub = publicationRolDao.get(insPubId);
        // 重构XML
        PubXmlDocument pubXmlDoc = rolPublicationXmlManager.reBuildInsPubxml(msg, assignPmId, cofirmPmId, pub);
        // 单位个数
        Element memberXml =
            (Element) pubXmlDoc.getNode(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH + "[@pm_id=" + cofirmPmId + "]");
        if (cofirmPmId.equals(assignPmId)) {
          pubMember.setPsnId(psnId);
          pubRolMemberDao.save(pubMember);
        } else {
          pubMember.setPsnId(psnId);
          pubRolMemberDao.save(pubMember);
          if (assignPubMember != null && psnId.equals(assignPubMember.getPsnId())) {
            assignPubMember.setPsnId(null);
            assignPubMember.setUnitId(null);
            assignPubMember.setParentUnitId(null);
            assignPubMember.setIsConfirm(0);
            pubRolMemberDao.save(pubMember);
          }
        }
        // 设置部门，省市
        pubMember.setUnitId(IrisNumberUtils.createLong(memberXml.attributeValue("unit_id")));
        pubMember.setParentUnitId(IrisNumberUtils.createLong(memberXml.attributeValue("parent_unit_id")));
        pubMember.setPmInsId(insId);
        pubMember.setIsConfirm(1);
        // 机构所在省市
        InsRegion region = insRegionDao.get(insId);
        if (region != null) {
          pubMember.setPmCity(region.getCyId());
          pubMember.setPmPrv(region.getPrvId());
          pubMember.setPmDis(region.getDisId());
        }
        // 重构用户名
        this.rolPublicationXmlManager.praseAuthorNames(pubXmlDoc, pub);
        // 变成已确认状态
        pub.setConfirm(1);
        publicationRolDao.save(pub);
        // 更新状态为认领
        pubPsnRolDao.saveAuthorConfirmResult(psnId, insPubId, cofirmPmId, 1);
        // 同步更新已认领成果数
        pubRolPsnStatRefreshProducer.sendRefreshMessage(insId, psnId);
        // 如果是用户自己确认，完善用户信息，将成果的确认数据放入个人的psn_pm_*数据中，作为以后的参考
        if (PubConfirmSyncMessageEnum.CONFIRM.equals(actionType)) {
          psnConfirmPmService.psnConfirmPm(insPubId, psnId, cofirmPmId);
        }
        String xmlData = pubXmlDoc.getXmlString();
        rolPublicationXmlService.save(insPubId, xmlData);
        // 用户自己确认的才发送XML，自动确认的不需要
        if (PubConfirmSyncMessageEnum.CONFIRM.equals(actionType)) {
          pubConfirmSyncMessageProducer.publishAssignXml(psnId, insId, insPubId, xmlData);
          // 标记成果认领后通知合作者标记.
          pubCfmCpMailService.markPubCfmCpMailStat(psnId, insPubId, insId);
        }
        publicationLogService.logOpDetail(insPubId, psnId, insId, PublicationOperationEnum.ConfirmAuthor,
            cofirmPmId.toString());
      } else {
        pubPsnRolDao.saveAuthorConfirmResult(psnId, insPubId, null, 2);
        PubPsnRol pubPsn = this.pubPsnRolDao.saveAuthorConfirmResult(psnId, insPubId, null, 2);
        if (pubPsn != null && pubPsn.getAuthorPMId() != null) {
          // 设置人员PubMemberRol psnId，如果为匹配到的人员则置为空
          PubMemberRol mb = pubRolMemberDao.get(pubPsn.getAuthorPMId());
          if (mb != null && psnId.equals(mb.getPsnId())) {
            // 设置人员ID为空
            mb.setPsnId(null);
            mb.setUnitId(null);
            mb.setParentUnitId(null);
            pubRolMemberDao.savePubMember(mb);
            PublicationRol pub = publicationRolDao.get(insPubId);
            // 获取XML，XML已经使用自定义缓存
            PublicationXml xmlData = rolPublicationXmlService.getById(insPubId);
            // 转换XML
            PubXmlDocument doc = new PubXmlDocument(xmlData.getXmlData());
            rolPublicationXmlManager.noSaveUpdatePubMemberXml(mb, doc);
            // 重构用户名
            this.rolPublicationXmlManager.praseAuthorNames(doc, pub);
            publicationRolDao.save(pub);
            // 保存成果XML.
            this.rolPublicationXmlService.save(insPubId, doc.getXmlString());
          }
          this.publicationLogService.logOp(insPubId, psnId, insId, PublicationOperationEnum.NotAnAuthor, null);
          // 同步更新拒绝成果数
          pubRolPsnStatRefreshProducer.sendRefreshMessage(insId, psnId);

        }
      }
      kpiRefreshPubService.addPubRefresh(insPubId, false);
      // 清理缓存中的任务统计数
      taskNoticeService.clearTaskNotice(ClearTaskNoticeEvent.getInstance(insId, 0, 0, 1));

    } catch (Exception e) {
      logger.error("个人确认成果后，将确认成果反馈给单位.insPubId=" + insPubId, e);
      this.publicationLogService.logOpDetail(insPubId, psnId, insId, PublicationOperationEnum.ConfirmAuthor,
          e.getMessage());

      throw new ServiceException(e);


    }

  }

  /**
   * 个人收到Xml成果，返回SNSPubID.
   * 
   * @param msg
   * @throws ServiceException
   */
  @Override
  public void pubConfirmSuccessSyncXml(PubConfirmSyncMessage message) throws ServiceException {
    Long insId = message.getInsId();
    Long insPubId = message.getInsPubId();
    // Long assignId = msg.getAssignId();
    Long psnId = message.getPsnId();
    // Long cofirmPmId = msg.getPmId();
    Long snsPubId = message.getSnsPubId();
    // 将成果变成提交状态
    pubRolSubmissionService.submitConfirmPub(snsPubId, insPubId, psnId, insId);

  }

}
