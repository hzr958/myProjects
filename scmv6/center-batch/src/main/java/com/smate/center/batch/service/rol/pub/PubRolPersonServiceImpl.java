package com.smate.center.batch.service.rol.pub;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.PubAssignSyncMessageEnum;
import com.smate.center.batch.constant.PubConfirmSyncMessageEnum;
import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.dao.rol.pub.InsRegionDao;
import com.smate.center.batch.dao.rol.pub.PubPsnRolDao;
import com.smate.center.batch.dao.rol.pub.PublicationRolDao;
import com.smate.center.batch.enums.pub.PublicationOperationEnum;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rcmd.pub.PubConfirmSyncMessage;
import com.smate.center.batch.model.rol.psn.RolPsnIns;
import com.smate.center.batch.model.rol.pub.InsRegion;
import com.smate.center.batch.model.rol.pub.PubMemberRol;
import com.smate.center.batch.model.rol.pub.PubPsnRol;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.sns.pub.PubAssignSyncMessage;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.psn.PsnStatisticsService;
import com.smate.center.batch.service.pub.PublicationLogService;
import com.smate.center.batch.service.pub.PublicationXmlService;
import com.smate.center.batch.service.pub.RolPublicationXmlManager;
import com.smate.center.batch.service.pub.rcmd.PublicationConfirmService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;



/**
 * 成果指派.
 * 
 * @author liqinghua
 * 
 */
