package com.smate.web.file.service.download;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.statistics.dao.DownloadCollectStatisticsDao;
import com.smate.core.base.statistics.model.DownloadCollectStatistics;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.file.consts.FileConsts;
import com.smate.web.file.dao.PubFullTextDAO;
import com.smate.web.file.dao.PubSnsDAO;
import com.smate.web.file.dao.ShareStatisticsQueryDao;
import com.smate.web.file.dao.fulltext.PdwhPubFullTextDAO;
import com.smate.web.file.dao.fulltext.PubFullTextReqQueryDao;
import com.smate.web.file.exception.FileDownloadNoPermissionException;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.FileDownloadForm;
import com.smate.web.file.model.fulltext.pdwh.PdwhPubFullTextPO;
import com.smate.web.file.utils.FileNamePathParseUtil;
import com.smate.web.pub.service.email.DownloadYourPubEmailService;

/**
 * 
 * 个人成果全文下载
 * 
 * @author tsz
 *
 */
@Transactional(rollbackOn = Exception.class)
public class BuildPubFulltextFileResServiceImpl extends BuildResServiceBase {

  @Autowired
  private DownloadCollectStatisticsDao downloadCollectStatisticsDao;
  @Autowired
  private PubFullTextReqQueryDao pubFTReqQueryDao;
  @Autowired
  private DownloadYourPubEmailService downloadYourPubEmailService;
  @Autowired
  private ShareStatisticsQueryDao shareStatisticsQueryDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private PubFullTextDAO pubFullTextDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public void check(FileDownloadForm form) throws Exception {

    String SERVER_URL = domainscm + FileConsts.QUERY_PUB_URL;
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPubId(form.getPubId());
    pubQueryDTO.setServiceType(FileConsts.QUERY_PUB_FULLTEXT);
    Boolean hasPubFulltext = false;
    Integer permission = 0;
    Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
    if (result.get("status").equals("success")) {
      List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
      if (resultList != null && resultList.size() > 0) {
        Object fullTextFieId = resultList.get(0).get("fullTextFieId");
        Object fullTextPermission = resultList.get(0).get("fullTextPermission");
        if (fullTextFieId != null) {
          // 防止文件id为空
          form.setFileId(NumberUtils.toLong(fullTextFieId.toString()));
          hasPubFulltext = true;
        }
        permission = NumberUtils.toInt(ObjectUtils.toString(fullTextPermission), 0);
      }
    }

    // 传过来的是成果id
    if (!hasPubFulltext && !form.isShortUrl()) {
      form.setResult(false);
      form.setResultMsg("文件不存在！");
      throw new FileNotExistException();
    }
    super.checkArchiveFile(form.getFileId(), form);

    // 不是短地址
    if (!form.isShortUrl()) {
      // 取全文权限
      switch (permission) {
        case 0: // 公开 所有人可下载
          break;
        default:// 隐私
        {
          pubQueryDTO.setServiceType(FileConsts.QUERY_PUB);
          result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
          Long pubOwnerId = null;// 拥有人
          if (result.get("status").equals("success")) {
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
            if (resultList != null && resultList.size() > 0) {
              Object ownerPsnIdObj = resultList.get(0).get("ownerPsnId");
              pubOwnerId = Long.parseLong(ObjectUtils.toString(ownerPsnIdObj));

            }
          }
          Long currPsnId = SecurityUtils.getCurrentUserId();
          // 隐私但是当前用户是成果的拥有者
          boolean flag = currPsnId != 0 && currPsnId.equals(pubOwnerId);
          if (flag)
            break;
          // 判断是否请求全文被同意
          flag = pubFTReqQueryDao.isFullTextReqAgree(form.getPubId(), currPsnId, PubDbEnum.SNS);
          if (flag)
            break;
          // 判断该成果是否是成果所有者分享给当前用户，如果是成果所有者分享给当前用户，则有权限下载该成果全文
          flag = shareStatisticsQueryDao.isPubBeingShared(currPsnId, pubOwnerId, form.getPubId());
          if (flag)
            break;
          form.setResult(false);
          form.setResultMsg("没有下载该文件的权限！");
          throw new FileDownloadNoPermissionException();
        }

      }
    }
  }

