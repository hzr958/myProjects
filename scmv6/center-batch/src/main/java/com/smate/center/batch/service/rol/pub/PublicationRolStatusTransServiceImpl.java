package com.smate.center.batch.service.rol.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.PubAssignModeEnum;
import com.smate.center.batch.constant.PubConfirmResultEnum;
import com.smate.center.batch.constant.PublicationRolStatusEnum;
import com.smate.center.batch.dao.rol.pub.EiPubcacheInsAssignDao;
import com.smate.center.batch.dao.rol.pub.IsiPubcacheInsAssignDao;
import com.smate.center.batch.dao.rol.pub.PdwhPubcacheInsAssignDao;
import com.smate.center.batch.dao.rol.pub.PubMedPubcacheInsAssignDao;
import com.smate.center.batch.dao.rol.pub.PubPsnRolDao;
import com.smate.center.batch.dao.rol.pub.PubRolSubmissionDao;
import com.smate.center.batch.dao.rol.pub.PubUnitOwnerDao;
import com.smate.center.batch.dao.rol.pub.PublicationRolDao;
import com.smate.center.batch.dao.rol.pub.SpsPubcacheInsAssignDao;
import com.smate.center.batch.enums.pub.PublicationOperationEnum;
import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.PublicationNotFoundException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.ClearTaskNoticeEvent;
import com.smate.center.batch.model.rol.pub.EiPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.IsiPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.PdwhPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.PubMedPubcacheInsAssign;
import com.smate.center.batch.model.rol.pub.PubMemberRol;
import com.smate.center.batch.model.rol.pub.PubRolSubmission;
import com.smate.center.batch.model.rol.pub.PubRolSubmissionStatusEnum;
import com.smate.center.batch.model.rol.pub.PubUnitOwner;
import com.smate.center.batch.model.rol.pub.PubXmlSyncEvent;
import com.smate.center.batch.model.rol.pub.PubXmlSyncEventEnum;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.PublicationRolForm;
import com.smate.center.batch.model.rol.pub.SpsPubcacheInsAssign;
import com.smate.center.batch.model.sns.pub.PublicationList;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.HttpMyPublicationService;
import com.smate.center.batch.service.pub.PubDupService;
import com.smate.center.batch.service.pub.PublicationCacheService;
import com.smate.center.batch.service.pub.PublicationListService;
import com.smate.center.batch.service.pub.PublicationLogService;
import com.smate.center.batch.service.pub.PublicationXmlService;
import com.smate.center.batch.service.pub.RolPublicationXmlManager;
import com.smate.center.batch.service.pub.mq.PubAssignMessageProducer;
import com.smate.center.batch.service.pub.mq.PubDupCheckMessage;
import com.smate.center.batch.service.pub.mq.PubDupCheckMessageProducer;
import com.smate.center.batch.service.pub.mq.PubRolSubmissionStatRefreshProducer;
import com.smate.center.batch.service.pub.mq.PubSubmissionInsOpSyncProducer;
import com.smate.center.batch.service.pub.mq.PullXmlSyncMessageProducer;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ScmRolRoleConstants;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;


