package com.smate.web.v8pub.service.sns;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPubDao;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.statistics.dao.DownloadCollectStatisticsDao;
import com.smate.core.base.statistics.model.DownloadCollectStatistics;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dao.fulltext.CheckHasAgreeRecordDao;
import com.smate.web.v8pub.dao.pdwh.PdwhPubFullTextDAO;
import com.smate.web.v8pub.dao.sns.PubFullTextDAO;
import com.smate.web.v8pub.dao.sns.PubFullTextReqBaseDao;
import com.smate.web.v8pub.dao.sns.PubSnsDAO;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.enums.PubHandlerEnum;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.po.sns.group.GroupPubPO;
import com.smate.web.v8pub.service.email.DownloadYourPubEmailService;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;
import com.smate.web.v8pub.utils.RestTemplateUtils;
import com.smate.web.v8pub.vo.PubFulltextSimilarVO;

@Service("pubFullTextService")
@Transactional(rollbackFor = Exception.class)
public class PubFullTextServiceImpl implements PubFullTextService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubFullTextDAO pubFullTextDAO;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PsnPubService psnPubService;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private ArchiveFileService archiveFileService;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private DownloadCollectStatisticsDao downloadCollectStatisticsDao;
  @Autowired
  private DownloadYourPubEmailService downloadYourPubEmailService;
  @Autowired
  private PubFullTextReqBaseDao pubFTReqBaseDao;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainrol}")
  private String domainrol;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${pub.saveorupdate.url}")
  private String SAVEORUPDATE;
  @Autowired
  private PdwhPubFullTextService pdwhPubFullTextService;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PsnConfigDao psnConfigDao;
  @Autowired
  private PsnConfigPubDao psnConfigPubDao;
  @Autowired
  private GroupPubService groupPubService;
  @Autowired
  private CheckHasAgreeRecordDao checkHasAgreeRecordDao;

  @Override
  public Long getSimilarCount(Long pubId) throws ServiceException {
    Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(pubId);
    if (pdwhPubId != null && pdwhPubId > 0) {
      List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsPubIds(pdwhPubId, pubId);
      if (CollectionUtils.isNotEmpty(snsPubIds)) {
        return pubFullTextDAO.getPubFulltextCount(snsPubIds);
      }
    }
    return null;
  }

  @Override
  public Long getPdwhSimilarCount(Long pdwhPubId) throws ServiceException {
    List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsPubIds(pdwhPubId, 0l);
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      return pubFullTextDAO.getPubFulltextCount(snsPubIds);
    }
    return null;
  }

  @Override
  public List<PubFulltextSimilarVO> getSimilarInfo(Long pubId) throws ServiceException {
    List<PubFulltextSimilarVO> infoList = new ArrayList<PubFulltextSimilarVO>();
    Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(pubId);
    if (pdwhPubId != null && pdwhPubId > 0) {
      List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsPubIds(pdwhPubId, pubId);
      buildSimilarInfo(infoList, snsPubIds);
    }
    return infoList;
  }

  @Override
  public List<PubFulltextSimilarVO> getPdwhSimilarInfo(Long pdwhPubId) throws ServiceException {
    List<PubFulltextSimilarVO> infoList = new ArrayList<PubFulltextSimilarVO>();
    List<Long> snsPubIds = pubPdwhSnsRelationDAO.getSnsPubIds(pdwhPubId, 0l);
    buildSimilarInfo(infoList, snsPubIds);
    return infoList;
  }

  private void buildSimilarInfo(List<PubFulltextSimilarVO> infoList, List<Long> snsPubIds) {
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      List<PubFullTextPO> fulltextList = pubFullTextDAO.getPubFulltextList(snsPubIds);
      if (CollectionUtils.isNotEmpty(fulltextList)) {
        for (PubFullTextPO pubFulltext : fulltextList) {
          PubFulltextSimilarVO info = new PubFulltextSimilarVO();
          // 1.全文图片
          String fulltextImagePath = pubFulltext.getThumbnailPath();
          if (StringUtils.isNotBlank(fulltextImagePath)) {
            fulltextImagePath = domainscm + fulltextImagePath;
          } else {
            fulltextImagePath = V8pubConst.PUB_DEFAULT_FULLTEXT_IMG_NEW;
          }
          info.setFulltextImg(fulltextImagePath);
          // 2.人名,头衔(机构+部门+职称)
          Long psnId = psnPubService.getPubOwnerId(pubFulltext.getPubId());// 成果拥有者id
          Person person = personProfileDao.getUnifiedPsnInfoByPsnId(psnId);
          if (person != null) {
            info.setPsnName(PsnInfoUtils.buildPsnName(person, null));
            info.setPsnTitle(PsnInfoUtils.buildPsnTitoloInfo(person, null));
          }
          // 3.文件名,文件大小
          ArchiveFile archiveFile = archiveFileService.getArchiveFileById(pubFulltext.getFileId());
          if (archiveFile != null) {
            info.setFulltextName(archiveFile.getFileName());
            info.setFulltextSize(archiveFile.getFileSize());
          }
          // 4.下载按钮
          String downloadUrl = getSnsFullTextDownloadUrl(pubFulltext.getPubId(), false);
          info.setDownloadUrl(downloadUrl);
          infoList.add(info);
        }
      }
    }
  }

  @Override
  public String getFulltextImageUrl(Long pubId) throws ServiceException {
    String fulltextImageUrl = "";
    try {
      // 全文包括个人库全文，基准库全文
      PubFullTextPO pubFulltext = this.pubFullTextDAO.getPubFullTextByPubId(pubId);
      PdwhPubFullTextPO pdwhPubFulltext = pdwhPubFullTextDAO.getByPubId(pubId);
      if (pubFulltext != null && StringUtils.isNotBlank(pubFulltext.getThumbnailPath())) {
        fulltextImageUrl = domainscm + pubFulltext.getThumbnailPath();
      } else if (pdwhPubFulltext != null && StringUtils.isNotBlank(pdwhPubFulltext.getThumbnailPath())) {
        fulltextImageUrl = domainscm + pdwhPubFulltext.getThumbnailPath();
      } else if (pubFulltext != null || pdwhPubFulltext != null) {
        fulltextImageUrl = "/resmod/images_v5/images2016/file_img1.jpg";
      }
    } catch (Exception e) {
      logger.error("获取成果pubId={}的全文图片链接出现异常：{}", pubId, e);
    }
    return fulltextImageUrl;
  }

  @Override
  public String getRcmdFullTextDownloadUrl(Long fulltextFileId, boolean isShortUrl) throws ServiceException {
    String url = "";
    if (isShortUrl) {
      url = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.RCMD_FULLTEXT, fulltextFileId);
    } else {
      url = fileDownUrlService.getDownloadUrl(FileTypeEnum.RCMD_FULLTEXT, fulltextFileId, 0L);
    }
    return url;
  }

  @Override
  public PubFullTextPO get(Long pubId) throws ServiceException {
    try {
      PubFullTextPO pubFullTextPO = pubFullTextDAO.getPubFullTextByPubId(pubId);
      return pubFullTextPO;
    } catch (Exception e) {
      logger.error("查询成果查全文表记录时出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public PubFullTextPO getPubFullTextById(Long fullTextId) throws ServiceException {
    try {
      PubFullTextPO pubFullTextPO = pubFullTextDAO.get(fullTextId);
      return pubFullTextPO;
    } catch (Exception e) {
      logger.error("查询成果查全文表记录时出错！id={}", fullTextId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubFullTextPO pubFullTextPO) throws ServiceException {
    try {
      pubFullTextDAO.save(pubFullTextPO);
    } catch (Exception e) {
      logger.error("保存成果查全文表记录时出错！对象属性为", pubFullTextPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubFullTextPO pubFullTextPO) throws ServiceException {
    try {
      pubFullTextDAO.update(pubFullTextPO);
    } catch (Exception e) {
      logger.error("更新成果查全文表记录时出错！对象属性为", pubFullTextPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubFullTextPO pubFullTextPO) throws ServiceException {
    try {
      pubFullTextDAO.saveOrUpdate(pubFullTextPO);
    } catch (Exception e) {
      logger.error("保存或更新成果查全文表记录时出错！对象属性为", pubFullTextPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      pubFullTextDAO.delete(id);
    } catch (Exception e) {
      logger.error("根据逻辑主键id删除成果查全文表记录时出错！id={}", id);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PubFullTextPO pubFullTextPO) throws ServiceException {
    try {
      pubFullTextDAO.delete(pubFullTextPO);
    } catch (Exception e) {
      logger.error("删除成果全文表记录时出错！对象属性为={}", pubFullTextPO);
      throw new ServiceException(e);
    }

  }

  @Override
  public Integer getFullTextPermission(Long pubId, Long fileId, Long reqPsnId) throws ServiceException {
    PubFullTextPO pubFullTextPO = null;
    try {
      /**
       * SCM-23246,全文请求改为只需要同意一次,后面不再需要请求
       */
      boolean hasAgreeRecord = checkHasAgreeRecordDao.checkHasAgreeRecord(reqPsnId, pubId, PubDbEnum.SNS);
      if (hasAgreeRecord) {
        return 0;
      } else {
        pubFullTextPO = pubFullTextDAO.getPubFullText(pubId, fileId);
        return pubFullTextPO.getPermission();
      }
    } catch (Exception e) {
      logger.error("根据pubId和fileId获取全文成果对象失败！对象属性为={}", pubFullTextPO);
      throw new ServiceException("获取成果全文权限失败");
    }
  }

  @Override
  public void updateFullTextFileDesc(Long pubId, Long fileId, String fileDesc) throws ServiceException {
    PubFullTextPO pubFullTextPO = null;
    try {
      pubFullTextPO = pubFullTextDAO.getPubFullText(pubId, fileId);
      pubFullTextPO.setFileDesc(fileDesc);
      pubFullTextPO.setGmtModified(new Date());
      this.saveOrUpdate(pubFullTextPO);
    } catch (Exception e) {
      logger.error("根据pubId和fileId获取全文成果对象失败！对象属性为={}", pubFullTextPO);
      throw new ServiceException("更新成果全文文件描述失败");
    }
  }

  @Override
  public void updateFullTextPermission(Long pubId, Long fileId, Integer permission) throws ServiceException {
    PubFullTextPO pubFullTextPO = null;
    try {
      pubFullTextPO = pubFullTextDAO.getPubFullText(pubId, fileId);
      pubFullTextPO.setPermission(permission);
      pubFullTextPO.setGmtModified(new Date());
      this.saveOrUpdate(pubFullTextPO);
    } catch (Exception e) {
      logger.error("根据pubId和fileId获取全文成果对象失败！对象属性为={}", pubFullTextPO);
      throw new ServiceException("更新成果全文权限失败");
    }
  }

  @Override
  public List<PubFullTextPO> findPubfulltextList(List<Long> pubIds) throws ServiceException {
    List<PubFullTextPO> list = pubFullTextDAO.getByPubIds(pubIds);
    return list;
  }

  @Override
  public String getSnsFullTextDownloadUrl(Long pubId, boolean isShortUrl) {
    String url = "";
    PubFullTextPO pubFulltext = pubFullTextDAO.getPubFullTextByPubId(pubId);
    Long fileId = pubFulltext == null ? null : pubFulltext.getFileId();
    if (fileId != null) {
      boolean flag = false; // 标志位，是否可下载
      // 取全文权限
      int permission = pubFulltext.getPermission();
      switch (permission) {
        case 0: // 公开 所有人可下载
          flag = true;
          break;
        default: {// 隐私
          Long pubOwnerId = getOwnerPsnId(pubId);
          Long currPsnId = SecurityUtils.getCurrentUserId();
          // 隐私但是当前用户是成果的拥有者
          flag = currPsnId != 0 && currPsnId.equals(pubOwnerId);
          if (flag) {
            break;
          }
          // 判断是否请求全文被同意
          flag = pubFTReqBaseDao.isFullTextReqAgree(pubId, currPsnId, PubDbEnum.SNS);
        }
      }
      if (flag) {
        url = buildPubFullTextUrl(fileId, ServiceConstants.SCHOLAR_NODE_ID_1, isShortUrl, pubId);
      }
    } else {
      logger.debug("获取个人成果全文附件地址失败，无个人成果全文！");
    }
    return url;
  }

  /**
   * 构建个人成果全文下载地址
   *
   * @author houchuanjie
   * @date 2017年12月11日 上午10:29:08
   * @param fileId
   * @param nodeId
   * @param isShortUrl
   * @param pubId
   * @return
   * @throws ServiceException
   */
  private String buildPubFullTextUrl(Long fileId, int nodeId, boolean isShortUrl, Long pubId) throws ServiceException {
    String url = "";
    if (isShortUrl) {
      if (nodeId == ServiceConstants.ROL_NODE_ID) {
        url = domainrol + "/scmwebrol/archiveFiles/fileDownload?fdesId=" + Des3Utils.encodeToDes3("" + fileId);
        // 保存下载记录DownloadCollectStatistics表并发送下载邮件
        saveDownRecordAndSendEmail(pubId);
      } else {
        url = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.SNS_FULLTEXT, fileId);
      }
    } else {
      if (nodeId == ServiceConstants.SCHOLAR_NODE_ID_1) {
        url = fileDownUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, fileId, pubId);
      } else if (nodeId == ServiceConstants.ROL_NODE_ID) {
        url = domainrol + "/scmwebrol/archiveFiles/fileDownload?fdesId=" + Des3Utils.encodeToDes3("" + fileId);
        // 保存下载记录DownloadCollectStatistics表并发送下载邮件
        saveDownRecordAndSendEmail(pubId);
      }
    }
    return url;
  }

  private void saveDownRecordAndSendEmail(Long pubId) {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    Long ownerPsnId = psnPubService.getPubOwnerId(pubId);;// 拥有人
    try {
      if (currentPsnId.equals(ownerPsnId)) {
        logger.warn("自己下载或者收藏自己的东西，不记录！！psnId=" + currentPsnId + ", actionKey=" + pubId + ", actionType=" + 1);
        return;
      }
      Date nowDate = new Date();
      long formateDate = DateUtils.getDateTime(nowDate);
      DownloadCollectStatistics dcs =
          downloadCollectStatisticsDao.findRecord(ownerPsnId, pubId, 1, currentPsnId, formateDate);
      if (dcs == null) {
        dcs = new DownloadCollectStatistics();
        dcs.setActionKey(pubId);
        dcs.setActionType(1);
        dcs.setDcdDate(nowDate);
        dcs.setDcPsnId(currentPsnId);
        dcs.setFormateDate(formateDate);
        dcs.setPsnId(ownerPsnId);
      } else {
        dcs.setDcdDate(nowDate);
        dcs.setFormateDate(formateDate);
      }
      dcs.setDcount(dcs.getDcount() + 1l);// 下载次数
      downloadCollectStatisticsDao.save(dcs);
      // 发送下载邮件
      if (currentPsnId != null && currentPsnId > 0l) {
        this.sendDownloadEmail(currentPsnId, 1, pubId, ownerPsnId);
      }
    } catch (Exception e) {
      logger.error(
          "保存成果全文下载记录出错！PsnId=" + currentPsnId + " dcPsnId=" + ownerPsnId + " actionKey=" + pubId + " actionType=1", e);
    }

  }

  private void sendDownloadEmail(Long downloadPsnId, int resType, Long resId, Long resOwnerPsnId) {
    try {
      if (!downloadPsnId.equals(resOwnerPsnId)) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("downlodedPsnId", downloadPsnId);
        map.put("psnId", resOwnerPsnId);
        map.put("pubId", resId);
        // 需要发送邮件
        downloadYourPubEmailService.sendDownloadFulltextMail(map);
      }
    } catch (Exception e) {
      logger.error("发送下载邮件错误：resType:{},resId:{},downloadPsnId:{}", resType, resId, downloadPsnId, e);
    }
  }

  @Override
  public PubFullTextPO findPubfulltext(Long pubId) throws ServiceException {
    PubFullTextPO pubFullTextPO = pubFullTextDAO.getPubFullTextByPubId(pubId);
    return pubFullTextPO;
  }

  @Override
  public String getPdwhFullTextDownloadUrl(Long pdwhPubId, boolean isShortUrl) {
    String url = "";
    PdwhPubFullTextPO pubfulltext = pdwhPubFullTextService.getPdwhPubfulltext(pdwhPubId);
    if (pubfulltext != null) {
      if (isShortUrl) { // 获取短地址
        url = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.PDWH_FULLTEXT, pubfulltext.getFileId());
      } else {
        url = fileDownUrlService.getDownloadUrl(FileTypeEnum.PDWH_FULLTEXT, pdwhPubId);
      }
    }
    return url;
  }

  @Override
  public String getPdwhFullTextUrl(Long pdwhPubId) {
    String pdwhFulltextUrl = V8pubConst.PUB_DEFAULT_FULLTEXT_IMG_NEW;
    PdwhPubFullTextPO pubfulltext = pdwhPubFullTextService.getPdwhPubfulltext(pdwhPubId);
    if (pubfulltext != null && StringUtils.isNotBlank(pubfulltext.getThumbnailPath())) {
      pdwhFulltextUrl = domainscm + pubfulltext.getThumbnailPath();
    }
    return pdwhFulltextUrl;
  }

  @Override
  public Map<String, String> uploadPubFulltext(Long pubId, Long fileId, Long psnId) throws ServiceException {
    Map<String, String> map = new HashMap<String, String>();
    try {
      PubSnsPO pubSns = pubSnsDAO.get(pubId);
      Long ownerPsnId = getOwnerPsnId(pubId);
      if (pubSns == null || !psnId.equals(ownerPsnId)) {
        map.put("fullTextImagePath", "");
        map.put("downUrl", "");
        return map;
      }
      ArchiveFile archiveFile = archiveFileService.getArchiveFileById(fileId);
      PubFulltextDTO fulltext = new PubFulltextDTO();
      fulltext.setDes3fileId(Des3Utils.encodeToDes3(String.valueOf(fileId)));
      fulltext.setFileName(archiveFile.getFileName());
      Integer pubAuthority = queryPubAuthority(psnId, pubId);
      // 获取成果权限 permission值为0:所有人可下载 1:好友 2:自己
      Integer permission = (pubAuthority != null && pubAuthority.intValue() == 7) ? 0 : 2;
      fulltext.setPermission(permission);
      Integer pubPermission = (pubAuthority == null) ? 7 : pubAuthority;
      savaPubFulltext(psnId, pubId, pubPermission, fulltext);
      PubFullTextPO pubFullText = pubFullTextDAO.getPubFullTextByPubId(pubId);
      String fullTextImagePath = "";
      if (pubFullText != null) {
        fullTextImagePath = pubFullText.getThumbnailPath();
      }
      map.put("fullTextImagePath", fullTextImagePath);
      String link = fileDownUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, pubId);
      map.put("downUrl", link);
    } catch (Exception e) {
      logger.error("上传全文出错,pubId=" + pubId);
    }
    return map;
  }

  private Long getOwnerPsnId(Long pubId) {
    Long ownerPsnId = psnPubService.getPubOwnerId(pubId);
    if (NumberUtils.isNullOrZero(ownerPsnId)) {
      GroupPubPO groupPub = groupPubService.getByPubId(pubId);
      if (groupPub != null) {
        ownerPsnId = groupPub.getOwnerPsnId();
      }
    }
    return ownerPsnId;
  }

  private void savaPubFulltext(Long psnId, Long pubId, Integer permission, PubFulltextDTO fulltext) {
    try {
      Map<String, Object> result = new HashMap<>();
      result.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(psnId)));
      result.put("des3PubId", Des3Utils.encodeToDes3(String.valueOf(pubId)));
      result.put("permission", permission);
      result.put("pubHandlerName", PubHandlerEnum.UPDATE_SNS_FULLTEXT.name);
      result.put("fullText", fulltext);
      RestTemplateUtils.post(restTemplate, SAVEORUPDATE, JacksonUtils.mapToJsonStr(result));
    } catch (Exception e) {
      logger.error("更新全文出错,pubId=" + pubId, e);
    }

  }

  /**
   * 获取成果权限 permission值为0:所有人可下载 1:好友 2:自己
   * 
   * @param psnId
   * @param pubId
   * @return
   */
  public Integer queryPubAuthority(Long psnId, Long pubId) {
    PsnConfig psnConfig = psnConfigDao.getByPsn(psnId);
    if (psnConfig != null) {
      return psnConfigPubDao.getAnyUser(psnConfig.getCnfId(), pubId);
    }
    return null;
  }

  @Override
  public PubFullTextPO getPubFullText(Long pubId, Long fileId) throws ServiceException {
    PubFullTextPO pubFullTextPO = null;
    try {
      pubFullTextPO = pubFullTextDAO.getPubFullText(pubId, fileId);
      return pubFullTextPO;
    } catch (Exception e) {
      logger.error("根据pubId和fileId获取全文成果对象失败！对象属性为={}", pubFullTextPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long getMaxFulltextId(Long pubId) throws ServiceException {
    try {
      return pubFullTextDAO.getMaxFulltextId(pubId);
    } catch (Exception e) {
      logger.error("根据pubId获取最大的全文成果全文id失败！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteByPubId(Long pubId) throws ServiceException {
    try {
      pubFullTextDAO.deleteByPubId(pubId);
    } catch (Exception e) {
      logger.error("根据pubId删除全文失败！pubId={}", pubId);
      throw new ServiceException(e);
    }

  }

  @Override
  public String getSnsFullTextDownloadUrl(Long pubId, Long currentPsnId, boolean isShortUrl) {
    String url = "";
    PubFullTextPO pubFulltext = pubFullTextDAO.getPubFullTextByPubId(pubId);
    Long fileId = pubFulltext == null ? null : pubFulltext.getFileId();
    if (fileId != null) {
      boolean flag = false; // 标志位，是否可下载
      // 取全文权限
      int permission = pubFulltext.getPermission();
      switch (permission) {
        case 0: // 公开 所有人可下载
          flag = true;
          break;
        default: {// 隐私
          Long pubOwnerId = psnPubService.getPubOwnerId(pubId);
          // 隐私但是当前用户是成果的拥有者
          flag = !NumberUtils.isNullOrZero(currentPsnId) && currentPsnId.equals(pubOwnerId);
          if (flag) {
            break;
          }
          // 判断是否请求全文被同意
          flag = pubFTReqBaseDao.isFullTextReqAgree(pubId, currentPsnId, PubDbEnum.SNS);
        }
      }
      if (flag) {
        url = buildPubFullTextUrl(fileId, ServiceConstants.SCHOLAR_NODE_ID_1, isShortUrl, pubId);
      }
    } else {
      logger.debug("获取个人成果全文附件地址失败，无个人成果全文！");
    }
    return url;
  }
}