@Service("pubRolPersonService")
@Transactional(rollbackFor = Exception.class)
public class PubRolPersonServiceImpl implements PubRolPersonService, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 9206225959986927539L;

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubRolMemberDao pubRolMemberDao;
  @Autowired
  private PubPsnRolDao pubPsnRolDao;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private InsRegionDao insRegionDao;
  @Autowired
  private PublicationLogService publicationLogService;
  @Autowired
  private PublicationRolDao publicationRolDao;
  @Autowired
  private InsUnitRolService insUnitRolService;
  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private RolPublicationXmlManager rOLPublicationXmlManager;
  @Autowired
  private PublicationConfirmService publicationConfirmService;
  @Autowired
  private KpiRefreshPubService kpiRefreshPubService;
  @Autowired
  private TaskNoticeService taskNoticeService;
  @Autowired
  private PubRolPsnStatService pubRolPsnStatService;
  @Autowired
  private PsnStatisticsService psnStatisticsService;

  /**
   * 个人确认成果后，将确认成果反馈给单位. <br/>
   * 个人确认成果，还没有导入个人成果库.
   * 
   * @param msg
   * @throws ServiceException
   */
  @Override
  public void receiveConfirmMessage(PubConfirmSyncMessage msg) throws ServiceException {
    Long insId = msg.getInsId();
    Long insPubId = msg.getInsPubId();
    Long assignId = msg.getAssignId();
    Long psnId = msg.getPsnId();
    Long cofirmPmId = msg.getPmId();
    PubConfirmSyncMessageEnum actionType = msg.getActionType();
    // 确认状态0不是我的成果，1是我的成果
    Integer confirmResult = msg.getConfirmResult();

    try {
      // 是我的成果
      if (confirmResult == 1) {
        PubPsnRol pubPsnRol = pubPsnRolDao.get(assignId);
        // 指派关系已经被删除，则忽略不处理
        if (pubPsnRol == null) {
          // pubAssignSyncMessageProducer.publishAssignError(psnId, insId, insPubId, 1);
          this.PublishAssignError(psnId, insId, insPubId, 1);
          return;
        }
        Long assignPmId = pubPsnRol.getAuthorPMId();

        // 之前RO指派的作者
        PubMemberRol assignPubMember = null;
        if (assignPmId != null) {
          assignPubMember = this.pubRolMemberDao.getPubMemberById(assignPmId);
        }

        // 确认的作者已经被删除，则清空之前RO指派的作者与成果的关系
        PubMemberRol pubMember = this.pubRolMemberDao.getPubMemberById(cofirmPmId);
        if (pubMember == null) {
          if (assignPubMember != null) {
            // 设置人员ID为空
            assignPubMember.setPsnId(null);
            pubRolMemberDao.savePubMember(assignPubMember);
            rOLPublicationXmlManager.updatePubMemeberXml(assignPubMember);
          }
          // pubAssignSyncMessageProducer.publishAssignError(psnId, insId, insPubId, 2);
          this.PublishAssignError(psnId, insId, insPubId, 1);
          return;
        }
        PublicationRol pub = publicationRolDao.get(insPubId);
        // 重构XML
        PubXmlDocument pubXmlDoc = rOLPublicationXmlManager.reBuildInsPubxml(msg, assignPmId, cofirmPmId, pub);
        // 单位个数
        Element memberXml =
            (Element) pubXmlDoc.getNode(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH + "[@pm_id=" + cofirmPmId + "]");
        // 作者没有选择别的pubmember
        if (cofirmPmId.equals(assignPmId)) {
          pubMember.setPsnId(psnId);
          pubRolMemberDao.save(pubMember);
        } else {// 作者选择了其他的pubmember
          pubMember.setPsnId(psnId);
          pubRolMemberDao.save(pubMember);
          // 清空原来的人员Id
          if (assignPubMember != null && psnId.equals(assignPubMember.getPsnId())) {
            assignPubMember.setPsnId(null);
            assignPubMember.setUnitId(null);
            assignPubMember.setParentUnitId(null);
            assignPubMember.setIsConfirm(0);
            pubRolMemberDao.save(pubMember);
          }
        }
        // 设置部门，省市
        if (memberXml != null) {
          pubMember.setUnitId(IrisNumberUtils.createLong(memberXml.attributeValue("unit_id")));
          pubMember.setParentUnitId(IrisNumberUtils.createLong(memberXml.attributeValue("parent_unit_id")));
        }
        pubMember.setPmInsId(insId);
        pubMember.setIsConfirm(1);
        // 机构所在省市
        InsRegion region = insRegionDao.get(insId);
        if (region != null) {
          pubMember.setPmCity(region.getCyId());
          pubMember.setPmPrv(region.getPrvId());
          pubMember.setPmDis(region.getDisId());
        }

        // 解析收录情况，老系统也被注释掉
        // this.publicationListService.prasePubList(pubXmlDoc);

        // 重构用户名
        this.rOLPublicationXmlManager.praseAuthorNames(pubXmlDoc, pub);

        // 变成已确认状态
        pub.setConfirm(1);
        publicationRolDao.save(pub);

        // 更新状态为认领
        this.pubPsnRolDao.saveAuthorConfirmResult(psnId, insPubId, cofirmPmId, 1);
        // 同步更新已认领成果数
        // 新系统不再使用消息。pubRolPsnStatRefreshProducer.sendRefreshMessage(insId, psnId);
        this.pubRolPsnStatRefresh(insId, psnId);


        // 如果是用户自己确认，完善用户信息，将成果的确认数据放入个人的psn_pm_*数据中，作为以后的参考
        // 暂时注释掉
        /*
         * if (PubConfirmSyncMessageEnum.CONFIRM.equals(actionType)) {
         * psnConfirmPmService.psnConfirmPm(insPubId, psnId, cofirmPmId); }
         */

        String xmlData = pubXmlDoc.getXmlString();
        // 保存成果XML.
        this.publicationXmlService.save(insPubId, xmlData);

        // 用户自己确认的才发送XML，自动确认的不需要
        // 暂时用于自动确认，自己确认TODO，注释掉
        /*
         * if (PubConfirmSyncMessageEnum.CONFIRM.equals(actionType)) { // 发送XML同步到个人
         * pubConfirmSyncMessageProducer.publishAssignXml(psnId, insId, insPubId, xmlData); //
         * 标记成果认领后通知合作者标记. pubCfmCpMailService.markPubCfmCpMailStat(psnId, insPubId, insId); }
         */

        this.publicationLogService.logOpDetail(insPubId, psnId, insId, PublicationOperationEnum.ConfirmAuthor,
            cofirmPmId.toString());
        // 不是我的成果
      } else {

        /*
         * this.pubPsnRolDao.saveAuthorConfirmResult(psnId, insPubId, null, 2); PubPsnRol pubPsn =
         * this.pubPsnRolDao.saveAuthorConfirmResult(psnId, insPubId, null, 2); if (pubPsn != null &&
         * pubPsn.getAuthorPMId() != null) { // 设置人员PubMemberRol psnId，如果为匹配到的人员则置为空 PubMemberRol mb =
         * this.pubRolMemberDao.get(pubPsn.getAuthorPMId()); if (mb != null && psnId.equals(mb.getPsnId()))
         * { // 设置人员ID为空 mb.setPsnId(null); mb.setUnitId(null); mb.setParentUnitId(null);
         * pubRolMemberDao.savePubMember(mb); PublicationRol pub = publicationRolDao.get(insPubId); //
         * 获取XML，XML已经使用自定义缓存 PublicationXml xmlData = publicationXmlService.getById(insPubId); // 转换XML
         * PubXmlDocument doc = new PubXmlDocument(xmlData.getXmlData());
         * rOLPublicationXmlManager.noSaveUpdatePubMemberXml(mb, doc); // 重构用户名
         * this.rOLPublicationXmlManager.praseAuthorNames(doc, pub); publicationRolDao.save(pub); //
         * 保存成果XML. this.publicationXmlService.save(insPubId, doc.getXmlString()); }
         * this.publicationLogService .logOp(insPubId, psnId, insId, PublicationOperationEnum.NotAnAuthor,
         * null); // 同步更新拒绝成果数 pubRolPsnStatRefreshProducer.sendRefreshMessage(insId, psnId); }
         */
      }
      kpiRefreshPubService.addPubRefresh(insPubId, false);

      // 个人新增成果自动查重确认后，更新待认领成果数
      Long pendingCfmPubNum = publicationConfirmService.getPubConfirmCountByPsnId(psnId);
      psnStatisticsService.updateStatisticsPcfPub(psnId, pendingCfmPubNum.intValue());
      // 清理缓存中的任务统计数
      // taskNoticeService.clearTaskNotice(ClearTaskNoticeEvent.getInstance(insId, 0, 0, 1));
    } catch (Exception e) {
      logger.error("个人确认成果后，将确认成果反馈给单位.insPubId=" + insPubId, e);
      this.publicationLogService.logOpDetail(insPubId, psnId, insId, PublicationOperationEnum.ConfirmAuthor,
          e.getMessage());

      throw new ServiceException();
    }
  }

  /**
   * pubAssignSyncMessageProducer.publishAssignError(psnId, insId, insPubId, 2) 重写，不用mq的通信方式来处理了
   * 
   */
  public void PublishAssignError(Long psnId, Long insId, Long insPubId, Integer errorType) throws ServiceException {
    try {
      Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
      PubAssignSyncMessage msg = new PubAssignSyncMessage(psnId, insId, insPubId,
          PubAssignSyncMessageEnum.SYC_ASSIGN_XML_ERROR, errorType, nodeId);

      this.publicationConfirmService.receivePubAssignFromInsSyncMessage(msg,
          PubAssignSyncMessageEnum.SYC_ASSIGN_XML_ERROR);

    } catch (Exception e) {
      logger.error("发布成果指派XML错误.", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 同步更新已认领成果数,不再使用消息
   * 
   */
  public void pubRolPsnStatRefresh(Long insId, Long psnId) {
    /*
     * boolean needToRefresh = AppSettingContext.getIntValue(AppSettingConstants.PUBPSN_STATREF_ENABLED)
     * == 1; if(!needToRefresh){ return; }
     */
    Integer nodeId = SecurityUtils.getCurrentAllNodeId().get(0);
    pubRolPsnStatService.refreshPubRolPsnStat(insId, psnId);

  }

  @Override
  public String saveAssign(Long pmId, Long psnId, Long pubId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void removeAssign(Long assignId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeAssignByPsnId(Long psnId, Long insId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeAssignByUnit(Long pubId, Long insId, Long unitId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeAssignByPubPsn(Long pubId, Long psnId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public List<PubMemberRol> loadPubMembersDetail(Long pubId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<PubPsnRol> loadEmptyPmPubPsnRolDetail(Long pubId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updatePubAuthorState(String pubIdsStr) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void pubConfirmSuccessSyncXml(PubConfirmSyncMessage msg) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, Long> getPubPsnNum(List<Long> pubIds, Long insId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Long> filterLinkPubPsn(List<Long> pubIds, Long insId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Long> filterLinkPmPsn(List<Long> pmIds) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, Long> getPsnPubNum(List<Long> psnIds, Long insId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<PubPsnRol> loadPubPsnRolDetail(Long pubId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<PubPsnRol> loadPmPsnRolDetail(Long pmId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updatePubConfirmReSend(Long pubId) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public List<PubMemberRol> getPubMembers(Long pubId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<RolPsnIns> getPubAuthors(Long pubId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Page<RolPsnIns> findSnsPubAuthorListByPubId(Page page, Long pubId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getPubAuthorsCount(Long pubId) throws ServiceException {
    // TODO Auto-generated method stub
    return 0;
  }

}