  @Override
  public void buildResUrl(FileDownloadForm form) throws Exception {
    // 构建文件真实地址
    // 构建下载资源
    String url = "/" + basicPath + FileNamePathParseUtil.parseFileNameDir(form.getArchiveFile().getFilePath());
    form.setResPath(url);
  }

  @Override
  public void extend(FileDownloadForm form) throws Exception {
    String SERVER_URL = domainscm + FileConsts.QUERY_PUB_URL;
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPubId(form.getPubId());
    pubQueryDTO.setServiceType(FileConsts.QUERY_PUB);
    Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
    Long ownerPsnId = null;// 拥有人
    if (result.get("status").equals("success")) {
      List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
      if (resultList != null && resultList.size() > 0) {
        Object ownerPsnIdObj = resultList.get(0).get("ownerPsnId");
        ownerPsnId = Long.parseLong(ObjectUtils.toString(ownerPsnIdObj));
      }
    }

    form.setOwnerPsnId(ownerPsnId);
    try {
      Date nowDate = new Date();
      long formateDate = DateUtils.getDateTime(nowDate);
      Long currentPsnId = form.getCurrentPsnId();
      int status = snsDownloadStatistics(form, ownerPsnId, nowDate, formateDate, form.getPubId());
      if (status == 0) {
        return;
      }
      // SCM-23563 个人库成果先直接操作，如果是关联成果，再来同步数据
      Long pdwhPubId = pubPdwhSnsRelationDAO.getPdwhPubIdBySnsPubId(form.getPubId());
      if (pdwhPubId != null && pdwhPubId > 0L) {
        PdwhPubFullTextPO fulltext = pdwhPubFullTextDAO.getByPubId(pdwhPubId);
        if (fulltext != null) {
          pdwhDownloadStatistics(fulltext.getFileId(), nowDate, formateDate, currentPsnId);// 基准库数据同步
        }
        List<Long> allSnsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
        List<Long> snsPubIds = pubFullTextDAO.getPubFulltextIdsList(allSnsPubIds);
        if (CollectionUtils.isNotEmpty(snsPubIds)) {
          for (Long snsPubId : snsPubIds) {
            if (!snsPubId.equals(form.getPubId())) {
              Long ownerId = pubSnsDAO.get(snsPubId).getCreatePsnId();
              snsRelationDownloadStatistics(form, ownerId, nowDate, formateDate, snsPubId);// 个人库数据同步
            }
          }
        }
      }

      // 发送下载邮件
      if (form.getCurrentPsnId() != null && form.getCurrentPsnId() > 0l) {
        if (pdwhPubId != null && pdwhPubId > 0) {
          sendDownEmail(form, pdwhPubId);
        } else {
          this.sendDownloadEmail(form.getCurrentPsnId(), 1, form.getPubId(), ownerPsnId);
        }
      }
    } catch (Exception e) {
      logger.error("JMS保存下载或收藏记录出错！PsnId=" + form.getCurrentPsnId() + " dcPsnId=" + ownerPsnId + " actionKey="
          + form.getPubId() + " actionType=1", e);
    }
  }

  public void sendDownEmail(FileDownloadForm form, Long pdwhPubId) {
    List<Long> allSnsPubIds = pubPdwhSnsRelationDAO.getSnsOpenPubIds(pdwhPubId, 0L);
    List<Long> snsPubIds = pubFullTextDAO.getPubFulltextIdsList(allSnsPubIds);
    if (CollectionUtils.isNotEmpty(snsPubIds)) {
      List<Long> pubOwnerList = pubSnsDAO.getSnsPubList(snsPubIds);
      List<Long> newPubOwnerList = pubOwnerList.stream().distinct().collect(Collectors.toList());
      List<Map<Long, Long>> pubPsbList = pubSnsDAO.getSnsPub(snsPubIds);
      if (CollectionUtils.isNotEmpty(newPubOwnerList)) {
        for (Long ownerId : newPubOwnerList) {// 操作要通知所有关联成果的所属用户
          if (pubPsbList != null && pubPsbList.size() > 0) {
            Long newPubId = null;
            for (Map<Long, Long> map : pubPsbList) {
              if (ownerId.equals(map.get("createPsnId"))) {
                newPubId = map.get("pubId");
              }
            }
            this.sendDownloadEmail(form.getCurrentPsnId(), 1, newPubId, ownerId);
          }
        }
      }
    }
  }