/**
 * 成果状态跳转服务。（批准成果使用）.
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("publicationRolStatusTransService")
public class PublicationRolStatusTransServiceImpl implements PublicationRolStatusTransService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PublicationRolDao publicationRolDao;
  @Autowired
  private PublicationLogService publicationLogService;
  @Autowired
  private PubRolSubmissionDao pubRolSubmissionDao;
  @Autowired
  private PubPsnRolDao pubPsnRolDao;
  @Autowired
  private EiPubcacheInsAssignDao eiPubcacheInsAssignDao;
  @Autowired
  private PublicationRolService publicationRolService;
  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private PublicationCacheService publicationCacheService;
  @Autowired
  private PubDupCheckMessageProducer pubDupCheckMessageProducer;
  @Autowired
  private PullXmlSyncMessageProducer pullXmlSyncMessageProducer;
  @Autowired
  private PubAssignMessageProducer pubAssignMessageProducer;
  @Autowired
  private PsnPubStatSyncService psnPubStatSyncService;
  @Autowired
  private PubInsSyncRolService pubInsSyncRolService;
  @Autowired
  private PubRolMemberDao pubRolMemberDao;
  @Autowired
  private PubSubmissionInsOpSyncProducer pubSubmissionInsOpSyncProducer;
  @Autowired
  private PubRolSubmissionStatRefreshProducer pubRolSubmissionStatRefreshProducer;
  @Autowired
  private PubRolPersonService pubRolPersonService;
  @Autowired
  private RolPublicationXmlManager rolPublicationXmlManager;
  @Autowired
  private PublicationListService publicationListService;
  @Autowired
  private IsiPubcacheInsAssignDao isiPubcacheInsAssignDao;
  @Autowired
  private PubMedPubcacheInsAssignDao pubMedPubcacheInsAssignDao;
  @Autowired
  private SpsPubcacheInsAssignDao spsPubcacheInsAssignDao;
  @Autowired
  private KpiRefreshPubService kpiRefreshPubService;
  @Autowired
  private TaskNoticeService taskNoticeService;
  @Autowired
  private PubUnitOwnerDao pubUnitOwnerDao;
  @Autowired
  private PubDupService pubDupService;
  @Autowired
  private PubFundInfoRolService pubFundInfoService;
  @Autowired
  private PubRolKeyWordsService pubRolKeyWordsService;
  @Autowired
  private HttpMyPublicationService httpMyPublicationService;
  @Autowired
  private PdwhPubcacheInsAssignDao pdwhPubcacheInsAssignDao;

  /**
   * 批准单位个人用户提交的成果 操作包括：
   * 
   * 1、更新单位publication状态为已批准
   * 
   * 2、更新单位pub_submission状态为已批准
   * 
   * 3、将该成果放入查重队列，进行查重处理
   * 
   * 4、将批准状态同步更新回个人库.
   */
  @Override
  public void approve(Set<Long> pubIds) throws ServiceException {

    Long opPsnId = SecurityUtils.getCurrentUserId();
    Long insId = SecurityUtils.getCurrentInsId();

    Iterator<Long> itor = pubIds.iterator();
    PublicationOperationEnum opApprove = PublicationOperationEnum.Approve;

    Long pubId = -1L;
    try {
      while (itor.hasNext()) {
        pubId = itor.next();
        PublicationRol pubRol = publicationRolDao.get(pubId);
        // 更新成果状态
        this.publicationRolDao.updateStatus(pubRol.getId(), PublicationRolStatusEnum.APPROVED);
        // 更新提交状态
        if (pubRol.getSnsPubId() != null) {
          PubRolSubmission sub =
              this.pubRolSubmissionDao.updateSubmitStatus(pubId, insId, PubRolSubmissionStatusEnum.APPROVED);

          Integer confirmResult = null;
          Long pmId = null;
          // 如果提交人员不再pub_member中，则需要提交人确认
          PubMemberRol pubMemeberByPubPsnd = pubRolMemberDao.getPubMemeberByPubPsnd(pubRol.getId(), sub.getPsnId());
          if (pubMemeberByPubPsnd == null) {
            confirmResult = PubConfirmResultEnum.PENDING_CONFIRMATION;
          } else {
            pmId = pubMemeberByPubPsnd.getId();
            // 如果提交人员在pub_memeber中，则设置该人员的状态为已认领状态
            confirmResult = PubConfirmResultEnum.CONFIRMED;
            // 重构pub_member xml
            PubXmlDocument pubXmlDoc = rolPublicationXmlManager.rebuildPubMember(pmId, insId, sub.getPsnId(), pubId);
            this.publicationXmlService.save(pubId, pubXmlDoc.getXmlString());
            // 单位个数
            Element memberXml =
                (Element) pubXmlDoc.getNode(PubXmlConstants.PUB_MEMBERS_MEMBER_XPATH + "[@pm_id=" + pmId + "]");
            if (memberXml != null) {
              Integer insCount = IrisNumberUtils.createInteger(memberXml.attributeValue("ins_count"));
              insCount = insCount == null ? 0 : insCount;
              pubMemeberByPubPsnd.setInsCount(insCount);
              this.pubRolMemberDao.savePubMember(pubMemeberByPubPsnd);
            }
          }
          Long pubPsnId = sub.getPsnId();
          Long insPubId = pubRol.getId();
          // 同步状态
          this.sysPubPsnStat(pubPsnId, insId, insPubId, pmId, confirmResult);

          // 同步批准状态回个人
          pubSubmissionInsOpSyncProducer.aproveSubmitPub(pubPsnId, insId, pubRol.getSnsPubId());
        }
        // 记录日志
        this.publicationLogService.logOp(pubId, opPsnId, insId, opApprove, null);

        // 把pubId放进MQ的查重队列,立刻处理
        this.pubDupCheckMessageProducer.sendDupCheckMessage(new PubDupCheckMessage(insId, pubId, opPsnId));
        // 把提交的成果指派给研究人员
        pubAssignMessageProducer.assignByPub(insId, opPsnId, pubId, 0);
      }

    } catch (Exception e) {
      logger.error("approve成果出错insId:" + insId + ", pubId:" + pubId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 同步认领状态.
   * 
   * @param pubPsnId
   * @param insId
   * @param insPubId
   * @param confirmResult
   * @throws ServiceException
   * @throws DaoException
   */

  public void sysPubPsnStat(Long pubPsnId, Long insId, Long insPubId, Long pmId, int confirmResult)
      throws ServiceException, DaoException {

    psnPubStatSyncService.addPubPsn(insPubId, insId, pubPsnId, PubAssignModeEnum.ASSIGN_ON_APPROVED, confirmResult == 1,
        pmId);
  }

  @Override
  public void delete(Long pubId, String functionName) throws ServiceException {

    try {

      PublicationRol pubRol = publicationRolDao.get(pubId);
      deletePublication(functionName, pubRol);

    } catch (ServiceException e) {

      logger.error("deletePublication删除成果出错pubId: " + pubId, e);
      throw e;
    }

  }

  @Override
  public void delIsiMatchPub(Long xmlId, Long insId) throws ServiceException {

    // 获取关系列表
    List<IsiPubcacheInsAssign> list = isiPubcacheInsAssignDao.getIsiPubcacheInsAssign(xmlId, insId);
    if (list == null || list.size() == 0)
      return;
    // 如果已经导入，则删除
    for (IsiPubcacheInsAssign assign : list) {
      if (assign.getImported() == 1) {
        this.delete(assign.getPubId(), "delIsiMatchPub");
      }
    }

  }

  @Override
  public void delPubMedMatchPub(Long xmlId, Long insId) throws ServiceException {
    // 获取关系列表
    List<PubMedPubcacheInsAssign> list = pubMedPubcacheInsAssignDao.getPubMedPubcacheInsAssign(xmlId, insId);
    if (list == null || list.size() == 0)
      return;
    // 如果已经导入，则删除
    for (PubMedPubcacheInsAssign assign : list) {
      if (assign.getImported() == 1) {
        this.delete(assign.getPubId(), "delPubMedMatchPub");
      }
    }
  }

  @Override
  public void delSpsMatchPub(Long xmlId, Long insId) throws ServiceException {
    // 获取关系列表
    List<SpsPubcacheInsAssign> list = spsPubcacheInsAssignDao.getSpsPubcacheInsAssign(xmlId, insId);
    if (list == null || list.size() == 0)
      return;
    // 如果已经导入，则删除
    for (SpsPubcacheInsAssign assign : list) {
      if (assign.getImported() == 1) {
        this.delete(assign.getPubId(), "delSpsMatchPub");
      }
    }
  }

  @Override
  public void delEiMatchPub(Long xmlId, Long insId) throws ServiceException {

    // 获取关系列表
    List<EiPubcacheInsAssign> list = eiPubcacheInsAssignDao.getEiPubcacheInsAssign(xmlId, insId);
    if (list == null || list.size() == 0)
      return;
    // 如果已经导入，则删除
    for (EiPubcacheInsAssign assign : list) {
      if (assign.getImported() == 1) {
        this.delete(assign.getPubId(), "delEiMatchPub");
      }
    }

  }

  public void deletePublication(String functionName, PublicationRol pubRol) throws ServiceException {

    Long dupGroupId = pubRol.getDupGroupId();
    Integer roleId = SecurityUtils.getCurrentUserRoleId();
    Long insId = SecurityUtils.getCurrentInsId();
    Long unitId = SecurityUtils.getCurrentUnitId();
    Long pubId = pubRol.getId();
    try {
      // 部门管理员
      /**
       * <pre>
       * 本部门或子部门添加的且未关联外部门人员的成果直接删除，如果关联了外部门人员的成果,只删除本部门的作者关系,拥用关系则逐级上调; 
       * 本部门关系的外部门成果只是删除作者关系
       * </pre>
       */
      if (ScmRolRoleConstants.UNIT_RO.equals(roleId) || ScmRolRoleConstants.UNIT_CONTACT.equals(roleId)) {

        // 删除与本部门关联的成果、人员关系
        this.pubRolPersonService.removeAssignByUnit(pubId, insId, unitId);
        // 否则，将拥有关系上移
        PubUnitOwner unitOwner = this.pubUnitOwnerDao.getPubUnitOwner(pubId, insId, unitId);
        if (unitOwner != null) {

          // 判断是否跟其他部门关联，否则删除成果
          boolean isPubMatchOtherUnit = this.pubPsnRolDao.isPubMatchOtherUnit(pubId, insId, unitId);
          // 未关联到其他部门，直接删除
          if (!isPubMatchOtherUnit) {
            pubDupService.setPubCanDup(pubId, false);
            publicationRolDao.deletePublication(pubRol);
            // 更新组，如果组成果个数小于2，则删除该组
            if (dupGroupId != null) {
              publicationRolDao.updateDupGroup(dupGroupId);
            }
            this.pubUnitOwnerDao.removePubUnitOwner(pubId, insId, unitId);

          } else {// 拥用关系则逐级上调

            // 一级部门，直接删除拥有关系
            if (unitOwner.getParentUnitId() == null || unitOwner.getParentUnitId().equals(unitId)) {
              this.pubUnitOwnerDao.delete(unitOwner);
            } else {
              // 如果是二级部门，关系上移
              unitOwner.setUnitId(unitOwner.getParentUnitId());
              unitOwner.setParentUnitId(null);
            }
          }
        }

      } else {
        pubDupService.setPubCanDup(pubId, false);
        publicationRolDao.deletePublication(pubRol);
        // 更新组，如果组成果个数小于2，则删除该组
        if (dupGroupId != null) {
          publicationRolDao.updateDupGroup(dupGroupId);
        }
        // 删除成果、人员关系
        this.psnPubStatSyncService.removePubPsn(pubId, pubRol.getInsId());
        // 删除关键词
        pubRolKeyWordsService.delPubKeywords(pubId);
      }
    } catch (Exception e1) {
      logger.error("deletePublication删除成果出错pubId: " + pubId, e1);
      throw new ServiceException(e1);
    }

    // 删除缓存
    publicationCacheService.removeROLPubXml(pubId);

    // 添加成果统计冗余
    this.kpiRefreshPubService.addPubRefresh(pubId, true);
    this.pubFundInfoService.removePubFundInfo(pubId);
    // 清理缓存中的任务统计数
    taskNoticeService
        .clearTaskNotice(ClearTaskNoticeEvent.getInstance(pubRol.getInsId(), 1, dupGroupId != null ? 1 : 0, 1));

    // 记录日志
    try {
      Map<String, String> opDetail = new HashMap<String, String>();
      opDetail.put("function", functionName);
      long opPsnId = SecurityUtils.getCurrentUserId();
      this.publicationLogService.logOp(pubId, opPsnId, pubRol.getInsId(), PublicationOperationEnum.Delete, opDetail);
    } catch (ServiceException e) {
      throw e;
    }
  }

  /**
   * 拒绝单位人员提交的成果 操作包括：
   * 
   * 1、更新publication状态为删除状态
   * 
   * 2、更新pub_submission状态为未提交状态
   * 
   * 3、同步拒绝操作到个人库.
   */

  @Override
  public void reject(Set<Long> pubIds) throws ServiceException {
    Iterator<Long> itor = pubIds.iterator();
    Long opPsnId = SecurityUtils.getCurrentUserId();
    Long insId = SecurityUtils.getCurrentInsId();
    PublicationOperationEnum opReject = PublicationOperationEnum.Reject;
    String functionName = "rejectPub";
    Long pubId = -1L;
    try {

      while (itor.hasNext()) {
        pubId = itor.next();
        PublicationRol pubRol = publicationRolDao.get(pubId);
        // 更新成果状态
        this.publicationRolDao.updateStatus(pubRol.getId(), PublicationRolStatusEnum.DELETED);
        // 更新提交状态
        if (pubRol.getSnsPubId() != null) {
          PubRolSubmission sub =
              this.pubRolSubmissionDao.updateSubmitStatus(pubId, insId, PubRolSubmissionStatusEnum.IN_PREPARATION);
          // 冗余提交标记
          this.pubInsSyncRolService.updateSnsPubSubmittedFlag(sub.getSubmitPubId(), insId, false);
          // 同步拒绝状态回个人库.
          this.pubSubmissionInsOpSyncProducer.rejectSubmitPub(sub.getPsnId(), insId, sub.getSubmitPubId());
        }
        // 执行成果删除操作
        this.deletePublication(functionName, pubRol);
        // 更新统计数
        pubRolSubmissionStatRefreshProducer.sendRefreshMessage(insId, pubRol.getCreatePsnId());
        // 记录日志
        this.publicationLogService.logOp(pubId, opPsnId, insId, opReject, null);
      }

    } catch (DaoException e) {
      logger.error("reject成果出错insId:" + insId + ", pubId:" + pubId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * ###################作废.
   */
  @SuppressWarnings("unchecked")
  @Override
  public void rejectApproved(Set<Long> pubIds) throws ServiceException {
    Iterator<Long> itor = pubIds.iterator();
    Long opPsnId = SecurityUtils.getCurrentUserId();
    Long insId = SecurityUtils.getCurrentInsId();
    PublicationOperationEnum opReject = PublicationOperationEnum.RejectApproved;
    Long pubId = -1L;
    try {

      while (itor.hasNext()) {
        pubId = itor.next();
        PublicationRol pubRol = publicationRolDao.get(pubId);
        // 保留成果数据，删除成果-人关系表的记录
        boolean flag = this.pubPsnRolDao.clearAll(insId, pubId);
        if (flag) {
          // 更新作者名称冗余字段
          Map<String, String> authorNames = this.publicationRolService.buildPubAuthorNames(pubId);

          // 更新XML数据
          PublicationXml xml = this.publicationXmlService.getById(pubId);
          try {
            PubXmlDocument doc = new PubXmlDocument(xml.getXmlData());
            doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names", authorNames.get("all_names"));
            doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_author_names",
                authorNames.get("brief_names"));
            List nodes = doc.getPubMembers();
            for (int index = 0; index < nodes.size(); index++) {
              Element node = (Element) nodes.get(index);
              node.addAttribute("member_psn_id", "");
            }
            xml.setXmlData(doc.getXmlString());
            this.publicationXmlService.save(xml.getId(), xml.getXmlData());
          } catch (DocumentException e) {
            logger.error("rejectApproved成果XML转换出错insId:" + insId + ", pubId:" + pubId, e);
            throw new ServiceException(e);
          }
        }
        // 更新统计数
        pubRolSubmissionStatRefreshProducer.sendRefreshMessage(insId, pubRol.getCreatePsnId());
        // 更新提交状态
        if (pubRol.getSnsPubId() != null) {
          PubRolSubmission sub =
              this.pubRolSubmissionDao.updateSubmitStatus(pubId, insId, PubRolSubmissionStatusEnum.IN_PREPARATION);
          // 冗余提交标记
          this.pubInsSyncRolService.updateSnsPubSubmittedFlag(sub.getSubmitPubId(), insId, false);
          // 同步提交状态回个人库.
          this.httpMyPublicationService.syncSubmissionState(new Long[] {pubRol.getSnsPubId()}, insId,
              PubRolSubmissionStatusEnum.IN_PREPARATION);
        }
        // 记录日志
        this.publicationLogService.logOp(pubId, opPsnId, insId, opReject, null);
      }

    } catch (Exception e) {
      logger.error("rejectApproved成果出错insId:" + insId + ", pubId:" + pubId, e);
      throw new ServiceException(e);
    }

  }

  /**
   * 
   * #################作废.
   * 
   * 批准本单位人员未提交的成果到单位库 主要的操作包括：
   * 
   * 1、查找该成果是否提交过(查询pub_submission)，但是被撤销，如果是则更新状态为已批准、如果不是则创建且更新状态为已批准
   * 
   * 2、发送XML同步请求到MQ中
   * 
   * 
   * 3、更新个人库中该成果的状态为已提交状态.
   */

  @Override
  public void pullFromSNS(String pubPsnPair) throws ServiceException {

    List<PubXmlSyncEvent> events = this.beforePullFromSNS(pubPsnPair);
    // 1. 发送XML同步消息
    logger.info("@@@@@@@@@@@@@@@@pullFromSNS发送消息@@@@@@@@@@@@@@@@@@@@@@@.{}", pubPsnPair);
    try {
      for (PubXmlSyncEvent event : events) {
        this.pullXmlSyncMessageProducer.sendPullXmlMessage(event);
      }
    } catch (Exception e) {
      logger.error("pullFromSNS发送消息错误.", e);
      throw new ServiceException(e);
    }
  }

  // @Transactional(rollbackFor = Exception.class,propagation =
  // Propagation.REQUIRES_NEW)
  @SuppressWarnings("unchecked")
  private List<PubXmlSyncEvent> beforePullFromSNS(String pubPsnPair) throws ServiceException {

    // 1. 创建提交记录表
    /*
     * pubPsnPair 为({
     * 'snsPubId':12222,'psnId':23455}|{'snsPubId':122232,'psnId':23455}|{'snsPubId':122223,'psnId':
     * 234535} )
     */
    logger.debug("pullFromSNS请求{}:{}", new Date(), pubPsnPair);

    Long insId = SecurityUtils.getCurrentInsId();
    Long opPsnId = SecurityUtils.getCurrentUserId();
    PublicationOperationEnum op = PublicationOperationEnum.SubmitForR;

    String[] pairs = pubPsnPair.split("\\|");
    // 同步数据MQ
    List<PubXmlSyncEvent> events = new ArrayList<PubXmlSyncEvent>();

    for (String pair : pairs) {
      // 转换JSON数据
      Map item = (Map) JSONObject.toBean(JSONObject.fromObject(pair), Map.class);
      try {
        Long snsPubId = Long.parseLong(String.valueOf(item.get("snsPubId")));
        Long psnId = Long.parseLong(String.valueOf(item.get("psnId")));
        // 判断是否已经提交过
        PubRolSubmission sub = this.pubRolSubmissionDao.getSubmissionBySNSPubId(snsPubId, insId);
        if (sub == null) {
          sub = this.pubRolSubmissionDao.create(psnId, snsPubId, insId, opPsnId, PubRolSubmissionStatusEnum.APPROVED);
        } else {
          this.pubRolSubmissionDao.update(psnId, snsPubId, insId, opPsnId, PubRolSubmissionStatusEnum.APPROVED);
        }
        pubRolSubmissionStatRefreshProducer.sendRefreshMessage(insId, psnId);
        PubXmlSyncEvent event = new PubXmlSyncEvent(snsPubId, insId, psnId, PubXmlSyncEventEnum.SUBMIT_OUTPUTS, psnId);
        events.add(event);
        // 记录日志
        this.publicationLogService.logOp(snsPubId, opPsnId, insId, op, null);
        // 冗余提交标记
        this.pubInsSyncRolService.updateSnsPubSubmittedFlag(sub.getSubmitPubId(), insId, true);
        // 2.把提交状态同步回SNS
        this.httpMyPublicationService.syncSubmissionState(new Long[] {snsPubId}, insId,
            PubRolSubmissionStatusEnum.SUBMITTED);

      } catch (NumberFormatException e) {
        logger.error("pullFromSNS参数格式不正确", e);
        throw new ServiceException(e);
      } catch (DaoException e) {
        logger.error("pullFromSNS创建提交表错误.", e);
        throw new ServiceException(e);
      } catch (ServiceException e) {
        throw e;
      }
    }
    return events;
  }

  /**
   * 作废，保留，之前合并成果需要设置作者匹配到的人,合并成果 主要操作.
   * 
   * 1、查询保留成果、删除合并成果
   * 
   * 2、如果合并的成果是从R提交到RO的成果，需要将pub_submission表的ins_pub_id字段更新为保留后的成果.
   */
  public void merge(Long mainPubId, Set<Long> othersPubId, String pubMemberMap) throws ServiceException {

    String functionName = "dupCheck";
    Iterator<Long> itor = othersPubId.iterator();
    Long opPsnId = SecurityUtils.getCurrentUserId();
    Long insId = SecurityUtils.getCurrentInsId();
    PublicationOperationEnum opMerge = PublicationOperationEnum.DuplicationMerge;
    PublicationRol mainPub = publicationRolDao.get(mainPubId);
    Long pubId = -1L;
    try {

      // 用户设置了匹配到的人
      if (StringUtils.isNotBlank(pubMemberMap)) {
        JSONArray jarray = JSONArray.fromObject(pubMemberMap);
        if (jarray != null && jarray.size() > 0) {
          for (int i = 0; i < jarray.size(); i++) {
            JSONObject jobj = jarray.getJSONObject(i);
            String strPsnId = jobj.getString("psnId");
            String strPmId = jobj.getString("pmId");
            Long psnId = NumberUtils.isDigits(strPsnId) ? Long.valueOf(strPsnId) : null;
            Long pmId = NumberUtils.isDigits(strPmId) ? Long.valueOf(strPmId) : null;
            if (pmId != null) {
              PubMemberRol pubMember = this.pubRolMemberDao.getPubMemberById(pmId);
              Long pmPsnId = pubMember.getPsnId();
              // 删除了原来的用户，指派新的用户
              if (psnId != null && pmPsnId != null && !psnId.equals(pmPsnId)) {
                // 删除指派
                this.pubRolPersonService.removeAssignByPubPsn(pubMember.getPubId(), pmPsnId);
                // 重新指派
                this.pubRolPersonService.saveAssign(pmId, psnId, pubMember.getPubId());
                pubMember.setPsnId(psnId);
                this.pubRolMemberDao.save(pubMember);
                // 更改成果XML
                this.rolPublicationXmlManager.updatePubMemeberXml(pubMember);
                // 原先没有匹配到的人
              } else if (psnId != null && pmPsnId == null) {
                // 指派用户
                this.pubRolPersonService.saveAssign(pmId, psnId, pubMember.getPubId());
                pubMember.setPsnId(psnId);
                this.pubRolMemberDao.save(pubMember);
                // 更改成果XML
                this.rolPublicationXmlManager.updatePubMemeberXml(pubMember);
                // 原先指派了用户，被删除了这里
              } else if (psnId == null && pmPsnId != null) {
                // 删除指派
                this.pubRolPersonService.removeAssignByPubPsn(pubMember.getPubId(), pmPsnId);
              }
            }
          }
        }
      }
      Map<String, String> opDetail = new HashMap<String, String>();
      opDetail.put("mainPubId", mainPubId.toString());
      PublicationRolForm mainForm = new PublicationRolForm();
      mainForm.setPubId(mainPubId);
      PublicationRolForm result = rolPublicationXmlManager.loadXml(mainForm);
      String mainPubXml = result.getPubXml();
      PubXmlDocument mainPubDoc = new PubXmlDocument(mainPubXml);
      while (itor.hasNext()) {
        pubId = itor.next();
        PublicationRol pubRol = publicationRolDao.get(pubId);
        PublicationRolForm form = new PublicationRolForm();
        form.setPubId(pubRol.getId());
        // 合并pub_list 包括pub_list表和xml
        PublicationRolForm pubXmlForm = rolPublicationXmlManager.loadXml(form);
        String pubXml = pubXmlForm.getPubXml();
        PubXmlDocument mergePubDoc = new PubXmlDocument(pubXml);
        // 合并收录情况
        Element ele = (Element) mergePubDoc.getNode(PubXmlConstants.PUB_LIST_XPATH);
        Element newEle = (Element) mainPubDoc.getNode(PubXmlConstants.PUB_LIST_XPATH);
        if (newEle == null && ele != null) {
          newEle = mainPubDoc.createElement(PubXmlConstants.PUB_LIST_XPATH);
          mainPubDoc.copyPubElement(newEle, ele);
        } else if (newEle != null && ele != null) {
          mainPubDoc.mergeList(newEle, ele);
        }
        this.deletePublication(functionName, pubRol);
        // 合并R提交的成果，更新pub-submission表的ins_pub_id字段
        // R提交的--->RO录入/导入的
        // R提交的--->R提交的
        List<PubRolSubmission> subs = this.pubRolSubmissionDao.getSubmissionByRolRealPubId(pubRol.getId(), insId);
        for (PubRolSubmission sub : subs) {
          sub.setRealInsPub(mainPub);
          this.pubRolSubmissionDao.save(sub);
        }
        this.publicationLogService.logOp(pubId, opPsnId, insId, opMerge, opDetail);
      }
      this.rolPublicationXmlManager.updatePubXml(mainPubId, mainPubDoc.getXmlString());
      Element pubListNode = (Element) mainPubDoc.getNode(PubXmlConstants.PUB_LIST_XPATH);
      if (pubListNode != null) {
        mergePubList(mainPubId, pubListNode);
      }
    } catch (DaoException e) {
      logger.error("merge合并成果出错mainPubId:" + mainPubId + ", pubId:" + pubId, e);
      throw new ServiceException(e);
    } catch (PublicationNotFoundException e) {
      logger.error("merge 指定的xml 没有找到 mainPubId:" + mainPubId + ", pubId:" + pubId, e);
    } catch (DocumentException e) {
      logger.error("merge 指定的xml 解析失败  mainPubId:" + mainPubId + ", pubId:" + pubId, e);
    }
  }

  @Override
  public void merge(Long mainPubId, Set<Long> othersPubId) throws ServiceException {

    String functionName = "dupCheck";
    Iterator<Long> itor = othersPubId.iterator();
    Long opPsnId = SecurityUtils.getCurrentUserId();
    Long insId = SecurityUtils.getCurrentInsId();
    PublicationOperationEnum opMerge = PublicationOperationEnum.DuplicationMerge;
    PublicationRol mainPub = publicationRolDao.get(mainPubId);
    Long pubId = -1L;
    try {
      Map<String, String> opDetail = new HashMap<String, String>();
      opDetail.put("mainPubId", mainPubId.toString());
      PublicationRolForm mainForm = new PublicationRolForm();
      mainForm.setPubId(mainPubId);
      PublicationRolForm result = rolPublicationXmlManager.loadXml(mainForm);
      String mainPubXml = result.getPubXml();
      PubXmlDocument mainPubDoc = new PubXmlDocument(mainPubXml);
      while (itor.hasNext()) {
        pubId = itor.next();
        PublicationRol pubRol = publicationRolDao.get(pubId);
        PublicationRolForm form = new PublicationRolForm();
        form.setPubId(pubRol.getId());
        // 合并pub_list 包括pub_list表和xml
        PublicationRolForm pubXmlForm = rolPublicationXmlManager.loadXml(form);
        String pubXml = pubXmlForm.getPubXml();
        PubXmlDocument mergePubDoc = new PubXmlDocument(pubXml);
        // 合并收录情况
        Element ele = (Element) mergePubDoc.getNode(PubXmlConstants.PUB_LIST_XPATH);
        Element newEle = (Element) mainPubDoc.getNode(PubXmlConstants.PUB_LIST_XPATH);
        if (newEle == null && ele != null) {
          newEle = mainPubDoc.createElement(PubXmlConstants.PUB_LIST_XPATH);
          mainPubDoc.copyPubElement(newEle, ele);
        } else if (newEle != null && ele != null) {
          mainPubDoc.mergeList(newEle, ele);
        }
        this.deletePublication(functionName, pubRol);
        // 合并R提交的成果，更新pub-submission表的ins_pub_id字段
        // R提交的--->RO录入/导入的
        // R提交的--->R提交的
        List<PubRolSubmission> subs = this.pubRolSubmissionDao.getSubmissionByRolRealPubId(pubRol.getId(), insId);
        for (PubRolSubmission sub : subs) {
          sub.setRealInsPub(mainPub);
          this.pubRolSubmissionDao.save(sub);
        }
        this.publicationLogService.logOp(pubId, opPsnId, insId, opMerge, opDetail);
      }
      this.rolPublicationXmlManager.updatePubXml(mainPubId, mainPubDoc.getXmlString());
      Element pubListNode = (Element) mainPubDoc.getNode(PubXmlConstants.PUB_LIST_XPATH);
      if (pubListNode != null) {
        mergePubList(mainPubId, pubListNode);
      }
    } catch (Exception e) {
      logger.error("合并单位成果失败  mainPubId:" + mainPubId + ", pubId:" + pubId, e);
    }
  }

  /**
   * 合并收录情况.
   * 
   * @param mainPubId
   * @param pubListNode
   * @throws ServiceException
   */
  private void mergePubList(Long mainPubId, Element pubListNode) throws ServiceException {
    PublicationList pubList = this.publicationListService.elementConvertPubList(pubListNode);
    if (pubList != null) {
      PublicationList mainPubList = this.publicationListService.getPublicationList(mainPubId);
      if (mainPubList != null) {
        mainPubList.setListEi(pubList.getListEi());
        mainPubList.setListSci(pubList.getListSci());
        mainPubList.setListIstp(pubList.getListIstp());
        mainPubList.setListSsci(pubList.getListSsci());
        this.publicationListService.saveOrUpdatePublictionList(mainPubList);
      } else {
        pubList.setId(mainPubId);
        this.publicationListService.saveOrUpdatePublictionList(pubList);
      }
    }
  }

  /**
   * 删除指派成果时，如果成果已认领，删除提交数据.
   * 
   * @param psnPubId
   * @throws ServiceException
   */
  public void deleteByDisAssign(Long psnPubId) throws ServiceException {
    Long insId = SecurityUtils.getCurrentInsId();
    try {
      PubRolSubmission submission = pubRolSubmissionDao.getSubmissionBySNSPubId(psnPubId, insId);
      if (submission == null) {
        return;
      }
      if (PubRolSubmissionStatusEnum.IN_PREPARATION != submission.getSubmitStatus()) {

        // 更新提交状态为未提交状态
        submission.setSubmitStatus(PubRolSubmissionStatusEnum.IN_PREPARATION);
        this.pubRolSubmissionDao.save(submission);
        // 更新统计数
        pubRolSubmissionStatRefreshProducer.sendRefreshMessage(insId, submission.getPsnId());
      }
    } catch (Exception e) {
      logger.error("删除指派成果时，如果成果已认领，删除提交数据出错insId:" + insId + ", pubId:" + psnPubId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 允许个人用户成果撤销请求 主要操作：
   * 
   * 1、判断成果状态是否为请求撤销状态，如果是才进入撤销操作，否则说明个人已经操作了该成果，防止删除操作
   * 
   * 2、更新pub_submission状态为未提交状态，允许撤销状态
   * 
   * 3、删除该成果与申请人员的关系 4、更新作者冗余数据（xml中的作者删除）
   * 
   * 5、如果该成果与其他人员有关系，则将该成果不删除该成果，否则删除
   * 
   * 6、同步批准状态回个人库.
   */
  @SuppressWarnings("unchecked")
  @Override
  public void approveWithdrawReq(Set<Long> submitIds) throws ServiceException {
    // 用提交记录ID来更新申请撤销状态
    Iterator<Long> itor = submitIds.iterator();
    Long opPsnId = SecurityUtils.getCurrentUserId();
    Long insId = SecurityUtils.getCurrentInsId();
    Long rolSubId = -1L;
    try {

      while (itor.hasNext()) {
        rolSubId = itor.next();
        PubRolSubmission submission = pubRolSubmissionDao.get(rolSubId);

        if (submission == null) {
          continue;
        }
        // 如果状态为非请求撤销状态
        if (0 != submission.getWithdrawStatus()) {
          continue;
        }

        // 更新提交状态为未提交状态
        submission.setSubmitStatus(PubRolSubmissionStatusEnum.IN_PREPARATION);
        // 更新撤销请求状态为允许撤销
        submission.setWithdrawStatus(1);
        submission.setWithdrawReqConfirmDate(new Date());
        this.pubRolSubmissionDao.save(submission);
        // 冗余提交标记
        this.pubInsSyncRolService.updateSnsPubSubmittedFlag(submission.getSubmitPubId(), insId, false);
        PublicationRol pubRol = submission.getRealInsPub();
        @SuppressWarnings("unused")
        PublicationRol submitPub = submission.getInsPub();
        long rolPubId = pubRol.getId();

        // 移除该人和成果的关系
        this.psnPubStatSyncService.removePubPsn(rolPubId, insId, submission.getPsnId());
        // pub_memeber
        boolean removeAuthor = this.pubPsnRolDao.resetMemberId(rolPubId, submission.getPsnId());
        boolean deletePubRecord = true;
        if (removeAuthor) {
          // 更新作者名称冗余字段
          Map<String, String> authorNames = this.publicationRolService.buildPubAuthorNames(rolPubId);

          // 更新XML数据
          PublicationXml xml = this.publicationXmlService.getById(rolPubId);
          try {
            PubXmlDocument doc = new PubXmlDocument(xml.getXmlData());
            doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "author_names", authorNames.get("all_names"));
            doc.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "brief_author_names",
                authorNames.get("brief_names"));
            List nodes = doc.getPubMembers();
            for (int index = 0; index < nodes.size(); index++) {
              Element node = (Element) nodes.get(index);
              String mpsnId = node.attributeValue("member_psn_id");
              if (StringUtils.isNotBlank(mpsnId)) {
                if (Long.parseLong(mpsnId) == submission.getPsnId()) {
                  node.addAttribute("member_psn_id", "");
                } else {
                  deletePubRecord = false;
                }
              }
            }
            xml.setXmlData(doc.getXmlString());
            this.publicationXmlService.save(xml.getId(), xml.getXmlData());
          } catch (DocumentException e) {
            logger.error("approveWithdrawReq成果XML转换出错insId:" + insId + ", pubId:" + rolPubId, e);
            throw new ServiceException(e);
          }
        }
        // 更新统计数
        pubRolSubmissionStatRefreshProducer.sendRefreshMessage(insId, pubRol.getCreatePsnId());
        // 记录日志
        if (removeAuthor) {
          Map<String, String> opDetail = new HashMap<String, String>();
          opDetail.put("psnId", submission.getPsnId().toString());
          this.publicationLogService.logOp(rolPubId, opPsnId, insId, PublicationOperationEnum.RemovePubPsn, opDetail);
        }
        // 如果该成果与其他人员有关系，则该成果不删除，否则删除
        if (deletePubRecord) {
          this.deletePublication("approveWithdrawReq", pubRol);
        }
        this.publicationLogService.logOp(submission.getSubmitPubId(), opPsnId, insId,
            PublicationOperationEnum.ApproveWithdrawRequest, null);
        // 同步允许撤销回个人库
        this.pubSubmissionInsOpSyncProducer.aproveWithdraw(submission.getPsnId(), insId, submission.getSubmitPubId());
      }

    } catch (Exception e) {
      logger.error("approveWithdrawReq同意申请撤销出错.insId:" + insId + ", submitId:" + rolSubId, e);
      throw new ServiceException(e);
    }

  }

  /**
   * 拒绝撤销申请 操作包括：
   * 
   * 1、更新提交状态为已审核、申请撤销为拒绝(pub_submission)
   * 
   * 2、同步提交状态回个人库.
   */

  @Override
  public void rejectWithdrawReq(Set<Long> submitIds) throws ServiceException {
    // 用提交记录ID来更新申请撤销状态

    Iterator<Long> itor = submitIds.iterator();
    Long opPsnId = SecurityUtils.getCurrentUserId();
    Long insId = SecurityUtils.getCurrentInsId();
    PublicationOperationEnum opFlag = PublicationOperationEnum.RejectWithdrawRequest;
    Long rolSubId = -1L;
    try {

      while (itor.hasNext()) {
        rolSubId = itor.next();
        PubRolSubmission sub = pubRolSubmissionDao.get(rolSubId);
        // 更新提交状态为已审核，更新申请撤销为拒绝状态
        if (sub != null) {
          sub.setSubmitStatus(PubRolSubmissionStatusEnum.APPROVED);
          sub.setWithdrawStatus(2);
          sub.setWithdrawReqConfirmDate(new Date());
          this.pubRolSubmissionDao.save(sub);
          // 记录日志
          this.publicationLogService.logOp(sub.getSubmitPubId(), opPsnId, insId, opFlag, null);

          // 同步不允许撤销回个人库
          this.pubSubmissionInsOpSyncProducer.rejectWithdraw(sub.getPsnId(), insId, sub.getSubmitPubId());
        }
      }

    } catch (Exception e) {
      logger.error("rejectWithdrawReq拒绝申请撤销出错insId:" + insId + ", submitId:" + rolSubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delPdwhMatchPub(Long xmlId, Long insId) {
    // 获取关系列表
    // List<EiPubcacheInsAssign> list = eiPubcacheInsAssignDao.getEiPubcacheInsAssign(xmlId, insId);
    List<PdwhPubcacheInsAssign> list = pdwhPubcacheInsAssignDao.getPdwhPubcacheInsAssign(xmlId, insId);
    if (list == null || list.size() == 0)
      return;
    // 如果已经导入，则删除
    for (PdwhPubcacheInsAssign assign : list) {
      if (assign.getImported() == 1) {
        this.delete(assign.getPubId(), "delPdwhMatchPub");
      }
    }
  }
}