  public int snsDownloadStatistics(FileDownloadForm form, Long ownerPsnId, Date nowDate, long formateDate, Long pubId)
      throws Exception {
    if (form.getCurrentPsnId().equals(ownerPsnId)) {
      logger.warn("自己下载或者收藏自己的东西，不记录！！psnId=" + form.getCurrentPsnId() + ", actionKey=" + form.getFileId()
          + ", actionType=" + 1);
      return 0;
    }
    DownloadCollectStatistics dcs =
        downloadCollectStatisticsDao.findRecord(ownerPsnId, pubId, 1, form.getCurrentPsnId(), formateDate);
    if (dcs == null) {
      dcs = new DownloadCollectStatistics();
      dcs.setActionKey(pubId);
      dcs.setActionType(1);
      dcs.setDcdDate(nowDate);
      dcs.setDcPsnId(form.getCurrentPsnId());
      dcs.setFormateDate(formateDate);
      dcs.setPsnId(ownerPsnId);
      dcs.setIp(Struts2Utils.getRemoteAddr());
    } else {
      dcs.setDcdDate(nowDate);
      dcs.setFormateDate(formateDate);
    }
    dcs.setDcount(dcs.getDcount() + 1l);// 下载次数
    downloadCollectStatisticsDao.save(dcs);
    return 1;
  }

  public void snsRelationDownloadStatistics(FileDownloadForm form, Long ownerPsnId, Date nowDate, long formateDate,
      Long pubId) throws Exception {
    if (!form.getCurrentPsnId().equals(ownerPsnId)) {
      DownloadCollectStatistics dcs =
          downloadCollectStatisticsDao.findRecord(ownerPsnId, pubId, 1, form.getCurrentPsnId(), formateDate);
      if (dcs == null) {
        dcs = new DownloadCollectStatistics();
        dcs.setActionKey(pubId);
        dcs.setActionType(1);
        dcs.setDcdDate(nowDate);
        dcs.setDcPsnId(form.getCurrentPsnId());
        dcs.setFormateDate(formateDate);
        dcs.setPsnId(ownerPsnId);
        dcs.setIp(Struts2Utils.getRemoteAddr());
      } else {
        dcs.setDcdDate(nowDate);
        dcs.setFormateDate(formateDate);
      }
      dcs.setDcount(dcs.getDcount() + 1l);// 下载次数
      downloadCollectStatisticsDao.save(dcs);
    }
  }

  public void pdwhDownloadStatistics(Long fileId, Date nowDate, long formateDate, Long currentPsnId) throws Exception {
    DownloadCollectStatistics dcspdwh =
        downloadCollectStatisticsDao.findRecord(0L, fileId, 1, currentPsnId, formateDate);
    if (dcspdwh == null) {
      dcspdwh = new DownloadCollectStatistics();
      dcspdwh.setActionKey(fileId);
      dcspdwh.setActionType(1);
      dcspdwh.setDcdDate(nowDate);
      dcspdwh.setDcPsnId(currentPsnId);
      dcspdwh.setFormateDate(formateDate);
      dcspdwh.setPsnId(0L);
      dcspdwh.setIp(Struts2Utils.getRemoteAddr());

    } else {
      dcspdwh.setDcdDate(nowDate);
      dcspdwh.setFormateDate(formateDate);
    }
    dcspdwh.setDcount(dcspdwh.getDcount() + 1l);// 下载次数
    downloadCollectStatisticsDao.save(dcspdwh);
  }

  private void sendDownloadEmail(Long downloadPsnId, int resType, Long resId, Long resOwnerPsnId) {
    try {
      if (!downloadPsnId.equals(resOwnerPsnId)) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("downlodedPsnId", downloadPsnId);
        map.put("psnId", resOwnerPsnId);
        map.put("pubId", resId);
        // TODO 需要发送邮件
        downloadYourPubEmailService.sendDownloadFulltextMail(map);
      }
    } catch (Exception e) {
      logger.error("发送下载邮件错误：resType:{},resId:{},downloadPsnId:{}", resType, resId, downloadPsnId, e);
    }
  }
}
